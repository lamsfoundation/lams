<div id="topicview">
	<div id="datatablecontainer">
	<BR>
	<table width="100%" align="CENTER" class="form" border="0" cellspacing="0">
		<tr>
			<th valign="MIDDLE" scope="col" >
				<span style="font-size:25px"><c:out value="${topic.message.subject}" /></span>
			</th>
		</tr>
		<tr>
			<th scope="col" >
				<fmt:message key="lable.topic.subject.by"/> <c:out value="${topic.author}"/> - 
				<fmt:formatDate value="${topic.message.created}" type="time" timeStyle="short" />
				<fmt:formatDate value="${topic.message.created}" type="date" dateStyle="full" />
			</th>
		</tr>
		<tr>
			<td align="left">
				<br>
				<c:out value="${topic.message.body}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td align="left">
				<br>
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true"/>
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
							<c:out value="${file.fileName}" />
						</a>
				</c:forEach>
			</td>
		</tr>
	</table>
	</div>
</div>
</div>
