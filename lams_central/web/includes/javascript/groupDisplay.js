/**
* Called from within Course Groups dialog, it saves groups and loads grouping
* page.
*/
function saveOrgGroups() {
	var groupsSaved = $('#dialogOrgGroup iframe')[0].contentWindow.saveGroups();
	if (groupsSaved) {
		showOrgGroupDialogContents(null, 460, 460,
			LAMS_URL + 'OrganisationGroup.do?method=viewGroupings&organisationID='
			         + $('#dialogOrgGroup').dialog('option', 'orgID'));
	}
}

function showOrgGroupDialogContents(title, width, height, url) {
	var id = "dialogOrgGroup",
		dialog = $('#' + id),
		exists = dialog.length > 0,
		orgID = null;
	if (exists) {
		if (!title) {
			title = $('.modal-title', dialog).text();
		}
		orgID = dialog.data('orgID');
	}
	showDialog(id, {
		'data' : {
			'orgID'  : orgID
		},
		'height' : height,
		'width'  : width,
		'title'  : title,
		'open'   : function() {
			$('iframe', this).attr('src', url);
		}
	}, true, exists);
}