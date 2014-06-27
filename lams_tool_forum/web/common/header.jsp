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
		<lams:css localLinkPath="${localLinkPath}" />
	</c:when>
	<c:otherwise>
		<lams:css />
	</c:otherwise>
</c:choose>

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>

<script type="text/javascript">
	function closeAndRefreshParentMonitoringWindow() {
		refreshParentMonitoringWindow();
		window.close();
	}  				
</script>


