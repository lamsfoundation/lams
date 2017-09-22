// for user selecting and sorting purposes
var sortOrderAscending = {};
var lastSelectedUsers = {};

$(document).ready(function(){
	$('.ui-button').button();
	// show unassigned users on the left
	fillGroup(unassignedUsers, $('#unassignedUserCell'));
	// add existing groups
	$.each(grouping.groups, function(){
		// if a course grouping is being copied as lesson grouping,
		// do not use group IDs as they will be created when assiging users to created groups
		var groupId = !lessonMode || skipAssigningWhenCreatingGroup ? this.groupId : null;
		var group = addGroup(groupId, this.name, this.users);
		if (this.locked) {
			markGroupLocked(group);
		}
	});
	// after initial group creation, all new groups will be created on server
	skipAssigningWhenCreatingGroup = false;
	
	// initialisation based on mode
	if (!lessonMode) {
		$('#groupingName').val(grouping.name);
		
		// ability to save a grouping on pressing the Enter key in the name input field
		$('#groupingName').on('keyup', function (e) {
			//remove alert class if it was applied
			$("#grouping-name-blank-error,#grouping-name-non-unique-error").hide();
			
		    if (e.keyCode == 13) {
				saveGroups();
		    }
		});
	}
	
	if (canEdit) {
		// allow adding new groups
		$('#newGroupPlaceholder').click(function(){
			// the label is "Group X" where X is the top group number
			addGroup(null, LABELS.GROUP_PREFIX_LABEL + ' ' + $('#groupsTable .groupContainer').length, null);
		});
	}
});


/**
 * Add a group and fills it with given users.
 */
function addGroup(groupId, name, users) {
	var group = $('#groupTemplate').clone()
					.attr({
						// remove template's HTML ID
						'id'      : null,
						'groupId' : groupId
					})
					.css('display', null);
	
	var groupNameFields = $('#groupsTable .groupContainer input');
	var groupTopIndex = groupNameFields.length;
	// names can be blank in course groups,
	// but in lesson groups they have to exist and be unique
	if (lessonMode && !name) {
		name = LABELS.GROUP_PREFIX_LABEL + ' ' + groupTopIndex;
	}
	var nameIsUnique;
	do {
		nameIsUnique = true;
		groupNameFields.each(function(){
			if (name == $(this).val()) {
				nameIsUnique = false;
				return false;
			}
		});
		if (!nameIsUnique) {
			groupTopIndex++;
			name = LABELS.GROUP_PREFIX_LABEL + ' ' + groupTopIndex;
		}
	} while (!nameIsUnique);
	
	group.find('input').val(name);
	
	var newGroupPlaceholder = $('#newGroupPlaceholder');
	if (newGroupPlaceholder.length > 0) {
		group.insertBefore(newGroupPlaceholder);
	} else {
		// there is no placeholder in read-only mode
		$('#groupsCell').append(group);
	}
	
	fillGroup(users, group);
	
	return group;
}


/**
 * Makes a list of users and adds drag&drop functionality to them.
 */
