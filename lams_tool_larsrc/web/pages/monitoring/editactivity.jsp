<%@ include file="/common/taglibs.jsp"%>

<div class="datatablecontainer">
	<table class="forms">
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.basic.title" />
				:
			</td>
			<td class="formcontrol">
				<c:out value="${resource.title}" escapeXml="false" />
			</td>
		</tr>

		<tr>
			<td class="formlabel"> 
				<fmt:message key="label.authoring.basic.instruction" />
				:
			</td>
			<td class="formcontrol">
				<c:out value="${resource.instructions}" escapeXml="false" />
			</td>
		</tr>
		
		<tr>
			<td class="formlabel" colspan="2">
				<c:set var="isPageEditable" value="${isPageEditable}" />
				<c:choose>
					<c:when test='${isPageEditable == "true"}'>
						<c:url value="/authoring/init.do" var="authoringUrl">
							<c:param name="toolContentID" value="${toolContentID}" />
						</c:url>
						<html:link href="${authoringUrl}" styleClass="button" target="_blank">
							<fmt:message key="label.monitoring.edit.activity.edit" />
						</html:link>
					</c:when>
					<c:otherwise>
						<SPAN style="font-size: 12px;">
							<fmt:message key="message.monitoring.edit.activity.not.editable" />
						</SPAN>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
</div>
