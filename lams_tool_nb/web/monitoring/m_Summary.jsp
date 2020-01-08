<%@ include file="/includes/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait.js" ></script>
	
<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover("<lams:LAMSURL />");
	});
</script>

<div class="panel">
	<h4><c:out value="${monitoringDTO.title}" escapeXml="true" /></h4>
	<div class="voffset5"><c:out value="${monitoringDTO.basicContent}" escapeXml="false" /></div>
</div>

<H4><fmt:message key="titleHeading.statistics"/></H2>
<%@ include file="m_Statistics.jsp"%>

<c:if test="${reflectOnActivity}" >
	<H4><fmt:message key="titleHeading.reflections"/></H4>
	<table class="table table-striped">
		<c:choose>
			<c:when test="${not empty reflections}">
				<c:forEach var="reflection" items="${reflections}">
					<tr>
						<td width="40%">
							<lams:Portrait userId="${reflection.userId}" hover="true"><c:out value="${reflection.username}" /></lams:Portrait>
						</td>
						<c:url value="/monitoring/viewReflection.do" var="viewReflection">
							<c:param name="userID" value="${reflection.userId}" />
							<c:param name="toolSessionID" value="${reflection.externalId}" />
						</c:url>
						<td>
						<a href="javascript:launchPopup('${viewReflection}')" class="btn btn-default btn-sm ${allowComments ? 'pull-right' : ''}">
									<fmt:message key="link.view" />
							</a>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="2">
					<fmt:message key="message.no.reflections" />
					</td>
				</tr>
			</c:otherwise>
		</c:choose>

	</table>	
</c:if>
	
<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
          	
<table class="table table-striped table-condensed">
	
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
</lams:AdvancedAccordian>