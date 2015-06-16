package com.zhou.compare;

import java.util.ArrayList;

import com.zhou.process.Constants;
/**
 * 通过三元组找到相关词语
 * @author Administrator
 *
 */
public class MyTriple {

	/**
	 * 三元组拓展，分别根据主谓，主宾，谓宾各进行一次拓展
	 * 
	 * @param items  //三元组 库
	 * @param in    //输入关键词(主谓宾)
	 * @return
	 */
	public static ArrayList<String> getLost(String[] items, ArrayList<String> in) {
		ArrayList<String> all = new ArrayList<String>();
		for (int i = 0; i < in.size(); i++) {// 对每个词分析
			switch (i) {
			case 0: // 去掉主语
				if (!in.get(1).equals(Constants.nullValue)
						&& !in.get(2).equals(Constants.nullValue)) {
					String temp=find(in.get(1), in.get(2), items);
					if(!Constants.nullValue.equals(temp))
						all.add(temp);
				}

				break;
			case 1: // 去掉谓语
				if (!in.get(0).equals(Constants.nullValue)
						&& !in.get(2).equals(Constants.nullValue)) {
					String temp=find(in.get(0), in.get(2), items);
					if(!Constants.nullValue.equals(temp))
						all.add(temp);
				}

				break;
			case 2: // 去掉宾语

				if (!in.get(0).equals(Constants.nullValue)
						&& !in.get(1).equals(Constants.nullValue)) {
					String temp=find(in.get(0), in.get(1), items);
					if(!Constants.nullValue.equals(temp))
						all.add(temp);
				}

				break;
			default:
				break;
			}
		}
		return all;
	}

	/**
	 * 找到all中除str1和str2之外的那个词
	 * 
	 * @param str1
	 * @param str2
	 * @param all
	 * @return
	 */
	private static String find(String str1, String str2, String[] all) {
		String out = Constants.nullValue;
		if (match(str1, all[0]) && match(str2, all[1])) {
			out = all[2];
		} else if (match(str1, all[0]) && match(str2, all[2])) {
			out = all[1];
		} else if (match(str1, all[1]) && match(str2, all[0])) {
			out = all[2];
		} else if (match(str1, all[1]) && match(str2, all[2])) {
			out = all[0];
		} else if (match(str1, all[2]) && match(str2, all[0])) {
			out = all[1];
		} else if (match(str1, all[2]) && match(str2, all[1])) {
			out = all[0];
		}
		return out;
	}

	/**
	 * 匹配算法 str1与str2之间包含或者被包含返回true
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	private static boolean match(String str1, String str2) {
		return (str1.indexOf(str2) != -1) || (str2.indexOf(str1) != -1);
	}
}
