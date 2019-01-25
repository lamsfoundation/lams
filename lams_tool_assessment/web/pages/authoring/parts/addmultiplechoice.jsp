<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="1"	scope="request" />

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<link href="<lams:WebAppURL/>includes/css/addQuestion.css" rel="stylesheet" type="text/css">
	<style>
	:focus {
	    outline: 0;
	    /* or */
	    outline: none;
	}
	label {
    	font-weight: initial;
	}
	.justify-content-end {
    -webkit-box-pack: end!important;
    -ms-flex-pack: end!important;
    justify-content: flex-end!important;
}
.nav-pills>li {
    float: right;
}
	
	.form-control-new {
	    display: block;
	    width: 100%;
	    height: 34px;
	    font-size: 14px;
	    line-height: 1.42857;
	    color: rgb(85, 85, 85);
	    background-color: rgb(255, 255, 255);
	    background-image: none;
	    /*box-shadow: rgba(0, 0, 0, 0.075) 0px 1px 1px inset;*/
	    /*padding: 6px 12px;*/
	    border-width: 0px;
	    border-style: solid;
	    border-color: rgb(204, 204, 204);
	    border-image: initial;
	    border-radius: 4px;
	    transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s ease-in-out 0s;
	    font-weight: bold;
	}
	.form-control-new::-webkit-input-placeholder {
	    font-weight: normal;
	}
	.form-control-new::-moz-placeholder {
	    font-weight: normal;
	}
	.form-control-new:-ms-input-placeholder {
	    font-weight: normal;
	}
	.form-control-new:-o-input-placeholder {
	    font-weight: normal;
	} 
	
	
	.ckeditor-without-borders {
	    display: block;
	    width: 100%;
	    padding: 0;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555;
	    background-color: #fff;
	    background-image: none;
	    border: 0;
	    border-radius: 0;
	    -webkit-box-shadow: inset 0 0 0 rgba(0,0,0,0.075);
	    box-shadow: 0;
	    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
	    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
	    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
	    border: 0;
	    overflow: hidden;
	    min-height: 60px;
	    -webkit-appearance: textfield;
	}
	
	#question-settings-link{
		margin-top: -10px;
    	margin-bottom: 5px;
    	position: absolute;
    	top: 60px;
	    right: 20px;
    	z-index: 2;
    }
	.delete-button, .arrows-div, .option-settings, #question-settings-link{
		visibility:hidden;
	}
	#option-table.hover-active tr:hover .delete-button, #option-table.hover-active tr:hover .arrows-div, #option-table.hover-active tr:hover .option-settings, #assessmentQuestionForm:hover #question-settings-link {
		visibility:visible;
	}
	.basic-tab[style*="display: none"] + #question-settings-link {
		visibility:visible;
	}
	#title {
	    padding-right: 80px;
	}
	.advanced-tab {
		display:none;
	}
	.delete-button {
		opacity: 0.6;
	}
	
/*----------UL numbers----------------*/
#option-table table {
	width: 100%;
}
#option-table td:first-child {
	width: 60px;
	vertical-align: top;
}
#option-table td:first-child span{
	display: inline-block;
	width: 4rem;
	height: 4rem;
	text-align: center;
	line-height: 2em;
	font-size: 1.8rem;
	color: #fff;
	border: .2rem solid #313537;
	-webkit-border-radius: 50%;
	border-radius: 50%;
	border-color: #337ab7 !important;
	background-color: #337ab7 !important;
}

/*----------SLIDER----------------*/

.ui-widget-content {
	border: 1px solid #bdc3c7;
	background: #e1e1e1;
	color: #222222;
	margin-top: 0.1em;
	padding: 1px;
}

.ui-state-default, .ui-widget-content .ui-state-default{
	background:transparent !important;
	border:none !important;
}
.ui-slider .ui-slider-handle label{
    background: #c3c3c3;
    border-radius: 20px;
    width:5.2em;
}

.ui-slider .ui-slider-handle {
	position: absolute;
	z-index: 2;
	width: 5.2em;
	height: 100px;
	cursor: default;
	margin: 0 -40px auto !important;
	text-align: center;	
	line-height: 32px;
	color: #FFFFFF;
}

.ui-slider .ui-slider-handle .fa {
	color: #FFFFFF;
    margin: 0 3px;
    font-size: 18px;
    opacity: 0.5;
    font-weight: bolder;
}

