<%@ include file="/common/taglibs.jsp"%>

<div style="padding: 10px 15px 7px; font-style: italic">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<table cellspacing="0" style="padding-bottom: 10px;">
	<c:forEach var="option" items="${question.questionOptions}">
		<tr>
			<c:if test="${(result.startDate != null)}">
				<td style="padding:5px 0px 2px 15px; vertical-align:middle; background:none; border-bottom:0px; width: 7px;">
					<c:if test="${(option.answerInt == option.sequenceId)}">
						<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">	
					</c:if>
					<c:if test="${(option.answerInt != -1) && (option.answerInt != option.sequenceId)}">
						<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>"	border="0">	
					</c:if>	
				</td>		
			</c:if>			
			<td style="padding:5px 15px 2px; vertical-align:middle; background:none; border-bottom:0px; width: 40%;">
				<c:out value="${option.question}" escapeXml="false" />
			</td>
			<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px; ">
				<html:select property="question${status.index}_${option.sequenceId}" value="${option.answerInt}">
					<html:option value="-1"><fmt:message key="label.learning.matching.pairs.choose" /></html:option>
					<c:forEach var="selectOption" items="${question.questionOptions}">
						<html:option value="${selectOption.sequenceId}">${selectOption.optionString}</html:option>
					</c:forEach>
				</html:select>
			</td>
			<c:if test="${(result.startDate != null) && (option.answerInt != -1)}">
				<td style="padding:5px 10px 2px; vertical-align:middle; background:none; border-bottom:0px;" width="30%">
					<c:out value="${option.feedback}" escapeXml="false" />
				</td>		
			</c:if>						
		</tr>
	</c:forEach>
</table>		

<%@ include file="markandpenaltyarea.jsp"%>