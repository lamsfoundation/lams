<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>
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
 	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable.css"> 
	<link rel="stylesheet" href="<lams:LAMSURL/>css/x-editable-lams.css"> 
 	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/x-editable.js"></script>
 
 	<title><fmt:message key="authoring.tbl.template.title"/></title>
	
	<script type="text/javascript">

		<%@ include file="../comms.jsp" %>

		var minimumWordsSpinnerArray  = new Array(); // Peer Review Tab.
		var showTime = 100; // how long should it take a field to show/hide.
		
		$(document).ready(function() {
			groupingChanged();

			// validate the main form
			var validator = $("#templateForm").validate({
				rules: {
					sequenceTitle: {
						required: true,
						validateNoSpecialCharacters: true
					},
					<%@ include file="../groupingvalidation.jsp" %>
				},
				messages: {
					sequenceTitle: {
						required: '<fmt:message key="authoring.section.lessondetails" />: <fmt:message key="authoring.fla.title.validation.error" />',
						validateNoSpecialCharacters: '<fmt:message key="authoring.section.lessondetails" />: <fmt:message key="authoring.fla.title.validation.error" />'
					},
	 				<%@ include file="../groupingerrors.jsp" %>
				},
				invalidHandler: templateInvalidHandler,
				errorClass: "text-danger",
				errorLabelContainer: "ul.error-message",
				wrapper: "li",
	  			submitHandler: function(form) { 
	 				submitForm(form);
	 			},
	 			invalidHandler : function() {
	 				$('.button-save').button('reset');
	 			}
			});

			jQuery.validator.addMethod("validateNoSpecialCharacters", validateNoSpecialCharacters, '<fmt:message key="authoring.section.lessondetails" />: <fmt:message key="authoring.fla.title.validation.error" />');
			initializeWizard(validator);
		});

		function createApplicationExercise(numAppexFieldname ) {
			var numAppex = $('#numAppEx');
			var currNum = numAppex.val();
			var nextNum = +currNum + 1;
			var newDiv = document.createElement("div");
			newDiv.id = 'divappex'+nextNum;
			newDiv.className = 'panel panel-default';
			var url=getSubmissionURL()+"/createApplicationExercise.do?appexNumber="+nextNum;
			$('#accordianAppEx').append(newDiv);
			$.ajaxSetup({ cache: true });
			$(newDiv).load(url, function( response, status, xhr ) {
				if ( status == "error" ) {
					console.log( xhr.status + " " + xhr.statusText );
					newDiv.remove();
				} else {
					numAppex.val(nextNum);
					newDiv.scrollIntoView();
					// close all the others
					var i;
					for (i = 1; i <= currNum; i++) {
						$('#collapseAppex'+i).removeClass('in');
					}
				}
			});
		}		

		<%-- matching importQTI(limit) function is in comms.jsp --%>
	    function saveQTI(formHTML, formName, callerID) {
	    	var form = $($.parseHTML(formHTML));

	    	if ( callerID == 'mcq' ) {
		    	var nextNum  = +$('#numQuestions').val()+1,
						url=getSubmissionURL()+'/importQTI.do?contentFolderID=${contentFolderID}&templatePage=mcquestionQTI&questionNumber='
								+nextNum+'&numQuestionsFieldname=numQuestions';
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
	    	} else {
	    		var	appexIndex = +(callerID.substring(5)),
	    				numQuestionsFieldname = 'numAssessments'+appexIndex,
	    				containingDivName = 'divass'+appexIndex,
		    			nextNum  = +$('#'+numQuestionsFieldname).val()+1,
		    			url=getSubmissionURL()+'/importQTI.do?contentFolderID=${contentFolderID}&templatePage=assessmentQTI&questionNumber='
		    					+nextNum+'&containingDivName='+containingDivName+'&numQuestionsFieldname='+numQuestionsFieldname;
				$.ajaxSetup({ cache: true });
				$.ajax({
					type: "POST",
					url: url,
					data: form.serializeArray(),
					success: function(response, status, xhr) {
						if ( status == "error" ) {
							console.log( xhr.status + " " + xhr.statusText );
						} else {
							$('#'+containingDivName).append(response);
							$('#'+containingDivName+'divassess'+nextNum)[0].scrollIntoView();
						}
					}
				});
	    	}
	    }

	</script>
    <script>
        $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();
        });
    </script>
	
</lams:head>
	
<body class="stripes">

<c:set var="title"><fmt:message key="authoring.tbl.template.title"/></c:set>
<lams:Page title="${title}" type="wizard">

