<%@ include file="/common/taglibs.jsp"%>

<div id="content">

	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="false" />
	</h1>

	<p>
		${scribeDTO.instructions}
	</p>

	<html:form action="learning">
		<html:hidden property="dispatch" value="submitReport"></html:hidden>
		<html:hidden property="toolSessionID"></html:hidden>
		<html:hidden property="mode"></html:hidden>

		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
			<h3>
				${reportDTO.headingDTO.headingText}
			</h3>

			<p>
				<lams:out value="${reportDTO.entryText}" />
			</p>

			<html:textarea property="report(${reportDTO.uid})" rows="4" cols="20"
				value="${reportDTO.entryText}" style="width: 100%;"></html:textarea>

		</c:forEach>

		<p>
			<html:submit styleClass="button">
				?Submit Report?
			</html:submit>
		</p>

	</html:form>

	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>

	<div class="space-bottom"></div>

</div>
