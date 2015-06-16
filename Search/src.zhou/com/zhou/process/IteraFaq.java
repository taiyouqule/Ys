package com.zhou.process;

import java.io.File;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.zhou.compare.FileService;

/**
 * 遍历faq获得所有问题，作为主谓宾提取的材料
 * @author Administrator
 *
 */
public class IteraFaq {
	final static void showAllFiles(String path) throws Exception {
		FileService f = new FileService();
		File[] fs = new File(path).listFiles();
		FileWriter fw=new FileWriter(new File(Constants.question_path));
		
		for (int i=0;i<fs.length;i++) {
			File one=fs[i];
			String html = f.read(one.getCanonicalPath(), "utf-8");

			String question = getQuestion(html);
			System.out.println(question);
			if(i<fs.length-1){
				fw.write(question+"\n");
			}else{
				fw.write(question);
			}
			
		}
		fw.flush();
		fw.close();
		System.out.println(fs.length);
	}

	private static String getQuestion(String htmlStr) {
		org.jsoup.nodes.Document document = null;
		document = Jsoup.parse(htmlStr);
		Elements meta = document.select(".q");
		// System.out.println(meta.text());
		return meta.text();
	}

	private static String getAnswer(String htmlStr) {
		org.jsoup.nodes.Document document = null;
		document = Jsoup.parse(htmlStr);
		Elements meta = document.select(".a");
		// System.out.println(meta.text());
		return meta.text();
	}

	public static void main(String[] args) throws Exception {
		String path = Constants.faq_path;
		showAllFiles(path);

	}
}
