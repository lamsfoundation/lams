<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
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
	
	function editCondition(orderId,sessionMapID){
		var url = "<c:url value='/authoringConditions/editCondition.do?orderId='/>"
			  + orderId + "&sessionMapID=" + sessionMapID;
		showConditionMessage(url);
	}
	
	function deleteCondition(orderId, sessionMapID){
		$.ajax({
			async : false,
			url : '<c:url value="/authoringConditions/removeCondition.do"/>',
			data : 'orderId=' + orderId + '&sessionMapID=' + sessionMapID + '&<csrf:token/>',
			type : "POST"
			});

	}
	
	function upCondition(orderId,sessionMapID){
		$("#conditionsArea").load("<c:url value='/authoringConditions/upCondition.do'/>",{
			'orderId' : orderId,
			'sessionMapID' : sessionMapID
		});
	}
	function downCondition(orderId,sessionMapID){
		$("#conditionsArea").load("<c:url value='/authoringConditions/downCondition.do'/>",{
			'orderId' : orderId,
			'sessionMapID' : sessionMapID
		});
	}
	
	//Packs additional elements and submits the question form
	function submitCondition(){
		var form = $('#QaConditionForm');
		var obj = form.serializeArray();
		$('#conditionInputArea').load(form.attr('action'), obj);
	} 

</script>

<!-- Conditions Tab Content -->
<div id="conditionsArea">
	<c:set var="sessionMapID" value="${authoringForm.httpSessionID}" />
	<%@ include file="/authoring/conditionList.jsp"%>
</div>
 
 <div class="form-inline">
	<a href="javascript:showConditionMessage('../authoringConditions/newConditionInit.do?sessionMapID=${authoringForm.httpSessionID}');" 
		class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.conditions.add.condition" /></a> 
</div>

<div id="conditionInputArea" name="conditionInputArea" class="voffset10"></div>
