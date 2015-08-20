<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="scratchie" value="${sessionMap.scratchie}" />
	<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<style type="text/css">
    	#scratches {margin: 40px 0px; border-spacing: 0;}
    	#scratches tr td { padding: 12px 15px; }
    	#scratches a, #scratches a:hover {border-bottom: none;}
    	.scartchie-image {border: 0;}
    </style>

	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript">

		function scratchItem(itemUid, answerUid){
			var id = '-' + itemUid + '-' + answerUid;
			
	        $.ajax({
	            url: '<c:url value="/learning/recordItemScratched.do"/>',
	            data: 'sessionMapID=${sessionMapID}&answerUid=' + answerUid,
	            dataType: 'json',
	            type: 'post',
	            success: function (json) {
	            	if (json == null) {
	            		return false;
	            	}
	            	
	            	if (json.answerCorrect) {
	            		//show animation
	            		$('#image' + id).attr("src", "<html:rewrite page='/includes/images/scratchie-correct-animation.gif'/>?reqID=" + (new Date()).getTime());
	            		
	            		//disable scratching
	            		$("[id^=imageLink-" + itemUid + "]").removeAttr('onclick'); 
	            		$("[id^=imageLink-" + itemUid + "]").css('cursor','default');
		            	$("a[id^=imageLink-" + itemUid + "]").not('#imageLink' + id).fadeTo(1300, 0.3);

	            	} else {
	            		
	            		//show animation, disable onclick
	            		$('#image' + id).attr("src", "<html:rewrite page='/includes/images/scratchie-wrong-animation.gif'/>?reqID=" + (new Date()).getTime());
	            		$('#imageLink' + id).removeAttr('onclick');
	            		$('#imageLink' + id).css('cursor','default');
	            	}
	            }
	       	});
        	
        	return false;
		}

		function finish(method){
			var numberOfAvailableScratches = $("[id^=imageLink-][onclick]").length;
			var	finishConfirmed = (numberOfAvailableScratches > 0) ? confirm("<fmt:message key="label.one.or.more.questions.not.completed"></fmt:message>") : true;
			
			if (finishConfirmed) {
				document.getElementById("finishButton").disabled = true;
				document.location.href ='<c:url value="/learning/' + method + '.do?sessionMapID=${sessionMapID}"/>';
				
				return false;
			}
		}
		
		var refreshIntervalId = null;
		if (${!isUserLeader && mode != "teacher"}) {
			refreshIntervalId = setInterval("refreshQuestionList();",3000);// Auto-Refresh every 3 seconds
		}
		
		function refreshQuestionList() {
			var url = "<c:url value="/learning/refreshQuestionList.do"/>",
				scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
			$("#questionListArea").load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					reqId: (new Date()).getTime()
				},
				function(){
					$("html,body").scrollTop(scrollTop);
				}
			);
		}
		
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${scratchie.title}" escapeXml="true"/>
		</h1>
		
		<h4>
			<fmt:message key="label.group.leader" >
				<fmt:param><c:out value="${sessionMap.groupLeaderName}" escapeXml="true"/></fmt:param>
			</fmt:message>
		</h4>

		<p style="font-style: italic;">
			<c:out value="${scratchie.instructions}" escapeXml="false"/>
		</p>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<div id="questionListArea">
			<%@ include file="questionlist.jsp"%>
		</div>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
