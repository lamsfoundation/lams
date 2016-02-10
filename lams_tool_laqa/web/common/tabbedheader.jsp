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
		<lams:css localLinkPath="${localLinkPath}" style="main"/>
	</c:when>
	<c:otherwise>
		<lams:css  style="main"/>
	</c:otherwise>
</c:choose>

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>


