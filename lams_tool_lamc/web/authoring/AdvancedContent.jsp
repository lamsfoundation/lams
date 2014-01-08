<%@ include file="/common/taglibs.jsp"%>

<p>
	<html:checkbox property="useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput" onclick="clickSelectLeaderToolOuputHandler();"
		styleClass="noBorder">
	</html:checkbox>
	<label for="useSelectLeaderToolOuput">
		<fmt:message key="label.use.select.leader.tool.output" />
	</label>
</p>


<p class="small-space-top">
	<html:checkbox property="prefixAnswersWithLetters" value="1" styleId="prefixAnswersWithLetters"
		styleClass="noBorder">
	</html:checkbox>
	<label for="prefixAnswersWithLetters">
		<fmt:message key="label.prefix.sequential.letters.for.each.answer" />
	</label>
</p>


<p class="small-space-top">
	<html:checkbox property="questionsSequenced" value="1" styleId="questionsSequenced"
		styleClass="noBorder">
	</html:checkbox>
	<label for="questionsSequenced">
		<fmt:message key="radiobox.onepq" />
	</label>
</p>


<p class="small-space-top">
	<html:checkbox property="showMarks" value="1" styleId="showMarks"
		styleClass="noBorder">
	</html:checkbox>
	<label for="showMarks">
		<fmt:message key="label.showMarks" />
	</label>
</p>


<p class="small-space-top">
	<html:checkbox property="randomize" value="1" styleId="randomize"
		styleClass="noBorder">
	</html:checkbox>
	<label for="randomize">
		<fmt:message key="label.randomize" />
	</label>
</p>

<p class="small-space-top">
	<html:checkbox property="displayAnswers" value="1" styleId="displayAnswers"
		styleClass="noBorder">
	</html:checkbox>
	<label for="displayAnswers">
		<fmt:message key="label.displayAnswers" />
	</label>
</p>


<p>
	<html:checkbox property="retries" value="1" styleId="retries"
		onclick="javascript:updatePass(this); submitMethod('updateMarksList');" styleClass="noBorder">
	</html:checkbox>
	<label for="retries">
		<fmt:message key="radiobox.retries" />		
	</label>
</p>

<p>
	<select name="passmark">
		<c:forEach var="passmarkEntry"
			items="${mcGeneralAuthoringDTO.passMarksMap}">
			<c:set var="SELECTED_PASS" scope="request" value="" />
			<c:if
				test="${passmarkEntry.value == mcGeneralAuthoringDTO.passMarkValue}">
				<c:set var="SELECTED_PASS" scope="request" value="SELECTED" />
			</c:if>
	
			<option value="<c:out value="${passmarkEntry.value}"/>"
				<c:out value="${SELECTED_PASS}"/>>
				<c:out value="${passmarkEntry.value}" />
			</option>
		</c:forEach>
	</select>
	<fmt:message key="radiobox.passmark" />

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

