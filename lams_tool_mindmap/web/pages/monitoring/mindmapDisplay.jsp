<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="tool"> <lams:WebAppURL /> 	</c:set>
	
	<script type="text/javascript">
		var initialTabId = "${mindmapDTO.currentTab}";
	</script>
	<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
	
	<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>css/jquery.minicolors.css"></link>
	<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
	<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
				
	<script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script src="<lams:LAMSURL/>includes/javascript/jquery.minicolors.min.js"></script>
	<script src="${tool}includes/javascript/mapjs/main.js"></script>
	<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>

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