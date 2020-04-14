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
			jQuery("li", "div#existing").each(function() {
				jQuery("form", "div#form").append("<input type='checkbox' name='userIds' value='"+jQuery(this).attr("id")+"' checked='checked' style='display:none;'>");
			});
			return true;
		}
	</script>
</lams:head>
    
<body class="stripes">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/orgmanage.do?org=1" | <fmt:message key="admin.course.manage" /></c:set>
    <c:if test="${not empty pOrgId}">
    	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=${pOrgId} | <c:out value="${pOrgCode}" escapeXml="true"/> : <c:out value="${pOrgName}" escapeXml="true"/></c:set>
    </c:if>
    <c:if test="${userOrgForm.orgId != 1}">
    	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/<c:if test="${orgType == 3}">user</c:if><c:if test="${orgType != 3}">org</c:if>manage.do?org=${userOrgForm.orgId} | <c:out value="${userOrgForm.orgCode}" escapeXml="true"/> : <c:out value="${userOrgForm.orgName}" escapeXml="true"/></c:set>
    	<c:set var="breadcrumbItems">${breadcrumbItems}, javascript:history.go(-1) | <fmt:message key="admin.user.management"/></c:set>
    	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.user.add"/></c:set>
	</c:if>
	<c:if test="${userOrgForm.orgId == 1}">
		<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/usermanage.do?org=${userOrgForm.orgId} | <fmt:message key="admin.global.roles.manage" /></c:set>
	</c:if>	


	<lams:Page type="admin" breadcrumbItems="${breadcrumbItems}">
		<div class="row">
			<div class="col bg-light ml-3 mr-3 p-4">


				<h1><c:out value="${userOrgForm.orgName}" escapeXml="true"/>: <fmt:message key="admin.user.add"/></h1>
				
				<c:if test="${orgType == 2}">
					<lams:Alert id="subgroup-warning" type="info" close="false">
						<fmt:message key="msg.remove.from.subgroups"/>
					</lams:Alert>
				</c:if>

				
				<h2><fmt:message key="heading.users"/></h2>
				<div class="pull-right"><span id="totalUsers"><c:out value="${numExistUsers}"/></span></div>
				<p>
					 <fmt:message key="msg.click.remove.user"/>
				</p>
				<div class="listBoundingBox border-success bg-white">
					<div id="existing" class="listScrollable">
					</div>
				</div>
				
				
				<h2 class="mt-3"><fmt:message key="heading.potential.users"/></h2>
				<p>
					<fmt:message key="msg.click.add.user"/>
				</p>
				<div class="pull-right"><span id="potentialUsers"><c:out value="${numPotentialUsers}"/></span></div>
				<form onsubmit="return loadSearchResults(0);">
					<p>
						<label for="term"><fmt:message key="admin.search"/></label>: <input id="term" class="form-control-inline form-control-sm" type="text"/> or <a onclick="loadSearchResults(1);" ><fmt:message key="msg.show.all.potential.users"/></a>
					</p>
				</form>
				<div class="listBoundingBox border-secondary bg-white">
					<div id="potential" class="listScrollable">
					</div>
				</div>
								
				<div id="form" class="pull-right mt-3 mb-1">
					<form:form action="./userorgsave.do" modelAttribute="userOrgForm" id="userOrgForm" method="post">
						<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
						<form:hidden path="orgId" />
						<a href="javascript:history.go(-1)" class="btn btn-default"><fmt:message key="admin.cancel"/></a>
						<input type="submit" id="nextButton" class="btn btn-primary ml-2" onclick="return populateForm();" value="<fmt:message key="label.next"/>" />
					</form:form>
				</div>
			</div>
		</div>				
	</lams:Page>

</body>
</lams:html>
