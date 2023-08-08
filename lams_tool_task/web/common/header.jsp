<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

 	<!-- ********************  CSS ********************** -->
	<link href="<lams:WebAppURL/>includes/css/taskList.css" rel="stylesheet" type="text/css">
	<lams:css />


 	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>    
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>   
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<lams:JSImport src="includes/javascript/taskListcommon.js" relative="true" />
	<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>