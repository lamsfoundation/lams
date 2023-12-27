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
			<form:checkbox path="useSelectLeaderToolOuput" id="useSelectLeaderToolOuput" value="1" />
			<fmt:message key="label.use.select.leader.tool.output" />
		</label>
	</div>
	
</lams:SimplePanel>

<lams:SimplePanel titleKey="label.voting.options">

	<div class="checkbox">
		<label for="allowText">
			<form:checkbox path="allowText" id="allowText" value="1" onchange="changeMinMaxVotes(-1, -1);" />
			<fmt:message key="label.allowText" />
		</label>
	</div>
	
	<div class="form-group">
		<form:select path="minNominationCount" cssClass="form-control form-control-inline input-sm" onchange="validateNominationCount(true);" />
		&nbsp;<fmt:message key="label.minNomCount" />
	</div>
	
	<div class="form-group">
		<form:select path="maxNominationCount" cssClass="form-control form-control-inline input-sm" onchange="validateNominationCount(false);" />
		&nbsp;<fmt:message key="label.maxNomCount" />
	</div>
	
	<c:if test="${not empty voteGeneralAuthoringDTO.dataFlowObjectNames}">
		<div class="form-group">
			<form:select path="maxInputs" cssClass="form-control input-sm">
				<form:option value="0" id="dataFlowLimitNoneOption">
					<fmt:message key="label.advanced.data.flow.limit.none" />
				</form:option>
				<c:forEach begin="1" end="50" var="index">
					<form:option value="${index}">${index}</form:option>
				</c:forEach>
			</form:select>
			<label for="maxInputs">
				<fmt:message key="label.advanced.data.flow.limit" />
			</label>
		</div>
	</c:if>
	
	<div class="checkbox">
		<label for="showResults">
			<form:checkbox path="showResults" id="showResults" value="1" />
			<fmt:message key="label.show.results" />
		</label>
	</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${voteGeneralAuthoringDTO.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="lockOnFinish">
			<form:checkbox path="lockOnFinish" id="lockOnFinish" value="1" />
			<fmt:message key="label.vote.lockedOnFinish" />
		</label>
	</div>
</lams:SimplePanel>

<script type="text/javascript">
	function validateNominationCount(isMinimunDropdownUsed) {
		var minDropDown = document.getElementById("minNominationCount");
		var min = parseInt(minNominationCount.options[minNominationCount.selectedIndex].value);
		var maxDropDown = document.getElementById("maxNominationCount");
		var max = parseInt(maxNominationCount.options[maxNominationCount.selectedIndex].value);

		if (min > max) {
			if (isMinimunDropdownUsed) {
				maxDropDown.selectedIndex = minDropDown.selectedIndex;
				if ( max > 1) {
					alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="js.error.validate.number"/></spring:escapeBody>');
				}
			} else {
				minDropDown.selectedIndex = maxDropDown.selectedIndex;
				alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="js.error.validate.number"/></spring:escapeBody>');
			} 
		}
	}

</script>
