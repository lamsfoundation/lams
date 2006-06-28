<%@ include file="/includes/taglibs.jsp"%>

<!-- Instructions Tab Content  -->

<table>
	<tr>
		<td>
			<lams:SetEditor id="onlineInstructions" text="${NbAuthoringForm.onlineInstructions}" key="instructions.onlineInstructions" />
		</td>
	</tr>

	<tr>
		<td>
			<html:file property="onlineFile" />
			<html:submit property="method">
				<fmt:message key="button.upload" />
			</html:submit>
		</td>
	</tr>
</table>

<hr />

<table>
	<tr>
		<td >
			<lams:SetEditor id="offlineInstructions" text="${NbAuthoringForm.onlineInstructions}" key="instructions.onlineInstructions" alt="true" />
		</td>
	</tr>

	<tr>
		<td>
			<html:file property="offlineFile" />
			<html:submit property="method">
				<fmt:message key="button.upload" />
			</html:submit>
		</td>
	</tr>
</table>

<logic:present name="attachmentList">
	<bean:size id="count" name="attachmentList" />
	<logic:notEqual name="count" value="0">
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
			</tr>
			<logic:iterate name="attachmentList" id="attachment">
				<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid" />&preferDownload=false</bean:define>
				<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid" />&preferDownload=true</bean:define>
				<bean:define id="uuid" name="attachment" property="uuid" />

				<tr>
					<td>
						<bean:write name="attachment" property="filename" />
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

						<html:link page="<%=download%>" styleClass="button">
							<fmt:message key="link.download" />
						</html:link>
						<html:link page="/authoring.do?method=Delete" paramId="uuid" paramName="attachment" paramProperty="uuid" onclick="javascript:return confirm('Are you sure you want to delete this file?')" target="_self" styleClass="button">
							<fmt:message key="link.delete" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
</logic:present>
