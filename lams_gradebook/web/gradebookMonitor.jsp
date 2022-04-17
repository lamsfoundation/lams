<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="gradebook.title.window.lessonMonitor"/></title>
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL />gradebook/includes/css/gradebook.css" rel="stylesheet" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable-lams.css"> 
	<lams:css suffix="chart"/>
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.cookie.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/d3.js"></script>
 	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/chart.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />gradebook/includes/javascript/blockexportbutton.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/x-editable.js"></script>
	
	<jsp:include page="includes/jsp/jqGridIncludes.jsp"></jsp:include>
	
</lams:head>

<body class="stripes">

	<%@ include file="gradebookMonitorContent.jsp"%>

</body>
</lams:html>