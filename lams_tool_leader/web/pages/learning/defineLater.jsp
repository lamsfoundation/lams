<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
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
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" />
		<style type="text/css">
	    	.dialog{display: none;}
	    	.ui-dialog-titlebar-close{display: none;}
	    	.ui-widget-overlay{opacity:0.9;}
	    </style>
	
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	</lams:head>
	<body class="stripes">
		<c:set var="title" scope="request">
			<fmt:message key="activity.title" />
		</c:set>
		
		<lams:Page type="learner" title="${title}">
			<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
		</lams:Page>

		<div class="footer">
		</div>					
	</body>
</lams:html>