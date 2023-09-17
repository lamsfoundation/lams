<%@ include file="/common/taglibs.jsp"%>

<!--options content goes here-->

<form:hidden path="nextQuestionSelected" />
<form:hidden path="continueOptionsCombined" />
<form:hidden path="questionIndex" value="${mcGeneralLearnerFlowDTO.questionIndex}" />

<c:forEach var="answerDto" varStatus="status" items="${requestScope.learnerAnswerDtos}">
	<c:if test="${answerDto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}">

		<div class="row g-0">
			<div class="col-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<table>
							<tr>
								<td style="vertical-align: top;">
									${answerDto.displayOrder})
								</td>
								<td width="100%" style="padding-left: 5px">
									<c:if test="${not sessionMap.hideTitles}">
										<c:out value="${answerDto.questionName}" escapeXml="false" />
	                					<br>
                					</c:if>
                					<c:out value="${answerDto.questionDescription}" escapeXml="false" />
								</td>
							</tr>
							<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
								<tr>
									<td width="100%" colspan="2">
										[ <strong><fmt:message key="label.mark" /></strong> <c:out value="${answerDto.mark}" /> ]
									</td>
								</tr>
							</c:if>
						</table>
					</div>
					
					<!-- Display answers -->
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover table-sm">
								<tbody>
									<c:forEach var="option" varStatus="status" items="${answerDto.options}">
										<tr>
											<td class="text-nowrap" width="100%">
												<div class="form-check">
													<input type="radio"	id="${answerDto.questionUid}-${option.qbOption.uid}" name="checkedCa" class="form-check-input"
															value="${answerDto.questionUid}-${option.qbOption.uid}" 
															<c:if test="${option.selected}">checked="checked"</c:if> 
													/> 
	
													<label for="${answerDto.questionUid}-${option.qbOption.uid}" class="form-check-label">
														<c:if test="${isPrefixAnswersWithLetters}">
															<c:set var="seqLetter" value="${status.index}" />
															<%=Character.toChars(97 + (Integer) pageContext.getAttribute("seqLetter"))%>)
		                              					</c:if>
	                              					 
														<c:out value="${option.qbOption.name}" escapeXml="false" />
													</label>
												</div>
											</td>
										</tr>
									</c:forEach>
									
									<c:if test="${sessionMap.content.enableConfidenceLevels}">
						                <tr>
						                  <td colspan="2">
						                  	<div class="question-type">
												<fmt:message key="label.confidence" />
											</div>
						
											<div>
												<input name="confidenceLevel${answerDto.questionUid}" class="bootstrap-slider" type="text" 
													data-slider-ticks="[0, 5, 10]" data-slider-ticks-labels='["0", "50", "100%"]' 
													data-slider-enabled="true" data-slider-tooltip="hide"
													<c:if test="${answerDto.confidenceLevel != -1}">data-slider-value="${answerDto.confidenceLevel}"</c:if>
												/>
											</div>
						                  </td>
						                </tr>
					                </c:if>

								</tbody>
							</table>
						</div>
					</div>
					<!-- end display answers -->
				</div>
			</div>
		</div>
	</c:if>
</c:forEach>

<div class="activity-bottom-buttons">
	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}">
		<button name="continueOptionsCombined" type="button" onclick="submitNextQuestionSelected();"
				class="btn btn-primary">
			<fmt:message key="button.continue" />
		</button>
	</c:if>
	
	<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
		<button name="continueOptionsCombined" type="button" onclick="doSubmit();" class="btn btn-primary">
			<fmt:message key="button.continue" />
		</button>
	</c:if>
	
	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
		<button name="continueOptionsCombined" type="button" onclick="submitAllAnswers();"
				class="btn btn-primary">
			<fmt:message key="button.submit" />
		</button>
	</c:if>
</div>

<!--options content ends here-->
