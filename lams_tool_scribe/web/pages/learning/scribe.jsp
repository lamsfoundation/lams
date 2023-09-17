<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
		
		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	</lams:head>
	
	<body class="stripes">
		
		<script type="text/javascript" src='${lams}includes/javascript/jquery.js'></script>
		<script type="text/javascript">
			$(document).ready(function(){
				// focus on the first report input
				$('textarea[id^="report-"]').first().focus();
			});
		
			//init the connection with server using server URL but with different protocol
			var scribeWebsocket = new WebSocket('${tool}'.replace('http', 'ws')
							+ 'learningWebsocket?toolSessionID=' + ${scribeSessionDTO.sessionID}),
				scribeWebsocketPingTimeout = null,
				scribeWebsocketPingFunc = null,
				agreementPercentageLabel = '<fmt:message key="message.voteStatistics" />',
				reportSubmitted = ${scribeSessionDTO.reportSubmitted};
				
			scribeWebsocketPingFunc = function(skipPing){
				if (scribeWebsocket.readyState == scribeWebsocket.CLOSING 
						|| scribeWebsocket.readyState == scribeWebsocket.CLOSED){
					location.reload();
				}
				
				// check and ping every 3 minutes
				scribeWebsocketPingTimeout = setTimeout(scribeWebsocketPingFunc, 3*60*1000);
				// initial set up does not send ping
				if (!skipPing) {
					scribeWebsocket.send("ping");
				}
			};
			// set up timer for the first time
			scribeWebsocketPingFunc(true);
		 	
		 	scribeWebsocket.onclose = function(e){
		 		// react only on abnormal close
		 		if (e.code === 1006) {
		 	 		location.reload();
		 		}
		 	};
		 	
			// run when the server pushes new reports and vote statistics
			scribeWebsocket.onmessage = function(e) {
				// create JSON object
				var input = JSON.parse(e.data),
					agreeButton = $('#agreeButton');
				
				// if the scribe or monitor force completes the activity, reload to display report page
				if (input.close) {
					window.location.href = '${tool}learning.do?toolSessionID=${scribeSessionDTO.sessionID}&mode=${MODE}';
					return;
				}
				
				// reset ping timer
				clearTimeout(scribeWebsocketPingTimeout);
				scribeWebsocketPingFunc(true);
				
				// only changed reports will be sent
				if (input.reports) {
					$.each(input.reports, function() {
						$('#reportText-' + this.uid).html(this.text);
					});
				}
				
				// can the user vote
				if (!reportSubmitted || input.approved) {
					agreeButton.hide();
				} else {
					agreeButton.show();
				}
				
				// update vote statistics 
				var label = agreementPercentageLabel.replace('{0}', input.numberOfVotes)
													.replace('{1}', input.numberOfLearners);
				$('#agreementPercentageLabel').text(label);
				$('#agreementPercentage').text('(' + input.votePercentage + '%)');
				$('#agreementPercentageBar').attr('aria-valuenow', input.votePercentage)
											.css('width',input.votePercentage + '%');
			}
			
			function submitApproval() {
				var data = {
						type : 'vote'	
					};
				scribeWebsocket.send(JSON.stringify(data));
				
				$('#agreeButton').hide();
			}
			
			function submitReport() {
				var reports = [],
					data = {
						type : 'submitReport'	
					};
				// get each report part (heading) text
				$('textarea[id^="report-"]').each(function(){
					reports.push({
						'uid'  : $(this).attr('id').split('-')[1],
						'text' : $(this).val() 
					});
				});
				data.reports = reports;
				scribeWebsocket.send(JSON.stringify(data));
				
				reportSubmitted = true;
			}
		
			function confirmForceComplete() {
				return confirm("<fmt:message key='message.confirmForceComplete'/>");
			}
		</script>
		
		<lams:Page type="learner" title="${scribeDTO.title}">
		
			<div class="panel">
				<c:out value="${scribeDTO.instructions}" escapeXml="false" />		
			</div>
		
			<div id="voteDisplay">
				<%@include file="/pages/parts/voteDisplay.jsp"%>
			</div>
		
			<h4>
				<abbr class="float-end d-none d-sm-block" title="<fmt:message key="message.scribeInstructions2" />&nbsp;<fmt:message key="message.scribeInstructions3" />"><i
											class="fa fa-question-circle text-info"></i></abbr>
				<fmt:message key="heading.report" />
			</h4>
			<c:forEach var="reportDTO" items="${scribeSessionDTO.reportDTOs}">
				<div class="row">
					<div class="col-12">
						<div class="panel panel-default">
							<div class="panel-heading panel-title">
								<c:out value="${reportDTO.headingDTO.headingText}" escapeXml="false" />
							</div>
							<div class="panel-body">
		
								<div class="panel-warning panel-body bg-warning">
									<abbr class="float-end d-none d-sm-block" title="<fmt:message key="label.what.others.see" />"><i
										class="fa fa-xs fa-question-circle text-info"></i></abbr>
		
									<c:set var="entry">
										<lams:out value="${reportDTO.entryText}" escapeHtml="true" />
									</c:set>
									<span id="reportText-${reportDTO.uid}"><c:out value="${entry}" escapeXml="false" /></span>
								</div>
		
								<c:if test="${not scribeUserDTO.finishedActivity}">
									<textarea id="report-${reportDTO.uid}" rows="6"
										class="form-control mt-2">${reportDTO.entryText}</textarea>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		
			<c:if test="${not scribeUserDTO.finishedActivity}">
				<div class="row">
					<div class="col-12" id="submitReportBtn">
						<button class="btn btn-sm btn-secondary float-end" onClick="javascript:submitReport()">
							<fmt:message key="button.submitReport" />
						</button>
					</div>
				</div>
			</c:if>
			<hr>
		
			<form:form action="forceCompleteActivity.do" modelAttribute="learningForm" onsubmit="return confirmForceComplete();">
				<input type="hidden" name="scribeUserUID" value="${scribeUserDTO.uid}" />
				<form:hidden path="mode" />
		
				<div id="forceCompleteBtn">
					<button class="btn btn-primary float-end">
						<fmt:message key="button.forceComplete" />
					</button>
				</div>
			</form:form>
		
			<div id="agreeButton">
				<a id="agreeButton" class="btn btn-success float-start" onclick="javascript:submitApproval();"> 
					<fmt:message key="button.agree" />
				</a>
			</div>
		
			<div id="footer"></div>
		
		</lams:Page>
			
	</body>
</lams:html>