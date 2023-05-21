<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.choice.title" /></title>
		<lams:css/>
		<style type="text/css">
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
				margin-bottom: 30px;
			}
		</style>
		<script type="text/javascript" src="includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript">
			window.resizeTo(1152, 648);

			var returnURL = '${returnURL}';
			var callerID = '${callerID}';

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

			$(document).ready(function(){
				$('.questionCheckbox').change(function(){
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
					<input type="hidden" name="questionCount" value="${fn:length(questions)}" />

					<c:if test="${not empty collections}">
						<fmt:message key="label.questions.choice.collection.new.option" var="newOptionLabel"/>
						<!-- Choose a collection where questions should be imported to -->
						<label id="collectionSelect">
							<fmt:message key="label.questions.choice.collection" />&nbsp;
							<select name="collectionUid" id="collectionUid" class="form-control">
								<c:forEach items="${collections}" var="collection">
									<option value="${collection.uid}" ${collection.personal ? "selected" : ""}>
										<c:out value="${collection.name}"/>
									</option>
								</c:forEach>
								<option value="-1"><c:out escapeXml="true" value="${newOptionLabel}" /></option>
							</select>
						</label>
					</c:if>

					<div id="selectAllDiv" class="checkbox">
						<label>
							<input id="selectAll" type="checkbox"/>&nbsp;<fmt:message key="label.questions.choice.select.all" />
						</label>
					</div>

					<c:forEach var="question" items="${questions}" varStatus="questionStatus">
						<c:set var="questionEditable" value="${editingEnabled and (question.type eq 'mc' or question.type eq 'mr')}" />
						<c:set var="questionTypeLabel">label.questions.choice.type.${question.type}</c:set>

						<%-- Question itself --%>
						<div class="checkbox">
							<label>
								<input id="question${questionStatus.index}checkbox"
									   data-question-index="${questionStatus.index}" class="questionCheckbox" type="checkbox" />
									${questionStatus.index + 1}. (<fmt:message key="${questionTypeLabel}" />)
								<c:choose>
								<c:when test="${questionEditable}">
							</label>
							<input id="question${questionStatus.index}" name="question${questionStatus.index}"
								   value="<c:out value='${question.title}' />"
								   class="questionAttribute editableAttribute form-control"
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
							   class="questionAttribute" type="hidden" />

						<input id="question${questionStatus.index}text" name="question${questionStatus.index}text"
							   value="<c:out value='${question.text}' />"
							   class="questionAttribute" type="hidden" disabled="disabled" />
						<div class="questionText">${question.text}</div>
						<input type="hidden" id="question${questionStatus.index}type" name="question${questionStatus.index}type"
							   value="${question.type}"
							   class="questionAttribute" disabled="disabled" />

						<c:if test="${not empty question.score}">
							<input type="hidden" id="question${questionStatus.index}score" name="question${questionStatus.index}score"
								   value="${question.score}"
								   class="questionAttribute" disabled="disabled" />
						</c:if>
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
										<c:when test="${questionEditable}">
											<div class="form-group">
												<input name="question${questionStatus.index}answer${answerStatus.index}"
													   value="<c:out value='${answer.text}' />" class="answerVisible editableAttribute form-control"
													   type="text" disabled="disabled" />
												<c:if test="${answer.score == 1}">(<fmt:message key="label.correct"/>)</c:if>
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

						<%-- Learning Outcomes, if required and exist --%>
						<c:if test="${fn:length(question.learningOutcomes) > 0}">
							<input type="hidden" name="learningOutcomeCount${questionStatus.index}"
								   value="${fn:length(question.learningOutcomes)}" />
							<c:forEach var="learningOutcome" items="${question.learningOutcomes}" varStatus="learningOutcomeStatus">
								<input name="question${questionStatus.index}learningOutcome${learningOutcomeStatus.index}"
									   value="<c:out value='${learningOutcome}'/>" type="hidden" />
							</c:forEach>
						</c:if>
					</c:forEach>

					<div id="buttonsDiv" class="voffset10 pull-right">
						<input class="btn btn-default" value='<fmt:message key="button.cancel"/>' type="button" onClick="javascript:window.close()" />
						<input id="submitButton" class="btn btn-primary" value='<fmt:message key="label.ok"/>'      type="button" onClick="javascript:submitForm()" />
					</div>
				</form>
			</c:otherwise>
		</c:choose>

		<div id="footer"></div>
	</lams:Page>
	</body>
</lams:html>