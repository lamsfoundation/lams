<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script type="text/javascript">
	   <%-- used for  eadventureitem.js --%>
       var removeGameAttachmentUrl = "<c:url value='/authoring/removeGameAttachment.do'/>";
	</script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/eadventureitem.js'/>"></script>
<script lang="javascript">


<!-- FICHERO CAMBIADO!!! -->
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="650px";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "reourceInputArea";
	}
	function hideMessage(){
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}

	function editItem(idx,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
	//The panel of eadventure list panel
	var eadventureListTargetDiv = "eadventureListArea";
	function deleteItem(idx,sessionMapID){
	
	var deletionConfirmed = true;
		
		var containsConditions = document.getElementById("conditionList").rows.length > 0;
		if (containsConditions) {
			deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.deletion.affect.conditions"></fmt:message>");
 		}
		
		if (deletionConfirmed) {
	
		var url = "<c:url value="/authoring/removeItem.do"/>";
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	eadventureListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
		}
	}
	function deleteItemLoading(){
		showBusy(eadventureListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(eadventureListTargetDiv);
	}
	
	

</script>

<!-- Basic Tab Content -->
<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				eAdventure Game
			</div>
			<html:text property="eadventure.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="eadventure.instructions"
				value="${formBean.eadventure.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>

</table>


<h2 class="no-space-left">e-Adventure game</h2>
<div id="addGame">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/gamearea.jsp"%>
</div>

<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="reourceInputArea" name="reourceInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
