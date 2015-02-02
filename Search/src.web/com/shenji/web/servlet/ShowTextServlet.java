package com.shenji.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shenji.common.util.TxtToHtml;
import com.shenji.search.Configuration;

/**
 * չʾ�������ݺ͹ؼ��������ڶ���
 * @author Administrator
 *
 */
public class ShowTextServlet extends HttpServlet {

	private String webpath = null;

	/**
	 * Constructor of the object.
	 */
	public ShowTextServlet() {
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

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String paperid = request.getParameter("paperid");
		String line = request.getParameter("line");
		String htmlcontent = TxtToHtml.getHtml(Configuration.sourcePath+ "/txt/"
				+ paperid + ".txt", Integer.parseInt(line));
		HttpSession session = request.getSession();
		session.setAttribute("content", htmlcontent);
		response.sendRedirect("par.jsp");
		//����������
		//out.print(htmlcontent);
		//System.out.println(String.format("paperidΪ%s,lineΪ%s", paperid, line));
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

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		ServletContext context=getServletContext();


		webpath = context.getRealPath("/");
	}

}