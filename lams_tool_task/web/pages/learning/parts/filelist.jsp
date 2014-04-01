<div>
	<c:set var="fileList" value="${itemDTO.attachments}" />
	
	<c:if test="${not empty fileList}">
		<div class="field-name">
			<fmt:message key="label.preview.filelist" />
		</div>
	</c:if>
							
	<%-- Display target file type --%>
	<ul>
		<c:forEach var="file" items="${fileList}">
			<c:if test="${item.showCommentsToAll || (sessionMap.userLogin == file.createBy.loginName) || (sessionMap.mode == 'teacher') || (sessionMap.mode == 'author')}">
			
				<li>
					<c:out value="${file.fileName}" escapeXml="true"/>
												
					<c:if test="${file.createBy != null}">
						[<c:out value="${file.createBy.loginName}" escapeXml="true"/>]
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
