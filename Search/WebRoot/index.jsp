<%@ page language="java" import="java.util.*"  contentType="text/html; charset=gbk" pageEncoding="gbk"%>
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
   <div class="col-md-4"><br><br><H1><b>学校招生问答系统</b></H1>
    </div>
   <div class="col-md-4"><img src="images/1.jpg" class="img-circle"></div>   
   <div class="col-md-2"></div>
   </div>
   <div class="row">
   <div class="col-md-2"></div>
   <div class="col-md-5">
   <form name="input" action="index.jsp" method="post">
    <input type="text" name="question" style="height:30px;width:449px;font-size:12px;"/>
    <input class="btn btn-default" type="submit" value="提问">
     <div class="checkbox">
  <label>
    <input type="checkbox" value="">
          第一类
  </label>
</div>
<div class="checkbox">
  <label>
    <input type="checkbox" value="">
    第二类
  </label>
</div>
   </form>
   </div>
   <div class="col-md-3"></div>
   </div>
   <div class="row">
      <div class="col-md-2"></div>
      <div id="content" class="col-md-8" >
      <% request.setCharacterEncoding("gbk"); %>
    <script type="text/javascript">
    
	$.getJSON("EasyJsonServlet?txtSearch=<%=request.getParameter("name")%>", function(json) {
        var res='';
		for ( var i = 0; i < json.length; i++) {
			var counter = json[i];
			res=res+counter.title+counter.content+"<br><br>";
       }
       document.getElementById("content").innerHTML=res;
	   if(res==""){
	   window.open('Exchange'); 
	   }
	} );
	
	
	
</script>
   </div>
    <div class="col-md-2"></div>
    </div>
  </body>
</html>
