<%@ include file="/common/taglibs.jsp"%>

<div>
	<div class="question-type">
		<fmt:message key="label.learning.ordering.sort.answers" />
	</div>
	
	<div class="table-responsive">
		<table class="table table-condensed ordering-table">
			<c:forEach var="option" items="${question.optionDtos}" varStatus="ordStatus">
				<tr>
					<td class="ordering-option">
						<input type="hidden" name="question${status.index}_${option.uid}" value="${ordStatus.index}" />
						<c:out value="${option.name}" escapeXml="false" />
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>
