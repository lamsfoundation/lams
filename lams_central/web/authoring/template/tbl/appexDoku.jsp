<%@ include file="/common/taglibs.jsp"%>
<%-- Application Exercise based on Dokumaran --%>

<%-- The title needs to look like an ordinary panel title, but be editable via the X-editable javascript.
     But that will not be returned to the server in the form data, so copy what appears in the displayed span to a hidden input field.  --%>
<c:if test="${empty appexNumber}">
	<c:set var="appexNumber" value="${param.appexNumber}" />
</c:if>
<c:set var="appexTitle"><fmt:message key="authoring.label.application.exercise.num"><fmt:param value="${param.appexNumber}"/></fmt:message></c:set>
<c:set var="appexTitleDisplay">divappex${param.appexNumber}TitleDisplay</c:set>
<c:set var="appexTitleField">divappex${param.appexNumber}Title</c:set>

<!--  Start of panel Appex${param.appexNumber} -->
 <div class="panel panel-default" id="divappex${param.appexNumber}" >
     <div class="panel-heading" id="headingAppex${param.appexNumber}">
		<a href="#" onclick="javascript:deleteAppexDiv('divappex${param.appexNumber}', '${appexTitleField}');"
					class="btn btn-default btn-sm panel-title-button" id="deleteAssessmentButton${param.appexNumber}">
			<i class="fa fa-lg fa-trash-o"></i>
			<fmt:message key="authoring.fla.delete.button"/>
		</a>
		
     	<span class="panel-title collapsable-icon-left">
     		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapseAppex${param.appexNumber}" 
			   aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapseAppex${param.appexNumber}" >&nbsp;&nbsp;
			</a>
		</span>
		<span class="panel-title hoverEdit" name="${appexTitleDisplay}" id="${appexTitleDisplay}" >
			<c:out value="${appexTitle}" />
		</span>
		<span>&nbsp;</span>
		<i class='fa fa-sm fa-pencil'></i>
		<input type="hidden" name="${appexTitleField}" id="${appexTitleField}" value="${appexTitle}"/>
		<span><fmt:message key="authoring.label.application.exercise.type.doku"/></span>
     </div>
    
    <div id="collapseAppex${param.appexNumber}" class="panel-body panel-collapse collapse in" 
    	 role="tabpanel" aria-labelledby="headingAppex${param.appexNumber}">	
		<div class="form-group">
		    <label for="divappex${param.appexNumber}dokuDescription"><fmt:message key="authoring.label.application.exercise.doku.description" /></label>
		    <lams:CKEditor id="divappex${param.appexNumber}dokuDescription" value="" contentFolderID="${param.contentFolderID}"/>
		</div>
		
		<div class="form-group">
		    <label for="divappex${param.appexNumber}dokuInstructions">
		    	<fmt:message key="authoring.label.application.exercise.doku.instructions" />
		    </label>
		    <lams:CKEditor id="divappex${param.appexNumber}dokuInstructions" value="" contentFolderID="${param.contentFolderID}" height="200" toolbarSet="DefaultDoku" method="inline"/>
		</div>

		<div class="space-top space-sides space-bottom">
			<div class="checkbox">
				<label for="divappex${param.appexNumber}NB">
					<input name="divappex${param.appexNumber}NB" id="divappex${param.appexNumber}NB" type="checkbox" value="true"/>
					<fmt:message key="authoring.tbl.use.noticeboard" />&nbsp; 
					<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right"
					   title="<fmt:message key='authoring.tbl.use.noticeboard.tooltip'/>">
					</i>
				</label>
			</div>
			<div class="form-group" id="divappex${param.appexNumber}NBEntryDiv">
				<lams:CKEditor id="divappex${param.appexNumber}NBEntry" value="" contentFolderID="${param.contentFolderID}" height="100"></lams:CKEditor>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		//only show Noticeboard area if the checkbox is on.
		// by hiding we should get correct inline when we show
		$('#divappex${param.appexNumber}NBEntryDiv').hide();	 
		var instructions${param.appexNumber} = document.getElementById("");
		$("#divappex${param.appexNumber}NB").change(function(){
			var isChecked = document.getElementById("divappex${param.appexNumber}NB").checked;
			if ( isChecked ) {
				$('#divappex${param.appexNumber}NBEntryDiv').show(showTime);
			} else {
				$('#divappex${param.appexNumber}NBEntryDiv').hide(showTime);
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
<!--  End of panel Appex${param.appexNumber} -->
