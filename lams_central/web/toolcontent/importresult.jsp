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
		<div id="page">	

		<h1 class="no-tabs-below">
			<fmt:message key="title.import" />
		</h1>
		<div id="header-no-tabs"></div>
		<div id="content">

		<c:choose>
			<c:when test="${empty ldErrorMessages}">
				<c:choose>
					<c:when test="${empty toolsErrorMessages}">
						<p><fmt:message key="msg.import.success"/></p>
					</c:when>
					<c:otherwise>
						<p><fmt:message key="msg.import.ld.success" /></p>
						<p><fmt:message key="msg.import.tool.error.prefix" /></p>
						<div id="error"/>
						<UL>
						<c:forEach var="toolError" items="${toolsErrorMessages}">
							<li>
								${toolError}
							</li>
						</c:forEach>
						</UL>
						</div/>
						</c:otherwise>
				</c:choose>
				<%-- display new learing desing in Flash side even some tool import failed --%>
				<%@include file="import_passon.jsp"%>
			</c:when>
			<c:otherwise>
					<p><fmt:message key="msg.import.failed" /></p>
					<div id="error"/>
						<fmt:message key="msg.reason.is" /> ${ldErrorMessages}
					</div>
			</c:otherwise>
		</c:choose>
		
		<P><a href="javascript:;" onclick="closeWin();"><fmt:message key="button.close" /></a></p>
		</div>
		<div id="footer"></div>
		
		</div>
		
	</BODY>
</HTML>
