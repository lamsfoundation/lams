<%@ include file="/taglibs.jsp"%>

<script src="<lams:LAMSURL/>/includes/javascript/jquery.js" type="text/javascript"></script>

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

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><fmt:message key="admin.statistics.overall" /></div>
	</div>
	<div class="panel-body">
	<table class="table table-striped table-condensed" >
		<tr>
			<td>
				<fmt:message key="admin.statistics.totalUsers" />
			</td>
			<td width="150px">
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
	</div>
</div>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><fmt:message key="admin.statistics.title.byGroup" /></div>
	</div>
	
	<div class="panel-body">
		<select onchange="javascript:loadGroupStats(this.options[this.selectedIndex].value)" class="form-control">
			<option></option>
			<c:forEach var="groupEntry" items="${groupMap}" >	
				<option value="${groupEntry.value}">${groupEntry.key}</option>
			</c:forEach>
		</select>
		<br />
		<div id="groupDiv">
	
		</div>
	</div>
</div>
