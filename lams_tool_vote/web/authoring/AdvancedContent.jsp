<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:SimplePanel titleKey="label.select.leader">
	
	<div class="checkbox">
		<label for="useSelectLeaderToolOuput">
		<html:checkbox property="useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput">
		</html:checkbox>
		<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
	
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.voting.options">

	<div class="checkbox">
		<label for="allowText">
		<html:checkbox property="allowText" value="1" styleId="allowText" onchange="changeMinMaxVotes(-1, -1);">
		</html:checkbox>
		<fmt:message key="label.allowText" />
		</label>
	</div>
	
	<div class="form-group">
		<html:select property="minNominationCount" styleId="minNominationCount" styleClass="form-control form-control-inline input-sm" onchange="validateNominationCount(true);">
		</html:select>&nbsp;<fmt:message key="label.minNomCount" />
	</div>
	
	<div class="form-group">
		<html:select property="maxNominationCount" styleId="maxNominationCount" styleClass="form-control form-control-inline input-sm" onchange="validateNominationCount(false);">
		</html:select>&nbsp;<fmt:message key="label.maxNomCount" />
	</div>
	
	<c:if test="${not empty voteGeneralAuthoringDTO.dataFlowObjectNames}">
		<div class="form-group">
			<html:select property="maxInputs" styleId="maxInputs"  styleClass="form-control input-sm">
				<html:option value="0" styleId="dataFlowLimitNoneOption">
					<fmt:message key="label.advanced.data.flow.limit.none" />
				</html:option>
				<c:forEach begin="1" end="50" var="index">
					<html:option value="${index}">${index}</html:option>
				</c:forEach>
			</html:select>
			<label for="maxInputs">
			<fmt:message key="label.advanced.data.flow.limit" /></label>
		</div>
	</c:if>
	
	<div class="checkbox">
		<label for="showResults">
		<html:checkbox property="showResults" value="1" styleId="showResults">
		</html:checkbox>
		<fmt:message key="label.show.results" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${voteGeneralAuthoringDTO.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">

	<div class="checkbox">
		<label for="lockOnFinish">
		<html:checkbox property="lockOnFinish" value="1" styleId="lockOnFinish">
		</html:checkbox>
		<fmt:message key="label.vote.lockedOnFinish" />
		</label>
	</div>

	<div class="checkbox">
		<label for="reflect">
		<html:checkbox property="reflect" value="1" styleId="reflect">
		</html:checkbox>
		<fmt:message key="label.reflect" />
		</label>
	</div>
	<div class="form-group">
		<html:textarea rows="3" property="reflectionSubject" styleId="reflectInstructions" styleClass="form-control"></html:textarea> 
	</div>

</lams:SimplePanel>

<script type="text/javascript">
<!--
//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflect");
	function turnOnRefect(){
		if(isEmpty(ra.value)){
		//turn off	
			rao.checked = false;
		}else{
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
	
	function validateNominationCount(isMinimunDropdownUsed) {
		var minDropDown = document.getElementById("minNominationCount");
		var min = parseInt(minNominationCount.options[minNominationCount.selectedIndex].value);
		var maxDropDown = document.getElementById("maxNominationCount");
		var max = parseInt(maxNominationCount.options[maxNominationCount.selectedIndex].value);

		if (min > max) {
			if (isMinimunDropdownUsed) {
				maxDropDown.selectedIndex = minDropDown.selectedIndex;
				if ( max > 1) {
					alert('<fmt:message key="js.error.validate.number"/>');
				}
			} else {
				minDropDown.selectedIndex = maxDropDown.selectedIndex;
				alert('<fmt:message key="js.error.validate.number"/>');
			} 
		}
	}


//-->
</script>
