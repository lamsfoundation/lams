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

<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="restrictUsageTreeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.date.restriction" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="restrictUsageDiv" style="display:none; ">

	<div>
		<fmt:message key="monitor.summary.when.date.restriction.is.set" />
	</div>
		
	<div id="datetimeDiv" <c:if test='${not empty submissionDeadline}'> style="display: none;" </c:if>>
		<span>
			<fmt:message key="monitor.summary.after.date" />
		</span>
		
		<input type="text" name="datetime" id="datetime" value=""/>
				
		<html:link	href="javascript:storeShowOtherAnswersAfterDeadline(); setSubmissionDeadline();" styleClass="button">
			<fmt:message key="monitor.summary.set.restriction" />
		</html:link>
	</div>

	<div id="dateInfoDiv" <c:if test='${empty submissionDeadline}'> style="display: none;" </c:if>>
		<span>
			<fmt:message key="monitor.summary.after.date" />
		</span>
		
		<span id="dateInfo">
		</span>
				
		<html:link	href="javascript:$('#show-other-answers-after-deadline').prop('checked', false); removeSubmissionDeadline();" styleClass="button">
			<fmt:message key="monitor.summary.unset.restriction" />
		</html:link>
	</div>
	
	<div class="small-space-top">
		<input type="checkbox" name="showOtherAnswersAfterDeadline" value="1" id="show-other-answers-after-deadline" class="noBorder"
			<c:if test="${empty submissionDeadline}">disabled="disabled"</c:if>
			<c:if test="${content.showOtherAnswersAfterDeadline}">checked="checked"</c:if>
		/>
		<label for="show-other-answers-after-deadline">
			<fmt:message key="label.allow.review.other.responses" />
		</label>
	</div>

</div>
