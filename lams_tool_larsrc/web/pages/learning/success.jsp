<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	
	<body class="stripes">
		<!--  If successful reload all the resources and ratings. -->
 		<script type="text/javascript">
 			var reqIDVar = new Date();
 			document.location.href = '<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}&reqID='+reqIDVar.getTime();	
 		</script>
		<div style="align:center">
			<!--  Should never be seen -->
			<a href="<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}" type="button" class="btn btn-primary">
					<i class="fa fa-xm fa-refresh"></i> <fmt:message key="label.check.for.new" /></a>
		</div>
	</body>
</lams:html>