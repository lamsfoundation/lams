<div id="topicview">

	<BR>
	<c:forEach var="topic" items="${topicThread}">
		<div id="datatablecontainer">
		<c:set var="indentSize" value="${topic.level*3}"/>
		<div style="margin-left:<c:out value="${indentSize}"/>em;">
		<table class="form" border="0" cellspacing="0">
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
				<td>
					<br>
					<c:out value="${topic.message.body}" escapeXml="false" />
				</td>
			</tr>
			<tr>
				<td>
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
			<tr>
				<td align="right">
					<br>
					<c:if test="${sessionScope.allowEdit}">
						<c:set var="edittopic">
							<html:rewrite page="/learning/editTopic.do?topicId=${topic.message.uid}&create=${topic.message.created.time}" />
						</c:set> 
						<html:link href="${edittopic}">
							<b><fmt:message key="label.edit" /></b>
						</html:link>
					</c:if>
					<c:set var="replytopic">
						<html:rewrite page="/learning/newReplyTopic.do?parentId=${topic.message.uid}" />
					</c:set> 
					<html:link href="${replytopic}">
						<b><fmt:message key="label.reply" /></b>
					</html:link>
				</td>
			</tr>
		</table>
		</div>
		</div>
	</c:forEach>
</div>
