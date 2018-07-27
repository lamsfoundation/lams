<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="kaltura" value="${sessionMap.kaltura}" />

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
    function submitForm(methodName) {
    	var f = document.getElementById('messageForm');
    	f.submit();
    }
</script>

<lams:Page type="learner" title="${kaltura.title}">

	<lams:Alert id="submission-deadline" type="danger" close="false">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
		</fmt:message>	
	</lams:Alert>
	
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="sessionMapID" value="${sessionMapID}"/>

			<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right" styleId="finishButton" onclick="submitForm('finish')">
				<span class="na">
					<c:choose>
		 				<c:when test="${sessionMap.activityPosition.last}">
		 					<fmt:message key="button.submit" />
		 				</c:when>
		 				<c:otherwise>
		 	 				<fmt:message key="button.finish" />
		 				</c:otherwise>
		 			</c:choose>
		 		</span>
			</html:link>
		</html:form>
	</c:if>
</lams:Page>

