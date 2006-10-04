<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL/>
</c:set>

<script type="text/javascript">
	var t=setTimeout("refreshPage()",5000)

	function refreshPage() {
		window.location.href='${tool}learning.do?toolSessionID=${scribeSessionDTO.sessionID}&mode=${MODE}'
	}
</script>

<div id="content">
	
	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="false" />
	</h1>

	<p>
		${scribeDTO.instructions}
	</p>
	
	<%@include file="/pages/parts/voteDisplay.jsp" %>

	<div class="field-name" style="text-align: left">
		<fmt:message key="heading.report" />
	</div>
	<html:form action="learning">
		<html:hidden property="dispatch" value="submitApproval"></html:hidden>
		<html:hidden property="toolSessionID"></html:hidden>
		<html:hidden property="mode"></html:hidden>

		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
			<p>
				${reportDTO.headingDTO.headingText}
			</p>

			<p>
				<lams:out value="${reportDTO.entryText}" />
			</p>

		</c:forEach>

		<p>
			<c:if test="${not scribeUserDTO.reportApproved}">
				<html:submit styleClass="button">
					<fmt:message key="button.agree" />					
				</html:submit>
			</c:if>
		</p>

	</html:form>
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>

	</c:if>

	<div class="space-bottom"></div>

</div>
