<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<p>
	<html:checkbox property="useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput" styleClass="noBorder">
	</html:checkbox>
	<label for="useSelectLeaderToolOuput">
		<fmt:message key="label.use.select.leader.tool.output" />
	</label>
</p>

<p>
	<html:checkbox property="lockOnFinish" value="1" styleClass="noBorder" styleId="lockOnFinish">
	</html:checkbox>
	<label for="lockOnFinish">
		<fmt:message key="label.vote.lockedOnFinish" />
	</label>
</p>

<p>
	<html:checkbox property="allowText" value="1" styleClass="noBorder" styleId="allowText" onchange="changeMinMaxVotes(-1, -1);">
	</html:checkbox>
	<label for="allowText">
		<fmt:message key="label.allowText" />
	</label>
</p>

<p>
	<fmt:message key="label.maxNomCount" />&nbsp;
	<html:select property="maxNominationCount" styleId="maxNominationCount">
	</html:select>	
</p>

<p>
	<fmt:message key="label.minNomCount" />&nbsp;&nbsp;
	<html:select property="minNominationCount" styleId="minNominationCount">
	</html:select>	
</p>

<c:if test="${not empty voteGeneralAuthoringDTO.dataFlowObjectNames}">
	<p>
		<label for="maxInputs">
		<fmt:message key="label.advanced.data.flow.limit" /></label>
		<html:select property="maxInputs" styleId="maxInputs">
			<html:option value="0" styleId="dataFlowLimitNoneOption">
				<fmt:message key="label.advanced.data.flow.limit.none" />
			</html:option>
			<c:forEach begin="1" end="50" var="index">
				<html:option value="${index}">${index}</html:option>
			</c:forEach>
		</html:select>
	</p>
</c:if>

<p>
	<html:checkbox property="showResults" value="1" styleClass="noBorder" styleId="showResults">
	</html:checkbox>
	<label for="showResults">
		<fmt:message key="label.show.results" />
	</label>
</p>

<p>
	<html:checkbox property="reflect" value="1" styleClass="noBorder" styleId="reflect">
	</html:checkbox>
	<label for="reflect">
		<fmt:message key="label.reflect" />
	</label>
</p>
<p>
	<html:textarea cols="30" rows="3" property="reflectionSubject" styleId="reflectInstructions"></html:textarea>
</p>

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
//-->
</script>
