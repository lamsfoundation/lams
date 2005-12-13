<%@ include file="/includes/taglibs.jsp" %>

<html:form action="/monitoring/editActivity" method="post" >
	<div id="basic">
	<h1><fmt:message key="label.authoring.heading.basic" /></h1>
	<h1><fmt:message key="label.authoring.heading.basic.desc" /></h1>
	<table class="forms">
		<!-- Title Row -->
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
			<td class="formcontrol">
				<c:out value="${title}" escapeXml="false"/>
			</td>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
			<td class="formcontrol">
				<c:out value="${instruction}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
				  <td class="formlabel" colspan="2">
				  	<html:errors property="activity.globel"/>
				  </td>
		</tr>
		<tr><td colspan="2">
			<html:submit>
				<fmt:message key="label.monitoring.edit.activity.edit"/>
			</html:submit>
		</td></tr>
	</table>
	</div>
</html:form>
