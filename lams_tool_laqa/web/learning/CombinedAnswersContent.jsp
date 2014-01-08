<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">
	<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswers}">
	
		<c:if test="${questionEntry.key == answerEntry.key}">
		
			<div class="shading-bg">
				<p>
			 		<strong><fmt:message key="label.question" /> <c:out
					value="${questionEntry.key}" />:</strong>
					<c:if test="${questionEntry.value.required}">
						<fmt:message key="label.required" />
					</c:if>
					<c:out value="${questionEntry.value.question}" escapeXml="false" />
				</p>
								 
				<p>
					<strong><fmt:message key="label.answer" /></strong>
				</p>
				
			<c:choose>
				<c:when test="${hasEditRight}">
				
					<c:choose>
						<c:when test="${generalLearnerFlowDTO.allowRichEditor}">
							<lams:CKEditor id="answer${questionEntry.key}" value="${answerEntry.value}"
								contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner">
							</lams:CKEditor>
						</c:when>
				
						<c:otherwise>
							<lams:textarea name="answer${questionEntry.key}" rows="5" cols="60" class="text-area"><c:out value='${answerEntry.value}' escapeXml='false' /></lams:textarea>
						</c:otherwise>
					</c:choose>
					
				</c:when>
				
				<c:otherwise>
					<lams:textarea name="answer${questionEntry.key}" rows="5" cols="60" class="text-area" disabled="disabled"><c:out value='${answerEntry.value}' escapeXml='false' /></lams:textarea>
				</c:otherwise>
			</c:choose>
			</div>
		
		</c:if>
	
	</c:forEach>
</c:forEach>

<c:if test="${hasEditRight}">
	<div class="space-bottom-top align-right">
		<html:button property="btnCombined" onclick="javascript:submitMethod('submitAnswersContent');"	styleClass="button">
			<fmt:message key="button.submitAllContent" />
		</html:button>
	</div>
</c:if>