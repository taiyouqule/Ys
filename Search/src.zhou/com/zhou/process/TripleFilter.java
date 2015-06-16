package com.zhou.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.zhou.bean.MainPart;

/**
 * 对faq中提取的三元组进行筛选，仅保留三元组均不为空的部分
 * @author Administrator
 *
 */
public class TripleFilter {

	private static void filter() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(
				Constants.extra_all_path)));
		ArrayList<String> all = new ArrayList<String>();
		String line = "";
		while ((line = (br.readLine())) != null) {
			if(!line.contains(Constants.nullValue)){   //主谓宾均不为空
				all.add(line);
			}
			
		}


		FileWriter fw = new FileWriter(new File(Constants.extra_path));
		for (String one : all) {

			fw.write(String.format("%s\n",one));
			fw.flush();

		}
		fw.close();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TripleFilter.filter();
	}

}
