<%@page import="org.lamsfoundation.lams.web.util.SessionMap"%>
<div id="recordListDiv">

<%@ include file="/common/taglibs.jsp"%>
<%-- This page modifies its content depending on the page it was included from. --%>
<c:if test="${not empty param.includeMode}">
	<c:set var="includeMode" value="${param.includeMode}" />
</c:if>
<c:if test="${empty includeMode}">
	<c:set var="includeMode" value="learning" />
</c:if>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<%-- Wheter to display the vertical or horizontal view. --%>
<c:set var="horizontal" value="${sessionMap.learningView=='horizontal'}" />
<c:if test="${empty isIE}">
	<%-- Some elements are displayed differently depending on the browser. --%>
	<c:set var="userAgent" value='<%=request.getHeader("User-Agent") %>' />
	<c:set var="isIE" value='${(not empty userAgent) && fn:indexOf(userAgent,"MSIE") != -1}' scope="session" />
</c:if>
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:choose>
	<%-- This page can be included multiple times on a single master page. That is why element IDs need prefixes. --%>
	<c:when test="${includeMode=='learning'}">
		<c:set var="elementIdPrefix" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="elementIdPrefix" value="user${user.uid}-" />
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	var editRecordUrl = "<c:url value='/learning/editRecord.do' />";
	var removeRecordUrl = "<c:url value='/learning/removeRecord.do' />";
</script>
<c:if test="${empty recordList}">
	<%-- In some cases record list is passed as an attribute, in other - in session map. --%>
	<c:set var="recordList" value="${sessionMap.recordList}" />
</c:if>

