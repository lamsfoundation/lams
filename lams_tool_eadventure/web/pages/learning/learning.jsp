<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="eadventure" value="${sessionMap.eadventure}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

	<script type="text/javascript">
	<!--
		function gotoURL(){
 		    var reqIDVar = new Date();
			var gurl = "<c:url value="/learning/addurl.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
	      	showMessage(gurl);
	      	return false;
		}
		function gotoFile(){
 		    var reqIDVar = new Date();
 		    var gurl = "<c:url value="/learning/addfile.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime();
	      	showMessage(gurl);
	      	return false;
		}
		function checkNew(checkFinishedLock){
 		    var reqIDVar = new Date();
				
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();				
				
 		    return false;
		}
		function viewItem(toolContentID){
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&toolContentID=" + toolContentID;
			launchPopup(myUrl,"LearnerView");
		}
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function showMessage(url) {
			var area=document.getElementById("reourceInputArea");
			if(area != null){
				area.style.width="100%";
				area.src=url;
				area.style.display="block";
			}
		}
	-->        
    </script>
</lams:head>
<body class="stripes">


	<div id="content">
		<h1>
			<c:out value="${eadventure.title}" escapeXml="true"/>
		</h1>

		<p>
			<c:out value="${eadventure.instructions}" escapeXml="false"/>
		</p>

		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
				<div class="info">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<table cellspacing="0" class="alternative-color">
			<tr>
				<th width="70%">
					eAventure Game
				</th>
				<th align="center">
					<fmt:message key="label.completed" />
				</th>
			</tr>
			
				
				<tr>
					<td>
						<a href="#" onclick="viewItem(${eadventure.contentId})">
							<c:out value="${eadventure.title}" escapeXml="true"/> </a>
						
						
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${eadventure.complete}">
								<img src="<html:rewrite page='/includes/images/tick.gif'/>"
									border="0">
							</c:when>
							<c:otherwise>
								-
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
		</table>





		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
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
					<html:button property="FinishButton"
						onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when
						test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton"
							onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton"
							onclick="return finishSession()" styleClass="button">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${sessionMap.activityPosition.last}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
				 				</c:choose>
							</span>
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