<c:set var="usePreview">${fn:toLowerCase('checked') eq 'checked'}</c:set>

	<div id="rootwizard">
	<div class="navbar">
	  <div class="navbar-inner">
	    <div class="container">
		<ul>
		  	<li><a href="#tab1" data-toggle="tab"><fmt:message key="authoring.section.introduction" /> </a></li>
			<li><a href="#tab2" data-toggle="tab"><fmt:message key="authoring.section.lessondetails" /> </a></li>
			<li><a href="#tab3" data-toggle="tab"><fmt:message key="authoring.section.questions" /></a></li>
			<li><a href="#tab4" data-toggle="tab"><fmt:message key="authoring.section.applicationexercise" /></a></li>
			<%--  Hide peer review page if not needed --%>
			<c:if test="${usePreview}">
			<li><a href="#tab5" data-toggle="tab"><fmt:message key="authoring.section.peerreview" /></a></li>
			</c:if>
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
 			<input type="checkbox" name="introduction" value="true" class="form-control-inline" id="introduction" checked />
			<input type="checkbox" name="iratra" value="true" class="form-control-inline" id="iratra" checked />
			<input type="checkbox" name="appex" value="true" class="form-control-inline" id="appex" checked />
			<input type="checkbox" name="preview" value="true" class="form-control-inline" id="preview"  checked  />
 			<input type="checkbox" name="reflect" value="true" class="form-control-inline" id="reflect" checked/>
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
			<span class="field-name"><fmt:message key="authoring.tbl.desc.question"/></span>

			<div class="form-group voffset10">
					<input title="123" type="checkbox" name="confidenceLevelEnable" value="true" class="form-control-inline" id="confidenceLevelEnable" checked/>&nbsp;
				<label for="confidenceLevelEnable">
					<fmt:message key="authoring.enable.confidence.levels"/>
				</label>
                <i class="fa fa-question-circle" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="<fmt:message key='authoring.tbl.enable.confidence.tooltip'/>"></i>
			</div>
			
		 	<input type="hidden" name="numQuestions" id="numQuestions" value="0"/>
			
			<div id="divquestions">
			</div>
		
			<span class="voffset10">
			<a href="#" id="createQuestionButton" onclick="javascript:createQuestion('numQuestions', 'divq', 'divquestions', '', '');" class="btn btn-default"><i class="fa fa-plus"></i> <fmt:message key="authoring.create.question"/></a>
			<a href="#" onClick="javascript:importQTI('mcq', 'mc')" class="btn btn-default pull-right">	<i class="fa fa-upload"></i> <fmt:message key="authoring.template.basic.import.qti" /></a>
			</span>
			
	    </div>
		<div class="tab-pane" id="tab4">
            <span class="field-name"><p><fmt:message key="authoring.tbl.desc.ae" /></p></span>
			
			<input type="hidden" name="numAppEx" id="numAppEx" value="1"/>
	
			<div class="panel-group" id="accordianAppEx" role="tablist" aria-multiselectable="true"> 
			<c:set var="appexNumber" scope="page">1</c:set>
			<%@ include file="appex.jsp" %>
			</div> <!--  end panel group -->

			<a href="#" id="createApplicationExerciseButton" onclick="javascript:createApplicationExercise();" class="btn btn-default"><i class="fa fa-plus"></i> <fmt:message key="authoring.create.application.exercise"/></a>
			
	    </div>

		<c:if test="${usePreview}">
    	<div class="tab-pane" id="tab5">
			<span class="field-name"><fmt:message key="authoring.tbl.desc.peer.review" /></span>
			
		 	<input type="hidden" name="numRatingCriterias" id="numRatingCriterias" value="1"/>
	
			<div id="divratings">
			<div id="divrating1" class="space-top">
			<c:set scope="request" var="criteriaNumber">1</c:set>
			<%@ include file="../tool/peerreviewstar.jsp" %>
			</div>
			</div>
			
			<a id="createCriteria" href="#" onclick="javascript:createPeerReviewCriteria();" class="btn btn-default voffset10"><fmt:message key="authoring.create.criteria"/></a>
	    </div>
	    </c:if>
	    
	    <div id="navigation-buttons" class="voffset10">
                <hr>
	    		<div style="float:right">
		  	<a href="#" class='btn btn-sm btn-primary button-next'><fmt:message key="button.next"/></a>
	    		<a href="#" class='btn btn-sm btn-primary button-save' onclick="javascript:doSaveForm();" style="display:none"
	    		   data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="button.save" /></span>"
	    		 ><fmt:message key="button.save"/></a>
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
		
