<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.matching.pairs.pick.up" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td>
					<c:out value="${option.matchingPair}" escapeXml="false" />
				</td>
				
				<td style="width: 100px;">
					<select name="question${status.index}_${option.uid}" <c:if test="${!hasEditRight}">disabled="disabled"</c:if>>
						<option value="-1">
							<fmt:message key="label.learning.matching.pairs.choose" />
						</option>
						
						<c:forEach var="selectOption" items="${question.matchingPairOptions}">
							<option value="${selectOption.uid}"
								<c:if test="${option.answerInt == selectOption.uid}">selected="selected"</c:if>>
								${selectOption.name}
							</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>