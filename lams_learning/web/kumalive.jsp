<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL />includes/font-awesome/css/font-awesome.css" type="text/css" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" />
	<link rel="stylesheet" href="<lams:WebAppURL/>css/kumalive.css" type="text/css" />

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript">
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
				SPEAK_FINISH : '<c:out value="${SPEAK_FINISH_VAR}" />'
			};
			
	</script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/kumalive.js"></script>
</lams:head>
<body>
	<div id="initDiv">
		<span>
			<i class="fa fa-2x fa-refresh fa-spin text-primary"></i><br /><br />
			<fmt:message key="label.kumalive.wait.start"/>
		</span>
	</div>
	<div id="createKumaliveDiv">
		<span><fmt:message key="label.kumalive.name.enter"/><br/><br/></span>
		<input type="text" />
		<button class="btn btn-default"><fmt:message key="button.kumalive.create"/></button>
	</div>
	<div id="closedDiv">
		<span><fmt:message key="label.kumalive.closed"/></span>
	</div>
	<div id="mainDiv" class="container-fluid">
		<div class="row">
			<div id="actionCell" class="col-sm-11">
				<div id="teacher" class="speaker">
					<h3><fmt:message key="label.kumalive.teacher"/></h3>
					<div class="profilePicture"></div>
					<div class="name"></div>
					<button id="raiseHandPromptButton" class="btn btn-default"><fmt:message key="button.kumalive.ask"/></button><br />
					<button id="finishButton" class="btn btn-default"><fmt:message key="button.kumalive.finish.kumalive"/></button>
				</div>
				<div id="raiseHandPrompt">
					<i class="fa fa-hand-stop-o"></i><br />
					<button id="raiseHandButton" class="btn btn-default"><fmt:message key="button.kumalive.raise"/></button>
					<button id="downHandButton" class="btn btn-default"><fmt:message key="button.kumalive.putdown"/></button>
					<button id="downHandPromptButton" class="btn btn-default"><fmt:message key="button.kumalive.finish.question"/></button>
				</div>
				<div id="score" class="score">
					<p><fmt:message key="label.kumalive.mark"/> <span></span></p>
					<i title="<fmt:message key="label.kumalive.mark.great"/>" class="scoreGood fa fa-smile-o"></i>
					<i title="<fmt:message key="label.kumalive.mark.ok"/>" class="scoreNeutral fa fa-meh-o"></i>
					<i title="<fmt:message key="label.kumalive.mark.bad"/>" class="scoreBad fa fa-frown-o"></i>
					<i title="<fmt:message key="label.kumalive.mark.cancel"/>" class="scoreNone fa fa-times"></i>
				</div>
			</div>
			<div id="learnersCell" class="col-sm-1">
				<div id="raiseHandContainer">
					<h4><fmt:message key="label.kumalive.raised.hands"/></h4>
				</div>
				<div id="learnersContainer">
					<h4><fmt:message key="label.kumalive.learners"/></h4>
				</div>
			</div>
		</div>
	</div>
</body>
</lams:html>