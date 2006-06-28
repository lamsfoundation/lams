<%@ include file="/includes/taglibs.jsp"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<c:choose>
		<c:when test='${requestScope.isPageEditable == "true"}'>
			<tr>
				<td class="field-name">
					<fmt:message key="basic.title" />
				</td>
				<td>
					<c:out value="${sessionScope.title}" escapeXml="false" />
				</td>
			</tr>

			<tr>
				<td class="field-name">
					<fmt:message key="basic.content" />
				</td>
				<td>
					<c:out value="${sessionScope.content}" escapeXml="false" />
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<td>
					<p class="warning">
						<fmt:message key="message.contentInUseSet" />
					</p>
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>

<c:if test='${requestScope.isPageEditable == "true"}'>
	<p align="right">
		<html:link forward="forwardToAuthorPage" name="NbMonitoringForm" property="parametersToAppend" styleClass="button" target="_blank">
			<fmt:message key="button.edit" />
		</html:link>
	</p>
</c:if>
