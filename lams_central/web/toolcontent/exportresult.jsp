<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="title.export.result"/></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		
	</head>

	<BODY>
			<c:choose>
			<c:when test="${empty ldErrorMessages}">
				<c:choose>
					<c:when test="${empty toolsErrorMessages}">
						<h1>
							<fmt:message key="msg.export.success" />
						</h1>
					</c:when>
					<c:otherwise>
						<h1>
							<fmt:message key="msg.export.ld.success" />
						</h1>
						<h2>
							<fmt:message key="msg.export.tool.error.prefix" />
						</h2>
						<c:forEach var="toolError" items="${toolsErrorMessages}">
							<li>
								${toolError}
							</li>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<h1>
					<fmt:message key="msg.export.failed" />
				</h1>
			</c:otherwise>
		</c:choose>
		<a href="javascript:;" onclick="closeWin();"  class="button"><fmt:message key="button.close" /></a>
	</BODY>
</HTML>
