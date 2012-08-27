<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="scratchie" value="${sessionMap.scratchie}" />

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

	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui-1.8.11.custom.min.js"></script>
	<script type="text/javascript">
	<!--

		function scratchItem(itemUid, answerUid){
			var id = '-' + itemUid + '-' + answerUid;
			
	        $.ajax({
	        	async: false,
	            url: '<c:url value="/learning/scratchItem.do"/>',
	            data: 'sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&answerUid=' + answerUid,
	            dataType: 'json',
	            type: 'post',
	            success: function (json) {
	            	if (json.answerCorrect) {
	            		//show animation
	            		$('#image' + id).attr("src", "<html:rewrite page='/includes/images/scratchie-correct-animation.gif'/>?reqID=" + (new Date()).getTime());
	            		
	            		//disable scratching
	            		$("[id^=imageLink-" + itemUid + "]").removeAttr('onclick'); 
	            		$("[id^=imageLink-" + itemUid + "]").css('cursor','default');
	            		$("[id^=image-" + itemUid + "]").not("img[src*='scratchie-correct-animation.gif']").not("img[src*='scratchie-correct.gif']").fadeTo(1300, 0.3);

	            	} else {
	            		
	            		//show animation, disable onclick
	            		$('#image' + id).attr("src", "<html:rewrite page='/includes/images/scratchie-wrong-animation.gif'/>?reqID=" + (new Date()).getTime());
	            		$('#imageLink' + id).removeAttr('onclick');
	            		$('#imageLink' + id).css('cursor','default');
	            	}
	            }
	       	});
		}

		function finishSession(){
			var numberOfAvailableScratches = $("[id^=imageLink-][onclick]").length;
			var	finishConfirmed = (numberOfAvailableScratches > 0) ? confirm("<fmt:message key="label.one.or.more.questions.not.completed"></fmt:message>") : true;
			
			if (finishConfirmed) {
				document.getElementById("finishButton").disabled = true;
				document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
				return false;
			}
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
	-->        
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			${scratchie.title}
		</h1>

		<p>
			${scratchie.instructions}
		</p>

		<%@ include file="/common/messages.jsp"%>

		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
		<h3>${item.title}</h3>
		<h4>${item.description}</h4>

		<table id="scratches" class="alternative-color">
			<c:forEach var="answer" items="${item.answers}" varStatus="status">
				<tr id="tr${answer.uid}">
					<td style="width: 40px;">
						<c:choose>
							<c:when test="${answer.scratched && answer.correct}">
								<img src="<html:rewrite page='/includes/images/scratchie-correct.png'/>" class="scartchie-image">
							</c:when>
							<c:when test="${answer.scratched && !answer.correct}">
								<img src="<html:rewrite page='/includes/images/scratchie-wrong.png'/>" id="image-${item.uid}-${answer.uid}" class="scartchie-image">
							</c:when>
							<c:when test="${sessionMap.userFinished || item.unraveled}">
								<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image">
							</c:when>
							<c:otherwise>
								<a href="#nogo" onclick="scratchItem(${item.uid}, ${answer.uid}); return false;" id="imageLink-${item.uid}-${answer.uid}">
									<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image" id="image-${item.uid}-${answer.uid}" />
								</a>
							</c:otherwise>
						</c:choose>
					</td>
					
					<td style="vertical-align: middle;">
						${answer.description} 
					</td>
				</tr>
			</c:forEach>
		</table>
		
		</c:forEach>


		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>
					${sessionMap.reflectInstructions}
				</h2>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${mode != 'teacher'}">
					<html:button property="finishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="finishButton" styleId="finishButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="finishButton" styleId="finishButton" onclick="return finishSession()" styleClass="button">
							<span class="nextActivity"><fmt:message key="label.finished" /></span>
						</html:link>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
