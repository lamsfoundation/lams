<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="template">
	<lams:WebAppURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />


<lams:html>
<lams:head>
 	<%@ include file="../header.jsp" %>
 
 	<title><fmt:message key="authoring.tbl.template.title"/></title>

	<script type="text/javascript">

		<%@ include file="../comms.jsp" %>

		var minimumWordsSpinnerArray  = new Array(); // Peer Review Tab.
		var showTime = 100; // how long should it take a field to show/hide.
		
		$(document).ready(function() {
			groupingChanged();

			// validate signup form on keyup and submit
			var validator = $("#templateForm").validate({
			rules: {
				sequenceTitle: {
					required: true,
					validateNoSpecialCharacters: true
				},
         		question1correct: "required",
 				question1: {
 					required: validateCK
       			},
       			assessment1: {
 					required: validateCK
       			},
				<%@ include file="../groupingvalidation.jsp" %>
			},
			messages: {
				sequenceTitle: {
					required: '<fmt:message key="authoring.fla.title.validation.error" />',
					validateNoSpecialCharacters: '<fmt:message key="authoring.fla.title.validation.error" />'
				},
				question1: '<fmt:message key="authoring.error.question.num"><fmt:param value="1"/></fmt:message>',
				question1correct: '<fmt:message key="authoring.error.question.correct.num"><fmt:param value="1"/></fmt:message>',
				assessment1: '<fmt:message key="authoring.error.application.exercise.num"><fmt:param value="1"/></fmt:message>',
				<%@ include file="../groupingerrors.jsp" %>
			},
			invalidHandler: templateInvalidHandler,
			errorClass: "text-danger",
			errorLabelContainer: "ul.error-message",
			wrapper: "li",
  			submitHandler: function(form) { 
 				submitForm(form);
 				},
			});

			jQuery.validator.addMethod("validateNoSpecialCharacters", validateNoSpecialCharacters, '<fmt:message key="authoring.fla.title.validation.error" />');

			// Remove the display:none or the fields won't be validate as jquery validation is set to only valid non hidden fields. 
			// If we allow validation of hidden fields then we cannot have validation on the Still should not be seen as visibility is hidden
			// catch the editor update and redo validation otherwise error message won't go away when the user enters text.
			initializeWizard(validator);
			reconfigureCKEditorInstance(CKEDITOR.instances.question1);

		});


		<%-- matching importQTI(limit) function is in comms.jsp --%>
	    function saveQTI(formHTML, formName, callerID) {
	    	var form = $($.parseHTML(formHTML));

	    	if ( callerID == 'assessment' ) {
		    	var nextNum  = +$('#numAssessments').val()+1;
				var url=getSubmissionURL()+"/importQTI.do?contentFolderID=${contentFolderID}&templatePage=assessmentQTI&questionNumber="+nextNum;
				$.ajaxSetup({ cache: true });
				$.ajax({
					type: "POST",
					url: url,
					data: form.serializeArray(),
					success: function(response, status, xhr) {
						if ( status == "error" ) {
							console.log( xhr.status + " " + xhr.statusText );
						} else {
							$('#divassessments').append(response);
							$('#divassess'+nextNum)[0].scrollIntoView();
						}
					}
				});

	    	} else {
		    	var nextNum  = +$('#numQuestions').val()+1;
				var url=getSubmissionURL()+"/importQTI.do?contentFolderID=${contentFolderID}&templatePage=mcquestionQTI&questionNumber="+nextNum;
				$.ajaxSetup({ cache: true });
				$.ajax({
					type: "POST",
					url: url,
					data: form.serializeArray(),
					success: function(response, status, xhr) {
						if ( status == "error" ) {
							console.log( xhr.status + " " + xhr.statusText );
						} else {
							$('#divquestions').append(response);
							$('#divq'+nextNum)[0].scrollIntoView();
						}
					}
				});
	    	}
	    }

	</script>
	
</lams:head>
	
<body class="stripes">

<c:set var="title"><fmt:message key="authoring.tbl.template.title"/></c:set>
<lams:Page title="${title}" type="wizard">

	<div id="rootwizard">
	<div class="navbar">
	  <div class="navbar-inner">
	    <div class="container">
		<ul>
		  	<li><a href="#tab1" data-toggle="tab"><fmt:message key="authoring.section.introduction" /> </a></li>
			<li><a href="#tab2" data-toggle="tab"><fmt:message key="authoring.section.lessondetails" /> </a></li>
			<li><a href="#tab3" data-toggle="tab"><fmt:message key="authoring.section.questions" /></a></li>
			<li><a href="#tab4" data-toggle="tab"><fmt:message key="authoring.section.applicationexercise" /></a></li>
			<li><a href="#tab5" data-toggle="tab"><fmt:message key="authoring.section.peerreview" /></a></li>
		</ul>
		</div>
	  </div>
	</div>
    <div id="bar" class="progress">
      <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>
    </div>

	<form id="templateForm" method="POST"  >
	<input type="hidden" name="template" id="template" value="TBL"/>
	<%@ include file="../init.jsp" %>

	<div class="tab-content">
	    <div class="tab-pane" id="tab1">
		    	<jsp:include page="../genericintro.jsp" ><jsp:param name="templateName" value="tbl"/></jsp:include>
		    	<div style="display:none">
