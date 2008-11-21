<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript"
	src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js"></script>
<script type="text/javascript">
<!--
	var evalcomixWindow = null;
	
	function openEvalcomixWindow(url)
	{
    	evalcomixWindow=window.open(url,'evalcomixWindow','width=800,height=600,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}
//-->
</script>

<c:set var="dto" value="${contentDTO}" />

<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon"
		onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a
		href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');">
		<fmt:message key="monitor.summary.th.advancedSettings" /> </a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display: none">
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

	</table>
</div>


	<c:forEach var="session" items="${dto.sessionDTOs}">
	
		<h1>
			${session.sessionName}
		</h1>
	
		<h2>
			<fmt:message key="monitor.summary.dimdimSettings" />
		</h2>
	
		
		<html:form action="monitoring"  target="dimdimMonitor${session.sessionID}" onsubmit="window.open('', 'dimdimMonitor${session.sessionID}', 'resizable=yes,scrollbars=yes')"> 
			<html:hidden property="dispatch" value="startMeeting" />
			<html:hidden property="toolSessionID" value="${session.sessionID}" />
			
			<p>
				<fmt:message key="label.authoring.basic.maxAttendeeMikes" />
					:
				<html:select property="maxAttendeeMikes">
					<html:option value="1"></html:option>
					<html:option value="2"></html:option>
					<html:option value="3"></html:option>
					<html:option value="4"></html:option>
						<html:option value="5"></html:option>
				</html:select>
			</p>
			
			<p>
				<html:submit styleClass="button">
				<fmt:message key="label.monitoring.startConference" />
				</html:submit>
			</p>
		</html:form>
		
	
		<p>
			<span class="field-name"> <fmt:message
					key="heading.totalLearners" /> </span> ${session.numberOfLearners}
		</p>
	
		<table cellspacing="0" class="alternative-color">
	
			<tr>
				<th style="width: 30%;">
					<fmt:message key="heading.learner" />
				</th>
				<th style="width: 70%;">
					<fmt:message key="heading.notebookEntry" />
				</th>
			</tr>
	
	
			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						${user.firstName} ${user.lastName}
					</td>
					<td>
						<c:choose>
							<c:when test="${user.notebookEntryUID == null}">
								<fmt:message key="label.notAvailable" />
							</c:when>
	
							<c:otherwise>
								<a
									href="./monitoring.do?dispatch=openNotebook&amp;userUID=${user.uid}">
									<fmt:message key="label.view" /> </a>
							</c:otherwise>
						</c:choose>
	
					</td>
				</tr>
			</c:forEach>
		</table>
		
		
	</c:forEach>

