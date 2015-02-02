<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>院士搜索系统</title>
<style>
a:visited {
	color: #0000FF;
}
</style>

<!-- <style type="text/css">
.align-center {
	position: fixed;
	left: 35%;
	top: 30%;
	margin-left: width/2;
	margin-top: height/2;
}
</style> -->
</head>
<script type="text/javascript" src="js/search.js"></script>
<%
	String tagType = (String) session.getAttribute("tagType");
	//int iTagType = Integer.parseInt(tagType);
	System.err.println(tagType);
%>
<body onload="load(<%=(tagType !=null) ? tagType : 2%>);">
	<form id="frmSearch" action="SearchServlet" method="post">
		<table style="line-height:1px;" align="center">
			<tr height="90">
			</tr>
			<tr height="150">
				<td>
					<p align="center">
						<img src="images/21logo.gif" align="bottom"
							style="width:240px;height:90px;" />
					</p></td>

			</tr>
			<tr>
				<td valign="top"><br></td>
			</tr>
			<tr>
				<td>

					<div align="center">
						<input type="text"
							style="width:500px;height:40px;line-height:30px;font-size:15px;color:#808080;"
							align="middle" maxlength="100" id="txtSearch" name="txtSearch"
							alt="Search Criteria" onkeyup="searchSuggest();"
							value="如：知识发现"
							onfocus="if(this.value=='如：知识发现？'){this.value=''};this.style.color='black';"
							onblur="if(this.value==''||this.value=='如：知识发现？'){this.value='如：知识发现？';this.style.color='#808080';}" />
						<input type="submit"
							style="width:100px;height:45px;font-size:15px;color:#0066FF" value="点击搜索"
							alt="Run Search" />
					</div>
				</td>
			</tr>

			<tr>
				<%
					String reStr = (String) session.getAttribute("reStr");
					//System.err.println(s);
				%>
				<td style="height:200px;">
					<p id="resultShow" align="center" style="font-size:18px;">
					  <%-- <label id="resultShow"><%=(reStr != null) ? reStr : ""%> </label> --%>
						<font color="#00EE00" ><%=(reStr != null) ? reStr : ""%></font> 
					</p>
				</td>
			</tr>

		</table>
	</form>

</body>

</html>


