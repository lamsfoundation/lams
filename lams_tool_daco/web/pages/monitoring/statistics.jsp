<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="includeMode" value="monitoring" />
<c:set var="userUid" value="${sessionMap.userUid}" />
<c:url var="refreshStatisticsUrl" value="/monitoring/summary.do?sessionMapID=${sessionMapID}"/>

	<%-- Same statistics as in the learner,
		but with an option to choose currently displayed learner. --%>
	<c:choose>
		<c:when test="${empty monitoringSummary || empty monitoringSummary[0].users}">
			<div align="center" style="font-weight: bold;">
				<fmt:message key="message.monitoring.summary.no.session" />
			</div>
		</c:when>
		<c:otherwise>
			<table>
				<tr>
					<td class="button-cell">
						<div style="float: left">
							<select id="userDropdown" style="margin-top: -2px; height: 30px">
								<c:forEach var="userGroup" items="${monitoringSummary}">
									<option value="-1" disabled="disabled">--- ${userGroup.sessionName} ---</option>
									<c:forEach var="nextUser" items="${userGroup.users}">
										<option value="${nextUser.uid}" 
										<c:if test="${userUid==nextUser.uid}">
											selected="selected"
											<c:set var="recordList" value="${nextUser.records}" />
											<c:set var="userFullName" value="<c:out value='${nextUser.fullName}' escapeXml='true'/>" />
										</c:if>
										<c:out value="${userFullName}" escapeXml="false"/></option>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					<a href="#" onclick="javascript:refreshPage('${refreshStatisticsUrl}')" class="button space-left">
						<fmt:message key="label.monitoring.chooseuser" />
					</a>
					</td>
				</tr>
			</table>
			</p>
			<%@ include file="/pages/learning/questionSummaries.jsp" %>
		</c:otherwise>
	</c:choose>
