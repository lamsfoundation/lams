<%@ include file="/common/taglibs.jsp"%>

<script lang="javascript">
	$(document).ready(function() {
		$("#allowContributeVideos").click(function() {
			if ($('#allowContributeVideos').is(':checked')) {
				$("#allowSeeingOtherUsersRecordings").prop("disabled", false);
				$("#learner-contribution-limit").prop("disabled", false);
			} else {
				$("#allowSeeingOtherUsersRecordings").prop("disabled", true);
				$("#allowSeeingOtherUsersRecordings").prop("checked", false);
				$("#learner-contribution-limit").prop("disabled", true);
				$("#learner-contribution-limit").prop("value", -1);
			}
		});
		
		<c:if test="${!authoringForm.allowContributeVideos}">
			$("#allowSeeingOtherUsersRecordings").prop("disabled", true);
			$("#learner-contribution-limit").prop("disabled", true);
		</c:if>

		$('#reflect-instructions').keyup(function(){
			$('#reflect-on-activity').prop('checked', !isEmpty($(this).val()));
		});
	});
</script>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.video.options">

	<div class="checkbox">
		<label for="allowContributeVideos">
			<form:checkbox path="allowContributeVideos" id="allowContributeVideos"/>
			<fmt:message key="advanced.allowContributeVideos" />
		</label>
	</div>
	
	<div class="checkbox loffset20">
		<label for="allowSeeingOtherUsersRecordings">
			<form:checkbox path="allowSeeingOtherUsersRecordings" id="allowSeeingOtherUsersRecordings"/>
			<fmt:message key="advanced.allowSeeingOtherUsersRecordings" />
		</label>
	</div>
	
	<div class="form-inline form-group loffset20">
		<label for="learner-contribution-limit">
			<fmt:message key="advanced.learnerContributionLimit" />&nbsp;
			<form:select path="learnerContributionLimit" id="learner-contribution-limit" cssClass="form-control input-sm">
			<form:option value="-1"><fmt:message key="advanced.unlimited" /></form:option>
			<form:option value="1">1</form:option>
			<form:option value="2">2</form:option>
			<form:option value="3">3</form:option>
			<form:option value="4">4</form:option>
			<form:option value="5">5</form:option>
			<form:option value="6">6</form:option>
			<form:option value="7">7</form:option>
			<form:option value="8">8</form:option>
			<form:option value="9">9</form:option>
			<form:option value="10">10</form:option>
		</form:select>
		
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allow-comments">
			<form:checkbox path="allowComments" id="allow-comments"/>
			<fmt:message key="advanced.allowComments" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allow-ratings">
			<form:checkbox path="allowRatings" id="allow-ratings"/>
			<fmt:message key="advanced.allowRatings" />
		</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

<div class="checkbox">
	<label for="lock-on-finished">
		<form:checkbox path="lockOnFinished" id="lock-on-finished"/>
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</div>

<div class="checkbox">
	<label for="reflect-on-activity">
		<form:checkbox path="reflectOnActivity" id="reflect-on-activity" />
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<form:textarea path="reflectInstructions" id="reflect-instructions" cssClass="form-control" rows="3"	/>
</div>

</lams:SimplePanel>