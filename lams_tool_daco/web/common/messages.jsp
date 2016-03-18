<%-- Error Messages --%>
<logic:messagesPresent>
		<lams:Alert id="errorMessages" type="danger" close="false">
        <html:messages id="error">
            <c:out value="${error}" escapeXml="false"/><BR/>
        </html:messages>
    </lams:Alert>
</logic:messagesPresent>
