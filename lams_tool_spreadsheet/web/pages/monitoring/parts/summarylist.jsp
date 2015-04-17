<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<div id="summaryList2">
	<c:forEach var="summary" items="${summaryList}" varStatus="firstGroup">
		<c:if test="${sessionMap.isGroupedActivity}">
			<h1>
				<fmt:message key="monitoring.label.group" /> ${summary.sessionName}	
			</h1>
		</c:if>
		
		<h2 style="color:black; margin-left: 20px;"><fmt:message key="label.monitoring.summary.overall.summary" />	</h2>
		
		<table cellpadding="0" class="alternative-color" >
			<tr>
				<th width="60%" align="left">
					<fmt:message key="label.monitoring.summary.learner" />
				</th>
				<c:if test="${spreadsheet.markingEnabled}">			
					<th width="40px" align="center">
						<fmt:message key="label.monitoring.summary.marked" />
					</th>
					<th width="20px" align="left">
					</th>
				</c:if>
			</tr>
	
			<c:forEach var="user" items="${summary.users}" varStatus="userStatus">
				<tr>
					<td>
						<c:choose>
							<c:when test="${spreadsheet.learnerAllowedToSave}">
								<c:set var="reviewItem">
									<c:url value="/reviewItem.do?userUid=${user.uid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${reviewItem}')">
									${user.loginName}
								</html:link>
							</c:when>
							<c:otherwise>
								${user.loginName}
							</c:otherwise>
						</c:choose>					
					</td>
					
					<c:if test="${spreadsheet.markingEnabled}">					
						<td align="center">
							<c:choose>
								<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
									<img src="<lams:LAMSURL/>/images/tick.gif" alt="tick" border="0"/>
								</c:when>
									
								<c:otherwise>
									<img src="<lams:LAMSURL/>/images/cross.gif" alt="cross" border="0"/>
								</c:otherwise>
							</c:choose>
						</td>
						
						
						
						
							<c:choose>
								<c:when test="${user.userModifiedSpreadsheet != null}">
									<td align="left">								
										<html:link
											href="javascript:editMark(${user.uid});"
											property="Mark" styleClass="button" >
											<fmt:message key="label.monitoring.summary.mark.button" />
										</html:link>
									</td>										
								</c:when>
									
								<c:otherwise>
									<td align="center">
										<fmt:message key="label.monitoring.summary.mark.button" />
									</td>
								</c:otherwise>
							</c:choose>						
						
						


					</c:if>				
				</tr>
			</c:forEach>
			
		</table>
		<c:if test="${spreadsheet.markingEnabled}">	
			<div style="position:relative; left:30px; ">
				<html:link href="javascript:viewAllMarks(${summary.sessionId});"
					property="viewAllMarks" styleClass="button">
					<fmt:message key="label.monitoring.summary.viewAllMarks.button" />
				</html:link>
				<html:link href="javascript:releaseMarks(${summary.sessionId});"
					property="releaseMarks" styleClass="button">
					<fmt:message key="label.monitoring.summary.releaseMarks.button" />
				</html:link>
 				<html:link href="javascript:downloadMarks(${summary.sessionId});"
					property="downloadMarks" styleClass="button">
					<fmt:message key="label.monitoring.summary.downloadMarks.button" />
				</html:link>
			</div>
		</c:if>
		
		<%-- Reflection list  --%>
		
		<c:if test="${sessionMap.spreadsheet.reflectOnActivity}">
		
			<h2 style="color:black; margin-left: 20px; " ><fmt:message key="label.monitoring.summary.title.reflection"/>	</h2>
			<table cellpadding="0"  class="alternative-color"  >
	
				<tr>
					<th>
						<fmt:message key="label.monitoring.summary.user"/>
					</th>
					<th>
						<fmt:message key="label.monitoring.summary.reflection"/>
					</th>
				</tr>				
							
				<c:forEach var="user" items="${summary.users}">
					<tr>
						<td>
							${user.loginName}
						</td>
						<td >
							<c:set var="viewReflection">
								<c:url value="/monitoring/viewReflection.do?userUid=${user.uid}"/>
							</c:set>
							<html:link href="javascript:launchPopup('${viewReflection}')">
								<fmt:message key="label.view" />
							</html:link>
						</td>
					</tr>
				</c:forEach>
							
			</table>
		</c:if>
		<br>
		
		
	</c:forEach>
</div>

<%-- This script will works when a new resoruce Condition submit in order to refresh "TaskList List" panel. --%>
<script type="text/javascript">
	var win = null;
	if (window.hideMessage) {
		win = window;
	} else if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('summariesArea');
		obj.innerHTML= document.getElementById("summaryList2").innerHTML;
	}
</script>
