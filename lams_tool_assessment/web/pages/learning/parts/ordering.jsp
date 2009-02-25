<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty resultForOrdering}">
	<c:set var="result" value="${resultForOrdering}" />
	<c:set var="question" value="${result.assessmentQuestion}" />
</c:if>

<div id="orderingArea">
	<div style="padding: 10px 15px 7px; font-style: italic">
		<fmt:message key="label.learning.ordering.sort.answers" />
	</div>
	
	<table cellspacing="0" style="padding-bottom: 10px;">
		<c:forEach var="option" items="${question.questionOptions}" varStatus="status">
			<tr>
	<%--				
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; width: 5px; border-bottom:0px; ">
					<c:choose>
						<c:when test="${question.multipleAnswersAllowed}">
							<input type="checkbox" name="question${status.index}_${option.sequenceId}" value="${true}" styleClass="noBorder"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>
							 
							/>						
						</c:when>
						<c:otherwise>
							<input type="radio" name="question${status.index}" value="${option.sequenceId}" styleClass="noBorder"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>
							 
							/>
						</c:otherwise>
					</c:choose>
					
				</td>
	--%>			
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; ">
					<input type="hidden" name="question${status.index}_${option.sequenceId}" value="${option.sequenceId}" />
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<td width="20px" style="padding:5px 0px 2px 15px; vertical-align:middle; text-align: center; background:none; border-bottom:0px;">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.up"/>"
							onclick="upOption(${question.uid},${status.index})">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.down"/>">
						</c:if>
					</c:if>
		
					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>">
						</c:if>
		
						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.down"/>"
							onclick="downOption(${question.uid},${status.index})">
					</c:if>
				</td>			
			</tr>
		</c:forEach>
	</table>		

	<%@ include file="markandpenaltyarea.jsp"%>
</div>