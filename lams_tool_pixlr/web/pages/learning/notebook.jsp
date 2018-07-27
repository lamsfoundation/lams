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
				var f = document.getElementById('messageForm');
				f.submit();
			}

			$(document).ready(function() {
				document.getElementById("focused").focus();
			});
		</script>
	</lams:head>
	
	<body class="stripes">
		<lams:Page type="learner" title="${pixlrDTO.title}">
		
			<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				<html:hidden property="mode" value="${mode}" />
				
				<div class="panel">
					<lams:out value="${pixlrDTO.reflectInstructions}" escapeHtml="true"/>
				</div>
		
				<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"/>
		
				<html:hidden property="dispatch" value="submitReflection" />
				<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right" styleId="finishButton" onclick="submitForm('finish')">
					<span class="na">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</span>
				</html:link>
			</html:form>
		
			<div class="footer"></div>
				
		</lams:Page>
	</body>
</lams:html>
