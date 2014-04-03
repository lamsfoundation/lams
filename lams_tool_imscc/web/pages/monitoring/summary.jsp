<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<h1>
  <c:out value="${sessionMap.commonCartridge.title}" escapeXml="true"/>  
</h1>
<div class="instructions space-top">
  <c:out value="${sessionMap.commonCartridge.instructions}" escapeXml="false"/>
</div>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>
<br/>
<table cellpadding="0" class="alternative-color">
	<c:forEach var="group" items="${summaryList}" varStatus="firstGroup">
		<c:set var="groupSize" value="${fn:length(group)}" />
		<c:forEach var="item" items="${group}" varStatus="status">
			<%-- display group name on first row--%>
			<c:if test="${status.first}">
				<tr>
					<td colspan="5">
						<B><fmt:message key="monitoring.label.group" /> ${item.sessionName}</B> 
						<SPAN style="font-size: 12px;"> 
							<c:if test="${firstGroup.index==0}">
								<fmt:message key="monitoring.summary.note" />
							</c:if> 
						</SPAN>
					</td>
				</tr>
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
				<%-- End group title display --%>
			</c:if>
			<c:if test="${item.itemUid == -1}">
				<tr>
					<td colspan="5">
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
					<td align="center">
						<c:choose>
							<c:when test="${item.viewNumber > 0}">
								<c:set var="listUrl">
									<c:url value='/monitoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}' />
								</c:set>
								<a href="#" onclick="launchPopup('${listUrl}','listuser')"> <c:out value="${item.viewNumber}" escapeXml="true"/><a>
							</c:when>
							<c:otherwise>
									0
							</c:otherwise>
						</c:choose>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.itemHide}">
								<a href="<c:url value='/monitoring/showitem.do'/>?sessionMapID=${sessionMapID}&itemUid=${item.itemUid}" class="button"> <fmt:message key="monitoring.label.show" /> </a>
							</c:when>
							<c:otherwise>
								<a href="<c:url value='/monitoring/hideitem.do'/>?sessionMapID=${sessionMapID}&itemUid=${item.itemUid}" class="button"> <fmt:message key="monitoring.label.hide" /> </a>
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
								<td colspan="5">
									<h2><fmt:message key="title.reflection"/>	</h2>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									<fmt:message key="monitoring.user.fullname"/>
								</th>
								<th>
									<fmt:message key="monitoring.user.reflection"/>
								</th>
							</tr>
						</c:if>
						<tr>
							<td colspan="2">
								<c:out value="${user.fullName}" escapeXml="true"/>
							</td>
							<td >
								<c:set var="viewReflection">
									<c:url value="/monitoring/viewReflection.do?toolSessionID=${item.sessionId}&userUid=${user.userUid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${viewReflection}')">
									<fmt:message key="label.view" />
								</html:link>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			
		</c:forEach>
		
	</c:forEach>
</table>



<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.commonCartridge.lockWhenFinished == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.run.content.auto" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.commonCartridge.runAuto == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.mini.number.resources.view" />
		</td>
		
		<td>
			${sessionMap.commonCartridge.miniViewCommonCartridgeNumber}
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${sessionMap.commonCartridge.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${sessionMap.commonCartridge.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${sessionMap.commonCartridge.reflectInstructions}" escapeHtml="true"/>
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>
