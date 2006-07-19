<%@ include file="/includes/taglibs.jsp"%>

<table cellpadding="0">
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="instructions.onlineInstructions" />
		</td>
		<td>
			<c:out value="${formBean.onlineInstructions}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="instructions.offlineInstructions" />
		</td>
		<td>
			<c:out value="${formBean.offlineInstructions}" escapeXml="false" />
		</td>
	</tr>
</table>

<c:forEach var="attachment" items="${formBean.attachmentsList}" varStatus="status">
	<c:if test="${status.first}">
		<hr />

		<h2>
			<fmt:message key="label.attachments" />
		</h2>

		<table>
			<tr>
				<th>
					<fmt:message key="label.filename" />
				</th>
				<th>
					<fmt:message key="label.type" />
				</th>
				<th>
					&nbsp;
				</th>
				<th>
					&nbsp;
				</th>
			</tr>
	</c:if>
	
		<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid" />&preferDownload=false</bean:define>
		<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid" />&preferDownload=true</bean:define>
	
		<tr>
			<td>
				<c:out value="${attachment.filename}" />
			</td>
			<td>
				<c:choose>
					<c:when test="${attachment.onlineFile}">
					<fmt:message key="instructions.type.online" />
				</c:when>
				<c:otherwise>
					<fmt:message key="instructions.type.offline" />
				</c:otherwise>
				</c:choose>
			</td>
			<td>
				<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button"> <fmt:message key="link.view" /> </a>
			</td>
			<td>
				<html:link page="<%=download%>" styleClass="button">
					<fmt:message key="link.download" />
				</html:link>
			</td>
		</tr>
	<c:if test="${status.last}">
		</table>
	</c:if>
	</c:forEach>
