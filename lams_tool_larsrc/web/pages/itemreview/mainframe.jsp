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
	</style>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	
	<script>
		$(document).ready(function(){
			$('.item-panel').load("<c:url value="/itemReviewContent.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemIndex=${itemIndex}&itemUid=${itemUid}");
 		});

		function setIframeHeight(panel) {
			var frame = $('.embedded-content iframe', panel);
			if (frame.length === 0) {
				return;
			}
			frame = frame[0];
			
		    var doc = frame.contentDocument? frame.contentDocument : frame.contentWindow.document,
	        	body = doc.body,
	        	html = doc.documentElement.
	        	height = Math.max(body.scrollHeight, body.offsetHeight, 
	            				  html.clientHeight, html.scrollHeight, html.offsetHeight);
		    frame.style.height = height + "px";
		}
		
		function iframelyCallback(itemUid, response) {
			let panel = $('#item-content-' + itemUid);
			if (!response) {
				 $('.embedded-open-button', panel).removeClass('btn-default btn-sm pull-right').addClass('btn-primary');
				return;
			}
			
			if (response.title) {
				$('.embedded-title', panel).text(response.title);
			}
			if (response.description) {
				$('.embedded-description', panel).text(response.description);
			}
			if (response.html) {
				$('.embedded-content', panel).append(response.html);
				setIframeHeight(panel);
			}
		}
	</script>
</lams:head>

<body class="stripes">
	<lams:Page title="${title}" type="learner" hideProgressBar="true">
		<div class="item-panel"></div>
	</lams:Page>
</body>

</lams:html>