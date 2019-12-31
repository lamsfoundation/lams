<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.OrganisationType" %>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="title.clone.lessons"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css"  rel="stylesheet">
			
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<%-- <script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.pack.js"></script>--%>
	<script type="text/javascript">
				var staffLoaded, learnersLoaded = false;
				var lessonsLoaded = {};
			
				$(document).ready(function() {
					$("#sourceGroupId").change(function() {
						loadSubgroups($("#sourceGroupId").val());
					});
					$("#sourceGroupId").load("<lams:LAMSURL/>admin/clone/getGroups.do", function() {
						loadSubgroups($("#sourceGroupId").val());
					});
					$("a.lessonNameLink").on("click", function() {
						lessonDialog($(this).attr("id"));
					});
				});
			
				function loadSubgroups(groupId) {
					$("#sourceSubgroupId").load("<lams:LAMSURL/>admin/clone/getSubgroups.do", { groupId: groupId });
				}
			
				function chosenGroup() {
					if ($("#sourceSubgroupId").val() != '') {
						return $("#sourceSubgroupId").val();
					} else {
						return $("#sourceGroupId").val();
					}
				}
			
				function loadGroupAttributes(sourceGroupId) {
					$("#cloneOptionsDiv").show();
					$("#availableLessons").load("<lams:LAMSURL/>admin/clone/availableLessons.do", { sourceGroupId: sourceGroupId });
				}
			
				function lessonDialog(lessonId) {
					if (lessonsLoaded[lessonId] == null || !lessonsLoaded[lessonId]) {
						$("#lessonDialog-"+lessonId).dialog({
							autoOpen: false,
							modal: true
						});
						lessonsLoaded[lessonId] = true;
					}
					$("#lessonDialog-"+lessonId).dialog("open");
					return false;
				}
			
				function initUserDialog(selector) {
					$(selector).dialog({
						autoOpen: false,
						modal: true, 
						width: 680, 
						buttons: { 
							"<fmt:message key='label.ok' />": function() {
								$(this).dialog("close");
							} 
						} 
					});
				}
			
				function staffDialog() {
					if (!staffLoaded) {
						$("#staffDialog").load("<lams:LAMSURL/>admin/clone/selectStaff.do", { groupId: <c:out value="${org.organisationId}" /> });
						initUserDialog("#staffDialog");
						staffLoaded = true;
					}
					$("#staffDialog").dialog("open");
					return false;
				}
			
				function learnerDialog() {
					if (!learnersLoaded) {
						$("#learnerDialog").load("<lams:LAMSURL/>admin/clone/selectLearners.do", { groupId: <c:out value="${org.organisationId}" /> });
						initUserDialog("#learnerDialog");
						learnersLoaded = true;
					}
					$("#learnerDialog").dialog("open");
					return false;
				}
			
				function clone() {
					var lessons = [];
					var staff = [];
					var learners = [];
					$("input[name=lessons]:checked").each(function() {
						lessons.push($(this).val());
					});
					$("input[name=staff]:checked").each(function() {
						staff.push($(this).val());
					});
					$("input[name=learners]:checked").each(function() {
						learners.push($(this).val());
					});
					$("input[name=lessons]").val(lessons.join(","));
					$("input[name=staff]").val(staff.join(","));
					$("input[name=learners]").val(learners.join(","));
					$("input[name=addAllStaff]").val($("#addAllStaff").is(":checked"));
					$("input[name=addAllLearners]").val($("#addAllLearners").is(":checked"));
					return true;
				}
	</script>
</lams:head>
    
<body class="stripes">

	<c:set var="classTypeId"><%= OrganisationType.CLASS_TYPE %></c:set>

	<lams:Page type="admin" title="${title}" formID="cloneForm">
			
			<p><a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a> :
				<c:if test="${org.organisationType.organisationTypeId eq classTypeId}">
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${org.parentOrganisation.organisationId}" />" class="btn btn-default">
						<c:out value="${org.parentOrganisation.name}" />
					</a> :
				</c:if>
				<a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${org.organisationId}" />" class="btn btn-default">
					<c:out value="${org.name}" />
				</a>
			</p>
			
			<h4><fmt:message key="title.clone.lessons.for"><fmt:param value="${org.name}" /></fmt:message></h4>
			
			<c:if test="${not empty errors}">
				<lams:Alert type="danger" id="errorKey" close="false">			
					<c:forEach items="${errors}" var="error">
						<c:out value="${error}" />
					</c:forEach>
				</lams:Alert>
			</c:if>
			
			<div class="panel panel-default voffset5" >
				<div class="panel-heading">
					<span class="panel-title">
						<fmt:message key="title.choose.group" />
					</span>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<fmt:message key="admin.course" />:
						<select id="sourceGroupId" class="form-control">
							<option value="">...</option>
						</select>
					</div>
					<div class="form-group">
						<fmt:message key="admin.class" />:
						<select id="sourceSubgroupId" class="form-control">
						</select>
					</div>
					<div class="form-group">
						<input type="submit" class="btn btn-default" value="<fmt:message key="label.choose" />" onclick="javascript:loadGroupAttributes(chosenGroup());">
					</div>
				</div>
			</div>
			
			<div id="staffDialog" title="<fmt:message key="label.configure.staff" />" style="display:none;">
			
			</div>
			
			<div id="learnerDialog" title="<fmt:message key="label.configure.learners" />" style="display:none;">
			
			</div>
			
			
			<form name="cloneForm" id="cloneForm" action="start.do" method="post">
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<input type="hidden" name="method" value="clone">
				<input type="hidden" name="groupId" value="<c:out value="${org.organisationId}" />">
				<input type="hidden" name="lessons">
				<input type="hidden" name="staff">
				<input type="hidden" name="learners">
			
				<div style="display:none;" id="cloneOptionsDiv">
				
					<div class="panel panel-default voffset5" >
					<div class="panel-heading">
						<span class="panel-title">
							<fmt:message key="title.select.lessons" />
						</span>
					</div>
					<div class="panel-body">
						<p id="availableLessons"></p>
					</div>
					</div>
				
					<div class="panel panel-default voffset5" >
					<div class="panel-heading">
						<span class="panel-title">
							<fmt:message key="title.select.staff" />
						</span>
					</div>
					<div class="panel-body">
						<input type="checkbox" id="addAllStaff" name="addAllStaff" checked="checked"> <fmt:message key="message.add.all.monitors" />
						<a onclick="staffDialog();" class="btn btn-default pull-right"><fmt:message key="label.configure.staff" /></a>
					</div>
					</div>
					
					<div class="panel panel-default voffset5" >
					<div class="panel-heading">
						<span class="panel-title">
							<fmt:message key="title.select.learners" />
						</span>
					</div>
					<div class="panel-body">
						<input type="checkbox" id="addAllLearners" name="addAllLearners" checked="checked"> <fmt:message key="message.add.all.learners" />
						<a onclick="learnerDialog();" class="btn btn-default pull-right"><fmt:message key="label.configure.learners" /></a>
					</div>
					</div>
			
					<div class="pull-right">
						<input type="submit" class="btn btn-primary" onclick="return clone();" value="<fmt:message key="label.clone" />">
					</div>
				</div>
				
			</form>
	</lams:Page>	
</body>
</lams:html>



