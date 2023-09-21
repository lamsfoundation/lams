<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>

<fmt:message var="title" key='activity.title'/>
<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}" refresh="60">
	<script type="text/javascript">
 		function finishSession(){
 			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
		}
     </script>
	
	<div class="container-lg">
		<lams:Alert5 type="info" id="removed-user-info" close="false">
			<fmt:message key="label.removed.user.warning" />
		</lams:Alert5>
		
		<div class="activity-bottom-buttons">
			<button type="button" onclick="javascript:location.reload(true);" class="btn btn-secondary btn-icon-refresh">
				<fmt:message key="button.try.again" />
			</button>
		
			<button type="button" name="FinishButton" id="finishButton" onclick="finishSession()" class="btn btn-primary na">
				<fmt:message key="label.finished" />
			</button>
		</div>
	</div>
</lams:PageLearner>
