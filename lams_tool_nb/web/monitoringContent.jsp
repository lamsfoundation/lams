<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<html:form action="/tool/nb/monitoring" target="_self">
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
           <%@ include file="nbActivityInstructions.jsp" %>
        </c:when>
         <c:when test='${NbMonitoringForm.method == "Edit Activity"}'>
           <%@ include file="nbActivityContent.jsp" %>
        </c:when>
        <c:when test='${NbMonitoringForm.method == "Statistics"}'>
           <%@ include file="nbStatistics.jsp" %>
        </c:when>
        <c:otherwise> 
      	   <%@ include file="nbSummary.jsp" %>
        </c:otherwise>
</c:choose>
</div>
</html:form>