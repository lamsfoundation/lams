<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.qb.print.title">
				<fmt:param value="${printTitleSuffix}" />
			</fmt:message>
		</title>
		<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
		<style>
			body {
				text-align: center;
				overflow: visible;
			}

			#question-list {
				display: inline-block;
				text-align: left;
			}

			.option-correct-name {
				font-weight: bold;
				color: var(--bs-success);
			}

			.hide-answers .option-feedback,
			.hide-answers .option-correct-label {
				display: none;
			}

			.hide-answers .option-correct-name {
				font-weight: initial;
				color: initial;
			}

			@media print {
				.hide-print {
					display: none;
				}

				body {
					text-align: left;
				}

				.question-item, img {
					break-inside: avoid;
				}
			}
		</style>

		<script src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
		<script>
			function printQuestions() {
				window.print();
			}
			function hideAnswers(checkbox) {
				$('body').toggleClass('hide-answers', checkbox.checked);
			}
		</script>
	</lams:head>
	<body>
	<div class="hide-print ms-2 mt-2">
		<button type="button" class="hide-print btn btn-primary"
				onclick="javascript:printQuestions()">
			<i class="fa-solid fa-print"></i> <fmt:message key="label.print" />
		</button>

		<div class="form-check form-check-inline ms-5">
			<input class="form-check-input" type="checkbox" id="hide-answers-checkbox"
				   onchange="javascript:hideAnswers(this)">
			<label class="form-check-label" for="hide-answers-checkbox">
				<fmt:message key="label.qb.print.hide.answers" />
			</label>
		</div>

	</div>

	<ol id="question-list" class="mt-4">
		<c:forEach var="question" items="${printQuestions}">
			<li class="mb-4 question-item">
				<p class="fw-bold"><c:out value="${question.name}" /></p>
				<c:out value="${question.description}" escapeXml="false" />
				<ol type="a">
					<c:forEach var="option" items="${question.qbOptions}">
						<li>
							<div class="option-name ${option.maxMark > 0 ? "option-correct-name" : ""}">
								<c:out value="${option.name}" escapeXml="false" />
								<c:if test="${option.maxMark > 0}">
									<span class="option-correct-label">
										(<fmt:message key="label.correct" />)
									</span>
								</c:if>
							</div>
							<c:if test="${not empty option.feedback}">
								<span class="text-muted option-feedback"
									  title="<fmt:message key="label.authoring.basic.option.feedback" />">
									<c:out value="${option.feedback}" escapeXml="false" />
								</span>
							</c:if>
						</li>
					</c:forEach>
				</ol>
			</li>
		</c:forEach>
	</ol>
	</body>
</lams:html>