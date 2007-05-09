<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="title.import.result" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<script type="text/javascript">
			function closeWin(){
				window.close();
			}
		</script>
	</head>

	<body class="stripes">

	
		<div id="content">
	  
		<h1>
			<fmt:message key="title.import" />
		</h1>

		<c:choose>
			<c:when test="${empty ldErrorMessages}">
				<c:choose>
					<c:when test="${empty toolsErrorMessages}">
						<h2><fmt:message key="msg.import.success"/></h2>
					</c:when>
					<c:otherwise>
						<h2><fmt:message key="msg.import.ld.success" /></h2>
						<h2><fmt:message key="msg.import.tool.error.prefix" /></h2>
						<c:forEach var="toolError" items="${toolsErrorMessages}">
							<p class="warning">${toolError}</p>
						</c:forEach>
						</c:otherwise>
				</c:choose>
				<%-- display new learing desing in Flash side even some tool import failed --%>
				<%@include file="import_passon.jsp"%>
			</c:when>
			<c:otherwise>
					<h2><fmt:message key="msg.import.failed" /></h2>
					<h2> <fmt:message key="msg.reason.is" /></h2>
					<div>
						<c:forEach var="ldError" items="${ldErrorMessages}">
							<p class="warning"> ${ldError}</p>
						</c:forEach>
					</div>
			</c:otherwise>
		</c:choose>
		<div class="right-buttons"><a href="javascript:;" onclick="closeWin();" class="button"><fmt:message key="button.close" /></a></div>
		
		</div>  <!--closes content-->
	   
		
		<div id="footer">
		</div><!--closes footer-->
		
	</body>
</lams:html>
