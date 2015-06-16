package com.zhou.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shenji.common.log.Log;
import com.shenji.search.IEnumSearch.SearchRelationType;
import com.shenji.search.SearchControl;
import com.shenji.web.bean.EasyItemBean;
import com.zhou.compare.GetRelative;

public class EasyZhouJsonServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EasyZhouJsonServlet() {
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
		String triple = request.getParameter("triple");
		System.out.println("搜索内容----->" + search);
		// System.err.println(tagType);
		List<EasyItemBean> list = null;

		try {

			if ("true".equals(triple)) { // 三元组关联搜索
				HashSet<String> relativeSet = new GetRelative()
						.getRelative(search);
				ArrayList<String> temp = new ArrayList<String>();
				for (String one : relativeSet) {
					temp.add(one);
				}
				System.out.println("相关词汇------->" + temp);
				String[] relativeWord = (String[]) temp.toArray(new String[temp
						.size()]);

				list = new SearchControl().searchEasyJson(search,
						SearchRelationType.OR_SEARCH, relativeWord);
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
