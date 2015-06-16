package com.zhou.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhou.bean.MainPart;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;

/**
 * 对faq中的问题提取主谓宾
 * 
 * @author hankcs
 */
public class MainPartExtractor {

	private static final Logger LOG = LoggerFactory
			.getLogger(MainPartExtractor.class);
	private static LexicalizedParser lp;
	private static GrammaticalStructureFactory gsf;
	static {
		// 模型
		String models = Constants.model;
		LOG.info("载入文法模型：" + models);
		lp = LexicalizedParser.loadModel(models);
		// 汉语
		TreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
		gsf = tlp.grammaticalStructureFactory();
	}

	/**
	 * 获取句子的主谓宾
	 * 
	 * @param sentence
	 *            问题
	 * @return 问题结构
	 */
	private static MainPart getMainPart(String sentence) {
		// 去掉不可见字符
		sentence = sentence.replace("\\s+", "");
		// 分词，用空格隔开
		List<Word> wordList = seg(sentence);
		return getMainPart(wordList);
	}

	/**
	 * 获取句子的主谓宾
	 * 
	 * @param words
	 *            HashWord列表
	 * @return 问题结构
	 */
	private static MainPart getMainPart(List<Word> words) {
		MainPart mainPart = new MainPart();
		if (words == null || words.size() == 0)
			return mainPart;
		Tree tree = lp.apply(words);
		LOG.info("句法树:{}", tree.pennString());
		// 根据整个句子的语法类型来采用不同的策略提取主干
		switch (tree.firstChild().label().toString()) {
		case "NP":
			// 名词短语，认为只有主语，将所有短NP拼起来作为主语即可
			mainPart = getNPPhraseMainPart(tree);
			break;
		default:
			GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
			Collection<TypedDependency> tdls = gs
					.typedDependenciesCCprocessed(true);
			LOG.info("依存关系：{}", tdls);
			TreeGraphNode rootNode = getRootNode(tdls);
			if (rootNode == null) {
				return getNPPhraseMainPart(tree);
			}
			LOG.info("中心词语：", rootNode);
			mainPart = new MainPart(rootNode);
			for (TypedDependency td : tdls) {
				// 依存关系的出发节点，依存关系，以及结束节点
				TreeGraphNode gov = td.gov();
				GrammaticalRelation reln = td.reln();
				String shortName = reln.getShortName();
				TreeGraphNode dep = td.dep();
				if (gov == rootNode) {
					switch (shortName) {
					case "nsubjpass":
					case "dobj":
					case "attr":
						mainPart.object = dep;
						break;
					case "nsubj":
					case "top":
						mainPart.subject = dep;
						break;
					}
				}
				if (mainPart.object != null && mainPart.subject != null) {
					break;
				}
			}
			// 尝试合并主语和谓语中的名词性短语
			combineNN(tdls, mainPart.subject);
			combineNN(tdls, mainPart.object);
			if (!mainPart.isDone())
				mainPart.done();
		}

		return mainPart;
	}

	private static MainPart getNPPhraseMainPart(Tree tree) {
		MainPart mainPart = new MainPart();
		StringBuilder sbResult = new StringBuilder();
		List<String> phraseList = getPhraseList("NP", tree);
		for (String phrase : phraseList) {
			sbResult.append(phrase);
		}
		mainPart.result = sbResult.toString();
		return mainPart;
	}

	/**
	 * 从句子中提取最小粒度的短语
	 * 
	 * @param type
	 * @param sentence
	 * @return
	 */
	private static List<String> getPhraseList(String type, String sentence) {
		return getPhraseList(type, lp.apply(seg(sentence)));
	}

	private static List<String> getPhraseList(String type, Tree tree) {
		List<String> phraseList = new LinkedList<String>();
		for (Tree subtree : tree) {
			if (subtree.isPrePreTerminal()
					&& subtree.label().value().equals(type)) {
				StringBuilder sbResult = new StringBuilder();
				for (Tree leaf : subtree.getLeaves()) {
					sbResult.append(leaf.value());
				}
				phraseList.add(sbResult.toString());
			}
		}
		return phraseList;
	}

	/**
	 * 合并名词性短语为一个节点
	 * 
	 * @param tdls
	 *            依存关系集合
	 * @param target
	 *            目标节点
	 */
	private static void combineNN(Collection<TypedDependency> tdls,
			TreeGraphNode target) {
		if (target == null)
			return;
		for (TypedDependency td : tdls) {
			// 依存关系的出发节点，依存关系，以及结束节点
			TreeGraphNode gov = td.gov();
			GrammaticalRelation reln = td.reln();
			String shortName = reln.getShortName();
			TreeGraphNode dep = td.dep();
			if (gov == target) {
				switch (shortName) {
				case "nn":
					target.setValue(dep.toString("value") + target.value());
					return;
				}
			}
		}
	}

	private static TreeGraphNode getRootNode(Collection<TypedDependency> tdls) {
		for (TypedDependency td : tdls) {
			if (td.reln() == GrammaticalRelation.ROOT) {
				return td.dep();
			}
		}

		return null;
	}

	/**
	 * 分词
	 * 
	 * @param sentence
	 *            句子
	 * @return 分词结果
	 */
	private static List<Word> seg(String sentence) {
		// 分词
		LOG.info("正在对短句进行分词：" + sentence);
		List<Word> wordList = new LinkedList<>();
		List<Term> terms = ToAnalysis.parse(sentence);
		StringBuffer sbLogInfo = new StringBuffer();
		for (Term term : terms) {
			Word word = new Word(term.getName());
			wordList.add(word);
			sbLogInfo.append(word);
			sbLogInfo.append(' ');
		}
		LOG.info("分词结果为：" + sbLogInfo);
		return wordList;
	}

	private static MainPart getMainPart(String sentence, String delimiter) {
		List<Word> wordList = new LinkedList<>();
		for (String word : sentence.split(delimiter)) {
			wordList.add(new Word(word));
		}
		return getMainPart(wordList);
	}

	/**
	 * 遍历提取所有主谓宾
	 * 
	 * @throws IOException
	 */
	private static void iteraExtra() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(new File(
				Constants.question_path)));
		ArrayList<String> all = new ArrayList<String>();
		String line = "";
		while ((line = (br.readLine())) != null) {
			all.add(line);
		}

		String[] testCaseArray = (String[]) all.toArray(new String[all.size()]);

		FileWriter fw = new FileWriter(new File(Constants.extra_all_path));
		for (String testCase : testCaseArray) {
			MainPart mp = MainPartExtractor.getMainPart(testCase);
			System.out.printf("%s\t%s\n", testCase, mp);
			System.out.println(String.format("主语是 %s ，谓语是%s 。 宾语是%s",
					mp.getSubject(), mp.getPredicate(), mp.getObject()));

			fw.write(String.format("%s_%s_%s\n", mp.getSubject(),
					mp.getPredicate(), mp.getObject()));
			fw.flush();

		}
		fw.close();

	}

	/**
	 * 获得主谓宾
	 * @param testCase
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> iteraOneString(String testCase)
			throws IOException {

		MainPart mp = MainPartExtractor.getMainPart(testCase);

		ArrayList<String> all = new ArrayList<String>();
		all.add(mp.getSubject());
		all.add(mp.getPredicate());
		all.add(mp.getObject());
		System.out.println("主谓宾分析结果------->"+all);
		return all;

	}

	/**
	 * 调用演示
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		iteraExtra();
//		System.out.println(iteraOneString("单独作为一个月工资、薪金所得计算纳税"));

	}
}