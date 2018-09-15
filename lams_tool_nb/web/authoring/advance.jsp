<%@ include file="/includes/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:CommentsAuthor/>

<lams:OutcomeAuthor toolContentId="${nbAuthoringForm.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">

<div class="checkbox">
	<label for="reflectOnActivity">
    <form:checkbox path="reflectOnActivity" value="1" cssClass="noBorder" id="reflectOnActivity"/><fmt:message key="advanced.reflectOnActivity" />
	</label>
</div>
<div class="form-group">
	<form:textarea path="reflectInstructions" cols="60" rows="3"  id="reflectInstructions" cssClass="form-control"></form:textarea>
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
