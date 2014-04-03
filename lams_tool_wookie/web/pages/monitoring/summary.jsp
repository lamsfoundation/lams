<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<c:set var="dto" value="${wookieDTO}" />

<script type="text/javascript">
	function submitForm(method, uid) {
		document.getElementById("dispatch").value = method;
		document.getElementById("monitoringForm").submit();
	}
</script>


<html:form action="/monitoring" method="post" styleId="monitoringForm">
	
	<html:hidden property="dispatch" styleId="dispatch" />
	<html:hidden property="contentFolderID" value="${contentFolderID}" />	
	<html:hidden property="toolContentID" value="${toolContentID}"/>
	
	<c:forEach var="session" items="${dto.sessionDTOs}">
		
		<c:if test="${multipleSessionFlag}">
		<h1>
			${session.sessionName}
		</h1>
		</c:if>
	
		<table cellpadding="0">
			<tr>
				<td class="field-name" width="30%">
					<fmt:message key="heading.totalLearners" />
				</td>
				<td width="70%">
					${session.numberOfLearners}
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr align="center">
				<td>
					<iframe
							id="widgetIframe"
							src="${session.sessionUserWidgetUrl}" 
							name="widgetIframe"
							style="width:${session.widgetWidth}px;height:${session.widgetHeight}px;border:0px;" 
							frameborder="no"
							scrolling="no"
							>
					</iframe>
				</td>
			</tr>
		</table>

		<c:if test="${wookieDTO.reflectOnActivity}">
			<br />
			
			<h3> <fmt:message key="monitoring.reflections" /></h3>
			
			<table cellpadding="0" class="alternative-color">
				<tr>
					<th><fmt:message key="monitoring.th.learner" /></th>
					
					<th><fmt:message key="monitoring.th.reflection" /></th>
					
				</tr>
				
				<c:forEach var="user" items="${session.userDTOs}">
					<tr>
						<td>
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
						</td>
						
							<td >
								<c:choose>
									<c:when test="${user.finishedReflection}">
										<lams:out value="${user.notebookEntry}" escapeHtml="true"/>
									</c:when>
									<c:otherwise>
										<fmt:message key="label.notAvailable" />
									</c:otherwise>
								</c:choose>
							</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		<br /> 
		<br />
	
	</c:forEach>

</html:form>


<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.lockOnFinish}">
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
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${dto.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${dto.reflectInstructions}" escapeHtml="true"/>	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>

