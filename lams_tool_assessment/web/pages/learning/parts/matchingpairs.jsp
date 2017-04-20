<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td>
					<c:out value="${option.question}" escapeXml="false" />
				</td>
				
				<td style="width: 100px;">
					<html:select property="question${status.index}_${option.sequenceId}" value="${option.answerInt}" disabled="${!hasEditRight}">
						<html:option value="-1">
							<fmt:message key="label.learning.matching.pairs.choose" />
						</html:option>
						
						<c:forEach var="selectOption" items="${question.matchingPairOptions}">
							<html:option value="${selectOption.uid}">
								${selectOption.optionString}
							</html:option>
						</c:forEach>
					</html:select>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>