<%@ include file="/common/taglibs.jsp"%>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="itemList" >
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.image.list" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="imageGalleryListArea_Busy" />
	</h2>

	<table class="alternative-color" id="itemTable" cellspacing="0" > 
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="image" items="${sessionMap.imageGalleryList}" varStatus="status">
			<tr>
				<td width="4%" align="center">
					<c:set var="thumbnailPath">
					   	<html:rewrite page='/download/?uuid='/>${image.thumbnailFileUuid}&preferDownload=false
					</c:set>
				 	<c:set var="mediumImagePath">
	   					<html:rewrite page='/download/?uuid='/>${image.mediumFileUuid}&preferDownload=false
					</c:set>					
					<a href="${mediumImagePath}" rel="lyteframe" title="${image.title}" style="border-style: none;" rev="width: ${image.mediumImageWidth + 20}px; height: ${image.mediumImageHeight + 30}px; scrolling: no;">
						<img src="${thumbnailPath}" alt="${image.title}" style="border-style: none;"/>
					</a>
				</td>
				
				<td style="vertical-align:middle;">
					<a href="${mediumImagePath}" rel="lyteframe" title="${image.title}" rev="width: ${image.mediumImageWidth + 20}px; height: ${image.mediumImageHeight + 30}px; scrolling: auto;">
						${image.title}
					</a>
				</td>

				<td width="40px" style="vertical-align:middle;">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.up"/>"
							onclick="upImage(${status.index},'${sessionMapID}')">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.up"/>">
						</c:if>

						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.down"/>"
							onclick="downImage(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
				
				<td width="20px" style="vertical-align:middle;">
					<img src="${tool}includes/images/edit.gif"
						title="<fmt:message key="label.authoring.basic.resource.edit" />"
						onclick="editItem(${status.index},'${sessionMapID}')" />
                </td>
                
				<td width="20px" style="vertical-align:middle;">
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.resource.delete" />"
						onclick="deleteItem(${status.index},'${sessionMapID}')" />
				</td>
			</tr>
		</c:forEach>
	</table>
	
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "ImageGallery List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('imageGalleryListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
		
		//call it from parent window as this one was just hidden
		win.initLytebox();
	}
</script>
