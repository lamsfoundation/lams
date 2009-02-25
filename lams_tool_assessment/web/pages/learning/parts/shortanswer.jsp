<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<tr>
		<c:forEach var="option" items="${question.questionOptions}">
		
		</c:forEach>
		<c:if test="${(result.startDate != null)}">
			<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
				<c:if test="${question.answerString == option.optionString}">
					<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
				</c:if>
				<c:if test="${question.answerString != option.optionString}">
					<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">	
				</c:if>	
			</td>		
		</c:if>	
		<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; ">
			<input type="text" name="question${status.index}" value="${question.answerString}" styleClass="noBorder" size="75"
	<%--						<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>
	--%>					 
			/>	
		</td>
		<c:if test="${(result.startDate != null) && (option.answerInt != -1)}">
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;" width="30%">
				<c:out value="${option.feedback}" escapeXml="false" />
			</td>		
		</c:if>			
	</tr>
</table>		

<%@ include file="markandpenaltyarea.jsp"%>