<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
	<div id="basic">
		<h1><bean:message key="label.authoring.heading.basic.desc" /></h1>
		<table class="forms">
			<!--hidden field contentID passed by flash-->
			<input type="hidden" name="toolContentID" value="<c:out value='${sessionScope.toolContentID}'/>"/>
			<!-- Title Row -->
			<tr>
				<td class="formlabel"><bean:message key="label.authoring.basic.title" />:</td>
				<td class="formcontrol">
					<c:out value="${authoring.title}" escapeXml="false"/>
				</td>
			</tr>
			<!-- Instructions Row -->
			<tr>
				<td class="formlabel"><bean:message key="label.authoring.basic.instruction" />:</td>
				<td class="formcontrol">
					<c:out value="${authoring.instruction}" escapeXml="false"/>
				</td>
			</tr>
			<tr><td colspan="2">
				<html:link href="javascript:doSubmit('editActivity');" property="editActivity" styleClass="button">
						<bean:message key="label.monitoring.edit.activity.edit" />
				</html:link>
			</td></tr>
		</table>
	</div>