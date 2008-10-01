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

		<tr>
			<td>
				<fmt:message key="advanced.allowRichEditor" />
			</td>

			<td>
				<c:choose>
					<c:when test="${dto.allowRichEditor}">
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

<c:choose>
	<c:when test="${fn:length(dto.sessionDTOs) gt 1}">
		<!-- Dimdim only supports one conference session at a time -->
	</c:when>
	<c:when test="${fn:length(dto.sessionDTOs) eq 0}">
		<!-- No Tool Sessions available -->
	</c:when>
	<c:otherwise>
		<h1>
			<fmt:message key="monitor.summary.dimdimSettings"/>
		</h1>

		<c:forEach var="session" items="${dto.sessionDTOs}">
			<html:form action="monitoring" target="_blank">
				<html:hidden property="dispatch" value="startDimdim" />
				<html:hidden property="toolSessionID" value="${session.sessionID}" />
				
				<table>
					<tr>
						<td>
							<fmt:message key="label.authoring.basic.maxAttendeeMikes" />
							:
							<html:select property="maxAttendeeMikes">
								<html:option value="1"></html:option>
								<html:option value="2"></html:option>
								<html:option value="3"></html:option>
								<html:option value="4"></html:option>
								<html:option value="5"></html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<html:submit styleClass="button">
								<fmt:message key="label.monitoring.startConference" />
							</html:submit>
						</td>
					</tr>
				</table>
			</html:form>
		</c:forEach>
	</c:otherwise>
</c:choose>

<c:forEach var="session" items="${dto.sessionDTOs}">

	<h2>
		${session.sessionName}
	</h2>

	<p>
		<span class="field-name">
			<fmt:message key="heading.totalLearners" />
		</span>
		${session.numberOfLearners}
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
