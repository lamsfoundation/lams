<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="title.import.result" /></title>
	<!-- ********************  CSS ********************** -->
	<lams:css />
	<script type="text/javascript">
		function closeWin() {
			window.close();
		}
	</script>
	
</lams:head>

<c:set var="title" scope="request">
	<fmt:message key="title.import" />
</c:set>

<body class="stripes">

	<lams:Page type="admin" title="${title}">

		<c:choose>
			<c:when test="${empty ldErrorMessages}">
				<c:choose>
					<c:when test="${empty toolsErrorMessages}">
						<h4>
							<i class="fa fa-check text-success"></i>&nbsp<fmt:message key="msg.import.success" />
						</h4>
					</c:when>
					<c:otherwise>
						<h4>
							<fmt:message key="msg.import.ld.success" />
						</h4>
						<h4>
							<fmt:message key="msg.import.tool.error.prefix" />
						</h4>
						<c:forEach var="toolError" items="${toolsErrorMessages}">
							<p class="warning">${toolError}</p>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				<%-- display new learning design Flash side even some tool import failed --%>
				<%@include file="import_passon.jsp"%>
			</c:when>
			<c:otherwise>
				<h4>
					<fmt:message key="msg.import.failed" />
				</h4>
				<h5>
					<fmt:message key="msg.reason.is" />:
				</h5>
				<lams:Alert id="errors" type="danger" close="false">
					<c:forEach var="ldError" items="${ldErrorMessages}">
						<p class="warning">${ldError}</p>
					</c:forEach>
				</lams:Alert>
			</c:otherwise>
		</c:choose>
		<div class="voffset10">
			<a href="importToolContent.do?method=import" class="btn btn-default pull-left"><fmt:message
						key="button.select.another.importfile" /></a> <a href="javascript:;" onclick="closeWin();" class="btn btn-primary pull-right"><fmt:message key="button.close" /></a>
		</div>

	</lams:Page>
</body>
</lams:html>
