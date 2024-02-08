<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.list.job"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>

	<lams:PageAdmin title="${title}">
		<div align="center" id="jobList">
			<div id="datatablecontainer">
			<table width="100%" align="CENTER" 	class="form">
				<tr>
					<td valign="MIDDLE"><b><fmt:message key="title.job.list"/></b></td>
					<td colspan="2" />
				</tr>
				<tr>
					<th scope="col" width="25%"><fmt:message key="lable.job.name"/></th>
					<th scope="col" width="25%"><fmt:message key="lable.job.start.date"/></th>
					<th scope="col" width="50%"><fmt:message key="lable.job.description"/></th>
				</tr>
				<c:forEach items="${jobList}" var="job" varStatus="status">
					<tr>
						<td>
							<c:out value="${job.name}"/>
						</td>
						<td>
							<lams:Date value="${job.startDate}"/>
						</td>
						<td>
							<c:out value="${job.description}"/>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="3" >Total jobs: ${fn:length(jobList)}</td>
				</tr>
			</table>
			</div>
		</div>
	</lams:PageAdmin>
</lams:html>
