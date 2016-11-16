<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		$.ajaxSetup({ cache: true });
		$("#resourceInputArea").load(url, function() {
			$(this).show();
			$("#saveCancelButtons").hide();
		});
	}
	function hideMessage(){
		$("#resourceInputArea").hide();
		$("#saveCancelButtons").show();
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
		    var reqIDVar = new Date();
			var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;
			var url = "<c:url value="/authoring/removeItem.do"/>?"+param;
			deleteItemLoading();
			$('#taskListListArea').load(url, function() {
	    		deleteItemComplete();
			});
 		}
		
	}
	function deleteItemLoading(){
		showBusy(taskListListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(taskListListTargetDiv);
	}
	
	function upQuestion(idx, sessionMapID){
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;
		var url = "<c:url value="/authoring/upItem.do"/>?"+param;
		deleteItemLoading();
		$('#taskListListArea').load(url, function() {
			deleteItemComplete();
		});
	}
	function downQuestion(idx, sessionMapID){
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		var url = "<c:url value="/authoring/downItem.do"/>?"+param;
		deleteItemLoading();
		$('#taskListListArea').load(url, function() {
			deleteItemComplete();
		});
	}
	
	//Packs additional elements and submits the question form
	function submitTask(){
		refreshCKEditors();
		var form = $('#taskListItemForm');
		$('#resourceInputArea').load(
			form.attr('action'), 
			form.serialize()
		);
	} 
	
</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="taskList"><fmt:message key="label.authoring.basic.title"/></label>
    <html:text property="taskList.title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="taskList.instructions"><fmt:message key="label.authoring.basic.description" /></label>
    <lams:CKEditor id="taskList.instructions" value="${formBean.taskList.instructions}" contentFolderID="${formBean.contentFolderID}">
    </lams:CKEditor>
</div>

<div id="taskListListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}"/>');" class="btn btn-default btn-sm">
	<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.task" />
</a> 

<div id="resourceInputArea" class="voffset10"></div>
