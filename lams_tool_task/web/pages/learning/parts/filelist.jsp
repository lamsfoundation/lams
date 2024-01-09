<c:set var="fileList" value="${itemDTO.attachments}" />

<c:if test="${not empty fileList}">
	<div>
		<ul>
			<c:forEach var="file" items="${fileList}">
				<c:if test="${(sessionMap.userLogin == file.createBy.loginName) || (sessionMap.mode == 'teacher') || (sessionMap.mode == 'author')}">

					<li>
						<c:out value="${file.fileName}" escapeXml="true" /> 
						
						<c:if test="${file.createBy != null}">
							<span class="badge text-bg-warning bg-opacity-50 mx-2">
								<c:out value="${file.createBy.getFullName()}" escapeXml="true" />
							</span>
						</c:if> 

						<c:set var="downloadURL">
							<lams:WebAppURL/>download/?uuid=${file.fileDisplayUuid}&versionID=${file.fileVersionId}&preferDownload=true
						</c:set> 
						<a href="${downloadURL}" class="btn btn-sm btn-light">
							<fmt:message key="label.download" />
							<i class="fa-solid fa-download ms-1"></i>
						</a>
					</li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
</c:if>