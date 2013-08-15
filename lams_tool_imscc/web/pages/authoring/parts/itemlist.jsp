<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.resource.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="commonCartridgeListArea_Busy" />
	</h2>

	<table class="alternative-color" id="itemTable" cellspacing="0">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="commonCartridge" items="${sessionMap.commonCartridgeList}" varStatus="status">
			<tr>
				<%-- CommonCartridge Type:1=URL,2=File,3=Website,4=Learning Object --%>
				<c:if test="${commonCartridge.type == 1}">
					<td>
						<span class="field-name"> 
							<fmt:message key="label.authoring.basic.resource.url" /> : 
						</span>
						${commonCartridge.title}
					</td>
					<td width="9%">
						<a href="#"
							onclick="previewItem(1,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.verify.url" /> </a>
					</td>
					<td width="5%">
					
						<c:set var="editItemUrl" >
							<c:url value='/authoring/editItemInit.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&height=540&width=850&modal=true
						</c:set>		
						<a href="${editItemUrl}" class="thickbox" style="border-style: none;"> 
							<img src="${tool}includes/images/edit.gif"
								title="<fmt:message key="label.authoring.basic.resource.edit" />" style="border-style: none;"/>
						</a>

					</td>
					<td width="5%">
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>

				<c:if test="${commonCartridge.type == 2}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.file" /> : </span>
						${commonCartridge.title}
					</td>
					<td>
						<a href="#"
							onclick="previewItem(2,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />

					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>

				<c:if test="${commonCartridge.type == 3}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.website" /> : </span>
						${commonCartridge.title}
					</td>
					<td>
						<a href="#"
							onclick="previewItem(3,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />
					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>

				<c:if test="${commonCartridge.type == 4}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.learning.object" /> : </span>
						${commonCartridge.title}
					</td>
					<td>
						<a href="#"
							onclick="previewItem(4,${status.index},'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(${status.index},'${sessionMapID}')" />
					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "CommonCartridge List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('commonCartridgeListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
