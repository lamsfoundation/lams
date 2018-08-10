<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>  
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	</lams:head>
	<body class="stripes">
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
				<form:form action="finishActivity.do" modelAttribute="messageForm" method="post" onsubmit="disableFinishButton();" id="messageForm">
					<form:hidden path="sessionMapID" value="${sessionMapID}"/>
		
					<a href="#nogo" class="btn btn-primary voffset10 pull-right" id="finishButton" onclick="submitForm('finish')">
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
					</a>
				</form:form>
			</c:if>
		</lams:Page>
	</body>
</lams:html>

