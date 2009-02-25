<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<c:forEach var="option" items="${question.questionOptions}">
		<tr>
			<c:if test="${(result.startDate != null)}">
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
					<c:if test="${option.answerBoolean && (option.grade > 0)}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
					</c:if>
					<c:if test="${option.answerBoolean && (option.grade <= 0)}">
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>"	border="0">	
					</c:if>	
				</td>		
			</c:if>		
			<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; width: 5px; border-bottom:0px; ">
				<c:choose>
					<c:when test="${question.multipleAnswersAllowed}">
						<input type="checkbox" name="question${status.index}_${option.sequenceId}" value="${true}" styleClass="noBorder"
	 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
	<%--						<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>
	--%>					 
						/>			
					</c:when>
					<c:otherwise>
						<input type="radio" name="question${status.index}" value="${option.sequenceId}" styleClass="noBorder"
	 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
	<%--						<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>
	--%>					 
						/>
					</c:otherwise>
				</c:choose>
				
			</td>
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;">
				<c:out value="${option.optionString}" escapeXml="false" />
			</td>
			<c:if test="${(result.startDate != null) && option.answerBoolean}">
				<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;" width="30%">
					<c:out value="${option.feedback}" escapeXml="false" />
				</td>		
			</c:if>
		</tr>
	</c:forEach>
</table>	

<c:if test="${(result.startDate != null)}">
	<c:choose>
		<c:when test="${result.mark == defaultGrade}">
			<div style="padding: 15px 15px 0px; font-style: italic">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</div>
		</c:when>
		<c:when test="${result.mark > 0}">
			<div style="padding: 15px 15px 0px; font-style: italic">
				<c:out value="${question.feedbackOnPartiallyCorrect}" escapeXml="false" />
			</div>
		</c:when>
		<c:when test="${result.mark <= 0}">
			<div style="padding: 15px 15px 0px; font-style: italic">
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</div>
		</c:when>		
	</c:choose>
</c:if>

<%@ include file="markandpenaltyarea.jsp"%>
