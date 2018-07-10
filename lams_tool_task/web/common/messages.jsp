<c:set var="hasErrors">
<form:errors path='*'/>
</c:set>
<c:if test="${not empty hasErrors}">
	<lams:Alert id="error" type="danger" close="false">
		<form:errors path="*"/>
	</lams:Alert>
</c:if>

<%-- Success Messages -
<logic:messagesPresent>
	<lams:Alert id="error" type="danger" close="false">
	        <form:errors path="*"/>
	</lams:Alert>
</logic:messagesPresent>
--%>

