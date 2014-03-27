<!-- de momento esta fuera de juego!!!!!!!!!!!!!!!!!!!!-->

<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="itemList">
	
	<table class="alternative-color" id="itemTable" cellspacing="0">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="eAdventure" value="${sessionMap.eadventure}" />
			<tr>
					<td>
						<span class="field-name"> eAdventure: </span>
						<c:out value="${eadventure.title}" escapeXml="true"/>
					</td>
					<td>
						<a href="#"
							onclick="previewItem(4,0,'${sessionMapID}')"> <fmt:message
								key="label.authoring.basic.resource.preview" /> </a>
					</td>
					<td>
						<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editItem(0,'${sessionMapID}')" />
					</td>
					<td>
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(0,'${sessionMapID}')" />
					</td>
			</tr>
	</table>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "Eadventure List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('eadventureListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
		win.document.getElementById('addGame').hide();
	}
</script>