<!-- 			<input type="checkbox" name="introduction" value="true" class="form-control-inline" id="introduction" @template_tbl_show_introduction@ />
 -->			<input type="checkbox" name="introduction" value="true" class="form-control-inline" id="introduction"/>
			<input type="checkbox" name="iratra" value="true" class="form-control-inline" id="iratra" checked />
			<input type="checkbox" name="appex" value="true" class="form-control-inline" id="appex" checked />
			<input type="checkbox" name="preview" value="true" class="form-control-inline" id="preview"  @template_tbl_show_preview@  />
<!-- 			<input type="checkbox" name="reflect" value="true" class="form-control-inline" id="preview" @template_tbl_show_notebook@/>
 -->
			<input type="checkbox" name="reflect" value="true" class="form-control-inline" id="preview" />
			</div>
	    </div>
	    <div class="tab-pane" id="tab2">
	 		<div class="form-group">			
				<span class="field-name">
					<label for="sequenceTitle" class="input required"><fmt:message key="authoring.label.sequence.title" /></label>
				</span>
				<input name="sequenceTitle" id="sequenceTitle" class="form-control" type="text" maxlength="200"/>
			</div>
			<%@ include file="../grouping.jsp" %>
	    </div>
		<div class="tab-pane" id="tab3">
			<span class="field-name"><fmt:message key="authoring.tbl.desc.question" /></span>

			<div class="form-group voffset10">
				<label for="confidenceLevelEnable">
					<input type="checkbox" name="confidenceLevelEnable" value="true" class="form-control-inline" id="confidenceLevelEnable"/>&nbsp;
					<fmt:message key="authoring.enable.confidence.levels"/>
				</label>
			</div>
			
		 	<input type="hidden" name="numQuestions" id="numQuestions" value="0"/>
			
			<div id="divquestions">
			</div>
		
			<span class="voffset10">
			<a href="#" id="createQuestionButton" onclick="javascript:createQuestion('numQuestions', 'divq', 'divquestions', '', '');" class="btn btn-default"><fmt:message key="authoring.create.question"/></a>
			<a href="#" onClick="javascript:importQTI('mcq', 'mc')" class="btn btn-default pull-right">
					<fmt:message key="authoring.template.basic.import.qti" /> 
			</a>
			</span>
			
	    </div>
		<div class="tab-pane" id="tab4">
			<span class="field-name"><fmt:message key="authoring.tbl.desc.ae" /></span>
			
		 	<input type="hidden" name="numAssessments" id="numAssessments" value="0"/>
	
			<div id="divassessments">
			</div>
			
			<span class="voffset10">
			<a href="#" onclick="javascript:createAssessment('essay');" class="btn btn-default"><fmt:message key="authoring.create.essay.question"/></a>
			<a href="#" onclick="javascript:createAssessment('mcq');" class="btn btn-default"><fmt:message key="authoring.create.mc.question"/></a>
			<a href="#" onClick="javascript:importQTI('assessment')" class="btn btn-default pull-right"><fmt:message key="authoring.template.basic.import.qti" /></a>
			</span>
			
	    </div>
	    	<div class="tab-pane" id="tab5">
			<span class="field-name"><fmt:message key="authoring.tbl.desc.peer.review" /></span>
			
		 	<input type="hidden" name="numRatingCriterias" id="numRatingCriterias" value="1"/>
	
			<div id="divratings">
			<div id="divrating1" class="space-top">
			<c:set scope="request" var="criteriaNumber">1</c:set>
			<%@ include file="../tool/peerreviewstar.jsp" %>
			</div>
			</div>
			
			<a href="#" onclick="javascript:createPeerReviewCriteria();" class="btn btn-default voffset10"><fmt:message key="authoring.create.criteria"/></a>
	    </div>
	    
	    <div id="navigation-buttons" class="voffset10">
	    		<div style="float:right">
		  	<a href="#" class='btn btn-sm btn-primary button-next'><fmt:message key="button.next"/></a>
	    		<a href="#" class='btn btn-sm btn-primary button-save' onclick="javascript:doSaveForm();" style="display:none"><fmt:message key="button.save"/></a>
		</div>
		<div style="float:left">
			<a href="#" class='btn btn-sm btn-default' onclick="javascript:doGotoList();"><fmt:message key="button.return.to.template.list"/></a>
			<a href="#" class='btn btn-sm btn-default button-previous'><fmt:message key="button.previous"/></a>
		</div>
		</div>
	</div>

	</form>

 	</div>

	<div id="footer"></div>
		
</lams:Page>

</body>
</lams:html>
		
