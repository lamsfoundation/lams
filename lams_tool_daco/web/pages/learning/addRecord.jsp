<%@ include file="/common/taglibs.jsp"%>
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

<div id="addRecordDiv">
	<c:if test="${daco.lockOnFinished and mode != 'teacher'}">
		<lams:Alert5 id="activityLocked" type="danger" close="false">
			<c:choose>
				<c:when test="${sessionMap.userFinished}">
					<fmt:message key="message.learning.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.learning.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</lams:Alert5>
	</c:if>
		
	<lams:errors5/>
	<%-- The status of the last add/edit operation. --%>
	<c:if test="${recordOperationSuccess=='add'}">
		<lams:Alert5 id="addrecordsuccess" type="info" close="true">
			<fmt:message key="message.learning.addrecordsuccess" />
		</lams:Alert5>
	</c:if>
	<c:if test="${recordOperationSuccess=='edit'}">
		<lams:Alert5 id="addrecordsuccess" type="info" close="true">
			<fmt:message key="message.learning.editrecordsuccess" />
		</lams:Alert5>
	</c:if>
	
	<lams:Alert5 id="minRecords" type="info" close="true">		
		<c:if test="${not (empty daco.minRecords or daco.minRecords eq 0)}">
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
	</lams:Alert5>

	<c:if test="${not finishedLock }">
	
		<!--  record panel  -->
		<div class="card lcard">
			<div class="card-header">
				<fmt:message key="label.learning.heading.recordnumber" />
				
				<span id="displayedRecordNumberSpan" class="hint">
					${displayedRecordNumber}
				</span>
			</div>
		
			<!-- Form to add/edit a record -->
			<form:form action="saveOrUpdateRecord.do" modelAttribute="recordForm" method="post" id="recordForm" enctype="multipart/form-data" >
				<form:hidden path="sessionMapID" value="${sessionMapID}" />
				<form:hidden id="displayedRecordNumber" path="displayedRecordNumber" value="${displayedRecordNumber}" />
			
				<c:set var="fileNumber" value="0" />
				<c:set var="answerIndex" value="0" />
			
				<div class="ltable table-striped no-header">
					<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
						<div class="row py-3">
							<div class="col">
							
							<c:choose>
								<c:when test="${question.type==1 || question.type==2 || question.type==3}">
									<label for="answer${answerIndex}">
										<c:out value="${question.description}" escapeXml="false"/>
									</label>
								</c:when>
								<c:otherwise>
									<c:out value="${question.description}" escapeXml="false"/>
								</c:otherwise>
							</c:choose>
							
							<c:choose>
								<%-- The content varies depending on the question type --%>
								<c:when test="${question.type==1}"><%-- Single line text --%>
									<fmt:message key="label.learning.textfield.hint" var="textfieldHint"/>	
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<c:choose>
										<%-- Textfield entry length is limited 
											depending on the maximum number of characters the teacher provided
										--%>
										<c:when test="${question.max!=null}">
											<form:input placeholder="${textfieldHint}" path="answer[${answerIndex}]" size="60" maxlength="${question.max}"  cssClass="form-control"/>
										</c:when>
										<c:otherwise>
											<form:input placeholder="${textfieldHint}" path="answer[${answerIndex}]" size="60"  cssClass="form-control"/>
										</c:otherwise>
									</c:choose>
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==2}"><%-- Multi-line text --%>
									<fmt:message key="label.learning.textarea.hint" var="textareaHint"/>
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									<form:textarea placeholder="${textareaHint}" path="answer[${answerIndex}]" cols="60" rows="3"  cssClass="form-control"/>
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==3}"><%-- Number --%>
									<fmt:message key="label.learning.number.hint" var="numberHint"/>
									<c:if test="${not empty question.digitsDecimal}">
										<br />
										<%-- An information for the learner is displayed,
											if the number he provides will be rounded to the number of places after the decimal point,
											as stated by the teacher. --%>
										<fmt:message key="label.learning.number.decimal">
											<fmt:param value="${question.digitsDecimal}" />
										</fmt:message>
									</c:if>

									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									<form:input placeholder="${numberHint}" path="answer[${answerIndex}]" size="10"  cssClass="form-control"/>
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==4}"><%-- Date can be entered in three textfields --%>
									<div class="hint">
										<fmt:message key="label.learning.date.hint" />
									</div>
									
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<span class="d-flex flex-row pt-3 align-items-center flex-wrap">
										<div class="mb-3">
											<label for="answer${answerIndex}">
												<fmt:message key="label.learning.date.day" />
											</label>
											<form:input path="answer[${answerIndex}]" size="3"  cssClass="form-control form-control-sm"/>
										</div>
										
										<div class="mb-3">
											<c:set var="answerIndex" value="${answerIndex+1}" />
											<label for="answer${answerIndex}">
												<fmt:message key="label.learning.date.month" />
											</label>
											<form:input path="answer[${answerIndex}]" size="3"  cssClass="form-control form-control-sm"/>
										</div>
										
										<div class="mb-3">
											<c:set var="answerIndex" value="${answerIndex+1}" />
											<label for="answer${answerIndex}">
												<fmt:message key="label.learning.date.year" />
											</label>
											<form:input path="answer[${answerIndex}]" size="5"  cssClass="form-control form-control-sm"/>
										</div>
									</span>							
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==5}"><%-- File --%>
									<div class="hint">
										<fmt:message key="label.learning.file.hint" />
									</div>
									
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<lams:FileUpload5 fileFieldId="file-${fileNumber+1}" fileFieldname="file[${fileNumber}]" 
										fileInputMessageKey="label.authoring.basic.file" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"
										fileButtonBrowse="fileButtonBrowse-${fileNumber+1}" fileInputNameFieldname="fileInputName-${fileNumber+1}" errorMsgDiv="fileerror-${fileNumber+1}"/>
									<c:set var="fileNumber" value="${fileNumber+1}" />
								</c:when>
								
								<c:when test="${question.type==6}"><%-- Image --%>  
									<div class="hint">
										<fmt:message key="label.learning.image.hint" />
									</div>
									
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<lams:FileUpload5 fileFieldId="file-${fileNumber+1}" fileFieldname="file[${fileNumber}]" 
										fileInputMessageKey="label.authoring.basic.image" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"
										fileButtonBrowse="fileButtonBrowse-${fileNumber+1}" fileInputNameFieldname="fileInputName-${fileNumber+1}" errorMsgDiv="imageerror-${fileNumber+1}"/>
									<c:set var="fileNumber" value="${fileNumber+1}" />
								</c:when>
								
								<c:when test="${question.type==7}"><%-- Radio buttons  --%>
									<div class="hint">
										<fmt:message key="label.learning.radio.hint" />
									</div>
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<fieldset>
										<legend class="visually-hidden">
											<fmt:message key="label.learning.radio.hint" />
										</legend>
									
										<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
											<div class="form-check">
												<form:radiobutton path="answer[${answerIndex}]" id="radio-${questionStatus.index}-${status.index}" value="${status.index+1}" cssClass="form-check-input"/>
												<label for="radio-${questionStatus.index}-${status.index}" class="form-check-label">
													<%-- It displays for example A) instead of 1) --%>
													${fn:substring(ordinal,status.index,status.index+1)})
													 
													&nbsp;<c:out value="${answerOption.answerOption}" escapeXml="true"/>
												</label>
											</div>
										</c:forEach>
									</fieldset>
									
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==8}"><%-- Dropdown menu --%>
									<label class="hint" for="answer${answerIndex}">
										<fmt:message key="label.learning.dropdown.hint" />
									</label>
									
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<form:select path="answer[${answerIndex}]"  cssClass="form-select">
										<form:option value="0"><fmt:message key="label.learning.dropdown.select"/></form:option>
										<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
											<form:option value="${status.index+1}"><c:out value="${answerOption.answerOption}" escapeXml="true"/></form:option>
										</c:forEach>
									</form:select>
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==9}"><%-- Checkboxes --%>
									<div class="hint">
										<fmt:message key="label.learning.checkbox.hint" />
									</div>
									
									<c:if test="${horizontal}">
										</div><div class="col" style="vertical-align: middle;">
									</c:if>
									
									<form:hidden id="checkbox-${questionStatus.index+1}" path="answer[${answerIndex}]" />
									<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
										<div class="form-check">
											<input type="checkbox" id="checkbox-${questionStatus.index+1}-${status.index+1}" value="${status.index+1}" class="form-check-input"/>
											<label for="checkbox-${questionStatus.index+1}-${status.index+1}" class="form-check-label">
												${fn:substring(ordinal,status.index,status.index+1)})
												 
												<c:out value="${answerOption.answerOption}" escapeXml="true"/>
											</label>
										</div>
									</c:forEach>
									
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
								
								<c:when test="${question.type==10}"><%-- Longitude/latitude --%>
									<div class="hint">
										<fmt:message key="label.learning.longlat.hint" />
									</div>
									
									<c:if test="${horizontal}">
										</div>
										<div class="col">
									</c:if>
									
									<div class="form-horizontal">
										<div class="row mb-3">
											<label class="col-sm-2 col-form-label" for="answer${answerIndex}">
												<fmt:message key="label.learning.longlat.longitude" />
											</label>
											
											<div class="col-sm-2">
												<form:input path="answer[${answerIndex}]" size="10"  cssClass="form-control "/>
											</div>
											
											<div class="col-sm-1">
												<p class="form-control-plaintext">
													<fmt:message key="label.learning.longlat.longitude.unit" />
												</p>
											</div>
																			
										</div>
										<c:set var="answerIndex" value="${answerIndex+1}" />
														
										<div class="row mb-3">
											<label class="col-sm-2 col-form-label" for="answer${answerIndex}">
												<fmt:message key="label.learning.longlat.latitude" />
											</label>
											
											<div class="col-sm-2">
												<form:input path="answer[${answerIndex}]" size="10"  cssClass="form-control"/>
											</div>
											
											<div class="col-sm-1">
												<p class="form-control-plaintext">
													<fmt:message key="label.learning.longlat.latitude.unit" />
												</p>
											</div>
										</div>
									</div>
									
									<c:set var="answerIndex" value="${answerIndex+1}" />
								</c:when>
							</c:choose>
							</div>
						</div>
					</c:forEach>
				</div>
				
				<c:if test="${mode != 'teacher'}">	
					<div class="m-3 text-end">
						<button type="submit" onclick="return saveOrUpdateRecord();" class="btn btn-sm btn-secondary btn-disable-on-submit mt-2">
							<i class="fa fa-plus"></i> 
							<fmt:message key="label.learning.add" />
						</button>
					</div>
				</c:if>
		
			</form:form>
		</div>
	<!--  end record panel -->
	</c:if>
	
	<!-- Reflection -->
	<c:if test="${sessionMap.userFinished and daco.reflectOnActivity}">
		<lams:NotebookReedit
			reflectInstructions="${daco.reflectInstructions}"
			reflectEntry="${sessionMap.reflectEntry}"
			isEditButtonEnabled="${mode != 'teacher'}"
			notebookHeaderLabelKey="label.export.reflection.heading"
			editNotebookLabelKey="label.common.edit"/>
	</c:if>

</div> <!-- End addRecordDiv -->
<script type="text/javascript">
	$("input[name='answer[0]']").focus();
</script>