<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:set var="appointedScribe">
	<c:out value="${scribeSessionDTO.appointedScribe}" escapeXml="true" />
</c:set>


<script type="text/javascript">
	var t = setTimeout("refreshPage()", 10000)

	function refreshPage() {
		window.location.href = '${tool}learning.do?toolSessionID=${scribeSessionDTO.sessionID}&mode=${MODE}'
	}
</script>

<lams:Page type="learner" title="${scribeDTO.title}">

	<div class="panel">
		<c:out value="${scribeDTO.instructions}" escapeXml="false" />
	</div>

	<%@include file="/pages/parts/voteDisplay.jsp"%>

	<h4>
		<abbr class="pull-right hidden-xs"
			title="<fmt:message key="message.learnerInstructions2" ><fmt:param value="${appointedScribe}"/></fmt:message>&nbsp;<fmt:message key="message.learnerInstructions3" />"><i
			class="fa fa-question-circle text-info"></i></abbr>
		<fmt:message key="heading.report" />
	</h4>
	<p class="help-block" style="font-size: 12px"><fmt:message key="activity.title"/>: ${appointedScribe}</p>
	<html:form action="learning">
		<html:hidden property="dispatch" value="submitApproval"></html:hidden>
		<html:hidden property="toolSessionID"></html:hidden>
		<html:hidden property="mode"></html:hidden>

		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">

			<div class="row">
				<div class="col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false" />
						</div>
						<div class="panel-body">
							<c:if test="${not empty reportDTO.entryText}">
								<abbr class="pull-right hidden-xs" title="<fmt:message key="label.scribe.posted" />"><i
									class="fa fa-xs fa-question-circle text-info"></i></abbr>

								<c:set var="entry">
									<lams:out value="${reportDTO.entryText}" escapeHtml="true" />
								</c:set>
								<c:out value="${entry}" escapeXml="false" />

							</c:if>
						</div>
					</div>
				</div>
			</div>

		</c:forEach>

		<c:if test="${scribeSessionDTO.reportSubmitted and (not scribeUserDTO.reportApproved)}">
			<div class="row">
				<div class="col-xs-12" id="agreeButton">
					<html:submit styleClass="btn btn-success pull-left">
						<fmt:message key="button.agree" />
					</html:submit>
				</div>
			</div>
		</c:if>

	</html:form>


	<div id="footer"></div>

</lams:Page>
