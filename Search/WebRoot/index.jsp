<%@ page language="java" import="java.util.*"  contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%request.setCharacterEncoding("gbk");%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
	 <style  type="text/css">
   p{font-color:#33CCFF;}
   body{background-color:#eee;}
   </style>
	
	   <script src="js/jquery-1.4.2.min.js" type="text/javascript"></script>
   
  </head>
  
  <body>
   <div class="row">
   <div class="col-md-2"></div> 
     <div class="col-md-2">
   <br><br><img src="images/21logo.gif">
   </div>
    <div class="col-md-6"><br><br><H1><b>˰���ʴ�ϵͳ</b></H1>
    </div> 
   <div class="col-md-2"></div>
   </div>
   <div class="row">
   <div class="col-md-2"></div>
 
   <div class="col-md-8">
   <form name="input" action="index.jsp" method="post">
    <input type="text" name="question" style="height:30px;width:449px;font-size:12px;"/>
    <input class="btn btn-default" type="submit" value="����"/>
     <div class="checkbox">
  <label>
    <input type="checkbox" value="">
          ��һ��
  </label>
</div>
<div class="checkbox">
  <label>
    <input type="checkbox" value="">
    �ڶ���
  </label>
  </div>
   </form>
   </div>
   <div class="col-md-2"></div>
   </div>
   <div class="row">
      <div class="col-md-2"></div>
      <div id="content" class="col-md-8" >
  <script type="text/javascript">
	<%if(request.getParameter("question")!=null){%> $.getJSON("EasyJsonServlet?txtSearch=<%=request.getParameter("question")%>", function(json) { 
        var res='';
		for ( var i = 0; i < json.length; i++) {
			var counter = json[i];
			res=res+"<a href=\""+counter.path+"\">"+(i+1)+counter.title+"</a>"+counter.content+"<br><br>";
			       }
       document.getElementById("content").innerHTML=res;
	   if(res==""){
	   window.open('Exchange'); 
	   } 
	} );
	<%}%>
   </script>
  
   </div>
    <div class="col-md-2"></div>
    </div>
  </body>
</html>
