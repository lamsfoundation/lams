<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="itemList" >

	<div class="panel panel-default voffset5">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.image.list" />
			<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="imageGalleryListArea_Busy" />
		</div>

		<table class="table table-condensed table-authoring" id="itemTable">
			<c:forEach var="image" items="${sessionMap.imageGalleryList}" varStatus="status">
			
				<tr>
					<td width="4%" >
						<c:set var="thumbnailPath">
						   	<html:rewrite page='/download/?uuid='/>${image.thumbnailFileUuid}&preferDownload=false
						</c:set>
					 	<c:set var="mediumImagePath">
		   					<html:rewrite page='/download/?uuid='/>${image.mediumFileUuid}&preferDownload=false
						</c:set>					
						<c:set var="title">
							<c:out value="${image.title}" escapeXml="true"/>
						</c:set>
						<a href="${mediumImagePath}" rel="lyteframe" title="${title}" style="border-style: none;" rev="width: ${image.mediumImageWidth + 20}px; height: ${image.mediumImageHeight + 30}px; scrolling: no;">
							<img src="${thumbnailPath}" alt="${title}" style="border-style: none;"/>
						</a>
					</td>
					
					<td>
						<a href="${mediumImagePath}" rel="lyteframe" title="${title}" rev="width: ${image.mediumImageWidth + 20}px; height: ${image.mediumImageHeight + 30}px; scrolling: auto;">
							<c:out value="${image.title}" escapeXml="true"/>
						</a>
					</td>
					
					<td class="arrows">
						<!-- Don't display up icon if first line -->
						<c:if test="${not status.first}">
			 				<lams:Arrow state="up" title="<fmt:message key='label.authoring.up'/>" onclick="javascript:upImage(${status.index},'${sessionMapID}')"/>
			 			</c:if>
						<!-- Don't display down icon if last line -->
						<c:if test="${not status.last}">
							<lams:Arrow state="down" title="<fmt:message key='label.authoring.down'/>" onclick="javascript:downImage(${status.index},'${sessionMapID}')"/>
			 			</c:if>
					</td>			
					
					<td width="30px">
						<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="javascript:editItem(${status.index},'${sessionMapID}')"></i>
					</td>
					<td width="30px">
						<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="javascript:deleteItem(${status.index},'${sessionMapID}')"></i>
					</td>
				</tr>
				
			</c:forEach>
		</table>
		
	</div>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "ImageGallery List" panel. --%>
<script lang="javascript">
	hideMessage();
	var obj = document.getElementById('imageGalleryListArea');
	obj.innerHTML= document.getElementById("itemList").innerHTML;
	initLytebox();
</script>
