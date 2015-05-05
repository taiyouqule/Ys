package com.shenji.search.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.ibm.icu.text.SimpleDateFormat;
import com.shenji.common.exception.ConnectionPoolException;
import com.shenji.common.log.Log;
import com.shenji.common.util.StringMatching;
import com.shenji.search.Configuration;
import com.shenji.search.IEnumSearch;
import com.shenji.search.IEnumSearch.SearchRelationType;
import com.shenji.search.Parameters;
import com.shenji.search.bean.SearchBean;
import com.shenji.search.dic.BusinessDic;
import com.shenji.search.dic.CommonSynonymDic;
import com.shenji.search.dic.CustomSynonymDic;
import com.shenji.search.dic.CustomWordDic;
import com.shenji.search.engine.CustomWordEngine;
import com.shenji.search.engine.SynonymEngine;
import com.shenji.search.exception.EngineException;
import com.shenji.search.exception.SearchProcessException;
import com.shenji.web.bean.EasyItemBean;
import com.shenji.web.bean.YsItemBean;
import com.zlj.db.Paper;
import com.zlj.db.PaperDB;

/**
 * 布尔查询
 * 
 * @author sj
 * 
 */
public class BooleanSearch {
	// private Searcher searcher = null;
	private Set<String> matchList = null;
	private Set<String> maxMatchSet = null;
	private String[] simWords = null;
	private SynonymEngine engine_Custom = null;
	private SynonymEngine engine_Common = null;
	private String from;
	private IEnumSearch.SearchRelationType rType;
	private String args = null;

	public BooleanSearch(String args, Set<String> matchList,
			Set<String> maxMatchSet, IEnumSearch.SearchRelationType rType) {
		if (rType == SearchRelationType.AND_SEARCH) {
			/*
			 * List<String> temp = new ArrayList<String>(); for (String s :
			 * maxMatchSet) temp.add(s);
			 */
			this.matchList = maxMatchSet;
			// temp.clear();
		}
		if (rType == SearchRelationType.OR_SEARCH)
			this.matchList = matchList;
		this.args = args;
		this.rType = rType;
		// this.maxTextShow = maxTextShow;
		this.maxMatchSet = maxMatchSet;
		this.simWords = (String[]) maxMatchSet.toArray(new String[maxMatchSet
				.size()]);
		try {
			// 我的同义词引擎
			this.engine_Custom = new CustomSynonymDic();
			// 哈工大词林同义词引擎
			this.engine_Common = new CommonSynonymDic();
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			Log.getLogger(this.getClass()).error(e.getMessage(), e);
		}
	}

	public List<SearchBean> getResult(String args, String from)
			throws Exception {
		try {
			Map<Document, Float> map = search(from);
			List<SearchBean> result = addHits2List(map);
			return result;
		} finally {
			// 关闭两类同义词引擎
			if (engine_Custom != null)
				engine_Custom.close();
			if (engine_Common != null)
				engine_Common.close();
		}
	}

	public List<YsItemBean> getJsonResult(String args, String from)
			throws Exception {
		try {
			Map<Document, Float> map = search(from);
			List<YsItemBean> result = getRecordJsonList(map);
			return result;
		} finally {
			// 关闭两类同义词引擎
			if (engine_Custom != null)
				engine_Custom.close();
			if (engine_Common != null)
				engine_Common.close();
		}
	}

	public List<EasyItemBean> getEasyJsonResult(String args, String from)
			throws Exception {
		try {
			Map<Document, Float> map = easySearch(from);
			List<EasyItemBean> result = getRecordEasyJsonList(map);
			return result;
		} finally {
			// 关闭两类同义词引擎
			if (engine_Custom != null)
				engine_Custom.close();
			if (engine_Common != null)
				engine_Common.close();
		}
	}

