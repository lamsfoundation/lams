<%-- Error Messages --%>
<logic:messagesPresent>
    <div class="error" id="errorMessages">
        <html:messages id="error">
            &nbsp&nbsp <html:img page="/images/iconWarning.gif" 
               styleClass="icon"/>
            &nbsp&nbsp <c:out value="${error}" escapeXml="false"/><br/>
        </html:messages>
    </div>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
    <div class="message" id="successMessages">
        <html:messages id="message" message="true">
            &nbsp&nbsp <html:img page="/images/iconInformation.gif" styleClass="icon"/>
            &nbsp&nbsp <c:out value="${message}" escapeXml="false"/><br/>
        </html:messages>
    </div>
</logic:messagesPresent>