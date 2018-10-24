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

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
		<label for="reflectOnActivity">
		<form:checkbox path="reflectOnActivity" value="true" id="reflectOnActivity"/>
		<fmt:message key="advanced.reflectOnActivity" />
		</label>
	</div>
	<div class="form-group">
		<form:textarea path="reflectInstructions" rows="3" id="reflectInstructions" cssClass="form-control"/>
	</div>
</lams:SimplePanel>

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