	/**
	 * 关键词、同义词标记
	 * 
	 * @param str
	 *            HTML文本
	 * @return HTML文本
	 * @throws SearchProcessException
	 */
	private String markContent(String str) {
		for (String s : matchList) { // if()
			String[] strings = null;
			try {
				str = str.replace(s, "<em>" + s + "</em>");
				if (engine_Custom != null)
					if ((strings = engine_Custom.getSynonyms(s)) != null) {
						for (int i = 0; i < strings.length; i++) {
							str = str.replace(strings[i], "<em>" + strings[i]
									+ "</em>");
						}
					}
				if (engine_Common != null) {
					if ((strings = engine_Common.getSynonyms(s)) != null) {
						for (int i = 0; i < strings.length; i++) {
							str = str.replace(strings[i], "<b>" + strings[i]
									+ "</b>");
						}
					}
				}
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				// 同义词标记失败，这个异常不应该抛出去
				Log.getLogger(this.getClass()).error(e.getMessage(), e);
			}
		}
		return str;
	}

	private List<SearchBean> addHits2List(Map<Document, Float> map) {
		List<SearchBean> tmplistBean = new ArrayList<SearchBean>();// 构造结果集
		// Map<String, String> contentNameMap = new HashMap<String, String>();
		Iterator<Map.Entry<Document, Float>> iterator = map.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Document, Float> entry = iterator.next();
			Document doc = entry.getKey();
			float score = entry.getValue();
			SearchBean bean = new SearchBean();
			// String content = doc.get("answer");
			String content = "";
			String dealContent = content;
			bean.setPureContent(dealContent);
			// 显示最大文本
			content = content
					.substring(
							0,
							content.length() > Parameters.maxTestShow ? Parameters.maxTestShow
									: content.length());
			String question = doc.get("question");
			String answer = doc.get("answer");
			bean.setQuestion(question);
			bean.setAnswer(answer);
			bean.setLine(doc.get("line"));
			bean.setPaperid(doc.get("paperid"));
			String path = doc.get("path");
			// 获得htm文件名,不包括后缀名
			String fileName = path.split("[/.]")[1];
			path = Configuration.webPath + "/" + path;
			bean.setFaqId(fileName);
			// 构造FAQ内容
			// if (from == Configuration.searchDir[0]) {
			if (true) {
				content = "<div class=\"q\">" + question
						+ "</div><div class=\"a\">" + "<font size=\"2\">"
						+ answer + "</font><br/>" + "</div>";
				content = markContent(content);
				// content = "<a href=\"" + path + "\">" + content + "</a>";
				try {
					content += new PaperDB().getPaperMsg(Integer.parseInt(bean
							.getPaperid()));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConnectionPoolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				content += "<a href=\""
						+ "ShowTextServlet?paperid="
						+ bean.getPaperid()
						+ "&line="
						+ bean.getLine()
						+ "\">"
						+ "点击此处预览"
						+ "</a>"
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
				content += "<a href=\"" + "DownloadPdfServlet?paperid="
						+ bean.getPaperid() + "\">" + "点击此处下载" + "</a>";
				bean.setContent(content);
			}// 可以不看
			else {
				content = markContent(content);
				content = "<a href=\"" + path + "\">" + content + "</a>";
				bean.setContent(content);
			}

			// 固有相似度
			double similarity = StringMatching.getInherentSimilarity(simWords,
					dealContent);
			bean.setSimilarity(similarity);
			bean.setScore(score);
			tmplistBean.add(bean);
			// contentNameMap.put(content, fileName);
		}
		return tmplistBean;

	}

