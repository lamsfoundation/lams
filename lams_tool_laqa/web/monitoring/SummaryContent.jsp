<%@ include file="/common/taglibs.jsp"%>

<div class="panel">
	<h4>
	    <c:out value="${content.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${content.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty listAllGroupsDTO}">
		<lams:Alert type="info" close="false">
			<fmt:message key="error.noLearnerActivity" />
		</lams:Alert>
	</c:if>
	
	<c:if test="${content.useSelectLeaderToolOuput && not empty listAllGroupsDTO}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="label.info.use.select.leader.outputs" />
		</lams:Alert>
	</c:if>
</div>

<c:forEach var="groupDto" items="${listAllGroupsDTO}" varStatus="status">

	<c:choose>
		<c:when test="${isGroupedActivity}">		  
		    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${sessionDto.sessionID}">
	        	<span class="panel-title collapsable-icon-left">
		        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${groupDto.sessionId}" 
							aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${groupDto.sessionId}" >
						<fmt:message key="group.label" />:	<c:out value="${groupDto.sessionName}" />
					</a>
				</span>
				<c:if test="${content.useSelectLeaderToolOuput and groupDto.numberOfLearners > 0 and not groupDto.sessionFinished}">
					<button type="button" class="btn btn-default btn-xs pull-right"
							onClick="javascript:showChangeLeaderModal(${groupDto.sessionId})">
						<fmt:message key='label.monitoring.change.leader'/>
					</button>
				</c:if>
	        </div>
	        
	        <div id="collapse${groupDto.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel"
				aria-labelledby="heading${groupDto.sessionId}">
		</c:when>
		<c:when test="${content.useSelectLeaderToolOuput and groupDto.numberOfLearners > 0 and not groupDto.sessionFinished}">
			<div style="text-align: right">
				<button type="button" class="btn btn-default" style="margin-bottom: 10px"
						onClick="javascript:showChangeLeaderModal(${groupDto.sessionId})">
					<fmt:message key='label.monitoring.change.leader'/>
				</button>
			</div>
		</c:when>
	</c:choose>
				  	
	<c:forEach var="questionDto" items="${questions}" varStatus="loop">
		<div class="panel panel-default"">
			<div class="panel-heading">
				<a href="javascript:launchPopup('<lams:WebAppURL/>monitoring/getPrintAnswers.do?questionUid=${questionDto.uid}&toolSessionID=${groupDto.sessionId}');"	
					id="printAnswers" class="btn btn-default btn-xs pull-right"><i class="fa fa-print"></i></a>
				<strong><c:if test="${questions.size() > 1}"><c:out value="${loop.index +1}"></c:out>.&nbsp;</c:if><c:out value="${questionDto.name}" escapeXml="false"/></strong>
				</br>
				<div><c:out value="${questionDto.description}" escapeXml="false"/></div>
			</div>
			<lams:TSTable numColumns="${content.allowRateAnswers ? (isCommentsEnabled ? 3 : 2) : (isCommentsEnabled ? 2 : 1)}"
						  dataId="data-session-id='${groupDto.sessionId}' data-question-uid='${questionDto.uid}'">
				<th title="<fmt:message key='label.sort.by.answer'/>">
					<fmt:message key="label.learning.answer"/>
				</th>
				
				<c:if test="${isRatingsEnabled}">
					<th title="<fmt:message key='label.sort.by.rating'/>">
						<fmt:message key="label.learning.rating" />
					</th>
				</c:if>
				
				<c:if test="${isCommentsEnabled}">
					<th>
						<fmt:message key="label.comment" />
					</th>
				</c:if>
			</lams:TSTable>
		</div>
	</c:forEach>
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
</c:forEach>
		
<%@include file="AdvanceOptions.jsp"%>

<lams:RestrictedUsageAccordian submissionDeadline="${submissionDeadline}"/>

<div id="change-leader-modals"></div>
