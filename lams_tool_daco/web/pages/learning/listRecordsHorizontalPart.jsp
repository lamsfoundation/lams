<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	<c:set var="recordList" value="${sessionMap.recordList}" />
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	<lams:css style="tabbed" />
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
	<table id="horizontalListTable" class="alternative-color" cellspacing="0">
		<tr>
			<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
				<td class="fixedCellHeight">
					<div class="bigNumber" style="float: left; margin-right: 30px;">
						${recordStatus.index+1}
					</div>
					<div>
					<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.edit" />"
							onclick="javascript:parent.editRecord('${sessionMapID}',${recordStatus.index+1})" />
					<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.delete" />"
							onclick="javascript:parent.removeRecord('${sessionMapID}',${recordStatus.index+1})" />
					</div>
				</td>
			</c:forEach>
		</tr>
		<c:forEach var="question" items="${daco.dacoQuestions}" varStatus="questionStatus">	
		<tr>
			<c:forEach var="record" items="${recordList}" varStatus="recordStatus">
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
									<td class="fixedCellHeight" 
										<c:if test="${questionStatus.index==fn:length(daco.dacoQuestions)-1}">
											id="lastHorizontalQuestion"
										</c:if>
									 >
									
									<c:choose>
										<c:when test="${question.type==1 || question.type==4}">
											<input type="text" size="72" readonly="readonly" value="${answer.answer}"/>
										</c:when>
										<c:when test="${question.type==2}">
											<textarea  cols="55" rows="3" readonly="readonly">${answer.answer}</textarea>
										</c:when>
										<c:when test="${question.type==3}">
											<input type="text" size="10" readonly="readonly" value="${answer.answer}"/>
										</c:when>
										<c:when test="${question.type==5 || question.type==6}">
											<c:choose>
												<c:when test="${empty answer.fileName}">
													<fmt:message key="label.learning.file.notuploaded" />
												</c:when>
												<c:otherwise>
													<fmt:message key="label.learning.file.uploaded" /> ${answer.fileName}
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
													<fmt:message key="label.learning.dropdown.selected" /> ${answer.answer}
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
															<input type="checkbox" disabled="disabled" id="checkbox-record${recordStatus.index+1}-question${questionStatus.index+1}-${status.index+1}">${answerOption.answerOption}</input><br />
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
	<script type="text/javascript">
	</script>
</body>
</lams:html>