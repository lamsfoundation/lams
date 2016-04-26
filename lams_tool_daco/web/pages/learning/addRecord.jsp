<%@ include file="/common/taglibs.jsp"%>
<div id="addRecordDiv">

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />
<%-- If the view is horizontal or vertical --%>
<c:set var="horizontal" value="${sessionMap.learningView=='horizontal'}" />
<%-- To display A) B) C) in answer options instead of 1) 2) 3) --%>
<c:set var="ordinal"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></c:set>
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

<div class="voffset10">
<c:if test="${daco.lockOnFinished and mode != 'teacher'}">
	<lams:Alert id="activityLocked" type="danger" close="false">
		<c:choose>
			<c:when test="${sessionMap.userFinished}">
				<fmt:message key="message.learning.activityLocked" />
			</c:when>
			<c:otherwise>
				<fmt:message key="message.learning.warnLockOnFinish" />
			</c:otherwise>
		</c:choose>
	</lams:Alert>
</c:if>

<%@ include file="/common/messages.jsp"%>
<%-- The status of the last add/edit operation. --%>
<c:if test="${recordOperationSuccess=='add'}">
	<lams:Alert id="addrecordsuccess" type="info" close="true">
		<fmt:message key="message.learning.addrecordsuccess" />
	</lams:Alert>
</c:if>
<c:if test="${recordOperationSuccess=='edit'}">
	<lams:Alert id="addrecordsuccess" type="info" close="true">
		<fmt:message key="message.learning.editrecordsuccess" />
	</lams:Alert>
</c:if>

<div class="panel">
	<c:out value="${daco.instructions}" escapeXml="false"/>
</div>
</div>

<c:if test="${not finishedLock }">

