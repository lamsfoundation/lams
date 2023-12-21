<%@ include file="/common/taglibs.jsp"%>

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
			deletionConfirmed = confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='warning.msg.authoring.deletion.affect.conditions'/></spring:escapeBody>");
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
		$.ajax({
			data: form.serialize(),
	       	type: form.attr('method'),
			url: form.attr('action'),
			success: function(data) {
	           $('#resourceInputArea').html(data);
			}
		});
	} 
	
</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="taskList"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input path="taskList.title" cssClass="form-control"></form:input>
</div>
<div class="form-group">
    <label for="taskList.instructions"><fmt:message key="label.authoring.basic.description" /></label>
    <lams:CKEditor id="taskList.instructions" value="${taskListForm.taskList.instructions}" contentFolderID="${taskListForm.contentFolderID}">
    </lams:CKEditor>
</div>

<div id="taskListListArea">
	<c:set var="sessionMapID" value="${taskListForm.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<a href="javascript:showMessage('<lams:WebAppURL/>authoring/newItemInit.do?sessionMapID=${taskListForm.sessionMapID}');" class="btn btn-default btn-sm">
	<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.task" />
</a> 

<div id="resourceInputArea" class="voffset10"></div>
