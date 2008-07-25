<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
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

<c:set var="dto" value="${notebookDTO}" />

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
								href="./monitoring.do?dispatch=showNotebook&amp;userUID=${user.uid}">
								<fmt:message key="label.view" /> </a>
						</c:otherwise>
					</c:choose>

				</td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>
