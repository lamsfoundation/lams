	
	<table border="0" cellspacing="30" width="98%">
	<%-- display group name on first row--%>
			
				<tr>
					<td colspan="5">
						<B><fmt:message key="monitoring.label.group" /> ${group.sessionName}</B> 
					
					</td>
				</tr>
			
			<tr>
			<th> 
				<fmt:message key="monitoring.label.access.time" />
			</th> 
			<th>
				<fmt:message key="monitoring.label.user.name" />
			</th>
		
		</tr>
		<c:forEach var="user" items="${group.users}" varStatus="status" >
			<tr>
				<td>
					<lams:Date value="${user.accessDate}"/>
				</td> 
				<td>
					<c:out value="${user.firstName},${user.lastName}" escapeXml="true"/>
				</td>	
				
				<c:choose>
				<c:when test="${group.existList[status.index]}">
					<td>
						<a href="#" onclick="showReport('${group.sessionId}','${sessionMapID}', '${user.uid}')" class="button">Show report</a>
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<fmt:message key="monitoring.no.report" />
					</td>
				</c:otherwise>
			</c:choose>	
			</tr>
			<%-- Reflection list  --%>
				<c:if test="${sessionMap.eadventure.reflectOnActivity && status.last}">
					<c:set var="userList" value="${sessionMap.reflectList[group.sessionId]}"/>
					<c:forEach var="userRef" items="${userList}" varStatus="refStatus">
						<c:if test="${refStatus.first}">
							<tr>
								<td colspan="5">
									<h2><fmt:message key="title.reflection"/>	</h2>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									<fmt:message key="monitoring.user.fullname"/>
								</th>
								<th>
									<fmt:message key="monitoring.user.reflection"/>
								</th>
							</tr>
						</c:if>
						<tr>
							<td colspan="2">
								<c:out value="${userRef.fullName}" escapeXml="true"/>
							</td>
							<td >
								<c:set var="viewReflection">
									<c:url value="/monitoring/viewReflection.do?toolSessionID=${group.sessionId}&userUid=${userRef.userUid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${viewReflection}')">
									<fmt:message key="label.view" />
								</html:link>
							</td>
						</tr>
					</c:forEach>
				</c:if>
		</c:forEach>
				
	</table>


