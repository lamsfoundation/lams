$(document).ready(function() {
	$(".ui-button").button();
});

function removeGrouping(groupingId) {
	if (!lessonMode && confirm(LABELS.REMOVE_GROUPING_CONFIRM_LABEL)) {
		document.location.href = "OrganisationGroup.do?method=removeGrouping&organisationID="
			+ organisationId + "&groupingId=" + groupingId;
	}
}

function showGroups(groupingId) {
	var url = LAMS_URL + 'OrganisationGroup.do?method=viewGroups&organisationID='
		+ organisationId;
	if (lessonId) {
		url += '&lessonID=' + lessonId;
	}
	// no grouping ID means we open a brand new grouping
	if (groupingId) {
		url += '&groupingId=' + groupingId;
	}
	
	if (lessonMode) {
		var executeShow = true;
		if (groupingId) {
			// make sure user want to use this grouping
			var groupingName = $('#grouping-' + groupingId + ' .groupingName').text().trim();
			executeShow = confirm(LABELS.USE_GROUPING_CONFIRM_LABEL.replace('[0]', groupingName));
		}
		if (executeShow) {
			// load to current iframe
			document.location.href = url + '&activityID=' + groupingActivityId;
		}
	} else {
		// load to dialog
		window.parent.loadOrgGroupDialogContents(null, 880, 460, url);
	}
}