function fillGroup(users, container) {
	// make calls to server or just create users in HTML
	var createOnServer = lessonMode && !skipAssigningWhenCreatingGroup
									&& container.attr('id') != 'unassignedUserCell';
	if (users) {
		var userDivs = [],
			userIds = [];
		// create user DIVs
		$.each(users, function(index, userJSON) {
			var userDiv = $('<div />').attr('userId', userJSON.id)
				.addClass('draggableItem');
			var portraitDiv = $('<div />').attr({
				'id': 'portrait-'+userJSON.id,
				})
				.addClass('roffset5')
				.appendTo(userDiv);
			addPortrait(portraitDiv, userJSON.portraitId, userJSON.id, 'small', true, LAMS_URL );
			$('<span/>').text(userJSON.firstName + ' ' + userJSON.lastName + ' (' + userJSON.login + ')')
				.addClass('portrait-sm-lineheight')
				.appendTo(userDiv);

			// for later use
			userDivs.push(userDiv);
			userIds.push(userJSON.id);
			
			// if teacher can not edit, then no drag&drop is available
			if (canEdit) {
	    		userDiv.draggable({ 
							'appendTo'    : 'body',
							'containment' : '#groupsTable',
						    'revert'      : 'invalid',
						    'distance'    : 20,
						    'scroll'      : false,
						    'cursor'      : 'move',
							'helper'      : function(event){
								// include the user from which dragging started
								$(this).addClass('draggableSelected');
								
								// copy selected users
								var helperContainer = $('<div />');
								$(this).siblings('.draggableSelected').andSelf().each(function(){
									$(this).clone().appendTo(helperContainer);
								});
								return helperContainer;
							}
					})
				
					.click(function(event){
						var wasSelected = $(this).hasClass('draggableSelected');
						var parentId = $(this).parent().parent().attr('id');
						// this is needed for shift+click
						var lastSelectedUser = lastSelectedUsers[parentId];
						
						if (event.shiftKey && lastSelectedUser && lastSelectedUser != this) {
							// clear current selection
							$(this).siblings().andSelf().removeClass('draggableSelected');
							
							// find range of users to select
							var lastSelectedIndex = $(lastSelectedUser).index();
							var index = $(this).index();
							
							var startingElem = lastSelectedIndex > index ? this : lastSelectedUser;
							var endingElem = lastSelectedIndex > index ? lastSelectedUser : this;
							
							$(startingElem).nextUntil(endingElem).andSelf().add(endingElem)
								.addClass('draggableSelected');
						} else {
							if (!event.ctrlKey) {
								// clear current sleection
								$(this).siblings().andSelf().removeClass('draggableSelected');
							}
							
							if (wasSelected && !event.shiftKey){
								$(this).removeClass('draggableSelected');
								lastSelectedUsers[parentId] = null;
							} else {
								$(this).addClass('draggableSelected');
								lastSelectedUsers[parentId] = this;
							}
						}
					});
			}
			
			if (!createOnServer) {
				$('.userContainer', container).append(userDiv);
			}
		});
		
		if (createOnServer) {
			// copy course groups as lesson groups, creating new instances
			if (assignUsersToGroup(userIds, container)) {
				$('.userContainer', container).append(userDivs);
			} else {
				// if it fails, put them in unassigned list
				$('#unassignedUserCell .userContainer').append(userDivs);
			}
		}
		
		sortUsers(container);
	} else if (createOnServer) {
		// just createa an empty group
		assignUsersToGroup(null, container);
	}
	
	$('.sortUsersButton', container).click(function(){
		sortUsers(container);
	});
	
	if (canEdit) {
		$(container).droppable({
		   'activeClass' : 'droppableHighlight',
		   'tolerance'   : 'pointer',
		   'drop'        : function (event, ui) {
			   var draggableUserContainer = $(ui.draggable).parent();
			   var thisUserContainer = $('.userContainer', this);
			   // do not do anything if it is the same container
			   // using "accept" feature breaks the layout
			   if (draggableUserContainer[0] !=  thisUserContainer[0]) {
				   var executeDrop = !lessonMode;
				   if (!executeDrop) {
					   var transferToLocked = $(this).hasClass('locked');
					   // make sure user wants to transfer learners to a group which is already in use
					   executeDrop = !transferToLocked || confirm(LABELS.TRANSFER_LOCKED_LABEL);
					   if (executeDrop) {
						   var userIds = [];
						   $('div.draggableSelected', draggableUserContainer).each(function(){
							   userIds.push($(this).attr('userId'));
						   });
						   // execute transfer on server side
						   executeDrop = assignUsersToGroup(userIds, $(this));
					   }
			       }
				   
				   if (executeDrop) {
					   transferUsers(draggableUserContainer, thisUserContainer);
				   }
			   }  
		   }
		});
	
		
		$('.removeGroupButton', container).click(function(){
			removeGroup(container);
		});
		
		if (lessonMode) {
			$('input', container).blur(function(){
				renameGroup(container);
			});
		}
	}
}

