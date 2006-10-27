<%@ include file="/includes/taglibs.jsp"%>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true">
		<p class="warning">
			<bean:write name="message" />
		</p>
	</html:messages>
</logic:messagesPresent>

<p align="right">
	<html:form action="/learner" target="_self">
		<html:hidden property="toolSessionID" />
		<html:hidden property="mode" />
		<html:submit property="method" styleClass="button">
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<fmt:message key="button.continue" />
				</c:when>
				<c:otherwise>
					<fmt:message key="button.finish" />
				</c:otherwise>
			</c:choose>
		</html:submit>
	</html:form>
</p>
