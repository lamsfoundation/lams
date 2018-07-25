<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript"> 
	$(document).ready(function(){

		//handles disabled state
		$("#datetime").change(function() {
			$("#show-other-answers-after-deadline").prop("disabled", ($("#datetime").val() == ""));
			if ($("#datetime").val() == "") {
				$("#show-other-answers-after-deadline").prop("checked", false);
			}
		});

		//
	    $('#show-other-answers-after-deadline').change(function() {
	        if($("#datetime").is(":hidden")) {
	        	storeShowOtherAnswersAfterDeadline();
	        }       
	    });
	});

	function storeShowOtherAnswersAfterDeadline() {
		if ($("#datetime").is(":not(:hidden)") && $("#datetime").datetimepicker('getDate') == null) {
			return;
		}
		
		$.ajax({
			url : '<c:url value="/monitoring.do?dispatch=setShowOtherAnswersAfterDeadline"/>',
			async: false,
			data: {
				toolContentID:'${content.qaContentId}',
				showOtherAnswersAfterDeadline: $("#show-other-answers-after-deadline").is(":checked"),
				reqID: (new Date()).getTime()
			},
			success : function() {
			}
		});
	}

</script>

<c:set var="ddTitle"><fmt:message key="monitor.summary.date.restriction" /></c:set>
<lams:RestrictedUsageAccordian title="${ddTitle}">

	<p><fmt:message key="monitor.summary.when.date.restriction.is.set" /></p>
		
	<div id="datetimeDiv" <c:if test='${not empty submissionDeadline}'> style="display: none;" </c:if>>
		<div class="form-group">
		<label for="datetime"><fmt:message key="monitor.summary.after.date" />&nbsp;
		<input type="text" name="datetime" id="datetime" value="" class="form-control form-control-inline"/>
		</label>							
		<html:link	href="javascript:storeShowOtherAnswersAfterDeadline(); setSubmissionDeadline();" styleClass="btn btn-default">
			<fmt:message key="monitor.summary.set.restriction" />
		</html:link>
		</div>
	</div>

	<div id="dateInfoDiv" <c:if test='${empty submissionDeadline}'> style="display: none;" </c:if>>
		<label><fmt:message key="monitor.summary.after.date" /></span>
		<span id="dateInfo">
		</span>
		</label>
		<html:link	href="javascript:$('#show-other-answers-after-deadline').prop('checked', false); removeSubmissionDeadline();" styleClass="btn btn-default">
			<fmt:message key="monitor.summary.unset.restriction" />
		</html:link>
	</div>
	
	<div class="checkbox">
		<label for="show-other-answers-after-deadline">
			<input type="checkbox" name="showOtherAnswersAfterDeadline" id="show-other-answers-after-deadline" value="1" 
				<c:if test="${empty submissionDeadline}">disabled="disabled"</c:if>
				<c:if test="${content.showOtherAnswersAfterDeadline}">checked="checked"</c:if>
			></input>
			<fmt:message key="label.allow.review.other.responses" />
		</label>
	</div>
	
</lams:RestrictedUsageAccordian>