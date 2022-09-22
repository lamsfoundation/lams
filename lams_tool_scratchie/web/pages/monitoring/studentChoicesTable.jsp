<%@ include file="/common/taglibs.jsp"%>

<table id="questions-data" class="table table-bordered table-hover table-condensed">
	<thead>
		<tr role="row" class="border-top-0">
			<th></th>
			<c:forEach var="item" items="${items}" varStatus="i">
				<th class="text-center">
					<a data-bs-toggle="modal" data-bs-target="#question${i.index}Modal" href="#">
						<fmt:message key="label.authoring.basic.question.text"/> ${i.index + 1}
					</a>
					<c:if test="${not empty burningQuestionItemDtos}">
						<c:forEach var="burningQuestionDto" items="${burningQuestionItemDtos}">
							<c:if test="${burningQuestionDto.scratchieItem.uid eq item.uid and not empty burningQuestionDto.burningQuestionDtos}">
								<a href="#" class="badge rounded-pill bg-warning text-decoration-none text-dark align-middle ms-1"
								   title='<fmt:message key="label.burning.questions"/>'
								   onClick="javascript:loadTab('burningQuestions')" >
									<c:out value="${fn:length(burningQuestionDto.burningQuestionDtos)}" />
								</a>
							</c:if>
						</c:forEach>
					</c:if>
				</th>
			</c:forEach>
			
			<th class="text-center">
				<fmt:message key="label.total"/>&nbsp;
				<lams:Popover>
					<fmt:message key="label.total.1st.attempt.by.team"/>
				</lams:Popover>
			</th>
			<th class="text-center">
				<fmt:message key="label.total"/> %
			</th>
		</tr>
	</thead>
	<tbody>
	
		<tr>
			<th>
				<b>
					<fmt:message key="label.correct.answer"/>
				</b>
			</th>
			<c:forEach var="item" items="${items}">
				<td class="text-center">
					<c:if test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
						${item.correctAnswerLetter}
					</c:if>
				</td>
			</c:forEach>
			
			<td class="text-center"></td>
			<td class="text-center"></td>
		</tr>
	</tbody>
	<tbody>
		<c:if test="${not empty sessionDtos}">
			<tr>
				<th style="font-weight: bold;">
					<fmt:message key="label.teams.notuppercase"/>
				</th> 
			</tr>
			
			<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="i">
				<tr>
					<th class="text-center">
						<c:if test="${not showStudentChoicesTableOnly or sessionMap.isGroupedActivity}">
							<c:choose>
								<c:when test="${empty sessionDto.leaderUid}">
									${sessionDto.sessionName}
								</c:when>
								<c:otherwise>
									<c:url var="userSummaryUrl" value='/learning/start.do'>
										<c:param name="userID" value="${sessionDto.leaderUid}" />
										<c:param name="toolSessionID" value="${sessionDto.sessionId}" />
										<c:param name="mode" value="teacher" />
									</c:url>
									<a href="#" onClick="javascript:launchPopup('${userSummaryUrl}', 'MonitoringReview')">${sessionDto.sessionName}</a>
								</c:otherwise>
							</c:choose>
							
						</c:if>
					</th>
					
					<c:choose>
						<c:when test="${empty sessionDto.itemDtos}">
							<c:forEach begin="1" end="${fn:length(items) + 2}">
								<td></td>
							</c:forEach>
						</c:when>
						
						<c:otherwise>
							<c:forEach var="itemDto" items="${sessionDto.itemDtos}">
								<td class="text-center">
									<c:forEach var="optionDto" items="${itemDto.optionDtos}">
										<c:if test="${optionDto.answer != ''}">
											<span class="user-response <c:if test="${optionDto.correct}">successful-response</c:if> <c:if test="${!optionDto.correct}">wrong-response</c:if>">
												<c:choose>
													<c:when test="${itemDto.type == 1 or itemDto.type == 8}">
														<c:out value="${optionDto.answer}" escapeXml="false"/>
													</c:when>
													<c:when test="${optionDto.correct}"><i class="fa fa-check"></i></c:when>
													<c:otherwise><i class="fa fa-close"></i></c:otherwise>
												</c:choose>
												
											</span>
										</c:if>
									</c:forEach>
								</td>
							</c:forEach>
							
							<c:set var="highlightClass">
								<c:choose>
									<c:when test="${sessionDto.totalPercentage > 95}">bg-success</c:when>
									<c:when test="${sessionDto.totalPercentage < 40}">bg-danger text-white</c:when>
									<c:when test="${sessionDto.totalPercentage < 75}">bg-warning</c:when>
								</c:choose>
							</c:set>
							
							<td class="text-center ${highlightClass}">
								${sessionDto.mark}
							</td>
							
							<td class="text-center ${highlightClass}">
								<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${sessionDto.totalPercentage}" /> %
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>      
										
			<c:set var="totalSum" value="0" />
			<c:set var="totalPercentSum" value="0" />
			<tr>
				<th><fmt:message key="label.total"/>&nbsp;
					<lams:Popover>
						<fmt:message key="label.total.1st.attempt.by.question"/>
					</lams:Popover>
				</th>
				   
				<c:forEach var="item" items="${items}">
					<c:set var="highlightClass">
						<c:choose>
							<c:when test="${item.correctOnFirstAttemptPercent > 95}">bg-success</c:when>
							<c:when test="${item.correctOnFirstAttemptPercent < 40}">bg-danger text-white</c:when>
							<c:when test="${item.correctOnFirstAttemptPercent < 75}">bg-warning</c:when>
						</c:choose>
					</c:set>
					
					<td class="text-center ${highlightClass}">
						${item.correctOnFirstAttemptCount}
					</td>
					<c:set var="totalSum" value="${totalSum + item.correctOnFirstAttemptCount}" />
					<c:set var="totalPercentSum" value="${totalPercentSum + item.correctOnFirstAttemptPercent}" />
				</c:forEach>
				
				<c:set var="totalPercentAverage" value="${totalPercentSum / fn:length(items)}" />
				<c:set var="totalAverageHighlightClass">
					<c:choose>
						<c:when test="${totalPercentAverage > 95}">bg-success</c:when>
						<c:when test="${totalPercentAverage < 40}">bg-danger text-white</c:when>
						<c:when test="${totalPercentAverage < 75}">bg-warning</c:when>
					</c:choose>
				</c:set>
				<td class="text-center ${totalAverageHighlightClass}">
					<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${totalSum / fn:length(items)}" />&nbsp;
					<lams:Popover>
						<fmt:message key="label.total.1st.attempt.average"/>
					</lams:Popover>
				</td>
				<td class="text-center">-</td>
			</tr>
			<tr>
				<th><fmt:message key="label.total"/> %</th>
				<c:forEach var="item" items="${items}">
					<c:set var="highlightClass">
						<c:choose>
							<c:when test="${item.correctOnFirstAttemptPercent > 95}">bg-success</c:when>
							<c:when test="${item.correctOnFirstAttemptPercent < 40}">bg-danger text-white</c:when>
							<c:when test="${item.correctOnFirstAttemptPercent < 75}">bg-warning</c:when>
						</c:choose>
					</c:set>
					
					<td class="text-center ${highlightClass}">
						<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${item.correctOnFirstAttemptPercent}" /> %
					</td>
				</c:forEach>
				<td class="text-center">-</td>
				<td class="text-center ${totalAverageHighlightClass}">					
					<fmt:formatNumber type="number" minFractionDigits="0" maxFractionDigits="2" value="${totalPercentAverage}" />&nbsp;%&nbsp;
					<lams:Popover>
						<fmt:message key="label.total.1st.attempt.average"/>
					</lams:Popover>
				</td>
			</tr>                         
		</c:if>		
	</tbody>
</table>