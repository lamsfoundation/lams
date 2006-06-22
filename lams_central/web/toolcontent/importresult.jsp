<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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

	<BODY>
		<c:choose>
			<c:when test="${empty ldErrorMessages}">
				<c:choose>
					<c:when test="${empty toolsErrorMessages}">
						<h1>
							<fmt:message key="msg.import.success" />
						</h1>
					</c:when>
					<c:otherwise>
						<h1>
							<fmt:message key="msg.import.ld.success" />
						</h1>
						<h2>
							<fmt:message key="msg.import.tool.error.prefix" />
						</h2>
						<c:forEach var="toolError" items="${toolsErrorMessages}">
							<li>
								${toolError}
							</li>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				<%-- display new learing desing in Flash side even some tool import failed --%>
				<%@include file="import_passon.jsp"%>
			</c:when>
			<c:otherwise>
				<h1>
					<fmt:message key="msg.import.failed" />
				</h1>
				<b><fmt:message key="msg.reason.is" /> ${ldErrorMessages}</b>
			</c:otherwise>
		</c:choose>
		<div align="center">
			<a href="javascript:;" onclick="closeWin();" class="button"><fmt:message key="button.close" /></a>
		</div>
	</BODY>
</HTML>
