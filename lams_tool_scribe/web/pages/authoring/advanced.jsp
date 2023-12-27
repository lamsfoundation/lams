<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.scribe.options">

	<strong></b><fmt:message key="advanced.selectScribe" /></strong>
	
	<div class="radio">
		<label for="autoSelectScribe1">
			<form:radiobutton path="autoSelectScribe" checked="checked" value="true" id="autoSelectScribe1"/>
			<fmt:message key="advanced.firstLearner" />
		</label>
	</div>
	<div class="radio">
		<label for="autoSelectScribe2">
			<form:radiobutton path="autoSelectScribe" value="false" id="autoSelectScribe2"/>
			<fmt:message key="advanced.selectInMonitor" />
		</label>
	</div>
	
	<div class="checkbox voffset20">
		<label for="showAggregatedReports">
		<form:checkbox path="showAggregatedReports" value="true" id="showAggregatedReports"/>
		<fmt:message key="advanced.showAggregatedReports" />
		<br/>
		<span class="help-block"><fmt:message key="advanced.showAggregatedReportsNote"/></span>
		</label>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

