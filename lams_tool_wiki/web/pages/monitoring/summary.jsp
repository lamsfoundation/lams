<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<script type="text/javascript">
<!--
	var viewWindow = null;
	function openViewWindow(url)
	{
		if(viewWindow && viewWindow.open && !viewWindow.closed){
			viewWindow.close();
		}
		viewWindow = window.open(url,'viewWindow','resizable,width=796,height=570,scrollbars');
		viewWindow.window.focus();
	}
	
	function showInFrame(url) {
		
		var area=document.getElementById("wikiArea");
		if(area != null){
			area.style.width="640px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		location.hash = "wikiArea";
	}
//-->
</script>

<c:set var="dto" value="${wikiDTO}" />

<h1>
	<c:out value="${dto.title}" escapeXml="true" />
</h1>

<div class="instructions space-top">
	<c:out value="${dto.instructions}" escapeXml="false" />
</div>
<br/>

<c:choose>
	<c:when test="${not empty dto.sessionDTOs}">
		<table class="alternative-color">
			<tr>
				<th>
					<fmt:message key="monitor.th.sessions"></fmt:message>
				</th>	
				<th>
					<fmt:message key="monitor.th.numlearners"></fmt:message>
				</th>
			</tr>
			<c:forEach var="session" items="${dto.sessionDTOs}">
			<tr>
				<td>
					<a href='javascript:openViewWindow("./monitoring.do?dispatch=showWiki&amp;toolSessionID=${session.sessionID}&contentFolderID=${contentFolderID}");'>${session.sessionName}</a>
				</td>	
				<td>
					${session.numberOfLearners}
				</td>	
			</tr>
			</c:forEach>
		</table>

	</c:when>
	<c:otherwise>
		<fmt:message key="monitor.nosessions"></fmt:message> 
	</c:otherwise>
</c:choose>


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
			<fmt:message key="advanced.allowLearnerCreatePages" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerCreatePages}">
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
			<fmt:message key="advanced.allowLearnerInsertLinks" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerInsertLinks}">
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
			<fmt:message key="advanced.allowLearnerAttachImages" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerAttachImages}">
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
			<fmt:message key="advanced.allowLearnerAttachImages" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerAttachImages}">
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
	
	<tr>
		<td>
			<fmt:message key="advanced.editingLimits.prompt" />
		</td>
		
		<td>
			<fmt:message key="advanced.editingLimits.minimum" /> 
			<c:choose>
				<c:when test="${dto.minimumEdits == 0}">
					<fmt:message key="advanced.editingLimits.nominimum" />
				</c:when>
				<c:otherwise>
					${dto.minimumEdits}
				</c:otherwise>
			</c:choose>
			
			<br />
			
			<fmt:message key="advanced.editingLimits.maximum" /> 
			<c:choose>
				<c:when test="${dto.maximumEdits == 0}">
					<fmt:message key="advanced.editingLimits.nomaximum" />
				</c:when>
				<c:otherwise>
					${dto.maximumEdits}
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	
</table>
</div>