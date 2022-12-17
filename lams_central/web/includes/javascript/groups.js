﻿// for user selecting and sorting purposes
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
			$("#grouping-name-blank-error,#grouping-name-non-unique-error").addClass('d-none');
			
		    if (e.keyCode == 13) {
				saveGroups();
		    }
		});
	}
	
	// allow adding new groups
	$('#newGroupPlaceholder').click(function(){
		// the label is "Group X" where X is the top group number
		addGroup(null, LABELS.GROUP_PREFIX_LABEL + ' ' + $('#groupsCell .groupContainer').length, null);
	});
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
					.removeClass('d-none');
	
	var groupNameFields = $('#groupsCell .groupContainer input');
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
	
	// if any users are allocated, warn before doing spreadsheet reload
	if ( users && users.length > 0 )
		warnBeforeUpload = true;

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
			var userDiv = $('<div />')
				.attr('userId', userJSON.id)
				.addClass('draggableItem');
			
			new bootstrap.Tooltip(userDiv, {
				'title'     : userJSON.login,
				'placement' : 'left'
			});
			
			var portraitDiv = $('<div />').attr({
				'id': 'portrait-'+userJSON.id,
				})
				.addClass('mb-2')
				.appendTo(userDiv);
			addPortrait(portraitDiv, userJSON.portraitId, userJSON.id, 'small', true, LAMS_URL );
			$('<span/>').text(userJSON.firstName + ' ' + userJSON.lastName)
				.addClass('portrait-sm-lineheight ms-1')
				.appendTo(userDiv);
				

			// for later use
			userDivs.push(userDiv);
			userIds.push(userJSON.id);
			
    		userDiv.draggable({ 
						'appendTo'    : 'body',
						'containment' : '#groupsTable',
					    'revert'      : 'invalid',
					    'distance'    : 20,
					    'scroll'      : true,
					    'scrollSensitivity' : 120,
						'scrollSpeed' : 100,
					    'cursor'      : 'move',
						'helper'      : function(event){
							// include the user from which dragging started
							$(this).addClass('draggableSelected');
							
							// copy selected users
							var helperContainer = $('<div />');
							$(this).siblings('.draggableSelected').addBack().each(function(){
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
						$(this).siblings().addBack().removeClass('draggableSelected');
						
						// find range of users to select
						var lastSelectedIndex = $(lastSelectedUser).index();
						var index = $(this).index();
						
						var startingElem = lastSelectedIndex > index ? this : lastSelectedUser;
						var endingElem = lastSelectedIndex > index ? lastSelectedUser : this;
						
						$(startingElem).nextUntil(endingElem).addBack().add(endingElem)
							.addClass('draggableSelected');
					} else {
						if (!event.ctrlKey) {
							// clear current sleection
							$(this).siblings().addBack().removeClass('draggableSelected');
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
	}
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
	}
}

/**
 * Remove a group both from the server and the page.
 */
function removeGroup(container) {
	// no groupId means this group was just added and it was not persisted in DB yet
	var groupId = container.attr('groupId');
	var executeDelete = true,
		deleteFunction = function(){
			executeDelete = !lessonMode;
			
			if (lessonMode) {
				var data = {
					'activityID' : groupingActivityId,
					'groupID'    : groupId
				};
				data[csrfTokenName] = csrfTokenValue;
				
				$.ajax({
					async    : false,
					cache    : false,
					dataType : 'json',
					url : LAMS_URL + 'monitoring/grouping/removeGroup.do',
					data : data,
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
		};
		
	if (groupId) {
		executeDelete = showConfirm(LABELS.GROUP_REMOVE_LABEL, deleteFunction);
	} else {
		deleteFunction();
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
		var data = {
			'groupID'    : groupId,
			'name'       : nameInput.val()//groupName
		};
		data[csrfTokenName] = csrfTokenValue;
		
		$.ajax({
			cache    : false,
			url : LAMS_URL + 'monitoring/grouping/changeGroupName.do',
			data : data,
			type : 'POST'
		});
	}
}

/**
 * Save a course grouping.
 */
function saveGroups(){
	if (lessonMode) {
		return false;
	}
	$('.errorMessage').hide();
	
	var groupingName = $('#groupingName').val();
	// course grouping name can not be blank
	if (!groupingName) {
		$('#grouping-name-blank-error').removeClass('d-none');
		$('#groupingName').focus();
		return false;
	}
	
	// course grouping name should be unique
	var isGroupingNameUnique = false;
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/grouping/checkGroupingNameUnique.do',
		cache : false,
		async : false,
		data : {
			'organisationID' : grouping.organisationId,
			'name'  : groupingName
		},		
		success : function(response) {
			isGroupingNameUnique = response.isGroupingNameUnique;
		}
	});
	if ((grouping.name != groupingName) && !isGroupingNameUnique) {
		$('#grouping-name-non-unique-error').removeClass('d-none');
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
		url : LAMS_URL + 'grouping/save.do',
		data : {
			'organisationID' : grouping.organisationId,
			'grouping' : JSON.stringify(newGrouping)
		},
		type : 'POST',
		success : function() {
			window.opener.showOrgGroupingDialog(organisationId);
			window.close();
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
	var data = {
		'activityID' : groupingActivityId,
		'groupID'    : groupId,
		'name'       : groupName,
		'members'    : userIds ? userIds.join() : null
	};
	data[csrfTokenName] = csrfTokenValue;
	
	$.ajax({
		async    : false,
		cache    : false,
		dataType : 'json',
		url : LAMS_URL + 'monitoring/grouping/addMembers.do',
		data : data,
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
	// if any group is locked cannot do the spreadsheet upload
	$('#accordionUploadGroupFile').css("display","none");
}

/**
 * Show a printable version of the groups. Open the same size as the current window
 */
function showPrintPage() {
	var url = LAMS_URL + 'monitoring/grouping/printGrouping.do',
		queryChar = '?';
	if ( groupingActivityId ) {
		url += queryChar + 'activityID='+groupingActivityId;
		queryChar = '&';
	}
	if ( lessonId ) {
		url += queryChar + 'lessonID='+lessonId;
		queryChar = '&';
	}
	if ( grouping && grouping.groupingId ) {
		url += queryChar + 'groupingId='+grouping.groupingId;
		queryChar = '&';
	}
	var height = Math.min(550, $(window).height()),
		width = Math.max(1093, $(window).width());
	window.open(url,LABELS.GROUP_PREFIX_LABEL,'height='+height+',width='+width+',resizable,scrollbars').focus();
}
/** 
 * *************** Import groups from a spreadsheet ***************
 */
	function enableButtons() {
		// do not disable the file button or the file will be missing on the upload.
		$('.btn-disable-on-downupload').prop('disabled', false);
		$('a.btn-disable-on-downupload').show(); // links must be hidden, cannot be disabled
	}
	function disableButtons() {
		// do not disable the file button or the file will be missing on the upload.
		$('.btn-disable-on-downupload').prop('disabled', true);
		$('a.btn-disable-on-downupload').hide(); // links must be hidden, cannot be disabled
	}
	
	function importGroupsFromSpreadsheet() {
		disableButtons();
		var file = getValidateSpreadsheetFile();
		if ( file != null && ( !warnBeforeUpload || confirm(LABELS.WARNING_REPLACE_GROUPS_LABEL) ) ) {
			var form = $("#uploadForm")[0];
			var formDataUpload = new FormData(form);
			formDataUpload.append("organisationID", organisationId); 
			formDataUpload.append("lessonMode", lessonMode);
			if ( lessonMode ) {
				callImportURL(form, formDataUpload);

			} else if ( grouping && grouping.groupingId  ) {
				// course grouping - grouping already exists so just upload users
				formDataUpload.append("groupingId", grouping.groupingId);
				callImportURL(form, formDataUpload);
			} else {
				// New course grouping. Need to check name okay first.

				$('.errorMessage').hide();
				var groupingName = $('#groupingName').val();
				// course grouping name can not be blank
				if (!groupingName) {
					$('#grouping-name-blank-error').removeClass('d-none');
					$('#groupingName').focus();
					enableButtons();
					return false;
				}
				
				// course grouping name should be unique
				var isGroupingNameUnique = false;
				$.ajax({
					dataType : 'json',
					url : LAMS_URL + 'monitoring/grouping/checkGroupingNameUnique.do',
					cache : false,
					async : false,
					data : {
						'organisationID' : grouping.organisationId,
						'name'  : groupingName
					},		
					success : function(response) {
						if ( response.isGroupingNameUnique ) {
							formDataUpload.append("name",  groupingName)
							callImportURL(form, formDataUpload);
						} else {
							$('#grouping-name-non-unique-error').removeClass('d-none');
							$('#groupingName').focus();
							enableButtons();
							return false;		
						}
					}
				});
			}

		} else {
			enableButtons();
		}
	}
	
	function reloadIframe(returnedGroupingId) {
		if ( !lessonMode && returnedGroupingId ) {
			var locationUrl = window.location.href;
			if ( locationUrl.indexOf('groupingId') == -1  ) {
				// if course grouping we need to add the new grouping id if this was a new grouping.
				locationUrl += '&groupingId=' + returnedGroupingId;
				window.location.assign(locationUrl);
				return;
			}
		} 
		window.location.reload(); 
	} 
	function callImportURL(form, formDataUpload) {
	    $.ajax({ 
	        data: formDataUpload, 
	        processData: false, // tell jQuery not to process the data
	        contentType: false, // tell jQuery not to set contentType
	        type: 'POST', 
	        url: form.action,
	        enctype: "multipart/form-data",
			dataType : 'json',
	        success: function (response) {
	        		var returnedGroupingId = response.groupingId;
	        		if ( response.result != 'OK') {
	        			if ( response.error ) {
	        				alert(response.error);
	        				if ( response.reload ) {
	        					// do not have another go, look at new data
	        					reloadIframe(returnedGroupingId);
	        				} else {
	        					enableButtons();  // let them have another go
	        				}
        				}
	        			else  {
	        				// unknown failure on back end. 
	        				alert(LABELS.GENERAL_ERROR_LABEL);
	        			}
	        		} else {
	        			var msg = LABELS.LABEL_IMPORT_SUCCESSFUL_LABEL.replace("%1", response.added).replace("%2", response.skipped);
	        			alert(msg);
	        			reloadIframe(returnedGroupingId);
	        		}
	        },
	        error: function() {
	        		// unknown failure on back end. 
	        		alert(LABELS.GENERAL_ERROR_LABEL);
	        }
		});
	}
	
	function getValidateSpreadsheetFile() {
		var file = null;
		// check file
		var fileSelect = document.getElementById('groupUploadFile');
		var files = fileSelect.files;
		if (files.length == 0) {
			clearFileError();
			var requiredMsg = LABELS.ERROR_FILE_REQUIRED_LABEL;
			showFileError(requiredMsg);
		} else if ( validateShowErrorSpreadsheetType(files[0], LABELS.ERROR_FILE_WRONG_FORMAT_LABEL, false) ) {
			file = files[0];
		}
		return file;
	}
	
	var fileDownloadCheckTimer;
	
	function downloadTemplate() {
		disableButtons();
		var token = new Date().getTime(); //use the current timestamp as the token value
	
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
				enableButtons();
			}
		}, 1000);
				
		var url = LAMS_URL + "monitoring/groupingUpload/getGroupTemplateFile.do?activityID="+groupingActivityId
		+"&organisationID="+organisationId+"&lessonID="+lessonId+"&downloadTokenValue=" + token;
		if ( grouping && grouping.groupingId) {
			url += "&groupingId=" + grouping.groupingId;
		}
		document.location.href = url;
		return false;
	}

	$(document).ready(function(){
	
		//scroll to the bottom of the page on opening Advanced settings
		$('#accordionUploadGroupFile').on('shown.bs.collapse', function () {
			$("html, body").animate({ scrollTop: 170 }, 1000);
		});
	});
	
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
			url : LAMS_URL + 'monitoring/grouping/checkGroupingNameUnique.do',
			cache : false,
			async : false,
			data : {
				'organisationID' : organisationId,
				'name'  : name
			},		
			success : function(response) {
				if (response.isGroupingNameUnique) {
					var data = {
						'organisationID' : organisationId,
						'activityID' : groupingActivityId,
						'name'  : name
					};
					data[csrfTokenName] = csrfTokenValue;
					
					$.ajax({
						dataType : 'json',
						url : LAMS_URL + 'monitoring/grouping/saveAsCourseGrouping.do',
						type : 'POST',
						cache : false,
						async : false,
						data : data,
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
