<%@ include file="/common/taglibs.jsp"%>

<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

function showConditionMessage(url) {
	$.ajaxSetup({ cache: true });
	$("#conditionInputArea").load(url, function() {
		$(this).show();
		$("#saveCancelButtons").hide();
	});
}
function hideConditionMessage(){
	$("#conditionInputArea").hide();
	$("#saveCancelButtons").show();
}

function editCondition(sequenceId,sessionMapID){
	var url = "<c:url value="/authoringCondition/editCondition.do?sequenceId="/>" + sequenceId +"&sessionMapID="+sessionMapID;
	showConditionMessage(url);
}

function deleteCondition(sequenceId, sessionMapID){
	var param = "sequenceId=" + sequenceId +"&sessionMapID="+sessionMapID;
	var url = "<c:url value="/authoringCondition/removeCondition.do"/>?"+param;
	$("#conditionsArea").load(url);
}

function upCondition(sequenceId,sessionMapID){
	var param = "sequenceId=" + sequenceId + "&sessionMapID="+sessionMapID;;
	var url = "<c:url value="/authoringCondition/upCondition.do"/>?"+param;
	$("#conditionsArea").load(url);
}
function downCondition(sequenceId,sessionMapID){
	var param = "sequenceId=" + sequenceId + "&sessionMapID="+sessionMapID;
	var url = "<c:url value="/authoringCondition/downCondition.do"/>?"+param;
	$("#conditionsArea").load(url);
}

//Packs additional elements and submits the question form
function submitCondition(){
	refreshCKEditors();
	var form = $('#taskListConditionForm');
	$('#conditionInputArea').load(form.attr('action'), form.serialize());
} 

</script>

<!-- Conditions Tab Content -->

<div id="conditionsArea">
	<c:set var="sessionMapID" value="${taskListForm.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/conditionlist.jsp"%>
</div>

 <div class="form-inline">
	<a href="javascript:showConditionMessage('<lams:WebAppURL/>authoringCondition/newConditionInit.do?sessionMapID=${taskListForm.sessionMapID}');"
	class="btn btn-default btn-sm"><i class="fa fa-plus"></i> <fmt:message key="label.authoring.conditions.add.condition" /></a>
</div>

<div id="conditionInputArea" name="conditionInputArea" class="voffset10"></div>
