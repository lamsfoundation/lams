<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
	
	<script type="text/javascript">

 		function finishSession(){
 			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
 		
     </script>
</lams:head>

<body class="stripes">

	<c:set scope="request" var="title">
		<fmt:message key="activity.title" />
	</c:set>
	<lams:Page type="learner" title="${title}">
	
		<lams:Alert5 type="info" id="removed-user-info" close="false">
			<fmt:message key="label.removed.user.warning" />
		</lams:Alert5>
		
		<div class="activity-bottom-buttons">
			<a href="javascript:location.reload(true);"	class="btn btn-secondary">
				<fmt:message key="button.try.again" />
			</a>
		
			<a href="#nogo" path="FinishButton" id="finishButton" onclick="return finishSession()" class="btn btn-primary na">
				<fmt:message key="label.finished" />
			</a>
		</div>
		
	</lams:Page>
	
	<div id="footer"></div>

</body>
</lams:html>
