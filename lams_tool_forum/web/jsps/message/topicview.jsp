<div align="center">
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>
<script type="text/javascript">
	function success(){
		var flag = "<c:out value="${SUCCESS_FLAG}"/>";
		if(flag == "DELETE_SUCCESS"){
			closeWin();
		}
	}
	success();
</script>

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
			<td>
				<br>
				<c:out value="${topic.bodyByHtml}" escapeXml="false" />
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
				<c:set var="edittopic">
					<html:rewrite page="/authoring/editTopic.do?topicIndex=${topicIndex}&create=${topic.message.created.time}" />
				</c:set> 
				<html:link href="${edittopic}">
					<b><fmt:message key="label.edit" /></b>
				</html:link>
				
				<c:set var="deletetopic">
					<html:rewrite page="/authoring/deleteTopic.do?topicIndex=${topicIndex}" />
				</c:set> 
				<html:link href="${deletetopic}">
					<b><fmt:message key="label.delete" /></b>
				</html:link>
			</td>
		</tr>
	</table>
	</div>
</div>
</div>
