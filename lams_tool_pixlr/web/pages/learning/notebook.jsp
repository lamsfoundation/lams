<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="pageTitle.monitoring.notebook"/>
		</title>
		<%@ include file="/common/learnerheader.jsp"%>
		
		<script type="text/javascript">
			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
			
			function submitForm(methodName){
				var f = document.getElementById('learningForm');
				f.submit();
			}

			$(document).ready(function() {
				document.getElementById("focused").focus();
			});
		</script>
	</lams:head>
	
	<body class="stripes">
		<lams:Page type="learner" title="${pixlrDTO.title}">
		
			<form:form action="submitReflection.do" modelAttribute="learningForm" method="post" onsubmit="disableFinishButton();" id="learningForm">
				<form:hidden path="toolSessionID" id="toolSessionID"/>
				<form:hidden path="mode" value="${mode}" />
				
				<div class="panel">
					<lams:out value="${pixlrDTO.reflectInstructions}" escapeHtml="true"/>
				</div>
		
				<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control"/>
		
				<div class="activity-bottom-buttons">
					<a href="#nogo" class="btn btn-primary" id="finishButton" onclick="submitForm('finish')">
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
		
			<div class="footer"></div>
				
		</lams:Page>
	</body>
</lams:html>
