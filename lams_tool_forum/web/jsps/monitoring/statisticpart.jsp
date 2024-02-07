<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:if test="${empty topicList}">
	<lams:Alert5 type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert5>
</c:if>
	
<c:forEach var="element" items="${topicList}">
	<c:set var="toolSessionDto" value="${element.key}" />
	<c:set var="sessionTopicList" value="${element.value}" />
	<c:forEach var="totalMsg" items="${totalMessage}">
		<c:if test="${totalMsg.key eq toolSessionDto.sessionID}">
			<c:set var="sessionTotalMessage" value="${totalMsg.value}" />
		</c:if>
	</c:forEach>
	<c:forEach var="avaMark" items="${markAverage}">
		<c:if test="${avaMark.key eq toolSessionDto.sessionID}">
			<c:set var="sessionMarkAverage" value="${avaMark.value}" />
		</c:if>
	</c:forEach>

	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="lcard card-secondary" >
        <div class="card-header" id="heading${toolSessionDto.sessionID}">
			<fmt:message key="message.session.name" />: <c:out value="${toolSessionDto.sessionName}" />
        </div>

        <div class="card-body p-2">
	</c:if>

	<div class="fs-4 my-3">
		<fmt:message key="monitoring.tab.summary"/>
	</div>
	
	<div class="ltable table-striped">
		<div class="row">
			<div class="col-2">
				<fmt:message key="lable.monitoring.statistic.total.message" />
			</div>
			<div class="col">
				<c:out value="${sessionTotalMessage}" />
			</div>
		</div>
		<div class="row">
			<div class="col-2">
				<fmt:message key="label.monitoring.statistic.average.mark" />
			</div>
			
			<div class="col">
				<fmt:formatNumber value="${sessionMarkAverage}"  maxFractionDigits="2"/>
			</div>
		</div>
	</div>

	<div class="ltable table-striped mt-5">
		<div class="row">
			<div class="col">
				<fmt:message key="lable.topic.title.subject" />
			</div>
			<div class="col-2">
				<fmt:message key="lable.topic.title.message.number" />
			</div>
			<div class="col-2">
				<fmt:message key="lable.topic.title.average.mark" />
			</div>
		</div>
		
		<c:forEach items="${sessionTopicList}" var="topic">
			<div class="row align-items-center">
				<div class="col">
					<c:out value="${topic.message.subject}" />
					
					<c:set var="viewtopic">
						<lams:WebAppURL />learning/viewTopic.do?sessionMapID=${sessionMapID}&toolSessionID=${toolSessionDto.sessionID}&topicID=${topic.message.uid}&mode=teacher&pageLastId=0
					</c:set>
					<button type="button" onclick="launchPopup('${viewtopic}');" class="btn btn-sm btn-light ms-2">
						<fmt:message key="page.title.monitoring.view.topic"/>
						<i class="fa-solid fa-arrow-up-right-from-square ms-2"></i>
					</button>
				</div>
				<div class="col-2">
					<c:out value="${topic.message.replyNumber+1}" />
				</div>
				<div class="col-2">
					<fmt:formatNumber value="${topic.mark}"  maxFractionDigits="2"/>
				</div>
			</div>
		</c:forEach>
	</div>

	<c:if test="${sessionMap.isGroupedActivity}">	
		</div>
		</div>
	</c:if>
</c:forEach>
