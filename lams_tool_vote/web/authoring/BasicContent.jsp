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
 		$.ajaxSetup({ cache: true });
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
		document.forms.voteAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeNomination');
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

<form:hidden path="questionIndex" />

<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.title.col"/></label>
    <form:input path="title" cssClass="form-control" />
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
	<a href="javascript:showMessage('<lams:WebAppURL />authoring/newNominationBox.do?contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}');"
			class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;
		<fmt:message key="label.add.new.nomination" /> 
	</a>
	
	<c:if test="${not empty voteGeneralAuthoringDTO.dataFlowObjectNames}">
		<span style="margin-left: 20px;"><fmt:message key="label.data.flow.choose" /></span>
		<form:select path="assignedDataFlowObject" onchange="javascript:onSelectDataInput();">
			<form:option value="0" id="dataFlowNoneOption"><fmt:message key="label.data.flow.none" /></form:option>
			<c:forEach items="${voteGeneralAuthoringDTO.dataFlowObjectNames}" var="dataFlowObject" varStatus="status">
				<form:option value="${status.index+1}">${dataFlowObject}</form:option>
			</c:forEach>
		</form:select>
	</c:if>
</p>

<div id="messageArea" name="messageArea" class="voffset10"></div>
