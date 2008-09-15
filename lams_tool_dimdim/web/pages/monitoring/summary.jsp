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

<c:out value="" />

<c:choose>
	<c:when test="${fn:length(dto.sessionDTOs) gt 1}">
		<!-- Dimdim only supports one conference session at a time -->
	</c:when>
	<c:when test="${fn:length(dto.sessionDTOs) eq 0}">
		<!-- No Tool Sessions available -->
	</c:when>
	<c:otherwise>
		<c:forEach var="session" items="${dto.sessionDTOs}">
			<html:form action="monitoring" target="_blank">
				<html:hidden property="dispatch" value="startDimdim" />
				<html:hidden property="toolSessionID" value="${session.sessionID}" />
	
				<div>		
					<fmt:message key="label.authoring.basic.topic" />
					:
					<html:text property="topic"></html:text>
				</div>
				<div>
					<fmt:message key="label.authoring.basic.meetingKey" />
					:
					<html:text property="meetingKey"></html:text>
				</div>
				<div>
					<fmt:message key="label.authoring.basic.maxAttendeeMikes" />
					:
					<html:select property="maxAttendeeMikes">
						<html:option value="1"></html:option>
						<html:option value="2"></html:option>
						<html:option value="3"></html:option>
						<html:option value="4"></html:option>
						<html:option value="5"></html:option>
					</html:select>
				</div>
				<html:submit styleClass="button">Open Conference</html:submit>
			</html:form>
		</c:forEach>
	</c:otherwise>
</c:choose>

<c:forEach var="session" items="${dto.sessionDTOs}">

	<table>
		<tr>
			<td>
				<h2>
					${session.sessionName}
				</h2>
			</td>
		</tr>
	</table>

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

	<table cellpadding="0">

		<tr>
			<th>
				<fmt:message key="heading.learner" />
			</th>
			<th>
				<fmt:message key="heading.notebookEntry" />
			</th>
		</tr>


		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td width="30%">
					${user.firstName} ${user.lastName}
				</td>
				<td width="70%">
					<c:choose>
						<c:when test="${user.entryUID == null}">
							<fmt:message key="label.notAvailable" />
						</c:when>

						<c:otherwise>
							<a
								href="./monitoring.do?dispatch=showDimdim&amp;userUID=${user.uid}">
								<fmt:message key="label.view" /> </a>
						</c:otherwise>
					</c:choose>

				</td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>
