<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />
<%-- This page modifies its content depending on the page it was included from. --%>
<c:set var="includeMode" value="${param.includeMode}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<%-- It contains users info divided into sessions. --%>
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:choose>
	<%-- Record list comes from different sources, depending on the including page. --%>
	<c:when test="${includeMode=='learning'}">
		<c:set var="recordList" value="${sessionMap.recordList}" />
	</c:when>
	<c:otherwise>
		<c:forEach var="user" items="${monitoringSummary.users}">
			<c:if test="${param.userId==user.userId}">
				<c:set var="recordList" value="${user.records}" />
			</c:if>
		</c:forEach>
	</c:otherwise>
</c:choose>

<%-- To enable the table to have maximum height.  --%>
<style type="text/css">
	html,body {
		height: 100%;
	}
</style>
<link href="<lams:WebAppURL/>includes/css/daco.css" rel="stylesheet" type="text/css">
	
<lams:JSImport src="includes/javascript/common.js" />
<lams:JSImport src="includes/javascript/dacoCommon.js" relative="true" />
<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.tabcontroller.js"></script>    
<script type="text/javascript">
	var editRecordUrl = "<lams:WebAppURL/>earning/editRecord.do";
	var removeRecordUrl = "<lams:WebAppURL/>learning/removeRecord.do";
	var recordListLength = "${fn:length(recordList)}";
</script>
<lams:JSImport src="includes/javascript/dacoLearning.js" relative="true" />

<div id="horizontalListTable" class="ltable table-striped table-sm no-header">
	<div class="row border">
 		<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
			<div class="col border-end" style="height:50px">
				<span class="bigNumber">
					${recordStatus.index+1}
				</span>

				<c:if test='${includeMode=="learning" and not finishedLock and (sessionMap.mode != "teacher")}'>
					<span class="float-end me-2">
						<%-- If the record can be edited, display these links. --%>
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
					</span>
				</c:if>
			</div>
		</c:forEach>
	</div>
		
	<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">	
		<div class="row border">
			<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
			<%-- For comments on the structure, see "learning/listRecords.jsp" --%>
				<c:set var="generated" value="false" />
				<c:forEach var="answer" items="${record}" varStatus="answerStatus">
					<c:if test="${answer.question.uid==question.uid}">
						<c:choose>
							<c:when test="${generated}">
								<c:if test="${question.type==10}">
									<c:if test="${not empty question.answerOptions and not empty longitude}">
										<c:set var="mapLinks">
											<c:forEach var="selectedMap" items="${question.answerOptions}">
												<c:choose>
													<c:when test="${selectedMap.answerOption=='Google Maps'}">
														'<button type="button" class="btn btn-light" onclick="javascript:launchPopup('+"'http://maps.google.com/maps?q=${longitude},+${answer.answer}&iwloc=A&hl=en','LAMS');"+'">${selectedMap.answerOption}</button> '+
													</c:when>
													<c:when test="${selectedMap.answerOption=='Geabios'}">
														<button type="button" class="btn btn-light" onclick="javascript:launchPopup('+"'http://www.geabios.com/html/services/maps/PublicMap.htm?lat=${answer.answer}&lon=${longitude}&fov=0.3&title=LAMS','LAMS'); "+'">${selectedMap.answerOption}</button> '+
													</c:when>
													<c:when test="${selectedMap.answerOption=='Open Street Map'}">
														'<button type="button" class="btn btn-light" onclick="javascript:launchPopup('+"'http://www.openstreetmap.org/index.html?lat=${answer.answer}&lon=${longitude}&zoom=11','LAMS'); "+'">${selectedMap.answerOption}</button> '+
													</c:when>
													<c:when test="${selectedMap.answerOption=='Multimap'}">
														'<button type="button" class="btn btn-light" onclick="javascript:launchPopup('+"'http://www.multimap.com/map/browse.cgi?scale=200000&lon=${longitude}&lat=${answer.answer}&icon=x','LAMS'); "+'">${selectedMap.answerOption}</button> '+
													</c:when>
												</c:choose>
											</c:forEach>
										</c:set>
										<script type="text/javascript">
											document.getElementById("maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}").innerHTML=''+${mapLinks}'';
										</script>
										</c:if>
									<script type="text/javascript">
										document.getElementById("latitude-record${recordStatus.index+1}-question${questionStatus.index+1}").innerHTML="${answer.answer}";
									</script>
								</c:if>
							</c:when>
							
							<c:otherwise>
								<c:set var="generated" value="true" />
								<div class="col fixedCellHeight ${questionStatus.first ? 'border-thick ' : ''} ${recordStatus.last ? '' : 'border-end '}">
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
														<fmt:message key="label.learning.file.uploaded" />
														<a class="link-primary bg-body" href="<c:url value='/download/?uuid=${answer.fileDisplayUuid}&preferDownload=true'/>">&nbsp;${answer.fileName}</a>
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
															<input type="checkbox" disabled="disabled" id="checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}" class="form-check-input">
															
															<label for="checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}" class="form-check-label">
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
														<span id="latitude-record${recordStatus.index+1}-question${questionStatus.index+1}"></span>&nbsp;
														<fmt:message key="label.learning.longlat.latitude.unit" />
														
														<c:if test="${not empty question.answerOptions and not empty longitude}">
															<div id="maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}" class="float-end"></div>
														</c:if>	
													</div>
												</div>							
											</c:when>
										</c:choose>
								</div>
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
		</div>
	</c:forEach>
</div>
