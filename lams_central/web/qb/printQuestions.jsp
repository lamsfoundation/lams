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
				padding-left: 1rem;
			}

			#question-list {
				display: inline-block;
				text-align: left;
			}

			.option-correct-name {
				font-weight: bold;
				color: var(--bs-success);
			}

			.reveal-answers {
				display: none;
			}

			.hide-answers .option-feedback,
			.hide-answers .option-correct-label {
				display: none;
			}

			.hide-answers .option-correct-name {
				font-weight: initial;
				color: initial;
			}

			.hide-answers .reveal-answers {
				display: block;
			}

			@media print {
				.hide-print {
					display: none;
				}

				body {
					text-align: left;
					padding-left: 0;
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
	<c:choose>
		<c:when test="${empty printQuestions}">
			<div class="alert alert-warning">
				<fmt:message key="label.questions.choice.none.found" />
			</div>
		</c:when>
		<c:otherwise>
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
						<c:choose>
							<c:when test="${question.type eq 1 or question.type eq 3 or question.type eq 8}">
								<!-- Multiple choice and Short Answers and Mark Hedging -->
								<ol type="a">
									<c:forEach var="option" items="${question.qbOptions}">
										<li>
											<div class="${option.maxMark > 0 ? "option-correct-name" : ""}">
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
							</c:when>
							<c:when test="${question.type eq 2}">
								<!-- Matching pairs -->
								<ol type="a">
									<c:forEach var="option" items="${question.qbOptions}">
										<li>
											<c:out value="${option.matchingPair}" escapeXml="false" />
											<span class="option-correct-name option-correct-label">
													(<c:out value="${option.name}" escapeXml="false" />)
												</span>
										</li>
									</c:forEach>
								</ol>
								<ol type="A" class="reveal-answers mt-4">
									<c:forEach var="matchingAnswer" items="${printRandomisedOptions[question.uid]}">
										<li>
											<c:out value="${matchingAnswer}" escapeXml="false" />
										</li>
									</c:forEach>
								</ol>
							</c:when>
							<c:when test="${question.type eq 4}">
								<!-- Numerical -->
								<ol type="a">
									<c:forEach var="option" items="${question.qbOptions}">
										<li>
											<div class="${option.maxMark > 0 ? "option-correct-name" : ""}">
												<c:out value="${option.numericalOption}" escapeXml="false" />
												<c:if test="${option.maxMark > 0}">
														<span class="option-correct-label">
															(<fmt:message key="label.correct" />)
														</span>
												</c:if>
											</div>
											<p>
												<fmt:message key="label.authoring.basic.option.accepted.error" />:
												<c:out value="${option.acceptedError}" escapeXml="false" />
											</p>
											<c:if test="${not empty option.feedback}">
													<span class="text-muted option-feedback"
														  title="<fmt:message key="label.authoring.basic.option.feedback" />">
														<c:out value="${option.feedback}" escapeXml="false" />
													</span>
											</c:if>
										</li>
									</c:forEach>
								</ol>
							</c:when>
							<c:when test="${question.type eq 5}">
								<!-- True/False -->
								<p class="option-correct-name option-correct-label">
									<c:choose>
										<c:when test="${question.correctAnswer}">
											(<fmt:message key="label.authoring.true.false.true" />)
										</c:when>
										<c:otherwise>
											(<fmt:message key="label.authoring.true.false.false" />)
										</c:otherwise>
									</c:choose>
								</p>
							</c:when>
							<c:when test="${question.type eq 7}">
								<!-- Ordering -->
								<ol type="a" class="option-correct-label">
									<c:forEach var="option" items="${question.qbOptions}">
										<li>
											<c:out value="${option.name}" escapeXml="false" />
										</li>
									</c:forEach>
								</ol>
								<ol type="a" class="reveal-answers">
									<c:forEach var="randomisedOption" items="${printRandomisedOptions[question.uid]}">
										<li>
											<c:out value="${randomisedOption}" escapeXml="false" />
										</li>
									</c:forEach>
								</ol>
							</c:when>
						</c:choose>
					</li>
				</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>
	</body>
</lams:html>