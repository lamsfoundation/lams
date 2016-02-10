<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

<!-- HTTP 1.1 -->
<meta http-equiv="Cache-Control" content="no-store" />
<!-- HTTP 1.0 -->
<meta http-equiv="Pragma" content="no-cache" />
<!-- Prevents caching at the Proxy Server -->
<meta http-equiv="Expires" content="0" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- ********************  CSS ********************** -->
<c:choose>
	<c:when test="${not empty localLinkPath}">
		<lams:css localLinkPath="${localLinkPath}" style="main"/>
	</c:when>
	<c:otherwise>
		<lams:css style="main"/>
	</c:otherwise>
</c:choose>

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>

