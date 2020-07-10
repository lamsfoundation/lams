<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="3"	scope="request" />

<lams:html>
	<lams:head>
	
		<%@ include file="/common/header.jsp"%>
		<link href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css" />
		<link href="<lams:LAMSURL/>css/qb-question.css" rel="stylesheet" type="text/css">
		<link href="<lams:WebAppURL/>includes/css/scratchie.css" rel="stylesheet" type="text/css" media="screen">
		<style>
			textarea {
			  resize: none;
			}
		</style>

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript">
			const QUESTION_TYPE = 3;
			const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
			const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
		</script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-question.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-option.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.slider.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.touch-punch.js"></script>
  	    <script>
			$(document).ready(function(){
		    	$("#scratchieItemForm").validate({
		    		ignore: 'hidden, div.cke_editable',
		    		rules: {
		    			title: "required",
		    			hasTwoOptionsFilled: {
		    				required: function(element) {
		    		        	return $("textarea[name^=optionName]:filled").length != 2;
			    		    }			    		    
	    			    }
		    		},
		    		messages: {
		    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
		    			hasTwoOptionsFilled: "<fmt:message key='label.error.two.answers.required'/>"
		    		},
     			    submitHandler: function(form) {
     			    	prepareOptionEditorsForAjaxSubmit();     			    
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#description").val(CKEDITOR.instances.description.getData());
		    			$("#feedback").val(CKEDITOR.instances.feedback.getData());
		    			$("#new-collection-uid").val($("#collection-uid-select option:selected").val());
		    			
		    	    	var options = { 
		    	    		target:  parent.jQuery('#itemArea'), 
		    		   		success: function (responseText, statusText)  { 
		    	    			self.parent.refreshThickbox()
		    	    			self.parent.tb_remove();
		    	    		} 
		    		    }; 				
		    		    				
		    			$('#scratchieItemForm').ajaxSubmit(options);
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
			    element.style.height = "75px";
			    element.style.height = (element.scrollHeight)+"px";
			}
  		</script>
</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.type.short.answer" />
		</div>

		<div class="panel-body">
			
			<form:form action="/lams/tool/lascrt11/authoring/saveItem.do" method="post" modelAttribute="scratchieItemForm" id="scratchieItemForm">
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="optionList" id="optionList" />
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="itemIndex" />
				<form:hidden path="questionType"/>
				<form:hidden path="contentFolderID" id="contentFolderID"/>
				<form:hidden path="oldCollectionUid" id="old-collection-uid"/>
				<form:hidden path="newCollectionUid" id="new-collection-uid"/>

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
						<c:set var="TITLE_LABEL"><fmt:message key="label.authoring.basic.question.name"/> </c:set>
					    <form:input path="title" id="title" cssClass="form-control borderless-text-input" tabindex="1" maxlength="255"
					    	placeholder="${TITLE_LABEL}"/>
					</div>
				
					<div class="form-group">
						<c:set var="QUESTION_DESCRIPTION_LABEL"><fmt:message key="label.authoring.basic.question.text"/></c:set>
						<lams:CKEditor id="description" value="${scratchieItemForm.description}" contentFolderID="${scratchieItemForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
					
					<div>
						<input type="text" name="hasTwoOptionsFilled" id="hasTwoOptionsFilled" class="fake-validation-input">
					</div>
				</div>
				
				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
					
					<div class="form-group row form-inline">
						<label for="caseSensitive" class="col-sm-3 voffset10">
							<fmt:message key="label.case.sensitivity" />
						</label>
						
						<div class="col-sm-9">
							<form:select path="caseSensitive" id="caseSensitive" cssClass="form-control input-sm">
								<form:option value="false"><fmt:message key="label.no.case.unimportant" /></form:option>
								<form:option value="true"><fmt:message key="label.yes.case.must.match" /></form:option>
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
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.general.feedback"/></c:set>
						<lams:CKEditor id="feedback" value="${scratchieItemForm.feedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${scratchieItemForm.contentFolderID}" />
					</div>
				</div>

			</form:form>
			
			<!-- Options -->
			<div class="question-tab">
				<form id="optionForm" name="optionForm">
				<div id="optionArea">
					<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
					<input type="hidden" name="optionCount" id="optionCount" value="2">
					
					<div id="option-table">
						<c:forEach var="option" items="${optionList}" varStatus="status">
							<table id="option-table-${status.index}" class="voffset10-bottom" data-id="${status.index}" tabindex="1">
								<tr>									
									<td>
										<input type="hidden" name="optionDisplayOrder${status.index}" value="${option.displayOrder}">
										<input type="hidden" name="optionUid${status.index}" value="${option.uid}">
										
										<div class="form-group">
											
											<c:choose>
												<c:when test="${status.index == 0}">
													<c:set var="OPTION_LABEL">
														<fmt:message key="label.correct.answers" />
													</c:set>
												</c:when>
												<c:otherwise>
													<c:set var="OPTION_LABEL">
														<fmt:message key="label.incorrect.answers" />
													</c:set>
												</c:otherwise>
											</c:choose>
											<div style="color: #999 !important;">
												${OPTION_LABEL}
											</div>
											
											<textarea name="optionName${status.index}" rows="3" class="form-control borderless-text-input2"
												oninput="autoGrowTextarea(this)"
												><c:out value='${option.name}' /></textarea>
												
											<div class="voffset10 voffset5-bottom">
											   	<c:set var="FEEDBACK_LABEL"><fmt:message key="label.option.feedback"/></c:set>
											   	<lams:CKEditor id="optionFeedback${status.index}" value="${option.feedback}" 
											     	placeholder="${FEEDBACK_LABEL}" contentFolderID="${contentFolderID}" height="50px"/>
											</div>
										</div>
									</td>
								</tr>
							</table>
						</c:forEach>
					</div>
				</div>
				</form>	
			</div>	

		</div>	
	</div>	
	
	<%@ include file="addItemFooter.jsp"%>
</body>
</lams:html>
