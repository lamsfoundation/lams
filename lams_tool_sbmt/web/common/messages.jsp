<%@include file="/common/taglibs.jsp"%>

<%-- Error Messages --%>
<logic:messagesPresent>
	<lams:Alert id="errors" type="danger" close="true">
	        <html:messages id="error">
	            <c:out value="${error}" escapeXml="false"/><br/>
	        </html:messages>
	</lams:Alert>
</logic:messagesPresent>