<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		<lams:head>
			<title>
				<fmt:message key="activity.title" />
			</title>
			<lams:headItems />
			<script type="text/javascript">
				var initialTabId = "${mindmapDTO.currentTab}";
			</script>
			<script type="text/javascript" src="<lams:WebAppURL />includes/javascript/monitoring.js"></script>
		</lams:head>

	<body class="stripes">	
	
		<lams:Page title="${pageTitle.monitoring}" type="learner">

			<div id="content">

				<c:choose>
				<c:when test="${isMultiUserMode}">
					<h4><fmt:message key="label.multimode" /></h4>
				</c:when>
				<c:otherwise>
					<h4><c:out value="${userDTO.firstName} ${userDTO.lastName}" escapeXml="true"/></h4>
				</c:otherwise>
				</c:choose>
				
				<%--  Monitor cannot edit mindmaps. mapjs.jsp also uses sessionId and mindmapId --%>
				<c:set var="multiMode">${isMultiUserMode}</c:set>
				<c:set var="contentEditable">false</c:set>
								
				<%@ include file="/common/mapjs.jsp"%>
				
				<button class="btn btn-primary voffset10 pull-right" name="backButton" onclick="history.go(-1)">
					<fmt:message>button.back</fmt:message>
				</button>

			</div>
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>