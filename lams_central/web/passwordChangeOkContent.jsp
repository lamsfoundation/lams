<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<link rel="stylesheet" href="${lams}css/components.css">
    <link rel="stylesheet" href="${lams}includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>

<body class="component no-decoration">
	<div class="container">
		<div class="col-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3 mt-3 mx-auto">
			<lams:Alert5 type="info" id="msgPasswordChanged">
				<fmt:message key="msg.password.changed"/>
			</lams:Alert5>
					
			<div class="col-12 text-end">
				<a class="btn btn-sm btn-primary" id="saveButton"
						href="<lams:LAMSURL/>${empty redirectURL ? 'index.do' : redirectURL}" role="button">
					<i class="fa-regular fa-circle-check me-1"></i>
					Ok
				</a>
			</div>
		</div>
	</div>
</body>
</lams:html>