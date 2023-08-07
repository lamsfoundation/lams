<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="allowCreatingQbCollections"><%=Configuration.get(ConfigurationKeys.QB_COLLECTIONS_CREATE_ALLOW)%></c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.choice.title" /></title>
		<lams:css/>
		<link rel="stylesheet" href="${lams}includes/font-awesome/css/font-awesome.min.css" type="text/css" media="screen" />

		<style>
			div.answerDiv {
				padding-left: 50px;
				display: none;
				margin-bottom: 10px;
			}

			div.checkbox, div.form-group {
				width: 100%;
				margin-bottom: 15px !important;
			}

			.editableAttribute {
				width: 80% !important;
			}

			#selectAllDiv {
				display: inline;
			}

			#buttonsDiv {
				padding: 15px 0px 15px 0px;
			}

			div#errorArea {
				display: none;
			}

			#collectionSelect {
				float: right;
				margin-bottom: 30px;
			}

			.questionLabelBadge {
				margin-bottom: 1rem;
			}
		</style>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript">
			window.resizeTo(1152, 648);

			let returnURL = '${returnURL}',
					callerID = '${callerID}',
					questionsContainer = null,
					questionTemplate = null;

			function submitForm() {
				var anyQuestionsSelected = false;
				$('.questionCheckbox').each(function(){
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

			<c:if test="${not empty param.questionSourceKey and not empty questions}">
			function generateQuestions() {
				$('#generateQuestionsButton').prop('disabled', true).button('loading');

				// questionSourceKey comes from the initial request from the form
				$.ajax({
					'url': '${lams}ai/authoring/ratMcqJsonFromSource.do',
					'type': 'post',
					'dataType': 'json',
					'data': {
						'questionSourceKey': '${param.questionSourceKey}',
						'ratQuestions' : JSON.stringify(${questionsJson})
					},
					'complete' : function(){
						$('#generateQuestionsButton').prop('disabled', false).button('reset');
					},
					'error' : function () {
						alert('<fmt:message key="label.questions.choice.generate.more.error" />');
					},
					'success': function (response) {
						let questions = response.questions[0],
								questionCount = +$('#questionCount').val();

						$('#selectAll').attr('checked', false);

						$('#questionCount').val(questionCount + questions.length);
						questionTemplate.find('.questionResourcesFolder').remove();

						// parse question in JSON format and add them to the page
						$.each(questions, function (iterationIndex, question) {
							let questionContainer = questionTemplate.clone().appendTo(questionsContainer),
									questionIndex = questionCount + iterationIndex + 1;

							questionContainer.find('.questionCheckbox').attr('data-question-index', questionIndex)
									.attr('id', 'question' + questionIndex + 'checkbox');
							questionContainer.find('.questionTitleDisplay')
									.text(questionIndex + '. (<fmt:message key="label.questions.choice.type.mc" />)');
							questionContainer.find('.editableQuestionTitle').attr('id', 'question' + questionIndex)
									.attr('name', 'question' + questionIndex).attr('value', question.title)
									.val(question.title);
							questionContainer.find('.questionType').attr('id', 'question' + questionIndex + 'type')
									.attr('name', 'question' + questionIndex + 'type')
									.val('mc');

							if (question.label) {
								questionContainer.find('.questionLabel').attr('id', 'question' + questionIndex + 'label')
										.attr('name', 'question' + questionIndex + 'label')
										.val(question.label);
							}

							if (question.text) {
								questionContainer.find('.questionText').attr('id', 'question' + questionIndex + 'text')
										.attr('name', 'question' + questionIndex + 'text')
										.val(question.text);
								questionContainer.find('.questionTextDisplay').html(question.text);
							} else {
								questionContainer.find('.questionText').remove();
							}

							if (question.score) {
								questionContainer.find('.questionScore').attr('id', 'question' + questionIndex + 'score')
										.attr('name', 'question' + questionIndex + 'score')
										.val(question.score);
							} else {
								questionContainer.find('.questionScore').remove();
							}

							if (question.feedback) {
								questionContainer.find('.questionFeedback').attr('id', 'question' + questionIndex + 'feedback')
										.attr('name', 'question' + questionIndex + 'feedback')
										.val(question.feedback);
							} else {
								questionContainer.find('.questionFeedback').remove();
							}

							let answersDiv = questionContainer.find('.answerDiv');
							if (question.answers) {
								answersDiv.attr('id', 'question' + questionIndex + 'answerDiv')
										.find('.answerCount')
										.attr('name', 'answerCount' + questionIndex)
										.val(question.answers.length);
								let answerTemplate = answersDiv.find('.answerContainer').first().clone();
								answerTemplate.find('.answerCorrect').remove();
								answersDiv.find('.answerContainer').remove();

								$.each(question.answers, function (answerIndex, answer) {
									let answerContainer = answerTemplate.clone().appendTo(answersDiv),
											answerText = answerContainer.find('.answerText')
													.attr('name', 'question' + questionIndex + 'answer' + answerIndex).attr('value', answer.text)
													.val(answer.text);
									if (answer.score == 1) {
										$('<span class="answerCorrect" />').text(' (<fmt:message key="label.correct"/>)').insertAfter(answerText);
									}
									answerContainer.find('.answerScore').attr('name', 'question' + questionIndex + 'answer' + answerIndex + 'score')
											.val(answer.score);
									if (answer.feedback) {
										answerContainer.find('.answerFeedback')
												.attr('name', 'question' + questionIndex + 'answer' + answerIndex + 'feedback')
												.val(answer.feedback);
									}
									answersDiv.append(answerContainer);
								});
							} else {
								answersDiv.remove();
							}
						});
					}
				});
			}
			</c:if>

			$(document).ready(function(){
				questionsContainer = $('#questionsContainer');
				questionTemplate = $('.questionContainer').first().clone();

				$('#questionsContainer').on('change', '.questionCheckbox', function(){
					var checked = $(this).is(':checked'),
							selector = '#question' + $(this).data('questionIndex'),
							answerDiv = $(selector + 'answerDiv'),
							answersVisible = answerDiv.find('.answerVisible').length > 0;


					$(this).attr('checked', checked);
					// enable/disable answers and feedback fields
					// so they do not get posted when not needed
					if (answersVisible) {
						// if this was not tested, there would be a tiny,
						// but annoying movement when question checkbox is clicked
						answerDiv.toggle('slow');
					}
					$('input', answerDiv).add(selector).add(selector + 'feedback').add(selector + 'type').add(selector + 'text')
							.add(selector + 'score').add(selector + 'resourcesFolder')
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

					$('.questionCheckbox').attr('checked', checked);
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

				$('.editableAttribute').change(function(){
					$(this).attr('value', $(this).val());
				});

				// as the form HTML is being passed, not its real values
				// we need to alter HTML manually on collection change
				$('#collectionUid').change(function(){
					let collectionSelect = $(this),
							newValue = collectionSelect.val(),
							previouslySelectedOption = $('option[selected]', collectionSelect);
					if (newValue == -1) {
						// create a new collection on the fly
						let newCollectionName = prompt('<fmt:message key="label.questions.choice.collection.new.prompt" />'),
								newCollectionUid = -1;
						if (newCollectionName) {
							newCollectionName = newCollectionName.trim();
							$.ajax({
								'url' : '${lams}qb/collection/addCollection.do',
								'async' : false,
								'type' : 'post',
								'dataType' : 'text',
								'data' : {
									'name' : newCollectionName,
									'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
								},
								success : function (response){
									if (!isNaN(response)) {
										newCollectionUid = +response;
									}
								},
								error : function (xhr) {
									alert(xhr.responseText);
								}
							});
						}

						if (newCollectionUid == -1 || newCollectionUid == 0) {
							// revert to previous selection
							previouslySelectedOption.prop('selected', true);
						} else {
							$('<option>').attr('value', newCollectionUid).attr('selected', 'selected')
									.prop('selected', true).text(newCollectionName).insertBefore($('option[value="-1"]', collectionSelect));
						}
					} else {
						previouslySelectedOption.removeAttr('selected');
						$('option[value="' + newValue + '"]', collectionSelect).attr('selected', 'selected');
					}
				});
			});
		</script>
	</lams:head>

	<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="label.questions.choice.title" />
	</c:set>

	<lams:Page type="admin" title="${title}">
		<c:choose>
			<c:when test="${empty questions}">
				<lams:Alert type="info" close="false">
					<fmt:message key="label.questions.choice.none.found" />
				</lams:Alert>

				<div id="buttonsDiv" class="voffset10 pull-right">
					<input class="btn btn-default" value='<fmt:message key="button.cancel"/>' type="button" onClick="javascript:window.close()" />
				</div>
			</c:when>
			<c:otherwise>
				<lams:Alert id="errorArea" type="danger" close="false">
					<fmt:message key="label.questions.choice.missing" />
				</lams:Alert>

				<form id="questionForm" method="POST" class="form-inline">
					<input id="questionCount" type="hidden" name="questionCount" value="${fn:length(questions)}" />
					<input type="hidden" name="questionSourceKey" value="${param.questionSourceKey}" />

					<c:if test="${not empty collections}">
						<!-- Choose a collection where questions should be imported to -->
						<label id="collectionSelect">
							<fmt:message key="label.questions.choice.collection" />&nbsp;
							<select name="collectionUid" id="collectionUid" class="form-control">
								<c:forEach items="${collections}" var="collection">
									<option value="${collection.uid}" ${collection.personal ? "selected" : ""}>
										<c:out value="${collection.name}"/>
									</option>
								</c:forEach>
								<c:if test="${allowCreatingQbCollections}">
									<fmt:message key="label.questions.choice.collection.new.option" var="newOptionLabel"/>
									<option value="-1"><c:out escapeXml="true" value="${newOptionLabel}" /></option>
								</c:if>
							</select>
						</label>
					</c:if>

					<div id="selectAllDiv" class="checkbox">
						<label>
							<input id="selectAll" type="checkbox"/>&nbsp;<fmt:message key="label.questions.choice.select.all" />
						</label>
					</div>

					<div id="questionsContainer">
						<c:forEach var="question" items="${questions}" varStatus="questionStatus">
							<c:set var="questionEditable" value="${editingEnabled and (question.type eq 'mc' or question.type eq 'mr')}" />
							<c:set var="questionTypeLabel">label.questions.choice.type.${question.type}</c:set>

							<div class="questionContainer">
									<%-- Question itself --%>
								<div class="checkbox">
									<label>
										<input id="question${questionStatus.index}checkbox"
											   data-question-index="${questionStatus.index}" class="questionCheckbox" type="checkbox" />
										<span class="questionTitleDisplay">${questionStatus.index + 1}. (<fmt:message key="${questionTypeLabel}" />)</span>
										<c:choose>
										<c:when test="${questionEditable}">
									</label>
									<input id="question${questionStatus.index}" name="question${questionStatus.index}"
										   value="<c:out value='${question.title}' />"
										   class="questionAttribute editableAttribute editableQuestionTitle form-control"
										   type="text" disabled="disabled" />
									</c:when>
									<c:otherwise>
										<c:out value='${question.title}' />
										</label>
										<input id="question${questionStatus.index}" name="question${questionStatus.index}"
											   value="<c:out value='${question.title}' />" class="questionAttribute" type="hidden" />
									</c:otherwise>
									</c:choose>
								</div>

								<input id="question${questionStatus.index}label" name="question${questionStatus.index}label"
									   value="<c:out value='${question.label}' />"
									   class="questionAttribute questionLabel" type="hidden" />

								<input id="question${questionStatus.index}text" name="question${questionStatus.index}text"
									   value="<c:out value='${question.text}' />"
									   class="questionAttribute questionText" type="hidden" disabled="disabled" />
								<div class="questionTextDisplay">${question.text}</div>
								<input type="hidden" id="question${questionStatus.index}type" name="question${questionStatus.index}type"
									   value="${question.type}"
									   class="questionAttribute questionType" disabled="disabled" />

								<c:if test="${not empty question.score}">
									<input type="hidden" id="question${questionStatus.index}score" name="question${questionStatus.index}score"
										   value="${question.score}"
										   class="questionAttribute questionScore" disabled="disabled" />
								</c:if>
									<%-- Question feedback --%>
								<input type="hidden" id="question${questionStatus.index}feedback" name="question${questionStatus.index}feedback"
									   value="<c:out value='${question.feedback}' />"
									   class="questionAttribute questionFeedback" disabled="disabled" />
									<%-- If question contains images, where to take them from --%>
								<input type="hidden" id="question${questionStatus.index}resourcesFolder"
									   name="question${questionStatus.index}resourcesFolder"
									   value="<c:out value='${question.resourcesFolderPath}' />"
									   class="questionAttribute questionResourcesFolder" disabled="disabled" />
									<%-- Answers, if required and exist --%>
								<c:if test="${fn:length(question.answers) > 0 or not empty question.label}">
									<div id="question${questionStatus.index}answerDiv" class="answerDiv">
										<c:if test="${not empty question.label}">
											<span class="questionLabelBadge badge" title="<fmt:message key="label.questions.choice.taxonomy" />">
												<c:out value="${question.label}" />
											</span>
										</c:if>

										<input type="hidden" name="answerCount${questionStatus.index}"
											   value="${fn:length(question.answers)}" class="answerCount" />

										<c:forEach var="answer" items="${question.answers}" varStatus="answerStatus">
											<div class="answerContainer">
													<%-- Answer itself --%>
												<c:choose>
													<c:when test="${questionEditable}">
														<div class="form-group">
															<input name="question${questionStatus.index}answer${answerStatus.index}"
																   value="<c:out value='${answer.text}' />" class="answerText answerVisible editableAttribute form-control"
																   type="text" disabled="disabled" />
															<c:if test="${answer.score == 1}"><span class="answerCorrect">(<fmt:message key="label.correct"/>)</span></c:if>
														</div>
													</c:when>
													<c:when test="${question.type eq 'mc' or question.type eq 'mr' or question.type eq 'mh' or question.type eq 'fb'}">
														<input name="question${questionStatus.index}answer${answerStatus.index}"
															   value="<c:out value='${answer.text}' />" class="answerVisible"
															   type="hidden" disabled="disabled" />
														<c:if test="${answer.score == 1}"><b></c:if>
														- ${answer.text}
														<c:if test="${answer.score == 1}">&nbsp;(<fmt:message key="label.correct"/>)</b></c:if>
														<br />
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
													   class="answerScore" value="${answer.score}" disabled="disabled" />
												<input type="hidden" name="question${questionStatus.index}answer${answerStatus.index}feedback"
													   class="answerFeedback" value="<c:out value='${answer.feedback}' />" disabled="disabled" />
											</div>
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

									<%-- Learning Outcomes, if required and exist --%>
								<c:if test="${fn:length(question.learningOutcomes) > 0}">
									<input type="hidden" name="learningOutcomeCount${questionStatus.index}"
										   value="${fn:length(question.learningOutcomes)}" />
									<c:forEach var="learningOutcome" items="${question.learningOutcomes}" varStatus="learningOutcomeStatus">
										<input name="question${questionStatus.index}learningOutcome${learningOutcomeStatus.index}"
											   value="<c:out value='${learningOutcome}'/>" type="hidden" />
									</c:forEach>
								</c:if>
							</div>
						</c:forEach>
					</div>


					<div id="buttonsDiv" class="voffset10">
						<c:if test="${not empty param.questionSourceKey and not empty questions}">
							<button id="generateQuestionsButton" class="btn btn-primary" type="button"
									data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="label.questions.choice.generate.more" /></span>"
									onClick="javascript:generateQuestions()">
								<fmt:message key="label.questions.choice.generate.more"/>
							</button>
						</c:if>

						<input id="submitButton" class="btn btn-primary loffset10 pull-right" value='<fmt:message key="label.ok"/>' type="button" onClick="javascript:submitForm()" />
						<input class="btn btn-default pull-right" value='<fmt:message key="button.cancel"/>' type="button" onClick="javascript:window.close()" />
					</div>
				</form>
			</c:otherwise>
		</c:choose>

		<div id="footer"></div>
	</lams:Page>
	</body>
</lams:html>