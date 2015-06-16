package com.zhou.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import com.zhou.process.Constants;
import com.zhou.process.MainPartExtractor;

/**
 * 获得相关词语
 * 
 * @author Administrator
 * 
 */
public class GetRelative {

	/**
	 * 根据输入语句提供相关语句
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public HashSet<String> getRelative(String input) throws IOException {
		// BufferedReader br = new BufferedReader(
		// new FileReader(new File(Constants.extra_path)));
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(Constants.extra_path)), "utf8"));

		ArrayList<String> all = new ArrayList<String>();
		String line = "";
		ArrayList<String> inputSplit = MainPartExtractor.iteraOneString(input);
		while ((line = (br.readLine())) != null) {
			all.addAll(MyTriple.getLost(line.split("_"), inputSplit));
		}

		HashSet<String> removeString = new HashSet<String>();
		for (String one : all) {
			for (String splitword : inputSplit) {
				if (one.equals(splitword)) {
					removeString.add(one);
				}
			}
		}
		all.removeAll(removeString);

		HashSet<String> returnWord = new HashSet<String>();
		for (String one : all) {
			returnWord.add(one);
		}
		return returnWord;
	}
}
