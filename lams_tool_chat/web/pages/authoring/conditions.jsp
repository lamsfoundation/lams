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
		var url = "<c:url value="/authoringCondition/editCondition.do?orderId="/>" + orderId +"&sessionMapID="+sessionMapID;
		showConditionMessage(url);
	}

	function deleteCondition(orderId, sessionMapID){
		$.ajaxSetup({ cache: true });
		$("#conditionsArea").load("<c:url value='/authoringCondition/removeCondition.do'/>",{
			'orderId' : orderId,
			'sessionMapID' : sessionMapID
		});
	}
	
	function upCondition(orderId,sessionMapID){
		$.ajaxSetup({ cache: true });
		$("#conditionsArea").load("<c:url value='/authoringCondition/upCondition.do'/>",{
			'orderId' : orderId,
			'sessionMapID' : sessionMapID
		});
	}
	function downCondition(orderId,sessionMapID){
		$.ajaxSetup({ cache: true });
		$("#conditionsArea").load("<c:url value='/authoringCondition/downCondition.do'/>",{
			'orderId' : orderId,
			'sessionMapID' : sessionMapID
		});
	}
	
	// Packs additional elements and submits the question form
	function submitCondition(){
		var form = $('#chatConditionForm');
		$.ajaxSetup({ cache: true });
		$('#conditionInputArea').load(form.attr('action'), form.serialize());
	} 
</script>

<!-- Conditions Tab Content -->

<div id="conditionsArea">
	<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
	<%@ include file="/pages/authoring/conditionList.jsp"%>
</div>
 
 <div class="form-inline">
	<a href="javascript:showConditionMessage('<lams:WebAppURL />authoringCondition/newConditionInit.do?sessionMapID=${authoringForm.sessionMapID}');" 
		class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.conditions.add.condition" /></a> 
	<lams:Popover titleKey="label.authoring.heading.conditions">
		<fmt:message key="label.authoring.conditions.add.condition.tooltip.1" /><br>
		<fmt:message key="label.authoring.conditions.add.condition.tooltip.2" />
	</lams:Popover>
</div>

<div id="conditionInputArea" name="conditionInputArea" class="voffset10"></div>