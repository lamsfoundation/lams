<!DOCTYPE html>

<%@ page import="org.lamsfoundation.lams.signup.model.SignupOrganisation" %>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.signup.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
	<script type="text/javascript">
	    $(document).ready(function(){
	        $("time.timeago").timeago();
	        
	    });
    </script>
</lams:head>
    
<body class="stripes">
	<c:set var="help"><fmt:message key="LAMS+Signup"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
	
		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="admin.signup.title"/></li>
		  </ol>
		</nav>

		<c:if test="${not empty error}">
			<lams:Alert type="warn" id="errorMessage" close="false">	
				<c:out value="${error}" />
			</lams:Alert>
		</c:if>
		
		<table class="table table-striped table-bordered" >
			<thead class="thead-light">
			<tr>
				<th><fmt:message key="admin.group" /></th>
				<th><fmt:message key="admin.group.code" /></th>
				<th><fmt:message key="admin.lessons" /></th>
				<th><fmt:message key="admin.staff" /></th>
				<th><fmt:message key="admin.added.on"/></th>
				<th class="text-center"><fmt:message key="admin.actions"/></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${signupOrganisations}" var="signupOrganisation">
				<tr <c:if test="${signupOrganisation.disabled == 'true'}">class="table-danger ${signupOrganisation.disabled}"</c:if> >
					<td>
						<a href="<lams:LAMSURL/>admin/signupManagement/edit.do?soid=${signupOrganisation.signupOrganisationId}"><strong><c:out value="${signupOrganisation.organisation.name}" /></strong></a>
						<c:if test="${signupOrganisation.disabled == 'true'}"><span class="badge badge-warning">Disabled</span></c:if>
					</td>
					<td>
						<c:out value="${signupOrganisation.organisation.code}" />
					</td>
					<td>
						<c:choose>
						<c:when test="${signupOrganisation.addToLessons}" >
							<fmt:message key="label.yes" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.no" />
						</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
						<c:when test="${signupOrganisation.addAsStaff}" >
							<fmt:message key="label.yes" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.no" />
						</c:otherwise>
						</c:choose>
					</td>
					<td>
						<lams:Date value="${signupOrganisation.createDate}" timeago="true"/>
					</td>
					<td class="text-center">
											
                        <csrf:form style="display: inline-block;" id="delete_${signupOrganisation.signupOrganisationId}" method="post" action="/lams/admin/signupManagement/delete.do">
                        <input type="hidden" name="soid" value="${signupOrganisation.signupOrganisationId}"/>
                        
                        <button title="<fmt:message key="admin.delete" />" class="btn btn-outline-danger btn-sm" type="submit"><i class="fa fa-trash"></i></button>
                        </csrf:form>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<div class="pull-right">
			<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm">
				<fmt:message key="admin.cancel"/>
			</a>
			
			<a href="<lams:LAMSURL/>admin/signupManagement/add.do" class="btn btn-primary btn-sm loffset5"><fmt:message key="admin.add.new.signup.page"/></a>

		</div>
	</lams:Page>

</body>
</lams:html>
