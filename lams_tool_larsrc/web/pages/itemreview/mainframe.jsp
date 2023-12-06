<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />

<lams:PageLearner title="${title}" toolSessionID="" hideHeader="true">
	<style type="text/css">
	 	.item-content {
	 		padding: 5px;
	 	}
	 		 	
	 	.embedded-title {
	 		clear: both;
	 		font-weight: 500;
	 		font-size: larger;
	 	}
	 	
	 	.embedded-description {
	 		padding: 0.5em;
	 	}
	 	
	 	.embedded-file {
	 		text-align: center;
	 		margin: auto;
	 	}
	 	
	 	.embedded-file img {
	 		max-width: 800px;
	 	}
	 	
	 	.embedded-file video {
	 		width: 100%;
	 	}
	 	
	 	.embedded-file embed {
	 		width: 100%;
	 		min-height: 500px;
	 	}
	 	
	 	.embedded-content iframe {
	 		border: 0;
	 		width: 100%;
	 		height: 100%;
	 		min-height: 500px;
	 	}
	</style>
	
	<lams:JSImport src="includes/javascript/rsrcembed.js" relative="true" />
	<script>
		$(document).ready(function(){
			$('#item-panel').load("<c:url value="/itemReviewContent.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemIndex=${itemIndex}&itemUid=${itemUid}");
 		});
	</script>

	<div id="container-main">
		<div id="item-panel"></div>
	</div>
</lams:PageLearner>
