﻿﻿var gtbDialog = null;

$(document).ready(function() {
	$(".ui-button").button();
});

function removeGrouping(groupingId) {
	if (!lessonMode && confirm(LABELS.REMOVE_GROUPING_CONFIRM_LABEL)) {
		document.location.href = "OrganisationGroup.do?method=removeGrouping&organisationID="
			+ organisationId + "&groupingId=" + groupingId;
	}
}

function viewGroups(groupingId, force) {
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
		if (groupingId && !force) {
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
		window.parent.showOrgGroupDialogContents(null, 880, 500, url);
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
	if (!gtbDialog) {
		// create the dialog
		gtbDialog = $('#groupMappingDialog')
			.dialog({
				'autoOpen' : false,
				'modal'    : true,
				'show'     : 'fold',
				'hide'     : 'fold',
				'width'    : 650,
				'height'   : 400,
				'title'    : LABELS.COURSE_GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE,
				'buttons' : [
				             {
				            	'text'   : LABELS.OK_BUTTON,
				            	'click'  : function() {
				            		var dialog = $(this),
				            			groupsToBranches = [];
			
				            		// fill JSON with group pairs
				            		$('.branchMappingBoundItemCell div, .branchMappingFreeItemCell div', dialog)
				            			.each(function(){
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
				            		
				            		// save the mapping
				            		$.ajax({
										url : LAMS_URL + 'OrganisationGroup.do',
										data : {
											'method'  : 'saveGroupMappings',
											'mapping' : JSON.stringify(groupsToBranches)
										},
										success : function(response) {
											// LAMS can reply 200 even if there is an error, so we need OK response
											if (response == 'OK') {
												// go straight to branching groups for final check
												viewGroups(groupingId, true);
											}
										}
				            		});
				            	}
				             }
				],
				'open' : function(){
					var dialog = $(this),
						groupsCell = $('.branchMappingFreeItemCell', dialog),
						branchesCell = $('.branchMappingFreeBranchCell', dialog),
						groupCell = $('.branchMappingBoundItemCell', dialog),
						branchCell = $('.branchMappingBoundBranchCell', dialog);
					
					// clear out previous entries
					$('.branchMappingListCell', dialog).empty();
					
					// fetch course and branching groups
					$.ajax({
						url : LAMS_URL + 'OrganisationGroup.do',
						data : {
							'method'			  : 'getGroupsForMapping',
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
				}
		});
	
		// initialise buttons and labels
		$('.branchMappingAddButton', gtbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-next'
			},
			'text' : false
		}).click(function(){
			addGroupMapping();
		});
		$('.branchMappingRemoveButton', gtbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-prev'
			},
			'text' : false
		}).click(function(){
			removeGroupMapping();
		});
	}
	
	gtbDialog.dialog('open');
};

/**
 * Make a pair out of selected groups.
 */
function addGroupMapping(){
	var dialog = gtbDialog,
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
	var dialog = gtbDialog,
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