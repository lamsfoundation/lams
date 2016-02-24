<%-- Error Messages --%>
<logic:messagesPresent>
		<lams:Alert id="errorMessages" type="danger" close="false">
        <html:messages id="error">
            <c:out value="${error}" escapeXml="false"/>
        </html:messages>
    </lams:Alert>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
    <lams:Alert id="message" type="info" close="true">
        <html:messages id="message" message="true">
             <c:out value="${message}" escapeXml="false"/>
        </html:messages>
    </lams:Alert>
</logic:messagesPresent>