<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	<%-- This page modifies its content depending on the page it was included from. --%>
	<c:set var="includeMode" value="${param.includeMode}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<%-- It contains users info divided into sessions. --%>
	<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />

	<lams:css/>
	<%-- To enable the table to have maximum height.  --%>
	<style type="text/css">
		html,body {
			height: 100%;
		}
	</style>
	<link href="<html:rewrite page='/includes/css/daco.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript">
		var editRecordUrl = "<html:rewrite page='/learning/editRecord.do' />";
		var removeRecordUrl = "<html:rewrite page='/learning/removeRecord.do' />";
		var recordListLength = "${fn:length(recordList)}";
	</script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoLearning.js'/>"></script>
</lams:head>
<body class="tabpart">
	
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

	<div class="table-responsive">
	<table id="horizontalListTable" class="table table-striped table-condensed table-bordered">
		<tr>
			<th><fmt:message key="label.learning.tableheader.questions" /></th>
			<th colspan="${fn:length(recordList)}"><fmt:message key="label.learning.tableheader.records" /></th>
		</tr>
		<tr>
			<td class="fixedCellHeight" width="320px"><fmt:message key="label.learning.tableheader.recordnumber" /></td>
 			<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
				<td class="fixedCellHeight">
					<div class="bigNumber" style="float: left; margin-right: 30px;">
						${recordStatus.index+1}
					</div>
					<c:if test='${includeMode=="learning" and not finishedLock}'>
						<div >
						<%-- If the record can be edited, display these links. --%>
						<i class="fa fa-pencil pull-right roffset10" title="<fmt:message key="label.common.edit" />"
									onclick="javascript:editRecord('${sessionMapID}',${recordStatus.index+1})"></i>
						<i class="fa fa-times  pull-right roffset10" title="<fmt:message key="label.common.delete" />"
									onclick="javascript:removeRecord('${sessionMapID}',${recordStatus.index+1})"></i>
						</div>
					</c:if>
				</td>
			</c:forEach>
		</tr>
		<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">	
		<tr>
			<td class="fixedCellHeight" style="width: 160px">
				<div class="bigNumber">${questionStatus.index+1}</div>
				<c:out value="${question.description}" escapeXml="false"/>
			</td>	
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
											document.getElementById("maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}").innerHTML=''+${mapLinks}'';
										</script>
										</c:if>
									<script type="text/javascript">
										setValue("latitude-record${recordStatus.index+1}-question${questionStatus.index+1}","${answer.answer}");
									</script>
								</c:if>
							</c:when>
							<c:otherwise>
								<c:set var="generated" value="true" />
										<td class="fixedCellHeight"
												<c:choose>
												<%-- Iframes are displayed differently in some browsers, this code fixes it.
													The last cell in a column is shorter to accomodate the horizontal scrollbar.
												 --%>
													<c:when test="${isIE}">
														<c:if test="${questionStatus.index==fn:length(daco.dacoQuestions)-1 && fn:length(recordList)>1}">
																id="lastHorizontalQuestion"
														</c:if>
													</c:when>
													<c:otherwise>
														<c:if test="${questionStatus.index==fn:length(daco.dacoQuestions)-1}">
																<c:choose>
																	<c:when test="${fn:length(recordList)>1}">
																		style="height: 69px;"
																	</c:when>
																	<c:otherwise>
																		style="height: 86px"
																	</c:otherwise>
																</c:choose>
															</c:if>
													</c:otherwise>
												</c:choose>
										 >
										<c:choose>
											<c:when test="${question.type==1}">
												<input type="text" size="45" readonly="readonly" value="<c:out  value='${answer.answer}'/>" />
											</c:when>
											<c:when test="${question.type==2}">
												<textarea cols="35" 
													<c:choose>
														<c:when test="${isIE}">
															rows="3"
														</c:when>
														<c:otherwise>
															rows="2"
														</c:otherwise>
													</c:choose>
												 readonly="readonly">${answer.answer}</textarea>
											</c:when>
											<c:when test="${question.type==3}">
												<input type="text" size="10" readonly="readonly" value="<c:out  value='${answer.answer}'/>"/>
											</c:when>
 											<c:when test="${question.type==4}">
<%-- 												<c:set var="date">
													<c:if test="${not empty answer.answer}">
														<lams:Date value="${fn:trim(answer.answer)}" type="date" style="medium"/>
													</c:if>
												</c:set>
 --%>												<input type="text" size="20" readonly="readonly" value="${date}" />
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
												<table class="alternative-color-inner-table">
													<tr>
														<td>
															<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
																<c:if test="${status.index>0 && (status.index%3==0)}">
																	</td>
																	<td>
																</c:if>
																<input type="radio" readonly="readonly" 
																<c:if test="${answer.answer==status.index+1}">
																checked="checked"
																</c:if>
																>
																<c:out value="${answerOption.answerOption}" escapeXml="true"/></input><br />
															</c:forEach>
														</td>
													</tr>
												</table>
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
												<table class="alternative-color-inner-table">
													<tr>
														<td>
															<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
																<c:if test="${status.index>0 && (status.index%3==0)}">
																	</td>
																	<td>
																</c:if>
																<input type="checkbox" disabled="disabled" id="checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}"><c:out value="${answerOption.answerOption}" escapeXml="true"/></input><br />
															</c:forEach>
														</td>
													</tr>
												</table>
											</c:when>
											<c:when test="${question.type==10}">
												<table class="alternative-color-inner-table">
													<tr>
														<td width="80px">
														<label><fmt:message key="label.learning.longlat.longitude" /></label>
														</td>
														<td>
															<c:set var="longitude" value="<c:out  value='${answer.answer}'/>" />
															<input type="text" size="10" readonly="readonly" value="<c:out  value='${answer.answer}'/>"/>
															<label><fmt:message key="label.learning.longlat.longitude.unit" /></label><br />
														</td>
														<c:if test="${not empty question.answerOptions and not empty longitude}">
															<td rowspan="2" id="maplinks-record${recordStatus.index+1}-question${questionStatus.index+1}">
															</td>
														</c:if>										
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
			</tr>
		</c:forEach>
	</table>
	</div>
</body>
</lams:html>
