<%@ include file="/common/taglibs.jsp"%>

<!--options content goes here-->

<html:hidden property="nextQuestionSelected" />
<html:hidden property="continueOptionsCombined" />
<html:hidden property="questionIndex" value="${mcGeneralLearnerFlowDTO.questionIndex}" />

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
												value="${dto.questionUid}-${option.uid}" <c:if test="${option.selected}">checked="checked"</c:if> /> 
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
	<html:button property="continueOptionsCombined" onclick="submitNextQuestionSelected();"
			styleClass="btn btn-sm btn-primary pull-right">
		<fmt:message key="button.continue" />
	</html:button>
</c:if>

<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
	<html:button property="continueOptionsCombined" onclick="doSubmit();" styleClass="btn btn-sm btn-primary pull-right">
		<fmt:message key="button.continue" />
	</html:button>
</c:if>

<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
	<html:button property="continueOptionsCombined" onclick="submitAllAnswers();"
			styleClass="btn btn-sm btn-primary pull-right">
		<fmt:message key="button.submit" />
	</html:button>
</c:if>

<!--options content ends here-->
