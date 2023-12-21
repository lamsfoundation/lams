<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<!-- Triggers redirection to the learner page after a file is submitted, to stop Refresh reloading the file. -->

<lams:PageLearner title="${sessionMap.title}" toolSessionID="${sessionMap.toolSessionID}" >

<script language="JavaScript" type="text/JavaScript">
	$(document).ready(function(){
		window.location.href = '<lams:WebAppURL />learning/refresh.do?sessionMapID=${sessionMapID}';
	});
</script>
	<div class="text-center" role="alert" aria-busy="true" style="margin-top: 10px; margin-bottom: 15px;">
		<i class="fa fa-2x fa-refresh fa-spin text-primary" aria-hidden="true"></i>
	</div>
</lams:PageLearner>


