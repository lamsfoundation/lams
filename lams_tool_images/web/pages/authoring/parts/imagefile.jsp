<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<input type="hidden" name="hasFile" value="${itemAttachment.hasFile}" id="has-file"/>

<c:choose>
	<c:when test="${itemAttachment.hasFile}">
		<table border="0" style="align:left; width:400px">
			<tr>
				<td>
					<c:out value="${itemAttachment.fileName}" />
				</td>
				
				<td width="3%" align="left">
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${itemAttachment.fileUuid}&preferDownload=false" />
					</c:set>					
					<a href="#nogo" onclick="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')" class="btn btn-default btn-xs">
						<fmt:message key="label.view" />
					</a>
				</td>
				
				<td>
					<a href="#nogo" onclick="javascript:removeItemAttachment(${itemAttachment.imageIndex})" class="btn btn-default btn-xs loffset5">
						<fmt:message key="label.delete" />
					</a>
				</td>
				
				<td>
					<i class="fa fa-refresh fa-spin fa-1x fa-fw" style="display:none" id="itemAttachmentArea_Busy"></i>
				</td>
			</tr>
		</table>
	</c:when>
	
	<c:otherwise>
		<input type="file" name="file" id="file-select"/>
	</c:otherwise>
</c:choose>
