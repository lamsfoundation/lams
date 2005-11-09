<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<html:form action="/monitoring" target="_self">
<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center">
		<html:submit property="method" styleClass="button">	
				<fmt:message key="button.summary" />
		</html:submit>
	
		<html:submit property="method" styleClass="button">
			<fmt:message key="button.instructions" />
		</html:submit>
	
		<html:submit property="method" styleClass="button">
			<fmt:message key="button.editActivity" />
		</html:submit>
		
		<html:submit property="method" styleClass="button">
			<fmt:message key="button.statistics" />
		</html:submit>
	</td>
</tr>
</table>
</div>

<div id="datatablecontainer">
<c:choose>
        <c:when test='${NbMonitoringForm.method == "Instructions"}'>
           <%@ include file="m_Instructions.jsp" %>
        </c:when>
         <c:when test='${NbMonitoringForm.method == "Edit Activity"}'>
           <%@ include file="m_EditActivity.jsp" %>
        </c:when>
        <c:when test='${NbMonitoringForm.method == "Statistics"}'>
           <%@ include file="m_Statistics.jsp" %>
        </c:when>
        <c:otherwise> 
      	   <%@ include file="m_Summary.jsp" %>
        </c:otherwise>
</c:choose>
</div>
</html:form>