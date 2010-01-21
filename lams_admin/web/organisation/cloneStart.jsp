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
<script language="JavaScript" type="text/javascript">
	<!--
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
		jQuery("#lessonDialog-"+lessonId).dialog({ modal: true });
		return false;
	}

	function staffDialog() {
		jQuery("#staffDialog").load("<lams:LAMSURL/>/admin/clone.do", { method: "selectStaff", groupId: <c:out value="${org.organisationId}" /> });
		jQuery("#staffDialog").dialog({ modal: true });
		return false;
	}

	function learnerDialog() {
		jQuery("#learnerDialog").load("<lams:LAMSURL/>/admin/clone.do", { method: "selectLearners", groupId: <c:out value="${org.organisationId}" /> });
		jQuery("#learnerDialog").dialog({ modal: true });
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
		/*
		jQuery.post("<lams:LAMSURL/>/admin/clone.do", {
			method: "clone",
			groupId: <c:out value="${org.organisationId}" />,
			'lessons[]': [ lessons.join(",") ],
			'staff[]': [ staff.join(",") ],
			'learners[]': [ learners.join(",") ],
			addAllStaff: jQuery("#addAllStaff").is(":checked"),
			addAllLearners: jQuery("#addAllLearners").is(":checked")
		});
		*/
		jQuery("input[name=lessons]").val(lessons.join(","));
		jQuery("input[name=staff]").val(staff.join(","));
		jQuery("input[name=learners]").val(learners.join(","));
		jQuery("input[name=addAllStaff]").val(jQuery("#addAllStaff").is(":checked"));
		jQuery("input[name=addAllLearners]").val(jQuery("#addAllLearners").is(":checked"));
		jQuery("#cloneForm").submit();
		return true;
	}
	//-->
</script>

<tiles:insert attribute="breadcrumbs" />

<h1>Clone Lessons for <c:out value="${org.name}" /></h1>

<c:if test="${not empty errors}">
	<p class="warning">
		<c:forEach items="${errors}" var="error">
			<c:out value="${error}" />
		</c:forEach>
	</p>
</c:if>

<h2>Choose group to clone lessons from</h2>
<div class="clone-box">
	<p>
		Group:
		<select id="sourceGroupId">
			<option value="">...</option>
		</select>
	</p>
	<p class="padding-bottom">
		Subgroup:
		<select id="sourceSubgroupId">
		</select>
	</p>
	<p>
		<input type="submit" class="button" value="Choose" onclick="javascript:loadGroupAttributes(chosenGroup());">
	</p>
</div>

<form name="cloneForm" id="cloneForm" action="<lams:LAMSURL/>/admin/clone.do" method="post">
	<input type="hidden" name="method" value="clone">
	<input type="hidden" name="groupId" value="<c:out value="${org.organisationId}" />">
	<input type="hidden" name="lessons">
	<input type="hidden" name="staff">
	<input type="hidden" name="learners">

	<div style="display:none;" id="cloneOptionsDiv">
		<h2>Select lessons</h2>
		<p class="clone-box" id="availableLessons"></p>
		
		<h2>Select staff</h2>
		<div class="clone-box">
			<p class="padding-bottom">
				<input type="checkbox" id="addAllStaff" name="addAllStaff" checked="checked"> Add all monitors in this group to each lesson?
			</p>
			<p>
				<a onclick="staffDialog();">Configure Staff</a>
			</p>
		</div>
		
		<h2>Select learners</h2>
		<div class="clone-box">
			<p class="padding-bottom">
				<input type="checkbox" id="addAllLearners" name="addAllLearners" checked="checked"> Add all learners in this group to each lesson?
			</p>
			<p>
				<a onclick="learnerDialog();">Configure Learners</a>
			</p>
		</div>
		
		<p style="float:right;">
			<input type="submit" class="button" onclick="clone();" value="Clone">
		</p>
		<p class="padding-bottom"></p>
	</div>
	
	<div id="staffDialog" style="display:none;">
	
	</div>
	
	<div id="learnerDialog" style="display:none;">
	
	</div>

</form>