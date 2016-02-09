<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE html>
<lams:html>
	<lams:head>
		<title><fmt:message key="title.export.loading" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<script type="text/javascript">
			// if the download starts and finishes too quickly, then the page doesn't get a chance to finish rendering. So slow it down a tad.
			function doRealDownload() {
				location.href="<c:url value='/authoring/exportToolContent.do?method=export&learningDesignID=${learningDesignID}&format=${format}'/>";
			}
			function download(){
				setTimeout("doRealDownload()", 3);
			}
			function closeWin(){
				window.close();
			}
		</script>
	</lams:head>

	<body class="stripes" onload="download()">

		 <div id="content">
	  
		<h1>
			<fmt:message key="title.export.loading" />
		</h1>
		
				<H2><fmt:message key="msg.export.loading" /></H2>
				<table><tr><td>
				<div class="right-buttons"><a href="javascript:;" onclick="closeWin();" class="button"><span class="close"><fmt:message key="button.close" /></span></a></div>
				</td></tr></table>
		
		</div>  <!--closes content-->
			
		<div id="footer">
		</div><!--closes footer-->

	</body>
</lams:html>
