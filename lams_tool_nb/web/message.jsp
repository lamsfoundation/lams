<%@ include file="/includes/taglibs.jsp"%>



<script type="text/javascript">
	
	function disableFinishButton() {
		var elem = document.getElementById("finishButton");
		elem.disabled = true;
	}
	 function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>


<div id="content">
	
	<h1>
		<fmt:message key="activity.title" />
	</h1>
	
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<p>
				<bean:write name="message" />
			</p>
		</html:messages>
	</logic:messagesPresent>
	
	<div class="align-right space-bottom-top">
		<html:form action="/learner" target="_self" onsubmit="disableFinishButton();" styleId="messageForm">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<html:hidden property="method" value="finish"/>
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<html:submit property="method" styleClass="button">
						<fmt:message key="button.continue" />
					</html:submit>
				</c:when>
				<c:otherwise>
					<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
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
					</html:link>
				</c:otherwise>
			</c:choose>
		</html:form>
	</div>

</div>
