<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>

<%-- To show the GLOBAL messages (default case) use no parameters --%>
<%-- To show the all messages, ignoring their keys (e.g. Change Password) use path="*" --%>
<%-- To show the messages for a particular item use path="itemName" --%>

<c:choose>

<%-- Show Messages for GLOBAL. Usual alert box --%>
<c:when test="${empty path}">
   	<c:set var="apath" value="GLOBAL" />
	<c:if test="${not empty errorMap and not empty errorMap[apath]}"> 
   	<lams:Alert id="error" type="danger" close="false"> 
       <c:forEach var="error" items="${errorMap[apath]}"> 
       	<c:out value="${error}" /><br /> 
       </c:forEach> 
	</lams:Alert> 
	</c:if>
</c:when>

<%-- Show All Messages --%>
<c:when test="${path eq '*'}">
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
<c:otherwise>
	<c:if test="${not empty errorMap and not empty errorMap[path]}">
		<span class="text-danger">
		<c:forEach var="error" items="${errorMap[path]}"> 
	       	<c:out value="${error}" /><br /> 
        </c:forEach> 
        </span>
    </c:if>
</c:otherwise>

</c:choose>
