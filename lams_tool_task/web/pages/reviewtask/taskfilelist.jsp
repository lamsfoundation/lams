<div>
	<c:set var="fileList" value="${sessionMap.taskListItemAttachmentList}" />
	 
	<c:if test="${(fn:length(fileList) != 0)}">
		<div class="field-name">
			<fmt:message key="label.preview.filelist" />
		</div>
	</c:if>
							
	<%-- Display target file type --%>
	<ul>
		<c:forEach var="file" items="${fileList}">
			<c:if test="${sessionMap.taskListItem.showCommentsToAll || (sessionMap.userLogin == file.createBy.loginName)}">
			
				<li>
					<c:out value="${file.fileName}" />
												
					<c:if test="${file.createBy != null}">
						[${file.createBy.loginName}]
					</c:if>
													
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
					</c:set>
					<html:link href="${downloadURL}">
						<fmt:message key="label.download" />
					</html:link>
												
				</li>
			</c:if>
		</c:forEach>
	</ul>
	<br/>
</div>
