<%@ include file="/common/taglibs.jsp"%>
<div id="addRecordDiv">

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:if test="${empty displayedRecordNumber}">
	<c:set var="displayedRecordNumber" value="1" />
</c:if>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}" />
<c:set var="horizontal" value="${sessionMap.learningView=='horizontal'}" />
<c:set var="ordinal"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></c:set>

<%@ include file="/common/messages.jsp"%>

<c:if test="${recordOperationSuccess=='add'}">
	<div class="info"><fmt:message key="message.learning.addrecordsuccess" /></div>
</c:if>
<c:if test="${recordOperationSuccess=='edit'}">
	<div class="info"><fmt:message key="message.learning.editrecordsuccess" /></div>
</c:if>
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

<p class="hint">
	<fmt:message key="label.learning.heading.recordnumber" />
	<span id="displayedRecordNumberSpan" class="hint">${displayedRecordNumber}</span>
</p>

<html:form action="learning/saveOrUpdateRecord" method="post" styleId="recordForm" enctype="multipart/form-data">

<c:set var="fileNumber" value="0" />
<c:set var="answerIndex" value="0" />

<html:hidden property="sessionMapID" value="${sessionMapID}" />
<html:hidden styleId="displayedRecordNumber" property="displayedRecordNumber" value="${displayedRecordNumber}" />

	<table cellspacing="0" class="alternative-color">
		<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">
			<tr>
				<td>
				<div class="bigNumber">${questionStatus.index+1}</div>
				${question.description}
				<c:choose>
					<c:when test="${question.type==1}">
						<div class="hint"><fmt:message key="label.learning.textfield.hint" /></div>		
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<c:choose>
							<c:when test="${question.max!=null}">
								<html:text property="answer[${answerIndex}]" size="72" maxlength="${question.max}" />
							</c:when>
							<c:otherwise>
								<html:text property="answer[${answerIndex}]" size="72" />
							</c:otherwise>
						</c:choose>
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==2}">
						<div class="hint"><fmt:message key="label.learning.textarea.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<html:textarea property="answer[${answerIndex}]" cols="55" rows="3" />
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==3}">
						<div class="hint"><fmt:message key="label.learning.number.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<html:text property="answer[${answerIndex}]" size="10" />
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==4}">
						<div class="hint"><fmt:message key="label.learning.date.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<label><fmt:message key="label.learning.date.day" /></label>
						<html:text property="answer[${answerIndex}]" size="3" />
						
						<c:set var="answerIndex" value="${answerIndex+1}" />
						<label><fmt:message key="label.learning.date.month" /></label>
						<html:text property="answer[${answerIndex}]" size="3" />
						
						<c:set var="answerIndex" value="${answerIndex+1}" />
						<label><fmt:message key="label.learning.date.year" /></label>
						<html:text property="answer[${answerIndex}]" size="5" />
						
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==5}">
						<div class="hint"><fmt:message key="label.learning.file.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<html:file property="file[${fileNumber}]" size="58" />
						<c:set var="fileNumber" value="${fileNumber+1}" />
					</c:when>
					<c:when test="${question.type==6}">
						<div class="hint"><fmt:message key="label.learning.image.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<html:file property="file[${fileNumber}]" size="58" />
						<c:set var="fileNumber" value="${fileNumber+1}" />
					</c:when>
					<c:when test="${question.type==7}">
						<div class="hint"><fmt:message key="label.learning.radio.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
						${fn:substring(ordinal,status.index,status.index+1)}) <html:radio property="answer[${answerIndex}]" value="${status.index+1}">${answerOption.answerOption}</html:radio><br />
						</c:forEach>
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==8}">
						<div class="hint"><fmt:message key="label.learning.dropdown.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<html:select property="answer[${answerIndex}]">
						<html:option value="0"><fmt:message key="label.learning.dropdown.select" /></html:option>
						<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
							<html:option value="${status.index+1}">${answerOption.answerOption}</html:option>
						</c:forEach>
						</html:select>
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==9}">
						<div class="hint"><fmt:message key="label.learning.checkbox.hint" /></div>
						<c:if test="${horizontal}">
							</td><td style="vertical-align: middle;">
						</c:if>
						<html:hidden  styleId="checkbox-${questionStatus.index+1}" property="answer[${answerIndex}]" />
						<c:forEach var="answerOption" items="${question.answerOptions}" varStatus="status">
						${fn:substring(ordinal,status.index,status.index+1)})
						<input type="checkbox" id="checkbox-${questionStatus.index+1}-${status.index+1}" value="${status.index+1}">
							${answerOption.answerOption}
						</input><br />
						</c:forEach>
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
					<c:when test="${question.type==10}">
						<div class="hint"><fmt:message key="label.learning.longlat.hint" /></div>
						<c:if test="${horizontal}">
							</td><td>
						</c:if>
						<table class="alternative-color-inner-table">
							<tr>
								<td width="80px">
								<label><fmt:message key="label.learning.longlat.longitude" /></label>
								</td>
								<td>
								<html:text property="answer[${answerIndex}]" size="10" />
								<label><fmt:message key="label.learning.longlat.longitude.unit" /></label><br />
								</td>									
							</tr>
							<c:set var="answerIndex" value="${answerIndex+1}" />
							<tr>
								<td>
								<label><fmt:message key="label.learning.longlat.latitude" /></label>
								</td>
								<td>
								<html:text property="answer[${answerIndex}]" size="10" />
								<label><fmt:message key="label.learning.longlat.latitude.unit" /></label><br />
								</td>
							</tr>
						</table>
						<c:set var="answerIndex" value="${answerIndex+1}" />
					</c:when>
				</c:choose>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="button-add-div">
	<lams:ImgButtonWrapper>
		<a href="#" onclick="javascript:saveOrUpdateRecord()" class="button-add-item"><fmt:message key="label.learning.add" />
		</a>
	</lams:ImgButtonWrapper>
	</div>
</html:form>
</div>