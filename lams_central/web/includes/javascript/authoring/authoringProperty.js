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
						'my' : 'right top',
						'at' : 'right top',
						'of' :  '#canvas'
					},
					'resizable'     : false,
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
			            			activity = dialog.dialog('option', 'parentObject'),
			            			error = null;
			            		
			            		// extract group names from text fields
			            		$('input', dialog).each(function(){
			            			var groupName = $(this).val().trim();
				            		if (!nameValidator.test(groupName)) {
				            			error = 'Group name can not contain any of these characters < > ^ * @ % $';
				            			return false;
				            		}
			            		});
			            		
			            		if (error) {
			            			alert(error);
			            			return;
			            		}
			            		
			            		$('input', dialog).each(function(index){
		            				activity.groups[index].name = $(this).val().trim();
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
		var gtbDialog = layout.items.groupsToBranchesMappingDialog = $('#branchMappingDialog')
			.clone()
			.attr('id','gtbDialog')
			.dialog({
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
			            			branchingActivity = dialog.dialog('option', 'branchingActivity'),
			            			assignedToDefault = false;

			            		// find references to groups and branches
			            		branchingActivity.groupsToBranches = [];
			            		$('.branchMappingBoundItemCell div, .branchMappingFreeItemCell div', dialog).each(function(){
			            			var groupUIID = +$(this).attr('uiid'),
			            				boundItem = $(this).data('boundItem'),
			            				branchUIID = boundItem ? +boundItem.attr('uiid') : null,
			            				group = null,
			            				branch = null;
			            			
			            			if (branchUIID) {
				            			$.each(branchingActivity.branches, function(){
				            				if (branchUIID == this.uiid) {
				            					branch = this;
				            					return false;
				            				}
				            			});
			            			} else {
			            				branch = branchingActivity.branches[0];
			            				assignedToDefault = true;
			            			}
			            			
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
			            		
		            			if (assignedToDefault){
		            				alert('All remaining groups will be mapped to the default branch');
		            			}
			            		dialog.dialog('close');
			            		setModified(true);
			            	}
			             }
			]
		});
		
		$('.branchMappingAddButton', gtbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-next'
			},
			'text' : false
		}).click(function(){
			PropertyLib.addBranchMapping(gtbDialog);
		});
		$('.branchMappingRemoveButton', gtbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-prev'
			},
			'text' : false
		}).click(function(){
			PropertyLib.removeBranchMapping(gtbDialog);
		});
		$('.branchMappingFreeItemHeaderCell', gtbDialog).text('Groups');
		$('.branchMappingBoundItemHeaderCell', gtbDialog).text('Group');
		
		layout.dialogs.push(gtbDialog);
		
		
		var outputConditionsDialog = layout.items.outputConditionsDialog = $('#outputConditionsDialog').dialog({
			'autoOpen' : false,
			'modal'  : true,
			'show'   : 'fold',
			'hide'   : 'fold',
			'position' : {
				'of' :  '#canvas'
			},
			'width'  : 400,
			'height' : 400,
			'title'  : 'Select Output Conditions for Input',
			'buttons' : [
				{
					'class'  : 'outputSelectDependent rangeOutputButton',
					'text'   : 'Clear all',
					'click'  : function() {
						var rows = $('#rangeConditions td', this).closest('tr');
						rows.each(function(){
							if ($(this).data('mappingEntry').branch) {
								if (confirm('There are conditions linked to an existing branch.\nDo you wish to remove them?')) {
									rows = null;
								}
								return false;
							}
						});
						
						if (rows) {
							rows.remove();
						}
					}
				},
				{
					'class'  : 'outputSelectDependent rangeOutputButton',
					'text'   : 'Remove',
					'click'  : function() {
						var selected = $('#rangeConditions tr.selected', this);
						if (!selected.data('mappingEntry').branch
								|| confirm('This condition is linked to an existing branch.\nDo you wish to remove it?')) {
							selected.remove();
						}
					}
				},
				{
					'class'  : 'outputSelectDependent complexOutputButton',
					'text'   : 'Refresh',
					'click'  : function() {
						$(this).dialog('option', 'refreshDefinitions')();
						$(this).dialog('option', 'buildContent')();
					}
				},
				{
					'text'   : 'Cancel',
					'click'  : function() {
						var dialog = $(this);
						dialog.dialog('close');
					}
				},
				{
					'text'   : 'OK',
					'click'  : function() {
						var dialog = $(this),
							activity = dialog.dialog('option', 'parentObject');
						
						if (activity instanceof ActivityLib.BranchingActivity) {
							activity.conditionsToBranches = [];
							$('#rangeConditions tr, #complexConditions li', dialog).each(function(){
								var mappingEntry = $(this).is(':visible') ? $(this).data('mappingEntry') : null;
								if (!mappingEntry) {
									return true;
								}
								
								if (!mappingEntry.uiid) {
									mappingEntry.uiid = ++layout.ld.maxUIID;
								}
								if (!mappingEntry.condition.conditionUIID) {
									mappingEntry.condition.conditionUIID = ++layout.ld.maxUIID;
								}
								
								var input = $('input', this);
								if (input.length > 0) {
									mappingEntry.condition.displayName = input.val();
								}
								activity.conditionsToBranches.push(mappingEntry);
							});
						}
												
						dialog.dialog('close');
						PropertyLib.openConditionsToBranchesMappingDialog(activity);
					}
				}
			 ],
			 
			 'open' : function(){
				 $(this).dialog('option', 'buildContent')();
			 }
		});
		
		outputConditionsDialog.dialog('option', 
			{
			'refreshDefinitions' : function(){
				var dialog = layout.items.outputConditionsDialog,
					activity = dialog.dialog('option', 'parentObject');
				
				$.ajax({
					url : LAMS_URL + 'authoring/author.do',
					data : {
						'method' : 'getToolOutputDefinitionsJSON',
						'toolContentID' : activity.input.toolContentID || activity.input.toolID
					},
					cache : false,
					async: false,
					dataType : 'json',
					success : function(response) {
						activity.input.outputDefinitions = response;
					}
				});
			},
			
			
			'buildContent' : function() {
				var dialog = layout.items.outputConditionsDialog,
					activity = dialog.dialog('option', 'parentObject'),
					outputSelect = $('#outputSelect', dialog),
					emptyOption = $('option[value="none"]', outputSelect).attr('selected', 'selected'),
					outputName = activity.conditionsToBranches && activity.conditionsToBranches.length > 0
							 	 ? activity.conditionsToBranches[0].condition.name.split('#')[0] : null;
				$('option[value!="none"]', outputSelect).remove();
				
				if (!activity.input.outputDefinitions) {
					dialog.dialog('option', 'refreshDefinitions')();
				}
				
				if (activity.input.outputDefinitions) {
					$.each(activity.input.outputDefinitions,function(){
						var suffix = '';
						switch(this.type) {
							case 'OUTPUT_COMPLEX' :
								suffix = ' (user defined)';
								break;
													
							case 'OUTPUT_LONG' :
								suffix = ' (range)';
								break;
						};
		
						this.toolActivityUIID = activity.input.uiid;
						var option = $('<option />')
									   .text(this.description + suffix)
									   .data('output', this)
									   .appendTo(outputSelect);
						if (this.name == outputName) {
							option.attr('selected', 'selected');
							emptyOption.attr('selected', null);
						}
					});
				}
				
				dialog.dialog('option', 'outputChange')();
			},
			
			
			'outputChange' : function(){
				var dialog = layout.items.outputConditionsDialog,
					container = dialog.closest('.ui-dialog'),
				 	activity = dialog.dialog('option', 'parentObject'),
				    outputOption = $('#outputSelect option:selected', dialog),
				    output = outputOption.data('output');
				
				$('.outputSelectDependent', container).hide();
				if (!output) {
					return;
				}
				
				var complexConditionNames = $('#complexConditions', dialog),
					rangeConditionNames = $('#rangeConditions', dialog),
					complexOutputWidgets = $('.complexOutputButton', container).add(complexConditionNames),
					rangeOutputWidgets = $('#rangeOptionSelect, .rangeOutputButton', container).add(rangeConditionNames);
				if (output.showConditionNameOnly) {
					complexOutputWidgets.show();
					var list =  $('ul', complexConditionNames);
					$('li', list).remove();
					 
					if (output.defaultConditions){
						$.each(output.defaultConditions, function(){
							$('<li />').text(this.displayName)
									   .data('mappingEntry', {
										   'condition' : {
											   'name' 			  : this.name,
											   'displayName' 	  : this.displayName,
											   'type' 			  : 'OUTPUT_COMPLEX',
											   'toolActivityUIID' : output.toolActivityUIID
										   }
									   })			   
							  		   .appendTo(list);
						});
					}
				} else {
					rangeOutputWidgets.show();
					outputConditionsDialog.dialog('option', 'rangeOptionChange')();
					
					$('td',rangeConditionNames).closest('tr').remove();
					if (activity.conditionsToBranches) {
						$.each(activity.conditionsToBranches, function(){
							if (output.toolActivityUIID == this.condition.toolActivityUIID
									&& output.name == this.condition.name) {
								if (this.condition.exactMatchValue) {
									this.condition.startValue = this.condition.endValue = this.condition.exactMatchValue;
								}
								outputConditionsDialog.dialog('option', 'addRangeConditionRow')(this);
							}
						});
					}
				}
			},
			
			'rangeOptionChange' : function(){
				var dialog = layout.items.outputConditionsDialog,
					selectedOption = $('#rangeOptionSelect option:selected', dialog).attr('value');
				
				switch(selectedOption){
					case 'greater':
					case 'less':
						$('#rangeAddDiv, #singleRangeSpinner', dialog).show();
						$('#multiRangeDiv', dialog).hide();
						break;
					case 'range':
						$('#rangeAddDiv, #multiRangeDiv', dialog).show();
						$('#singleRangeSpinner', dialog).hide();
						break;
					default:
						$('#rangeAddDiv', dialog).hide();
				}
			},
			
			
			'addRangeConditionRow' : function(mappingEntry){
				var condition = mappingEntry.condition;
				if (condition.type != 'OUTPUT_LONG') {
					return;
				}
				var rangeConditionNames = $('#outputConditionsDialog #rangeConditions'),
					conditionText = null;
				
				if (condition.exactMatchValue) {
					conditionText = 'Exact value of ' + +condition.exactMatchValue;
				} else if (typeof condition.startValue == 'undefined') {
					conditionText = 'Less than or eq ' + +condition.endValue;
				} else if (typeof condition.endValue == 'undefined') {
					conditionText = 'Greater than or eq ' + +condition.startValue;
				} else {
					conditionText = 'Range ' + +condition.startValue + ' to ' + +condition.endValue;
				}
				
				var row = $('<tr />').appendTo(rangeConditionNames).data('mappingEntry', mappingEntry).click(function(){
						$(this).addClass('selected').siblings('tr').removeClass('selected');
					}),
					nameCell = $('<td />').appendTo(row),
					nameInput = $('<input />').val(condition.displayName).appendTo(nameCell),
					conditionCell = $('<td />').text(conditionText).appendTo(row);
			}
		});
		
		
		$('#outputSelect', outputConditionsDialog).change(outputConditionsDialog.dialog('option', 'outputChange'));
		$('#rangeOptionSelect', outputConditionsDialog).change(outputConditionsDialog.dialog('option', 'rangeOptionChange'));
		$('#rangeAddButton', outputConditionsDialog).button().click(function(){
			var dialog = layout.items.outputConditionsDialog, 
				rangeConditionNames = $('#rangeConditions', dialog),
				selectedOption = $('#rangeOptionSelect option:selected', dialog).attr('value'),
				output = $('#outputSelect option:selected', dialog).data('output'),
				condition = {
					'name' : output.name,
					'type' : 'OUTPUT_LONG',
 				    'toolActivityUIID' : output.toolActivityUIID
				};
				
				switch(selectedOption){
					case 'greater':
						condition.startValue = $('#singleRangeSpinner', dialog).val();
						break;
					case 'less':
						condition.endValue = $('#singleRangeSpinner', dialog).val();
						break;
					case 'range':
						condition.startValue = $('#multiRangeFromSpinner', dialog).val();
						condition.endValue = $('#multiRangeToSpinner', dialog).val();
						if (condition.startValue == condition.endValue) {
							condition.exactMatchValue = condition.startValue;
						}
						break;
				}
				
				$('td', rangeConditionNames).closest('tr').each(function(){
					var existingCondition = $(this).data('mappingEntry').condition;
					
					if ((typeof condition.startValue == 'undefined' && existingCondition.startValue <= condition.endValue)
						|| (typeof condition.endValue == 'undefined'
							&& (typeof existingCondition.endValue == 'undefined' || existingCondition.endValue >= condition.startValue))
						|| (!(condition.startValue > existingCondition.endValue) && !(condition.endValue < existingCondition.startValue))) {
						alert('The start value can not be within the range of an existing condition');
						condition = null;
						return false;
					}
					
					if ((typeof condition.endValue == 'undefined' && existingCondition.endValue >= condition.startValue)
						|| (typeof condition.startValue == 'undefined'
							&& (typeof existingCondition.startValue == 'undefined' || existingCondition.startValue <= condition.endValue))
						|| (!(condition.endValue < existingCondition.startValue) && !(condition.startValue > existingCondition.endValue))) {
						alert('The end value can not be within the range of an existing condition');
						condition = null;
						return false;
					}
				});
				
				if (!condition){
					return;
				}

				var nameIndex = 1;
				while (!condition.displayName) {
					condition.displayName = 'Untitled ' + nameIndex;
					$('input', rangeConditionNames).each(function(){
						if (condition.displayName == $(this).val()) {
							condition.displayName = null;
							nameIndex++;
							return false;
						}
					});
				}
				
				dialog.dialog('option', 'addRangeConditionRow')({'condition' : condition});
		});
		$('#singleRangeSpinner, #multiRangeFromSpinner, #multiRangeToSpinner', outputConditionsDialog).spinner({
			'min' : 0,
		}).spinner('value', 0);
		
		layout.dialogs.push(outputConditionsDialog);
		
		
		// initialise dialog for matching conditions to branches in branching activities
		var ctbDialog = layout.items.conditionsToBranchesMappingDialog = $('#branchMappingDialog')
			.clone()
			.attr('id','ctbDialog')
			.dialog({
				'autoOpen' : false,
				'modal'  : true,
				'show'   : 'fold',
				'hide'   : 'fold',
				'position' : {
					'of' :  '#canvas'
				},
				'width'  : 800,
				'height' : 400,
				'title'  : 'Match Conditions to Branches',
				'buttons' : [
				             {
				            	'text'   : 'OK',
				            	'click'  : function() {
				            		var dialog = $(this),
				            			branchingActivity = dialog.dialog('option', 'branchingActivity'),
				            			assignedToDefault = false;
				            		
			            			$.each(branchingActivity.conditionsToBranches, function(){
			            				var mappingEntry = this;
			            				mappingEntry.branch = null;
					            		// find references to conditions and branches
					            		$('.branchMappingBoundItemCell div', dialog).each(function(){
					            			var entryUIID = +$(this).attr('uiid'),
					            				branchUIID = +$(this).data('boundItem').attr('uiid');

					            			if (entryUIID == mappingEntry.uiid) {
						            			$.each(branchingActivity.branches, function(){
						            				if (branchUIID == this.uiid) {
						            					mappingEntry.branch = this;
						            					return false;
						            				}
						            			});
						            			return false;
					            			}
					            		});
					            		
				            			if (!mappingEntry.branch) {
				            				assignedToDefault = true;
				            				mappingEntry.branch = branchingActivity.branches[0];
				            			}
			            			});
			            			
				            		
			            			if (assignedToDefault){
			            				alert('All remaining conditions will be mapped to the default branch');
			            			}
			            			
				            		dialog.dialog('close');
				            		setModified(true);
				            	}
				             }
				]
		});
		
		$('.branchMappingAddButton', ctbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-next'
			},
			'text' : false
		}).click(function(){
			PropertyLib.addBranchMapping(ctbDialog);
		});
		$('.branchMappingRemoveButton', ctbDialog).button({
			'icons' : {
				'primary' :  'ui-icon-seek-prev'
			},
			'text' : false
		}).click(function(){
			PropertyLib.removeBranchMapping(ctbDialog);
		});
		$('.branchMappingFreeItemHeaderCell', ctbDialog).text('Conditions');
		$('.branchMappingBoundItemHeaderCell', ctbDialog).text('Condition');
		
		layout.dialogs.push(ctbDialog);
	},
	
	openPropertiesDialog : function(object) {
		object.loadPropertiesDialogContent();
		var dialog = layout.items.propertiesDialog;
		dialog.children().detach();
		dialog.append(object.propertiesContent);
		dialog.dialog('open');
		dialog.find('input').blur();
		var box = object.items.getBBox(),
			x = box.x2 + canvas.offset().left + 5,
			y = box.y + canvas.offset().top;
		dialog.dialog('option', 'position',	[x, y]);
		if (dialog.offset().left < box.x2 + canvas.offset().left) {
			// if dialog covers the activity (too close to right border),
			// move it to the other side
			x = box.x + canvas.offset().left - dialog.width() - 35;
			dialog.dialog('option', 'position',	[x, y]);
		}
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
            		if (!nameValidator.test(newTitle)) {
            			alert('Transition title can not contain any of these characters < > ^ * @ % $');
            		} else {
						transition.title = newTitle;
						if (transition.branch) {
							transition.branch.title = newTitle;
						}
						redrawNeeded = true;
            		}
				}
				
				if (redrawNeeded) {
					transition.draw();
					ActivityLib.addSelectEffect(activity, true);
					setModified(true);
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
			if (!allowsGrouping) {
				$('.propertiesContentFieldGrouping', content).closest('tr').remove();
			}
			
			$('input, select', content).change(function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					if (!nameValidator.test(newTitle)) {
            			alert('Activity title can not contain any of these characters < > ^ * @ % $');
            		} else {
						activity.title = newTitle;
						redrawNeeded = true;
            		}
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
					ActivityLib.addSelectEffect(activity, true);
					setModified(true);
				}
			});
		}
		
		if (allowsGrouping){
			PropertyLib.fillGroupingDropdown(activity, activity.grouping);
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
					if (!nameValidator.test(newTitle)) {
            			alert('Grouping title can not contain any of these characters < > ^ * @ % $');
            		} else {
						activity.title = newTitle;
						redrawNeeded = true;
            		}
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
					ActivityLib.addSelectEffect(activity, true);
				}
				
				setModified(true);
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
			
			// make onChange function a local variable, because it's used several times
			var changeFunction = function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle != activity.title) {
					if (!nameValidator.test(newTitle)) {
            			alert('The title can not contain any of these characters < > ^ * @ % $');
            		} else {
						activity.title = newTitle;
						redrawNeeded = true;
            		}
				}
				activity.gateType = $('.propertiesContentFieldGateType', content).val();
				if (activity.gateType == 'schedule') {
					$(".propertiesContentRowGateSchedule").show();
					activity.offsetDay = +$('.propertiesContentFieldOffsetDay', content).val();
					activity.offsetHour = +$('.propertiesContentFieldOffsetHour', content).val();
					activity.offsetMinute = +$('.propertiesContentFieldOffsetMinute', content).val();
					activity.gateActivityCompletionBased = $('.propertiesContentFieldActivityCompletionBased').is(':checked');
				} else {
					$(".propertiesContentRowGateSchedule").hide();
				}
				
				if (redrawNeeded) {
					activity.draw();
					ActivityLib.addSelectEffect(activity, true);
				}
				
				setModified(true);
			};
			
			// create groups/learners spinners
			$('.propertiesContentFieldOffsetDay', content).spinner({
				'min' : 0,
				'max' : 364
			}).spinner('value', activity.offsetDay || 0)
			  .on('spinchange', changeFunction);
			
			$('.propertiesContentFieldOffsetHour', content).spinner({
				'min' : 0,
				'max' : 23
			}).spinner('value', activity.offsetHour || 0)
			  .on('spinchange', changeFunction);
			
			$('.propertiesContentFieldOffsetMinute', content).spinner({
				'min' : 0,
				'max' : 59
			}).spinner('value', activity.offsetMinute || 0)
			  .on('spinchange', changeFunction);
			
			$('.propertiesContentFieldActivityCompletionBased', content)
				.attr('checked', activity.gateActivityCompletionBased? 'checked' : null);
			
			$('input, select', content).change(changeFunction);
			changeFunction.call(content);
		}
		
		if (activity.transitions.to.length == 0){
			$('.propertiesContentFieldActivityCompletionBased', content)
				.attr('checked', null)
				.attr('disabled', 'disabled');
		} else {
			$('.propertiesContentFieldActivityCompletionBased', content)
				.attr('disabled', null);
		}
	},
	
	
	/**
	 * Properties dialog content for Branching activities.
	 */
	branchingProperties : function() {
		var activity = this,
			content = activity.propertiesContent,
			fillWidgetsFunction = function(){
				$('.propertiesContentFieldTitle', content).val(activity.branchingActivity.title);
				$('.propertiesContentFieldBranchingType', content).val(activity.branchingActivity.branchingType);
				PropertyLib.fillGroupingDropdown(activity, activity.branchingActivity.grouping);
				PropertyLib.fillToolInputDropdown(activity, activity.branchingActivity.input);
				
				$('.propertiesContentFieldOptionalSequenceMin', content).spinner('value',
																				 activity.branchingActivity.minOptions)
																		.spinner('option', 'max',
																				 activity.branchingActivity.branches.length);
				$('.propertiesContentFieldOptionalSequenceMax', content).spinner('value',
																				 activity.branchingActivity.maxOptions)
																		.spinner('option', {
																			'min' : activity.branchingActivity.minOptions,
																			'max' : activity.branchingActivity.branches.length
																		});
				if (activity.branchingActivity.branches.length == 0) {
					$('.propertiesContentFieldCreateConditions, .propertiesContentFieldMatchConditions,'+
					  '.propertiesContentFieldMatchGroups', content)
						.closest('tr').hide();
				}
			}
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentBranching').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldMatchGroups', content).button().click(function(){
				PropertyLib.openGroupsToBranchesMappingDialog(activity.branchingActivity);
			});
			$('.propertiesContentFieldCreateConditions', content).button().click(function(){
				PropertyLib.openOutputConditionsDialog(activity.branchingActivity);
			});
			$('.propertiesContentFieldMatchConditions', content).button().click(function(){
				PropertyLib.openConditionsToBranchesMappingDialog(activity.branchingActivity);
			});
			
			var changeFunction = function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContainer'),
					activity = content.data('parentObject'),
					branchingActivity = activity.branchingActivity,
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle != branchingActivity.title) {
					if (!nameValidator.test(newTitle)) {
            			alert('The title can not contain any of these characters < > ^ * @ % $');
            		} else {
						branchingActivity.title = newTitle;
						redrawNeeded = true;
            		}
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
					.css('display', branchingActivity.grouping && branchingActivity.branches.length > 0 ? '' : 'none');
				
				var inputRow = $('.propertiesContentFieldInput', content).closest('tr'),
					inputDefinitionRows = $('.propertiesContentFieldCreateConditions, .propertiesContentFieldMatchConditions', content)
										.closest('tr');
				if (branchingActivity.branchingType == 'tool') {
					branchingActivity.input = inputRow.show()
						.find('option:selected').data('input');
					if (branchingActivity.input && branchingActivity.branches.length > 0) {
						inputDefinitionRows.show();
					} else {
						inputDefinitionRows.hide();
					}
				} else {
					inputRow.hide();
					inputDefinitionRows.hide();
					branchingActivity.input = null;
				}
				
				var optionalSequenceRows = $('.spinner', content).closest('tr');
				if (branchingActivity.branchingType == 'optional') {
					optionalSequenceRows.show();
				} else {
					optionalSequenceRows.hide();
				}

				if (redrawNeeded) {
					branchingActivity.start.draw();
					branchingActivity.end.draw();
					ActivityLib.addSelectEffect(layout.items.selectedObject, true);
				}
				
				setModified(true);
			}
			
			$('.propertiesContentFieldOptionalSequenceMin', content).spinner({'min' : 0})
			  .on('spinchange', function(){
				  var value = +$(this).val();
				  activity.branchingActivity.minOptions = Math.min(value, activity.branchingActivity.branches.length);
				  if (value != activity.branchingActivity.minOptions) {
					  $(this, content).spinner('value', activity.branchingActivity.minOptions);
				  }
				  if (activity.branchingActivity.minOptions > activity.branchingActivity.maxOptions) {
					  $('.propertiesContentFieldOptionalSequenceMax', content).spinner('value', value);
				  }
				  $('.propertiesContentFieldOptionalSequenceMax', content).spinner('option', 'min', value);
			  });
			
			
			$('.propertiesContentFieldOptionalSequenceMax', content).spinner({'min' : 0})
			  .on('spinchange', function(){
				  var value = +$(this).val();
				  activity.branchingActivity.maxOptions = Math.min(value, activity.branchingActivity.branches.length);
				  if (value != activity.branchingActivity.maxOptions) {
					  $(this, content).spinner('value', activity.branchingActivity.maxOptions);
				  }
			  });
			
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
					if (!nameValidator.test(newTitle)) {
            			alert('The title can not contain any of these characters < > ^ * @ % $');
            		} else {
						activity.title = newTitle;
						redrawNeeded = true;
            		}
				}
				var newGroupingValue = $('.propertiesContentFieldGrouping option:selected', content)
									.data('grouping');
				if (newGroupingValue != activity.grouping) {
					activity.grouping = newGroupingValue;
					redrawNeeded = true;
				}
				
				if (redrawNeeded) {
					activity.draw();
					ActivityLib.addSelectEffect(activity, true);
					setModified(true);
				}
			});
		}
		
		PropertyLib.fillGroupingDropdown(activity, activity.grouping);
	},
	
	
	/**
	 * Properties dialog content for Optional Activity.
	 */
	optionalActivityProperties : function() {
		var activity = this,
			content = activity.propertiesContent;

		activity.minOptions = Math.min(activity.minOptions, activity.childActivities.length);
		activity.maxOptions = Math.min(activity.maxOptions, activity.childActivities.length);
		
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
					if (!nameValidator.test(newTitle)) {
            			alert('The title can not contain any of these characters < > ^ * @ % $');
            		} else {
						activity.title = newTitle;
						activity.draw();
						ActivityLib.addSelectEffect(activity, true);
						setModified(true);
            		}
				}
			});
				
			$('.propertiesContentFieldOptionalActivityMin', content).spinner({'min' : 0})
			  .on('spinchange', function(){
				  var value = +$(this).val();
				  activity.minOptions = Math.min(value, activity.childActivities.length);
				  if (value != activity.minOptions) {
					  $(this, content).spinner('value', activity.minOptions);
				  }
				  if (activity.minOptions > activity.maxOptions) {
					  $('.propertiesContentFieldOptionalActivityMax', content).spinner('value', value);
				  }
				  $('.propertiesContentFieldOptionalActivityMax', content).spinner('option', 'min', value);
			  });
			
			
			$('.propertiesContentFieldOptionalActivityMax', content).spinner({'min' : 0})
			  .on('spinchange', function(){
				  var value = +$(this).val();
				  activity.maxOptions = Math.min(value, activity.childActivities.length);
				  if (value != activity.maxOptions) {
					  $(this, content).spinner('value', activity.maxOptions);
				  }
			  });
		}
		
		$('.propertiesContentFieldOptionalActivityMin', content).spinner('value', activity.minOptions)
																.spinner('option', 'max', activity.childActivities.length);
		$('.propertiesContentFieldOptionalActivityMax', content).spinner('value', activity.maxOptions)
																.spinner('option', {
																	'min' : activity.minOptions,
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
			var color = region.items.shape.attr('fill');
			$('.propertiesContentFieldColor', content).val(color)
													  .simpleColor({
														'colors' : layout.colors.annotationPalette,
														'chooserCSS' : {
															'left'	   : 0,
															'top'      : '30px',
															'margin'   : '0'
														}
													  });
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContainer'),
					region = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val(),
					color = region.items.shape.attr('fill'),
					newColor = $('.propertiesContentFieldColor', content).val();
				if (newTitle != region.title) {
					if (!nameValidator.test(newTitle)) {
            			alert('The title can not contain any of these characters < > ^ * @ % $');
            		} else {
						region.title = newTitle;
						redrawNeeded = true;
            		}
				}
				redrawNeeded |= newColor != color;
				
				if (redrawNeeded) {
					region.draw(null, null, null, null, newColor);
					ActivityLib.addSelectEffect(region, true);
					setModified(true);
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
					if (!nameValidator.test(newTitle)) {
            			alert('The title can not contain any of these characters < > ^ * @ % $');
            		} else {
						label.title = newTitle;
						redrawNeeded = true;
            		}
				}
				
				if (redrawNeeded) {
					label.draw();
					ActivityLib.addSelectEffect(label, true);
					setModified(true);
				}
			});
		}
	},
	
	
	/**
	 * 	Find all groupings on canvas and fill dropdown menu with their titles
	 */
	fillGroupingDropdown : function(activity, grouping) {
		// find all groupings on canvas and fill dropdown menu with their titles
		var emptyOption = $('<option />').attr('selected', 'selected'),
			groupingDropdown = $('.propertiesContentFieldGrouping', activity.propertiesContent).empty().append(emptyOption),
			groupings = [];
		
		if (activity.parentActivity && activity.parentActivity instanceof ActivityLib.FloatingActivity) {
			// Support activities can use any grouping on canvas
			$.each(layout.activities, function(){
				if (this instanceof ActivityLib.GroupingActivity) {
					groupings.push(this);
				}
			});
		} else {
			// normal activities can use only preceeding groupings
			var candidate = activity;
			do {
				if (candidate.transitions && candidate.transitions.to.length > 0) {
					candidate = candidate.transitions.to[0].fromActivity;
				} else if (candidate.parentActivity) {
					candidate = candidate.parentActivity;
				} else {
					candidate = null;
				}
				
				if (candidate instanceof ActivityLib.GroupingActivity) {
					groupings.push(candidate);
				}
			} while (candidate != null);
		}

		// fill dropdown menu
		$.each(groupings, function(){
			var option = $('<option />').text(this.title)
						.appendTo(groupingDropdown)
						// store reference to grouping object
						.data('grouping', this);
			if (this == grouping) {
				emptyOption.removeAttr('selected');
				option.attr('selected', 'selected');
			}
		});
	},
	
	
	/**
	 * 	Find all activities that support outputs and fill dropdown menu with their titles
	 */
	fillToolInputDropdown : function(activity, input) {
		// find all tools that support input and fill dropdown menu with their titles
		var emptyOption = $('<option />'),
			inputDropdown = $('.propertiesContentFieldInput', activity.propertiesContent).empty().append(emptyOption),
			inputActivities = [],
			candidate = activity;

		do {
			if (candidate.transitions && candidate.transitions.to.length > 0) {
				candidate = candidate.transitions.to[0].fromActivity;
			} else if (candidate.parentActivity) {
				candidate = candidate.parentActivity;
			} else {
				candidate = null;
			}
			
			if (candidate instanceof ActivityLib.ToolActivity
					&& layout.toolMetadata[candidate.learningLibraryID].supportsOutputs) {
				inputActivities.push(candidate);
			}
		} while (candidate != null);
		

		// fill dropdown menu
		$.each(inputActivities, function(){
			var option = $('<option />').text(this.title)
						.appendTo(inputDropdown)
						// store reference to grouping object
						.data('input', this);
			if (this == input) {
				emptyOption.removeAttr('selected');
				option.attr('selected', 'selected');
			}
		});

	},
	
	
	/**
	 * Fills group naming dialog with existing group names and opens it.
	 */
	openGroupNamingDialog : function(activity) {
		var dialog = layout.items.groupNamingDialog;
		// remove existing entries and add reference to the initiating activity
		dialog.empty().dialog('option', 'parentObject', activity);
		
		$.each(activity.groups, function(){
			$('<input type="text" />').addClass('groupName').appendTo(dialog).val(this.name);
			dialog.append('<br />');
		});
		
		dialog.dialog('open');
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
				item.uiid = ++layout.ld.maxUIID;
			}
			list.push(item);
		}
		return list;
	},
	
	
	/**
	 * Opens dialog for assigned groups to branches in branching activity.
	 */
	openGroupsToBranchesMappingDialog : function(branchingActivity ) {
		var dialog = layout.items.groupsToBranchesMappingDialog,
			grouping = branchingActivity.grouping,
			groupsCell = $('.branchMappingFreeItemCell', dialog),
			branchesCell = $('.branchMappingFreeBranchCell', dialog),
			groupCell = $('.branchMappingBoundItemCell', dialog),
			branchCell = $('.branchMappingBoundBranchCell', dialog),
			branches = branchingActivity.branches;
		
		// remove existing entries and add reference to the initiating activity
		dialog.dialog('option', 'branchingActivity', branchingActivity);
		$('.branchMappingListCell', dialog).empty();
		
		// find group names and create DOM elements out of them
		grouping.groups = PropertyLib.fillNameAndUIIDList(grouping.groupCount,
				grouping.groups, 'name', 'Group ');
		branches = branchingActivity.branches = PropertyLib.fillNameAndUIIDList(branches.length,
				branches, 'title', 'Branch ');
		
		$.each(grouping.groups, function(){
			var group = this,
				groupElem = $('<div />').click(PropertyLib.selectBranchMappingListItem)
										.text(group.name).attr('uiid', group.uiid);
			
			// check if a group-branch mapping is already defined
			$.each(branchingActivity.groupsToBranches, function() {
				if (this.group == group && branches.indexOf(this.branch) != -1) {
					var branchElem = $('<div />').click(PropertyLib.selectBranchMappingListItem)
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
			$('<div />').click(PropertyLib.selectBranchMappingListItem).appendTo(branchesCell)
						.text(this.title).attr('uiid', this.uiid);
		});
		
		dialog.dialog('open');
	},
	
	
	/**
	 * Selects a list item and optionally the matched pair.
	 */
	selectBranchMappingListItem : function(){
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
	 * Make a pair out of selected group/condition and branch.
	 */
	addBranchMapping : function(dialog){
		var selectedItem = $('.branchMappingFreeItemCell .selected', dialog),
			selectedBranch =  $('.branchMappingFreeBranchCell .selected', dialog);
		
		if (selectedItem.length != 1 || selectedBranch.length != 1) {
			return;
		}
		
		// original branch stays in its list
		selectedBranch = selectedBranch.clone().click(PropertyLib.selectBranchMappingListItem);
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
	},
	
	
	/**
	 * Remove a mapping of group/condition and branch.
	 */
	removeBranchMapping : function(dialog) {
		var selectedItem = $('.branchMappingBoundItemCell .selected', dialog),
			selectedBranch =  $('.branchMappingBoundBranchCell .selected', dialog);
	
		if (selectedItem.length != 1 || selectedBranch.length != 1) {
			return;
		}
		
		selectedItem.removeData('boundItem');
		selectedBranch.remove();
		$('.branchMappingFreeItemCell', dialog).append(selectedItem);
	},

	
	openOutputConditionsDialog : function(activity){
		if (!activity || !activity.input) {
			return;
		}

		$('#outputConditionsDialog').dialog('option', 'parentObject', activity)
			  						.dialog('open');
	},
	
	
	/**
	 * Opens dialog for assigned conditions to branches in branching activity.
	 */
	openConditionsToBranchesMappingDialog : function(branchingActivity) {
		var dialog = layout.items.conditionsToBranchesMappingDialog,
			conditionsCell = $('.branchMappingFreeItemCell', dialog),
			branchesCell = $('.branchMappingFreeBranchCell', dialog),
			conditionCell = $('.branchMappingBoundItemCell', dialog),
			branchCell = $('.branchMappingBoundBranchCell', dialog),
			branches = branchingActivity.branches;
		
		// remove existing entries and add reference to the initiating activity
		dialog.dialog('option', 'branchingActivity', branchingActivity);
		$('.branchMappingListCell', dialog).empty();
		
		// find group names and create DOM elements out of them
		branches = branchingActivity.branches = PropertyLib.fillNameAndUIIDList(branches.length,
				branches, 'title', 'Branch ');
		
		$.each(branchingActivity.conditionsToBranches, function(){
			var entry = this,
				condition = entry.condition,
				conditionElem = $('<div />').click(PropertyLib.selectBranchMappingListItem)
										.text(condition.displayName).attr('uiid', entry.uiid);
			
			if (entry.branch && branchingActivity.branches.indexOf(entry.branch) != -1) {
				var branchElem = $('<div />').click(PropertyLib.selectBranchMappingListItem)
											 .appendTo(branchCell)
											 .text(entry.branch.title)
											 .attr('uiid', entry.branch.uiid)
											 .data('boundItem', conditionElem);
				conditionElem.appendTo(conditionCell).data('boundItem', branchElem);
			} else {
				conditionElem.appendTo(conditionsCell);
			}
		});
		
		$.each(branches, function(){
			$('<div />').click(PropertyLib.selectBranchMappingListItem).appendTo(branchesCell)
						.text(this.title).attr('uiid', this.uiid);
		});
		
		dialog.dialog('open');
	}
};