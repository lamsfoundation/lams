<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%-- Build breadcrumb --%>
<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.statistics.title"/></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.statistics.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript">
		function loadGroupStats(orgId) {
			if (orgId) {
				jQuery.ajax({		
					type: "GET",
					url: "<lams:WebAppURL/>/statistics/groupStats.do",
					data: {orgId : orgId},
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
</lams:head>

	<lams:PageAdmin title="${title}" breadcrumbItems="${breadcrumbItems}">
		<div class="card">
			<div class="card-header">
				<fmt:message key="admin.statistics.overall" />
			</div>
			<div class="card-body">
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
		
		<div class="card mt-3">
			<div class="card-header">
				<fmt:message key="admin.statistics.title.byGroup" />
			</div>
			
			<div class="card-body">
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
	</lams:PageAdmin>
</lams:html>
