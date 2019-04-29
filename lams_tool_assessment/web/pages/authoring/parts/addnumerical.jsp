<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="4"	scope="request" />
		
<lams:html>
	<lams:head>
		<%@ include file="/common/authoringQuestionHeader.jsp"%>
  	    <script>
			$(document).ready(function(){
		    	$("#assessmentQuestionForm").validate({
		    		ignore: 'hidden',
		    		rules: {
		    			title: "required",
		    			defaultGrade: {
		    			      required: true,
		    			      digits: true
		    			},
		    			penaltyFactor: {
		    			      required: true,
		    			      number: true
		    			},
		    			hasOptionFilled: {
		    				required: function(element) {
		    					$("input[name^='optionFloat']").each(function() {
		    						$(this).attr("value", this.value);
		    					});
		    		        	return $("input[name^=optionFloat][value!='0.0']").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOneHundredGrade: {
		    				required: function(element) {
		    					var hasOneHundredGrade = false;
		    					$("input[name^='optionGrade']").each(function() {
		    						hasOneHundredGrade = hasOneHundredGrade || (eval(this.value) == 1);
		    					});
	    			    		return !hasOneHundredGrade;
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
		    			defaultGrade: {
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
		    			hasOneHundredGrade: {
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
		    			$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
		    			
		    	    	var options = { 
		    	    		target:  parent.jQuery('#questionListArea'), 
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
			
			<form:form action="saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm"
				method="post" autocomplete="off">
				<c:set var="sessionMap" value="${sessionScope[assessmentQuestionForm.sessionMapID]}" />
				<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<input type="hidden" name="unitList" id="unitList" />
				<form:hidden path="sequenceId" />
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
						<input type="text" name="hasOneHundredGrade" id="hasOneHundredGrade" class="fake-validation-input">
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
					
					<c:if test="${!isAuthoringRestricted}">
						<div class="form-group row form-inline">
						    <label for="defaultGrade" class="col-sm-3">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />
						    	<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    
						    <div class="col-sm-9">
						    	<form:input path="defaultGrade" cssClass="form-control short-input-text input-sm"/>
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
						<lams:CKEditor id="generalFeedback" value="${assessmentQuestionForm.generalFeedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
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
	
	<footer class="footer fixed-bottom">
		<div class="panel-heading">
        	<div class="pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
				</a>
			</div>	
      	</div>
    </footer>
</body>
</lams:html>
