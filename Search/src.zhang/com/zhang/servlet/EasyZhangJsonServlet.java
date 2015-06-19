package com.zhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shenji.common.log.Log;
import com.shenji.search.SearchControl;
import com.shenji.search.IEnumSearch.SearchRelationType;
import com.shenji.web.bean.EasyItemBean;
import com.zhang.parseword.ParseWord;
import com.zhou.compare.GetRelative;

public class EasyZhangJsonServlet extends HttpServlet {

	
	/**
	 * Constructor of the object.
	 */
	public EasyZhangJsonServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		// String search = new
		// String(request.getParameter("txtSearch").getBytes(
		// "ISO8859_1"), "utf-8");
		final String search = new String(request.getParameter("txtSearch")
				.getBytes("ISO8859_1"), "gbk");
		String apriori = request.getParameter("apriori");
		System.out.println("搜索内容----->" + search);
		// System.err.println(tagType);
		List<EasyItemBean> list = null;

		try {

			if ("true".equals(apriori)) { // apriori扩展查询
				ParseWord parseWord = new ParseWord();
				List<String> wordList = parseWord.fenci(search);
				
//				for(String x:wordList)
//					System.out.println("分词："+x);
				
				Set<String> extendedSet = new HashSet<String>();
				Iterator<String> iter = wordList.iterator();
				while(iter.hasNext()){
					String[] results = parseWord.get(iter.next());
//					if(results != null){
//						for(String x : results)
//							System.out.println("扩展词："+x);
//					}
					//System.out.println("=======================");
					if(results != null){
						for(String x : results){
							if(!wordList.contains(x))
								extendedSet.add(x);
						}
					}	
				}
				ArrayList<String> temp = new ArrayList<String>();
				if(extendedSet.size() > 0){
					for (String one : extendedSet) {
						temp.add(one);
					}
				}
				//System.out.println("相关词汇------->" + temp);
				if(temp.size() == 0){
					list = new SearchControl().searchEasyJson(search,
							SearchRelationType.OR_SEARCH);
				}else{
					String[] relativeWord = (String[]) temp.toArray(new String[temp.size()]);
					list = new SearchControl().searchEasyJson(search,
							SearchRelationType.OR_SEARCH, relativeWord);
				}			
			} else {
				list = new SearchControl().searchEasyJson(search,
						SearchRelationType.OR_SEARCH);
			}
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String line = gson.toJson(list);
			out.println(line);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.write("null");
			Log.getLogger(this.getClass()).error(e.getMessage(), e);
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}
