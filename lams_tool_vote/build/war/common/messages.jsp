<%-- Error Messages --%>
<logic:messagesPresent>
	<lams:Alert id="error" close="false" type="danger">
		<html:messages id="error">
			<c:out value="${error}" escapeXml="false" />
		</html:messages>
	</lams:Alert>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
	<lams:Alert id="message" close="false" type="info">
		<html:messages id="message" message="true">
			<c:out value="${message}" escapeXml="false" />
		</html:messages>
	</lams:Alert>
</logic:messagesPresent>