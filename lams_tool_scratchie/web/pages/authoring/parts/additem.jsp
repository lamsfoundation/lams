<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<link href="<lams:WebAppURL/>includes/css/scratchie.css" rel="stylesheet" type="text/css" media="screen">
 		
		<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />

		<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript">
			var addAnswerUrl = "<c:url value='/authoring/addAnswer.do'/>";
		   	var removeAnswerUrl = "<c:url value='/authoring/removeAnswer.do'/>";
    	    var upAnswerUrl = "<c:url value='/authoring/upAnswer.do'/>";
    	    var downAnswerUrl = "<c:url value='/authoring/downAnswer.do'/>";
    	    
    		/*
    		 This is Scratchie Item answer area.
    		 */
    		var answerTargetDiv = "#answerArea";
    		function addAnswer(){

    			//check maximum number of answers
    			var numberOfAnswers = $("textarea[name^=answerDescription]").length;
    			if (numberOfAnswers >= 10) {
    				alert("<fmt:message key="label.authoring.maximum.answers.warning" />");
    				return;
    			}
    			
    			//store old InstanceIds before doing Ajax call. We need to keep record of old ones to prevent reinitializing new CKEditor two times.
    			var oldAnswerIds = new Array();
    	 		for (var instanceId in CKEDITOR.instances){
    				oldAnswerIds[instanceId] = instanceId;
    			}
    			
    			var url= addAnswerUrl;
    			var contentFolderID= $("#contentFolderID").val();
    			prepareAnswerEditorsForAjaxSubmit();
    			var answerList = $("#answerForm").serialize(true);
    			$.ajaxSetup({ cache: true });
    			$(answerTargetDiv).load(
    				url,
    				{
    					contentFolderID: contentFolderID,
    					answerList: answerList 
    				},
    				function(){
    					reinitializeAnswerEditors(oldAnswerIds);
    				}
    			);
    		}
    		function removeAnswer(idx){
    	 		var url= removeAnswerUrl;
    			var contentFolderID= $("#contentFolderID").val();
    			prepareAnswerEditorsForAjaxSubmit();
    	 		var answerList = $("#answerForm").serialize(true);
    			$(answerTargetDiv).load(
    					url,
    					{
    						contentFolderID: contentFolderID,
    						answerIndex: idx,
    						answerList: answerList 
    					},
    					function(){
    						reinitializeAnswerEditors(null);
    					}
    			);
    		}
    		function upAnswer(idx){
    	 		var url= upAnswerUrl;
    			var contentFolderID= $("#contentFolderID").val();
    			prepareAnswerEditorsForAjaxSubmit();
    	 		var answerList = $("#answerForm").serialize(true);
    			$(answerTargetDiv).load(
    					url,
    					{
    						contentFolderID: contentFolderID,
    						answerIndex: idx,
    						answerList: answerList 
    					},
    					function(){
    						reinitializeAnswerEditors(null);
    					}
    			);
    		}
    		function downAnswer(idx){
    	 		var url= downAnswerUrl;
    			var contentFolderID= $("#contentFolderID").val(); 	
    			prepareAnswerEditorsForAjaxSubmit();
    	 		var answerList = $("#answerForm").serialize(true);
    	 		$(answerTargetDiv).load(
    					url,
    					{
    						contentFolderID: contentFolderID,
    						answerIndex: idx,
    						answerList: answerList 
    					},
    					function(){
    						reinitializeAnswerEditors(null);
    					}
    			);
    		}
    		
    		//reinitialize all CKEditors responsible for answers after Ajax call has done
    		function reinitializeAnswerEditors(answerIds){
    			return;
    			if (answerIds == null) {
    				answerIds = CKEDITOR.instances;
    			}
    			
    			for (var instanceId in answerIds){
    				//skip general fckeditors
    				if (instanceId.match("^(description)")) { 
    					if (instanceId == null) continue;
    					var instance = CKEDITOR.instances[instanceId];
    					if (instance == null) continue;
    					var initializeFunction = instance.initializeFunction;
    					CKEDITOR.remove(instance);
    					//don't initialize elements that were deleted
    					if ($("#" + instanceId).length == 0) continue;
    					instance = initializeFunction();
    					instance.initializeFunction = initializeFunction;
    				}
    			}
    		}
    		
    		//in order to be able to use answer's value, copy it from CKEditor to textarea
    		function prepareAnswerEditorsForAjaxSubmit(){
    			$("textarea[name^=answerDescription]").each(function() {
    	    		var ckeditorData = CKEDITOR.instances[this.name].getData();
    	    		//skip out empty values
    	    		this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
    			});
    		}
			$(document).ready(function(){
				
		    	$("#scratchieItemForm").validate({
		    		ignore : 'div.cke_editable',
		    		rules: {
		    			title: "required",
		    			hasAnswerFilled: {
		    				required: function(element) {
		    					//check there should be at least one answer filled
				    			prepareAnswerEditorsForAjaxSubmit();
		    		        	return $("textarea[name^=answerDescription]:filled").length < 1;
			    		    }
	    			    },
	    			    hasFilledCorrectAnswer: {
		    				required: function(element) {
		    					//check one answer should be selected as correct (and be filled at the same time)
		    					prepareAnswerEditorsForAjaxSubmit();
		    					
		    					var hasFilledCorrectAnswer = false;
		    					$("input[name^=answerCorrect]:checked").each(function() {
		    						var statusIndex = this.alt;
		    						var answerDescription = $("textarea[name=answerDescription" + statusIndex + "]");
		    						hasFilledCorrectAnswer = answerDescription.val() != "";
		    					});
		    					
		    		        	return !hasFilledCorrectAnswer;
			    		    }
	    			    }
		    		},
		    		messages: {
		    			title: "<fmt:message key='label.authoring.title.required'/>",
		    			hasAnswerFilled: "<fmt:message key='label.authoring.error.possible.answer'/>",
		    			hasFilledCorrectAnswer: "<fmt:message key='label.authoring.error.correct.answer'/>"
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
		    		debug: false,
     			    submitHandler: function(form) {
     			    	prepareAnswerEditorsForAjaxSubmit();
		    			$("#answerList").val($("#answerForm").serialize(true));
		    			$("#description").val(CKEDITOR.instances.description.getData());
		    			
		    	    	var answers = { 
		    	    		target:  parent.jQuery('#itemArea'), 
		    		   		success: function (responseText, statusText)  { 
		    	    			self.parent.refreshThickbox()
		    	    			self.parent.tb_remove();
		    	    		} 
		    		    };
		    		    				
		    			$('#scratchieItemForm').ajaxSubmit(answers);
		    		}
		  		});
			});   
  		</script>
		
	</lams:head>
	
	<body class="stripes">
	
	<div class="row no-gutter">
		<div class="col-xs-12">
			<div class="container" id="content">
			
			<div class="panel panel-default panel-learner-page">
				<div class="panel-heading">
					<div class="panel-title panel-learner-title">
						<fmt:message key="label.edit.question" />
					</div>
				</div>
			<div class="panel-body panel-${type}-body">
	
			<lams:errors/>
			
			<form:form action="/lams/tool/lascrt11/authoring/saveItem.do" method="post" modelAttribute="scratchieItemForm" id="scratchieItemForm">
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="answerList" id="answerList" />
				<form:hidden path="itemIndex" />
				<form:hidden path="contentFolderID" id="contentFolderID"/>
	
				<div class="form-group">
					<label for="title"><fmt:message key="label.authoring.basic.question.name" /></label>
					<form:input path="title" cssClass="form-control"/>
				</div>

				<div class="form-group">
					<label for="description"><fmt:message key="label.authoring.basic.question.text" /></label>
					<lams:CKEditor id="description" value="${scratchieItemForm.description}" contentFolderID="${authoringForm.contentFolderID}"></lams:CKEditor>
				</div>

				<label for="hasAnswerFilled" class="error" style="display: none;"></label>
				<input type="text" name="hasAnswerFilled" id="hasAnswerFilled" class="fake-validation-input" >
				<label for="hasFilledCorrectAnswer" class="error" style="display: none;"></label>
				<input type="text" name="hasFilledCorrectAnswer" id="hasFilledCorrectAnswer" class="fake-validation-input">

				<div class="form-group"><fmt:message key="label.authoring.scratchie.answers" /></div>
			
			</form:form>
			
			<!-- Answers -->
			<form id="answerForm" name="answerForm">
 				<%@ include file="answerlist.jsp"%>
 				<a href="javascript:;" onclick="addAnswer();" class="btn btn-default btn-sm" style="margin-right: 40px; margin-top:0px">
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.add.blank.answer" /> 
				</a>
			</form>
			<div class="pull-right">
				<a href="#" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="btn btn-default btn-sm">
					<fmt:message key="label.authoring.cancel.button" /> 
				</a>
				<a href="#" onclick="$('#scratchieItemForm').submit();" onmousedown="self.focus();" class="btn btn-primary btn-sm">
					<fmt:message key="label.authoring.save.button" /> 
				</a>
			</div>
			
			<div id="footer"></div>

			</div>
			</div>

			</div>
		</div>
	</div>
					
	</body>
</lams:html>
