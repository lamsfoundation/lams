<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<%@ include file="/common/mobileheader.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}
	         function submitForm(methodName){
	                var f = document.getElementById('messageForm');
	                f.submit();
	        }
	</script>
</lams:head>

<body>

	<html:form action="/learning?method=displayMc&validate=false"
		method="POST" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolContentID" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="userID" />

		<div data-role="page" data-cache="false">
		
			<div data-role="header" data-theme="b" data-nobackbtn="true">
				<h1>
					<fmt:message key="activity.title" />
				</h1>
			</div>
	
			<div data-role="content">
				<div class="warning">
					<fmt:message key="authoring.info.teacher.set.restriction" >
						<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
					</fmt:message>							
				</div>	
			</div>	
								
			<div data-role="footer" data-theme="b" class="ui-bar">
				<span class="ui-finishbtn-right">

					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
						<html:hidden property="learnerFinished" value="Finished" />
						
						<a href="#nogo" id="finishButton" onclick="javascript:submitForm('finish');return false"  data-role="button" data-icon="arrow-r">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${activityPosition.last}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
					 			</c:choose>
					 		</span>
						</a>
					</c:if>
	
					<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
						<button type="submit" name="forwardtoReflection" data-icon="arrow-r">
							<fmt:message key="label.continue" />
						</button>
					</c:if>
				</span>
			</div>
			
			
		</div>
	</html:form>
</body>
</lams:html>








