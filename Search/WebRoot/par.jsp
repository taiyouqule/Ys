<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'answer.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="css/bootstrap.min.css">

  </head>
  
  <body>
   <div class="row">
  <div class="col-md-2"></div>
  <div class="col-md-8"><img src="images/banner2.jpg"  width="100%"/>
  </div>
  <div class="col-md-2"></div>
  </div>
  <div class="row" style="height:100%">
  <div class="col-md-2"></div>
  <div class="col-md-8"><%
    	String content =(String) session.getAttribute("content");
    	String[] arrays = content.split("<body>");
    	String[] cont = arrays[1].split("</html>");
    	String myResult = cont[0];
    	System.out.println(myResult);
    	out.println(myResult);
     %>
  </div>
  <div class="col-md-2"></div>
    </div>
  </body>
</html>
