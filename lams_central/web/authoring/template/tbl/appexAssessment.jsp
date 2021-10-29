<%@ include file="/common/taglibs.jsp"%>
<%-- Application Exercise based on Assessment.
     It should have one or more essay or multiple choice questions.
     If QTI Import is used then all the questions are put into one exercise. --%>

<%-- The title needs to look like an ordinary panel title, but be editable via the X-editable javascript.
     But that will not be returned to the server in the form data, so copy what appears in the displayed span to a hidden input field.  --%>
<c:if test="${empty appexNumber}">
	<c:set var="appexNumber" value="${param.appexNumber}" />
</c:if>
<c:set var="appexTitle"><fmt:message key="authoring.label.application.exercise.num"><fmt:param value="${appexNumber}"/></fmt:message></c:set>
<c:set var="appexTitleDisplay">divappex${appexNumber}TitleDisplay</c:set>
<c:set var="appexTitleField">divappex${appexNumber}Title</c:set>


<!--  Start of panel Appex${appexNumber} -->
 <div class="panel panel-default" id="divappex${appexNumber}" >
     <div class="panel-heading" id="headingAppex${appexNumber}">
		<a href="#" onclick="javascript:deleteAppexDiv('divappex${appexNumber}', '${appexTitleField}');"
					class="btn btn-default btn-sm panel-title-button" id="deleteAssessmentButton${appexNumber}">
			<i class="fa fa-lg fa-trash-o"></i>
			<fmt:message key="authoring.fla.delete.button"/>
		</a>
		
     	<span class="panel-title collapsable-icon-left">
     		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapseAppex${appexNumber}" 
			   aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapseAppex${appexNumber}" >&nbsp;&nbsp;
			</a>
		</span>
		<span class="panel-title hoverEdit" name="${appexTitleDisplay}" id="${appexTitleDisplay}" >
			<c:out value="${appexTitle}" />
		</span>
		<span>&nbsp;</span>
		<i class='fa fa-sm fa-pencil'></i>
		<input type="hidden" name="${appexTitleField}" id="${appexTitleField}" value="${appexTitle}"/>
     </div>
    
    <div id="collapseAppex${appexNumber}" class="panel-collapse collapse in" 
    	 role="tabpanel" aria-labelledby="headingAppex${appexNumber}">	
    				
	 	<input type="hidden" name="numAssessments${appexNumber}" id="numAssessments${appexNumber}" value="0"/>
		<div id="divass${appexNumber}"></div>
		<div class="space-top space-sides">
			<div>
              <div class="dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="fa fa-plus text-primary"></i>
                    <fmt:message key="authoring.create.question"/>&nbsp;
                  <span class="caret"></span>
                </button>
                
                <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenu2">
                    <li>
                    	<a href="javascript:void(0)" id="divass${appexNumber}CEssay"
                    	   onclick="javascript:createAssessment('essay', 'numAssessments${appexNumber}', 'divass${appexNumber}');">
                    		<i class="fa fa-align-justify fa-fw text-primary"></i> <fmt:message key="authoring.create.essay.question"/>
                    	</a>
                    </li>
                    <li>
                    	<a href="javascript:void(0)" id="divass${appexNumber}CMCQ"
                    	   onclick="javascript:createAssessment('mcq', 'numAssessments${appexNumber}', 'divass${appexNumber}');">
                    	   	<i class="fa fa-list-ul fa-fw text-primary"></i> <fmt:message key="authoring.create.mc.question"/>
                    	</a>
                    </li>
                     <li>
                    	<a href="javascript:void(0)"
                    	   onclick="javascript:createApplicationExercise('doku', ${appexNumber})">
                    	   	<i class="fa fa-file-text fa-fw text-primary"></i> <fmt:message key="authoring.create.doku.question"/>&nbsp;&nbsp; 
							<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="" data-original-title="<fmt:message key="authoring.create.doku.question.tooltip"/>"></i>

                    	</a>
                    </li>
                    <li>
                    	<a href="javascript:void(0)" id="divass${appexNumber}CQB"
                    	   onClick="javascript:openQuestionBank(${appexNumber})">
                    	   	<i class="fa fa-bank fa-fw text-primary"></i> <fmt:message key="authoring.create.question.qb" />
                    	</a>
                   	</li>
                    <li role="separator" class="divider"></li>
                    <li class="dropdown-header"><fmt:message key="authoring.tbl.import.questions.from"/></li>
                    <li>
                    	<a style="margin-left: 1em;" href="javascript:void(0)" id="divass${appexNumber}CQTI"
                    	   onClick="javascript:importQTI('appex${appexNumber}', 'mc,mr,mh,es', 'word')">
                    		<i class="fa fa-file-word-o fa-fw text-primary"></i> <fmt:message key="label.qb.collection.word"/>...
                    	</a>
                    </li>
                  	<li><a style="margin-left: 1em;" href="javascript:void(0)" id="divass${appexNumber}CWord"
                  		   onClick="javascript:importQTI('appex${appexNumber}', 'mc,mr,es', 'qti')">
                  		   	<i class="fa fa-file-code-o fa-fw text-primary"></i> <fmt:message key="label.qb.collection.qti"/>...
                  		</a>
                  	</li>
                </ul>
              </div>
			</div>
		</div>
		
		<!-- Question Bank for this Application Excercise -->
		<div class="question-bank-div space-sides" id="question-bank-ae-div-${appexNumber}" role="tablist" aria-multiselectable="true"> 
		    <div class="panel panel-default">
		        <div class="panel-heading collapsable-icon-left" id="question-bank-ae-heading-${appexNumber}">
		        	<span class="panel-title">
				    	<a role="button" data-toggle="collapse" href="#question-bank-ae-collapse-${appexNumber}"
				    	   aria-expanded="true" aria-controls="question-bank-ae-collapse-${appexNumber}" >
			          		<fmt:message key="label.question.bank" />
			        	</a>
		      		</span>
		        </div>
		
				<div id="question-bank-ae-collapse-${appexNumber}" class="panel-body panel-collapse collapse in" role="tabpanel"
					 aria-labelledby="question-bank-ae-heading-${appexNumber}">
					<i class="fa fa-refresh fa-spin fa-2x fa-fw" style="margin: auto; display: block"></i>			
				</div>
			</div>
		</div>
		
		<div class="space-top space-sides space-bottom">
			<div class="panel panel-default">
		        <div class="panel-heading collapsable-icon-left" id="advanced-settings-ae-${appexNumber}-heading">
		        	<span class="panel-title">
				    	<a role="button" data-toggle="collapse" href="#advanced-settings-ae-${appexNumber}-collapse" class="collapsed"
				    	   aria-expanded="false" aria-controls="advanced-settings-ae-${appexNumber}-collapse">
			          		<fmt:message key="label.tab.advanced" />
			        	</a>
		      		</span>
		        </div>
		
				<div id="advanced-settings-ae-${appexNumber}-collapse" class="panel-body panel-collapse collapse" role="tabpanel" 
					 aria-labelledby="#advanced-settings-ae-${appexNumber}-heading">
					<div class="checkbox">
						<label for="divappex${appexNumber}NB">
							<input name="divappex${appexNumber}NB" id="divappex${appexNumber}NB" type="checkbox" value="true"/>
							<fmt:message key="authoring.tbl.use.noticeboard" />&nbsp; 
							<i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right"
							   title="<fmt:message key='authoring.tbl.use.noticeboard.tooltip'/>">
							</i>
						</label>
					</div>
					<div class="form-group" id="divappex${appexNumber}NBEntryDiv">
						<lams:CKEditor id="divappex${appexNumber}NBEntry" value="" contentFolderID="${contentFolderID}" height="100"></lams:CKEditor>
					</div>
				</div>
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
	    	onblur: 'ignore',
	        type: 'text',
	 	    validate: validateXEditable,
	        success: function(response, newValue) {
	        	var trimmedValue = newValue.trim();
	        	$('#${appexTitleDisplay}').val(trimmedValue);
	        	$('#${appexTitleField}').val(trimmedValue);
	       }
	    }).on('shown', onShownForXEditable)
		  .on('hidden', onHiddenForXEditable);

		$('[data-toggle="tooltip"]').tooltip();
	</script>
	
</div> 
<!--  End of panel Appex${appexNumber} -->