/**
 * Move user DIVs from one group to another
 */
function transferUsers(fromContainer, toContainer) {
	var selectedUsers =  $('.draggableSelected', fromContainer);
	var locked = toContainer.parent().hasClass('locked');
	if (selectedUsers.length > 0){   
	   // move the selected users
	   selectedUsers.each(function(){	  
		  $(this).css({'top'  : '0px',
              		   'left' : '0px',
          }).removeClass('draggableSelected')
            .appendTo(toContainer);
		  
		  if (locked) {
            $(this).draggable('disable');
		  }
	   });
	   
	   // recolour both containers
	   colorDraggableUsers(toContainer);
	   colorDraggableUsers(fromContainer);
	}
}


function colorDraggableUsers(container) {
	// every second line is different
	$(container).find('div.draggableItem').each(function(index, userDiv){
		$(this).removeClass( index % 2 ? 'draggableEven' : 'draggableOdd');
		$(this).addClass( index % 2 ? 'draggableOdd' : 'draggableEven');
	});
}

/**
 * Sort users according to their names.
 */
function sortUsers(container) {
	var containerId = container.attr('id');
	var users = $('div.draggableItem', container);
	if (users.length > 1) {
		var sortOrderAsc = sortOrderAscending[containerId];
		
		users.each(function(){
			$(this).detach();
		}).sort(function(a, b){
			var keyA = $(a).text().toLowerCase();
			var keyB = $(b).text().toLowerCase();
			var result = keyA > keyB ? 1 : keyA < keyB ? -1 : 0;
			return sortOrderAsc ? -result : result;
		}).each(function(){
			$(this).appendTo($('.userContainer', container));
		});
		
		var button = container.find('.sortUsersButton');
		if (sortOrderAsc) {
			button.html('▼');
			sortOrderAscending[containerId] = false;
		} else {
			button.html('▲');
			sortOrderAscending[containerId] = true;
		}
		
		colorDraggableUsers(container);
	}
}

/**
 * Remove a group both from the server and the page.
 */
function removeGroup(container) {
	// no groupId means this group was just added and it was not persisted in DB yet
	var groupId = container.attr('groupId');
	var executeDelete = true;
	if (groupId) {
		executeDelete = confirm(LABELS.GROUP_REMOVE_LABEL);
	}
	if (executeDelete) {
		executeDelete = !lessonMode;
		
		if (lessonMode) {
			$.ajax({
				async    : false,
				cache    : false,
				dataType : 'json',
				url : LAMS_URL + 'monitoring/grouping.do',
				data : {
					'method'     : 'removeGroup',
					'activityID' : groupingActivityId,
					'groupID'    : groupId
				},
				type : 'POST',
				success : function(response) {
					executeDelete = response.result;
				}
			});
		}
		
		if (executeDelete) {
			$('#unassignedUserCell .userContainer').append($('.userContainer div.draggableItem', container));
			container.remove();
		}
	}
}

/**
 * Change name of a group both on the server and on the page.
 */
function renameGroup(container) {
	var groupId = $(container).attr('groupId');
	var nameInput = $('input', container);
	var inputEditable = !nameInput.attr('readonly');
	// only lesson groups which exist on the server need to have their names changed immediatelly
	if (lessonMode && groupId && inputEditable) {
		var groupName = nameInput.val();
		$.ajax({
			cache    : false,
			url : LAMS_URL + 'monitoring/grouping.do',
			data : {
				'method'     : 'changeGroupName',
				'groupID'    : groupId,
				'name'       : groupName
			},
			type : 'POST'
		});
	}
}

/**
 * Save a course grouping.
 */
