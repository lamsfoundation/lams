<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool">	<lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<script type="text/javascript">
			var initialTabId = "${mindmapDTO.currentTab}";
		</script>
		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<lams:JSImport src="includes/javascript/common.js" />
		
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
		
		<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
		<script src="${tool}includes/javascript/jquery.timer.js"></script>
		<script src="<lams:LAMSURL/>includes/javascript/fullscreen.js"></script>
		<script src="${tool}includes/javascript/mapjs/main.js"></script>
		<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
	</lams:head>

	<body class="stripes">	
	
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="learner">

			
			<div id="content">

				<c:choose>
				<c:when test="${isMultiUserMode}">
					<h4><fmt:message key="label.multimode" /></h4>
				</c:when>
				<c:otherwise>
					<h4><c:out value="${userDTO.getFullName()}" escapeXml="true"/></h4>
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
		</lams:Page>
		<div id="footer"></div>
	</body>
</lams:html>