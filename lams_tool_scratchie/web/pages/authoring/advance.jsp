<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="activitiesProvidingConfidenceLevels" value="${sessionMap.activitiesProvidingConfidenceLevels}" />
<c:set var="activitiesProvidingVsaAnswers" value="${sessionMap.activitiesProvidingVsaAnswers}" />

<script lang="javascript">
	$(document).ready(function(){
		$("#display-confidence-levels-activities").click(function() {
			var disabled = !$(this).is(':checked');
			$("#confidence-levels-activity").prop("disabled", disabled);
			
			var confidenceLevelsAnonymousCheckbox = $("#confidence-levels-anonymous").prop('disabled', disabled).closest('.checkbox');
			if (disabled) {
				confidenceLevelsAnonymousCheckbox.slideUp();
			} else {
				confidenceLevelsAnonymousCheckbox.slideDown();
			}
		});
		<c:if test="${authoringForm.scratchie.confidenceLevelsActivityUiid == null}">
			$("#confidence-levels-activity").prop('disabled', true);
		</c:if>
		
		if (!$("#display-confidence-levels-activities").is(':checked')) {
			$("#confidence-levels-anonymous").prop('disabled', true).closest('.checkbox').hide();
		}
		
		
		$("#display-activities-providing-vsa-answers").click(function() {
			$("#activity-providing-vsa-answers").prop("disabled", !$(this).is(':checked'));
		});
		<c:if test="${authoringForm.scratchie.activityUiidProvidingVsaAnswers == null}">$("#activity-providing-vsa-answers").prop('disabled','disabled'); </c:if>
		
		$("#overwrite-preset-marks").click(function() {
			$("#preset-marks").prop("disabled", !$(this).is(':checked'));
		});
		<c:choose>
			<c:when test="${sessionMap.isPresetMarksOverwritten}">
				$("#collapseAdvanced").collapse("show");
			</c:when>
			<c:otherwise>
				$("#preset-marks").val("${sessionMap.defaultPresetMarks}"); 
				$("#preset-marks").prop('disabled','disabled');
			</c:otherwise>
		</c:choose>
		
	});
</script>

