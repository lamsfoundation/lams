<%@ include file="/taglibs.jsp"%>

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
<script>
	var removedUsers = [];

	jQuery(document).ready(function() {
		jQuery("div#existing").load(
			"user/basiclist.do", 
			{orgId: <bean:write name="UserOrgForm" property="orgId"/>},
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
		var size = jQuery("li", "#existing").size();
		jQuery("span#totalUsers").text("<fmt:message key="label.number.of.users"><fmt:param>"+size+"</fmt:param></fmt:message>");
	}
	
	function updatePotentialTotal() {
		var size = jQuery("li", "#potential").size();
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
				{orgId: <bean:write name="UserOrgForm" property="orgId"/>,
				potential: potential}, 
				function() {
					loadSearchResultsCallback(potential);
				}
			);
		} else {
			jQuery("div#potential").load(
				"user/searchsingle.do",
				{term: jQuery("#term").val(),
				orgId: <bean:write name="UserOrgForm" property="orgId"/>}, 
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

<p>
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
    <logic:notEmpty name="pOrgId">
        : <a href="orgmanage.do?org=<bean:write name="pOrgId" />" class="btn btn-default"><bean:write name="pOrgName"/></a>
    </logic:notEmpty>
    <logic:notEqual name="UserOrgForm" property="orgId" value="1">
		: <a href="<logic:equal name="orgType" value="3">user</logic:equal><logic:notEqual name="orgType" value="3">org</logic:notEqual>manage.do?org=<bean:write name="UserOrgForm" property="orgId" />" class="btn btn-default">
		<bean:write name="UserOrgForm" property="orgName"/></a>
	</logic:notEqual>
	<logic:equal name="UserOrgForm" property="orgId" value="1">
		: <a href="usermanage.do?org=<bean:write name="UserOrgForm" property="orgId" />" class="btn btn-default"><fmt:message key="admin.global.roles.manage" /></a>
	</logic:equal>
</p>

<h4><fmt:message key="admin.user.add"/></h4>

<div align="center"><html-el:errors/></div>

<logic:equal name="orgType" value="2">
	<lams:Alert id="subgroup-warning" type="info" close="false">
		<fmt:message key="msg.remove.from.subgroups"/>
	</lams:Alert>
</logic:equal>

<h3><fmt:message key="heading.users"/></h3>
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
	<html:form action="userorgsave.do" method="post">
		<html:hidden property="orgId" />
		<html:cancel styleId="cancelButton" styleClass="btn btn-default"><fmt:message key="admin.cancel"/></html:cancel>
		<html:submit styleId="nextButton" styleClass="btn btn-primary loffset5" onclick="return populateForm();"><fmt:message key="label.next"/></html:submit>
	</html:form>
</div>
