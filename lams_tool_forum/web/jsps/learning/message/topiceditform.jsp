<%@ include file="/common/taglibs.jsp"%>

<div class="form-group">
    <label><fmt:message key="message.label.subject" />&nbsp;</label>
	<html:text size="50" tabindex="1" property="message.subject" maxlength="60"/> &nbsp;
	<html:errors property="message.subject" />
</div>

<div class="form-group">
    <label><fmt:message key="message.label.body" />  *</label><BR/>
	<%@include file="bodyarea.jsp"%>
</div>

<c:if test="${topic.hasAttachment || sessionMap.allowUpload}">
	<c:set var="allowUpload" value="${sessionMap.allowUpload}" />

	 <div class="form-group">
    	<label><fmt:message key="message.label.attachment" />&nbsp;</label>
		<div id="itemAttachmentArea">
			<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
			<input type="hidden" name="hasAttachment" value="${topic.hasAttachment}"/>
			<c:if test="${topic.hasAttachment}">
					<c:forEach var="file" items="${topic.message.attachments}">
					    <a id="removeAttachmentButton" href="#" onclick="removeAtt('${sessionMapID}')" class="btn btn-default btn-xs"><i class="fa fa-trash-o"></i></a>
						<fmt:message key="message.label.attachment"/>: 
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" /> 
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
				  		<BR/>
			  		</c:forEach>
				</ul>
			</c:if>
			
			<c:if test="${not topic.hasAttachment && allowUpload}">
				<lams:FileUpload fileFieldname="attachmentFile" maxFileSize="${sessionMap.uploadMaxFileSize}"/>
			</c:if>
		</div>
	</div>		

	<lams:WaitingSpinner id="itemAttachmentArea_Busy"/>
</c:if>

<div class="btn-group-xs voffset5 pull-right">
<html:button property="goback" styleId="cancelButton" onclick="javascript:cancelEdit();" styleClass="btn btn-default">
	<fmt:message key="button.cancel" />
</html:button>
<html:submit styleClass="btn btn-default" styleId="submitButton">
	<fmt:message key="button.submit" />
</html:submit>
</div>