<!--  record panel  -->
<div class="panel panel-default">
	<div class="panel-heading panel-title">
		<fmt:message key="label.learning.heading.recordnumber" />
		<span id="displayedRecordNumberSpan" class="hint">
			${displayedRecordNumber}
			<c:if test="${not (empty daco.minRecords or daco.minRecords eq 0)}">
				<br />
				<fmt:message key="error.record.notenough">
					<fmt:param value="${daco.minRecords}" />
				</fmt:message>
			</c:if>
			<c:if test="${not (empty daco.maxRecords or daco.maxRecords eq 0)}">
				<br />
				<fmt:message key="error.record.toomuch">
					<fmt:param value="${daco.maxRecords}" />
				</fmt:message>
			</c:if>
		</span>
	</div>

	<!-- Form to add/edit a record -->
	<html:form action="learning/saveOrUpdateRecord" method="post" styleId="recordForm" enctype="multipart/form-data">
	
	<c:set var="fileNumber" value="0" />
	<c:set var="answerIndex" value="0" />
	
	<html:hidden property="sessionMapID" value="${sessionMapID}" />
	<html:hidden styleId="displayedRecordNumber" property="displayedRecordNumber" value="${displayedRecordNumber}" />
	
		<table cellspacing="0" class="table table-striped">
			<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
				<tr>
					<td>
					<div class="bigNumber">${questionStatus.index+1}</div>
					<c:out value="${question.description}" escapeXml="false"/>
					<c:choose>
						<%-- The content varies depending on the question type --%>
						<c:when test="${question.type==1}"><%-- Single line text --%>
							<div class="hint"><fmt:message key="label.learning.textfield.hint" /></div>		
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<c:choose>
								<%-- Textfield entry length is limited 
									depending on the maximum number of characters the teacher provided
								--%>
								<c:when test="${question.max!=null}">
									<html:text property="answer[${answerIndex}]" size="60" maxlength="${question.max}"  styleClass="form-control"/>
								</c:when>
								<c:otherwise>
									<html:text property="answer[${answerIndex}]" size="60"  styleClass="form-control"/>
								</c:otherwise>
							</c:choose>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==2}"><%-- Multi-line text --%>
							<div class="hint"><fmt:message key="label.learning.textarea.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<html:textarea property="answer[${answerIndex}]" cols="60" rows="3"  styleClass="form-control"/>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==3}"><%-- Number --%>
							<div class="hint"><fmt:message key="label.learning.number.hint" />
							<c:if test="${not empty question.digitsDecimal}">
								<br />
								<%-- An information for the learner is displayed,
									if the number he provides will be rounded to the number of places after the decimal point,
									as stated by the teacher. --%>
								<fmt:message key="label.learning.number.decimal">
									<fmt:param value="${question.digitsDecimal}" />
								</fmt:message>
							</c:if>
							</div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<html:text property="answer[${answerIndex}]" size="10"  styleClass="form-control"/>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==4}"><%-- Date can be entered in three textfields --%>
							<div class="hint"><fmt:message key="label.learning.date.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<span class="form-inline">
								<div class="form-group">
								<label><fmt:message key="label.learning.date.day" /></label>
								<html:text property="answer[${answerIndex}]" size="3"  styleClass="form-control"/>&nbsp;
								</div>
								
								<div class="form-group">
								<c:set var="answerIndex" value="${answerIndex+1}" />
								<label><fmt:message key="label.learning.date.month" /></label>
								<html:text property="answer[${answerIndex}]" size="3"  styleClass="form-control"/>&nbsp;
								</div>
								
								<div class="form-group">
								<c:set var="answerIndex" value="${answerIndex+1}" />
								<label><fmt:message key="label.learning.date.year" /></label>
								<html:text property="answer[${answerIndex}]" size="5"  styleClass="form-control"/>
								</div>
							</span>							
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==5}"><%-- File --%>
							<div class="hint"><fmt:message key="label.learning.file.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<html:file styleId="file-${fileNumber+1}" property="file[${fileNumber}]" size="50"/>
							<c:set var="fileNumber" value="${fileNumber+1}" />
						</c:when>
						<c:when test="${question.type==6}"><%-- Image --%>
							<div class="hint"><fmt:message key="label.learning.image.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<html:file styleId="file-${fileNumber+1}" property="file[${fileNumber}]" size="50" />
							<c:set var="fileNumber" value="${fileNumber+1}" />
						</c:when>
						<c:when test="${question.type==7}"><%-- Radio buttons  --%>
							<div class="hint"><fmt:message key="label.learning.radio.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
							<%-- It displays for example A) instead of 1) --%>
							${fn:substring(ordinal,status.index,status.index+1)}) 
							<html:radio property="answer[${answerIndex}]" value="${status.index+1}"><label>&nbsp;<c:out value="${answerOption.answerOption}" escapeXml="true"/></label></html:radio><br />
							</c:forEach>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==8}"><%-- Dropdown menu --%>
							<div class="hint"><fmt:message key="label.learning.dropdown.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<html:select property="answer[${answerIndex}]"  styleClass="form-control">
							<html:option value="0"><fmt:message key="label.learning.dropdown.select"/></html:option>
							<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
								<html:option value="${status.index+1}"><c:out value="${answerOption.answerOption}" escapeXml="true"/></html:option>
							</c:forEach>
							</html:select>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==9}"><%-- Checkboxes --%>
							<div class="hint"><fmt:message key="label.learning.checkbox.hint" /></div>
							<c:if test="${horizontal}">
								</td><td style="vertical-align: middle;">
							</c:if>
							<html:hidden  styleId="checkbox-${questionStatus.index+1}" property="answer[${answerIndex}]" />
							<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
							${fn:substring(ordinal,status.index,status.index+1)}) 
							<input type="checkbox" id="checkbox-${questionStatus.index+1}-${status.index+1}" value="${status.index+1}"/>
							<label><c:out value="${answerOption.answerOption}" escapeXml="true"/>
							</label><br/>
							</c:forEach>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
						<c:when test="${question.type==10}"><%-- Longitude/latitude --%>
							<div class="hint"><fmt:message key="label.learning.longlat.hint" /></div>
							<c:if test="${horizontal}">
								</td>
								<td>
							</c:if>
								<div class="form-horizontal">
									<div class="form-group  no-gutter">
									<label class="col-sm-2 control-label"><fmt:message key="label.learning.longlat.longitude" /></label>
									<div class="col-sm-2">
									<html:text property="answer[${answerIndex}]" size="10"  styleClass="form-control "/>
									</div>
									<div class="col-sm-1">
									<p class="form-control-static"><fmt:message key="label.learning.longlat.longitude.unit" /></p>
									</div>								
									</div>
									<c:set var="answerIndex" value="${answerIndex+1}" />
												
									<div class="form-group  no-gutter">
									<label class="col-sm-2 control-label"><fmt:message key="label.learning.longlat.latitude" /></label>
									<div class="col-sm-2">
									<html:text property="answer[${answerIndex}]" size="10"  styleClass="form-control"/>
									</div>
									<div class="col-sm-1">
									<p class="form-control-static"><fmt:message key="label.learning.longlat.latitude.unit" /></p>
									</div>
									</div>
								</div>
							<c:set var="answerIndex" value="${answerIndex+1}" />
						</c:when>
					</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>

		<a href="#" onclick="javascript:saveOrUpdateRecord()" class="btn btn-sm btn-default voffset5 pull-left"><i class="fa fa-plus"></i> <fmt:message key="label.learning.add" /></a>
	</html:form>
</div>
<!--  end record panel -->
</c:if>

<!-- Reflection -->
<c:if test="${sessionMap.userFinished and daco.reflectOnActivity}">
	<div class="panel panel-default">
		<div class="panel-heading panel-title">
			<fmt:message key="label.export.reflection.heading" />
		</div>
		<div class="panel-body">
			<div class="reflectionInstructions">
				<lams:out value="${daco.reflectInstructions}" escapeHtml="true" />
			</div>
			<div class="panel">
				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<em><fmt:message key="message.no.reflection.available" /></em>
					</c:when>
					<c:otherwise>
						<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
					</c:otherwise>
				</c:choose>
			</div>

			<c:if test="${mode != 'teacher'}">
				<html:button property="FinishButton" onclick="javascript:continueReflect()" styleClass="btn btn-default pull-left">
					<fmt:message key="label.common.edit" />
				</html:button>
			</c:if>
		</div>
	</div>
</c:if>
<!-- End Reflection -->

</div> <!-- End addRecordDiv -->