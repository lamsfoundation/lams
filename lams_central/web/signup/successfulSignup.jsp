<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>


<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams.signup" /></title>
	<lams:css/>
	<script language="JavaScript" type="text/JavaScript" src="<lams:WebAppURL />includes/javascript/changeStyle.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
<lams:Page type="learner" title="">
<lams:css/>

<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>

<div class="panel-heading text-center">
	<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg" class="img-center" width="200px"></img>
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





