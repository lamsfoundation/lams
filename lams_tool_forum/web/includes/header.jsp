<%@ include file="/includes/taglibs.jsp" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<head>
<title><bean:message key="appName" /></title>

<link href="<c:out value="${lams}"/>css/aqua.css" rel="stylesheet" type="text/css">
<script src='<c:out value="${lams}"/>includes/javascript/common.js'></script>
<meta http-equiv="content-type" content="text/html; charset=<%=session.getAttribute("org.lamsfoundation.lams.web.filter.CHARSET.KEY")%>">
<script lang="javascript">
<html:javascript dynamicJavascript="false" staticJavascript="true"/>
</script>

</head>

