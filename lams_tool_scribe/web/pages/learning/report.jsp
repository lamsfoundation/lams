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

		<div class="shading-bg">
			<p>
				${reportDTO.headingDTO.headingText}
			</p>

			<c:if test="${not empty reportDTO.entryText}">
				<ul>
					<li>
						<p>
							<lams:out value="${reportDTO.entryText}" />
						</p>
					</li>
				</ul>
			</c:if>
		</div>
	</c:forEach>

	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>

<div id="footer"></div>