.ui-slider-horizontal .ui-slider-handle {
	top: -.9em;
}

.ui-state-default, .ui-widget-content .ui-state-default {
	border: 1px solid #f9f9f9;
	background: #3498db;
}

.ui-slider-horizontal .ui-slider-handle {
	margin-left: -0.5em;
}

.ui-slider .ui-slider-handle {
	cursor: pointer;
}

.ui-slider a, .ui-slider a:focus {
	cursor: pointer;
	outline: none;
}

.price-slider {
    height: 30px;
    padding-top: 15px;
}
.sortable-placeholder {
	height: 130px; 
	background-color: #f1f1f1;
	margin: 0px 0 20px -0;
}
.ui-sortable-helper {
	opacity: 0.8;
}
	</style>

	<script type="text/javascript">
		var questionType = ${questionType};
		var addOptionUrl = "<c:url value='/authoring/addOption.do'/>";
	   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
   	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
   	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";
	</script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/assessmentoption.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.touch-punch.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@shopify/draggable@1.0.0-beta.8/lib/draggable.bundle.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@shopify/draggable@1.0.0-beta.8/lib/sortable.js"></script>
	
    <script>

    import { Sortable } from '@shopify/draggable';

	const sortable = new Sortable(document.querySelectorAll('#option-table'), {
	  draggable: 'table'
	});
		$(document).ready(function(){

			sortable.on('sortable:start', () => console.log('sortable:start'));
			sortable.on('sortable:sort', () => console.log('sortable:sort'));
			sortable.on('sortable:sorted', () => console.log('sortable:sorted'));
			sortable.on('sortable:stop', () => console.log('sortable:stop'));
				
		    	$("#assessmentQuestionForm").validate({
		    		onkeyup: false,
		    		onclick: false,
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
				    			prepareOptionEditorsForAjaxSubmit();
		    		        	return $("textarea[name^=optionString]:filled").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOneHundredGrade: {
		    				required: function(element) {
		    					var hasOneHundredGrade = false;
		    					$("input[name^='optionGrade']").each(function() {
		    						hasOneHundredGrade = hasOneHundredGrade || (eval(this.value) == 1);
		    					});
	    			    		return !hasOneHundredGrade && !eval($("#multipleAnswersAllowed").val());
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
		    			}
		    		},
		    	    invalidHandler: function(form, validator) {
		    		      var errors = validator.numberOfInvalids();
		    		      if (errors) {
		    		          var message = errors == 1
		    		          	  ? "<fmt:message key='error.form.validation.error'/>"
		    		          	  : "<fmt:message key='error.form.validation.errors'><fmt:param >" + errors + "</fmt:param></fmt:message>";
	    		          	  
		    		          $("div.error span").html(message);
		    		          $("div.error").show();
		    		      } else {
		    		          $("div.error").hide();
		    		      }
		    		},
		    		debug: true,
		    		errorClass: "alert alert-danger",
     			    submitHandler: function(form) {
     			    	prepareOptionEditorsForAjaxSubmit();
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#question").val(CKEDITOR.instances.question.getData());
		    			$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
		    			$("#feedbackOnCorrect").val(CKEDITOR.instances.feedbackOnCorrect.getData());
		    			$("#feedbackOnPartiallyCorrect").val(CKEDITOR.instances.feedbackOnPartiallyCorrect.getData());
		    			$("#feedbackOnIncorrect").val(CKEDITOR.instances.feedbackOnIncorrect.getData());
		    			
		    	    	var options = { 
		    	    		target:  parent.jQuery('#questionListArea'), 
		    		   		success: afterRatingSubmit  // post-submit callback
		    		    }; 				
		    		    				
		    			$('#assessmentQuestionForm').ajaxSubmit(options);
		    		}
		  		});
		    	
				$("#multipleAnswersAllowed").on('change', function() {
					$("#incorrect-answer-nullifies-mark-area").toggle(eval($(this).val()));
				}).trigger("change");

				// Only one of prefixAnswersWithLetters or shuffle at a time
 				$("#prefixAnswersWithLetters").on('change', function() {
					if ( this.checked ) {
						if ($("#shuffle").prop('checked')) {
							$("#shuffle").prop('checked', false);
						}
						$("#shuffle").prop('disabled', true);
						$("#shuffleText").addClass('text-muted');
					} else {
						$("#shuffle").prop('disabled', false);
						$("#shuffleText").removeClass('text-muted');
					}
				}).trigger("change");

				$("#shuffle").on('change', function() {
					if ( this.checked ) {
						if ($("#prefixAnswersWithLetters").prop('checked')) {
							$("#prefixAnswersWithLetters").prop('checked', false);
						}
						$("#prefixAnswersWithLetters").prop('disabled', true);
						$("#prefixAnswersWithLettersText").addClass('text-muted');
					} else {
						$("#prefixAnswersWithLetters").prop('disabled', false);
						$("#prefixAnswersWithLettersText").removeClass('text-muted');
					}
				}).trigger("change"); 
				
				$("#question-settings-link").on('click', function() {
					$(".basic-tab, .advanced-tab").fadeToggle("fast", function() {});
					
					$(this).toggleClass("btn-default btn-primary");
				});
				
			initializeOptionItems();
			
		});
   		// post-submit callback 
   		function afterRatingSubmit(responseText, statusText)  { 
   			self.parent.refreshThickbox()
   			self.parent.tb_remove();
   		}

   		function initializeOptionItems() {
			//Grading slider
	        $(".slider").slider({
	            animate: true,
	            min: -1,
	            max: 1,
	            step: 0.05,
	            create: function(event, ui){
		            //get initial value from the responsible form input
	            	var initialValue = $(this).next("input").val();
	            	$(this).slider('value', initialValue);
	                //$(this).slider('value',$(this).parent().find(".inputNumber").val());
	            }
	        }).on('slide',function(event,ui){
		        //ui is not available at the initial call 
		        var newValueInt = ui ? eval(ui.value) : eval($(this).slider('value'));
		        
		        //prepare string value
		        var newValue;
		        switch (newValueInt) {
				  case 0:
					newValue = " <fmt:message key="label.authoring.basic.none" /> ";
				    break;
				  case 1:
				  case -1:
					newValue = parseInt(newValueInt*100) + " %";
					break;
				  default:
					newValue = " " + parseInt(newValueInt*100) + " % ";
				}
		        
				//update slider's label
	        	$('span', $(this)).html('<label><span class="fa fa-angle-left"></span><span>'+newValue+'</span><span class="fa fa-angle-right"></span></label>');

	        	//update input with the new value, it it's not initial call
	        	if (ui) {
		        	$(this).next("input").val(ui.value);
	        	}
	        });
	        //update slider's label with the initial value
	        //$('.slider').trigger('slide');
	        //$('#slider').slider('value', $('#slider').slider('value'));


/**
			$( "#option-table" ).sortable({
				placeholder: "sortable-placeholder",
				cancel: ':input,button,[contenteditable]',
				start: function( event, ui ) {
					//stop answers' hover effect
					$("#option-table").removeClass("hover-active");
				},
				stop: function( event, ui ) {
					//activate answers' hover effect
					$("#option-table").addClass("hover-active");
				}
			});**/
		}
	</script>
