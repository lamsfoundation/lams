<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="title.export.loading" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<script type="text/javascript">
			// if the download starts and finishes too quickly, then the page doesn't get a chance to finish rendering. So slow it down a tad.
			function doRealDownload() {
				location.href="<c:url value='/authoring/exportToolContent.do?method=export&learningDesignID=${learningDesignID}'/>";
			}
			function download(){
				setTimeout("doRealDownload()", 3);
			}
			function closeWin(){
				window.close();
			}
		</script>
	</head>

	<BODY onload="download()">

	<div id="page-learner"><!--main box 'page'-->

		<h1 class="no-tabs-below">
			<fmt:message key="title.export.loading" />
		</h1>
		<div id="header-no-tabs-learner">
		</div><!--closes header-->
		
		 <div id="content-learner">
	  
				<H2><fmt:message key="msg.export.loading" /></H2>
				<div class="right-buttons"><a href="javascript:;" onclick="closeWin();" class="button"><fmt:message key="button.close" /></a></div>
		
		</div>  <!--closes content-->
			
		<div id="footer-learner">
		</div><!--closes footer-->
			
	</div><!--closes page-->

	</BODY>
</HTML>
