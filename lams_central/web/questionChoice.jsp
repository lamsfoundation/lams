<!DOCTYPE HTML>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-function" prefix="fn" %>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.choice.title" /></title>
	<lams:css/>	
	<style type="text/css">
		div.answerDiv {
			padding-left: 20px;
			display: none;
		}
		
		input[type="checkbox"] {
			border: none;
			margin: 10px 10px 0px 15px;
		}
		
		div.answerDiv input {
			margin-top: 5px;
		}
		
		.button {
			float: right;
			margin-left: 10px;
		}
		
		#buttonsDiv {
			padding: 15px 0px 15px 0px;
		}
		
		div#errorArea {
			display: none;
		}
	</style>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
		window.resizeTo(800, 600);
	
		var returnURL = '${returnURL}';
		var chooseAnswers = '${chooseAnswers}';
		
		function submitForm() {
			var anyQuestionsSelected = false;
			$('.question').each(function(){
				if ($(this).is(':checked')) {
					anyQuestionsSelected = true;
					return false;
				}
			});
			
			if (anyQuestionsSelected) {
				if (returnURL == '') {
					var queryString = $('#questionForm').formSerialize();
					window.opener.saveQTI(queryString);
				} else {
					$('#questionForm').ajaxSubmit({
						'success' : function(){
							window.opener.location.reload();	
					}});
				}
				window.close();
			} else {
				$('#errorArea').show();
			}
		}
		
		$(document).ready(function(){
			if (chooseAnswers == 'true') {
				$('.question').change(function(){
					var checked = $(this).is(':checked');
					var selector = '#' + $(this).attr('id');
					
					// enable/disable answers and feedback fields
					// so they do not get posted when not needed
					$(selector + 'answerDiv').toggle('slow')
					    .find('input')
					    .add(selector + 'feedback')
					    .attr('disabled', checked ? null : 'disabled');
					
					if (checked) {
						$('#errorArea').hide('slow');
					} else {
						// one unchecked question makes "Select all" checkbox uncheck as well
						$('#selectAll').attr('checked', false);
					}
				});
			}
			
			$('#selectAll').click(function(){
				var checked = $(this).is(':checked');
				
				$('.question').attr('checked', checked);
				$('.questionFeedback').attr('disabled', checked ? null : 'disabled');
				if (checked) {
					$('#errorArea').hide('slow');
				}
				
				if (chooseAnswers == 'true') {
					if (checked) {
						$('.answerDiv').show('slow')
						  .find('input').attr('disabled', null);
					} else {
						$('.answerDiv').hide('slow')
						   .find('input').attr('disabled', 'disabled');
					}
				}
			});
		});
	</script>
</lams:head>

<body class="stripes">
<div id="content">
	<div id="errorArea" class="warning" >
		<fmt:message key="label.questions.choice.missing" />
	</div>
	
	<h2><fmt:message key="label.questions.choice.title" /></h2>
	
	<input id="selectAll" type="checkbox" /><fmt:message key="label.questions.choice.select.all" /><br /><br />
			       
	<form id="questionForm" action="${returnURL}">
		<input type="hidden" name="chooseAnswers" value="${chooseAnswers}" />
		<input type="hidden" name="questionCount" value="${fn:length(questions)}" />
		
		<c:forEach var="question" items="${questions}" varStatus="questionStatus">
			<!-- Question itself -->
			<input id="question${questionStatus.index}" name="question${questionStatus.index}"
			       value="<c:out value='${question.text}' />"
			       class="question" type="checkbox" />${questionStatus.index + 1}. <c:out value='${question.text}' /><br />
			<!-- Question feedback -->
		    <input type="hidden" id="question${questionStatus.index}feedback" name="question${questionStatus.index}feedback"
		           value="${question.feedback}"
		           class="questionFeedback" disabled="disabled" />
		    <!-- Answers, if required and exist -->
			<c:if test="${chooseAnswers and fn:length(question.answers) > 0}">
				<div id="question${questionStatus.index}answerDiv" class="answerDiv">
					<input type="hidden" name="answerCount${questionStatus.index}" value="${fn:length(question.answers)}" />
					<c:forEach var="answer" items="${question.answers}" varStatus="answerStatus">
						<!-- Answers itself -->
						<input name="question${questionStatus.index}answer${answerStatus.index}"
			       			   value="<c:out value='${answer.text}' />"
			       			   type="checkbox" checked="checked" disabled="disabled" /><c:out value='${answer.text}' /><br />
			       		<!-- Answers score and feedback -->
			       		<input type="hidden" name="question${questionStatus.index}answer${answerStatus.index}score"
						       value="${answer.score}" disabled="disabled" />
						<input type="hidden" name="question${questionStatus.index}answer${answerStatus.index}feedback"
		           			   value="${answer.feedback}" disabled="disabled" />
					</c:forEach>
				</div>
			</c:if>
		</c:forEach>
		
		<div id="buttonsDiv">
			<input class="button" value='<fmt:message key="button.cancel"/>' type="button" onClick="javascript:window.close()" />
			<input class="button" value='<fmt:message key="label.ok"/>'      type="button" onClick="javascript:submitForm()" />			
		</div>
	</form>
</div>   
<div id="footer"></div>
</body>
</lams:html>