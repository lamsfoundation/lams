<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>
<div id="showGame">
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />	
	<c:set var="gameDelete" value="${sessionScope[gameDelete]}" />	
	<c:out value="${gameDelete}"/>

	<!-- No se xk no coge el atrubuto delete, ver que pasa-->
	<c:if test="${gameDelete == 1}">
	<script lang="javascript">
		if (window.parent && window.parent.forceEmptyTable) {
			window.parent.forceEmptyTable();
		} else if (window.top && window.top.forceEmptyTable) {
			window.top.forceEmptyTable();
		}
	</script>
	
	
	</c:if>
	<c:choose>
		<c:when test="${sessionMap.hasFile}">
			
		<!-- show the conditions add button -->
		<script lang="javascript">
			if (window.parent && window.parent.showConditionsAddButton) {
				window.parent.showConditionsAddButton();
			} else if (window.top && window.top.showConditionsAddButton) {
				window.top.showConditionsAddButton();
			}
		</script>
			
		<table border="0" style="align:left;width:400px">
		<tr>
			<td>
				<p id="gameName" align="center">
					<c:out value="${sessionMap.eadventureForm.eadventure.fileName}" />
				</p>
			</td>
			<td>
			<p align="small-space-bottom">
				<a href="javascript:showMessage('<html:rewrite page="/authoring/removeGameAttachment.do?sessionMapID=${sessionMapID}"/>');"  class="button-add-item">
					<fmt:message key="label.delete" />
				</a>
			</p>
				<!--<a href="#"
					onclick="removeGameAttachment('${sessionMapID}')"  
					class="button"> <fmt:message key="label.delete" /> </a>-->
			</td>		
		</tr>
		</table>
		</c:when>
		<c:otherwise>
				<!-- show the conditions add button -->
				<script lang="javascript">
					var win = null;
					if (window.parent && window.parent.hideConditionsAddButton) {
						win = window.parent;
					} else window.top && window.top.hideConditionsAddButton) {
						win = window.top;
					}
					win.hideConditionsAddButton();
					win.forceEmptyTable();
				</script>
			<p align="small-space-bottom">
				<a href="javascript:showMessage('<html:rewrite page="/authoring/editGameInit.do?sessionMapID=${sessionMapID}"/>');"  class="button-add-item">
					<fmt:message key="label.authoring.basic.add.game" />
				</a>
			</p>
		</c:otherwise>
	</c:choose>
</div>			
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage){
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		win.showGame();
		var obj = win.document.getElementById('addGame');
		obj.innerHTML= document.getElementById("showGame").innerHTML;
	}
	
</script>