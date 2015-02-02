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
  <div class="col-md-8"><img src="images/banner2.jpg" width="100%"/>
  </div>
  <div class="col-md-2"></div>
  </div>
  <div class="row" style="height:100%">
  <div class="col-md-2"></div>
  <div class="col-md-6" style="border-right:1px solid">
  <%=session.getAttribute("msg")%>
  </div>
  <div class="col-md-2" >
  <img src="images/2.jpg"  width="100%"/> <br>
  <a href="#" class="btn btn-primary btn-lg active" role="button">数据挖掘</a><br><br>
  <a href="#" class="btn btn-primary btn-lg active" role="button">知识仓库</a><br><br>
  <a href="#" class="btn btn-primary btn-lg active" role="button">智能信息处理</a><br><br><br>
  <img src="images/3.jpg" class="img-responsive" alt="Responsive image" width="100%"><br>
  数据挖掘用可视化效果展示
  </div>
  <div class="col-md-2"></div>
  </div>
  </body>
</html>
