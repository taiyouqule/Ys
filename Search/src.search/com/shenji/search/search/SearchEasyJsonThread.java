package com.shenji.search.search;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.shenji.search.FenciControl;
import com.shenji.search.IEnumSearch;
import com.shenji.search.exception.SearchProcessException;
import com.shenji.web.bean.EasyItemBean;

public class SearchEasyJsonThread implements Callable<List<EasyItemBean>> {

	private String args;
	private String from;
	// 或查询 结果词集
	private Set<String> matchList = new LinkedHashSet<String>();
	// 和查询 结果词集（不能重复）
	private Set<String> maxMatchSet = new LinkedHashSet<String>();
	public IEnumSearch.SearchRelationType rType;
	
	private void initSet(){
		FenciControl control=new FenciControl();
		String matchStr=control.iKAnalysis(args);
		matchList.addAll(Arrays.asList(matchStr.split("/")));
		String maxStr=control.iKAnalysisMax(args);
		maxMatchSet.addAll(Arrays.asList(maxStr.split("/")));
		
//		System.out.println("matchStr--------->"+matchStr);
//		System.out.println("maxStr----------->"+maxStr);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param args
	 *            查询请求
	 * @param from
	 *            来源
	 * @param maxTextShow
	 *            最大文本显示
	 * @param searchType
	 *            查询类型（并查询、或查询）
	 */
	public SearchEasyJsonThread(String args, String from,
			IEnumSearch.SearchRelationType rType) {
		this.args = args;
		this.from = from;
		this.rType = rType;
		// 中文分词
		//IKAnalysis();
		initSet();
	}
	
	

	public List<EasyItemBean> call() throws Exception {
		// 构建布尔查询对象并返回结果
		BooleanSearch booleanSearch = new BooleanSearch(args, matchList,
				maxMatchSet, rType);
		List<EasyItemBean> result = null;
		try {
			result = booleanSearch.getEasyJsonResult(args, from);
		} catch (SearchProcessException e) {
			// 这里发生严重错误，没有查询结果，需要子线程抛给主线程进行处理
			throw e;// 这里线程池会包装成ExecutionException异常
		}
		return result;

	}

}

