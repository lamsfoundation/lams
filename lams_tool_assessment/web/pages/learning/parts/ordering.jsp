<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty questionForOrdering}">
	<c:set var="question" value="${questionForOrdering}" />
</c:if>

<div id="orderingArea${question.uid}">
	<div class="question-type">
		<fmt:message key="label.learning.ordering.sort.answers" />
	</div>
	
	<div class="table-responsive">
		<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}" varStatus="ordStatus">
			<tr>
			
				<td class="ordering-option">
					<input type="hidden" name="question${status.index}_${option.displayOrder}" value="${option.displayOrder}" />
					<c:out value="${option.name}" escapeXml="false" />
				</td>
								
				<c:if test="${(mode != 'teacher') || !hasEditRight}">
					<td class="arrows">
						<c:if test="${not ordStatus.first}">
							<c:set var="up"><fmt:message key='label.authoring.basic.up'/></c:set>
							<lams:Arrow state="up" title="${up}" onclick="upOption(${question.uid},${ordStatus.index})"/>
						</c:if>
			
						<c:if test="${not ordStatus.last}">
							<c:set var="down"><fmt:message key='label.authoring.basic.down'/></c:set>
							<lams:Arrow state="down" title="${down}" onclick="downOption(${question.uid},${ordStatus.index})"/>
						</c:if>
					</td>
				</c:if>

			</tr>
		</c:forEach>
		</table>
	</div>
</div>
