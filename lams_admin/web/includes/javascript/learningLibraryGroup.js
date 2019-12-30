var lastSelected = {};

$(document).ready(function(){
	// show all learning libraries on the left
	fillGroup(learningLibraries, $('#learningLibraryCell'));
	// add existing groups
	$.each(groups, function(){
		var group = addGroup(this);
	});
	
	$('#newGroupPlaceholder').click(function(){
		addGroup();
	});
});


/**
 * Add a group and fills it with given learning libraries;
 */
function addGroup(groupData) {
	var group = $('#groupTemplate').clone()
					.attr({
						// remove template's HTML ID
						'id'      : null,
						'groupId' : groupData ? groupData.groupId : null
					})
					.css('display', null)
					.insertBefore('#newGroupPlaceholder');
	
	group.find('input').val(groupData ? groupData.name : LABELS.GROUP_DEFAULT_NAME);
	fillGroup(groupData ? groupData.learningLibraries : null, group);
	
	return group;
}


/**
 * Makes a list of learning libraries and adds drag&drop functionality to them.
 */
function fillGroup(learningLibraries, container) {
	if (learningLibraries) {
		// create learning library DIVs
		$.each(learningLibraries, function(index, learningLibraryJSON) {
			var learningLibraryDiv = createLearningLibraryDiv(learningLibraryJSON.learningLibraryId, learningLibraryJSON.title);
			$('.learningLibraryContainer', container).append(learningLibraryDiv);
		});
		
		colorDraggableLearningLibraries(container);
	}
	
	$(container).droppable({
	   'activeClass' : 'droppableHighlight',
	   'tolerance'   : 'pointer',
	   'drop'        : function (event, ui) {
		   var draggableLearningLibraryContainer = $(ui.draggable).parent(),
		   	   thisLearningLibraryContainer = $('.learningLibraryContainer', this);
		   // do not do anything if it is the same container
		   // using "accept" feature breaks the layout
		   if (draggableLearningLibraryContainer[0] !=  thisLearningLibraryContainer[0]) {
			   transferLearningLibraries(draggableLearningLibraryContainer, thisLearningLibraryContainer);
		   }
	   }
	});
	
	$('.removeGroupButton', container).click(function(){
		removeGroup(container);
	});
}


/**
 * Constructs a single learning library label with events. 
 */
function createLearningLibraryDiv(learningLibraryId, title) {
	return $('<div />').attr('learningLibraryId', learningLibraryId)
	.addClass('draggableLearningLibrary')
	.text(title)
	.draggable({
			'appendTo'    : 'body',
			'containment' : '#groupsTable',
		    'revert'      : 'invalid',
		    'distance'    : 20,
		    'cursor'      : 'move',
			'helper'      : function(event){
				// include the learning library from which dragging started
				$(this).addClass('draggableLearningLibrarySelected');
				
				// copy selected learning libraries
				var helperContainer = $('<div />');
				$(this).siblings('.draggableLearningLibrarySelected').andSelf().each(function(){
					$(this).clone().appendTo(helperContainer);
				});
				return helperContainer;
			}
	})

	.click(function(event){
		var wasSelected = $(this).hasClass('draggableLearningLibrarySelected'),
			parentId = $(this).parent().parent().attr('id'),
			// this is needed for shift+click
			lastSelectedLearningLibrary = lastSelected[parentId];
		
		if (event.shiftKey && lastSelectedLearningLibrary && lastSelectedLearningLibrary != this) {
			// clear current selection
			$(this).siblings().andSelf().removeClass('draggableLearningLibrarySelected');
			
			// find range of learning libraries to select
			var lastSelectedIndex = $(lastSelectedLearningLibrary).index(),
				index = $(this).index().
				startingElem = lastSelectedIndex > index ? this : lastSelectedLearningLibrary,
				endingElem = lastSelectedIndex > index ? lastSelectedLearningLibrary : this;
			
			$(startingElem).nextUntil(endingElem).andSelf().add(endingElem)
				.addClass('draggableLearningLibrarySelected');
		} else {
			if (!event.ctrlKey) {
				// clear current sleection
				$(this).siblings().andSelf().removeClass('draggableLearningLibrarySelected');
			}
			
			if (wasSelected && !event.shiftKey){
				$(this).removeClass('draggableLearningLibrarySelected');
				lastSelected[parentId] = null;
			} else {
				$(this).addClass('draggableLearningLibrarySelected');
				lastSelected[parentId] = this;
			}
		}
	});
}


