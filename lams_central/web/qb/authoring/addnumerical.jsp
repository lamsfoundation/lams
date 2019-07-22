<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="4"	scope="request" />
		
<lams:html>
	<lams:head>
		<%@ include file="addQuestionHeader.jsp"%>
  	    <script>
			$(document).ready(function(){
		    	$("#assessmentQuestionForm").validate({
		    		ignore: 'hidden',
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
		    					$("input[name^='numericalOption']").each(function() {
		    						$(this).attr("value", this.value);
		    					});
		    		        	return $("input[name^=numericalOption][value!='0.0']").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOneHundredMaxMark: {
		    				required: function(element) {
		    					var hasOneHundredMaxMark = false;
		    					$("input[name^='optionMaxMark']").each(function() {
		    						hasOneHundredMaxMark = hasOneHundredMaxMark || (eval(this.value) == 1);
		    					});
	    			    		return !hasOneHundredMaxMark;
			    		    }			    		    
	    			    },
	    			    allAcceptedErrorsPositive: {
		    				required: function(element) {
		    					var count = 0;
		    					$("input[name^=optionAcceptedError]").each(function(){
			    					if (eval(this.value) < 0) {
				    					count++;
			    					}
		    					});
	    		        		return  count > 0;
			    		    }			    		    
	    			    },	    			    
		    			unitList: {
		    				required: function(element) {
	    		        		return ! $("#unitForm").valid();
		    		    	}
    			    	},
		    			optionList: {
		    				required: function(element) {
	    		        		return ! $("#optionForm").valid();
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
		    			hasOptionFilled: {
		    				required: "<fmt:message key='label.authoring.numerical.error.answer'/>"
		    			},
		    			hasOneHundredMaxMark: {
		    				required: "<fmt:message key='error.form.validation.hundred.score'/>"
		    			},		
		    			allAcceptedErrorsPositive: {
		    				required: "<fmt:message key='error.form.validation.positive.accepted.errors'/>"
		    			}, 
		    			unitList: {
		    				required: ""
		    			},
		    			optionList: {
		    				required: ""
		    			}
		    		},
     			    submitHandler: function(form) {
     			    	prepareOptionEditorsForAjaxSubmit();
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#unitList").val($("#unitForm").serialize(true));
		    			$("#question").val(CKEDITOR.instances.question.getData());
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

    		function addUnit(){
    			var url= "<c:url value='/authoring/newUnit.do'/>";
    			var unitList = $("#unitForm").serialize(true);
    			$("#unitArea").load(
    				url,
    				{
    					questionType: QUESTION_TYPE,
    					unitList: unitList 
    				}
    			);
    		}
  		</script>
	</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.numerical.question" />
		</div>
			
		<div class="panel-body">
			
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm"
				method="post" autocomplete="off">
				<form:hidden path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<input type="hidden" name="unitList" id="unitList" />
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
						<lams:CKEditor id="question" value="${assessmentQuestionForm.question}" contentFolderID="${assessmentQuestionForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
					
					<div>
						<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
					</div>
					<div>
						<input type="text" name="hasOneHundredMaxMark" id="hasOneHundredMaxMark" class="fake-validation-input">
					</div>
					<div>
						<input type="text" name="allAcceptedErrorsPositive" id="allAcceptedErrorsPositive" class="fake-validation-input">
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
					
					<div class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="feedback" value="${assessmentQuestionForm.feedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
					
					<c:if test="${not empty assessmentQuestionForm.questionId}">
						<lams:OutcomeAuthor qbQuestionId="${assessmentQuestionForm.questionId}" inPanel="false" />
					</c:if>
				</div>			
				
			</form:form>
			
			<!-- Options -->
			<div class="question-tab">
				<form id="optionForm" name="optionForm">
					<%@ include file="optionlist.jsp"%>
					
					<a href="#nogo" onclick="javascript:addOption();" class="btn btn-xs btn-default button-add-item pull-right">
						<fmt:message key="label.authoring.numerical.add.answer" />  
					</a>
				</form>
			</div>
			
			<!-- Units -->
			<div class="settings-tab">
				<h5 class="voffset20">
					<fmt:message key="label.authoring.numerical.units" />
				</h5>			
				<form id="unitForm" name="unitForm">
					<%@ include file="unitlist.jsp"%>
					
					<a href="#nogo" onclick="javascript:addUnit();" class="btn btn-xs btn-default button-add-item pull-right">
						<fmt:message key="label.authoring.numerical.add.unit" />  
					</a>
				</form>
			</div>	

		</div>		
	</div>	

	<%@ include file="addQuestionFooter.jsp"%>
</body>
</lams:html>
