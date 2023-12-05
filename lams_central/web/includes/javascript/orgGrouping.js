function removeGrouping(groupingId) {
	if (confirm(LABELS.REMOVE_GROUPING_CONFIRM_LABEL)) {
		//dynamically create a form and submit it
		var form = $('<form method="post" action="' +  LAMS_URL + 'grouping/removeGrouping.do"></form>');

		var hiddenField = $('<input type="hidden" name="organisationID" value="' + organisationId + '"></input>');
		form.append(hiddenField);

		hiddenField = $('<input type="hidden" name="groupingId" value="' + groupingId + '"></input>');
		form.append(hiddenField);

		hiddenField = $('<input type="hidden" name="lessonID" value="' + lessonId + '"></input>');
		form.append(hiddenField);

		hiddenField = $('<input type="hidden" name="activityID" value="' + activityId + '"></input>');
		form.append(hiddenField);

		hiddenField = $('<input type="hidden" name="' + csrfTokenName + '" value="' + csrfTokenValue + '"></input>');
		form.append(hiddenField);

		// The form needs to be a part of the document in order to be submitted
		$(document.body).append(form);
		form.submit();
	}
}

function viewGroups(groupingId, force) {
	var url = LAMS_URL + 'grouping/viewGroups.do?organisationID=' + organisationId
		+ '&targetOrganisationID=' + targetOrganisationId;
	if (lessonId) {
		url += '&lessonID=' + lessonId;
	}
	// no grouping ID means we open a brand new grouping
	if (groupingId) {
		url += '&groupingId=' + groupingId;
	}

	if (lessonMode) {
		var executeShow = true;
		if (groupingId && !force) {
			// make sure user want to use this grouping
			var groupingName = $('#grouping-' + groupingId + ' .groupingName').text().trim();
			executeShow = confirm(LABELS.USE_GROUPING_CONFIRM_LABEL.replace('[0]', groupingName));
		}
		if (executeShow) {
			// load to current iframe
			url +=  '&activityID=' + groupingActivityId;
			document.location.href = url;
		}
	} else {
		document.location.href = url;
	}
}
/**
 * Opens dialog for matching course groups to groups assigned to branches in a branching activity.
 */
function openGroupMappingDialog(groupingId) {
	/* dialog and labels are called "branching" because
	 * 1) it was taken from Authoring dialog for groups-to-branches mapping
	 * 2) two types of groups: course and branching could easily get mixed
	 */

	let gtbDialog = $('#groupMappingDialogContents').clone().attr('id', 'groupMappingDialog');
	$('#branchMappingOKButton', gtbDialog).click(function(){
		var dialog = $(this).closest('.modal'),
			groupsToBranches = [];

		// fill JSON with group pairs
		$('.branchMappingBoundItemCell div, .branchMappingFreeItemCell div', dialog).each(function(){
			var groupID = $(this).attr('id'),
				boundItem = $(this).data('boundItem'),
				branchID = boundItem ? boundItem.attr('id') : null;

			// add the mapping
			if (branchID) {
				groupsToBranches.push({
					'groupID'  : groupID,
					'branchID' : branchID
				});
			}
		});

		var data = {
			'mapping' : JSON.stringify(groupsToBranches)
		};
		data[csrfTokenName] = csrfTokenValue;

		// save the mapping
		$.ajax({
			url : LAMS_URL + 'grouping/saveGroupMappings.do',
			data : data,
			type : 'POST',
			success : function(response) {
				// LAMS can reply 200 even if there is an error, so we need OK response
				if (response == 'OK') {
					// go straight to branching groups for final check
					viewGroups(groupingId, true);
				}
			}
		});
	});

	// initialise the dialog buttons
	$('.branchMappingAddButton', gtbDialog).click(function(){
		addGroupMapping();
	});
	$('.branchMappingRemoveButton', gtbDialog).click(function(){
		removeGroupMapping();
	});

	let groupsCell = $('.branchMappingFreeItemCell', gtbDialog),
		branchesCell = $('.branchMappingFreeBranchCell', gtbDialog),
		groupCell = $('.branchMappingBoundItemCell', gtbDialog),
		branchCell = $('.branchMappingBoundBranchCell', gtbDialog);

	// clear out previous entries
	$('.branchMappingListCell', gtbDialog).empty();

	// fetch course and branching groups
	$.ajax({
		url : LAMS_URL + 'grouping/getGroupsForMapping.do',
		data : {
			'groupingId' 		  : groupingId,
			'activityID' 		  : groupingActivityId
		},
		dataType : 'json',
		success : function(response) {
			// fill table with course and branching groups
			$.each(response.groups, function(){
				var group = this,
					groupElem = $('<div />').click(selectGroupMappingListItem)
						.text(group.name).attr('id', group.id);

				$.each(response.branches, function() {
					// check if a branching group alread exists with the same name as a course group
					if (this.name == group.name) {
						var branchElem = $('<div />').click(selectGroupMappingListItem)
							.appendTo(branchCell)
							.text(this.name)
							.attr('id', this.id)
							.data('boundItem', groupElem);
						groupElem.appendTo(groupCell).data('boundItem', branchElem);
						groupElem = null;
						return false;
					}
				});

				if (groupElem) {
					// no existing mapping was found, make the group available for mapping
					groupElem.appendTo(groupsCell);
				}
			});
			// fill in branch groups
			$.each(response.branches, function(){
				$('<div />').click(selectGroupMappingListItem).appendTo(branchesCell)
					.text(this.name).attr('id', this.id);
			});
		}
	});


	gtbDialog.modal('show');
};

/**
 * Make a pair out of selected groups.
 */
function addGroupMapping(){
	var dialog = $('#groupMappingDialog'),
		selectedItem = $('.branchMappingFreeItemCell .selected', dialog),
		selectedBranch =  $('.branchMappingFreeBranchCell .selected', dialog);

	if (selectedItem.length != 1 || selectedBranch.length != 1) {
		return;
	}

	// original branch stays in its list
	selectedBranch = selectedBranch.clone().click(selectGroupMappingListItem);
	// add info about the pair for later reference
	selectedItem.data('boundItem', selectedBranch);
	selectedBranch.data('boundItem', selectedItem);
	var itemCell = $('.branchMappingBoundItemCell', dialog),
		branchCell = $('.branchMappingBoundBranchCell', dialog);
	// clear existing selection
	$('.selected', itemCell).removeClass('selected');
	$('.selected', branchCell).removeClass('selected');
	itemCell.append(selectedItem);
	branchCell.append(selectedBranch);
}

function removeGroupMapping() {
	var dialog = $('#groupMappingDialog'),
		selectedItem = $('.branchMappingBoundItemCell .selected', dialog),
		selectedBranch =  $('.branchMappingBoundBranchCell .selected', dialog);

	if (selectedItem.length != 1 || selectedBranch.length != 1) {
		return;
	}

	selectedItem.removeData('boundItem');
	selectedBranch.remove();
	$('.branchMappingFreeItemCell', dialog).append(selectedItem);
}

/**
 * Highlight clicked group
 */
function selectGroupMappingListItem(){
	var item = $(this),
		boundItem = item.data('boundItem');

	item.siblings().removeClass('selected');
	item.addClass('selected');

	if (boundItem) {
		boundItem.siblings().removeClass('selected');
		boundItem.addClass('selected');
	}
}