function saveGroups(){
	if (!canEdit || lessonMode) {
		return false;
	}
	$('.errorMessage').hide();
	
	var groupingName = $('#groupingName').val();
	// course grouping name can not be blank
	if (!groupingName) {
		$('#grouping-name-blank-error').show();
		$('#groupingName').focus();
		return false;
	}
	
	// course grouping name should be unique
	var isGroupingNameUnique = false;
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/grouping.do',
		cache : false,
		async : false,
		data : {
			'method'    : 'checkGroupingNameUnique',
			'organisationID' : grouping.organisationId,
			'name'  : groupingName
		},		
		success : function(response) {
			isGroupingNameUnique = response.isGroupingNameUnique;
		}
	});
	if ((grouping.name != groupingName) && !isGroupingNameUnique) {
		$('#grouping-name-non-unique-error').show();
		$('#groupingName').focus();
		return false;		
	}
	
	// ask if removing new, empty groups is OK
	var acceptEmptyGroups = true;
	var groupContainers = $('#groupsTable .groupContainer').not('#newGroupPlaceholder');
	$.each(groupContainers.not('[groupId]'), function(){
		if ($('div.draggableItem', this).length == 0) {
			 acceptEmptyGroups = false;
			 return false;
		}
	});
	if (!acceptEmptyGroups) {
		acceptEmptyGroups = confirm(LABELS.EMPTY_GROUP_SAVE_LABEL);
	}
	if (!acceptEmptyGroups) {
		return false;
	}
	
	var newGrouping = {
			'groupingId' : grouping.groupingId,
			'name' : groupingName,
			'groups' : []
	};
	groupContainers.each(function(){
		var groupId = $(this).attr('groupId'),
			users = $('div.draggableItem', this);
		if (!groupId && users.length == 0) {
			return true;
		}
		
		var userIds = [];
		$.each(users, function(){
			userIds.push($(this).attr('userId'));
		});
		
		// add the group JSON to grouping JSON
		newGrouping.groups.push({
			'groupId' : groupId,
			'name'    : $('input', this).val(),
			'users'   : userIds
		});
	});
	
	$.ajax({
		async : false,
		cache : false,
		url : LAMS_URL + 'OrganisationGroup.do',
		data : {
			'method' : 'save',
			'organisationID' : grouping.organisationId,
			'grouping' : JSON.stringify(newGrouping)
		},
		type : 'POST',
		success : function() {
			window.parent.showOrgGroupingDialog(organisationId);
		}
	});
}

/**
 * Transfer learners between groups on the server.
 */
function assignUsersToGroup(userIds, groupContainer) {
	// if removing from any groups, set group ID as -1; server understands this
	var groupId = groupContainer.attr('id') == 'unassignedUserCell' ? -1 : groupContainer.attr('groupId');
	// name is only needed when creating a new group, i.e. a group which does not have ID yet
	var groupName = groupId ? null : $('input', groupContainer).val();
	var result = false;
	
	$.ajax({
		async    : false,
		cache    : false,
		dataType : 'json',
		url : LAMS_URL + 'monitoring/grouping.do',
		data : {
			'method'     : 'addMembers',
			'activityID' : groupingActivityId,
			'groupID'    : groupId,
			'name'       : groupName,
			'members'    : userIds ? userIds.join() : null
		},
		type : 'POST',
		success : function(response) {
			result = response.result;
			if (response.groupId) {
				groupContainer.attr('groupId', response.groupId);
			}
			if (!result && response.locked) {
				// the server says that the group became in use while teacher was working with this screen
				markGroupLocked($('div[userId="' + userIds[0] + '"]').parents('.groupContainer'));
				alert(LABELS.GROUP_LOCK_LABEL);
			}
		}
	});
	
	return result;
}

/**
 * Makes a group almost immutable as server does not allow changed.
 */
function markGroupLocked(container) {
	container.addClass('locked');
	$('.removeGroupButton', container).remove();
	// $('input', container).attr('readonly', 'readonly');
	$('div.draggableItem', container).off('click').draggable('disable');
}

