<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.choice.title" /></title>
	<lams:css/>	
	<style type="text/css">
		div.answerDiv {
			padding-left: 25px;
			display: none;
		}
		
		input[type="checkbox"] {
			border: none;
			margin: 10px 10px 0px 15px;
		}
		
		.questionText {
			margin-left: 43px;
			overflow: auto;
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
		
		#collectionSelect {
			float: right;
		}
	</style>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		window.resizeTo(1152, 648);
	
		var returnURL = '${returnURL}';
		var callerID = '${callerID}';
		
		function submitForm() {
			var anyQuestionsSelected = false;
			$('.question').each(function(){
				if ($(this).is(':checked')) {
					anyQuestionsSelected = true;
					return false;
				}
			});
			
			if (anyQuestionsSelected) {
				var form = $("#questionForm");
				if (returnURL == '') {
					form.css('visibility', 'hidden');
					window.opener.saveQTI(form[0].outerHTML, 'questionForm', callerID);
					// needs to be called twice for Chrome to close pop up window
					window.close();
					window.close();
				} else {
					// this code is not really used at the moment, but it's available
					$.ajax({
						type: "POST",
						url: returnURL,
						data: form.serializeArray(),
						success: function(response) {
							window.opener.location.reload();
							// needs to be called twice for Chrome to close pop up window
							window.close();
							window.close();
						}
					});
				}
			} else {
				$('#errorArea').show();
			}

		}
		
		$(document).ready(function(){
			$('.question').change(function(){
				var checked = $(this).is(':checked'),
					selector = '#' + $(this).attr('id'),
					answerDiv = $(selector + 'answerDiv'),
					answersVisible = answerDiv.find('input[type="checkbox"]').length > 0;
					
				$(this).attr('checked', checked);
				// enable/disable answers and feedback fields
				// so they do not get posted when not needed
				if (answersVisible) {
					// if this was not tested, there would be a tiny,
					// but annoying movement when question checkbox is clicked 
					answerDiv.toggle('slow');
				}
				$('input', answerDiv).add(selector + 'feedback').add(selector + 'type').add(selector + 'text')
				                     .add(selector + 'resourcesFolder')
				                     .attr('disabled', checked ? null : 'disabled');
				
				if (checked) {
					$('#errorArea').hide('slow');
				} else {
					// one unchecked question makes "Select all" checkbox uncheck as well
					$('#selectAll').attr('checked', false);
				}
			});
			
			
			$('#selectAll').click(function(){
				var checked = $(this).is(':checked');
				
				$('.question').attr('checked', checked);
				$('.questionAttribute').attr('disabled', checked ? null : 'disabled');
				if (checked) {
					$('#errorArea').hide('slow');
				}
				
				if (checked) {
					$('.answerDiv').show('slow')
					  .find('input').attr('disabled', null);
				} else {
					$('.answerDiv').hide('slow')
					   .find('input').attr('disabled', 'disabled');
				}
			});
			
			$('.answer').change(function(){
				// FF does not seem to do it right
				$(this).attr('checked', $(this).is(':checked'));
			});
		});
	</script>
</lams:head>

<body class="stripes">

<c:set var="title" scope="request">
	<fmt:message key="label.questions.choice.title" />
</c:set>

