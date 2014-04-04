<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="indentSize" value="${msgDto.level*3}" />
	<div style="margin-left:<c:out value="${indentSize}"/>em;">
		<table cellspacing="0" class="forum">
			<tr>
				<th class="first">
					<b> <c:out value="${msgDto.message.subject}" /> </b>
				</th>
			</tr>
			<tr>
				<td class="first posted-by">
					<fmt:message key="lable.topic.subject.by" />
							<c:set var="author" value="${msgDto.author}"/>
							<c:if test="${empty author}">
								<c:set var="author">
									<fmt:message key="label.default.user.name"/>
								</c:set>
							</c:if>
							<c:out value="${author}" escapeXml="true"/>						
					
							-
							<lams:Date value="${msgDto.message.updated}"/>
				</td>
			</tr>
			<tr>
				<td>
					<c:out value="${msgDto.message.body}" escapeXml="false" />
				</td>
			</tr>

			<c:if test="${not empty msgDto.message.attachments}">
				<tr>
					<td>
						<c:forEach var="file" items="${msgDto.message.attachments}">
							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
							</c:set>
							<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
						</c:forEach>
					</td>
				</tr>
			</c:if>
			<c:if test="${not empty msgDto.mark}">
				<tr>
					<td>
						<span class="field-name" ><fmt:message key="lable.topic.title.mark"/></span>
						<BR>
							${msgDto.mark}
					</td>
				</tr>
				<tr>
					<td>
						<span class="field-name" ><fmt:message key="lable.topic.title.comment"/></span>
						<BR>
						<c:choose>
							<c:when test="${empty msgDto.comment}">
								<fmt:message key="message.not.avaliable"/>
							</c:when>
							<c:otherwise>
								<c:out value="${msgDto.comment}" escapeXml="false" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:if>
		</table>
	</div>
</c:forEach>

<table cellpadding="0">
	<tr>
		<td>
			<a href="javascript:window.close();" class="button"><fmt:message key="button.close"/></a>
		</td>
	</tr>
</table>
