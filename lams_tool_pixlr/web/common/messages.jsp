<%-- Error Messages --%>
<logic:messagesPresent>
    <lams:Alert id="errorMessages" type="danger" close="false">
        <html:messages id="error">
            &nbsp&nbsp <html:img page="/images/iconWarning.gif" 
               styleClass="icon"/>
            &nbsp&nbsp <c:out value="${error}" escapeXml="false"/><br/>
        </html:messages>
    </lams:Alert>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
	<lams:Alert id="successMessages" type="info" close="false">
        <html:messages id="message" message="true">
            &nbsp&nbsp <html:img page="/images/iconInformation.gif" styleClass="icon"/>
            &nbsp&nbsp <c:out value="${message}" escapeXml="false"/><br/>
        </html:messages>
	</lams:Alert>
</logic:messagesPresent>
