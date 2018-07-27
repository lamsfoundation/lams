<%-- Error Messages --%>
<logic:messagesPresent>
    <div class="error" id="errorMessages">
       	<lams:Alert id="errorMessages" type="danger" close="false">
	        <html:messages id="error">
            	<c:out value="${error}" escapeXml="false"/><br/>
 	       </html:messages>
        </lams:Alert>
   </div>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
    <div class="message" id="successMessages">
       	<lams:Alert id="errorMessages" type="info" close="false">
	        <html:messages id="message" message="true">
            	<c:out value="${message}" escapeXml="false"/><br/>
    	    </html:messages>
	    </lams:Alert>
    </div>
</logic:messagesPresent>