 <c:set var="errorKey" value="GLOBAL" />
        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
            <lams:Alert id="error" type="danger" close="false">
                <c:forEach var="error" items="${errorMap[errorKey]}">
                    <c:out value="${error}" />
                </c:forEach>
            </lams:Alert>
        </c:if>

<%-- Success Messages -
<logic:messagesPresent>
	<lams:Alert id="error" type="danger" close="false">
	        <form:errors path="*"/>
	</lams:Alert>
</logic:messagesPresent>
--%>

