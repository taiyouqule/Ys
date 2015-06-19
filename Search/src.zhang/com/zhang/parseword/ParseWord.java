package com.zhang.parseword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.AttributeSource;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.shenji.search.FenciControl;

import antlr.TokenStream;

public class ParseWord {
	//扩展查询结果的map
	private static Map<String,String> extentedMap = new HashMap<String,String>();
	
	static {
		try {
			initMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//对查询语句进行分词
	public List<String> fenci(String textString){
		FenciControl control=new FenciControl();
		String matchStr=control.iKAnalysis(textString);
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(matchStr.split("/")));
		return list;
	}
	//初始化生成扩展查询词的map
	private static void initMap() throws IOException{
		File file = new File("D:\\Ys\\Search\\WebRoot\\extention.properties");
		//System.out.println("00000000000000000000000+"+file.exists());
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
		String x = null;
		while((x = br.readLine()) != null){
			String[] temp = x.split("=");
//			System.out.println("-------------------length"+temp.length);
//			System.out.println(temp[0]);
//			System.out.println(temp[1]);
			extentedMap.put(temp[0], temp[1]);
		}
	}
	
	public String[] get(String word){
		String temp = extentedMap.get(word);
		if(temp == null)
			return null;
		String[] results = temp.split("\\|");
		return results;
	}
}
	
