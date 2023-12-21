<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${toolSessionID}" >
	<!--  If successful reload all the resources and ratings. -->
 	<script type="text/javascript">
 		var reqIDVar = new Date();
 		document.location.href = '<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}&reqID='+reqIDVar.getTime();	
 	</script>
 	
	<div id="container-main text-center">
		<!--  Should never be seen -->
		<a href="<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}" type="button" class="btn btn-primary">
			<i class="fa fa-xm fa-refresh"></i> <fmt:message key="label.check.for.new" />
		</a>
	</div>
</lams:PageLearner>