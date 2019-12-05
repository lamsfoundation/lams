<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<c:set var="questionDtos" value="${sessionMap.questionDtos}" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>

<script>
	// Inform author whether the QB question was modified
	var qbQuestionModified = ${empty qbQuestionModified ? 0 : qbQuestionModified},
		qbMessage = null;
	switch (qbQuestionModified) {
		case <%= IQbService.QUESTION_MODIFIED_UPDATE %>: 
			qbMessage = '<fmt:message key="message.qb.modified.update" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_VERSION_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.version" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_ID_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.new" />';
			break;
	}
	if (qbMessage) {
		alert(qbMessage);
	}
</script>

<div id="itemList">
<div class="panel panel-default add-file">
	
	<div class="panel-heading panel-title">
		<fmt:message key="label.questions" />
		<i class="fa fa-spinner" style="display: none" id="itemArea_Busy"></i>
		<div class="btn-group btn-group-xs pull-right">
			<c:if test="${!isAuthoringRestricted}">
				<a href="#nogo" onClick="javascript:importQTI()" class="btn btn-default" id="importQTI">
					<fmt:message key="label.authoring.import.qti" />
				</a>
			</c:if>
		</div>
	</div>

	<table id="itemTable" class="table table-striped table-condensed">
		<tr>
			<th>
				#
			</th>
			<th>
				<fmt:message key="label.questions" />
			</th>

			<th class="text-center">
				<fmt:message key="label.question.marks" />
			</th>

			<th colspan="3">
				&nbsp;
			</th>
		</tr>

		<c:forEach items="${questionDtos}" var="questionDto" varStatus="status">
			<c:set var="queIndex" scope="request" value="${status.index + 1}" />

			<tr>
				<td>
					${queIndex})
				</td>
				<td>
					<div style="overflow: auto;">
						<c:out value="${questionDto.name}" escapeXml="false" />
						
						<!-- handle old questions, that have generic question name 'MCQ question'-->
						<c:if test="${questionDto.name == 'MCQ question'}">
							<div class="small">
								<c:out value="${questionDto.description}" escapeXml="false" />
							</div>
						</c:if>
						
						<%--
					    <span class='pull-right alert-info btn-xs loffset5 roffset5'>
					    	v.&nbsp;${questionDto.version}
					    </span>	
					    --%>
					</div>
				</td>

				<td width="70px" class="text-center question-max-mark">
					<c:out value="${questionDto.mark}" />
				</td>

				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${fn:length(questionDtos) != 1}">
						<!-- Don't display up icon if first line -->
						<c:if test="${queIndex != 1}">
							<c:set var="tip"><fmt:message key='label.tip.moveQuestionUp'/></c:set>
 		 					<lams:Arrow state="up" title="${tip} ${queIndex}" 
		 						onclick="javascript:submitModifyAuthoringQuestion(${queIndex},'moveQuestionUp');"/>
 		 				</c:if>
						<!-- Don't display down icon if last line -->
						<c:if test="${queIndex != fn:length(questionDtos)}">
							<c:set var="tip"><fmt:message key='label.tip.moveQuestionDown'/></c:set>
	 						<lams:Arrow state="down" title="${tip} ${queIndex}"  
								onclick="javascript:submitModifyAuthoringQuestion(${queIndex},'moveQuestionDown');"/>
	 	 				</c:if>
					</c:if>
				</td>

				<td align="center" style="width:5%">
					<c:set var="editItemUrl" >
						<lams:WebAppURL />authoring/editQuestionBox.do?questionIndex=${queIndex}&sessionMapId=${sessionMapId}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>
					<a href="${editItemUrl}" class="thickbox"> 
						<i class="fa fa-pencil" title="<fmt:message key='label.tip.editQuestion'/>"></i>
					</a>
				</td>

				<c:if test="${!isAuthoringRestricted}">
					<td  align="center" style="width:5%">
						<i class="fa fa-times" title="<fmt:message key="label.tip.deleteQuestion" />" onclick="removeQuestion(${queIndex});"></i>
					</td>
				</c:if>

			</tr>
		</c:forEach>

	</table>
</div>
</div>
