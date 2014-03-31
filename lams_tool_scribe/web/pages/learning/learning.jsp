<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
	var t=setTimeout("refreshPage()",5000)

	function refreshPage() {
		window.location.href='${tool}learning.do?toolSessionID=${scribeSessionDTO.sessionID}&mode=${MODE}'
	}
</script>

<div id="content">

	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="true" />
	</h1>

	<p>
		<c:out value="${scribeDTO.instructions}" escapeXml="false"/>
	</p>

	<%@include file="/pages/parts/voteDisplay.jsp"%>

	<div class="field-name">
		<fmt:message key="heading.report" />
	</div>
	<html:form action="learning">
		<html:hidden property="dispatch" value="submitApproval"></html:hidden>
		<html:hidden property="toolSessionID"></html:hidden>
		<html:hidden property="mode"></html:hidden>

		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">

			<div class="shading-bg">
				<p>
					<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false"/> 
				</p>

				<c:if test="${not empty reportDTO.entryText}">
					<ul>
						<li>
							<p>
								<c:set var="entry">
									<lams:out value="${reportDTO.entryText}" escapeHtml="true"/>
								</c:set>
								<c:out value="${entry}" escapeXml="false"/> 
							</p>
						</li>
					</ul>
				</c:if>
			</div>

		</c:forEach>

		<p>
			<c:if
				test="${scribeSessionDTO.reportSubmitted and (not scribeUserDTO.reportApproved)}">
				<html:submit styleClass="button">
					<fmt:message key="button.agree" />
				</html:submit>
			</c:if>
		</p>

	</html:form>

</div>

<div id="footer"></div>