</lams:head>
<body>
	<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.multiple.choice" />
		</div>
			
		<div class="panel-body">
			
			<lams:errors/>
		    <div class="error">
		    	<lams:Alert id="errorMessages" type="danger" close="false" >
					<span></span>
				</lams:Alert>	
		    </div>
			
			<form:form action="saveOrUpdateQuestion.do" method="post" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm">
				<c:set var="sessionMap" value="${sessionScope[assessmentQuestionForm.sessionMapID]}" />
				<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<form:hidden path="sequenceId" />
				<form:hidden path="contentFolderID" id="contentFolderID"/>

				<div class="basic-tab">
					<div class="form-group">
					    <form:input path="title" id="title" cssClass="form-control-new" maxlength="255" tabindex="1" placeholder="Enter a question's title"/>
					</div>
				
					<div class="form-group">
						<lams:CKEditor id="question" classes="ckeditor-without-borders" 
						placeholder="Question's description"
						value="${assessmentQuestionForm.question}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
				</div>
				
				<button type="button" id="question-settings-link" class="btn btn-default btn-sm">
					Settings
				</button>

				<div class="advanced-tab">
				<div class="checkbox">
					<label for="answer-required">
						<form:checkbox path="answerRequired" id="answer-required"/>
						<fmt:message key="label.authoring.answer.required" />
					</label>
				</div>
				
				<div class="form-group form-inline">
					<c:if test="${!isAuthoringRestricted}">
					    <label for="defaultGrade">
					    	<fmt:message key="label.authoring.basic.default.question.grade" />:
					    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
					    </label>
					    <form:input path="defaultGrade" cssClass="form-control short-input-text input-sm"/>
				    </c:if>
				</div>
				
				<div class="form-group form-inline">
				    <label for="penaltyFactor"> 
				    	<fmt:message key="label.authoring.basic.penalty.factor" />:
						  <i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
				    </label>
				    <form:input path="penaltyFactor" cssClass="form-control short-input-text input-sm"/>
				</div>
				
				<div class="form-inline form-group">
					<label for="multipleAnswersAllowed">
						<fmt:message key="label.authoring.choice.one.multiple.answers" />
					</label>
					<form:select path="multipleAnswersAllowed" id="multipleAnswersAllowed" cssClass="form-control input-sm">
						<form:option value="false"><fmt:message key="label.authoring.choice.one.answer" /></form:option>
						<form:option value="true"><fmt:message key="label.authoring.choice.multiple.answers" /></form:option>
					</form:select>
				</div>
				
				<div class="checkbox" id="incorrect-answer-nullifies-mark-area">
					<label for="incorrectAnswerNullifiesMark">
						<form:checkbox path="incorrectAnswerNullifiesMark" id="incorrectAnswerNullifiesMark"/>
						<fmt:message key="label.incorrect.answer.nullifies.mark" />
					</label>
				</div>		
	
				<div class="checkbox">
					<label for="shuffle">
						<form:checkbox path="shuffle" id="shuffle"/>
						<span id="shuffleText"><fmt:message key="label.authoring.basic.shuffle.the.choices" /></span>
					</label>
				</div>
				
				<div class="checkbox" id="prefixAnswersWithLettersDiv">
					<label for="prefixAnswersWithLetters">
						<form:checkbox path="prefixAnswersWithLetters" id="prefixAnswersWithLetters"/>
						<span id="prefixAnswersWithLettersText"><fmt:message key="label.prefix.sequential.letters.for.each.answer" /></span>
					</label>
				</div>

				<div class="generalFeedback">
					<div id="general-feedback"  class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="generalFeedback" value="${assessmentQuestionForm.generalFeedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
				</div>
			
				<!-- Overall feedback -->
				<div class="overallFeedback">
				  <fmt:message key="label.authoring.choice.overall.feedback" />
	
					<div id="overall-feedback">				
						<div class="form-group">
							<c:set var="FEEDBACK_ON_CORRECT_LABEL"><fmt:message key="label.authoring.choice.feedback.on.correct"/></c:set>
							<lams:CKEditor id="feedbackOnCorrect" value="${assessmentQuestionForm.feedbackOnCorrect}" 
								placeholder="${FEEDBACK_ON_CORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
						</div>
						
						<div class="form-group">
							<c:set var="FEEDBACK_ON_PARTICALLY_CORRECT_LABEL"><fmt:message key="label.authoring.choice.feedback.on.partially.correct" /></c:set>
							<lams:CKEditor id="feedbackOnPartiallyCorrect" value="${assessmentQuestionForm.feedbackOnPartiallyCorrect}" 
								placeholder="${FEEDBACK_ON_PARTICALLY_CORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
						</div>
						
						<div class="form-group">
							<c:set var="FEEDBACK_ON_INCORRECT_LABEL"><fmt:message key="label.authoring.choice.feedback.on.incorrect" /></c:set>
							<lams:CKEditor id="feedbackOnIncorrect" value="${assessmentQuestionForm.feedbackOnIncorrect}" 
								placeholder="${FEEDBACK_ON_INCORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
						</div>
					</div>
				</div>
				</div><!-- advanced tab ends -->
				
				<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
				<input type="text" name="hasOneHundredGrade" id="hasOneHundredGrade" class="fake-validation-input">
			</form:form>
			
			<!-- Options -->
			<div class="basic-tab">	
				<form id="optionForm" name="optionForm" class="form-group">
					<%@ include file="optionlist.jsp"%>
					
					<a href="#nogo" onclick="javascript:addOption();" class="btn btn-xs btn-default button-add-item pull-right">
						<fmt:message key="label.authoring.choice.add.option" /> 
					</a>
				</form>
			</div>

			<br/>
			<div class="voffset10 pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
				</a>
			</div>			

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
	</div>		
</body>
</lams:html>
