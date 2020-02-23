<%@ include file="/common/taglibs.jsp"%>

<!--options content goes here-->

<form:hidden path="nextQuestionSelected" />
<form:hidden path="continueOptionsCombined" />
<form:hidden path="questionIndex" value="${mcGeneralLearnerFlowDTO.questionIndex}" />

<c:forEach var="dto" varStatus="status" items="${requestScope.learnerAnswersDTOList}">
	<c:if test="${dto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}">

		<div class="row no-gutter">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<table>
							<tr>
								<td style="vertical-align: top;">${dto.displayOrder})</td>
								<td width="100%" style="padding-left: 5px"><c:out value="${dto.question}" escapeXml="false" /></td>
							</tr>
							<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
								<tr>
									<td width="100%" colspan="2">[ <strong><fmt:message key="label.mark" /></strong> <c:out
											value="${dto.mark}" /> ]
									</td>
								</tr>
							</c:if>
						</table>
					</div>
					<!-- Display answers -->
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-hover table-condensed">
								<tbody>
									<c:forEach var="option" varStatus="status" items="${dto.options}">
										<tr>
											<td class="text-nowrap" style="vertical-align: top;">
												<input type="radio"	id="${dto.questionUid}-${option.uid}" name="checkedCa" class="noBorder"
														value="${dto.questionUid}-${option.uid}" 
														<c:if test="${option.selected}">checked="checked"</c:if> 
												/> 
												<c:if test="${isPrefixAnswersWithLetters}">
													<c:set var="seqLetter" value="${status.index}" />
													<%=Character.toChars(97 + (Integer) pageContext.getAttribute("seqLetter"))%>)
                              					</c:if>
                              				</td>

											<td width="100%">
												<label for="${dto.questionUid}-${option.uid}"> <c:out
														value="${option.mcQueOptionText}" escapeXml="false" />
												</label>
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
												<input name="confidenceLevel${dto.questionUid}" class="bootstrap-slider" type="text" 
													data-slider-ticks="[0, 5, 10]" data-slider-ticks-labels='["0", "50", "100%"]' 
													data-slider-enabled="true" data-slider-tooltip="hide"
													<c:if test="${dto.confidenceLevel != -1}">data-slider-value="${dto.confidenceLevel}"</c:if>
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

<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}">
	<button name="continueOptionsCombined" type="button" onclick="submitNextQuestionSelected();"
			class="btn btn-sm btn-primary pull-right">
		<fmt:message key="button.continue" />
	</button>
</c:if>

<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
	<button name="continueOptionsCombined" type="button" onclick="doSubmit();" class="btn btn-sm btn-primary pull-right">
		<fmt:message key="button.continue" />
	</button>
</c:if>

<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
	<button name="continueOptionsCombined" type="button" onclick="submitAllAnswers();"
			class="btn btn-sm btn-primary pull-right">
		<fmt:message key="button.submit" />
	</button>
</c:if>

<!--options content ends here-->
