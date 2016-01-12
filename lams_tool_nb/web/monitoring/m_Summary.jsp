<%@ include file="/includes/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	
	<H2><c:out value="${formBean.title}" escapeXml="true" /></H2>
	<div class="space-top"><c:out value="${formBean.basicContent}" escapeXml="false" /></div>

	<H2><fmt:message key="titleHeading.statistics"/></H2>
	<%@ include file="m_Statistics.jsp"%>

	<c:if test="${reflectOnActivity}" >
		<H2><fmt:message key="titleHeading.reflections"/></H2>
		<table class="alternative-color">
		<c:forEach var="reflection" items="${reflections}">
			<logic:empty name="reflections">
				<tr>
					<td colspan="2">
						<fmt:message key="message.no.reflections" />
					</td>
				</tr>
			</logic:empty>
			<tr>
				<td>
					<c:out value="${reflection.username}" />
				</td>
				<c:url value="monitoring.do" var="viewReflection">
					<c:param name="method" value="viewReflection" />
					<c:param name="userID" value="${reflection.userId}" />
					<c:param name="toolSessionID" value="${reflection.externalId}" />
				</c:url>
				<td><html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="link.view" />
					</html:link>
				</td>
			</tr>
		</c:forEach>
		</table>
		
	</c:if>
	
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
			<fmt:message key="advanced.allow.comments" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${allowComments}">
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
				<c:when test="${reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:if test="${reflectOnActivity}">
		<tr>
			<td>
				<fmt:message key="monitor.summary.td.notebookInstructions" />
			</td>
			<td>
				<lams:out value="${reflectInstructions}" escapeHtml="true"/>
			</td>
		</tr>
	</c:if>
</table>
</div>	
