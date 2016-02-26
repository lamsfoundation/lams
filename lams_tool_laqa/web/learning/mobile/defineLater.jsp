<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />	
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>&nbsp;</h1>
	</div><!-- /header -->

	<div data-role="content">
		<lams:DefineLater defineLaterMessageKey="error.defineLater" />
	</div>

	<div data-role="footer" data-theme="b">
		<h2>&nbsp;</h2>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
