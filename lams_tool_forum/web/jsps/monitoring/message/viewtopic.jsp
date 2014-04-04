<%@ include file="/common/taglibs.jsp"%>

<table class="forum" cellspacing="0">
	<tbody>
		<tr>
			<th class="first" scope="col">
				<c:out value="${topic.message.subject}" />
			</th>

		</tr>

		<tr>
			<td class="first posted-by">
				<fmt:message key="lable.topic.subject.by" />
				<c:set var="author" value="${topic.author}"/>
				<c:if test="${empty author}">
					<c:set var="author">
						<fmt:message key="label.default.user.name"/>
					</c:set>
				</c:if>
				<c:out value="${author}" escapeXml="true"/>						
				
				-
				<lams:Date value="${topic.message.updated}"/>

			</td>
		</tr>
		<tr>
			<td class="first">
				<c:out value="${topic.message.body}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<td class="align-right">
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
				</c:forEach>
			</td>
		</tr>
	</tbody>
</table>


<table cellpadding="0">
	<tr>
		<td>
			<a href="javascript:window.close();" class="button"><fmt:message key="button.close"/></a>
		</td>
	</tr>
</table>


