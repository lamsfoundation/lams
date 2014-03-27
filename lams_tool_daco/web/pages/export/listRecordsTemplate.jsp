<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<%-- This page is displayed if a teacher clicks on "view records" link in his main portfolio page --%>
	<title><fmt:message key="label.export.title" /></title>
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	<c:set var="user" value="${sessionMap.user}" />
	<c:set var="recordList" value="${user.records}" />
	<%-- To modify behavior of the included files (record lists) --%>
	<c:set var="includeMode" value="exportportfolio" />
	<c:set var="userFullName" value="${user.fullName}" />
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<script type="text/javascript">
	function checkCheckbox(checkboxName){
		var checkbox = document.getElementById(checkboxName);
		checkbox.checked=true;
	}
	function launchPopup(url,title) {
		var wd = null;
		if(wd && wd.open && !wd.closed){
			wd.close();
		}
		wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
		wd.window.focus();
	}
	</script>
	<lams:css localLinkPath="../../" />
	<link rel="StyleSheet" href="../daco.css" type="text/css" />
</lams:head>
<body class="stripes">
<div id="content">
	<%-- It displays user's full name, his records and summary table --%>
	<h1><c:out value="${userFullName}" escapeXml="true"/></h1>
	<%@ include file="/pages/learning/listRecords.jsp" %>
	<%@ include file="/pages/learning/questionSummaries.jsp" %>
</div>
<div id="footer"></div>
</body>
</lams:html>
