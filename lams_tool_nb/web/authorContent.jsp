<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<html:form action="/authoring" target="_self" >
<div id="datatablecontainer">
<table width="100%" align="center">
<tr>
	<td align="center">
	<html:submit property="method" styleClass="button">
	
				<fmt:message key="button.basic" />
		</html:submit>
	
		<html:submit property="method" disabled="true" styleClass="button">
			<fmt:message key="button.advanced" />
		</html:submit>
	
		<html:submit property="method" styleClass="button">
			<fmt:message key="button.instructions" />
		</html:submit>
	</td>
</tr>
</table>

<%@ include file="" %>

<div id="datatablecontainer">
<table width="100%" align="center">
<c:if test="${NbAuthoringForm.method == 'Basic'}" >
<tr>
	<td align="center"><html:button property="cancel" onclick="window.close()" styleClass="button"><fmt:message key="button.cancel"/></html:button>
	<html:submit property="method" styleClass="button"><fmt:message key="button.ok" /></html:submit>
	</td>
</tr>
</c:if>
</table>
</div>