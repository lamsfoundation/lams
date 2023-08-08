<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> 	<lams:WebAppURL />	</c:set>
	
	<lams:head>  
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
	
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<lams:JSImport src="includes/javascript/common.js" />
		
		<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		function submitForm(methodName){
		var f = document.getElementById('learningForm');
		f.submit();
	}
</script>
	</lams:head>

	<body class="stripes">
		<lams:Page type="learner" title="${mindmapDTO.title}" formID="learningForm">
	
		<lams:Alert id="deadline" type="danger" close="false">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
			</fmt:message>
		</lams:Alert>
	
		<c:if test="${mode == 'learner' || mode == 'author'}">
			<form:form action="finishActivity.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="learningForm">
				<form:hidden path="toolSessionID" />
	
				<div class="voffset10 pull-right">
					<a href="#nogo" class="btn btn-primary na" id="finishButton" onclick="submitForm('finish')">
						<span class="na">
							<c:choose>
								<c:when test="${isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
						</span>
					</a>
				</div>
			</form:form>
		</c:if>
		
	</lams:Page>

		<div class="footer">
		</div>					
	</body>
</lams:html>