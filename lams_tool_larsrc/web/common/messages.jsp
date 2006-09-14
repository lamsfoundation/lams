<%-- Error Messages --%>
<logic:messagesPresent>
	<p>&nbsp;</p>
	<p class="warning">
	        <html:messages id="error">
	            <c:out value="${error}" escapeXml="false"/><br/>
	        </html:messages>
	</p>
</logic:messagesPresent>