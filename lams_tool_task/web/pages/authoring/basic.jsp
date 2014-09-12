<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("resourceInputArea");
		if(area != null){
			area.style.width="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "resourceInputArea";
	}
	function hideMessage(){
		var area=document.getElementById("resourceInputArea");
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

	function editItem(idx, sessionMapID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
	//The panel of taskList list panel
	var taskListListTargetDiv = "taskListListArea";
	function deleteItem(idx, sessionMapID){
		var deletionConfirmed = true;
		
		var containsConditions = document.getElementById("conditionTable").rows.length > 0;
		if (containsConditions) {
			deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.deletion.affect.conditions"></fmt:message>");
 		}
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeItem.do"/>";
		    var reqIDVar = new Date();
			var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
			deleteItemLoading();
		    var myAjax = new Ajax.Updater(
			    	taskListListTargetDiv,
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
		showBusy(taskListListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(taskListListTargetDiv);
	}
	
	function upQuestion(idx, sessionMapID){
		var url = "<c:url value="/authoring/upItem.do"/>";
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	taskListListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function downQuestion(idx, sessionMapID){
		var url = "<c:url value="/authoring/downItem.do"/>";
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	taskListListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function resizeOnMessageFrameLoad(){
		var messageAreaFrame = document.getElementById("resourceInputArea");
		messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
	}
</script>

<!-- Basic Tab Content -->
<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="taskList.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.description"></fmt:message>
			</div>
			<lams:CKEditor id="taskList.instructions"
				value="${formBean.taskList.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>

</table>

<div id="taskListListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<p class="small-space-bottom">
	<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}"/>');" class="button-add-item">
	<fmt:message key="label.authoring.basic.add.task" /></a> 
</p>


<p>
	<iframe
		onload="javascript:resizeOnMessageFrameLoad();"
		id="resourceInputArea" name="resourceInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
