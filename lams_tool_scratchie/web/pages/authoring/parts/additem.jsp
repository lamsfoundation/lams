<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<style media="screen,projection" type="text/css">
			label { width: 10em; float: left; }
			label.error { float: none; color: red; vertical-align: top; font-weight: bold; font-style: italic;}
			em { font-weight: bold; padding-right: 1em; vertical-align: top; }
			#content {width: 91%; margin-bottom: 10px;}
			table.alternative-color td {padding-left: 0;}
			input[type=text].shortInputText {width:10%;}
			input[type=text] {width:98%;}
			textarea {width:98%;}
		</style>
		
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
		    		debug: true,
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
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >	
			<%@ include file="/common/messages.jsp"%>
		    <div class="error" style="display:none;">
		      	<img src="${ctxPath}/includes/images/warning.gif" alt="Warning!" width="24" height="24" style="float:left; margin: -5px 10px 0px 0px; " />
		      	<span></span>.<br clear="all"/>
		    </div>			
			
			<html:form action="/authoring/saveItem" method="post" styleId="scratchieItemForm">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="answerList" id="answerList" />
				<html:hidden property="itemIndex" />
				<html:hidden property="contentFolderID" styleId="contentFolderID"/>
	
				<h2 class="no-space-left">
					<fmt:message key="label.edit.question" />
				</h2>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.name" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="title" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<lams:CKEditor id="description" value="${formBean.description}" contentFolderID="${formBean.contentFolderID}" >
				</lams:CKEditor>
				<br><br>
				
				<input type="text" name="hasAnswerFilled" id="hasAnswerFilled" class="fake-validation-input">
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.scratchie.answers" />
				</div>
				<label for="hasAnswerFilled" class="error" style="display: none;"></label>
				
				<input type="text" name="hasFilledCorrectAnswer" id="hasFilledCorrectAnswer" class="fake-validation-input">
				<label for="hasFilledCorrectAnswer" class="error" style="display: none;"></label>
				
			</html:form>
			
			<!-- Answers -->
			<form id="answerForm" name="answerForm">
				<%@ include file="answerlist.jsp"%>
				<a href="javascript:;" onclick="addAnswer();" class="button-add-item right-buttons" style="margin-right: 40px; margin-top:0px">
					<fmt:message key="label.authoring.add.blank.answer" /> 
				</a>
			</form>
			<br><br>
			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="$('#scratchieItemForm').submit();" onmousedown="self.focus();" class="button-add-item">
					<fmt:message key="label.authoring.save.button" /> 
				</a>
				<a href="#" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="button space-left">
					<fmt:message key="label.authoring.cancel.button" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
