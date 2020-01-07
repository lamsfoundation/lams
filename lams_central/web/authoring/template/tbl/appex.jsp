<%@ include file="/common/taglibs.jsp"%>
<%-- Application Exercise. It should have one or more essay or multiple choice questions. If QTI Import is used then all the questions are put into one exercise. --%>

<%-- The title needs to look like an ordinary panel title, but be editable via the X-editable javascript. But that won't be returned to the server in the form data, so copy what appears in the displayed span to a hidden input field.  --%>
<c:set var="appexTitle"><fmt:message key="authoring.label.application.exercise.num"><fmt:param value="${appexNumber}"/></fmt:message></c:set>
<c:set var="appexTitleDisplay">divappex${appexNumber}TitleDisplay</c:set>
<c:set var="appexTitleField">divappex${appexNumber}Title</c:set>

<!--  Start of panel Appex${appexNumber} -->
 <div class="panel panel-default" id="divappex${appexNumber}" >
     <div class="panel-heading" id="headingAppex${appexNumber}">
		<c:if test="${appexNumber > 1}"><a href="#" onclick="javascript:deleteAppexDiv('divappex${appexNumber}', '${appexTitleField}');" class="btn btn-default btn-sm panel-title-button" id="deleteAssessmentButton${appexNumber}">
			<i class="fa fa-lg fa-trash-o"></i> <fmt:message key="authoring.fla.delete.button"/></a></c:if>
     	<span class="panel-title collapsable-icon-left">
     		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapseAppex${appexNumber}" 
			aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapseAppex${appexNumber}" >&nbsp;
			</a>
		</span>
			${appexNumber eq 1 ? "<label class=\"required\">" : ""}
			<span class="panel-title hoverEdit" name="${appexTitleDisplay}" id="${appexTitleDisplay}" ><c:out value="${appexTitle}" /></span><span>&nbsp;</span><i class='fa fa-sm fa-pencil'></i>
			${appexNumber eq 1 ? "</label>" : ""}
			<input type="hidden" name="${appexTitleField}" id="${appexTitleField}" value="${appexTitle}"/>
     </div>
    
    <div id="collapseAppex${appexNumber}" class="panel-collapse collapse in" 
    		role="tabpanel" aria-labelledby="headingAppex${appexNumber}">	
    				
		 	<input type="hidden" name="numAssessments${appexNumber}" id="numAssessments${appexNumber}" value="0"/>
			<div id="divass${appexNumber}"></div>
			<div class="space-top space-sides">
				<a href="#" id="divass${appexNumber}CEssay" onclick="javascript:createAssessment('essay', 'numAssessments${appexNumber}', 'divass${appexNumber}');" class="btn btn-default"><i class="fa fa-plus"></i> <fmt:message key="authoring.create.essay.question"/></a>
				<a href="#" id="divass${appexNumber}CMCQ"  onclick="javascript:createAssessment('mcq', 'numAssessments${appexNumber}', 'divass${appexNumber}');" class="btn btn-default"><i class="fa fa-plus"></i> <fmt:message key="authoring.create.mc.question"/></a>
				<a href="#" id="divass${appexNumber}CQTI" onClick="javascript:importQTI('appex${appexNumber}')" class="btn btn-default pull-right"><i class="fa fa-upload"></i> <fmt:message key="authoring.template.basic.import.qti" /></a>
			</div>
			<div class="space-top space-sides space-bottom">
				<div class="checkbox"><label for="divappex${appexNumber}NB">
					<input name="divappex${appexNumber}NB" id="divappex${appexNumber}NB" type="checkbox" value="true"/> <fmt:message key="authoring.tbl.use.noticeboard" />&nbsp; 
					<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="<fmt:message key='authoring.tbl.use.noticeboard.tooltip'/>"></i>
				</label></div>
				<div class="form-group" id="divappex${appexNumber}NBEntryDiv">
					<lams:CKEditor id="divappex${appexNumber}NBEntry" value="" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
				</div>
			</div>
	</div>
	
	<script type="text/javascript">
	//only show Noticeboard area if the checkbox is on.
	// by hiding we should get correct inline when we show
	$('#divappex${appexNumber}NBEntryDiv').hide();	 
	var instructions${appexNumber} = document.getElementById("");
	$("#divappex${appexNumber}NB").change(function(){
		var isChecked = document.getElementById("divappex${appexNumber}NB").checked;
		if ( isChecked ) {
			$('#divappex${appexNumber}NBEntryDiv').show(showTime);
		} else {
			$('#divappex${appexNumber}NBEntryDiv').hide(showTime);
		}
	}); 
	
    $('#${appexTitleDisplay}').editable({
    	mode: 'inline',
        type: 'text',
 	    validate: validateXEditable,
        success: function(response, newValue) {
        	var trimmedValue = newValue.trim();
        	$('#${appexTitleDisplay}').val(trimmedValue);
        	$('#${appexTitleField}').val(trimmedValue);
       }
    }).on('shown', onShownForXEditable)
	  .on('hidden', onHiddenForXEditable);
    
	</script>
	
</div> 
<!--  End of panel Appex${appexNumber} -->
