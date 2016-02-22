<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

<!-- ********************  CSS ********************** -->
<c:choose>
	<c:when test="${not empty localLinkPath}">
		<lams:css localLinkPath="${localLinkPath}"/>
	</c:when>
	<c:otherwise>
		<lams:css />
	</c:otherwise>
</c:choose>

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>

<link type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>

