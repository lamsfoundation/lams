<%@ include file="/common/taglibs.jsp" %>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<c:set var="nextInstructionUrl"><c:url value="/nextInstruction.do"/>?mode=${mode}&itemIndex=${itemIndex}&itemUid=${itemUid}&toolSessionID=${toolSessionID}&sessionMapID=${sessionMapID}</c:set>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		
	<script type="text/javascript">
		$(document).ready(function(){
			$('#headerFrame').load('${nextInstructionUrl}');
		});
		
		function setIframeHeight() {
			var rscFrame = document.getElementById('resourceFrame');
		    var doc = rscFrame.contentDocument? rscFrame.contentDocument : rscFrame.contentWindow.document;
	        var body = doc.body;
	        var html = doc.documentElement;
	        var height = Math.max( body.scrollHeight, body.offsetHeight, 
	            html.clientHeight, html.scrollHeight, html.offsetHeight );
		    rscFrame.style.height = height + "px";
		}
	</script>
</lams:head>

<body class="stripes">

	<lams:Page title="" type="learner" usePanel="false" hideProgressBar="${!sessionMap.runAuto}">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div id="headerFrame"></div>
			</div>
			<div class="panel-body" style="height:100vh;">
				<iframe src="<c:url value='${commonCartridgeItemReviewUrl}'/>" id="commonCartridgeFrame" style="border:0px;width:100%;height:100%;" onload="setIframeHeight()"></iframe>
				</div>
		</div>
	</lams:Page>

</body>	

</lams:html>
			