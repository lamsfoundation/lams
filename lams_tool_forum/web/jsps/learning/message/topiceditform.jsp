
<table width="80%" cellspacing="8" align="CENTER" class="forms">
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.subject" /></b></td>
		<td valign="MIDDLE"><b class="required">*</b><html:text size="30"
			 tabindex="1" property="message.subject" /> 
			 <html:errors property="message.subject" /></td>
	</tr>
	<tr>
		<td valign="MIDDLE"><b><bean:message key="message.label.body" /></b></td>
		<td valign="MIDDLE"><b class="required">*</b><html:textarea rows="10" cols="60"
			tabindex="2" property="message.body" /> 
			<BR><html:errors property="message.body" /></td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="message.label.attachment" /></b>
		</td>
		<td valign="MIDDLE">&nbsp;
			<c:if test="${topic.hasAttachment}">
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true"/>
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
						<c:out value="${file.fileName}" />
					</a>
					<c:set var="deleteURL">
							<html:rewrite page="/learning/deleteAttachment.do?uuid=${file.fileUuid}&versionID=${file.fileVersionId}"/>
					</c:set>
					&nbsp;
					<a href="<c:out value='${deleteURL}'/>">
						<fmt:message key="label.delete" />
					</a>					
				</c:forEach>
			</c:if>
			<c:if test="${not topic.hasAttachment}">
				<html:file tabindex="3"		property="attachmentFile" /> <html:errors	property="message.attachment" />
			</c:if>
		</td>
	</tr>
	<tr>
		<td>&nbsp;&nbsp;
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
		</td>
		<td>
		</td>
	</tr>
</table>
