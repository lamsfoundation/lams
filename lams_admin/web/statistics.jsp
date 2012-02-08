<%@ include file="/taglibs.jsp"%>

<script src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js" type="text/javascript"></script>

<script type="text/javascript">
	function loadGroupStats(orgId) {
		if (orgId) {
			jQuery.ajax({		
				type: "GET",
				url: "<lams:WebAppURL/>/statistics.do",
				data: {method : "groupStats", orgId : orgId},
				cache: false,
				success: function (html) {
					jQuery("#groupDiv").html(html);
				}
			});
		} else {
			jQuery("#groupDiv").html(null);
		}
	}
</script>

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
<br />
<select onchange="javascript:loadGroupStats(this.options[this.selectedIndex].value)">
	<option></option>
	<c:forEach var="groupEntry" items="${groupMap}" >	
		<option value="${groupEntry.value}">${groupEntry.key}</option>
	</c:forEach>
</select>
<br />
<div id="groupDiv">

</div>

