<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams.signup" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<lams:css/>

	<lams:JSImport src="includes/javascript/getSysInfo.js" />
	<lams:JSImport src="includes/javascript/openUrls.js" />
</lams:head>
    
<body class="stripes">
<lams:Page type="learner" title="">
<lams:css/>

<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>

<div class="panel-heading text-center">
	<img src="<lams:LAMSURL/>/images/svg/lamsv5_logo.svg" class="img-center" width="100px"></img>
</div>
<div class="panel-body text-center">
	<c:if test="${not empty error}">
		<lams:Alert type="danger" id="error-messages" close="false">
			<fmt:message key="success.errors" />,
					<c:out value="${error}" />
		</lams:Alert>
	</c:if>
	<div class="panel">
		<fmt:message key="success.msg.1" />
		<p class="voffset20">
			<a class="btn btn-sm btn-primary" href="<lams:LAMSURL />"><fmt:message key="success.login" /></a>
		</p>
	</div>
</div>
</lams:Page>
</body>
</lams:html>