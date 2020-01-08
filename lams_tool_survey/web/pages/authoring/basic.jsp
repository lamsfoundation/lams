<%@ include file="/common/taglibs.jsp"%>
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		$.ajaxSetup({ cache: true });
		$("#questionInputArea").load(url, function() {
			$(this).show();
			$("#saveCancelButtons").hide();
		});
	}
	function hideMessage(){
		$("#questionInputArea").hide();
		$("#saveCancelButtons").show();
	}
	
	function editItem(idx,itemType,sessionMapID,contentFolderID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&itemType="+itemType+"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID+"&contentFolderID="+contentFolderID;
		showMessage(url);
	}

	function copyItem(idx,itemType,sessionMapID,contentFolderID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/copyItemInit.do?itemIndex="/>" + idx +"&itemType="+itemType+"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID+"&contentFolderID="+contentFolderID;
		showMessage(url);
	}
	
	//The panel of survey list panel
	function deleteItem(idx,sessionMapID){
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		var url = "<c:url value="/authoring/removeItem.do"/>?<csrf:token/>&"+param;
		deleteItemLoading();
		$("#surveyListArea").load(url,deleteItemComplete);
	}
	
	function upQuestion(idx,sessionMapID){
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		var url = "<c:url value="/authoring/upItem.do"/>?"+param;
		deleteItemLoading();
		$("#surveyListArea").load(url, deleteItemComplete);
	}
	function downQuestion(idx,sessionMapID){
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		var url = "<c:url value="/authoring/downItem.do"/>?"+param;
		deleteItemLoading();
		$("#surveyListArea").load(url,deleteItemComplete);
	}
	function deleteItemLoading(){
		$("#resourceListArea_Busy").show();
	}
	function deleteItemComplete(){
		$("#resourceListArea_Busy").hide();
	}
</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="survey.title"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input path="survey.title" cssClass="form-control"></form:input>
</div>
<div class="form-group">
    <label for="survey.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="survey.instructions" value="${authoringForm.survey.instructions}" contentFolderID="${authoringForm.contentFolderID}"></lams:CKEditor>
</div>

<div id="surveyListArea">
	<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<a href="javascript:showMessage('<lams:WebAppURL/>authoring/newItemInit.do?itemType=1&contentFolderID=${authoringForm.contentFolderID}&sessionMapID=${authoringForm.sessionMapID}');"
	class="btn btn-default">
	<i class="fa fa-plus"></i>&nbsp; <fmt:message key="label.authoring.basic.add.survey.question"/> </a>

<a href="javascript:showMessage('<lams:WebAppURL/>authoring/newItemInit.do?itemType=3&contentFolderID=${authoringForm.contentFolderID}&sessionMapID=${authoringForm.sessionMapID}');"
	class="btn btn-default"> <i class="fa fa-plus"></i>&nbsp; <fmt:message key="label.authoring.basic.add.survey.open.question" /> </a>

<div id="questionInputArea" class="voffset10"></div>
