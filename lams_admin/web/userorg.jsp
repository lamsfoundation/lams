<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<style>
		.listBoundingBox {
			border:thin solid #c1c1c1;
			margin-left:5px;
		}
		p {
			margin-left:5px;
		}
		.listScrollable {
			margin:5px; 
			height:250px; 
			overflow-y:auto;
		}
		a {
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		var removedUsers = [];
	
		jQuery(document).ready(function() {
			jQuery("div#existing").load(
				"user/basiclist.do", 
				{orgId: <c:out value="${userOrgForm.orgId}"/>,
				"<csrf:tokenname/>":"<csrf:tokenvalue/>"},
				function() {
					updateExistingTotal();
					jQuery("li", this).each(function() {
						var rowHtml = jQuery(this).html();
						jQuery(this).html("<a class='removeLink'>"+rowHtml+"</a>");
					});
					bindRemoveLink();
				}
			);
			loadSearchResults(0);
		});
		
		function updateExistingTotal() {
			var size = jQuery("li", "#existing").length;
			jQuery("span#totalUsers").text("<fmt:message key="label.number.of.users"><fmt:param>"+size+"</fmt:param></fmt:message>");
		}
		
		function updatePotentialTotal() {
			var size = jQuery("li", "#potential").length;
			jQuery("span#potentialUsers").text("<fmt:message key="label.number.of.potential.users"><fmt:param>"+size+"</fmt:param></fmt:message>");
		}
		
		function bindRemoveLink() {
			jQuery("body").click(function(event) {
				if (jQuery(event.target).is("a.removeLink")) {
					if (jQuery(event.target).parent().is("li")) {
						removedUsers[jQuery(event.target).parent().attr("id")] = jQuery(event.target).parent().text();
						jQuery(event.target).parent().remove();
						updateExistingTotal();
					}
				}
			});
		}
		
		function bindAddLink() {
			jQuery("a.addLink").click(function() {
				var rowId = jQuery(this).parent().attr("id");
				var rowText = jQuery(this).parent().text();
				jQuery("div#existing").append("<li id='"+rowId+"'><a class='removeLink'>"+rowText+"</a></li>");
				jQuery(this).parent().remove();
				updateExistingTotal();
				updatePotentialTotal();
			});
		}
		
		function loadSearchResults(potential) {
			if (potential=='1') {
				jQuery("div#potential").load(
					"user/basiclist.do",
					{orgId: <c:out value="${userOrgForm.orgId}"/>,
					"<csrf:tokenname/>":"<csrf:tokenvalue/>",
					potential: potential}, 
					function() {
						loadSearchResultsCallback(potential);
					}
				);
			} else {
				jQuery("div#potential").load(
					"user/searchsingle.do",
					{term: jQuery("#term").val(),
					"<csrf:tokenname/>":"<csrf:tokenvalue/>",
					orgId: <c:out value="${userOrgForm.orgId}"/>}, 
					function() {
						loadSearchResultsCallback(potential);
					}
				);
			}
			return false;
		}
		
		function loadSearchResultsCallback(potential) {
			updatePotentialTotal();
			jQuery("li", "div#potential").each(function() {
				if ($('div#existing li#' + $(this).attr('id')).length > 0) {
					$(this).remove();
				} else {
					$(this).html("<a class='addLink'>"+$(this).html()+"</a>");
				}
			});
	
			if (potential == '1') {
				for (var userId in removedUsers) {
					if ($('div#existing li#' + userId + ', div#potential li#' + userId).length == 0) {
						$("div#potential").append("<li id=\"" + userId + "\"><a class='addLink'>" + removedUsers[userId] + "</a></li>");
					}
				}
				removedUsers = []; 
			} else {
				for (var userId in removedUsers) {
					var term = jQuery("#term").val();
					if (removedUsers[userId].toLowerCase().indexOf(term.toLowerCase()) >= 0 
						&& $('div#existing li#' + userId + ', div#potential li#' + userId).length == 0) {
						jQuery("div#potential").append("<li id=\"" + userId + "\"><a class='addLink'>" + removedUsers[userId] + "</a></li>");
						delete removedUsers[userId];
					}
				}
			}
	
			bindAddLink();
			updatePotentialTotal();
		}
		
		function populateForm() {
			let userIds = '';
			jQuery("li", "div#existing").each(function() {
				userIds += $(this).attr("id") + ',';
			});
			$('#userOrgForm #userIds').val(userIds);
			return true;
		}
	</script>
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.user.add"/></c:set>
	<lams:Page type="admin" title="${title}">
				<p>
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
				    <c:if test="${not empty pOrgId}">
				        : <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="pOrgId" />" class="btn btn-default"><c:out value="${pOrgName}"/></a>
				    </c:if>
				    <c:if test="${userOrgForm.orgId != 1}">
						: <a href="<lams:LAMSURL/>admin/<c:if test="${orgType == 3}">user</c:if><c:if test="${orgType != 3}">org</c:if>manage.do?org=<c:out value="${userOrgForm.orgId}" />" class="btn btn-default">
						<c:out value="${userOrgForm.orgName}"/></a>
					</c:if>
					<c:if test="${userOrgForm.orgId == 1}">
						: <a href="<lams:LAMSURL/>admin/usermanage.do?org=<c:out value="${userOrgForm.orgId}" />" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
					</c:if>
				</p>
				
				<h3><fmt:message key="admin.user.add"/></h3>
				
				<div align="center">
				</div>
				
				<c:if test="${orgType == 2}">
					<lams:Alert id="subgroup-warning" type="info" close="false">
						<fmt:message key="msg.remove.from.subgroups"/>
					</lams:Alert>
				</c:if>
				
				<h4><fmt:message key="heading.users"/></h4>
				<div class="pull-right"><span id="totalUsers"><c:out value="${numExistUsers}"/></span></div>
				<p>
					 <fmt:message key="msg.click.remove.user"/>
				</p>
				<div class="listBoundingBox">
					<div id="existing" class="listScrollable">
					</div>
				</div>
				
				<p>&nbsp;</p>
				
				<h4><fmt:message key="heading.potential.users"/></h4>
				<p>
					<fmt:message key="msg.click.add.user"/>
				</p>
				<div class="pull-right"><span id="potentialUsers"><c:out value="${numPotentialUsers}"/></span></div>
				<form onsubmit="return loadSearchResults(0);">
					<p>
						<fmt:message key="admin.search"/>: <input id="term" type="text"/> or <a onclick="loadSearchResults(1);"><fmt:message key="msg.show.all.potential.users"/></a>
					</p>
				</form>
				<div class="listBoundingBox">
					<div id="potential" class="listScrollable">
					</div>
				</div>
				
				<p>&nbsp;</p>
				
				<div id="form" class="pull-right">
					<form:form action="./userorgsave.do" modelAttribute="userOrgForm" id="userOrgForm" method="post">
						<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
						<form:hidden path="orgId" />
						<form:hidden path="userIds" />
						<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
						<input type="submit" id="nextButton" class="btn btn-primary loffset5" onclick="return populateForm();" value="<fmt:message key="label.next"/>" />
					</form:form>
				</div>
	</lams:Page>

</body>
</lams:html>
