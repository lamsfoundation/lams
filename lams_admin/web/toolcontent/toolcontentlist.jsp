<%@ include file="/taglibs.jsp"%>

<h2>
	<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" />
	</a> :
	<fmt:message key="sysadmin.tool.management" />
</h2>
<p>
	&nbsp;
</p>

<p>
	<fmt:message key="msg.edit.tool.content.1" />
	&nbsp;&nbsp;
	<fmt:message key="msg.edit.tool.content.2" />
</p>
<p>
	<fmt:message key="msg.edit.tool.content.3" />
</p>

<c:set var="displayToolManagement" value="false" />
<logic:iterate id="dto" name="activeTools">
	<c:if test="${dto.adminURL != null}">
		<c:set var="displayToolManagement" value="true" />
	</c:if>
</logic:iterate>

<p>
<table class="alternative-color" cellspacing="0">
	<logic:iterate name="activeTools" id="dto">
		<tr>
			<td>
				<c:out value="${dto.activityTitle}" />
			</td>
			<td>
				<a
					href="<lams:LAMSURL /><c:out value="${dto.authoringURL}" />?toolContentID=<c:out value="${dto.toolContentID}" />&contentFolderID=-1">
					<c:out value="${dto.toolDisplayName}" /> </a>
			</td>

			<c:if test="${displayToolManagement == 'true'}">
				<td>
					<c:choose>
						<c:when test="${dto.adminURL == null}">
							&nbsp;
						</c:when>
						<c:otherwise>
							<a href="<lams:LAMSURL /><c:out value="${dto.adminURL}" />">
								<fmt:message key="msg.tool.management"></fmt:message>
							</a>
						</c:otherwise>
					</c:choose>
				</td>
			</c:if>
		</tr>
	</logic:iterate>
</table>
</p>