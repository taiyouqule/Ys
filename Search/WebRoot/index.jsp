<%@ page language="java" import="java.util.*"  contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%request.setCharacterEncoding("gbk");%> 



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>税务问答</title>
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
    <div class="col-md-6"><br><br><H1><b>税务问答系统</b></H1>
    </div> 
   <div class="col-md-2"></div>
   </div>
   <div class="row">
   <div class="col-md-2"></div>
 
   <div class="col-md-8">
    <!-- <input type="text"  id="question"  style="height:30px;width:449px;font-size:12px;"/>
    <input class="btn btn-lg btn-info" type="submit" value="提问" onclick="ask()" /> -->
    
    
      <div class="input-group">
      <input type="text" class="form-control" id="question" style="height:50px">
       <span class="input-group-btn">
        <button class="btn btn-default" type="button"  style="height:50px" onclick="askTwo()">提问</button>
      </span>
    </div>
    
    
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
   
   <label class="checkbox-inline">
  <input type="checkbox" id="triple" value="option1"> 二元组
</label>
<label class="checkbox-inline">
  <input type="checkbox" id="" value="option2"> 同义词拓展
</label>
<label class="checkbox-inline">
  <input type="checkbox" id="apriori" value="option3"> apriori算法
</label>
   </div>
   <div class="col-md-2"></div>
   </div>
   <div class="row">
      <div class="col-md-2"></div>
      <div id="content" class="col-md-8" >
   </div>
    <div class="col-md-2"></div>
    </div>
    
  </body>
  
  <script type="text/javascript">
    function ask(){
    	//alert('aaaaaaa');
    	$.getJSON("EasyZhouJsonServlet?txtSearch="+document.getElementById("question").value
	+"&triple="+(document.getElementById("triple")).checked, function(json) { 
        var res='';
		for ( var i = 0; i < json.length; i++) {
			var counter = json[i];
			res=res+"<a href=\""+counter.path+"\">"+(i+1)+counter.title+"</a>"+counter.content+"<br><br>";
			       }
       document.getElementById("content").innerHTML=res;
	} );
    }
    function askTwo(){
    	$.getJSON("EasyZhangJsonServlet?txtSearch="+document.getElementById("question").value
	+"&apriori="+(document.getElementById("apriori")).checked, function(json) { 
        //alert('aaaaaaa');
        var res='';
		for ( var i = 0; i < json.length; i++) {
			var counter = json[i];
			res=res+"<a href=\""+counter.path+"\">"+(i+1)+counter.title+"</a>"+counter.content+"<br><br>";
			       }
       document.getElementById("content").innerHTML=res;
	} );
    }
    
    </script>
</html>
