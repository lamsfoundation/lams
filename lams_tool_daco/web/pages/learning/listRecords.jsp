<%@ include file="/common/taglibs.jsp"%>
<%@page import="org.lamsfoundation.lams.web.util.SessionMap"%>
<%-- This page modifies its content depending on the page it was included from. --%>
<c:if test="${not empty param.includeMode}"><c:set var="includeMode" value="${param.includeMode}" /></c:if>
<c:if test="${empty includeMode}"><c:set var="includeMode" value="learning" /></c:if>
<c:if test="${not empty param.sessionMapID}"><c:set var="sessionMapID" value="${param.sessionMapID}" /></c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />
<c:set var="tool"><lams:WebAppURL /></c:set>
<%-- Whether to display the vertical or horizontal view. --%>
<c:set var="horizontal" value="${sessionMap.learningView=='horizontal'}" />
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
<c:if test="${empty recordList}">
	<%-- In some cases record list is passed as an attribute, in other - in session map. --%>
	<c:set var="recordList" value="${sessionMap.recordList}" />
</c:if>

<script type="text/javascript">
	var editRecordUrl = "<c:url value='/learning/editRecord.do' />";
	var removeRecordUrl = "<c:url value='/learning/removeRecord.do' />";
</script>

<div id="recordListDiv">
	<c:choose>
		<c:when test="${ empty recordList}">
			<lams:Alert5 type="info">
				<fmt:message key="label.learning.heading.norecords" />
			</lams:Alert5>
		</c:when>
		
		<c:otherwise>
			<lams:Alert5 type="success">
				<fmt:message key="label.learning.heading.recordcount" />: ${fn:length(recordList)}
			</lams:Alert5>
	
			<c:choose>
				<c:when test="${horizontal}">
					<div class="row">
						<div class="col-2" style="padding:0">
							<strong><fmt:message key="label.learning.tableheader.questions" /></strong>
						</div>
							
						<div class="col-9" style="padding-left:0">
							<strong><fmt:message key="label.learning.tableheader.records" /></strong>					
						</div>
					</div>
	
					<div class="row">
						<div class="col-2 p-0">
							<div id="recordListTable" class="ltable table-striped table-bordered table-sm no-header">
								<div class="row border">
									<div class="pe-0" style="height:50px;">
										<fmt:message key="label.learning.tableheader.recordnumber" />
									</div>
								</div>
								<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
									<div class="row border">
										<div class="fixedCellHeight">
											<!-- <div class="bigNumber">${questionStatus.index+1}</div> -->
											<c:out value="${question.description}" escapeXml="false"/>
										</div>	
									</div>
								</c:forEach>
							</div>
						</div>
							
						<%-- Link that displayes the horizontal record list --%>
						<c:url var="showRecordsUrl" value='/learning/diplayHorizontalRecordList.do'>
							<c:param name="sessionMapID" value="${sessionMapID}" />
							<c:if test="${includeMode=='monitoring'}">
								<c:param name="userId" value="${user.userId}" />
							</c:if>
							<c:param name="includeMode" value="${includeMode}" />
						</c:url>
	
						<div class="col-10 ps-0" id="${elementIdPrefix}horizontalRecordListFrame"></div>
						<script type="text/javascript">
							$("#${elementIdPrefix}horizontalRecordListFrame").load("${showRecordsUrl}");
						</script>
					</div>
				</c:when>
				
				<c:otherwise>
					<%-- Vertical view displays records as separate tables of answers. --%>
					<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
					<!--  record panel  -->
					<div class="card lcard">
						<div class="card-header">
							<fmt:message key="label.learning.heading.recordnumber" />&nbsp;${recordStatus.index+1}
							
							<%-- If the record can be edited, display these links. --%>
							<c:if test='${includeMode=="learning" and not finishedLock and (mode != "teacher")}'>
								<div class="float-end">
									<button type="button" class="no-decoration fa fa-trash text-bg-danger shadow rounded p-2"
									   title="<fmt:message key="label.common.delete" />"
									   aria-label="<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.common.delete" /></spring:escapeBody>"
									   onclick="javascript:removeRecord('${sessionMapID}',${recordStatus.index+1})">
									 </button>
									 
									<button type="button" class="no-decoration fa fa-pencil text-bg-light shadow rounded p-2 ms-2"
									   title="<fmt:message key="label.common.edit" />"
									   aria-label="<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.common.edit" /></spring:escapeBody>"
									   onclick="javascript:editRecord('${sessionMapID}',${recordStatus.index+1})">
									 </button>
								 </div>
							</c:if>
						</div>
						
						<div class="ltable table-striped table-sm no-header">
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
																	'<button type="button" class="btn btn-secondary" onclick="javascript:launchPopup('+"'http://maps.google.com/maps?q=${longitude},+${answer.answer}&iwloc=A&hl=en','LAMS');"+'">${selectedMap.answerOption}</button>'+
																</c:when>
																<c:when test="${selectedMap.answerOption=='Geabios'}">
																	'<button type="button" class="btn btn-secondary" onclick="javascript:launchPopup('+"'http://www.geabios.com/html/services/maps/PublicMap.htm?lat=${answer.answer}&lon=${longitude}&fov=0.3&title=LAMS','LAMS'); "+'">${selectedMap.answerOption}</button>'+
																</c:when>
																<c:when test="${selectedMap.answerOption=='Open Street Map'}">
																	'<button type="button" class="btn btn-secondary" onclick="javascript:launchPopup('+"'http://www.openstreetmap.org/index.html?lat=${answer.answer}&lon=${longitude}&zoom=11','LAMS'); "+'">${selectedMap.answerOption}</button>'+
																</c:when>
																<c:when test="${selectedMap.answerOption=='Multimap'}">
																	'<button type="button" class="btn btn-secondary" class="btn btn-secondary" onclick="javascript:launchPopup('+"'http://www.multimap.com/map/browse.cgi?scale=200000&lon=${longitude}&lat=${answer.answer}&icon=x','LAMS'); "+'">${selectedMap.answerOption}</button>'+
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
													document.getElementById("${elementIdPrefix}latitude-record${recordStatus.index+1}-question${questionStatus.index+1}").innerHTML="${answer.answer}";
												</script>
											</c:if>
										</c:when>
										
										<c:otherwise>
											<c:set var="generated" value="true" />
											<div class="row py-3">
												<!-- <div class="bigNumber">${questionStatus.index+1}</div> -->
												<c:out value="${question.description}" escapeXml="false"/>
												
												<div>
												<c:choose>
													<c:when test="${question.type==1 || question.type==2 || question.type==3 || question.type==4}">
														<c:out  value='${answer.answer}'/>
													</c:when>
													
													<c:when test="${question.type==5 || question.type==6}">
														<c:choose>
															<c:when test="${empty answer.fileName}">
																<fmt:message key="label.learning.file.notuploaded" />
															</c:when>
															<c:otherwise>
																<fmt:message key="label.learning.file.uploaded" />&nbsp;
																<a class="link-primary bg-body" href="<c:url value='/download/?uuid=${answer.fileDisplayUuid}&preferDownload=true'/>">${answer.fileName}</a>
															</c:otherwise>
														</c:choose>
													</c:when>
													
													<c:when test="${question.type==7}">
														<div>
															<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
																<div class="form-check">
																	<input type="radio" readonly="readonly" disabled id="answerOption-${questionStatus.index}-${status.index}-${recordStatus.index}" class="form-check-input"
																			<c:if test="${answer.answer==status.index+1}">
																				checked="checked"
																			</c:if>
																	>
																	<label for="answerOption-${questionStatus.index}-${status.index}-${recordStatus.index}" class="form-check-label">
																		<c:out value="${answerOption.answerOption}" escapeXml="true"/>
																	</label>
																</div>
															</c:forEach>
														</div>
													</c:when>
													
													<c:when test="${question.type==8}">
														<c:choose>
															<c:when test="${empty answer.answer}">
																<fmt:message key="label.learning.dropdown.noneselected" />
															</c:when>
															<c:otherwise>
																<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
																	<c:if test="${status.index+1==answer.answer}">
																		<fmt:message key="label.learning.dropdown.selected" />&nbsp;<c:out value="${answerOption.answerOption}" escapeXml="true"/>
																	</c:if>
																</c:forEach>
															</c:otherwise>
														</c:choose>
													</c:when>
													
													<c:when test="${question.type==9}">
														<div>
															<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
																<div class="form-check">
																	<input type="checkbox" disabled="disabled" class="form-check-input" 
																			id="${elementIdPrefix}checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}">
																	<label for="${elementIdPrefix}checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}" class="form-check-label">
																		<c:out value="${answerOption.answerOption}" escapeXml="true"/>
																	</label>
																</div>
															</c:forEach>
														</div>
													</c:when>
													
													<c:when test="${question.type==10}">
														<div>
															<div>
																<fmt:message key="label.learning.longlat.longitude" />&nbsp;
																<c:set var="longitude"><c:out  value='${answer.answer}'/></c:set>
																${longitude}&nbsp;
																<fmt:message key="label.learning.longlat.longitude.unit" />
															</div>
															
															<div>
																<fmt:message key="label.learning.longlat.latitude" />&nbsp;
																<span id="${elementIdPrefix}latitude-record${recordStatus.index+1}-question${questionStatus.index+1}"></span>&nbsp;
																<fmt:message key="label.learning.longlat.latitude.unit" />	
																	
																<c:if test="${not empty question.answerOptions and not empty longitude}">
																	<%-- A placeholder for external maps links --%>
																	<div id="${elementIdPrefix}maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}" class="float-end">
																	</div>
																</c:if>	
															</div>
														</div>
													</c:when>
												</c:choose>
												</div>
											</div>
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
						</div>
					</div> <!--  end record panel -->		
					</c:forEach>
				</c:otherwise>
			</c:choose> <!--  end vertical/horizontal -->
		</c:otherwise> 
	</c:choose>
</div>
