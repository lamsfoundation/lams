<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 <!-- ********************  CSS ********************** -->
<link href="<html:rewrite page='/includes/css/commonCartridge.css'/>" rel="stylesheet" type="text/css">
<lams:css style="main"/>

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgecommon.js'/>"></script>
<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    

	