<c:if test='${includeMode=="learning"}'>
	<table>
		<tr>
			<td>
			<h1><c:out value="${daco.title}" escapeXml="true"/></h1>
			</td>
		</tr>
		<tr>
			<td><c:out value="${daco.instructions}" escapeXml="false"/></td>
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
		<c:choose>
			<%-- Indenting is different depending on the page this page is included to. --%>
			<c:when test='${includeMode=="learning"}'>
				<p class="hint" style="margin-left: 17px; font-weight: bold;">
					<fmt:message key="label.learning.heading.recordcount" />: ${fn:length(recordList)}
				</p>
			</c:when>
			<c:when test='${includeMode=="exportportfolio"}'>
				<p class="hint" style="font-weight: bold;">
					<fmt:message key="label.learning.heading.recordcount" />: ${fn:length(recordList)}
				</p>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${horizontal}">
				<%-- Horizontal view creates a table with question list and one large cell where iframe with records is displayed --%>
				<table cellspacing="0" class="alternative-color" id="recordListTable">
					<tr>
						<th><fmt:message key="label.learning.tableheader.questions" /></th>
						<th><fmt:message key="label.learning.tableheader.records" /></th>
					</tr>
					<tr>
					<td class="fixedCellHeight" style="width: 160px"><fmt:message key="label.learning.tableheader.recordnumber" /></td>
					<td rowspan="${fn:length(daco.dacoQuestions) + 2}" style="padding: 0px; height: 100%;">
						<%-- Link that displayes the horizontal record list --%>
						<c:url var="showRecordsUrl" value='/learning/diplayHorizontalRecordList.do'>
							<c:param name="sessionMapID" value="${sessionMapID}" />
							<c:if test="${includeMode=='monitoring'}">
								<c:param name="userId" value="${user.userId}" />
							</c:if>
							<c:param name="includeMode" value="${includeMode}" />
						</c:url>
						<iframe id="${elementIdPrefix}horizontalRecordListFrame"
								onLoad="javascript:resizeHorizontalRecordListFrame('${elementIdPrefix}',${fn:length(daco.dacoQuestions)});"
								style="width: 100%;" frameborder="0" scrolling="auto" src="${showRecordsUrl}"></iframe>
					</td>
					
					</tr>
						<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
							<tr>
								<td class="fixedCellHeight" style="width: 160px">
									<div class="bigNumber">${questionStatus.index+1}</div>
									<c:out value="${question.description}" escapeXml="false"/>
								</td>	
							</tr>
						</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<%-- Vertical view displays records as separate tables of answers. --%>
				<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
				<table style="width: 97%">
					<tr>
						<td class="hint">
							<fmt:message key="label.learning.heading.recordnumber" /> ${recordStatus.index+1}
						</td>
						<c:if test='${includeMode=="learning" and not finishedLock}'>
						<%-- If the record can be edited, display these links. --%>
						<td width="5%">
							<img src="${tool}includes/images/edit.gif"
								title="<fmt:message key="label.common.edit" />"
								onclick="javascript:editRecord('${sessionMapID}',${recordStatus.index+1})" />
						</td>
						<td width="5%">
							<img src="${tool}includes/images/cross.gif"
								title="<fmt:message key="label.common.delete" />"
								onclick="javascript:removeRecord('${sessionMapID}',${recordStatus.index+1})" />
						</td>
						</c:if>
					</tr>
				</table>
				
					<table cellspacing="0" class="alternative-color recordList">
						<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
							<%-- "Generated" means that the table for a long/lat question was already generated
								 and the current answer only needs to be filled in in the existing textfield.
							--%>
							<c:set var="generated" value="false" />
							<c:forEach var="answer" items="${record}" varStatus="answerStatus">
								<c:if test="${answer.question.uid==question.uid}">
									<c:choose>
									<c:when test="${generated}">
										<c:if test="${question.type==10}">
											<c:if test="${not empty question.answerOptions and not empty longitude}">
												<c:set var="mapLinks">
													<c:forEach var="selectedMap" items="${question.answerOptions}">
														<%-- Display the links for external maps,
														where the longitude and latitude can be marked and shown.
														Strange syntax of this var (mapLinks) comes from later usage in a javascript.--%>
														<c:choose>
															<c:when test="${selectedMap.answerOption=='Google Maps'}">
																'<a onclick="javascript:launchPopup('+"'http://maps.google.com/maps?q=${longitude},+${answer.answer}&iwloc=A&hl=en','LAMS');"+'" href="#">${selectedMap.answerOption}</a><br />'+
															</c:when>
															<c:when test="${selectedMap.answerOption=='Geabios'}">
																'<a onclick="javascript:launchPopup('+"'http://www.geabios.com/html/services/maps/PublicMap.htm?lat=${answer.answer}&lon=${longitude}&fov=0.3&title=LAMS','LAMS'); "+'" href="#">${selectedMap.answerOption}</a><br />'+
															</c:when>
															<c:when test="${selectedMap.answerOption=='Open Street Map'}">
																'<a onclick="javascript:launchPopup('+"'http://www.openstreetmap.org/index.html?lat=${answer.answer}&lon=${longitude}&zoom=11','LAMS'); "+'" href="#">${selectedMap.answerOption}</a><br />'+
															</c:when>
															<c:when test="${selectedMap.answerOption=='Multimap'}">
																'<a onclick="javascript:launchPopup('+"'http://www.multimap.com/map/browse.cgi?scale=200000&lon=${longitude}&lat=${answer.answer}&icon=x','LAMS'); "+'" href="#">${selectedMap.answerOption}</a><br />'+
															</c:when>
														</c:choose>
													</c:forEach>
												</c:set>
												<script type="text/javascript">
													<%-- Fill the existing cell in long/lat question table with created links. --%>
													document.getElementById("${elementIdPrefix}maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}").innerHTML=''+${mapLinks}'';
												</script>
											</c:if>
											<script type="text/javascript">
												<%-- Set the value of latitude in long/lat question according to the answer. --%>
												setValue("${elementIdPrefix}latitude-record${recordStatus.index+1}-question${questionStatus.index+1}","${answer.answer}");
											</script>
										</c:if>
									</c:when>
									<c:otherwise>
										<c:set var="generated" value="true" />
										<tr>
											<td>
											<div class="bigNumber">${questionStatus.index+1}</div>
											<c:out value="${question.description}" escapeXml="false"/>
											<c:choose>
												<c:when test="${question.type==1}">
													<input type="text" size="72" readonly="readonly" value="<c:out  value='${answer.answer}'/>"/>
												</c:when>
												<c:when test="${question.type==2}">
													<textarea  cols="55" 
														<c:choose>
															<c:when test="${isIE}">															
																rows="3"
															</c:when>
															<c:otherwise>
																<%-- In Firefox 2 rows are displayed as 3 rows (!) --%>
																rows="2"
															</c:otherwise>
														</c:choose>
													 readonly="readonly">${answer.answer}</textarea>
												</c:when>
												<c:when test="${question.type==3}">
													<input type="text" size="10" readonly="readonly" value="<c:out  value='${answer.answer}'/>"/>
												</c:when>
												<c:when test="${question.type==4}">
													<c:set var="date">
														<c:if test="${not empty answer.answer}">
															<%-- To display a date a single textfield with formatted date is used. --%>
															<lams:Date value="${fn:trim(answer.answer)}" type="date" style="medium"/>
														</c:if>
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
															<c:choose>
																<%-- The file can be downloaded back from the server.
																	It is done differently in learning and export portfolio modes. --%>
																<c:when test="${includeMode=='exportportfolio'}">
																	<a href="<c:url value='files/${answer.fileUuid}-${answer.fileName}'/>">${answer.fileName}</a>
																</c:when>
																<c:otherwise>
																	<a href="<c:url value='/download/?uuid=${answer.fileUuid}&preferDownload=true'/>">${answer.fileName}</a>
																</c:otherwise>
															</c:choose>				
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
														<c:out value="${answerOption.answerOption}" escapeXml="true"/></input><br />
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
																	<fmt:message key="label.learning.dropdown.selected" /> <c:out value="${answerOption.answerOption}" escapeXml="true"/>
																</c:if>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:when test="${question.type==9}">
													<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
														<input type="checkbox" disabled="disabled" id="${elementIdPrefix}checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}"><c:out value="${answerOption.answerOption}" escapeXml="true"/></input><br />
													</c:forEach>
												</c:when>
												<c:when test="${question.type==10}">
													<table class="alternative-color-inner-table">
														<tr>
															<td width="80px">
																<label><fmt:message key="label.learning.longlat.longitude" /></label>
															</td>
															<td>
																<c:set var="longitude"><c:out  value='${answer.answer}'/></c:set>
																<input type="text" size="10" readonly="readonly" value="${longitude}"/>
																<label><fmt:message key="label.learning.longlat.longitude.unit" /></label><br />
															</td>
															<c:if test="${not empty question.answerOptions and not empty longitude}">
																<%-- A placeholder for external maps links --%>
																<td rowspan="2" id="${elementIdPrefix}maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}">
																</td>
															</c:if>						
														</tr>
														<tr>
															<td>
																<label><fmt:message key="label.learning.longlat.latitude" /></label>
															</td>
															<td>
																<input type="text" size="10" readonly="readonly" id="${elementIdPrefix}latitude-record${recordStatus.index+1}-question${questionStatus.index+1}" />
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
										<%-- Each answer contains one piece of information: whether the checkbox is checked or not.
											Since the whole table is generated for the first answer,
											other answers simply check the existing boxes. --%>
										<script type="text/javascript">
											checkCheckbox("${elementIdPrefix}checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${answer.answer}");
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
