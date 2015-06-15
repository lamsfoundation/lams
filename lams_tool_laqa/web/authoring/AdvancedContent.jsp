<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script language="JavaScript" type="text/JavaScript">
	
	jQuery( document ).ready(function( $ ) {
		
		$("#useSelectLeaderToolOuput").click(function() {
			if (document.QaAuthoringForm.useSelectLeaderToolOuput.checked) {
				//uncheck checkboxes
				document.QaAuthoringForm.showOtherAnswers.checked = false;
				//disable checkboxes
				document.QaAuthoringForm.showOtherAnswers.disabled = true;
				
				$("#show-other-answers-options").hide('slow');
			} else {
				//enable checkboxes
				document.QaAuthoringForm.showOtherAnswers.disabled = false;
			}	
		});
		
		$("#showOtherAnswers").click(function() {
			$("#show-other-answers-options").toggle('slow');
			if ($(this).is(':checked')) {
				$("#usernameVisible").prop('checked', true);
			}
		});
		
		//initial state
		if (${formBean.showOtherAnswers == 0}) {
			$("#show-other-answers-options").hide();
		}
		
	    $('#allow-rate-answers').click(function() {
	        $("#criterias-holder").toggle("slow");
	    });
		
	});
	
</script>

<p>
	<html:checkbox property="useSelectLeaderToolOuput" value="1" styleId="useSelectLeaderToolOuput"
		styleClass="noBorder"/>
	<label for="useSelectLeaderToolOuput">
		<fmt:message key="label.use.select.leader.tool.output" />
	</label>
</p>

<p class="small-space-top">
	<html:checkbox property="showOtherAnswers" value="1" styleId="showOtherAnswers"
		styleClass="noBorder" disabled="${formBean.useSelectLeaderToolOuput}"/>
	<label for="showOtherAnswers">
		<fmt:message key="label.learner.answer" />
	</label>
</p>

<div style="padding-left: 20px;" id="show-other-answers-options">

	<p>
		<html:checkbox property="usernameVisible" value="1" styleId="usernameVisible" styleClass="noBorder"/>
		<label for="usernameVisible">
			<fmt:message key="label.show.names" />
		</label>
	</p>

	<p>
		<html:checkbox property="allowRateAnswers" value="1" styleId="allow-rate-answers" styleClass="noBorder"/>
		<label for="allow-rate-answers">
			<fmt:message key="label.authoring.allow.rate.answers" />
		</label>
	</p>
	
	<div>
		<div style="margin-left:20px;">
			<div id="criterias-holder" <c:if test="${formBean.allowRateAnswers == 0}"> style="display:none;"</c:if>>
				<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
					upLabel="label.authoring.up" downLabel="label.authoring.down"/>
			</div>
		</div>
	</div>
	
</div>

<p class="small-space-top">
	<html:checkbox property="notifyTeachersOnResponseSubmit" value="1" styleClass="noBorder" styleId="notifyTeachersOnResponseSubmit"/>
	<label for="notifyTeachersOnResponseSubmit">
		<fmt:message key="label.notify.teachers.on.response.submit" />
	</label>
</p>

<p>
	<html:checkbox property="reflect" value="1" styleClass="noBorder" styleId="reflect"/>
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

<p>
	<html:checkbox property="questionsSequenced" value="1" styleId="questionsSequenced"
		styleClass="noBorder"/>
	<label for="questionsSequenced">
		<fmt:message key="radiobox.questionsSequenced" />
	</label>
</p>

<p>
	<html:checkbox property="lockWhenFinished" value="1" styleId="lockWhenFinished"
		styleClass="noBorder"/>
	<label for="lockWhenFinished">
		<fmt:message key="label.lockWhenFinished" />
	</label>
</p>

<p>
	<html:checkbox property="allowRichEditor" value="1" styleId="allowRichEditor"
		styleClass="noBorder"/>
	<label for="allowRichEditor">
		<fmt:message key="label.allowRichEditor" />
	</label>
</p>
