<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="label.kumalive.title"/></title>
	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL />includes/font-awesome/css/font-awesome.css" type="text/css" />
	<link rel="stylesheet" href="<lams:WebAppURL/>css/kumalive.css" type="text/css" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/d3.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/chart.js"></script>
	<script type="text/javascript">
		"use strict"
		
		var	orgId = ${param.organisationID},
			userId = <lams:user property="userID"/>,
			role = '${param.role}',
			
			LAMS_URL = '<lams:LAMSURL/>',
			LEARNING_URL = LAMS_URL + 'learning/',
			KUMALIVE_URL = LEARNING_URL + 'kumalive.jsp?organisationID='+ orgId,
				
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="label.kumalive.title" var="KUMALIVE_TITLE_VAR"/>
				KUMALIVE_TITLE : '<c:out value="${KUMALIVE_TITLE_VAR}" />',
				<fmt:message key="message.kumalive.finish.kumalive" var="FINISH_KUMALIVE_MESSAGE_VAR"/>
				FINISH_KUMALIVE_MESSAGE : decoderDiv.html('<c:out value="${FINISH_KUMALIVE_MESSAGE_VAR}" />').text(),
				<fmt:message key="message.kumalive.finish.kumalive.confirm" var="FINISH_KUMALIVE_CONFIRM_VAR"/>
				FINISH_KUMALIVE_CONFIRM : decoderDiv.html('<c:out value="${FINISH_KUMALIVE_CONFIRM_VAR}" />').text(),
				<fmt:message key="message.kumalive.speak.not.raised.hand" var="SPEAK_CONFIRM_VAR"/>
				SPEAK_CONFIRM : decoderDiv.html('<c:out value="${SPEAK_CONFIRM_VAR}" />').text(),
				<fmt:message key="label.kumalive.finish.speak" var="SPEAK_FINISH_VAR"/>
				SPEAK_FINISH : '<c:out value="${SPEAK_FINISH_VAR}" />',
				<fmt:message key="message.kumalive.poll.finish.confirm" var="POLL_FINISH_CONFIRM_VAR"/>
				POLL_FINISH_CONFIRM : decoderDiv.html('<c:out value="${POLL_FINISH_CONFIRM_VAR}" />').text(),
				<fmt:message key="message.kumalive.poll.release.votes.confirm" var="POLL_RELEASE_VOTES_CONFIRM_VAR"/>
				POLL_RELEASE_VOTES_CONFIRM : decoderDiv.html('<c:out value="${POLL_RELEASE_VOTES_CONFIRM_VAR}" />').text(),
				<fmt:message key="message.kumalive.poll.release.voters.confirm" var="POLL_RELEASE_VOTERS_CONFIRM_VAR"/>
				POLL_RELEASE_VOTERS_CONFIRM : decoderDiv.html('<c:out value="${POLL_RELEASE_VOTERS_CONFIRM_VAR}" />').text(),
				<fmt:message key="label.kumalive.poll.missing.voters" var="MISSING_VOTERS_VAR"/>
				MISSING_VOTERS : '<c:out value="${MISSING_VOTERS_VAR}" />'
			};
			
	</script>
	<lams:JSImport src="includes/javascript/kumalive.js" relative="true" />
