<%-- Error Messages --%>
 <c:set var="errorKey" value="GLOBAL" /> 
 <c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
     <lams:Alert id="error" type="danger" close="false"> 
         <c:forEach var="error" items="${errorMap[errorKey]}"> 
             <c:out value="${error}" /><br /> 
         </c:forEach> 
     </lams:Alert> 
</c:if>

<%-- Success Messages --%>
 <c:set var="infokey" value="GLOBALINFO" /> 
 <c:if test="${not empty infoMap and not empty infoMap[infokey]}"> 
     <lams:Alert type="info"> 
         <c:forEach var="info" items="${infoMap[infokey]}"> 
             <c:out value="${info}" /><br /> 
         </c:forEach> 
     </lams:Alert> 
</c:if>
