<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

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
		
		<c:if test="${!formBean.allowContributeVideos}">
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
			<html:checkbox property="allowContributeVideos" styleId="allowContributeVideos"/>
			<fmt:message key="advanced.allowContributeVideos" />
		</label>
	</div>
	
	<div class="checkbox loffset20">
		<label for="allowSeeingOtherUsersRecordings">
			<html:checkbox property="allowSeeingOtherUsersRecordings" styleId="allowSeeingOtherUsersRecordings"/>
			<fmt:message key="advanced.allowSeeingOtherUsersRecordings" />
		</label>
	</div>
	
	<div class="form-inline form-group loffset20">
		<label for="learner-contribution-limit">
			<fmt:message key="advanced.learnerContributionLimit" />&nbsp;
			<html:select property="learnerContributionLimit" styleId="learner-contribution-limit" styleClass="form-control input-sm">
			<html:option value="-1"><fmt:message key="advanced.unlimited" /></html:option>
			<html:option value="1">1</html:option>
			<html:option value="2">2</html:option>
			<html:option value="3">3</html:option>
			<html:option value="4">4</html:option>
			<html:option value="5">5</html:option>
			<html:option value="6">6</html:option>
			<html:option value="7">7</html:option>
			<html:option value="8">8</html:option>
			<html:option value="9">9</html:option>
			<html:option value="10">10</html:option>
		</html:select>
		
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allow-comments">
			<html:checkbox property="allowComments" styleId="allow-comments"/>
			<fmt:message key="advanced.allowComments" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="allow-ratings">
			<html:checkbox property="allowRatings" styleId="allow-ratings"/>
			<fmt:message key="advanced.allowRatings" />
		</label>
	</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

<div class="checkbox">
	<label for="lock-on-finished">
		<html:checkbox property="lockOnFinished" styleId="lock-on-finished"/>
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</div>

<div class="checkbox">
	<label for="reflect-on-activity">
		<html:checkbox property="reflectOnActivity" styleId="reflect-on-activity" />
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="reflectInstructions" styleId="reflect-instructions" styleClass="form-control" rows="3"	/>
</div>

</lams:SimplePanel>