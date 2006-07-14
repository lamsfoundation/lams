<%@ include file="/includes/taglibs.jsp"%>

	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<p class="warning"><bean:write name="message" /></p>
		</html:messages>
	</logic:messagesPresent>

	<p align="right">
		<html:button property="closeWindow" onclick="window.close()" styleClass="button">
				<fmt:message key="button.next" />
		</html:button>
	</p>
