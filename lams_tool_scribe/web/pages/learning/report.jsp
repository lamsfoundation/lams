<%@ include file="/common/taglibs.jsp"%>

<div id="content">

	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="false" />
	</h1>

	<p>
		${scribeDTO.instructions}
	</p>

	<%@include file="/pages/parts/voteDisplay.jsp"%>

	<div class="field-name">
		<fmt:message key="heading.report" />
	</div>
		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
			<p>
				${reportDTO.headingDTO.headingText}
			</p>

			<p>
				<lams:out value="${reportDTO.entryText}" />
			</p>

		</c:forEach>

		<c:if test="${MODE == 'learner' || MODE == 'author'}">
			<%@ include file="parts/finishButton.jsp"%>
		</c:if>
		
		<div class="space-bottom"></div>
</div>
