/**
 * This file contains methods for Activity properties dialogs.
 */
var PropertyLib = {
		
	init : function(){
		// initialise the properties dialog singleton
		var propertiesDialog = layout.items.propertiesDialog =
			$('<div />')
				.appendTo('body')
				.dialog({
					'autoOpen'      : false,
					'closeOnEscape' : false,
					'position'      : {
						'my' : 'left top',
						'at' : 'left top',
						'of' :  '#canvas'
					},
					'resizable'     : true,
					'title'         : 'Properties'
				});
		// for proximity detection throttling (see handlers)
		propertiesDialog.lastRun = 0;
		// remove close button, add dimming
		propertiesDialog.container = propertiesDialog.closest('.ui-dialog');
		propertiesDialog.container.addClass('propertiesDialogContainer')
								  .css('opacity', layout.conf.propertiesDialogDimOpacity)
		 						  .mousemove(HandlerLib.approachPropertiesDialogHandler)
		                          .find('.ui-dialog-titlebar-close').remove();
		layout.dialogs.push(propertiesDialog);
		
		// initialise dialog from group naming
		layout.items.groupNamingDialog = $('<div />').dialog({
			'autoOpen' : false,
			'modal'  : true,
			'show'   : 'fold',
			'hide'   : 'fold',
			'position' : {
				'of' :  '#canvas'
			},
			'title'  : 'Group Naming',
			'buttons' : [
			             {
			            	'text'   : 'OK',
			            	'click'  : function() {
			            		var dialog = $(this),
			            			activity = dialog.dialog('option', 'parentObject');
			            		
			            		// extract group names from text fields
			            		$('input', dialog).each(function(index){
		            				activity.groups[index].groupName = $(this).val().trim();
			            		});
			            		
			            		dialog.dialog('close');
							}
			             },
			             {
			            	'text'   : 'Cancel',
			            	'click'  : function() {
								$(this).dialog('close');
							}
			             }
			]
		});
		layout.dialogs.push(layout.items.groupNamingDialog);
		
		
		// initialise dialog from matching groups to branches in branching activities
		var gtbDialog = layout.items.groupsToBranchesMappingDialog = $('#gtbDialog').dialog({
			'autoOpen' : false,
			'modal'  : true,
			'show'   : 'fold',
			'hide'   : 'fold',
			'position' : {
				'of' :  '#canvas'
			},
			'width'  : 800,
			'height' : 400,
			'title'  : 'Match Groups to Branches',
			'buttons' : [
			             {
			            	'text'   : 'OK',
			            	'click'  : function() {
			            		var dialog = $(this),
			            			branchingActivity = dialog.dialog('option', 'branchingActivity');
			            		
			            		// find references to groups and branches
			            		branchingActivity.groupsToBranches = [];
			            		$('#gtbMappingGroupCell div', dialog).each(function(){
			            			var groupUIID = +$(this).attr('uiid'),
			            				branchUIID = +$(this).data('boundItem').attr('uiid'),
			            				group = null,
			            				branch = null;
			            			
			            			$.each(branchingActivity.branches, function(){
			            				if (branchUIID == this.uiid) {
			            					branch = this;
			            					return false;
			            				}
			            			});
			            			$.each(branchingActivity.grouping.groups, function(){
			            				if (groupUIID == this.uiid) {
			            					group = this;
			            					return false;
			            				}
			            			});
			            			
			            			// add the mapping
			            			branchingActivity.groupsToBranches.push({
			            				'uiid'   : ++layout.ld.maxUIID,
			            				'group'  : group,
			            				'branch' : branch
			            			});
			            		});
			            		
			            		dialog.dialog('close');
							}
			             },
			             {
			            	'text'   : 'Cancel',
			            	'click'  : function() {
								$(this).dialog('close');
							}
			             }
			]
		});
		
		$('#gtbAddButton', gtbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-next'
			},
			'text' : false
		});
		$('#gtbRemoveButton', gtbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-prev'
			},
			'text' : false
		});
		
		layout.dialogs.push(gtbDialog);
	},
	
	
	/**
	 * Properties dialog content for transitions.
	 */
	transitionProperties : function() {
		var transition = this,
			content = transition.propertiesContent;
		if (!content) {
			// first run, create the content
			content = transition.propertiesContent = $('#propertiesContentTransition').clone().attr('id', null)
													.show().data('parentObject', transition);
			$('.propertiesContentFieldTitle', content).val(transition.title);
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContainer'),
					transition = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != transition.title) {
					transition.title = newTitle;
					if (transition.branch) {
						transition.branch.title = newTitle;
					}
					redrawNeeded = true;
				}
				
				if (redrawNeeded) {
					transition.draw();
				}
			});
		}
	},
	
	
	/**
	 * Properties dialog content for Tool activities.
	 */
	toolProperties : function() {
		var activity = this,
			content = activity.propertiesContent,
			allowsGrouping = !this.parentActivity || !(this.parentActivity instanceof ActivityLib.ParallelActivity);
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentTool').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldTitle', content).val(activity.title);
			$('.propertiesContentFieldGrouping', content).closest('tr').remove();
			
			$('input, select', content).change(function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					activity.title = newTitle;
					redrawNeeded = true;
				}
				
				var selectedGrouping = $('.propertiesContentFieldGrouping option:selected', content);
				if (selectedGrouping.length > 0){
					var newGroupingValue = $('.propertiesContentFieldGrouping option:selected', content)
										.data('grouping');
					if (newGroupingValue != activity.grouping) {
						activity.grouping = newGroupingValue;
						redrawNeeded = true;
					}
				}
				
				if (redrawNeeded) {
					activity.draw();
				}
			});
		}
		
		if (allowsGrouping){
			PropertyLib.fillGroupingDropdown(content, activity.grouping);
		}
	},
	
	
	/**
	 * Properties dialog content for Grouping activities.
	 */
	groupingProperties : function() {
		var activity = this,
			content = activity.propertiesContent;

		if (!content) {
			// make onChange function a local variable, because it's used several times
			var changeFunction = function(){
				// extract changed properties and redraw the activity, if needed
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					activity.title = newTitle;
					redrawNeeded = true;
				}
				
				activity.groupingType = $('.propertiesContentFieldGroupingType', content).val();
				
				$('input[name="propertiesContentFieldGroupDivide"]', content).each(function(){
						// enable/disable division types, depending on radio buttons next to them
						$(this).next().find('.spinner').spinner('option', 'disabled', !$(this).is(':checked'));
					})
					// hide group/learner division with some grouping types
					.add($('.propertiesContentFieldLearnerCount', content).closest('tr'))
					.css('display', activity.groupingType == 'monitor' ? 'none' : '');
				
				$('.propertiesContentFieldEqualSizes, .propertiesContentFieldViewLearners', content)
					.closest('tr').css('display', activity.groupingType == 'learner' ? '' : 'none');
				
				activity.groupDivide = activity.groupingType == 'monitor' ||
					$('.propertiesContentFieldGroupCountEnable', content).is(':checked') ?
					'groups' : 'learners';
				$('.propertiesContentFieldNameGroups', content).css('display',
						activity.groupDivide == 'groups' ? '' : 'none');		
				
				activity.groupCount = +$('.propertiesContentFieldGroupCount', content).val();
				activity.learnerCount = +$('.propertiesContentFieldLearnerCount', content).val();
				activity.equalSizes = $('.propertiesContentFieldEqualSizes', content).is(':checked');
				activity.viewLearners = $('.propertiesContentFieldViewLearners', content).is(':checked');
				
				if (redrawNeeded) {
					activity.draw();
				}
			};
			
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentGrouping').clone().attr('id', null)
													.show().data('parentObject', activity);
			
			// init widgets
			$('.propertiesContentFieldTitle', content).val(activity.title);
			$('.propertiesContentFieldGroupingType', content).val(activity.groupingType);
			if (activity.groupDivide == 'learners') {
				$('.propertiesContentFieldLearnerCountEnable', content).attr('checked', 'checked');
			} else {
				$('.propertiesContentFieldGroupCountEnable', content).attr('checked', 'checked');
			}	
			
			// create groups/learners spinners
			$('.propertiesContentFieldGroupCount', content).spinner({
				'min' : 2,
				'disabled' : activity.groupDivide == 'learners'
			}).spinner('value', activity.groupCount)
			  .on('spinchange', changeFunction);
			
			$('.propertiesContentFieldLearnerCount', content).spinner({
				'min' 	   : 1,
				'disabled' : activity.groupDivide == 'groups'
			}).spinner('value', activity.learnerCount)
			  .on('spinchange', changeFunction);
			
			$('.propertiesContentFieldEqualSizes', content).attr('checked', activity.equalSizes ? 'checked' : null);
			$('.propertiesContentFieldViewLearners', content).attr('checked', activity.viewLearners ? 'checked' : null);
			$('.propertiesContentFieldNameGroups', content).button().click(function(){
				PropertyLib.openGroupNamingDialog(activity);
			});
			
			$('input, select', content).change(changeFunction);
			changeFunction.call(content);
		}
	},
	
	
	/**
	 * Properties dialog content for Gates.
	 */
	gateProperties : function() {
		var activity = this,
			content = activity.propertiesContent;
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentGate').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldTitle', content).val(activity.title);
			$('.propertiesContentFieldGateType', content).val(activity.gateType);

			$('input, select', content).change(function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					activity.title = newTitle;
					redrawNeeded = true;
				}
				activity.gateType = $('.propertiesContentFieldGateType', content).val();
				
				if (redrawNeeded) {
					activity.draw();
				}
			});
		}
	},
	
	
	/**
	 * Properties dialog content for Branching activities.
	 */
	branchingProperties : function() {
		var activity = this,
			content = activity.propertiesContent;
		
		var fillWidgetsFunction = function(){
			$('.propertiesContentFieldTitle', content).val(activity.branchingActivity.title);
			$('.propertiesContentFieldBranchingType', content).val(activity.branchingActivity.branchingType);
			PropertyLib.fillGroupingDropdown(content, activity.branchingActivity.grouping);
			PropertyLib.fillToolInputDropdown(content, activity.branchingActivity.input);
		}
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentBranching').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldMatchGroups', content).button().click(function(){
				PropertyLib.openGroupsToBranchesMappingDialog(activity);
			});
			
			var changeFunction = function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					branchingActivity = activity.branchingActivity,
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle != branchingActivity.title) {
					branchingActivity.title = newTitle;
					redrawNeeded = true;
				}
				branchingActivity.branchingType = $('.propertiesContentFieldBranchingType', content).val();
				
				var groupingRow = $('.propertiesContentFieldGrouping', content).closest('tr');
				if (branchingActivity.branchingType == 'group') {
					branchingActivity.grouping = groupingRow.show()
												.find('option:selected').data('grouping');
				} else {
					groupingRow.hide();
					branchingActivity.grouping = null;
				}
				$('.propertiesContentFieldMatchGroups', content).closest('tr')
					.css('display', branchingActivity.grouping ? '' : 'none');
				
				var inputRow = $('.propertiesContentFieldInput', content).closest('tr');
				if (branchingActivity.branchingType == 'tool') {
					branchingActivity.input = inputRow.show()
						.find('option:selected').data('parentObject');
				} else {
					inputRow.hide();
					branchingActivity.input = null;
				}

				if (redrawNeeded) {
					branchingActivity.start.draw();
					branchingActivity.end.draw();
				}
			}
			
			$('input, select', content).change(changeFunction);
			fillWidgetsFunction();
			changeFunction.call(content);
		}
		
		fillWidgetsFunction();
	},
	
	
	/**
	 * Properties dialog content for Parallel activities.
	 */
	parallelProperties : function() {
		var activity = this,
			content = activity.propertiesContent;
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentParallel').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldTitle', content).val(activity.title);
			
			$('input, select', content).change(function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					activity.title = newTitle;
					redrawNeeded = true;
				}
				var newGroupingValue = $('.propertiesContentFieldGrouping option:selected', content)
									.data('grouping');
				if (newGroupingValue != activity.grouping) {
					activity.grouping = newGroupingValue;
					redrawNeeded = true;
				}
				
				if (redrawNeeded) {
					activity.draw();
				}
			});
		}
		
		PropertyLib.fillGroupingDropdown(content, activity.grouping);
	},
	
	
	/**
	 * Properties dialog content for Optional Activity.
	 */
	optionalActivityProperties : function() {
		var activity = this,
			content = activity.propertiesContent;

		activity.minActivities = Math.min(activity.minActivities, activity.childActivities.length);
		activity.maxActivities = Math.min(activity.maxActivities, activity.childActivities.length);
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentOptionalActivity').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldTitle', content).val(activity.title);
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					activity.title = newTitle;
					activity.draw();
				}
			});
				
			$('.propertiesContentFieldOptionalActivityMin', content).spinner({'min' : 0})
			  .on('spinchange', function(){
				  var value = +$(this).val();
				  activity.minActivities = Math.min(value, activity.childActivities.length);
				  if (value != activity.minActivities) {
					  $(this, content).spinner('value', activity.minActivities);
				  }
				  if (activity.minActivities > activity.maxActivities) {
					  $('.propertiesContentFieldOptionalActivityMax', content).spinner('value', value);
				  }
				  $('.propertiesContentFieldOptionalActivityMax', content).spinner('option', 'min', value);
			  });
			
			
			$('.propertiesContentFieldOptionalActivityMax', content).spinner({'min' : 0})
			  .on('spinchange', function(){
				  var value = +$(this).val();
				  activity.maxActivities = Math.min(value, activity.childActivities.length);
				  if (value != activity.maxActivities) {
					  $(this, content).spinner('value', activity.maxActivities);
				  }
			  });
		}
		
		$('.propertiesContentFieldOptionalActivityMin', content).spinner('value', activity.minActivities)
																.spinner('option', 'max', activity.childActivities.length);
		$('.propertiesContentFieldOptionalActivityMax', content).spinner('value', activity.maxActivities)
																.spinner('option', {
																	'min' : activity.minActivities,
																	'max' : activity.childActivities.length
																});
	},
	
	
	/**
	 * Properties dialog content for regions (annotations).
	 */
	regionProperties : function() {
		var region = this,
			content = region.propertiesContent;
		if (!content) {
			// first run, create the content
			content = region.propertiesContent = $('#propertiesContentRegion').clone().attr('id', null)
													.show().data('parentObject', region);
			$('.propertiesContentFieldTitle', content).val(region.title);
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContainer'),
					region = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != region.title) {
					region.title = newTitle;
					redrawNeeded = true;
				}
				
				if (redrawNeeded) {
					region.draw();
				}
			});
		}
	},
	
	
	/**
	 * Properties dialog content for labels (annotations).
	 */
	labelProperties : function() {
		var label = this,
			content = label.propertiesContent;
		if (!content) {
			// first run, create the content
			content = label.propertiesContent = $('#propertiesContentLabel').clone().attr('id', null)
													.show().data('parentObject', label);
			$('.propertiesContentFieldTitle', content).val(label.title);
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContainer'),
					label = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != label.title) {
					label.title = newTitle;
					redrawNeeded = true;
				}
				
				if (redrawNeeded) {
					label.draw();
				}
			});
		}
	},
	
	
	/**
	 * 	Find all groupings on canvas and fill dropdown menu with their titles
	 */
	fillGroupingDropdown : function(content, grouping) {
		// find all groupings on canvas and fill dropdown menu with their titles
		var emptyOption = $('<option />'),
			groupingDropdown = $('.propertiesContentFieldGrouping', content).empty().append(emptyOption);
		$.each(layout.activities, function(){
			if (this instanceof ActivityLib.GroupingActivity) {
				var option = $('<option />').text(this.title)
											.appendTo(groupingDropdown)
											// store reference to grouping object
											.data('grouping', this);
				if (this == grouping) {
					option.attr('selected', 'selected');
				}
			}
		});
		if (!grouping) {
			// no grouping selected
			emptyOption.attr('selected', 'selected');
		}
	},
	
	
	/**
	 * 	Find all activities that support outputs and fill dropdown menu with their titles
	 */
	fillToolInputDropdown : function(content, input) {
		// find all tools that support input and fill dropdown menu with their titles
		var emptyOption = $('<option />'),
			inputDropdown = $('.propertiesContentFieldInput', content).empty().append(emptyOption);
		$.each(layout.activities, function(){
			if (this instanceof ActivityLib.ToolActivity && layout.toolMetadata[this.toolID].supportsOutputs) {
				var option = $('<option />').text(this.title)
											.appendTo(inputDropdown)
											// store reference to grouping object
											.data('parentObject', this);
				if (this == input) {
					option.attr('selected', 'selected');
				}
			}
		});
		if (!input) {
			// no grouping selected
			emptyOption.attr('selected', 'selected');
		}
	},
	
	
	/**
	 * Fills group naming dialog with existing group names and opens it.
	 */
	openGroupNamingDialog : function(activity) {
		var dialog = layout.items.groupNamingDialog;
		// remove existing entries and add reference to the initiating activity
		dialog.empty().dialog('option', 'parentObject', activity);
		
		activity.groups = PropertyLib.fillNameAndUIIDList(activity.groupCount,
				activity.groups, 'name', 'Group ');
		$.each(activity.groups, function(){
			$('<input type="text" />').addClass('groupName').appendTo(dialog).val(this.groupName);
			dialog.append('<br />');
		});
		
		dialog.dialog('open');
	},
	
	
	/**
	 * Opens dialog for assigned groups to branches in branching activity.
	 */
	openGroupsToBranchesMappingDialog : function(activity) {
		var dialog = layout.items.groupsToBranchesMappingDialog,
			branchingActivity = activity.branchingActivity,
			grouping = branchingActivity.grouping,
			groupsCell = $('#gtbGroupsCell', dialog),
			branchesCell = $('#gtbBranchesCell', dialog),
			groupCell = $('#gtbMappingGroupCell', dialog),
			branchCell = $('#gtbMappingBranchCell', dialog),
			branches = branchingActivity.branches;
		
		// remove existing entries and add reference to the initiating activity
		dialog.dialog('option', 'branchingActivity', branchingActivity);
		$('.gtbListCell', dialog).empty();
		
		// find group names and create DOM elements out of them
		grouping.groups = PropertyLib.fillNameAndUIIDList(grouping.groupCount,
				grouping.groups, 'name', 'Group ');
		branches = branchingActivity.branches = PropertyLib.fillNameAndUIIDList(branches.length,
				branches, 'title', 'Branch ');
		
		$.each(grouping.groups, function(){
			var group = this,
				groupElem = $('<div />').click(PropertyLib.selectGroupsToBranchesListItem)
										.text(group.name).attr('uiid', group.uiid);
			
			// check if a group-branch mapping is already defined
			$.each(branchingActivity.groupsToBranches, function() {
				if (this.group == group) {
					var branchElem = $('<div />').click(PropertyLib.selectGroupsToBranchesListItem)
												 .appendTo(branchCell)
												 .text(this.branch.title)
												 .attr('uiid', this.branch.uiid)
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
		$.each(branches, function(){
			$('<div />').click(PropertyLib.selectGroupsToBranchesListItem).appendTo(branchesCell)
						.text(this.title).attr('uiid', this.uiid);
		});
		
		dialog.dialog('open');
	},
	
	
	/**
	 * Selects a list item and optionally the matched pair.
	 */
	selectGroupsToBranchesListItem : function(){
		var item = $(this),
			boundItem = item.data('boundItem');
		
		item.siblings().removeClass('selected');
		item.addClass('selected');
		
		if (boundItem) {
			boundItem.siblings().removeClass('selected');
			boundItem.addClass('selected');
		}
	},
	
	
	/**
	 * Creates group and branch names, if they are not already provided.
	 */
	fillNameAndUIIDList : function(size, existingList, nameAttr, prefix) {
		var list = [];
		for (var itemIndex = 1; itemIndex <= size; itemIndex++) {
			// generate a name and an UIID if they do not exist
			var item = itemIndex > existingList.length ? {} : existingList[itemIndex - 1];
			if (!item[nameAttr]) {
				item[nameAttr] = prefix + itemIndex;
			}
			if (!item.uiid) {
				// new unique UIID; TODO: change
				item.uiid = new Date().getTime() + '_' + itemIndex;
			}
			list.push(item);
		}
		return list;
	},
	
	
	/**
	 * Make a pair out of selected group and branch.
	 */
	addGroupToBranchMapping : function(){
		var dialog = layout.items.groupsToBranchesMappingDialog,
			selectedGroup = $('#gtbGroupsCell .selected', dialog),
			selectedBranch =  $('#gtbBranchesCell .selected', dialog);
		
		if (selectedGroup.length != 1 || selectedBranch.length != 1) {
			return;
		}
		
		// original branch stays in its list
		selectedBranch = selectedBranch.clone().click(PropertyLib.selectGroupsToBranchesListItem);
		// add info about the pair for later reference
		selectedGroup.data('boundItem', selectedBranch);
		selectedBranch.data('boundItem', selectedGroup);
		var groupCell = $('#gtbMappingGroupCell', dialog),
			branchCell = $('#gtbMappingBranchCell', dialog);
		// clear existing selection
		$('.selected', groupCell).removeClass('selected');
		$('.selected', branchCell).removeClass('selected');
		groupCell.append(selectedGroup);
		branchCell.append(selectedBranch);
	},
	
	
	/**
	 * Remove a mapping of group and branch.
	 */
	removeGroupToBranchMapping : function() {
		var dialog = layout.items.groupsToBranchesMappingDialog,
			selectedGroup = $('#gtbMappingGroupCell .selected', dialog),
			selectedBranch =  $('#gtbMappingBranchCell .selected', dialog);
	
		if (selectedGroup.length != 1 || selectedBranch.length != 1) {
			return;
		}
		
		selectedGroup.removeData('boundItem');
		selectedBranch.remove();
		$('#gtbGroupsCell', dialog).append(selectedGroup);
	}
};