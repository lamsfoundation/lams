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
			function openDesign(ldID){
			}
			function closeWin(){
				
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
						<a href="javascript:;" onclick="openDesign(${learningDesignID});" class="button"><fmt:message key="button.open.design" /></a>
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
						<a href="javascript:;" onclick="openDesign(${learningDesignID});"  class="button"><fmt:message key="button.open.design" /></a>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<h1>
					<fmt:message key="msg.import.failed" />
				</h1>
				<a href="javascript:;" onclick="closeWin();"  class="button"><fmt:message key="button.close" /></a>
			</c:otherwise>
		</c:choose>
	</BODY>
</HTML>