<lams:SimplePanel titleKey="label.scratchie.options">
	<c:if test="${sessionMap.isEnabledExtraPointOption}">
		<div class="checkbox">
			<label for="extraPoint">
				<form:checkbox path="scratchie.extraPoint" id="extraPoint"/>
				<fmt:message key="label.authoring.advanced.give.extra.point" />
			</label>
		</div>
	</c:if>
	
	<div class="checkbox">
		<label for="burning-questions-enabled">
			<form:checkbox path="scratchie.burningQuestionsEnabled" id="burning-questions-enabled"/>
			<fmt:message key="label.authoring.advanced.burning.questions" />
		</label>
	</div>
	
	<c:if test="${sessionMap.isQuestionEtherpadEnabled}">
		<div class="checkbox">
			<label for="question-etherpad-enabled">
				<form:checkbox path="scratchie.questionEtherpadEnabled" id="question-etherpad-enabled"/>
				<fmt:message key="label.authoring.advanced.question.etherpad" />
			</label>
		</div>
	</c:if>
	
	<div class="checkbox">
		<label for="shuffle-items">
			<form:checkbox path="scratchie.shuffleItems" id="shuffle-items"/>
			<fmt:message key="label.authoring.advanced.shuffle.items" />
		</label>
	</div>
	
	<div class="checkbox">
		<label for="show-scratchies-in-results">
			<form:checkbox path="scratchie.showScrachiesInResults" styleId="show-scratchies-in-results"/>
			<fmt:message key="label.authoring.advanced.show.scratchies.in.results" />
		</label>
	</div>
	
	<div class="form-inline">
		<label for="time-limit">
			<fmt:message key="label.time.limit" />&nbsp;
			<form:input path="scratchie.timeLimit" size="3" id="time-limit" cssClass="form-control input-sm"/>
		</label>
	</div>
	
	<div class="checkbox">
		<c:if test="${activitiesProvidingConfidenceLevels == null}">
			<div class="alert-warning" style="margin-bottom: 5px;">
				<fmt:message key="label.save.learning.design" />
			</div>
		</c:if>
		
		<c:choose>
			<c:when test="${fn:length(activitiesProvidingConfidenceLevels) == 0}">
				<fmt:message key="label.no.confidence.levels.activities" />
			</c:when>
				
			<c:otherwise>
				<label for="display-confidence-levels-activities">
					<input type="checkbox" id="display-confidence-levels-activities"
							<c:if test="${authoringForm.scratchie.confidenceLevelsActivityUiid != null}">checked="true"</c:if>
					/>
				
					<fmt:message key="label.show.confidence.level" />&nbsp;
					<form:select path="scratchie.confidenceLevelsActivityUiid" cssClass="form-control input-sm" id="confidence-levels-activity">
						<c:forEach var="confidenceProvidingActivity" items="${activitiesProvidingConfidenceLevels}">
							<form:option value="${confidenceProvidingActivity.activityUIID}">${confidenceProvidingActivity.title}</form:option>
						</c:forEach>
					</form:select>
				</label>
				
				<div class="checkbox loffset20">
					<label for="confidence-levels-anonymous">
						<form:checkbox path="scratchie.confidenceLevelsAnonymous" id="confidence-levels-anonymous"/>
						<fmt:message key="label.anonymous.confidence.level" />
					</label>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="checkbox">
		<c:choose>
			<c:when test="${fn:length(activitiesProvidingVsaAnswers) == 0}">
				<fmt:message key="label.no.activities.provide.vsa.answers" />
			</c:when>
				
			<c:otherwise>
				<label for="display-activities-providing-vsa-answers">
					<input type="checkbox" id="display-activities-providing-vsa-answers"
							<c:if test="${authoringForm.scratchie.activityUiidProvidingVsaAnswers != null}">checked="true"</c:if>
					/>
				
					<fmt:message key="label.show.vsa.answers" />&nbsp;
					<form:select path="scratchie.activityUiidProvidingVsaAnswers" cssClass="form-control input-sm" id="activity-providing-vsa-answers">
						<c:forEach var="activityProvidingVsaAnswers" items="${activitiesProvidingVsaAnswers}">
							<form:option value="${activityProvidingVsaAnswers.activityUIID}">${activityProvidingVsaAnswers.title}</form:option>
						</c:forEach>
					</form:select>
				</label>
			</c:otherwise>
		</c:choose>
	</div>
</lams:SimplePanel>

<c:set var="adTitle"><fmt:message key="label.change.marking.allocation" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<div class="panel-body ">
		<div class="alert alert-warning">
			<fmt:message key="label.change.marking.allocation.warning" />
		</div>
	
		<div class="checkbox">
			<label for="overwrite-preset-marks">
				<input type="checkbox" id="overwrite-preset-marks"
					<c:if test="${sessionMap.isPresetMarksOverwritten}">checked="true"</c:if>
				/>
				<fmt:message key="label.modify.default.marking" />
			</label>
		</div>
		
		<div class="form-inline">
			<label for="preset-marks">
				<fmt:message key="admin.preset.marks" />&nbsp;
				<form:input id="preset-marks" path="scratchie.presetMarks" size="50" maxlength="255" cssClass="form-control form-control-inline"	/>
			</label>
		</div>
	</div>
</lams:AdvancedAccordian>

<lams:OutcomeAuthor toolContentId="${authoringForm.scratchie.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="reflect-on">
			<form:checkbox path="scratchie.reflectOnActivity" id="reflect-on"/>
			<fmt:message key="label.authoring.advanced.reflectOnActivity" />
		</label>
	</div>
	
	<div class="form-group">
		<form:textarea path="scratchie.reflectInstructions"  cssClass="form-control" id="reflectInstructions" rows="3" />
	</div>
</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflect-on");
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
	
	//disable #shuffle-items when burning questions option is ON
	$("#burning-questions-enabled").click(function() {
		var isBurningQuestionsOn = $("#burning-questions-enabled").is(":checked");
	
		$("#shuffle-items").prop("disabled", isBurningQuestionsOn);
		if (isBurningQuestionsOn) {
			$("#shuffle-items").prop("checked", false);
		}
	});
	
	<c:if test="${authoringForm.scratchie.burningQuestionsEnabled}">$("#shuffle-items").prop("disabled", true);</c:if>
	
</script>
