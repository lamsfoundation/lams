<table width="90%" cellspacing="8" align="CENTER">
	<tr>
		<td valign="MIDDLE">
			<b><bean:message key="message.label.subject" /><b class="required">*</b></b>
		</td>
		<td valign="MIDDLE" align="left">
			<html:text size="30" tabindex="1" property="message.subject" /><br>
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td valign="MIDDLE">
			<b><bean:message key="message.label.body" /></b><b class="required">*</b>
		</td>
		<td valign="MIDDLE" align="left">
			<%@include file="bodyarea.jsp"%>
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="message.label.attachment" /></b>
		</td>
		<td valign="MIDDLE" align="left">
			<c:if test="${topic.hasAttachment}">
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
					<c:set var="deleteURL">
						<html:rewrite page="/learning/deleteAttachment.do?uuid=${file.fileUuid}&versionID=${file.fileVersionId}" />
					</c:set>
					&nbsp;
					<a href="<c:out value='${deleteURL}'/>"> <fmt:message key="label.delete" /> </a>
				</c:forEach>
			</c:if>
			<c:if test="${not topic.hasAttachment}">
				<html:file tabindex="3" property="attachmentFile" />
				<html:errors property="message.attachment" />
			</c:if>
		</td>
	</tr>
	<tr>
		<td></td>
		<td align="left">
			<BR>
			<html:submit styleClass="buttonStyle" style="width:120px">
				<bean:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite page="/learning/viewTopic.do?topicId=${rootUid}&create=${topic.message.created.time}" />
			</c:set> 			
			<html:button property="goback" onclick="javascript:location.href='${backToTopic}';" styleClass="buttonStyle" style="width:120px">
				<bean:message key="button.cancel" />
			</html:button>
		</td>
	</tr>
</table>
