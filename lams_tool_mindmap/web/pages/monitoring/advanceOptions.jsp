<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">

	<table class="table table-condensed">
	
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
				<fmt:message key="advanced.multiUserMode" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${dto.multiUserMode}">
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
				<fmt:message key="advanced.reflectOnActivity" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${dto.reflectOnActivity}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		  <c:if test="${dto.reflectOnActivity}">	
			<tr>
				<td>
					<fmt:message key="label.notebookInstructions" />
				</td>
			
				<td>
					<lams:out value="${dto.reflectInstructions}" escapeHtml="true"/>
				</td>
			</tr>
		 </c:if>
		
	</table>
</lams:AdvancedAccordian>