<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}"/>
	<c:set var="survey" value="${sessionMap.survey}"/>
	<c:set var="finishedLock" value="${sessionMap.finishedLock}"/>
	
	<script type="text/javascript">
	<!--

		function checkNew(){
 		    var reqIDVar = new Date();
			document.location.href = "<c:url value="/learning/start.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&reqID="+reqIDVar.getTime();
 		    return false;
		}
		function viewItem(itemUid){
			var myUrl = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		function completeItem(itemUid){
			document.location.href = "<c:url value="/learning/completeItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=" + itemUid;
			return false;
		}
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
	-->        
    </script>
</head>
<body>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			${survey.title}
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<!--closes header-->

		<div id="content-learner">
			<h2>
				${survey.instructions}
			</h2>

			<%@ include file="/common/messages.jsp"%>
			<table cellpadding="0" cellspacing="0" class="alternative-color">
				<tr>
					<th width="255px">
						<fmt:message key="label.resoruce.to.review" />
					</th>
					<th width="75px">
						<fmt:message key="label.completed" />
					</th>
					<th align="center">

					</th>
				</tr>
				<c:forEach var="item" items="${sessionMap.surveyList}">
					<tr>
						<td align="center">
							${item.title}
							<c:if test="${!item.createByAuthor}">
								[${item.createBy.loginName}]
							</c:if>
						</td>
						<td align="center">
							<c:if test="${item.complete}">
								<img src="<html:rewrite page='/includes/images/tick.gif'/>" border="0">
							</c:if>

						</td>

						<td>
							<c:if test="${mode != 'teacher' && (not finishedLock)}">
								<a href="javascript:;" onclick="return completeItem(${item.uid})" class="button">
									<fmt:message key="label.completed"/>
								</a>
							</c:if>
							&nbsp;
							<c:if test="${not finishedLock}">
								<a href="javascript:;"  onclick="viewItem(${item.uid})" class="button">
									<fmt:message key="label.view"/>
								</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
				<c:if test="${survey.miniViewSurveyNumber > 0}">
					<tr>
						<td colspan="3" align="left">
							<b>${survey.miniViewNumberStr}</b>
						</td>
					</tr>
				</c:if>
			</table>
			<div class="left-buttons">
				<html:button property="checkNewButton" onclick="return checkNew()" disabled="${finishedLock}" styleClass="button">
					<fmt:message key="label.check.for.new" />
				</html:button>
			</div>
			<c:if test="${mode != 'teacher'}">
				<div class="right-buttons">
					<c:choose>
						<c:when test="${sessionMap.reflectOn}">
							<html:button property="FinishButton"  onclick="return continueReflect()" disabled="${finishedLock}"  styleClass="button">
								<fmt:message key="label.continue" />
							</html:button>
						</c:when>
						<c:otherwise>
							<html:button property="FinishButton"  onclick="return finishSession()" disabled="${finishedLock}"  styleClass="button">
								<fmt:message key="label.finished" />
							</html:button>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<div style="height: 70px"></div> 
			
				<%-- end mode != teacher --%>
			</c:if>
		</div>  <!--closes content-->
		
		<div id="footer-learner">
		</div>
		<!--closes footer-->

	</div><!--closes page-->
</body>
</html:html>

