<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>

	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="commonCartridge" value="${sessionMap.commonCartridge}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

	<script type="text/javascript">
		function checkNew(checkFinishedLock) {
		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();
		    return false;
		}

		function viewItem(itemUid) {
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		
		function finishSession() {
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		
		function continueReflect() {
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}     
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${commonCartridge.title}">

		<div class="panel">
			<c:out value="${commonCartridge.instructions}" escapeXml="false"/>
		</div>

		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert type="danger" id="lock-on-finish" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		
		<c:if test="${commonCartridge.miniViewCommonCartridgeNumber > 0}">
			<lams:Alert type="warning" id="lock-on-finish" close="false">
				${commonCartridge.miniViewNumberStr}
			</lams:Alert>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<div class="table-responsive">
			<table class="table table-hover table-condensed">
				<thead>
					<tr>
						<th width="70%">
							<fmt:message key="label.resoruce.to.review" />
						</th>
						<th class="text-center">
							<fmt:message key="label.completed" />
						</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="item" items="${sessionMap.commonCartridgeList}">
						<tr>
							<td>
								<a href="#" onclick="viewItem(${item.uid})"> <c:out value="${item.title}"/> </a>
							</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${item.complete}">
										<i class="fa fa-lg fa-check text-success"></i>
									</c:when>
									<c:otherwise>
										<i class="fa fa-lg fa-minus"></i>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<%--Reflection--------------------------------------------------%>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="panel panel-default voffset10">
				
				<div class="panel-heading panel-title">
					<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
				</div>

				<div class="panel-body">
					<c:choose>
						<c:when test="${empty sessionMap.reflectEntry}">
							<em> <fmt:message key="message.no.reflection.available" /></em>
						</c:when>
						<c:otherwise>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</c:otherwise>
					</c:choose>
	
					<c:if test="${mode != 'teacher'}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-default loffset10">
							<fmt:message key="label.edit" />
						</html:button>
					</c:if>
				</div>
			</div>
		</c:if>
		
		<%--Finish buttons--------------------------------------------------%>

		<c:if test="${mode != 'teacher'}">
			<div class="voffset10 pull-right">
				<c:choose>
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-primary">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()" styleClass="btn btn-primary">
							<span class="na">
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

	</lams:Page>

</body>
</lams:html>
