<%@include file="/common/taglibs.jsp"%>

<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="restrictUsageTreeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.date.restriction" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="restrictUsageDiv" style="display:none; ">

	<p>
		<fmt:message key="monitor.summary.when.date.restriction.is.set" />
	</p	>
		
	<div id="datetimeDiv" <c:if test='${not empty submissionDeadline}'> style="display: none;" </c:if>>
		<span>
			<fmt:message key="monitor.summary.after.date" />
		</span>
		
		<input type="text" name="datetime" id="datetime" value=""/>
				
		<html:link	href="javascript:setSubmissionDeadline();" styleClass="button">
			<fmt:message key="monitor.summary.set.restriction" />
		</html:link>
	</div>

	<div id="dateInfoDiv" <c:if test='${empty submissionDeadline}'> style="display: none;" </c:if>>
		<span>
			<fmt:message key="monitor.summary.after.date" />
		</span>
		
		<span id="dateInfo">
		</span>
				
		<html:link	href="javascript:removeSubmissionDeadline();" styleClass="button">
			<fmt:message key="monitor.summary.unset.restriction" />
		</html:link>
	</div>

</div>