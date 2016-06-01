<%@include file="/common/taglibs.jsp"%>

<c:set var="ddTitle"><fmt:message key="monitor.summary.date.restriction" /></c:set>
<lams:RestrictedUsageAccordian title="${ddTitle}">

	<div>
		<fmt:message key="monitor.summary.when.date.restriction.is.set" />
	</div>
		
	<div id="datetimeDiv" <c:if test='${not empty submissionDeadline}'> style="display: none;" </c:if>>
		<div class="form-group">
			<label for="datetime"><fmt:message key="monitor.summary.after.date" />&nbsp;
			
			<input type="text" name="datetime" id="datetime" value="" class="form-control form-control-inline"/>
			</label>
									
			<html:link	href="javascript:setSubmissionDeadline();" styleClass="btn btn-sm btn-default loffset5">
				<fmt:message key="monitor.summary.set.restriction" />
			</html:link>
		</div>
	</div>

	<div id="dateInfoDiv" <c:if test='${empty submissionDeadline}'> style="display: none;" </c:if>>
		<label>
			<span><fmt:message key="monitor.summary.after.date" /></span>
			
			<span id="dateInfo">
			</span>
		</label>
		
		<html:link	href="javascript:removeSubmissionDeadline();" styleClass="btn btn-sm btn-default loffset5">
			<fmt:message key="monitor.summary.unset.restriction" />
		</html:link>
	</div>

</lams:RestrictedUsageAccordian>