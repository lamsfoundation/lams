// ********** MAIN FUNCTIONS **********
var sortOrderAscending = {};
var lastSelectedUsers = {};

$(document).ready(function(){
	$('#groupingName').val(grouping.name);
	// show unassigned users on the left
	fillGroup(unassignedUsers, $('#unassignedUserCell'));
	$.each(grouping.groups, function(){
		// add existing groups
		addGroup(this.groupId, this.name, this.users);
	});
	// move Save button to the titlebar, i.e. outside of this iframe to the enveloping dialog
	$('div.ui-dialog-titlebar', window.parent.document).prepend($('.customDialogButton'));
	
	if (canEdit) {
		// allow adding new groups
		$('#newGroupPlaceholder').click(function(){
			addGroup(null, LABELS.GROUP_PREFIX_LABEL + $('table .groupContainer').length, null);
		});
	}
});


/**
 * Add a group and fills it with given users.
 */
function addGroup(groupId, name, users) {
	var groupCount = $('table .groupContainer').length;
	var group = $('#groupTemplate').clone()
					.attr({
						'id' : 'group-' + groupCount,
						'groupId' : groupId
					})
					.css('display', null);
	group.find('input').val(name);
	
	// there is no placeholder in read-only mode
	if (canEdit) {
		group.insertBefore('#newGroupPlaceholder');
	} else {
		$('#groupsCell').append(group);
	}
	
	fillGroup(users, group);
}


/**
 * Makes a list of users and adds drag&drop functionality to them.
 */
function fillGroup(users, container) {
	if (users) {
		// create user DIVs
		$.each(users, function(index, userJSON) {
			var userDiv = $('<div />').attr('userId', userJSON.id)
				.addClass('draggableUser')
	    		.text(userJSON.firstName + ' ' + userJSON.lastName 
	    				  + ' (' + userJSON.login + ')'
	    			   );
			
			// if teacher can not edit, then no drag&drop is available
			if (canEdit) {
	    		userDiv.draggable({ 
							'appendTo'    : 'body',
							'containment' : 'table',
						    'revert'      : 'invalid',
						    'distance'    : 20,
						    'scroll'      : false,
						    'cursor'      : 'move',
							'helper'      : function(event){
								// include the user from which dragging started
								$(this).addClass('draggableUserSelected');
								
								// copy selected users
								var helperContainer = $('<div />');
								$(this).siblings('.draggableUserSelected').andSelf().each(function(){
									$(this).clone().appendTo(helperContainer);
								});
								return helperContainer;
							}
					})
				
					.click(function(event){
						var wasSelected = $(this).hasClass('draggableUserSelected');
						var parentId = $(this).parent().parent().attr('id');
						// this is needed for shift+click
						var lastSelectedUser = lastSelectedUsers[parentId];
						
						if (event.shiftKey && lastSelectedUser && lastSelectedUser != this) {
							// clear current selection
							$(this).siblings().andSelf().removeClass('draggableUserSelected');
							
							// find range of users to select
							var lastSelectedIndex = $(lastSelectedUser).index();
							var index = $(this).index();
							
							var startingElem = lastSelectedIndex > index ? this : lastSelectedUser;
							var endingElem = lastSelectedIndex > index ? lastSelectedUser : this;
							
							$(startingElem).nextUntil(endingElem).andSelf().add(endingElem)
								.addClass('draggableUserSelected');
						} else {
							if (!event.ctrlKey) {
								// clear current sleection
								$(this).siblings().andSelf().removeClass('draggableUserSelected');
							}
							
							if (wasSelected && !event.shiftKey){
								$(this).removeClass('draggableUserSelected');
								lastSelectedUsers[parentId] = null;
							} else {
								$(this).addClass('draggableUserSelected');
								lastSelectedUsers[parentId] = this;
							}
						}
					});
			}
			
			$('.userContainer', container).append(userDiv);
		});
		
		sortUsers(container);
	}
	
	$('.sortUsersButton', container).click(function(){
		sortUsers(container);
	});
	
	if (canEdit) {
		$(container).droppable({
		   'activeClass' : 'droppableHighlight',
		   'tolerance'   : 'touch',
		   'drop'        : function (event, draggable) {
			   var draggableUserContainer = $(draggable.draggable).parent();
			   var thisUserContainer = $('.userContainer', this);
			   // do not do anything if it is the same container
			   // using "accept" feature breaks the layout
			   if (draggableUserContainer[0] !=  thisUserContainer[0]) {
				   transferUsers(draggableUserContainer, thisUserContainer);
			   }
		   }
		});
	
		
		$('.removeGroupButton', container).click(function(){
			removeGroup(container);
		});
	}
}


function transferUsers(fromContainer, toContainer) {
	var selectedUsers =  $('.draggableUserSelected', fromContainer);
	if (selectedUsers.length > 0){
	   // move the selected users
	   selectedUsers.each(function(){
		  $(this).css({'top'  : '0px',
              		   'left' : '0px',
          })
          .appendTo(toContainer);
	   });
	            
	   
	   // recolour both containers
	   toContainer.children().removeClass('draggableUserSelected');
	   colorDraggableUsers(toContainer);
	   colorDraggableUsers(fromContainer);
	}
}


function colorDraggableUsers(container) {
	// every second line is different
	$(container).find('div.draggableUser').each(function(index, userDiv){
		// exact colour should be defined in CSS, but it's easier this way...
		$(this).css('background-color', index % 2 ? '#dfeffc' : 'inherit');
	});
}

/**
 * Sort users according to their names.
 */
function sortUsers(container) {
	var containerId = container.attr('id');
	var users = $('div.draggableUser', container);
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

function removeGroup(container) {
	// no groupId means this group was just added and it was not persisted in DB yet
	var executeDelete = !$(container).attr('groupId');
	if (!executeDelete) {
		executeDelete = confirm(LABELS.GROUP_REMOVE_LABEL);
	}
	if (executeDelete) {
		$('#unassignedUserCell .userContainer').append($('.userContainer div.draggableUser', container));
		container.remove();
	}
}


function saveGroups(){
	if (!canEdit) {
		return false;
	}
	$('.errorMessage').hide();
	
	var groupingName = $('#groupingName').val();
	if (!groupingName) {
		$('#groupingNameBlankError').show();
		return false;
	}
	
	// ask if removing new, empty groups is OK
	var acceptEmptyGroups = true;
	var groupContainers = $('table .groupContainer').not('#newGroupPlaceholder');
	$.each(groupContainers.not('[groupId]'), function(){
		if ($('div.draggableUser', this).length == 0) {
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
	
	var groupsSaved = false;
	var newGrouping = {
			'groupingId' : grouping.groupingId,
			'name' : groupingName,
			'groups' : []
	};
	groupContainers.each(function(){
		var groupId = $(this).attr('groupId');
		var users = $('div.draggableUser', this);
		if (!groupId && users.length == 0) {
			return true;
		}
		
		var userIds = [];
		$.each(users, function(){
			userIds.push($(this).attr('userId'));
		});
		
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
		success : function(json) {
			groupsSaved = true;
		}
	});
	
	return groupsSaved;
}