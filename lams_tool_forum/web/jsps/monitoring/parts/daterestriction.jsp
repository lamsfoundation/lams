<%@include file="/common/taglibs.jsp"%>
<c:set var="ddTitle"><spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction" /></spring:escapeBody></c:set>
<lams:RestrictedUsageAccordian title="${ddTitle}">
	<p>
		<fmt:message key="monitor.summary.when.date.restriction.is.set" />
	</p>
		
	<div id="datetimeDiv" <c:if test='${not empty forum.submissionDeadline}'> style="display: none;" </c:if>>
		<div class="row align-items-center">
			<div class="col-auto pe-0">
				<label for="datetime">
					<fmt:message key="monitor.summary.after.date" />
				</label>
			</div>
			
			<div class="col-auto pe-0">
				<input type="text" name="datetime" id="datetime" value="" class="form-control form-control-inline" autocomplete="off"/>
			</div>
			
			<div class="col-auto pe-0">
				<button type="button" onclick="setSubmissionDeadline();" class="btn btn-secondary">
					<i class="fa-solid fa-wrench me-1"></i>
					<fmt:message key="monitor.summary.set.restriction" />
				</button>
			</div>
		</div>
	</div>

	<div id="dateInfoDiv" <c:if test='${empty forum.submissionDeadline}'> style="display: none;" </c:if>>
		<span>
			<fmt:message key="monitor.summary.after.date" />
			<span id="dateInfo" class="badge text-bg-danger mx-2"></span>
		</span>
		
		<button type="button" onclick="removeSubmissionDeadline();" class="btn btn-secondary">
			<i class="fa-solid fa-trash-can me-1"></i>
			<fmt:message key="monitor.summary.unset.restriction" />
		</button>
	</div>
</lams:RestrictedUsageAccordian>
