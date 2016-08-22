<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
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
	var url = "<c:url value="/authoring/editCondition.do?sequenceId="/>" + sequenceId +"&sessionMapID="+sessionMapID;
	showConditionMessage(url);
}

function deleteCondition(sequenceId, sessionMapID){
	var param = "sequenceId=" + sequenceId +"&sessionMapID="+sessionMapID;
	var url = "<c:url value="/authoring/removeCondition.do"/>?"+param;
	$("#conditionsArea").load(url);
}

function upCondition(sequenceId,sessionMapID){
	var param = "sequenceId=" + sequenceId + "&sessionMapID="+sessionMapID;;
	var url = "<c:url value="/authoring/upCondition.do"/>?"+param;
	$("#conditionsArea").load(url);
}
function downCondition(sequenceId,sessionMapID){
	var param = "sequenceId=" + sequenceId + "&sessionMapID="+sessionMapID;
	var url = "<c:url value="/authoring/downCondition.do"/>?"+param;
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
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/conditionlist.jsp"%>
</div>

 <div class="form-inline">
	<a href="javascript:showConditionMessage('<html:rewrite page="/authoring/newConditionInit.do?sessionMapID=${formBean.sessionMapID}"/>');"
		 class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.conditions.add.condition" /></a> 
</div>

<div id="conditionInputArea" name="conditionInputArea" class="voffset10"></div>
