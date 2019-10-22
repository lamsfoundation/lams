<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="3"	scope="request" />

<lams:html>
	<lams:head>
		<%@ include file="addQuestionHeader.jsp"%>
		<style>
			textarea {
			  resize: none;
			}
		</style>
		
  	    <script>
			$(document).ready(function(){
		    	$("#assessmentQuestionForm").validate({
		    		ignore: 'hidden, div.cke_editable',
		    		rules: {
		    			title: "required",
		    			maxMark: {
		    			      required: true,
		    			      digits: true
		    			},
		    			penaltyFactor: {
		    			      required: true,
		    			      number: true
		    			},
		    			hasOptionFilled: {
		    				required: function(element) {
		    		        	return $("textarea[name^=optionName]:filled").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOnePositiveMaxMark: {
		    				required: function(element) {
		    					var hasOnePositiveMaxMark = false;
		    					$("input[name^='optionMaxMark']").each(function() {
		    						hasOnePositiveMaxMark = hasOnePositiveMaxMark || (eval(this.value) > 0);
		    					});
	    			    		return !hasOnePositiveMaxMark;
			    		    }		    		    
	    			    }
		    		},
		    		messages: {
		    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
		    			maxMark: {
		    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    				digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
		    			},
		    			penaltyFactor: {
		    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    				number: "<fmt:message key='label.authoring.choice.enter.float'/>"
		    			},
		    			hasOptionFilled: "<fmt:message key='label.authoring.numerical.error.answer'/>",
		    			hasOnePositiveMaxMark: {
		    				required: "<fmt:message key='error.positive.grade.required'/>"
		    			}
		    		},
     			    submitHandler: function(form) {
     			    	prepareOptionEditorsForAjaxSubmit();     			    
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#description").val(CKEDITOR.instances.description.getData());
		    			$("#feedback").val(CKEDITOR.instances.feedback.getData());
		    			$("#new-collection-uid").val($("#collection-uid-select option:selected").val());
		    			
		    	    	var options = { 
		    	    		target:  parent.jQuery('#itemArea'), 
		    		   		success: afterRatingSubmit  // post-submit callback
		    		    }; 				
		    		    				
		    			$('#assessmentQuestionForm').ajaxSubmit(options);
		    		},
		    		invalidHandler: formValidationInvalidHandler,
					errorElement: "em",
					errorPlacement: formValidationErrorPlacement,
					success: formValidationSuccess,
					highlight: formValidationHighlight,
					unhighlight: formValidationUnhighlight
		  		});
			});

			function autoGrowTextarea(element) {
			    element.style.height = "5px";
			    element.style.height = (element.scrollHeight)+"px";
			}
  		</script>
</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.short.answer" />
		</div>

		<div class="panel-body">
			
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm"
				method="post" autocomplete="off">
				<form:hidden path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<form:hidden path="questionId" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<form:hidden path="oldCollectionUid" id="old-collection-uid"/>
				<form:hidden path="newCollectionUid" id="new-collection-uid"/>
				<form:hidden path="contentFolderID" id="contentFolderID"/>

				<button type="button" id="question-settings-link" class="btn btn-default btn-sm">
					<fmt:message key="label.settings" />
				</button>
				
				<div class="question-tab">
					<lams:errors/>
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
					
					<div id="title-container" class="form-group">
						<c:set var="TITLE_LABEL"><fmt:message key="label.enter.question.title"/> </c:set>
					    <form:input path="title" id="title" cssClass="form-control borderless-text-input" tabindex="1" maxlength="255"
					    	placeholder="${TITLE_LABEL}"/>
					</div>
				
					<div class="form-group">
						<c:set var="QUESTION_DESCRIPTION_LABEL"><fmt:message key="label.enter.question.description"/></c:set>
						<lams:CKEditor id="description" value="${assessmentQuestionForm.description}" contentFolderID="${assessmentQuestionForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
					
					<div>
						<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
					</div>
					<div>
						<input type="text" name="hasOnePositiveMaxMark" id="hasOnePositiveMaxMark" class="fake-validation-input">
					</div>
				</div>
				
				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
				
					<div>
						<label class="switch">
							<form:checkbox path="answerRequired" id="answer-required"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="answer-required">
							<fmt:message key="label.authoring.answer.required" />
						</label>
					</div>
					
					<c:if test="${!assessmentQuestionForm.authoringRestricted}">
						<div class="form-group row form-inline">
						    <label for="maxMark" class="col-sm-3">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />
						    	<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    
						    <div class="col-sm-9">
						    	<form:input path="maxMark" cssClass="form-control short-input-text input-sm"/>
						    </div>
						</div>
					</c:if>
					
					<div class="form-group row form-inline">
					    <label for="penaltyFactor" class="col-sm-3"> 
					    	<fmt:message key="label.authoring.basic.penalty.factor" />
							  <i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
					    </label>
					    
					    <div class="col-sm-9">
					    	<form:input path="penaltyFactor" cssClass="form-control short-input-text input-sm"/>
					    </div>
					</div>
					
					<div class="form-group row form-inline">
						<label for="caseSensitive" class="col-sm-3">
							<fmt:message key="label.authoring.short.answer.case.sensitivity" />
						</label>
						
						<div class="col-sm-9">
							<form:select path="caseSensitive" id="caseSensitive" cssClass="form-control input-sm">
								<form:option value="false"><fmt:message key="label.authoring.short.answer.no.case.unimportant" /></form:option>
								<form:option value="true"><fmt:message key="label.authoring.short.answer.yes.case.must.match" /></form:option>
							</form:select>
						</div>
					</div>
					
					
					<div>
						<label class="switch">
							<form:checkbox path="autocompleteEnabled" id="autocomplete-enabled"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="autocomplete-enabled">
							<fmt:message key="label.autocomplete.as.student" />
						</label>
					</div>
	
					<div class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="feedback" value="${assessmentQuestionForm.feedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
					
					<lams:OutcomeAuthor qbQuestionId="${assessmentQuestionForm.questionId}"  />
				</div>

			</form:form>
			
			<!-- Options -->
			<div class="question-tab">
				<form id="optionForm" name="optionForm">
					<%@ include file="optionlist.jsp"%>
					
					<a href="#nogo" onclick="javascript:addOption();" class="btn btn-xs btn-default button-add-item pull-right">
						<fmt:message key="label.authoring.short.answer.add.answer" />  
					</a>
				</form>	
			</div>	

		</div>	
	</div>	
	
	<%@ include file="addQuestionFooter.jsp"%>
</body>
</lams:html>
