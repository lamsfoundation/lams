<div id="recordListDiv">

<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.learningMode}">
	<c:set var="learningMode" value="${param.learningMode}" />
</c:if>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="horizontal" value="${sessionMap.learningView=='horizontal'}" />
<c:set var="userAgent" value='<%=request.getHeader("User-Agent") %>' />
<c:set var="isIE" value='${(not empty userAgent) && fn:indexOf(userAgent,"MSIE") != -1}' />
<script type="text/javascript">
	var editRecordUrl = "<html:rewrite page='/learning/editRecord.do' />";
	var removeRecordUrl = "<html:rewrite page='/learning/removeRecord.do' />";
</script>
<c:if test="${empty recordList}">
	<c:set var="recordList" value="${sessionMap.recordList}" />
</c:if>
<c:if test="${learningMode}">
	<table>
		<tr>
			<td>
			<h1>${daco.title}</h1>
			</td>
		</tr>
		<tr>
			<td>${daco.instructions}</td>
		</tr>
	</table>
</c:if>
<c:choose>
	<c:when test="${ empty recordList}">
		<p class="hint">
			 <fmt:message key="label.learning.heading.norecords" />
		</p>
	</c:when>
	<c:otherwise>
			<c:if test="${learningMode}">
				<p class="hint" style="margin-left: 17px; font-weight: bold;">
					<fmt:message key="label.learning.heading.recordcount" />: ${fn:length(recordList)}
				</p>
			</c:if>

		<c:choose>
			<c:when test="${horizontal}">
				<table cellspacing="0" class="alternative-color" id="recordListTable">
					<tr>
						<th><fmt:message key="label.learning.tableheader.questions" /></th>
						<th><fmt:message key="label.learning.tableheader.records" /></th>
					</tr>
					<tr>
					<td class="fixedCellHeight" style="width: 160px"><fmt:message key="label.learning.tableheader.recordnumber" /></td>
					<td rowspan="${fn:length(daco.dacoQuestions) + 2}" style="padding: 0px; height: 100%;">
						<iframe id="horizontalRecordListFrame" onLoad="javascript:resizeHorizontalRecordListFrame();" style="width: 100%;"
						frameborder="0" scrolling="auto"
						src="<html:rewrite page='/learning/diplayHorizontalRecordList.do?sessionMapID=${sessionMapID}&isIE=${isIE}' />"></iframe>
					</td>
					
					</tr>
						<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
							<tr>
								<td class="fixedCellHeight" style="width: 160px">
									<div class="bigNumber">${questionStatus.index+1}</div>
									${question.description}
								</td>	
							</tr>
						</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
				<table>
					<tr>
						<td class="hint">
							<fmt:message key="label.learning.heading.recordnumber" /> ${recordStatus.index+1}
						</td>
						<c:if test="${learningMode}">
						<td width="5%">
							<img src="${tool}includes/images/edit.gif"
								title="<fmt:message key="label.authoring.basic.edit" />"
								onclick="javascript:editRecord('${sessionMapID}',${recordStatus.index+1})" />
						</td>
						<td width="5%">
							<img src="${tool}includes/images/cross.gif"
								title="<fmt:message key="label.authoring.basic.delete" />"
								onclick="javascript:removeRecord('${sessionMapID}',${recordStatus.index+1})" />
						</td>
						</c:if>
					</tr>
				</table>
				
					<table cellspacing="0" class="alternative-color recordList">
						<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
							<c:set var="generated" value="false" />
							<c:forEach var="answer" items="${record}" varStatus="answerStatus">
								<c:if test="${answer.question.uid==question.uid}">
									<c:choose>
									<c:when test="${generated}">
										<c:if test="${question.type==10}">
										<script type="text/javascript">
											setValue("latitude-record${recordStatus.index+1}-question${questionStatus.index+1}","${answer.answer}");
										</script>
										</c:if>
									</c:when>
									<c:otherwise>
										<c:set var="generated" value="true" />
										<tr>
											<td>
											<div class="bigNumber">${questionStatus.index+1}</div>
											${question.description}
											<c:choose>
												<c:when test="${question.type==1}">
													<input type="text" size="72" readonly="readonly" value="${answer.answer}"/>
												</c:when>
												<c:when test="${question.type==2}">
													<textarea  cols="55" rows="3" readonly="readonly">${answer.answer}</textarea>
												</c:when>
												<c:when test="${question.type==3}">
													<input type="text" size="10" readonly="readonly" value="${answer.answer}"/>
												</c:when>
												<c:when test="${question.type==4}">
													<c:set var="date">
														<lams:Date value="${fn:trim(answer.answer)}" type="date" style="medium"/>
													</c:set>
													<input type="text" size="20" readonly="readonly" value="${date}" />
												</c:when>
												<c:when test="${question.type==5 || question.type==6}">
													<c:choose>
														<c:when test="${empty answer.fileName}">
															<fmt:message key="label.learning.file.notuploaded" />
														</c:when>
														<c:otherwise>
															<fmt:message key="label.learning.file.uploaded" /> 
															<a href="<c:url value='/download/?uuid=${answer.fileUuid}&preferDownload=true'/>">${answer.fileName}</a>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${question.type==7}">
													<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
														<input type="radio" readonly="readonly" 
														<c:if test="${answer.answer==status.index+1}">
														checked="checked"
														</c:if>
														>
														${answerOption.answerOption}</input><br />
													</c:forEach>
												</c:when>
												<c:when test="${question.type==8}">
													<c:choose>
														<c:when test="${empty answer.answer}">
															<fmt:message key="label.learning.dropdown.noneselected" />
														</c:when>
														<c:otherwise>
															<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
																<c:if test="${status.index+1==answer.answer}">
																	<fmt:message key="label.learning.dropdown.selected" /> ${answerOption.answerOption}
																</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${question.type==9}">
													<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
														<input type="checkbox" disabled="disabled" id="checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}">${answerOption.answerOption}</input><br />
													</c:forEach>
												</c:when>
												<c:when test="${question.type==10}">
													<table class="alternative-color-inner-table">
														<tr>
															<td width="80px">
															<label><fmt:message key="label.learning.longlat.longitude" /></label>
															</td>
															<td>
																<input type="text" size="10" readonly="readonly" value="${answer.answer}"/>
															<label><fmt:message key="label.learning.longlat.longitude.unit" /></label><br />
															</td>									
														</tr>
														<tr>
															<td>
															<label><fmt:message key="label.learning.longlat.latitude" /></label>
															</td>
															<td>
															<input type="text" size="10" readonly="readonly" id="latitude-record${recordStatus.index+1}-question${questionStatus.index+1}" />
															<label><fmt:message key="label.learning.longlat.latitude.unit" /></label><br />
															</td>
														</tr>
													</table>
												</c:when>
											</c:choose>
											</td>
										</tr>
									</c:otherwise>
									</c:choose>
									<c:if test="${question.type==9 && (not empty answer.answer)}">
										<script type="text/javascript">
											checkCheckbox("checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${answer.answer}");
										</script>
									</c:if>
								</c:if>
							</c:forEach>
						</c:forEach>
					</table>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
</div>