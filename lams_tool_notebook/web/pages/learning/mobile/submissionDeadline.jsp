<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
    function submitForm(methodName){
    	var f = document.getElementById('messageForm');
    	f.submit();
    }
</script>

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<c:out value="${notebookDTO.title}" escapeXml="true"/>
	</h1>
</div>

<div data-role="content">
	<div class="warning">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${notebookDTO.submissionDeadline}" /></fmt:param>
		</fmt:message>	
	</div>
</div>

<div data-role="footer" data-theme="b" class="ui-bar">
	<span class="ui-finishbtn-right">
	
		<c:if test="${mode == 'learner' || mode == 'author'}">
			<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
				<html:hidden property="dispatch" value="finishActivity" />
				<html:hidden property="toolSessionID" />
	
				<a href="#nogo" id="finishButton" onclick="submitForm('finish')" data-role="button" data-icon="arrow-r" data-theme="b">
					<span class="nextActivity">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</span>
				</a>
			</html:form>
		</c:if>
		
	</span>
</div>
