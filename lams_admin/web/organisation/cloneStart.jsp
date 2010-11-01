<%@ include file="/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>/includes/javascript/jquery-ui/themes/base/jquery-ui.css">
<style type="text/css">
.clone-box {
	border: 1px solid grey; padding: 10px; margin-top: 10px;
}
.padding-bottom {
	padding-bottom: 20px;
}
</style>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-ui/ui/ui.core.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-ui/ui/ui.dialog.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-ui/ui/ui.resizable.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.tablesorter.pager.js"></script>
<script language="JavaScript" type="text/javascript">
	<!--
	var staffLoaded, learnersLoaded = false;
	var lessonsLoaded = {};

	jQuery(document).ready(function() {
		jQuery("#sourceGroupId").change(function() {
			loadSubgroups(jQuery("#sourceGroupId").val());
		});
		jQuery("#sourceGroupId").load("<lams:LAMSURL/>/admin/clone.do", { method: "getGroups" }, function() {
			loadSubgroups(jQuery("#sourceGroupId").val());
		});
		jQuery("a.lessonNameLink").live("click", function() {
			lessonDialog(jQuery(this).attr("id"));
		});
	});

	function loadSubgroups(groupId) {
		jQuery("#sourceSubgroupId").load("<lams:LAMSURL/>/admin/clone.do", { method: "getSubgroups", groupId: groupId });
	}

	function chosenGroup() {
		if (jQuery("#sourceSubgroupId").val() != '') {
			return jQuery("#sourceSubgroupId").val();
		} else {
			return jQuery("#sourceGroupId").val();
		}
	}

	function loadGroupAttributes(sourceGroupId) {
		jQuery("#cloneOptionsDiv").show();
		jQuery("#availableLessons").load("<lams:LAMSURL/>/admin/clone.do", { method: "availableLessons", sourceGroupId: sourceGroupId });
	}

	function lessonDialog(lessonId) {
		if (lessonsLoaded[lessonId] == null || !lessonsLoaded[lessonId]) {
			jQuery("#lessonDialog-"+lessonId).dialog({
				autoOpen: false,
				modal: true
			});
			lessonsLoaded[lessonId] = true;
		}
		jQuery("#lessonDialog-"+lessonId).dialog("open");
		return false;
	}

	function initUserDialog(selector) {
		jQuery(selector).dialog({
			autoOpen: false,
			modal: true, 
			width: 500, 
			buttons: { 
				"<fmt:message key='label.ok' />": function() {
					jQuery(this).dialog("close");
				} 
			} 
		});
	}

	function staffDialog() {
		if (!staffLoaded) {
			jQuery("#staffDialog").load("<lams:LAMSURL/>/admin/clone.do", { method: "selectStaff", groupId: <c:out value="${org.organisationId}" /> });
			initUserDialog("#staffDialog");
			staffLoaded = true;
		}
		jQuery("#staffDialog").dialog("open");
		return false;
	}

	function learnerDialog() {
		if (!learnersLoaded) {
			jQuery("#learnerDialog").load("<lams:LAMSURL/>/admin/clone.do", { method: "selectLearners", groupId: <c:out value="${org.organisationId}" /> });
			initUserDialog("#learnerDialog");
			learnersLoaded = true;
		}
		jQuery("#learnerDialog").dialog("open");
		return false;
	}

	function clone() {
		var lessons = [];
		var staff = [];
		var learners = [];
		jQuery("input[name=lessons]:checked").each(function() {
			lessons.push(jQuery(this).val());
		});
		jQuery("input[name=staff]:checked").each(function() {
			staff.push(jQuery(this).val());
		});
		jQuery("input[name=learners]:checked").each(function() {
			learners.push(jQuery(this).val());
		});
		jQuery("input[name=lessons]").val(lessons.join(","));
		jQuery("input[name=staff]").val(staff.join(","));
		jQuery("input[name=learners]").val(learners.join(","));
		jQuery("input[name=addAllStaff]").val(jQuery("#addAllStaff").is(":checked"));
		jQuery("input[name=addAllLearners]").val(jQuery("#addAllLearners").is(":checked"));
		return true;
	}
	//-->
</script>

<tiles:insert attribute="breadcrumbs" />

<h1><fmt:message key="title.clone.lessons.for"><fmt:param value="${org.name}" /></fmt:message></h1>

<c:if test="${not empty errors}">
	<p class="warning">
		<c:forEach items="${errors}" var="error">
			<c:out value="${error}" />
		</c:forEach>
	</p>
</c:if>

<h2><fmt:message key="title.choose.group" /></h2>
<div class="clone-box">
	<p>
		<fmt:message key="admin.course" />:
		<select id="sourceGroupId">
			<option value="">...</option>
		</select>
	</p>
	<p class="padding-bottom">
		<fmt:message key="admin.class" />:
		<select id="sourceSubgroupId">
		</select>
	</p>
	<p>
		<input type="submit" class="button" value="<fmt:message key="label.choose" />" onclick="javascript:loadGroupAttributes(chosenGroup());">
	</p>
</div>

<form name="cloneForm" id="cloneForm" action="<lams:LAMSURL/>/admin/clone.do" method="post">
	<input type="hidden" name="method" value="clone">
	<input type="hidden" name="groupId" value="<c:out value="${org.organisationId}" />">
	<input type="hidden" name="lessons">
	<input type="hidden" name="staff">
	<input type="hidden" name="learners">

	<div style="display:none;" id="cloneOptionsDiv">
		<h2><fmt:message key="title.select.lessons" /></h2>
		<p class="clone-box" id="availableLessons"></p>
		
		<h2><fmt:message key="title.select.staff" /></h2>
		<div class="clone-box">
			<p class="padding-bottom">
				<input type="checkbox" id="addAllStaff" name="addAllStaff" checked="checked"> <fmt:message key="message.add.all.monitors" />
			</p>
			<p>
				<a onclick="staffDialog();"><fmt:message key="label.configure.staff" /></a>
			</p>
		</div>
		
		<h2><fmt:message key="title.select.learners" /></h2>
		<div class="clone-box">
			<p class="padding-bottom">
				<input type="checkbox" id="addAllLearners" name="addAllLearners" checked="checked"> <fmt:message key="message.add.all.learners" />
			</p>
			<p>
				<a onclick="learnerDialog();"><fmt:message key="label.configure.learners" /></a>
			</p>
		</div>
		
		<p style="float:right;">
			<input type="submit" class="button" onclick="return clone();" value="<fmt:message key="label.clone" />">
		</p>
		<p class="padding-bottom"></p>
	</div>
	
	<div id="staffDialog" title="<fmt:message key="label.configure.staff" />" style="display:none;">
	
	</div>
	
	<div id="learnerDialog" title="<fmt:message key="label.configure.learners" />" style="display:none;">
	
	</div>

</form>