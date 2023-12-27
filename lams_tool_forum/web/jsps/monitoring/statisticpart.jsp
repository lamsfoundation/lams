<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:if test="${empty topicList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
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
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${toolSessionDto.sessionID}">
			<span class="panel-title">
				<fmt:message key="message.session.name" />: <c:out value="${toolSessionDto.sessionName}" />
			</span>
        </div>

        <div class="panel-body">
	</c:if>

	<h4><fmt:message key="monitoring.tab.summary"/></h4>
	<table class="table table-condensed table-no-border">
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="lable.monitoring.statistic.total.message" />
			</td>
			<td>
				<c:out value="${sessionTotalMessage}" />
			</td>
			
		</tr>
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="label.monitoring.statistic.average.mark" />
			</td>
			
			<td>
				<fmt:formatNumber value="${sessionMarkAverage}"  maxFractionDigits="2"/>
			</td>
		</tr>
	</table>

	<table class="table table-condensed table-striped">
		<tr>
			<th scope="col" width="50%">
				<fmt:message key="lable.topic.title.subject" />
			</th>
			<th scope="col" width="25%">
				<fmt:message key="lable.topic.title.message.number" />
			</th>
			<th scope="col" width="25%">
				<fmt:message key="lable.topic.title.average.mark" />
			</th>
		</tr>
		<c:forEach items="${sessionTopicList}" var="topic">
			<tr>
				<td valign="MIDDLE" width="48%">
					<c:set var="viewtopic">
						<lams:WebAppURL />learning/viewTopic.do?sessionMapID=${sessionMapID}&toolSessionID=${toolSessionDto.sessionID}&topicID=${topic.message.uid}&mode=teacher&pageLastId=0
					</c:set>
					<a href="javascript:launchPopup('${viewtopic}');">
						<c:out value="${topic.message.subject}" />
					</a>
				</td>
				<td>
					<c:out value="${topic.message.replyNumber+1}" />
				</td>
				<td>
					<fmt:formatNumber value="${topic.mark}"  maxFractionDigits="2"/>
				</td>
			</tr>
		</c:forEach>
	</table>

	<c:if test="${sessionMap.isGroupedActivity}">	
		</div>
		</div>
	</c:if>

</c:forEach>

