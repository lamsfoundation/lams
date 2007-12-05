<%@ include file="/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-1.1.4.pack.js"></script>
<script>
	<!--
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
		jQuery("span#totalUsers").text(jQuery("li", "#existing").size());
	}
	
	function updatePotentialTotal() {
		jQuery("span#potentialUsers").text(jQuery("li", "#potential").size());
	}
	
	function bindRemoveLink() {
		jQuery("body").click(function(event) {
			if (jQuery(event.target).is("a.removeLink")) {
				if (jQuery(event.target).parent().is("li")) {
					jQuery(event.target).parent().remove();
					updateExistingTotal();
				}
			}
		});
	}
	
	function bindAddLink() {
		jQuery("a.addLink").click(function() {
			var rowText = jQuery(this).parent().text();
			var rowId = jQuery(this).parent().attr("id");
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
					loadSearchResultsCallback();
				}
			);
		} else {
			jQuery("div#potential").load(
				"user/searchsingle.do",
				{term: jQuery("#term").val(),
				orgId: <bean:write name="UserOrgForm" property="orgId"/>}, 
				function() {
					loadSearchResultsCallback();
				}
			);
		}
		return false;
	}
	
	function loadSearchResultsCallback() {
		updatePotentialTotal();
		jQuery("li", this).each(function() {
			var rowHtml = jQuery(this).html();
			jQuery(this).html("<a class='addLink'>"+rowHtml+"</a>");
		});
		bindAddLink();
	}
	
	function populateForm() {
		jQuery("li", "div#existing").each(function() {
			jQuery("form", "div#form").append("<input type='checkbox' name='userIds' value='"+jQuery(this).attr("id")+"' checked='checked' style='display:none;'>");
		});
		return true;
	}
	//-->
</script>

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
.floatRight {
	float:right;
	margin-top:5px;
}
</style>

<h2 class="align-left">
	<a href="orgmanage.do?org=1"><fmt:message key="admin.course.manage" /></a>
    <logic:notEmpty name="pOrgId">
        : <a href="orgmanage.do?org=<bean:write name="pOrgId" />"><bean:write name="pOrgName"/></a>
    </logic:notEmpty>
    <logic:notEqual name="UserOrgForm" property="orgId" value="1">
		: <a href="<logic:equal name="orgType" value="3">user</logic:equal><logic:notEqual name="orgType" value="3">org</logic:notEqual>manage.do?org=<bean:write name="UserOrgForm" property="orgId" />">
		<bean:write name="UserOrgForm" property="orgName"/></a>
	</logic:notEqual>
	<logic:equal name="UserOrgForm" property="orgId" value="1">
		: <a href="usermanage.do?org=<bean:write name="UserOrgForm" property="orgId" />"><fmt:message key="admin.global.roles.manage" /></a>
	</logic:equal>
    : <fmt:message key="admin.user.add"/>
</h2>

<p>&nbsp;</p>

<div align="center"><html-el:errors/></div>

<logic:equal name="orgType" value="2">
	<p><fmt:message key="msg.remove.from.subgroups"/></p>
</logic:equal>

<h3><fmt:message key="heading.users"/></h3>
<div class="floatRight"><span id="totalUsers">0</span> <fmt:message key="label.number.of.users"/></div>
<p>
	 <fmt:message key="msg.click.remove.user"/>
</p>
<div class="listBoundingBox">
	<div id="existing" class="listScrollable">
	</div>
</div>

<p>&nbsp;</p>

<h3><fmt:message key="heading.potential.users"/></h3>
<p>
	<fmt:message key="msg.click.add.user"/>
</p>
<div class="floatRight"><span id="potentialUsers">0</span> <fmt:message key="label.number.of.potential.users"/></div>
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

<div id="form" class="floatRight">
	<html:form action="userorgsave.do" method="post">
		<html:hidden property="orgId" />
		<html:submit styleClass="button" onclick="return populateForm();"><fmt:message key="label.next"/></html:submit>
		<html:cancel styleClass="button"><fmt:message key="admin.cancel"/></html:cancel>
	</html:form>
</div>

<p>&nbsp;</p>
