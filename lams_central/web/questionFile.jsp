<!DOCTYPE HTML>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.file.title" /></title>
	
	<lams:css/>	
	<style type="text/css">
		.button {
			float: right;
			margin-left: 10px;
		}
		
		#buttonsDiv {
			padding: 15px 0px 15px 0px;
		}
		
		div#errorArea {
			display: none;
		}
	</style>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
	  		if ($.trim($('#errorArea').html()) != "") {
	  			window.resizeTo(600, 350);
	  			$('#errorArea').show();
	  		}
		});
	</script>
</lams:head>

<body class="stripes">
<div id="content">
	<div id="errorArea" class="warning" >
		<html:messages id="error">
		   <c:out value="${error}" escapeXml="false"/><br/>
		</html:messages>
	</div>
	<h2><fmt:message key="label.questions.file.title" /></h2>
 
	<form id="questionForm" action="<lams:LAMSURL/>questions.do" enctype="multipart/form-data" method="post">
		<input type="hidden" name="returnURL" value="${empty param.returnURL ? returnURL : param.returnURL}" />
		<input type="hidden" name="limitType" value="${empty param.limitType ? limitType : param.limitType}" />
		
		<input type="file" name="file" size="53"/>
		<div id="buttonsDiv">
			<input class="button" value='<fmt:message key="button.cancel"/>' type="button" onClick="javascript:window.close()" />	
			<input class="button" value='<fmt:message key="label.ok"/>' type="submit" />
		</div>
	</form>
</div>   
<div id="footer"></div>
</body>
</lams:html>