<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>

<script type="text/javascript">
	setTimeout("refreshPage()", 5000)

	function refreshPage() {

		var url = '${tool}learning.do';
		var params = 'dispatch=getVoteDisplay&toolSessionID=${scribeSessionDTO.sessionID}';

		var myAjax = new Ajax.Updater('voteDisplay', url, {
			method : 'get',
			parameters : params
		});

		setTimeout("refreshPage()", 5000)
	}

	function submitApproval() {
		var url = '${tool}learning.do';
		var params = 'dispatch=submitApproval&toolSessionID=${scribeSessionDTO.sessionID}';

		var myAjax = new Ajax.Updater('voteDisplay', url, {
			method : 'get',
			parameters : params
		});

		// remove the Agree button.
		document.getElementById("agreeButton").innerHTML = "";
	}

	function confirmForceComplete() {
		var message = "<fmt:message key='message.confirmForceComplete'/>";
		if (confirm(message)) {
			return true;
		} else {
			return false;
		}
	}
</script>

<lams:Page type="learner" title="${scribeDTO.title}">

	<div class="panel">
		<c:out value="${scribeDTO.instructions}" escapeXml="false" />		
	</div>

	<html:form action="learning">
		<html:hidden property="dispatch" value="submitReport"></html:hidden>
		<html:hidden property="toolSessionID"></html:hidden>
		<html:hidden property="mode"></html:hidden>

		<div id="voteDisplay">
			<%@include file="/pages/parts/voteDisplay.jsp"%>
		</div>

		<h4>
			<abbr class="pull-right hidden-xs" title="<fmt:message key="message.scribeInstructions2" />&nbsp;<fmt:message key="message.scribeInstructions3" />"><i
										class="fa fa-question-circle text-info"></i></abbr>
			<fmt:message key="heading.report" />
		</h4>
		<c:set var="counter" value="0"/>
		<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
		<c:set var="counter" value="${counter + 1}"/>
			<div class="row">
				<div class="col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading panel-title">
							<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false" />
						</div>
						<div class="panel-body">

							<c:if test="${not empty reportDTO.entryText}">
								<div class="panel-warning panel-body bg-warning">
									<abbr class="pull-right hidden-xs" title="<fmt:message key="label.what.others.see" />"><i
										class="fa fa-xs fa-question-circle text-info"></i></abbr>

									<c:set var="entry">
										<lams:out value="${reportDTO.entryText}" escapeHtml="true" />
									</c:set>
									<c:out value="${entry}" escapeXml="false" />
								</div>
							</c:if>

							<c:if test="${not scribeUserDTO.finishedActivity}">
								<html:textarea styleId="report-${counter}" property="report(${reportDTO.uid})" rows="6" value="${reportDTO.entryText}"
									styleClass="form-control voffset5"></html:textarea>
							</c:if>


						</div>
					</div>
				</div>
			</div>
		</c:forEach>

		<c:if test="${not scribeUserDTO.finishedActivity}">
			<div class="row">
				<div class="col-xs-12" id="submitReportBtn">
					<html:submit styleClass="btn btn-sm btn-default pull-right">
						<fmt:message key="button.submitReport" />
					</html:submit>
				</div>
			</div>
		</c:if>


	</html:form>

	<hr>


	<html:form action="learning" onsubmit="return confirmForceComplete();">
		<html:hidden property="dispatch" value="forceCompleteActivity" />
		<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
		<html:hidden property="mode" />

		<div id="forceCompleteBtn">
			<html:submit styleClass="btn btn-primary pull-right">
				<fmt:message key="button.forceComplete" />
			</html:submit>
		</div>
	</html:form>

	<c:if test="${scribeSessionDTO.reportSubmitted and (not scribeUserDTO.reportApproved)}">
		<div id="agreeButton">
			<a id="agreeButton" class="btn btn-success pull-left" onclick="submitApproval();"> <fmt:message
					key="button.agree" />
			</a>
		</div>
	</c:if>

	<div id="footer"></div>

</lams:Page>


<script type="text/javascript">
	window.onload = function() {
		document.getElementById("report-1").focus();
	}
</script>

