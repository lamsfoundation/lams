<%@ include file="/includes/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		var elem = document.getElementById("method");
		// TODO, disable the button
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
		<html:form action="/learner" target="_self" onsubmit="disableFinishButton();">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<html:submit property="method" styleClass="button">
						<fmt:message key="button.continue" />
					</html:submit>
				</c:when>
				<c:otherwise>
					<html:submit property="method" styleClass="button" styleId="method">
						<fmt:message key="button.finish" />
					</html:submit>
				</c:otherwise>
			</c:choose>
		</html:form>
	</div>

</div>
