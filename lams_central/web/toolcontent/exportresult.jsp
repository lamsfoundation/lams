<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="title.export.result" /></title>
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
	
	
	
		<c:choose>
			<c:when test="${empty ldErrorMessages}">
				<c:choose>
					<c:when test="${empty toolsErrorMessages}">
						<h1 class="no-tabs-below">
							<fmt:message key="msg.export.success" />
						</h1>
						<div id="header-no-tabs"></div>
						<div id="content">
					</c:when>
					<c:otherwise>
						<h1 class="no-tabs-below">
							<fmt:message key="msg.export.ld.success" />
						</h1>
						<div id="header-no-tabs"></div>
						<div id="content">
							<h2>
								<fmt:message key="msg.export.tool.error.prefix" />
							</h2>
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
			</c:when>
			<c:otherwise>
				<h1 class="no-tabs-below">
					<fmt:message key="msg.export.failed" />
				</h1>
				<div id="header-no-tabs"></div>
				<div id="content">
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
