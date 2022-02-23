<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<style media="screen,projection" type="text/css">
	 	.item-content {
	 		padding: 5px;
	 	}
	 	
	 	.item-instructions {
	 		margin-bottom: 15px;
	 		padding-bottom: 10px;
	 		border-bottom: 1px solid #ddd;
	 	}
	 		 	
	 	.embedded-title {
	 		clear: both;
	 		font-weight: 500;
	 		font-size: larger;
	 	}
	 	
	 	.embedded-description {
	 		padding: 0.5em;
	 	}
	</style>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	
	<lams:JSImport src="includes/javascript/rsrcembed.js" relative="true" />
	<script>
		$(document).ready(function(){
			$('#item-panel').load("<c:url value="/itemReviewContent.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemIndex=${itemIndex}&itemUid=${itemUid}");
 		});
	</script>
</lams:head>

<body class="stripes">
	<lams:Page title="${title}" type="learner" hideProgressBar="true">
		<div id="item-panel"></div>
	</lams:Page>
</body>

</lams:html>