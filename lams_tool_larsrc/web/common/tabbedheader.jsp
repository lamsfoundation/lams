<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 	<!-- ********************  CSS ********************** -->
	<lams:css/>

 	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<lams:JSImport src="includes/javascript/rsrccommon.js" relative="true" />
	<script type="text/javascript" src="<c:url value='/includes/javascript/rsrcresourceitem.js'/>"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>