/**
 * *************** Save as a course grouping dialog ***************
 */

//open save as a course grouping dialog
function saveAsCourseGrouping() {
	$('#saveAsCourseGroupingDialog').modal('show');
}

$(document).ready(function(){

	// sets up dialog for editing class
	var saveAsCourseGroupingDialog = showDialog('saveAsCourseGroupingDialog',{
		'autoOpen'  : false,
		'width'     : 510,
		'title' 	: LABELS.SAVE_AS_COURSE_GROUPING_LABEL,
		'resizable' : true,
		'open'      : function(){
			//focus name text field
			setTimeout(
				function() { 
					$('#dialog-course-grouping-name').focus(); 
				}, 
				700
			);
		},
		'close' : function(){
			//reset dialog to its initial state
			resetSaveGroupingDialog();
			$('#dialog-course-grouping-name', this).val("");
		}
	}, false);
	
	$('.modal-body', saveAsCourseGroupingDialog).empty().append($('#save-course-grouping-dialog-contents').show());

	//save button handler
	$('#dialog-save-button', saveAsCourseGroupingDialog).click(function() {
		var dialog = $('#saveAsCourseGroupingDialog'),
			name = $('#dialog-course-grouping-name', dialog).val();
		
		//name can't be blank
		if (!name || /^\s*$/.test(name)) {
			$('#dialog-course-grouping-name', dialog).addClass("alert-danger");
			$("#span-tooltip", dialog).addClass("alert-danger").text(LABELS.NAME_BLANK_LABEL);
			return;
		}
		
		//name should be unique
		$.ajax({
			dataType : 'json',
			url : LAMS_URL + 'monitoring/grouping.do',
			cache : false,
			async : false,
			data : {
				'method'    : 'checkGroupingNameUnique',
				'organisationID' : organisationId,
				'name'  : name
			},		
			success : function(response) {
				if (response.isGroupingNameUnique) {
					$.ajax({
						dataType : 'json',
						url : LAMS_URL + 'monitoring/grouping.do',
						cache : false,
						async : false,
						data : {
							'method'    : 'saveAsCourseGrouping',
							'organisationID' : organisationId,
							'activityID' : groupingActivityId,
							'name'  : name
						},
						success : function(response) {
							$('#saveAsCourseGroupingDialog').modal('hide');
							alert(LABELS.SAVED_SUCCESSFULLY_LABEL);
						}
					});
					
				} else {
					$('#dialog-course-grouping-name', dialog).addClass("alert-danger");
					$("#span-tooltip", dialog).addClass("alert-danger").text(LABELS.NAME_NOT_UNIQUE_LABEL);
				}
			}
		});
	});
	
	//close button handler
	$('#dialog-close-button', saveAsCourseGroupingDialog).click(function(){
		$('#saveAsCourseGroupingDialog').modal('hide');
	});
	
	// ability to save a grouping on pressing the Enter key in the name input field
	$('#dialog-course-grouping-name', saveAsCourseGroupingDialog).on('keyup', function (e) {
		//remove alert class if it was applied
		if ($("#span-tooltip", saveAsCourseGroupingDialog).hasClass("alert-danger")) {
			resetSaveGroupingDialog();
		}
		
	    if (e.keyCode == 13) {
			$('#dialog-save-button', saveAsCourseGroupingDialog).trigger( "click" );
	    }
	});

	//reset dialog to its initial state
	function resetSaveGroupingDialog() {
		$("#span-tooltip", saveAsCourseGroupingDialog).removeClass("alert-danger").text(LABELS.ENTER_COURSE_GROUPING_NAME_LABEL);
		$('#dialog-course-grouping-name', saveAsCourseGroupingDialog).removeClass("alert alert-danger");
	}
	
	//scroll to the botom of the page on opening Advanced settings
	$('#accordionAdvanced').on('shown.bs.collapse', function () {
		$("html, body").animate({ scrollTop: 170 }, 1000);
	});

});