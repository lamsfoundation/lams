<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript"
	src="${lams}includes/javascript/prototype.js"></script>

<script type="text/javascript">
	setTimeout("refreshPage()",5000)

	function refreshPage() {
		
		var url = '${tool}learning.do';
		var params = 'dispatch=getVoteDisplay&toolSessionID=${scribeSessionDTO.sessionID}';
		
		var myAjax = new Ajax.Updater(
			'voteDisplay',
			url,
			{
				method: 'get',
				parameters: params
			});
				
		setTimeout("refreshPage()",5000)		
	}
	
	function submitApproval() {
		var url = '${tool}learning.do';
		var params = 'dispatch=submitApproval&toolSessionID=${scribeSessionDTO.sessionID}';
		
		var myAjax = new Ajax.Updater(
			'voteDisplay',
			url,
			{
				method: 'get',
				parameters: params
		});
		
		// remove the Agree button.
		document.getElementById("agreeButton").innerHTML = "";
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

		<div id="voteDisplay">
			<%@include file="/pages/parts/voteDisplay.jsp"%>
		</div>

		<h2>
			<fmt:message key="heading.report" />
		</h2>
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

				<c:if test="${not scribeUserDTO.finishedActivity}">
					<html:textarea property="report(${reportDTO.uid})" rows="4"
						cols="20" value="${reportDTO.entryText}" style="width: 100%;"></html:textarea>
				</c:if>
			</div>
		</c:forEach>

		<c:if test="${not scribeUserDTO.finishedActivity}">
			<html:submit styleClass="button small-space-bottom">
				<fmt:message key="button.submitReport" />
			</html:submit>
		</c:if>

	</html:form>

	<hr>

	<div class="space-bottom-top">
		<html:form action="learning">
			<html:hidden property="dispatch" value="forceCompleteActivity" />
			<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
			<html:hidden property="mode" />

			<div class="right-buttons">
				<html:submit styleClass="button">
					<fmt:message key="button.forceComplete" />
				</html:submit>
			</div>
		</html:form>

		<span id="agreeButton"> <c:if
				test="${scribeSessionDTO.reportSubmitted and (not scribeUserDTO.reportApproved)}">
				<a id="agreeButton" class="button" onclick="submitApproval();">
					<fmt:message key="button.agree" /> </a>
			</c:if> </span>
	</div>
</div>

<div id="footer"></div>
