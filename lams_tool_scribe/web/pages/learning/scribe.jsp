<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL/>
</c:set>
<c:set var="tool">
	<lams:WebAppURL/>
</c:set>

<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>

<script type="text/javascript">
	setTimeout("refreshPage()",5000)

	function refreshPage() {
		
		var url = '${tool}learning.do';
		var params = 'dispatch=getVoteCount&toolSessionID=${scribeSessionDTO.sessionID}';
		
		var myAjax = new Ajax.Updater(
			'voteDisplay',
			url,
			{
				method: 'get',
				parameters: params
			});
				
		setTimeout("refreshPage()",5000)		
	}
</script>

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

		<div class="field-name" style="text-align: left">
			?Number of Votes?
		</div>

		<p id="voteDisplay">
			${scribeSessionDTO.numberOfVotes}
		</p>

		<div class="field-name" style="text-align: left">
			?Headings?
		</div>
		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
			<p>
				${reportDTO.headingDTO.headingText}
			</p>

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
