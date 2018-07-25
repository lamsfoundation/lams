<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>  
	<title>
		<fmt:message key="activity.title" />
	</title>
	<lams:css/>
	<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet" />
	<style type="text/css">
    	.dialog{display: none;}
    	.ui-dialog-titlebar-close{display: none;}
    	.ui-widget-overlay{opacity:0.9;}
    </style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
</lams:head>
