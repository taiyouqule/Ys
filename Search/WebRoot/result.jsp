 <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link href="css/index.css" rel="stylesheet" type="text/css" />
<title>搜索结果</title>
</head>
<body>
<div id="templatemo_body_wrapper">
<div id="templatemo_wrapper">
<div id="templatemo_header">   
        <div id="site_img" style="height:150px;width:300px"><h1><img src="images/banner2.jpg" /></h1></div>
    </div>	    
  <div id="templatemo_middle" style="height:100%;">
    <div id="templatemo_middle" style="height:100%;">
    <div class="float_r">
      <img src="images/2.jpg" />       				
    </div>
    <div style="width:300px">
  
   </div>
<td><%=session.getAttribute("msg")%></td><br/>
 </div>
</div>
</div> <!-- end of templatemo wrapper -->

</div> <!-- end of templatemo body wrapper -->

</body>
</html>
