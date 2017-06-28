<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>

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
		var LAMS_URL = '<lams:LAMSURL/>',
			LEARNING_URL = LAMS_URL + 'learning/',
				
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="index.emailnotifications" var="EMAIL_NOTIFICATIONS_TITLE_VAR"/>
				EMAIL_NOTIFICATIONS_TITLE : '<c:out value="${EMAIL_NOTIFICATIONS_TITLE_VAR}" />',
			},
			
			orgId = ${param.organisationID};
	</script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/kumalive.js"></script>
</lams:head>
<body>
	<table>
		<tr>
			<td>
				<div id="raiseHandContainer" class="container-fluid">
					<h4>Raised hand</h4>
				</div>
				<div id="learnersContainer" class="container-fluid">
					<h4>Learners</h4>
				</div>
			</td>
			<td id="actionCell">
				<div id="teacher" class="speaker">
					<h3>Teacher</h3>
					<div class="profilePicture"></div>
					<div class="name"></div>
				</div>
				<div id="raiseHandPrompt">
					<h3>Raise hand</h3>
					<div class="fa fa-hand-stop-o fa-5x"></div>
				</div>
			</td>
		</tr>
	</table>
</body>
</lams:html>