</lams:head>
<body>
	<div id="initDiv">
		<span>
			<i class="fa fa-2x fa-refresh fa-spin text-primary"></i><br /><br />
			<fmt:message key="label.kumalive.wait.start"/>
		</span>
	</div>
	<div id="createKumaliveDiv">
		<input type="text" class="form-control" placeholder="<fmt:message key='label.kumalive.name.enter'/>" />
		<button class="btn btn-primary"><fmt:message key="button.kumalive.create"/></button>
		<br />
		<div id="rubrics" class="panel">
			<div class="panel-body">
				<h4><fmt:message key="label.kumalive.rubric.choose"/></h4>
			</div>
		</div>
	</div>
	<div id="closedDiv">
		<span><fmt:message key="label.kumalive.closed"/></span>
	</div>
	<div id="mainDiv" class="container-fluid">
		<div class="row">
			<div id="actionCell" class="col-md-1">
				<div id="teacher" class="speaker">
					<h3><fmt:message key="label.kumalive.teacher"/></h3>
					<div class="profilePicture"></div>
					<div class="name"></div>
					<button id="raiseHandPromptButton" class="btn btn-default"><fmt:message key="button.kumalive.ask"/></button><br />
					<button class="pollButton btn btn-default"><fmt:message key="button.kumalive.poll.create"/></button><br />
					<button id="finishButton" class="btn btn-default"><fmt:message key="button.kumalive.finish.kumalive"/></button>
				</div>
				<div id="raiseHandPrompt">
					<i class="fa fa-hand-stop-o"></i><br />
					<button id="raiseHandButton" class="btn btn-default"><fmt:message key="button.kumalive.raise"/></button>
					<button id="downHandButton" class="btn btn-default"><fmt:message key="button.kumalive.putdown"/></button>
					<button id="downHandPromptButton" class="btn btn-default"><fmt:message key="button.kumalive.finish.question"/></button>
					<button class="pollButton btn btn-default"><fmt:message key="button.kumalive.poll.create"/></button><br />
				</div>
				<div id="score" class="score">
					<p></p>
					<div class="scoreButtons">
						<i title="<fmt:message key="label.kumalive.mark.great"/>" class="scoreGood fa fa-smile-o"></i>
						<i title="<fmt:message key="label.kumalive.mark.ok"/>" class="scoreNeutral fa fa-meh-o"></i>
						<i title="<fmt:message key="label.kumalive.mark.bad"/>" class="scoreBad fa fa-frown-o"></i>
					</div>
					<hr />
				</div>
			</div>
			<div id="pollCell" class="col-md-1">
				<div id="pollSetup">
					<h3><fmt:message key='button.kumalive.poll'/></h3>
					<div id="pollSetupQuestionGroup" class="form-group">
						<label for="pollSetupQuestion" class="control-label">
							<h4><fmt:message key="label.kumalive.poll.question"/></h4>
						</label>
						<input id="pollSetupQuestion" type="text" class="form-control"
							   placeholder="<fmt:message key='label.kumalive.poll.question.tip'/>"
							   maxlength="250" required/>
					</div>
					<div class="form-group">
						<label for="pollSetupAnswer" class="control-label">
							<h4><fmt:message key="label.kumalive.poll.answer"/></h4>
						</label>
						<select id="pollSetupAnswer" class="form-control">
							<option value="true-false">
								<fmt:message key='label.kumalive.poll.answer.true'/>,
								<fmt:message key='label.kumalive.poll.answer.false'/>
							</option>
							<option value="yes-no">
								<fmt:message key='label.kumalive.poll.answer.yes'/>,
								<fmt:message key='label.kumalive.poll.answer.no'/>
							</option>
							<option value="positive-negative">
								<fmt:message key='label.kumalive.poll.answer.positive'/>,
								<fmt:message key='label.kumalive.poll.answer.negative'/>
							</option>
							<option value="custom">
								<fmt:message key='label.kumalive.poll.answer.custom'/>
							</option>
						</select>
					</div>
					<div id="pollSetupAnswerCustomGroup" class="form-group">
						<input id="pollSetupAnswerCustom" type="text" class="form-control"
							   placeholder="<fmt:message key='label.kumalive.poll.answer.custom.tip'/>" required/>
						<span id="pollSetupAnswerCustomParseError" class="help-block">
							<fmt:message key='label.kumalive.poll.answer.custom.error.syntax'/>
						</span>
						<span id="pollSetupAnswerCustomCountError" class="help-block">
							<fmt:message key='label.kumalive.poll.answer.custom.error.count'/>
						</span>
					</div>
					<div id="pollSetupButtons">
						<button id="pollSetupStartButton" class="btn btn-primary">
							<fmt:message key='button.kumalive.poll.start'/>
						</button><br />
						<button id="pollSetupCancelButton" class="btn btn-default">
							<fmt:message key='label.cancel.button'/>
						</button>
					</div>
				</div>
				<div id="pollRun">
					<h3><fmt:message key='button.kumalive.poll'/></h3>
					<h4 id="pollRunQuestion"></h4>
					<div id="pollRunAnswerRadioTemplate" class="radio">
  						<label><input type="radio" name="pollAnswer" /></label>
					</div>
					<div id="pollRunAnswerRadios"></div>
					<button id="pollRunVoteButton" class="btn btn-primary">
						<fmt:message key='button.kumalive.poll.vote'/>
					</button>
					<ul id="pollRunAnswerList" class="list-group"></ul>
					<button id="pollRunReleaseVotesButton" class="btn btn-default">
						<fmt:message key='button.kumalive.poll.release.votes'/>
					</button>
					<button id="pollRunReleaseVotersButton" class="btn btn-default">
						<fmt:message key='button.kumalive.poll.release.voters'/>
					</button>
					<button id="pollRunChartSwitch" class="btn btn-default">
						<fmt:message key='button.kumalive.poll.chart.switch'/>
					</button>
					<button id="pollRunFinishButton" class="btn btn-default">
						<fmt:message key='button.kumalive.poll.finish'/>
					</button>
					<button id="pollRunCloseButton" class="btn btn-default">
						<fmt:message key='button.kumalive.poll.close'/>
					</button>
					<div id="pollRunChart">
						<h3><fmt:message key='label.kumalive.poll.results'/></h3>
						<h4><fmt:message key='label.kumalive.poll.votes.total'/>&nbsp;<span id="pollRunTotalVotes"></span></h4>
						<div id="pollRunChartPie"></div>
						<div id="pollRunChartBar"></div>
					</div>
				</div>
			</div>
			<div id="learnersCell">
				<div id="raiseHandContainer">
					<h3><fmt:message key="label.kumalive.raised.hands"/></h3>
				</div>
				<div id="learnersContainer">
					<h3><fmt:message key="label.kumalive.learners"/></h3>
				</div>
			</div>
		</div>
	</div>
</body>
</lams:html>