<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />

<div class="panel">

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<table class="table table-striped table-condensed">

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
</lams:AdvancedAccordian>



	<c:forEach var="session" items="${dto.sessionDTOs}">
	
		<h4>
			<c:out value="${session.sessionName}" escapeXml="true"/>
		</h4>
	
		<html:form action="monitoring"  target="bbbMonitor${session.sessionID}" onsubmit="window.open('', 'bbbMonitor${session.sessionID}', 'resizable=yes,scrollbars=yes')"> 
			<html:hidden property="dispatch" value="startMeeting" />
			<html:hidden property="toolSessionID" value="${session.sessionID}" />
						
			<p>
				<html:submit styleClass="btn btn-primary loffset5 voffset10">
				<fmt:message key="label.monitoring.startConference" />
				</html:submit>
			</p>
		</html:form>
		
	
		<p>
			<span class="field-name"> <fmt:message
					key="heading.totalLearners" /> </span> ${session.numberOfLearners}
		</p>
		
		<c:if test="${contentDTO.reflectOnActivity}">
			<div class="table-responsive">
			<table class="table table-striped">
		
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
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
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
			</div>
		</c:if>
		
		
	</c:forEach>

