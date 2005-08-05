<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<html:form action="/authoring" target="_self" enctype="multipart/form-data">
<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center">
		<c:if test="${NbAuthoringForm.defineLater != 'true'}">
			<html:submit property="method" styleClass="button">
				<fmt:message key="button.basic" />
			</html:submit>
			
			<html:submit property="method" disabled="true" styleClass="button">
				<fmt:message key="button.advanced" />
			</html:submit>
		
			<html:submit property="method" styleClass="button">
				<fmt:message key="button.instructions" />
			</html:submit>		
		</c:if>
	</td>
</tr>
</table>
</div>

<%@ include file="errorbox.jsp" %>

<div id="datatablecontainer">
<c:choose>
        <c:when test='${NbAuthoringForm.method == "Advanced"}'>
           <%@ include file="a_Advanced.jsp" %>
        </c:when>
         <c:when test='${NbAuthoringForm.method == "Instructions"}'>
           <%@ include file="a_Instructions.jsp" %>
        </c:when>
        <c:otherwise> 
      	   <%@ include file="a_Basic.jsp" %>
        </c:otherwise>
</c:choose>
</div> 


<html:hidden property="content" />
<html:hidden property="title" />
<html:hidden property="onlineInstructions" />
<html:hidden property="offlineInstructions" />



</html:form>