	private List<YsItemBean> getRecordJsonList(Map<Document, Float> map) {
		List<YsItemBean> list = new ArrayList<YsItemBean>();
		// Map<String, String> contentNameMap = new HashMap<String, String>();
		Iterator<Map.Entry<Document, Float>> iterator = map.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Document, Float> entry = iterator.next();
			Document doc = entry.getKey();
			float score = entry.getValue();
			SearchBean bean = new SearchBean();
			YsItemBean item = new YsItemBean();
			// String content = doc.get("answer");
			String content = "";
			String dealContent = content;
			bean.setPureContent(dealContent);
			// 显示最大文本
			content = content
					.substring(
							0,
							content.length() > Parameters.maxTestShow ? Parameters.maxTestShow
									: content.length());
			String question = doc.get("question");
			String answer = doc.get("answer");
			bean.setQuestion(question);
			bean.setAnswer(answer);
			bean.setLine(doc.get("line"));
			bean.setPaperid(doc.get("paperid"));
			String path = doc.get("path");
			// 获得htm文件名,不包括后缀名
			String fileName = path.split("[/.]")[1];
			path = Configuration.webPath + "/" + path;
			bean.setFaqId(fileName);
			// 构造FAQ内容
			// if (from == Configuration.searchDir[0]) {
			if (true) {
				// content = "<div class=\"q\">" + question
				// + "</div><div class=\"a\">" + "<font size=\"2\">"
				// + answer + "</font><br/>"+"</div>";
				// content = markContent(content);

				item.setTitle(markContent("<div class=\"q\">" + question
						+ "</div>"));
				item.setContent(markContent("<div class=\"a\">"
						+ "<font size=\"2\">" + answer + "</font><br/>"
						+ "</div>"));
				// content = "<a href=\"" + path + "\">" + content + "</a>";
				try {
					// content+=new
					// PaperDB().getPaperMsg(Integer.parseInt(bean.getPaperid()));
					Paper paper = new PaperDB().getPaperJsonMsg(Integer
							.parseInt(bean.getPaperid()));
					item.setTag_from(paper.getWhere_from());
					item.setTag_title(paper.getTitle());
					item.setTag_write_time(new SimpleDateFormat("yyyy-MM-dd")
							.format(new Date(paper.getWrite_time().getTime())));
					item.setTag_writer(paper.getWriter());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConnectionPoolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				item.setView("<a href=\"" + "ShowTextServlet?paperid="
						+ bean.getPaperid() + "&line=" + bean.getLine() + "\">"
						+ "点击此处预览" + "</a>");
				item.setDownload("<a href=\"" + "DownloadPdfServlet?paperid="
						+ bean.getPaperid() + "\">" + "点击此处下载" + "</a>");
				bean.setContent(content);
			}
			// 固有相似度
			double similarity = StringMatching.getInherentSimilarity(simWords,
					dealContent);
			item.setSimilarity(similarity);
			item.setScore(score);
			list.add(item);
		}
		return list;

	}

