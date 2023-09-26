<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="2"	scope="request" />

<lams:html>
<lams:head>
	<%@ include file="addQuestionHeader.jsp"%>
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
		    				prepareOptionEditorsForAjaxSubmit();	    				
		    		       	return $("textarea[name^=matchingPair]:filled").length < 1;
			    	    }
	    			   }
		    	},
				messages: {
					title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.choice.field.required'/></spring:escapeBody>",
					maxMark: {
						required: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.choice.field.required'/></spring:escapeBody>",
						digits: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.choice.enter.integer'/></spring:escapeBody>"
					},
					penaltyFactor: {
						required: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.choice.field.required'/></spring:escapeBody>",
						number: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.choice.enter.float'/></spring:escapeBody>"
					},
					hasOptionFilled: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.authoring.matching.pairs.error.one.matching.pair'/></spring:escapeBody>"
				},
   			    submitHandler: function(form) {
	    			$("#optionList").val($("#optionForm").serialize(true));
	    			$("#description").val(CKEDITOR.instances.description.getData());
	    			$("#feedback").val(CKEDITOR.instances.feedback.getData());
	    			$("#new-collection-uid").val($("#collection-uid-select option:selected").val());
		    			
	    	    	var isVersionBeingChecked = isVersionCheck(),
	    	    		options = { 
		    	    		target:  isVersionBeingChecked ? null : parent.jQuery('#itemArea'), 
		    	    		dataType : isVersionBeingChecked? 'json' : null, 
		    		   		success: isVersionBeingChecked? afterVersionCheck : afterRatingSubmit  // post-submit callback
		    		    };
		    		    				
	    			$('#assessmentQuestionForm').ajaxSubmit(options);
	    		},
	    		invalidHandler: formValidationInvalidHandler,
				errorElement: "em",
				errorPlacement: formValidationErrorPlacement,
				highlight: formValidationHighlight,
				unhighlight: formValidationUnhighlight
	  		});
		}); 
	</script>
</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.matching.pairs" />
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
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="oldCollectionUid" id="old-collection-uid"/>
				<form:hidden path="newCollectionUid" id="new-collection-uid"/>
				<form:hidden path="contentFolderID" id="contentFolderID"/>				
				<form:hidden path="feedbackOnCorrect" />
				<form:hidden path="feedbackOnPartiallyCorrect" />
				<form:hidden path="feedbackOnIncorrect" />

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
						<label for="hasOptionFilled" class="error" style="display: none;"></label>
					</div>
				</div>
				
				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
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
	
					<div>
						<label class="switch">
							<form:checkbox path="shuffle" id="shuffle"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="shuffle">		
							<fmt:message key="label.authoring.basic.shuffle.the.choices" />
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
						<fmt:message key="label.authoring.matching.pairs.add.matching.pair" /> 
					</a>
				</form>
			</div>

		</div>		
	</div>	
	
	<%@ include file="addQuestionFooter.jsp"%>
</body>
</lams:html>