/**
 * Move learning library DIVs from one group to another
 */
function transferLearningLibraries(fromContainer, toContainer) {
	var selectedLearningLibraries =  $('.draggableLearningLibrarySelected', fromContainer);
	if (selectedLearningLibraries.length > 0){
		if (toContainer.parent().attr('id') == 'learningLibraryCell'){
			// just remove the selected items as they already exist in the full learning library list
			selectedLearningLibraries.remove();
		} else {
			// find out which learning libraries are already there and do not move them
			var filteredLearningLibraries = [],
				existingLearningLibraries = $('.draggableLearningLibrary', toContainer),
				isAdd = fromContainer.parent().attr('id') == 'learningLibraryCell';
			$.each(selectedLearningLibraries, function(){
				var selectedLearningLibrary = $(this);
				$.each(existingLearningLibraries, function(){
					if ($(this).attr('learningLibraryId') == selectedLearningLibrary.attr('learningLibraryId')){
						selectedLearningLibrary = null;
						return false;
					}
				});
				if (selectedLearningLibrary) {
					if (isAdd) {
						// the selected items were pulled out from the full library list,
						// so do not remove them from the source container
						selectedLearningLibrary = createLearningLibraryDiv(selectedLearningLibrary.attr('learningLibraryId'),
								selectedLearningLibrary.text());
					}
					filteredLearningLibraries.push(selectedLearningLibrary);
				}
			});
			
		   // move the selected learning libraries
			$.each(filteredLearningLibraries, function(){	  
				  $(this).css({'top'  : '0px',
		              		   'left' : '0px',
		          }).removeClass('draggableLearningLibrarySelected')
		            .appendTo(toContainer);
		   });
		}
	   // recolour both containers
	   colorDraggableLearningLibraries(toContainer);
	   colorDraggableLearningLibraries(fromContainer);
	}
}


function colorDraggableLearningLibraries(container) {
	// every second line is different
	$(container).find('div.draggableLearningLibrary').each(function(index, learningLibraryDiv){
		$(this).removeClass( index % 2 ? 'draggableEven' : 'draggableOdd');
		$(this).addClass( index % 2 ? 'draggableOdd' : 'draggableEven');
	});
}


/**
 * Remove a group from the page.
 */
function removeGroup(container) {
	if (confirm(LABELS.GROUP_REMOVE_CONFIRM)){
		container.remove();
	}
}


/**
 * Save groups and let parent window that it can close the dialog
 */
function saveGroups(){
	var groups = [];
	
	$('#groupsTable .groupContainer').not('#newGroupPlaceholder').each(function(){
		var learningLibraries = $('div.draggableLearningLibrary', this),
			learningLibraryIds = [],
			name =  $('input', this).val();
		if (!name || name.trim() == '') {
			alert(LABELS.GROUP_NAME_VALIDATION_ERROR);
			groups = null;
			return false;
		}
		$.each(learningLibraries, function(){
			learningLibraryIds.push($(this).attr('learningLibraryId'));
		});
		
		// add the group JSON
		groups.push({
			'groupId' 			: $(this).attr('groupId'),
			'name'    			: name,
			'learningLibraries' : learningLibraryIds
		});
	});
	
	if (groups){	
		$.ajax({
			'async'   : false,
			'cache'   : false,
			'url'     : 'saveLearningLibraryGroups.do',
			'data'    : {
				'groups' : JSON.stringify(groups)
			},
			'type' 	  : 'POST',
			'success' : function(){
				$('#dialogToolGroup', window.parent.document).remove();
			},
			'error'	  : function(){
				alert(LABELS.SAVE_ERROR);
			}
		});
	}
}