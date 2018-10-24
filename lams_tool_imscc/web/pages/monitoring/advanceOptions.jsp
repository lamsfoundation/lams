<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">          
	<table class="table table-striped table-condensed">
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.run.content.auto" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${sessionMap.commonCartridge.runAuto}">
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
					<c:when test="${sessionMap.commonCartridge.reflectOnActivity}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<c:choose>
			<c:when test="${sessionMap.commonCartridge.reflectOnActivity}">
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
</lams:AdvancedAccordian>