<lams:Page type="admin" title="${title}">

	<lams:Alert id="errorArea" type="danger" close="false">
		<fmt:message key="label.questions.choice.missing" />
	</lams:Alert>
	
	<input id="selectAll" type="checkbox" /><fmt:message key="label.questions.choice.select.all" /><br /><br />
			       
	<form id="questionForm" method="POST">
		<input type="hidden" name="questionCount" value="${fn:length(questions)}" />
		
		<c:if test="${not empty collections}">
			<!-- Choose a collection where questions should be imported to -->
			<label id="collectionSelect">
				<fmt:message key="label.questions.choice.collection" />&nbsp;
				<select name="collectionUid">
					<c:forEach items="${collections}" var="collection">
						<option value="${collection.uid}" ${empty collection.userId ? "selected" : ""}>
							<c:out value="${collection.name}" />
						</option>
					</c:forEach>
				</select>
			</label>
		</c:if>
		
		<c:forEach var="question" items="${questions}" varStatus="questionStatus">
			<%-- Question itself --%>
			<input id="question${questionStatus.index}" name="question${questionStatus.index}"
			       value="<c:out value='${question.title}' />"
			       class="question" type="checkbox" />${questionStatus.index + 1}.
     		<c:choose>
				<c:when test="${question.type eq 'mc'}">
					(<fmt:message key="label.questions.choice.type.mc" />)
				</c:when>
				<c:when test="${question.type eq 'mr'}">
					(<fmt:message key="label.questions.choice.type.mr" />)
				</c:when>
				<c:when test="${question.type eq 'mt'}">
					(<fmt:message key="label.questions.choice.type.mt" />)
				</c:when>
				<c:when test="${question.type eq 'fb'}">
					(<fmt:message key="label.questions.choice.type.fb" />)
				</c:when>
				<c:when test="${question.type eq 'es'}">
					(<fmt:message key="label.questions.choice.type.es" />)
				</c:when>
				<c:when test="${question.type eq 'tf'}">
					(<fmt:message key="label.questions.choice.type.tf" />)
				</c:when>
				<c:otherwise>
					(<fmt:message key="label.questions.choice.type.unknown" />)
				</c:otherwise>
			</c:choose>
			<c:out value='${question.title}' />
			
			<input id="question${questionStatus.index}label" name="question${questionStatus.index}label"
			       value="<c:out value='${question.label}' />"
			       class="questionAttribute" type="hidden" />
			       
			<input id="question${questionStatus.index}text" name="question${questionStatus.index}text"
			       value="<c:out value='${question.text}' />"
			       class="questionAttribute" type="hidden" disabled="disabled" />
			       <div class="questionText">${question.text}</div><br />
			<input type="hidden" id="question${questionStatus.index}type" name="question${questionStatus.index}type"
		           value="${question.type}"
		           class="questionAttribute" disabled="disabled" />
			<%-- Question feedback --%>
		    <input type="hidden" id="question${questionStatus.index}feedback" name="question${questionStatus.index}feedback"
		           value="<c:out value='${question.feedback}' />"
		           class="questionAttribute" disabled="disabled" />
		    <%-- If question contains images, where to take them from --%>
		    <input type="hidden" id="question${questionStatus.index}resourcesFolder"
		           name="question${questionStatus.index}resourcesFolder"
		           value="<c:out value='${question.resourcesFolderPath}' />"
		           class="questionAttribute" disabled="disabled" />
		    <%-- Answers, if required and exist --%>
			<c:if test="${fn:length(question.answers) > 0}">
				<div id="question${questionStatus.index}answerDiv" class="answerDiv">
					<input type="hidden" name="answerCount${questionStatus.index}" 
					       value="${fn:length(question.answers)}" />
					<c:forEach var="answer" items="${question.answers}" varStatus="answerStatus">
						<%-- Answer itself --%>
						<c:choose>
							<c:when test="${question.type eq 'mc' or question.type eq 'mr' or question.type eq 'fb'}">
								<input name="question${questionStatus.index}answer${answerStatus.index}"
					       			   value="<c:out value='${answer.text}' />" class="answer"
					       			   type="checkbox" checked="checked" disabled="disabled" />${answer.text}<br />
			       			</c:when>
			       			<c:otherwise>
			       				<%-- Do not display answers if management is too difficult or pointless --%>
								<input name="question${questionStatus.index}answer${answerStatus.index}"
					       			   value="<c:out value='${answer.text}' />"
					       			   type="hidden" disabled="disabled" />
							</c:otherwise>
						</c:choose>
			       		<%-- Answers score and feedback --%>
			       		<input type="hidden" name="question${questionStatus.index}answer${answerStatus.index}score"
						       value="${answer.score}" disabled="disabled" />
						<input type="hidden" name="question${questionStatus.index}answer${answerStatus.index}feedback"
		           			   value="<c:out value='${answer.feedback}' />" disabled="disabled" />
					</c:forEach>
					<c:if test="${question.type eq 'mt'}">
						<input type="hidden" name="matchAnswerCount${questionStatus.index}" 
						       value="${fn:length(question.matchAnswers)}" />
						<c:forEach var="matchAnswer" items="${question.matchAnswers}" varStatus="matchAnswerStatus">
							<input name="question${questionStatus.index}matchAnswer${matchAnswerStatus.index}"
					       		   value="<c:out value='${matchAnswer.text}' />"
					       		   type="hidden" disabled="disabled" />
						</c:forEach>
						<c:forEach var="entry" items="${question.matchMap}">
				       		<input name="question${questionStatus.index}match${entry.key}"
				       		   	   value="${entry.value}"
				       		   	   type="hidden" disabled="disabled" />
					    </c:forEach>
					</c:if>
				</div>
			</c:if>
		</c:forEach>
		
		<div id="buttonsDiv" class="voffset10 pull-right">
			<input class="btn btn-default" value='<fmt:message key="button.cancel"/>' type="button" onClick="javascript:window.close()" />
			<input id="submitButton" class="btn btn-primary" value='<fmt:message key="label.ok"/>'      type="button" onClick="javascript:submitForm()" />			
		</div>
	</form>

<div id="footer"></div>
</lams:Page>
</body>
</lams:html>