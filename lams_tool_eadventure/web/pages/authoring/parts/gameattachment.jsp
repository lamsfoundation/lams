<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<div id="gameAttachment">
<c:choose>
	<c:when test="${sessionMap.hasFile}">
		<table border="0" style="align:left;width:400px">
			<tr>
				<td>
					<c:out value="${gameAttachment.fileName}" />
				</td>
				
				<td>
					<a href="#"
						onclick="removeGameAttachment('${sessionMapID}', 'no')"  
						class="button"> <fmt:message key="label.delete"/> </a>
				</td>
				
				<td>
					<img src="${ctxPath}/includes/images/indicator.gif"
						style="display:none" id="gameAttachmentArea_Busy" />
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<input type="file" name="file" />
	</c:otherwise>
</c:choose>
</div>
