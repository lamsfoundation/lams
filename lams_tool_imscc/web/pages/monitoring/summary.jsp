<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<div class="panel">
	<h4>
	  <c:out value="${sessionMap.commonCartridge.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${sessionMap.commonCartridge.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
</div>

<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>


<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
	<c:set var="groupSize" value="${fn:length(group)}" />
		
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${group[0].sessionId}">
	        	<span class="panel-title collapsable-icon-left">
	        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${group[0].sessionId}" 
							aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${group[0].sessionId}" >
						<fmt:message key="monitoring.label.group" />:	<c:out value="${group[0].sessionName}" />
					</a>
				</span>
	        </div>
	        
        <div id="collapse${group[0].sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" 
	       		role="tabpanel" aria-labelledby="heading${sessionSummary.sessionId}">
	</c:if>
				
	<table class="table">
		<tr>
			<th width="18%" align="center">
				<fmt:message key="monitoring.label.type" />
			</th>
			<th width="25%">
				<fmt:message key="monitoring.label.title" />
			</th>
			<th width="22%" align="center">
				<fmt:message key="monitoring.label.number.learners" />
			</th>
			<th width="15%">
				<!--hide/show-->
			</th>
		</tr>
				
		<c:forEach var="item" items="${group}" varStatus="status">
			<c:if test="${item.itemUid == -1}">
				<tr>
					<td colspan="4">
						<div class="align-left">
							<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
						</div>
					</td>
				</tr>
			</c:if>
			<c:if test="${item.itemUid != -1}">
				<tr>
					<td>
						<c:choose>
							<c:when test="${item.itemType == 1}">
								<fmt:message key="label.authoring.basic.resource.url" />
							</c:when>
							<c:when test="${item.itemType == 2}">
								<fmt:message key="label.authoring.basic.resource.file" />
							</c:when>
							<c:when test="${item.itemType == 3}">
								<fmt:message key="label.authoring.basic.resource.website" />
							</c:when>
							<c:when test="${item.itemType == 4}">
								<fmt:message key="label.authoring.basic.resource.learning.object" />
							</c:when>
						</c:choose>
					</td>
					<td>
						<a href="javascript:;" onclick="viewItem(${item.itemUid},'${sessionMapID}')"><c:out value="${item.itemTitle}" escapeXml="true"/></a>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.viewNumber > 0}">
								<c:set var="listUrl">
									<c:url value='/monitoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}' />
								</c:set>
								<a href="#" onclick="launchPopup('${listUrl}','listuser')"> 
									<c:out value="${item.viewNumber}" escapeXml="true"/>
								<a>
							</c:when>
							<c:otherwise>
								0
							</c:otherwise>
						</c:choose>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.itemHide}">
								<a href="<c:url value='/monitoring/showitem.do'/>?sessionMapID=${sessionMapID}&itemUid=${item.itemUid}" 
										class="btn btn-default btn-xs loffset5"> 
									<fmt:message key="monitoring.label.show" /> 
								</a>
							</c:when>
							<c:otherwise>
								<a href="<c:url value='/monitoring/hideitem.do'/>?sessionMapID=${sessionMapID}&itemUid=${item.itemUid}" 
										class="btn btn-default btn-xs"> 
									<fmt:message key="monitoring.label.hide" /> 
								</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:if>
			
				<%-- Reflection list  --%>
				<c:if test="${sessionMap.commonCartridge.reflectOnActivity && status.last}">
					<c:set var="userList" value="${sessionMap.reflectList[item.sessionId]}"/>
					<c:forEach var="user" items="${userList}" varStatus="refStatus">
						<c:if test="${refStatus.first}">
							<tr>
								<td colspan="4">
									<b><fmt:message key="title.reflection"/>	</b>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									<fmt:message key="monitoring.user.fullname"/>
								</th>
								<th colspan="2">
									<fmt:message key="monitoring.user.reflection"/>
								</th>
							</tr>
						</c:if>
						<tr>
							<td colspan="2">
								<c:out value="${user.fullName}" escapeXml="true"/>
							</td>
							<td colspan="2" class="align-right">
								<c:set var="viewReflection">
									<c:url value="/monitoring/viewReflection.do?toolSessionID=${item.sessionId}&userUid=${user.userUid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${viewReflection}')" styleClass="btn btn-default btn-xs loffset5">
									<fmt:message key="label.view" />
								</html:link>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			
		</c:forEach>	
	</table>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<%@ include file="advanceOptions.jsp"%>
