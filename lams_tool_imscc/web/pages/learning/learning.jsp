<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />

	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="commonCartridge" value="${sessionMap.commonCartridge}" />
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishSession);
		
		function checkNew() {
		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();
		    return false;
		}

		function viewItem(itemUid) {
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		
		function finishSession() {
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
		}  
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${commonCartridge.title}">

		<div class="panel">
			<c:out value="${commonCartridge.instructions}" escapeXml="false"/>
		</div>
		
		<c:if test="${commonCartridge.miniViewCommonCartridgeNumber > 0}">
			<lams:Alert type="warning" id="lock-on-finish" close="false">
				${commonCartridge.miniViewNumberStr}
			</lams:Alert>
		</c:if>

		<lams:errors/>

		<div class="table-responsive">
			<table class="table table-hover table-sm">
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
		
		<%--Finish buttons--------------------------------------------------%>

		<c:if test="${mode != 'teacher'}">
			<div class="mt-2 float-end">
						<a href="#nogo" name="FinishButton" id="finishButton" class="btn btn-primary">
							<span class="na">
								<c:choose>
				 					<c:when test="${sessionMap.isLastActivity}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
				 				</c:choose>
							</span>
						</a>
			</div>
		</c:if>

	</lams:Page>

</body>
</lams:html>
