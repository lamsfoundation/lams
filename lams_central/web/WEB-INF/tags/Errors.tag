<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ attribute name="showAll" required="false" rtexprvalue="true"%>
<%@ attribute name="errorKey" required="false" rtexprvalue="true"%>

<%-- To show the GLOBAL messages (default case) use no parameters --%>
<%-- To show the all messages, ignoring their keys (e.g. Change Password) use showAll="true" --%>
<%-- To show the messages for a particular key use errorKey="theKey" --%>

<%-- Show All Messages --%>
<c:choose>
<c:when test="${showAll}">
	<c:if test="${not empty errorMap}"> 
   	<lams:Alert id="error" type="danger" close="false"> 
       <c:forEach var="error" items="${errorMap}"> 
       <c:forEach var="erorString" items="${error.value}"> 
       	<c:out value="${erorString}" /><br /> 
       </c:forEach> 
       </c:forEach> 
	</lams:Alert> 
	</c:if>
</c:when>

<%-- Show messages for a particular key --%>
<c:when test="${not empty errorKey}">
	<c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
		<span class="text-danger">
		<c:forEach var="error" items="${errorMap[errorKey]}"> 
	       	<c:out value="${error}" /><br /> 
        </c:forEach> 
        </span>
    </c:if>
</c:when>

<%-- Show Messages for GLOBAL. Usual alert box --%>
<c:otherwise>
   	<c:set var="errorKeyisGlobal" value="GLOBAL" />
	<c:if test="${not empty errorMap and not empty errorMap[errorKeyisGlobal]}"> 
   	<lams:Alert id="error" type="danger" close="false"> 
       <c:forEach var="error" items="${errorMap[errorKeyisGlobal]}"> 
       	<c:out value="${error}" /><br /> 
       </c:forEach> 
	</lams:Alert> 
	</c:if>
</c:otherwise>
</c:choose>