	private List<EasyItemBean> getRecordEasyJsonList(Map<Document, Float> map) {
		List<EasyItemBean> list = new ArrayList<EasyItemBean>();
		// Map<String, String> contentNameMap = new HashMap<String, String>();
		Iterator<Map.Entry<Document, Float>> iterator = map.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Document, Float> entry = iterator.next();
			Document doc = entry.getKey();
			float score = entry.getValue();
			SearchBean bean = new SearchBean();
			EasyItemBean item = new EasyItemBean();
			// String content = doc.get("answer");
			String content = "";
			String dealContent = content;
			bean.setPureContent(dealContent);
			// 显示最大文本
			content = content
					.substring(
							0,
							content.length() > Parameters.maxTestShow ? Parameters.maxTestShow
									: content.length());
			String question = doc.get("question");
			String answer = doc.get("answer");
			bean.setQuestion(question);
			bean.setAnswer(answer);
			String path = doc.get("path");
			// 获得htm文件名,不包括后缀名
			String fileName = path.split("[/.]")[1];
			path = Configuration.webPath + "/" + path;
			bean.setFaqId(fileName);
			// 构造FAQ内容
			// if (from == Configuration.searchDir[0]) {
			if (true) {
				// content = "<div class=\"q\">" + question
				// + "</div><div class=\"a\">" + "<font size=\"2\">"
				// + answer + "</font><br/>"+"</div>";
				// content = markContent(content);

				item.setTitle(markContent("<div class=\"q\">" + question
						+ "</div>"));
				item.setContent(markContent("<div class=\"a\">"
						+ "<font size=\"2\">" + answer + "</font><br/>"
						+ "</div>"));
				// content = "<a href=\"" + path + "\">" + content + "</a>";
				item.setPath(path);
				bean.setContent(content);
			}
			// 固有相似度
			double similarity = StringMatching.getInherentSimilarity(simWords,
					dealContent);
			item.setSimilarity(similarity);
			item.setScore(score);
			list.add(item);
		}
		return list;

	}

	private float getCustomWeight(String args) throws EngineException {
		float weight = 1;
		// 业务词典设置权重
		float weight_bussiness = 1;
		try {
			weight_bussiness = BusinessDic.getInstance().getWeight(args);
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			Log.getLogger(this.getClass()).error(e.getMessage(), e);
		}
		if (weight_bussiness != -1) {
			weight = weight * weight_bussiness;
		}
		// 最长匹配词典设置权重
		boolean isPutIn = maxMatchSet.add(args);
		if (isPutIn)
			maxMatchSet.remove(args);
		else
			weight = weight * Parameters.maxMatchWeight;
		CustomWordEngine engine = CustomWordDic.getInstance();
		// 自建词典设置权重
		if (engine.isCustomWord(args))
			weight = weight * Parameters.myIkdictWeight * args.length();
		return weight;

	}

	/**
	 * @param from
	 * @return
	 * @throws Exception
	 */
	private Map<Document, Float> search(String from) throws Exception {
		// 布尔查询向量
		String[] filedValues = (String[]) matchList
				.toArray(new String[matchList.size()]);
		// 查询字段（域）
		String filedKey = "content";
		String QfiledKey = "question";
		String AfiledKey = "answer";
		// ArrayList<Document> arrayList = null;
		IndexReader reader = null;
		Directory dir = null;
		Searcher searcher = null;

		File file = new File(from);
		// 判断索引目录存在长度不为0
		if (file.isDirectory() && file.listFiles().length == 0) {
			throw new SearchProcessException("Lucene Index is Null!",
					SearchProcessException.ErrorCode.LuceneFileError);
		}
		// 打开索引目录
		try {
			dir = FSDirectory.open(file);
			reader = IndexReader.open(dir);
		} catch (Exception ex) {
			try {
				if (reader != null)
					reader.close();
				if (dir != null)
					dir.close();
			} catch (IOException e) {
				Log.getLogger(this.getClass()).error(e.getMessage(), e);
			}
			throw new SearchProcessException("Lucene Index Open failed!", ex,
					SearchProcessException.ErrorCode.LuceneFileError);
		}
		try {
			searcher = new IndexSearcher(reader);
			BooleanQuery booleanQuery = new BooleanQuery();
			for (int i = 0; i < filedValues.length; i++) {
				float weight = 1;
				weight = this.getCustomWeight(filedValues[i]);
				// FAQ查询
				if (from == Configuration.searchDir[0]) {
					Term term_q = null;
					term_q = new Term(QfiledKey, filedValues[i]);
					Query query_q = new TermQuery(term_q);
					query_q.setBoost(weight);

					Term term_a = new Term(AfiledKey, filedValues[i]);
					Query query_a = new TermQuery(term_a);
					query_a.setBoost(weight / Parameters.qaProportion);
					// System.err.println(filedValues[i]+":"+query.getBoost());
					if (rType == IEnumSearch.SearchRelationType.AND_SEARCH) {
						Occur occur = BooleanClause.Occur.MUST;
						booleanQuery.add(query_q, occur);
					} else if (rType == IEnumSearch.SearchRelationType.OR_SEARCH) {
						Occur occur = BooleanClause.Occur.SHOULD;
						booleanQuery.add(query_q, occur);
						booleanQuery.add(query_a, occur);
					}
				}
				// 其他查询，可以不看
				else {
					Term term = null;
					term = new Term(filedKey, filedValues[i]);
					Query query = new TermQuery(term);
					query.setBoost(weight);
					if (rType == IEnumSearch.SearchRelationType.AND_SEARCH) {
						Occur occur = BooleanClause.Occur.MUST;
						booleanQuery.add(query, occur);
					} else if (rType == IEnumSearch.SearchRelationType.OR_SEARCH) {
						Occur occur = BooleanClause.Occur.SHOULD;
						booleanQuery.add(query, occur);
					}
				}
			}
			TopDocs topDocs = searcher.search(booleanQuery,
					Parameters.maxResult);
			ScoreDoc[] docs = topDocs.scoreDocs;
			// 构造查询结果集
			Map<Document, Float> map = new LinkedHashMap<Document, Float>();
			for (ScoreDoc doc : docs) {
				map.put(searcher.doc(doc.doc), doc.score);
			}
			System.out.println("共找到：" + topDocs.totalHits);
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new SearchProcessException(
					"Search ScoreDocs has IoException!", e,
					SearchProcessException.ErrorCode.SearchDocError);
		} catch (Exception ex) {
			// TODO: handle exception
			throw new SearchProcessException(
					"Search ScoreDocs has UnKnow Exception!", ex,
					SearchProcessException.ErrorCode.UnKnowError);
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (dir != null)
					dir.close();
				if (searcher != null)
					searcher.close();
			} catch (Exception e) {
				Log.getLogger(this.getClass()).error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @param from
	 * @return
	 * @throws Exception
	 */
	private Map<Document, Float> easySearch(String from) throws Exception {
		// 布尔查询向量
		String[] filedValues = (String[]) matchList
				.toArray(new String[matchList.size()]);
		// 查询字段（域）
		String QfiledKey = "question";
		String AfiledKey = "answer";
		// ArrayList<Document> arrayList = null;
		IndexReader reader = null;
		Directory dir = null;
		Searcher searcher = null;

		File file = new File(from);
		// 判断索引目录存在长度不为0
		if (file.isDirectory() && file.listFiles().length == 0) {
			throw new SearchProcessException("Lucene Index is Null!",
					SearchProcessException.ErrorCode.LuceneFileError);
		}
		// 打开索引目录
		try {
			dir = FSDirectory.open(file);
			reader = IndexReader.open(dir);
		} catch (Exception ex) {
			try {
				if (reader != null)
					reader.close();
				if (dir != null)
					dir.close();
			} catch (IOException e) {
				Log.getLogger(this.getClass()).error(e.getMessage(), e);
			}
			throw new SearchProcessException("Lucene Index Open failed!", ex,
					SearchProcessException.ErrorCode.LuceneFileError);
		}
		try {
			searcher = new IndexSearcher(reader);
			BooleanQuery booleanQuery = new BooleanQuery();
			for (int i = 0; i < filedValues.length; i++) {
				float weight = 1;
				weight = this.getCustomWeight(filedValues[i]);
				// FAQ查询
				if (from == Configuration.searchDir[0]) {
					Term term_q = null;
					term_q = new Term(QfiledKey, filedValues[i]);
					Query query_q = new TermQuery(term_q);
					query_q.setBoost(weight);

					Term term_a = new Term(AfiledKey, filedValues[i]);
					Query query_a = new TermQuery(term_a);
					query_a.setBoost(weight / Parameters.qaProportion);
					// System.err.println(filedValues[i]+":"+query.getBoost());
					if (rType == IEnumSearch.SearchRelationType.AND_SEARCH) {
						Occur occur = BooleanClause.Occur.MUST;
						booleanQuery.add(query_q, occur);
					} else if (rType == IEnumSearch.SearchRelationType.OR_SEARCH) {
						Occur occur = BooleanClause.Occur.SHOULD;
						booleanQuery.add(query_q, occur);
						booleanQuery.add(query_a, occur);
					}
				}
			}
			TopDocs topDocs = searcher.search(booleanQuery,
					Parameters.maxResult);
			ScoreDoc[] docs = topDocs.scoreDocs;
			// 构造查询结果集
			Map<Document, Float> map = new LinkedHashMap<Document, Float>();
			for (ScoreDoc doc : docs) {
				map.put(searcher.doc(doc.doc), doc.score);
			}
			System.out.println("共找到：" + topDocs.totalHits);
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new SearchProcessException(
					"Search ScoreDocs has IoException!", e,
					SearchProcessException.ErrorCode.SearchDocError);
		} catch (Exception ex) {
			// TODO: handle exception
			throw new SearchProcessException(
					"Search ScoreDocs has UnKnow Exception!", ex,
					SearchProcessException.ErrorCode.UnKnowError);
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (dir != null)
					dir.close();
				if (searcher != null)
					searcher.close();
			} catch (Exception e) {
				Log.getLogger(this.getClass()).error(e.getMessage(), e);
			}
		}
	}

}
