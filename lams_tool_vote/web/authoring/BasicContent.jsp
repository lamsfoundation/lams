<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->

 	function showMessage(url) {
		$("#messageArea").load(url, function() {
			$(this).show();
			$("#saveCancelButtons").hide();
		});
	}
	function hideMessage(){
		$("#messageArea").hide();
		$("#saveCancelButtons").show();
	}

	function removeNomination(questionIndex){
		document.VoteAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeNomination');
	}

	function removeMonitoringNomination(questionIndex){
		document.VoteMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeNomination');
	}
	
	
	function onSelectDataInput(){
		if (document.getElementById("dataFlowNoneOption")!=null){
			if (noneDataFlowSelectedPreviously && !document.getElementById("dataFlowNoneOption").selected){
					document.getElementById("maxInputs").disabled=false;		
						
			}
			else if (!noneDataFlowSelectedPreviously && document.getElementById("dataFlowNoneOption").selected){
					document.getElementById("dataFlowLimitNoneOption").selected=true;
					document.getElementById("maxInputs").disabled=true;		
			}
			noneDataFlowSelectedPreviously = document.getElementById("dataFlowNoneOption").selected;
		}
	} 

</script>

<html:hidden property="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <html:text property="title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="instructions"><fmt:message key="label.authoring.instructions.col" /></label>
	<lams:CKEditor id="instructions" value="${voteGeneralAuthoringDTO.activityInstructions}"
		contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}"></lams:CKEditor>
</div>

<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<p>
	<a href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newNominationBox&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}"/>');"
			class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;
		<fmt:message key="label.add.new.nomination" /> 
	</a>
	
	<c:if test="${not empty voteGeneralAuthoringDTO.dataFlowObjectNames}">
		<span style="margin-left: 20px;"><fmt:message key="label.data.flow.choose" /></span>
		<html:select property="assignedDataFlowObject" onchange="javascript:onSelectDataInput();">
			<html:option value="0" styleId="dataFlowNoneOption"><fmt:message key="label.data.flow.none" /></html:option>
			<c:forEach items="${voteGeneralAuthoringDTO.dataFlowObjectNames}" var="dataFlowObject" varStatus="status">
				<html:option value="${status.index+1}">${dataFlowObject}</html:option>
			</c:forEach>
		</html:select>
	</c:if>
</p>

<div id="messageArea" name="messageArea" class="voffset10"></div>
