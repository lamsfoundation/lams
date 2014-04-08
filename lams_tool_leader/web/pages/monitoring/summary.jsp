<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<c:set var="dto" value="${leaderselectionDTO}" />
<h1><c:out value="${leaderselectionDTO.title}" escapeXml="true"/></h1>
<div class="instructions space-top space-bottom">
<c:out value="${leaderselectionDTO.instructions}" escapeXml="false"/>
</div>

<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<h2>
			${session.sessionName}
		</h2>
	</c:if>

	<table cellpadding="0" class="alternative-color">

		<tr>
			<th>
				<fmt:message key="heading.learner" />
			</th>
			<th align="center">
				<fmt:message key="heading.leader" />
			</th>
		</tr>

		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td width="30%" style="padding: 5px 0;">
					<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
				</td>
				<td width="70%" align="center">
					<c:choose>
						<c:when test="${session.groupLeader != null && session.groupLeader.uid == user.uid}">
							<img src="<lams:LAMSURL />images/tick.png">
						</c:when>

						<c:otherwise>
							-
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
		
	</table>
</c:forEach>

<div class="bottom-buttons">
	<a href="<c:url value='/monitoring.do'/>?dispatch=manageLeaders&sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true&height=400&width=650" class="float-right button thickbox" title="<fmt:message key="label.manage.leaders" />">
		<fmt:message key="label.manage.leaders" />
	</a>
</div>

