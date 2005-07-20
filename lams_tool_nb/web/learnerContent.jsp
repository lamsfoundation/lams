<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>


<html:form action="/learner" target="_self" >
<hr>
<div id="datatablecontainer">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="center">
			<c:out value="${NbLearnerForm.title}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="left">
				<c:out value="${NbLearnerForm.content}" escapeXml="false"/>
			</td>
		</tr>
	</table>
</div>

<div id="formtablecontainer">
<c:if test="${sessionScope.readOnlyMode != 'true'}" >
<table width="100%">
<tr>
	<td align="right">
		<html:submit property="method"><fmt:message key="button.finish"/></html:submit>
	</td>	
</tr>
</table>
</c:if>
</div>
<hr>
</html:form>
