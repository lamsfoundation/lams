<%@ include file="/common/taglibs.jsp" %>

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
				<c:set var="isPageEditable" value="${isPageEditable}" />
				<c:choose>
					<c:when test='${isPageEditable == "true"}'>
						<c:url value="/authoring.do" var="authoringUrl">
							<c:param name="mode" value="teacher" />
							<c:param name="toolContentID" value="${sessionScope.toolContentID}" />
						</c:url>
						<html:link href="${authoringUrl}" styleClass="button" target="_blank">
							<fmt:message key="label.monitoring.edit.activity.edit" />
						</html:link>
					</c:when>
					<c:otherwise>
						<div align="center"><B>
							<fmt:message key="message.monitoring.edit.activity.not.editable" />
						</B></div>
					</c:otherwise>
				</c:choose>
			</td></tr>
		</table>
	</div>