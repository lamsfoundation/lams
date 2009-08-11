<%@ include file="/taglibs.jsp"%>

<h4 class="align-left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
</h4>

<h1><fmt:message key="admin.statistics.title" /></h1>

<h3><fmt:message key="admin.statistics.overall" /></h3>

<table class="alternative-color">
	<tr>
		<td>
			<fmt:message key="admin.statistics.totalUsers" />
		</td>
		<td>
			${statisticsDTO.users}
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="admin.statistics.learningDesigns" />
		</td>
		<td>
			${statisticsDTO.sequences}
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="admin.statistics.lessons" />
		</td>
		<td>
			${statisticsDTO.lessons}
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="admin.statistics.activities" />
		</td>
		<td>
			${statisticsDTO.activities}
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="admin.statistics.completedActivities" />
		</td>
		<td>
			${statisticsDTO.completedActivities}
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="admin.statistics.groups" />
		</td>
		<td>
			${statisticsDTO.groups}
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="admin.statistics.subGroups" />
		</td>
		<td>
			${statisticsDTO.subGroups}
		</td>
	</tr>
</table>

<br />
<h1><fmt:message key="admin.statistics.title.byGroup" /></h1>

<c:forEach var="groupStatisticDTO" items="${statisticsDTO.groupStatistics}">
	<h3>${groupStatisticDTO.name}</h3>
	
	<table class="alternative-color">
		<tr>
			<td>
				<fmt:message key="admin.statistics.totalUsers" />
			</td>
			<td>
				${groupStatisticDTO.totalUsers}
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.statistics.lessons" />
			</td>
			<td>
				${groupStatisticDTO.lessons}
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.statistics.group.monitors" />
			</td>
			<td>
				${groupStatisticDTO.monitors}
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.statistics.group.authors" />
			</td>
			<td>
				${groupStatisticDTO.authors}
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.statistics.group.learners" />
			</td>
			<td>
				${groupStatisticDTO.learners}
			</td>
		</tr>
	</table>
	
	<c:if test="${not empty  groupStatisticDTO.subGroups}">
		<div style="margin-left:50px">
			<c:forEach var="subGroupStatisticDTO" items="${groupStatisticDTO.subGroups}">
				
				<h3>${subGroupStatisticDTO.name}</h3>
				
				<table class="alternative-color">
					<tr>
						<td>
							<fmt:message key="admin.statistics.totalUsers" />
						</td>
						<td>
							${subGroupStatisticDTO.totalUsers}
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key="admin.statistics.lessons" />
						</td>
						<td>
							${subGroupStatisticDTO.lessons}
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key="admin.statistics.group.monitors" />
						</td>
						<td>
							${subGroupStatisticDTO.monitors}
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key="admin.statistics.group.authors" />
						</td>
						<td>
							${subGroupStatisticDTO.authors}
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key="admin.statistics.group.learners" />
						</td>
						<td>
							${subGroupStatisticDTO.learners}
						</td>
					</tr>
				</table>
			</c:forEach>
		</div>
	</c:if>
</c:forEach>
