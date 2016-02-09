<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE html>
<lams:html>
	<lams:head>
		<title><fmt:message key="title.export" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<script type="text/javascript">
			function closeWin(){
				window.close();
			}
		</script>
	</lams:head>

	<body class="stripes">

	<div id="content">

	<c:choose>
		<c:when test="${empty ldErrorMessages}">
			<c:choose>
				<c:when test="${empty toolsErrorMessages}">
					<h1><fmt:message key="title.export" /></h1>
					<h2><fmt:message key="msg.export.success" /></h2>
				</c:when>
				<c:otherwise>
					<h1><fmt:message key="title.export" /></h1>
					<h2><fmt:message key="msg.export.failed" /></h2>
					<h2><fmt:message key="msg.export.tool.error.prefix" /></h2>
					<c:forEach var="toolError" items="${toolsErrorMessages}">
						<p class="warning">${toolError}</p>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<div id="content">
				<h1><fmt:message key="title.export" /></h1>
				<h2><fmt:message key="msg.export.failed" /></h2>
				<p class="warning"><fmt:message key="msg.reason.is" /> ${ldErrorMessages}</p>
		</c:otherwise>
	</c:choose>
		
	<table><tr><td>
		<div class="right-buttons"><a href="javascript:;" onclick="closeWin();" class="button"><span class="close"><fmt:message key="button.close" /></span></a></div>
	</td></tr></table>
	</div>  <!--closes content-->

	<div id="footer">
	</div><!--closes footer-->
		
	</body>
</lams:html>

	  

