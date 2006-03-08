<div id="topicview">

	<br />
	<c:set var="mode" value="${sessionScope.mode}" />
	<c:forEach var="msgDto" items="${topicThread}">
		<div id="datatablecontainer">
			<c:set var="indentSize" value="${msgDto.level*3}" />
			<c:set var="hidden" value="${msgDto.message.hideFlag}" />
			<div style="margin-left:<c:out value="${indentSize}"/>em;">
				<table class="form" border="0" cellspacing="0">
					<tr>
						<th valign="middle" scope="col">
							<c:choose>
								<c:when test='${(mode == "teacher") || (not hidden)}'>
									<b>
										<c:out value="${msgDto.message.subject}" />
									</b>
								</c:when>
								<c:otherwise>
									<fmt:message key="topic.message.subject.hidden" />
								</c:otherwise>
							</c:choose>
						</th>
					</tr>
					<tr>
						<th scope="col">
							<fmt:message key="lable.topic.subject.by" />
							<c:out value="${msgDto.author}" />
							-
							<fmt:formatDate value="${msgDto.message.created}" type="time" timeStyle="short" />
							<fmt:formatDate value="${msgDto.message.created}" type="date" dateStyle="full" />
						</th>
					</tr>
					<tr>
						<td>
							<br>
							<c:if test='${(not hidden) || (hidden && mode == "teacher")}'>
								<c:out value="${msgDto.message.body}" escapeXml="false" />
							</c:if>
							<c:if test='${hidden}'>
								<br />
								<b>
									<fmt:message key="topic.message.body.hidden" />
								</b>
							</c:if>
						</td>
					</tr>
					<tr>
						<td>
							<br />
							<c:forEach var="file" items="${msgDto.message.attachments}">
								<c:set var="downloadURL">
									<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
								</c:set>
								<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<c:out value="${file.fileName}" />
								</a>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td align="right">
							<br />
							<!--  Hide/Unhide Button -->
							<c:if test='${mode == "teacher"}'>
								<!--  call the hide action -->
								<c:choose>
									<c:when test="${hidden}">
										<!--  display a show link  -->
										<c:set var="hidetopic">
											<html:rewrite page="/learning/updateMessageHideFlag.do?msgId=${msgDto.message.uid}&hideFlag=false" />
										</c:set>
										<html:link href="${hidetopic}">
											<b>
												<fmt:message key="label.show" />
											</b>
										</html:link>
									</c:when>
									<c:otherwise>
										<!--  display a hide link -->
										<c:set var="hidetopic">
											<html:rewrite page="/learning/updateMessageHideFlag.do?msgId=${msgDto.message.uid}&hideFlag=true" />
										</c:set>
										<html:link href="${hidetopic}">
											<b>
												<fmt:message key="label.hide" />
											</b>
										</html:link>
									</c:otherwise>
								</c:choose>
							</c:if>

							<!--  Edit Button -->
							<c:if test='${(mode == "teacher") || (msgDto.isAuthor && not finishedLock && sessionScope.allowEdit)}'>
								<c:set var="edittopic">
									<html:rewrite page="/learning/editTopic.do?topicId=${msgDto.message.uid}&create=${msgDto.message.created.time}" />
								</c:set>
								<html:link href="${edittopic}">
									<b>
										<fmt:message key="label.edit" />
									</b>
								</html:link>
							</c:if>

							<!--  Reply Button -->
							<c:if test="${not finishedLock}">
								<c:set var="replytopic">
									<html:rewrite page="/learning/newReplyTopic.do?parentId=${msgDto.message.uid}" />
								</c:set>
								<html:link href="${replytopic}">
									<b>
										<fmt:message key="label.reply" />
									</b>
								</html:link>
							</c:if>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</c:forEach>
</div>
