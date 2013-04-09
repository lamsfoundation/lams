<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script lang="javascript">
	$(document).ready(function(){
		$("#allowContributeVideos").click(function() {
			if ($(this).is(':checked')) {
				$("#allowSeeingOtherUsersRecordings").prop("disabled", false);
				$("#learnerContributionLimit").prop("disabled", false);
				$("#allow-teacher-cue-points").prop("disabled", false);
			} else {
				$("#allowSeeingOtherUsersRecordings").prop("disabled", true);
				$("#allowSeeingOtherUsersRecordings").prop("checked", false);
				$("#learnerContributionLimit").prop("disabled", true);
				$("#learnerContributionLimit").prop("value", -1);
				$("#allow-teacher-cue-points").prop("checked", false);
				$("#allow-teacher-cue-points").prop("disabled", true);
			}
		});
		
		<c:if test="${!formBean.allowContributeVideos}">
			$("#allowSeeingOtherUsersRecordings").prop("disabled", true);
			$("#learnerContributionLimit").prop("disabled", true);
			$("#allow-teacher-cue-points").prop("disabled", true);
		</c:if>
		
		//time stamped comments
		<c:choose>
			<c:when test="${formBean.allowTeacherCuePoints || formBean.allowLernerCuePoints}">
				$("#allow-time-stamped-comments").prop("checked", true);
			</c:when>
			<c:otherwise>
				$('#allow-time-stamped-comments-area').css("display", "none");
			</c:otherwise>
		</c:choose>
		$("#allow-time-stamped-comments").click(function(){
			$('#allow-time-stamped-comments-area').toggle('slow');
			$('#allow-lerner-cue-points').prop("checked", false);
			$('#allow-teacher-cue-points').prop("checked", false);
		});
		$("#allow-teacher-cue-points").click(function(){
			$('#allow-lerner-cue-points').prop("checked", false);
		});
		$("#allow-lerner-cue-points").click(function(){
			$('#allow-teacher-cue-points').prop("checked", false);
		});
		
	});

</script>


<!-- ========== Advanced Tab ========== -->

<p class="small-space-top">
	<html:checkbox property="lockOnFinished" value="1" styleClass="noBorder" styleId="lockOnFinished"/>
	<label for="lockOnFinished">
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</p>

<p class="small-space-top">
	<html:checkbox property="allowContributeVideos" value="1" styleClass="noBorder" styleId="allowContributeVideos"/>
	<label for="allowContributeVideos">
		<fmt:message key="advanced.allowContributeVideos" />
	</label>
</p>

<p>
	<html:checkbox property="allowSeeingOtherUsersRecordings" value="1" styleClass="noBorder" styleId="allowSeeingOtherUsersRecordings" />
	<label for="allowSeeingOtherUsersRecordings">
		<fmt:message key="advanced.allowSeeingOtherUsersRecordings" />
	</label>
</p>

<p>
	<label for="learnerContributionLimit">
		<fmt:message key="advanced.learnerContributionLimit" />
	</label>
	<html:select property="learnerContributionLimit" styleId="learnerContributionLimit">
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
</p>

<p class="small-space-top">

	<input type="checkbox" value="" id="allow-time-stamped-comments" class="noBorder">
	<label for="allow-time-stamped-comments">
		<fmt:message key="label.time.stamped.comments" />
	</label>

	<div id="allow-time-stamped-comments-area" style="padding-left: 50px;">
		<html:radio property="allowTeacherCuePoints" value="true" styleClass="noBorder" styleId="allow-teacher-cue-points" />
		<label for="allow-teacher-cue-points">
			<fmt:message key="label.teacher.can.leave.personal.comments" />
		</label>
		<br><br>
	
		<html:radio property="allowLernerCuePoints" value="true" styleClass="noBorder" styleId="allow-lerner-cue-points" />
		<label for="allow-lerner-cue-points">
			<fmt:message key="label.allow.lerners.create.cue.points" />
		</label>
	</div>
</p>

<p>
	<html:checkbox property="allowComments" value="1" styleClass="noBorder" styleId="allowComments"/>
	<label for="allowComments">
		<fmt:message key="advanced.allowComments" />
	</label>
</p>

<p>
	<html:checkbox property="allowRatings" value="1" styleClass="noBorder" styleId="allowRatings"/>
	<label for="allowRatings">
		<fmt:message key="advanced.allowRatings" />
	</label>
</p>

<p>
	<html:checkbox property="reflectOnActivity" styleClass="noBorder" styleId="reflectOnActivity"/>
	<label for="reflectOnActivity">
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
</p>
<script type="text/javascript">
	<!--
	//automatically turn on refect option if there are text input in refect instruction area
		var ra = document.getElementById("reflectInstructions");
		var rao = document.getElementById("reflectOnActivity");
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

