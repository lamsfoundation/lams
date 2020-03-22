<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.lamsfoundation.lams.util.FileValidatorUtil" %> 

<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)%></c:set> 
<c:set var="UPLOAD_FILE_MAX_SIZE_AS_USER_STRING"><%=FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE))%></c:set> 
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set> 
<c:set var="classTypeId"><%= OrganisationType.CLASS_TYPE %></c:set>
<c:set var="courseTypeId"><%= OrganisationType.COURSE_TYPE %></c:set>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.import.groups.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script> 
	<script type="text/JavaScript">
		
		function verifyAndSubmit() {
			if ( "undefined" === typeof bCancel || ! bCancel ) {
				var fileSelect = document.getElementById('file');
				var files = fileSelect.files;
					if (files.length == 0) {
					clearFileError();
					showFileError('<fmt:message key="button.select.importfile"/>');
					return false;
				} else {
					var file = files[0];
					if ( ! validateShowErrorSpreadsheetType(file, '<fmt:message key="error.attachment.not.xls"/>', false)
							 || ! validateShowErrorFileSize(file, '${UPLOAD_FILE_MAX_SIZE}', '<fmt:message key="errors.maxfilesize"/>') ) {
						return false;
					}
				}
			document.getElementById('fileUpload_Busy').style.display = '';
			}
		}
	 </script>
</lams:head>
    
<body class="stripes">

	<c:set var="help"><fmt:message key="Import+Groups"/></c:set>
	<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
	<lams:Page type="admin" title="${title}" titleHelpURL="${help}" formID="importForm">
	
		<nav aria-label="breadcrumb" role="navigation">
		  <ol class="breadcrumb">
		    <li class="breadcrumb-item">
		    	<a href="<lams:LAMSURL/>admin/sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
		    </li>
		    <li class="breadcrumb-item active" aria-current="page"><fmt:message key="sysadmin.import.groups.title"/></li>
		  </ol>
		</nav>		

	<div id="main-page">
	
	<c:if test="${not empty results}">
		<h4><fmt:message key="heading.import.results"/></h4>
		<table cellspacing="5" cellpadding="5">
			<tr>
				<th width="115" align="right"><fmt:message key="table.heading.organisation.id"/></th>
				<th><fmt:message key="admin.organisation.name"/></th>
			</tr>
			<c:set var="results" value="${results}" />
			<c:forEach var="i" begin="0" step="1" end="${results.size()-1}">
				<tr>
				<c:set var="rowResult" value="${results[i]}"/>
				<c:choose>
					<c:when test="${(rowResult != null) && (rowResult.size() >= 4)}">
						<c:if test="${courseTypeId == rowResult[3]}">
							<th> <c:out value="${rowResult[0]}" /> </th>
							<th> <c:out value="${rowResult[1]}" /> </th>
						</c:if>
						<c:if test="${classTypeId == rowResult[3]}">
							<td> <c:out value="${rowResult[0]}" /> </td>
							<td> <c:out value="${rowResult[1]}" /> </td>
						</c:if>
					</c:when>
					<c:otherwise>
						<td colspan="2">
						<c:forEach var="j" begin="0" step="1" end="${results.size()-1}">
							<c:out value="${rowResult[j]}"/> <br />
						</c:forEach>
						</td>
					</c:otherwise>
				</c:choose>
				</tr>
			</c:forEach>
		</table>
		<hr />
	</c:if>
	
	<p><fmt:message key="import.groups.intro"/></p>
	<p>
	<ul>
		<li>
			<fmt:message key="msg.import.1"/>
		</li>
		<li>
			<fmt:message key="import.groups.instructions"/>
		</li>
		<li>
			<fmt:message key="import.groups.download"/>
			<ul><li><p><a href="file/lams_groups_template.xls">lams_groups_template.xls</a></p></li></ul>
		</li>
	</ul>
	</p>
	<p><fmt:message key="msg.import.conclusion"/></p>
	
	<form:form action="importgroups.do" modelAttribute="importForm" id="importForm" method="post" enctype="multipart/form-data" onsubmit="return verifyAndSubmit();">
		<form:hidden path="orgId" />
		
		<lams:FileUpload fileFieldname="file" fileInputMessageKey="label.excel.spreadsheet" maxFileSize="${UPLOAD_FILE_MAX_SIZE_AS_USER_STRING}"/> 
			<lams:WaitingSpinner id="fileUpload_Busy"/> 
		
		<div class="pull-right">
		<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-outline-secondary btn-sm"><fmt:message key="admin.cancel"/></a>
		<input type="submit" id="importButton" class="btn btn-primary btn-sm loffset5" value="<fmt:message key="label.import"/>" /> &nbsp; 	
		</div>
	
	</form:form>
	
	</div>
	</lams:Page>
		
</body>
</lams:html>




