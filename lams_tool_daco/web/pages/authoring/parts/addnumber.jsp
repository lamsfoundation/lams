<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css/>
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var msgShowAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.show' />";
       var msgHideAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.hide' />";
	   	//Initial behavior
	   	$(document).ready(function() {
	   		defaultShowAdditionaOptionsArea();
	   	});
	</script>
</lams:head>
<body class="tabpart">

<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.authoring.basic.number" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="3" />
	<html:hidden property="questionIndex" />

	<p><fmt:message key="label.authoring.basic.number.help" /></p>

	<%@ include file="description.jsp"%>
  
  	<!--  Options -->  
	<a href="javascript:toggleAdditionalOptionsArea()" class="fa-xs"><i id="faIcon" class="fa fa-plus-square-o"></i> <span id="toggleAdditionalOptionsAreaLink"><fmt:message key="label.authoring.basic.additionaloptions.show" /></span></a>
	<div id="additionalOptionsArea" style="display: none;" class="panel-body">

			<div class="form-group">
	    	<label for=min><fmt:message key="label.common.min" />: </label>
				<html:text styleId="min" property="min" size="5" styleClass="form-control-inline input-sm"/>
	    	<label class="roffset10" for="max"><fmt:message key="label.common.max" />: </label>
				<html:text styleId="max" property="max" size="5" styleClass="form-control-inline input-sm"/>
			</div>
			<div class="form-group">
	    	<label for="summary"><fmt:message key="label.common.summary" />: </label>
				<html:select property="summary" styleClass="form-control-inline input-sm">
					<html:option value="0" styleId="noSummaryOption"><fmt:message key="label.common.summary.none" /></html:option>
					<html:option value="1"><fmt:message key="label.common.summary.sum" /></html:option>
					<html:option value="2"><fmt:message key="label.common.summary.average" /></html:option>
					<html:option value="3"><fmt:message key="label.common.summary.count" /></html:option>
				</html:select>
			</div>
			<div class="checkbox">
			    <label>
		 	      <html:checkbox property="questionRequired" styleId="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
			    </label>
		  </div>
	</div>	
 	<!--  end options -->
  
 </html:form>

<c:set var="addButtonMessageKey" value="label.authoring.basic.number.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>

</body>
</lams:html>
