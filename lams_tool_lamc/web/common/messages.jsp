<%@include file="/common/taglibs.jsp"%>

<%-- Error Messages --%>
 <c:set var="errorKey" value="GLOBAL" />
        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
            <lams:Alert id="error" type="danger" close="false">
                <c:forEach var="error" items="${errorMap[errorKey]}">
                    <c:out value="${error}" />
                </c:forEach>
            </lams:Alert>
        </c:if>
        
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