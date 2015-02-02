/**
 * 
 */
package com.shenji.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shenji.common.exception.OntoReasonerException;
import com.shenji.common.log.Log;
import com.shenji.search.FenciControl;
import com.shenji.search.IEnumSearch.SearchRelationType;
import com.shenji.search.SearchControl;
import com.shenji.search.exception.SearchProcessException;

/**
 * @author zhq
 * 
 */
public class SearchServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public SearchServlet() {
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String search = request.getParameter("txtSearch");
		String tagType = request.getParameter("tag0");
		HttpSession session = request.getSession();
		session.setAttribute("tagType", tagType);
		session.removeAttribute("reStr");
		// System.err.println(tagType);
		String str = null;

		try {
			str = new SearchControl().searchBasic(search,
						SearchRelationType.OR_SEARCH);
			request.getSession().setAttribute("msg", str);
			response.sendRedirect("answer.jsp");
			out.write(str);
			out.close();
		} catch (SearchProcessException e) {
			// TODO Auto-generated catch block
			out.write("查询过程发生错误!");
			Log.getLogger(this.getClass()).error(e.getMessage(),e);
		} /*catch (OntoReasonerException e) {
			// TODO Auto-generated catch block
			out.write("查询过程发生错误!");
			Log.getLogger(this.getClass()).error(e.getMessage(),e);
		}*/ catch (RuntimeException e) {
			// TODO Auto-generated catch block
			out.write("查询过程发生错误!");
			Log.getLogger(this.getClass()).error(e.getMessage(),e);
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
