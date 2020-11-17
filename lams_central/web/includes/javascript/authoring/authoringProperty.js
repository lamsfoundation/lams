﻿﻿﻿/**
 * This file contains methods for Activity properties dialogs.
 */

/**
 * Stores different Activity properties structures.
 */
var PropertyDefs = {
		
	/**
	 * Properties dialog content for Branching activities.
	 */
	branchingProperties : function() {
		var activity = this,
			content = activity.propertiesContent,
			fillWidgetsFunction = function(){
				// fill widgets based on data stored in Activity object
				$('.propertiesContentFieldTitle', content).val(activity.branchingActivity.title);
				$('.propertiesContentFieldBranchingType', content).val(activity.branchingActivity.branchingType);
				// see what grouping and input Tools are available
				PropertyLib.fillGroupingDropdown(activity, activity.branchingActivity.grouping);
				PropertyLib.fillToolInputDropdown(activity, activity.branchingActivity.input);
				
				$('.propertiesContentFieldOptionalSequenceMin', content).attr('value',
																				 activity.branchingActivity.minOptions)
																		.attr('max',
																				 activity.branchingActivity.branches.length);
				$('.propertiesContentFieldOptionalSequenceMax', content).attr('value',
																				 activity.branchingActivity.maxOptions)
																		.attr({
																			'min' : activity.branchingActivity.minOptions,
																			'max' : activity.branchingActivity.branches.length
																		});
				if (activity.branchingActivity.branches.length == 0) {
					// no branches = no buttons to define and match groups/conditions to branches
					$('.propertiesContentRowConditions', content)
						.add($('.propertiesContentFieldMatchGroups', content).closest('tr'))
						.hide();
				}
			};
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentBranching').clone().attr('id', null)
													.show().data('parentObject', activity);
			// extra buttons for group/input based branching
			$('.propertiesContentFieldMatchGroups', content).button().click(function(){
				PropertyLib.openGroupsToBranchesMappingDialog(activity.branchingActivity);
			});
			$('.propertiesContentFieldCreateConditions', content).button().click(function(){
				PropertyLib.openOutputConditionsDialog(activity.branchingActivity);
			});
			$('.propertiesContentFieldMatchConditions', content).button().click(function(){
				PropertyLib.openConditionsToBranchesMappingDialog(activity.branchingActivity);
			});
			
			// if the user changed something in the dialog, save the state
			// make onChange function a local variable, because it's used several times
			var changeFunction = function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContents'),
					activity = content.data('parentObject'),
					branchingActivity = activity.branchingActivity,
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != branchingActivity.title) {
					if (GeneralLib.validateName(newTitle)) {
						branchingActivity.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(branchingActivity.title);
            		}
				}
				branchingActivity.branchingType = $('.propertiesContentFieldBranchingType', content).val();
				
				// show/hide group match button
				var groupingRow = $('.propertiesContentFieldGrouping', content).closest('tr');
				if (branchingActivity.branchingType == 'group') {
					branchingActivity.grouping = groupingRow.show()
												.find('option:selected').data('grouping');
				} else {
					branchingActivity.grouping = null;
					groupingRow.hide();
				}
				$('.propertiesContentFieldMatchGroups', content).closest('tr')
					.css('display', branchingActivity.branchingType == 'group' && branchingActivity.grouping
							&& branchingActivity.branches.length > 0 ? '' : 'none');
				
				// show/hide conditions define/match buttons
				var inputRow = $('.propertiesContentFieldInput', content).closest('tr'),
					inputDefinitionRows = $('.propertiesContentRowConditions', content);
				if (branchingActivity.branchingType == 'tool') {
					branchingActivity.input = inputRow.show()
						.find('option:selected').data('input');
					// no branches = no buttons
					if (branchingActivity.input && branchingActivity.branches.length > 0) {
						inputDefinitionRows.show();
					} else {
						inputDefinitionRows.hide();
					}
				} else {
					branchingActivity.orderedAsc = null;
					inputRow.hide();
					inputDefinitionRows.hide();
				}
				
				var optionalSequenceRows = $('.spinner', content).closest('tr');
				if (branchingActivity.branchingType == 'optional') {
					optionalSequenceRows.show();
				} else {
					optionalSequenceRows.hide();
				}

				// if title changed, redraw branching and converge points
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(layout.selectedObject);
					branchingActivity.start.draw();
					branchingActivity.end.draw();
					ActivityLib.addSelectEffect(layout.selectedObject, true);
				}
				
				GeneralLib.setModified(true);
			}
			
			// min can not be higher than max; neither of them can be higher than number of branches
			$('.propertiesContentFieldOptionalSequenceMin', content).attr('min', 0)
			  .on('input', function(){
				  var value = +$(this).val();
				  activity.branchingActivity.minOptions = Math.min(value, activity.branchingActivity.branches.length);
				  if (value != activity.branchingActivity.minOptions) {
					  $(this, content).attr('value', activity.branchingActivity.minOptions);
				  }
				  if (activity.branchingActivity.minOptions > activity.branchingActivity.maxOptions) {
					  $('.propertiesContentFieldOptionalSequenceMax', content).attr('value', value);
				  }
				  $('.propertiesContentFieldOptionalSequenceMax', content).attr('min', value);
			  });
			
			
			$('.propertiesContentFieldOptionalSequenceMax', content).attr('min', 0)
			  .on('input', function(){
				  var value = +$(this).val();
				  activity.branchingActivity.maxOptions = Math.min(value, activity.branchingActivity.branches.length);
				  if (value != activity.branchingActivity.maxOptions) {
					  $(this, content).attr('value', activity.branchingActivity.maxOptions);
				  }
			  });
			
			// set up the handler and run functions for the first time
			$('input, select', content).change(changeFunction);
			fillWidgetsFunction();
			changeFunction.call(content);
		}
		
		
		fillWidgetsFunction();
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
			if (activity.gateType == 'system') {
				// remove everything except for the title
				$('.propertiesContentFieldTitle', content).closest('tr').siblings().remove();
				return;
			}
			
			$('.propertiesContentFieldDescription', content).val(activity.description ? activity.description : '');
			$('.propertiesContentFieldGateType', content).val(activity.gateType);
			
			$('.propertiesContentFieldCreateConditions', content).button().click(function(){
				PropertyLib.openOutputConditionsDialog(activity);
			});
			$('.propertiesContentFieldMatchConditions', content).button().click(function(){
				PropertyLib.openConditionsToBranchesMappingDialog(activity);
			});
			
			// make onChange function a local variable, because it's used several times
			var changeFunction = function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContents'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != activity.title) {
					if (GeneralLib.validateName(newTitle)) {
						activity.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(activity.title);
            		}
				}
				
				// only Gate activity has description
				activity.description = $('.propertiesContentFieldDescription', content).val();
				
				activity.gateType = $('.propertiesContentFieldGateType', content).val();
				
				if (activity.gateType == 'schedule') {
					// show inputs for setting delay before the gate is closed
					$(".propertiesContentRowGateSchedule", content).show();
					activity.offsetDay = +$('.propertiesContentFieldOffsetDay', content).val();
					activity.offsetHour = +$('.propertiesContentFieldOffsetHour', content).val();
					activity.offsetMinute = +$('.propertiesContentFieldOffsetMinute', content).val();
					activity.gateActivityCompletionBased = $('.propertiesContentFieldActivityCompletionBased', content).is(':checked');
				} else {
					$(".propertiesContentRowGateSchedule", content).hide();
				}
				
				if (activity.gateType == 'password') {
					$(".propertiesContentRowGatePassword", content).show();
					activity.password = $('.propertiesContentFieldPassword', content).val();
				} else {
					$(".propertiesContentRowGatePassword", content).hide();
				}
				
				// Gate can be input-based
				var inputRow = $('.propertiesContentFieldInput', content).closest('tr'),
					inputDefinitionRows = $('.propertiesContentRowConditions', content);
				if (activity.gateType == 'condition') {
					activity.input = inputRow.show().find('option:selected').data('input');
					if (activity.input) {
						inputDefinitionRows.show();
					} else {
						inputDefinitionRows.hide();
					}
				} else {
					inputRow.hide();
					inputDefinitionRows.hide();
				}
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(activity);
					activity.draw();
					ActivityLib.addSelectEffect(activity, true);
				}
				
				GeneralLib.setModified(true);
			};
			
			// create groups/learners spinners
			$('.propertiesContentFieldOffsetDay', content).attr({
				'min' : 0,
				'max' : 364
			}).attr('value', activity.offsetDay || 0)
			  .on('input', changeFunction);
			
			$('.propertiesContentFieldOffsetHour', content).attr({
				'min' : 0,
				'max' : 23
			}).attr('value', activity.offsetHour || 0)
			  .on('input', changeFunction);
			
			$('.propertiesContentFieldOffsetMinute', content).attr({
				'min' : 0,
				'max' : 59
			}).attr('value', activity.offsetMinute || 0)
			  .on('input', changeFunction);
			
			$('.propertiesContentFieldActivityCompletionBased', content)
				.attr('checked', activity.gateActivityCompletionBased? 'checked' : null);
			
			$('.propertiesContentFieldPassword', content)
			  .val(activity.password);
			
			$('input, textarea, select', content).change(changeFunction);
			PropertyLib.fillToolInputDropdown(activity, activity.input);
			changeFunction.call(content);
		}
		
		if (activity.transitions.to.length == 0){
			$('.propertiesContentFieldActivityCompletionBased', content)
				.attr('checked', null)
				.prop('checked', false)
				.attr('disabled', 'disabled');

		} else {
			$('.propertiesContentFieldActivityCompletionBased', content)
				.attr('disabled', null);
		}
		
		PropertyLib.fillToolInputDropdown(activity, activity.input);
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
				var content = $(this).closest('.dialogContents'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val(),
					newGroupCount = +$('.propertiesContentFieldGroupCount', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != activity.title) {
					if (GeneralLib.validateName(newTitle)) {
						activity.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(activity.title);
            		}
				}
				
				activity.groupingType = $('.propertiesContentFieldGroupingType', content).val();
				
				$('input[name="propertiesContentFieldGroupDivide"]', content).each(function(){
						// enable/disable division types, depending on radio buttons next to them
						$(this).next().find('.spinner').prop('disabled', !$(this).is(':checked'));
					})
					// hide group/learner division with some grouping types
					.add($('.propertiesContentFieldLearnerCount', content).closest('tr'))
					.css('display', activity.groupingType == 'monitor' ? 'none' : '');
				
				// show "equal group sizes" only for Learner's Choice type and Number of Groups selected
				$('.propertiesContentFieldEqualSizes', content)
				   .closest('tr').css('display', activity.groupingType == 'learner' 
					   && $('.propertiesContentFieldGroupCountEnable', content).is(':checked') ? '' : 'none');
				
				// show "view learners before selection" only for Learner's Choice type
				$('.propertiesContentFieldViewLearners', content)
					.closest('tr').css('display', activity.groupingType == 'learner' ? '' : 'none');
				
				
				activity.groupDivide = activity.groupingType == 'monitor'
									   || $('.propertiesContentFieldGroupCountEnable', content).is(':checked')
									   ? 'groups' : 'learners';
				$('.propertiesContentFieldNameGroups', content).css('display', activity.groupDivide == 'groups' ? '' : 'none');		
				
				if (activity.groupCount != newGroupCount){
					activity.groupCount = newGroupCount;
					activity.groups = PropertyLib.fillNameAndUIIDList(activity.groupCount, activity.groups, 'name',
																	  LABELS.DEFAULT_GROUP_PREFIX);
				} 
				activity.learnerCount = +$('.propertiesContentFieldLearnerCount', content).val();
				activity.equalSizes = $('.propertiesContentFieldEqualSizes', content).is(':checked');
				activity.viewLearners = $('.propertiesContentFieldViewLearners', content).is(':checked');
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(activity);
					activity.draw();
					ActivityLib.addSelectEffect(activity, true);
				}
				
				GeneralLib.setModified(true);
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
			$('.propertiesContentFieldGroupCount', content).attr({
				'min' : 2
			}).attr('value', activity.groupCount)
			  .on('input', changeFunction);
			
			$('.propertiesContentFieldLearnerCount', content).attr({
				'min' 	   : 1
			}).attr('value', activity.learnerCount)
			  .on('input', changeFunction);
			
			$('.propertiesContentFieldEqualSizes', content).attr('checked', activity.equalSizes ? 'checked' : null);
			$('.propertiesContentFieldViewLearners', content).attr('checked', activity.viewLearners ? 'checked' : null);
			$('.propertiesContentFieldNameGroups', content).click(function(){
				PropertyLib.openGroupNamingDialog(activity);
			});
			
			$('input, select', content).change(changeFunction);
			changeFunction.call(content);
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
			var color = label.items.shape.attr('fill');
			// init colour chooser
			$('.propertiesContentFieldColor', content).val(color)
													  .simpleColor({
														'colors' : layout.colors.annotationPalette,
														'chooserCSS' : {
															'left'	   : 2,
															'top'      : '25px',
															'margin'   : '0'
														}
													  });
			
			var changeFunction = function(){
				// extract changed properties and redraw the label, if needed
				var content = $(this).closest('.dialogContents'),
					label = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val(),
					color = label.items.shape.attr('fill'),
					newColor = $('.propertiesContentFieldColor', content).val(),
					size =  label.items.shape.attr('font-size');
				size = +size.substring(0, size.indexOf('px'));

				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != label.title) {
					if (GeneralLib.validateName(newTitle)) {
						label.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(label.title);
            		}
				}
				
				redrawNeeded |= newColor != color;
				redrawNeeded |= label.newSize && (label.newSize <= layout.conf.labelMaxSize
										|| label.newSize >= layout.conf.labelMinSize);
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(label);
					label.draw(null, null, newColor, label.newSize);
					ActivityLib.addSelectEffect(label, true);
					GeneralLib.setModified(true);
				}
				
				label.newSize = null;
			};
			
			$('input', content).change(changeFunction);
			
			$('.labelPlusSize, .labelMinusSize', content).click(function(){
				label.newSize = label.items.shape.attr('font-size');
				label.newSize = +label.newSize.substring(0, label.newSize.indexOf('px'))
								+ ($(this).hasClass('labelPlusSize') ? 1 : -1);
				changeFunction.call(content);
			});
		}
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
				// extract changed properties and redraw the activity, if needed
				var content = $(this).closest('.dialogContents'),
					activity = content.data('parentObject'),
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != activity.title) {
					if (GeneralLib.validateName(newTitle)) {
            			activity.title = newTitle;
    					ActivityLib.removeSelectEffect(activity);
						activity.draw();
						ActivityLib.addSelectEffect(activity, true);
						GeneralLib.setModified(true);
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(activity.title);
            		}
				}
			});
			
			// min can not be higher than max; neither of them can be higher than children count
			$('.propertiesContentFieldOptionalActivityMin', content).attr({'min' : 0})
			  .on('input', function(){
				  var value = +$(this).val();
				  activity.minOptions = Math.min(value, activity.childActivities.length);
				  if (value != activity.minOptions) {
					  $(this, content).attr('value', activity.minOptions);
				  }
				  if (activity.minOptions > activity.maxOptions) {
					  $('.propertiesContentFieldOptionalActivityMax', content).attr('value', value);
				  }
				  $('.propertiesContentFieldOptionalActivityMax', content).attr('min', value);
			  });
			
			
			$('.propertiesContentFieldOptionalActivityMax', content).attr('min', 0)
			  .on('input', function(){
				  var value = +$(this).val();
				  activity.maxOptions = Math.min(value, activity.childActivities.length);
				  if (value != activity.maxOptions) {
					  $(this, content).attr('value', activity.maxOptions);
				  }
			  });
		}
		
		$('.propertiesContentFieldOptionalActivityMin', content).attr('value', activity.minOptions)
																.attr('max', activity.childActivities.length);
		$('.propertiesContentFieldOptionalActivityMax', content).attr('value', activity.maxOptions)
																.attr({
																	'min' : activity.minOptions,
																	'max' : activity.childActivities.length
																});
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
				var content = $(this).closest('.dialogContents'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != activity.title) {
					if (GeneralLib.validateName(newTitle)) {
						activity.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(activity.title);
            		}
				}
				var newGroupingValue = $('.propertiesContentFieldGrouping option:selected', content)
									.data('grouping');
				if (newGroupingValue != activity.grouping) {
					activity.grouping = newGroupingValue;
					$.each(activity.childActivities, function(){
						this.grouping = newGroupingValue;
					});
					redrawNeeded = true;
				}
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(activity);
					activity.draw();
					ActivityLib.addSelectEffect(activity, true);
					GeneralLib.setModified(true);
				}
			});
		}
		
		PropertyLib.fillGroupingDropdown(activity, activity.grouping);
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
			// init colour chooser
			$('.propertiesContentFieldColor', content).val(color)
													  .simpleColor({
														'colors' : layout.colors.annotationPalette,
														'chooserCSS' : {
															'left'	   : 2,
															'top'      : '25px',
															'margin'   : '0'
														}
													  });
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContents'),
					region = content.data('parentObject'),
					redrawNeeded = false,
					newTitle = $('.propertiesContentFieldTitle', content).val(),
					color = region.items.shape.attr('fill'),
					newColor = $('.propertiesContentFieldColor', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != region.title) {
					if (GeneralLib.validateName(newTitle)) {
						region.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(region.title);
            		}
				}
				redrawNeeded |= newColor != color;
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(region);
					region.draw(null, null, null, null, newColor);
					ActivityLib.addSelectEffect(region, true);
					GeneralLib.setModified(true);
				}
			});
		}
	},

	
	/**
	 * Properties dialog content for Tool activities.
	 */
	toolProperties : function() {
		var activity = this,
			content = activity.propertiesContent;
		
		if (!content) {
			// first run, create the content
			content = activity.propertiesContent = $('#propertiesContentTool').clone().attr('id', null)
													.show().data('parentObject', activity);
			$('.propertiesContentFieldTitle', content).val(activity.title);
			if (activity.parentActivity && (activity.parentActivity instanceof ActivityDefs.ParallelActivity)) {
				// parts of Parallel Activity are grouped as the parent activity
				$('.propertiesContentFieldGrouping', content).attr('disabled', 'disabled');
			}

			if (activity.outputDefinitions) {
				PropertyLib.fillOutputDefinitionsDropdown(activity);
			}
			
			$('input, select', content).change(function(){
				// extract changed properties and redraw the activity
				var content = $(this).closest('.dialogContents'),
					activity = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				if (newTitle == '') {
					newTitle = undefined;
				} 
				// validate and save the title
				if (newTitle != activity.title) {
					if (GeneralLib.validateName(newTitle)) {
						activity.title = newTitle;
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(activity.title);
            		}
				}
				
				var selectedGrouping = $('.propertiesContentFieldGrouping option:selected', content);
				if (selectedGrouping.length > 0){
					var newGroupingValue = selectedGrouping.data('grouping');
					if (newGroupingValue != activity.grouping) {
						activity.grouping = newGroupingValue;
						redrawNeeded = true;
						if (activity.requireGrouping && newGroupingValue != null) {
							// get rid of red border set by TBL check
							activity.requireGrouping = false;
						}
					}
				}
				
				var selectedGradebookToolOutputDefinition = $('.propertiesContentFieldGradebook option:selected', content);
				if (selectedGradebookToolOutputDefinition.length > 0){
					activity.gradebookToolOutputDefinitionName = selectedGradebookToolOutputDefinition.val();
					if (activity.gradebookToolOutputDefinitionName) {
						$.each(activity.outputDefinitions, function(){
							if (this.name == activity.gradebookToolOutputDefinitionName) {
								activity.gradebookToolOutputDefinitionDescription = this.description;
								activity.gradebookToolOutputDefinitionWeightable = this.weightable;
								return false;
							}
						});
					} else {
						activity.gradebookToolOutputDefinitionDescription = null;
						activity.gradebookToolOutputDefinitionWeightable = false;
						activity.gradebookToolOutputDefinitionWeight = null;
					}
				}
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(activity);
					activity.draw();
					ActivityLib.addSelectEffect(activity, true);
					GeneralLib.setModified(true);
				}
			});
		}
		
		PropertyLib.fillGroupingDropdown(activity, activity.grouping);
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
			
			var defaultBranch = $('.propertiesContentFieldDefault', content);
			if (transition.branch) {
				defaultBranch.show();
			} else {
				defaultBranch.hide();
			}
			
			$('input', content).change(function(){
				// extract changed properties and redraw the transition
				var content = $(this).closest('.dialogContents'),
					transition = content.data('parentObject'),
					redrawNeeded = false,
					newTitle =  $('.propertiesContentFieldTitle', content).val();
				
				// validate and save the title
				if (newTitle != transition.title) {
            		if (GeneralLib.validateName(newTitle)) {
						transition.title = newTitle;
						if (transition.branch) {
							transition.branch.title = newTitle;
						}
						redrawNeeded = true;
            		} else {
            			layout.infoDialog.data('show')(LABELS.TITLE_VALIDATION_ERROR);
            			$('.propertiesContentFieldTitle', content).val(transition.title);
            		}
				}
				
				if (transition.branch) {
					transition.branch.defaultBranch = $('.propertiesContentFieldDefault', content).is(':checked');
					if (transition.branch.defaultBranch) {
						// reset default branch marker in other branches, there can be only one
						$.each(transition.branch.branchingActivity.branches, function(){
							if (this != transition.branch) {
								this.defaultBranch = false;
							}
						});
					}
				}
				
				if (redrawNeeded) {
					ActivityLib.removeSelectEffect(activity);
					transition.draw();
					ActivityLib.addSelectEffect(activity, true);
					GeneralLib.setModified(true);
				}
			});
		}

		$('.propertiesContentFieldDefault', content).attr('checked',
				transition.branch && transition.branch.defaultBranch ? 'checked' : null);
	}
},



/**
 * Contains Properties dialog manipulation methods.
 */
PropertyLib = {
		
	init : function(){
		// initialise the properties dialog singleton
		var propertiesDialog = layout.propertiesDialog = showDialog('propertiesDialog',{
			'autoOpen' 		: false,
			'title'         : LABELS.PROPERTIES_DIALOG_TITLE,
			'width'			: 370,
			'close'			: null,
			'data'			: {
				'position'  : false
			}
		}, false);
		$('.modal-body', propertiesDialog).empty();
		// for proximity detection throttling (see handlers)
		propertiesDialog.data('lastRun', 0);
		// remove close button, add dimming
		propertiesDialog.addClass('propertiesDialogContainer smallHeader')
								  .css('opacity', layout.conf.propertiesDialogDimOpacity)
		 						  .mousemove(HandlerPropertyLib.approachPropertiesDialogHandler)
		                          .find('.modal-header button').remove();
		propertiesDialog.on('drag', function(event, ui){
			$(this).data('dragged', true);
		});
		layout.dialogs.push(propertiesDialog);
		
		var groupNamingContent = $('#propertiesContentGroupNaming');
		$('#groupNamingOKButton', groupNamingContent).click(function() {
    		var dialog = layout.groupNamingDialog,
				activity = groupNamingContent.data('parentObject'),
				names = [],
				error = null;
			
			// extract group names from text fields and validate them
			$('input', groupNamingContent).each(function(){
				var groupName = $(this).val().trim();
	    		if (GeneralLib.validateName(groupName)) {
	    			names.push(groupName);
	    		} else {
	    			error = LABELS.GROUP_TITLE_VALIDATION_ERORR;
	    			return false;
	    		}
			}); 
			
			if (error) {
				layout.infoDialog.data('show')(error);
				return;
			}
			
			$('input', groupNamingContent).each(function(index){
				activity.groups[index].name = names[index];
			});
			
			dialog.modal('hide');
		});
		$('#groupNamingCancelButton', groupNamingContent).click(function() {
			layout.groupNamingDialog.modal('hide');
		});
		// initialise dialog from group naming
		layout.groupNamingDialog = showDialog('groupNamingDialog',{
			'autoOpen' : false,
			'modal'  : true,
			'draggable' : true,
			'width'  : 400,
			'title'  : LABELS.GROUP_NAMING_DIALOG_TITLE,
			'close'  : null
		}, false)
		.addClass('smallHeader');
		
		
		$('.modal-body', layout.groupNamingDialog).empty().append(groupNamingContent.show());
		$('.modal-header button', layout.groupNamingDialog).remove();
		// add to dialogs array so they can be easily closed at once
		layout.dialogs.push(layout.groupNamingDialog);
		
		
		var branchMappingDialogContents = $('#branchMappingDialogContents').clone().attr('id', null);
		$('.branchMappingOKButton', branchMappingDialogContents).click(function(){
    		var dialog = layout.groupsToBranchesMappingDialog,
				branchingActivity = dialog.data('branchingActivity'),
				groupsToBranches = [],
				assignedToDefault = false,
				defaultBranch = null,
				close = true;
    		
			// find references to groups and branches
			$('.branchMappingBoundItemCell div, .branchMappingFreeItemCell div', dialog).each(function(){
				var groupUIID = +$(this).attr('uiid'),
					boundItem = $(this).data('boundItem'),
					branchUIID = boundItem ? +boundItem.attr('uiid') : null,
					group = null,
					branch = null;
				
				// look for branch
				if (branchUIID) {
	    			$.each(branchingActivity.branches, function(){
	    				if (branchUIID == this.uiid) {
	    					branch = this;
	    					return false;
	    				}
	    			});
				} else {
					// assign to default branch
					if (!defaultBranch) {
						$.each(branchingActivity.branches, function(){
							if (this.defaultBranch) {
								defaultBranch = this;
								return false;
							}
						});
						// fall back to first branch
						if (!defaultBranch) {
							defaultBranch = branchingActivity.branches[0];
							defaultBranch.defaultBranch = true;
						}
					}
					branch = defaultBranch;
					assignedToDefault = true;
				}
				
				// look for group
				$.each(branchingActivity.grouping.groups, function(){
					if (groupUIID == this.uiid) {
						group = this;
						return false;
					}
				});
				
				// add the mapping
				groupsToBranches.push({
					'uiid'   : ++layout.ld.maxUIID,
					'group'  : group,
					'branch' : branch
				});
			});
			
				if (assignedToDefault){
					// ask the user if it's OK to assign groups to default branch
					close = confirm(LABELS.GROUPS_TO_DEFAULT_BRANCH_CONFIRM);
				}
				
				if (close) {
					branchingActivity.groupsToBranches = groupsToBranches;
		    		dialog.modal('hide');
		    		GeneralLib.setModified(true);
				}
			});
		
		// initialise the dialog buttons
		$('.branchMappingAddButton', branchMappingDialogContents).click(function(){
			PropertyLib.addBranchMapping(layout.groupsToBranchesMappingDialog);
		});
		$('.branchMappingRemoveButton', branchMappingDialogContents).click(function(){
			PropertyLib.removeBranchMapping(layout.groupsToBranchesMappingDialog);
		});
		$('.branchMappingFreeItemHeaderCell', branchMappingDialogContents).text(LABELS.BRANCH_MAPPING_CONDITIONS_HEADER);
		$('.branchMappingBoundItemHeaderCell', branchMappingDialogContents).text(LABELS.BRANCH_MAPPING_CONDITION_HEADER);
			
		// initialise dialog for matching groups to branches in branching activities
		layout.groupsToBranchesMappingDialog = showDialog('gtbDialog',{
			'autoOpen' : false,
			'modal'  : true,
			'width'  : 800,
			'title'  : LABELS.GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE,
			'close'  : null
		}, false)
		.addClass('smallHeader');
		
		$('.modal-body', layout.groupsToBranchesMappingDialog).empty().append(branchMappingDialogContents.show());
		layout.dialogs.push(layout.groupsToBranchesMappingDialog);
		
		var outputConditionsDialogContents = $('#outputConditionsDialogContents');
		$('#outputConditionsClearAllButton', outputConditionsDialogContents).click(function(){
			// remove all range conditions
			var rows = $('#rangeConditions td', layout.outputConditionsDialog).closest('tr');
			rows.each(function(){
				// check for conditions already linked to branches/gate states
				if ($(this).data('mappingEntry').branch) {
					if (!confirm(LABELS.CLEAR_ALL_CONFIRM)) {
						rows = null;
					}
					return false;
				}
			});
			
			if (rows) {
				rows.remove();
			}
		});
		
		$('#outputConditionsRemoveButton', outputConditionsDialogContents).click(function(){
			// remove the selected range condition
			var selected = $('#rangeConditions tr.selected', layout.outputConditionsDialog);
			if (!selected.data('mappingEntry').branch
					|| confirm(LABELS.REMOVE_CONDITION_CONFIRM)) {
				selected.remove();
			}
		});
		
		$('#outputConditionsCancelButton', outputConditionsDialogContents).click(function(){
			layout.outputConditionsDialog.modal('hide');
		});
		
		$('#outputConditionsOKButton', outputConditionsDialogContents).click(function(){
			var dialog = layout.outputConditionsDialog,
				activity = dialog.data('parentObject');
			
			// extract created mappings from UI
			activity.conditionsToBranches = [];
			$('#rangeConditions tr, #complexConditions li', dialog).each(function(){
				// if it's hidden, then another output was selected, so skip it
				var mappingEntry = $(this).is(':visible') ? $(this).data('mappingEntry') : null;
				if (!mappingEntry) {
					return true;
				}
				
				// new UIID for new conditions
				if (!mappingEntry.uiid) {
					mappingEntry.uiid = ++layout.ld.maxUIID;
				}
				if (!mappingEntry.condition.conditionUIID) {
					mappingEntry.condition.conditionUIID = ++layout.ld.maxUIID;
				}
				
				// range conditions can have their names changed
				var input = $('input', this);
				if (input.length > 0) {
					mappingEntry.condition.displayName = input.val();
				}
				activity.conditionsToBranches.push(mappingEntry);
			});
									
			dialog.modal('hide');
			// go straight to mapping dialog
			PropertyLib.openConditionsToBranchesMappingDialog(activity);
		});
		
		// initialise dialog for defining input conditions
		layout.outputConditionsDialog = showDialog('outputConditionsDialog',{
			'autoOpen' : false,
			'modal'  : true,
			'width'  : 400,
			'height' : 400,
			'title'  : LABELS.CONDITIONS_DIALOG_TITLE,
			'open' : function(){
				 $(this).data('buildContent')();
			 },
			'close'  : null,
			'data' : {
				/**
				 * Link output data to UI widgets
				 */
				'buildContent' : function() {
					var dialog = layout.outputConditionsDialog,
						activity = dialog.data('parentObject'),
						outputSelect = $('#outputSelect', dialog),
						emptyOption = $('option[value="none"]', outputSelect).attr('selected', 'selected'),
						// conditions can have names like "output.name#6", but real output names do not have a suffix
						outputName = activity.conditionsToBranches && activity.conditionsToBranches.length > 0
								 	 ? activity.conditionsToBranches[0].condition.name.split('#')[0] : null;
					// remove all previously defined outputs
					$('option[value!="none"]', outputSelect).remove();
					
					if (activity.input.outputDefinitions) {
						// build output dropdown and bind data to each option
						$.each(activity.input.outputDefinitions,function(){
							var suffix = '';
							if (!this.showConditionNameOnly) {
								switch(this.type) {
									case 'OUTPUT_COMPLEX' :
										suffix = LABELS.COMPLEX_OUTPUT_SUFFIX;
										break;
															
									case 'OUTPUT_LONG' :
										suffix = LABELS.RANGE_OUTPUT_SUFFIX;
										break;
										
									case 'OUTPUT_BOOLEAN' :
										suffix = LABELS.BOOLEAN_OUTPUT_SUFFIX;
										break;
								};
							}
			
							this.toolActivityUIID = activity.input.uiid;
							var option = $('<option />')
										   .text(this.description + ' ' + suffix)
										   .data('output', this)
										   .appendTo(outputSelect);
							// select the output for which mappings were already defined
							if (this.name == outputName) {
								option.attr('selected', 'selected');
								emptyOption.attr('selected', null);
							}
						});
					}
					
					dialog.data('outputChange')();
				},
				
				
				/**
				 * Rebuild dialog content based on selected output
				 */
				'outputChange' : function(){
					var dialog = layout.outputConditionsDialog,
					 	activity = dialog.data('parentObject'),
					    outputOption = $('#outputSelect option:selected', dialog),
					    output = outputOption.data('output');
					
					
					$('.outputSelectDependent', dialog).hide();
					// no output = "none" option was selected
					if (!output) {
						return;
					}
					
					var complexConditionNames = $('#complexConditions', dialog),
						rangeConditionNames = $('#rangeConditions', dialog),
						complexOutputWidgets = $('.complexOutputButton', dialog).add(complexConditionNames),
						rangeOutputWidgets = $('#rangeOptionSelect, .rangeOutputButton', dialog).add(rangeConditionNames);
					if (output.showConditionNameOnly) {
						// complex, i.e. user defined conditions
						complexOutputWidgets.show();
						// build a list with immutable conditoon names
						var list =  $('ul', complexConditionNames),
							mappingEntries = {};
						
						// extract existing mappings from the list and remove the list
						$('li', list).each(function(){
							var mappingEntry = $(this).data('mappingEntry');
							mappingEntries[mappingEntry.condition.conditionID] = mappingEntry;
						}).remove();
						
						PropertyLib.validateConditionMappings(activity.input);
	
						if (output.conditions) {
							// build list using conditions from Tool activity output definitions
							$.each(output.conditions, function(){
								// use an existing mapping or build a new one
								var mappingEntry = this.conditionId ? (mappingEntries[this.conditionId] || {}) : {};
								
								mappingEntry.condition = {
									'name' 			  : this.name,
								    'displayName' 	  : this.displayName,
								    'type' 			  : this.type,
								    'conditionID'	  : this.conditionId,
								    'toolActivityUIID' : output.toolActivityUIID
								};
								
								$('<li />').text(this.displayName)
										   .appendTo(list)
								   		   .data('mappingEntry', mappingEntry);			   
							});
						}
					} else {
						// show menu and list of range conditions
						rangeOutputWidgets.show();
						layout.outputConditionsDialog.data('rangeOptionChange')();
						
						$('td',rangeConditionNames).closest('tr').remove();
						if (activity.conditionsToBranches) {
							$.each(activity.conditionsToBranches, function(){
								if (output.toolActivityUIID == this.condition.toolActivityUIID
										&& output.name == this.condition.name) {
									// set start and end values for easier validation of collapsing ranges
									if (this.condition.exactMatchValue) {
										this.condition.startValue = this.condition.endValue = this.condition.exactMatchValue;
									}
									layout.outputConditionsDialog.data('addRangeConditionRow')(this);
								}
							});
						}
					}
				},
				
				
				/**
				 * Show widgets appropriate for given range definition
				 */
				'rangeOptionChange' : function(){
					var dialog = layout.outputConditionsDialog,
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
				
				
				/**
				 * Fill table of existing range conditions
				 */
				'addRangeConditionRow' : function(mappingEntry){
					var condition = mappingEntry.condition;
					if (condition.type != 'OUTPUT_LONG') {
						return;
					}
					var rangeConditionNames = $('#outputConditionsDialog #rangeConditions'),
						conditionText = null;
					
					if (condition.exactMatchValue) {
						conditionText = LABELS.EXACT_CONDITION_DESCRIPTION + +condition.exactMatchValue;
					} else if (typeof condition.startValue == 'undefined') {
						conditionText = LABELS.LESS_CONDITION_DESCRIPTION + +condition.endValue;
					} else if (typeof condition.endValue == 'undefined') {
						conditionText = LABELS.GREATER_CONDITION_DESCRIPTION + +condition.startValue;
					} else {
						conditionText = LABELS.RANGE_CONDITION_DESCRIPTION.replace('[0]', +condition.startValue)
																		  .replace('[1]', +condition.endValue);
					}
					
					var row = $('<tr />').appendTo(rangeConditionNames).data('mappingEntry', mappingEntry).click(function(){
							// highlight the row
							$(this).addClass('selected').siblings('tr').removeClass('selected');
						}),
						nameCell = $('<td />').appendTo(row),
						conditionCell = $('<td />').text(conditionText).appendTo(row);
					$('<input type="text"/>').val(condition.displayName).appendTo(nameCell);
				}
			}
		}, false)
		.addClass('smallHeader');
		
		// assign handlers for events
		$('#outputSelect', outputConditionsDialogContents).change(function(){layout.outputConditionsDialog.data('outputChange')()});
		$('#rangeOptionSelect', outputConditionsDialogContents).change(layout.outputConditionsDialog.data('rangeOptionChange'));
		// add a new range condition
		$('#rangeAddButton', outputConditionsDialogContents).click(function(){
			var dialog = layout.outputConditionsDialog, 
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
					
					// validate the new condition so it does not overlap with an existing one
					if ((typeof condition.startValue == 'undefined' && existingCondition.startValue <= condition.endValue)
						|| (typeof condition.endValue == 'undefined'
							&& (typeof existingCondition.endValue == 'undefined' || existingCondition.endValue >= condition.startValue))
						|| (!(condition.startValue > existingCondition.endValue) && !(condition.endValue < existingCondition.startValue))) {
						layout.infoDialog.data('show')(LABELS.RANGE_CONDITION_ADD_START_ERROR);
						condition = null;
						return false;
					}
					
					if ((typeof condition.endValue == 'undefined' && existingCondition.endValue >= condition.startValue)
						|| (typeof condition.startValue == 'undefined'
							&& (typeof existingCondition.startValue == 'undefined' || existingCondition.startValue <= condition.endValue))
						|| (!(condition.endValue < existingCondition.startValue) && !(condition.startValue > existingCondition.endValue))) {
						layout.infoDialog.data('show')(LABELS.RANGE_CONDITION_ADD_END_ERROR);
						condition = null;
						return false;
					}
				});
				
				if (!condition){
					return;
				}

				// find an unique name for the new condition
				var nameIndex = 1;
				while (!condition.displayName) {
					condition.displayName = LABELS.DEFAULT_RANGE_CONDITION_TITLE_PREFIX + nameIndex;
					$('input', rangeConditionNames).each(function(){
						if (condition.displayName == $(this).val()) {
							condition.displayName = null;
							nameIndex++;
							return false;
						}
					});
				}
				
				dialog.data('addRangeConditionRow')({'condition' : condition});
		});
		
		// initialise spinner widgets
		$('#singleRangeSpinner, #multiRangeFromSpinner, #multiRangeToSpinner', outputConditionsDialogContents).attr({
			'min' : 0
		}).attr('value', 0);
		
		$('.modal-body', layout.outputConditionsDialog).empty().append(outputConditionsDialogContents.show());
		layout.dialogs.push(layout.outputConditionsDialog);
		
		
		branchMappingDialogContents = $('#branchMappingDialogContents').clone().attr('id', null);
		$('.branchMappingOKButton', branchMappingDialogContents).click(function(){
			layout.conditionsToBranchesMappingDialog.modal('hide');
		});
		// initialise the dialog buttons
		$('.branchMappingAddButton', branchMappingDialogContents).click(function(){
			PropertyLib.addBranchMapping(layout.conditionsToBranchesMappingDialog);
		});
		$('.branchMappingRemoveButton', branchMappingDialogContents).click(function(){
			PropertyLib.removeBranchMapping(layout.conditionsToBranchesMappingDialog);
		});
		$('.branchMappingFreeItemHeaderCell', branchMappingDialogContents).text(LABELS.BRANCH_MAPPING_CONDITIONS_HEADER);
		$('.branchMappingBoundItemHeaderCell', branchMappingDialogContents).text(LABELS.BRANCH_MAPPING_CONDITION_HEADER);
		// initialise dialog for matching conditions to branches/gate states in branching/gate activities
		layout.conditionsToBranchesMappingDialog = showDialog('ctbDialog',{
				'autoOpen' : false,
				'modal'  : true,
				'width'  : 800,
				'close'  : null,
				/**
				 * This event can be triggered either by "OK" button or cross in the dialog's upper right corner
				 */
				'beforeClose' : function() {
            		var dialog = layout.conditionsToBranchesMappingDialog,
	        			activity = dialog.data('activity'),
	        			mappingCopy = activity.conditionsToBranches.slice(),
	        			isGate = activity instanceof ActivityDefs.GateActivity,
	        			assignedToDefault = false,
	        			defaultBranch = isGate ? 'closed' : null,
	        			close = true;
            		
            		// see what was mapped
	    			$.each(mappingCopy, function(){
	    				var mappingEntry = this;
	    				mappingEntry.branch = null;
	            		// find references to conditions and branches
	            		$('.branchMappingBoundItemCell div', dialog).each(function(){
	            			if (mappingEntry.uiid != +$(this).attr('uiid')) {
	            				return true;
	            			}
	            			
	            			// same dialog works for both branching and gate activities, but mapping is done slightly different
	            			if (isGate) {
	            				mappingEntry.branch = $(this).data('boundItem').attr('gateState');
	            			} else {
	            				var branchUIID = +$(this).data('boundItem').attr('uiid');
		            			$.each(activity.branches, function(){
		            				if (branchUIID == this.uiid) {
		            					mappingEntry.branch = this;
		            					return false;
		            				}
		            			});
	            			}
	            			return false;
	            		});
	            		
	            		// a condition was not mapped, so map it to the default branch
	        			if (!mappingEntry.branch) {
	        				if (!defaultBranch) {
	        					$.each(activity.branches, function(){
	        						if (this.defaultBranch) {
	        							defaultBranch = this;
	        							return false;
	        						}
	        					});
	        					// fall back to first branch
	        					if (!defaultBranch) {
	        						defaultBranch = activity.branches[0];
	        						defaultBranch.defaultBranch = true;
	        					}
	        				}
	        				
	        				mappingEntry.branch = defaultBranch;
	        				assignedToDefault = true;
	        			}
	    			});
	    			
	        		// were any conditions assigned to the default branch?
	    			if (assignedToDefault) {
	    				close = confirm(isGate ? LABELS.CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM 
	    									   : LABELS.CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM);
	    			}
	    			
	    			if (close) {
	    				activity.conditionsToBranches = mappingCopy;
	    				activity.orderedAsc = $('#branchMappingOrderedRow', dialog).is(':visible') ? 
     						   				  $('#branchMappingOrderedAscCheckbox', dialog).prop('checked') : null;
	            		GeneralLib.setModified(true);
	    			}
	    			
	    			// if false, the dialog will not close
	    			return close;
				}
		}, false)
		.addClass('smallHeader');
		
		$('.modal-body', layout.conditionsToBranchesMappingDialog).empty().append(branchMappingDialogContents.show());
		layout.dialogs.push(ctbDialog);
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
	 * 	Find all groupings on canvas and fill dropdown menu with their titles
	 */
	fillGroupingDropdown : function(activity, grouping) {
		// find all groupings on canvas and fill dropdown menu with their titles
		var emptyOption = $('<option />').attr('selected', 'selected'),
			groupingDropdown = $('.propertiesContentFieldGrouping', activity.propertiesContent).empty().append(emptyOption),
			groupings = [];
		
		// every activity has access to any grouping
		// if it is correct, it will be validated before LD save
		$.each(layout.activities, function(){
			if (this instanceof ActivityDefs.GroupingActivity) {
				groupings.push(this);
			}
		});

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
	 * Creates group and branch names, if they are not already provided.
	 */
	fillNameAndUIIDList : function(size, existingList, nameAttr, prefix) {
		var list = [];
		for (var itemIndex = 1; itemIndex <= size; itemIndex++) {
			// generate a name and an UIID if they do not exist
			var item = itemIndex > existingList.length ? {} : existingList[itemIndex - 1];
			if (!item[nameAttr]) {
				item[nameAttr] = prefix + ' ' + itemIndex;
			}
			if (!item.uiid) {
				item.uiid = ++layout.ld.maxUIID;
			}
			list.push(item);
		}
		return list;
	},
	
	
	/**
	 * 	Find all activities that support outputs and fill dropdown menu with their titles
	 */
	fillToolInputDropdown : function(activity, input) {
		// find all tools that support input and fill dropdown menu with their titles
		var emptyOption = $('<option />'),
			inputDropdown = $('.propertiesContentFieldInput', activity.propertiesContent).empty().append(emptyOption),
			inputActivityDefs = [],
			candidate = activity.branchingActivity ? activity.branchingActivity.start : activity;

		do {
			if (candidate.transitions && candidate.transitions.to.length > 0) {
				candidate = candidate.transitions.to[0].fromActivity;
			} else if (candidate.branchingActivity && !candidate.isStart) {
				candidate = candidate.branchingActivity.start;
			}  else if (!candidate.branchingActivity && candidate.parentActivity) {
				candidate = candidate.parentActivity;
			} else {
				candidate = null;
			}
			
			if (candidate instanceof ActivityDefs.ToolActivity
					&& layout.toolMetadata[candidate.learningLibraryID].supportsOutputs) {
				inputActivityDefs.push(candidate);
			}
		} while (candidate != null);
		

		// fill dropdown menu
		$.each(inputActivityDefs, function(){
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
	 * Fill output definitions of given activity for Gradebook.
	 */
	fillOutputDefinitionsDropdown : function(activity) {
		// find all tools that support input and fill dropdown menu with their titles
		var emptyOption = $('<option/>').text(LABELS.GRADEBOOK_OUTPUT_NONE),
			gradebookDropdown = $('.propertiesContentFieldGradebook', activity.propertiesContent).empty().append(emptyOption);
		// build output dropdown and bind data to each option
		$.each(activity.outputDefinitions,function(){
			if (this.type != 'OUTPUT_LONG' && this.type != 'OUTPUT_BOOLEAN') {
				return true;
			}
			optionsFound = true;
			
			var suffix = '';
			if (!this.showConditionNameOnly) {
				switch(this.type) {
					case 'OUTPUT_BOOLEAN' :
						suffix = LABELS.BOOLEAN_OUTPUT_SUFFIX;
						break;
	                                                            
					case 'OUTPUT_LONG' :
						suffix = LABELS.RANGE_OUTPUT_SUFFIX;
						break;
				};
			}
			
			var option = $('<option />')
						   .text(this.description + ' ' + suffix)
						   .val(this.name)
						   .appendTo(gradebookDropdown);
			
			// select the default output
			if (activity.gradebookToolOutputDefinitionName == this.name) {
				option.attr('selected', 'selected');
				emptyOption.attr('selected', null);
			}
		});
		
		if (gradebookDropdown.children().length == 1) {
			// no valid ouput definitons, remove the whole row
			gradebookDropdown.parent().parent().remove();
		}
	},
	
		
	/**
	 * Opens dialog for assigned conditions to branches/gate states in branching/gate activity
	 */
	openConditionsToBranchesMappingDialog : function(activity) {
		var isGate = activity instanceof ActivityDefs.GateActivity, 
			dialog = layout.conditionsToBranchesMappingDialog,
			conditionsCell = $('.branchMappingFreeItemCell', dialog),
			branchesCell = $('.branchMappingFreeBranchCell', dialog),
			conditionCell = $('.branchMappingBoundItemCell', dialog),
			branchCell = $('.branchMappingBoundBranchCell', dialog),
			branches = null;
		
		// customise labels depending on the receiving activity
		$('.branchMappingFreeBranchHeaderCell', ctbDialog).text(isGate ? LABELS.BRANCH_MAPPING_GATE_HEADER
																	   : LABELS.BRANCH_MAPPING_BRANCHES_HEADER);
		$('.branchMappingBoundBranchHeaderCell', ctbDialog).text(isGate ? LABELS.BRANCH_MAPPING_GATE_HEADER
																		: LABELS.BRANCH_MAPPING_BRANCH_HEADER);
		
		// remove existing entries and add reference to the receiving activity
		dialog.data('activity', activity);
		$('.modal-title', dialog).text(isGate ? LABELS.GATE_STATE_MAPPING_DIALOG_TITLE : LABELS.BRANCH_MAPPING_DIALOG_TITLE);
		$('.branchMappingListCell', dialog).empty();
		
		if (isGate) {
			// define gate states as "branches" for easier use
			branches = ['open', 'closed'];
		} else {
			// find branch names and create DOM elements out of them
			branches = activity.branches = PropertyLib.fillNameAndUIIDList(activity.branches.length,
					activity.branches, 'title', LABELS.DEFAULT_BRANCH_PREFIX);
		}
		
		var orderedAsc = null;
		$.each(activity.conditionsToBranches, function(){
			// see what conditions are already mapped to branches/gate states and which are free
			var entry = this,
				condition = entry.condition,
				conditionElem = $('<div />').click(PropertyLib.selectBranchMappingListItem)
										.text(condition.displayName).attr('uiid', entry.uiid);
			// there are special conditions that tell that this is an ordered branching
			if (orderedAsc === null && condition.name.startsWith('ordered.answer')){
				orderedAsc = activity.orderedAsc === null ? true : activity.orderedAsc;
			}
			
			// is it mapped already?
			if (entry.branch && (isGate || activity.branches.indexOf(entry.branch) != -1)) {
				var branchElem = $('<div />').click(PropertyLib.selectBranchMappingListItem)
											 .appendTo(branchCell)
											 .text(isGate ? entry.branch : entry.branch.title)
											 .data('boundItem', conditionElem);
				if (isGate) {
					branchElem.attr('gateState', entry.branch);
				} else {
					branchElem.attr('uiid', entry.branch.uiid);
				}
				conditionElem.appendTo(conditionCell).data('boundItem', branchElem);
			} else {
				conditionElem.appendTo(conditionsCell);
			}
		});
		
		if (orderedAsc === null) {
			$('#branchMappingOrderedRow', dialog).hide();
		} else {
			$('#branchMappingOrderedRow', dialog).show().prop('checked', orderedAsc);
		}
		
		// find the default branch
		var defaultBranch = isGate ? 'closed' : null;
		if (!defaultBranch) {
			$.each(branches, function(){
				if (this.defaultBranch) {
					defaultBranch = this;
					return false;
				}
			});
			// fall back to first branch
			if (!defaultBranch) {
				defaultBranch = branches[0];
				defaultBranch.defaultBranch = true;
			}
		}
		
		// fill the branches list
		$.each(branches, function(){
			var branchTitle = (isGate ? (this == 'open' ? LABELS.GATE_STATE_OPEN : LABELS.GATE_STATE_CLOSED) : this.title)
							+ (this == defaultBranch ? ' ' + LABELS.BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX : ''),
				branchElem = $('<div />').click(PropertyLib.selectBranchMappingListItem).appendTo(branchesCell).text(branchTitle);
			if (isGate) {
				branchElem.attr('gateState', this);
			} else {
				branchElem.attr('uiid', this.uiid);
			}
		});
		
		dialog.modal('show');
	},
	
	
	/**
	 * Fills group naming dialog with existing group names and opens it.
	 */
	openGroupNamingDialog : function(activity) {
		var dialog = layout.groupNamingDialog,
			// add reference to the initiating activity
			content = $('.dialogContents', dialog).data('parentObject', activity),
			// remove existing entries
			groupsDiv = $('#groupNamingGroups', content).empty();
		$.each(activity.groups, function(){
			$('<input type="text" />').addClass('groupName').appendTo(groupsDiv).val(this.name);
		});
		
		dialog.modal('show');
	},
	
	
	openOutputConditionsDialog : function(activity){
		if (!activity || !activity.input) {
			return;
		}

		var conditionsLength = activity.input.outputDefinitions.length;
		ActivityLib.getOutputDefinitions(activity.input);
		if (conditionsLength != activity.input.outputDefinitions.length) {
			PropertyLib.fillOutputDefinitionsDropdown(activity.input);
		}
		layout.outputConditionsDialog.data('parentObject', activity)
			  						 .modal('show');
	},
	
	
	/**
	 * Opens properties dialog with the contents specific for each activity
	 */
	openPropertiesDialog : function(object) {
		object.loadPropertiesDialogContent();
		var dialog = layout.propertiesDialog,
			modalBody = $('.modal-body', dialog),
			modalContent = $('.modal-content', dialog);
		modalBody.children().detach();
		modalBody.append(object.propertiesContent);
		object.propertiesContent.show();
		modalBody.change(function(){
			dialog.height(modalContent.height() + 10);
		});
		
		if (object.readOnly) {
			// make all widgets read-only
			modalBody.find('input, select, textarea').prop('disabled', true);
			modalBody.find('.spinner').prop('disabled', true);
		}
		modalBody.find('input').blur();
		dialog.on('shown.bs.modal', function(){
			if (dialog.data('dragged')){
				// if user dragged the dialog at least once, it does not automatically change its position anymore
				return;
			}
			var box = object.items.getBBox(),
				canvasOffset = canvas.offset(),
				canvasWidth = canvas.width(),
				canvasHeight = canvas.height(),
				dialogWidth = dialog.width(),
				dialogHeight = dialog.height(),
				x = box.x2 + canvasOffset.left + 15,
				y = box.y + canvasOffset.top - dialogHeight;
	
			if (x + dialogWidth > canvasOffset.left + canvasWidth + 30) {
				// if dialog covers the activity (too close to right border),
				// move it to the other side
				x = box.x + canvasOffset.left - dialogWidth;
			}
			
			if (y < canvasOffset.top) {
				y = box.y + canvasOffset.top;
				var adjuster = 0;
				while (y > canvasOffset.top && y + dialogHeight > canvasOffset.top + canvasHeight){
					y -= adjuster++;
				};
			}		
			
			dialog.offset({
				'left' : x,
				'top'  : y
			});
		});

		if (dialog.css('display') == 'none') {
			dialog.modal('show');
		} else {
			dialog.trigger('shown.bs.modal');
		}
	},
	
	
	/**
	 * Opens dialog for assigned groups to branches in branching activity.
	 */
	openGroupsToBranchesMappingDialog : function(branchingActivity ) {
		var dialog = layout.groupsToBranchesMappingDialog,
			grouping = branchingActivity.grouping,
			groupsCell = $('.branchMappingFreeItemCell', dialog),
			branchesCell = $('.branchMappingFreeBranchCell', dialog),
			groupCell = $('.branchMappingBoundItemCell', dialog),
			branchCell = $('.branchMappingBoundBranchCell', dialog),
			branches = branchingActivity.branches;
		
		// remove existing entries and add reference to the initiating activity
		dialog.data('branchingActivity', branchingActivity);
		$('.branchMappingListCell', dialog).empty();
		
		// find group names and create DOM elements out of them
		grouping.groups = PropertyLib.fillNameAndUIIDList(grouping.groupCount,
				grouping.groups, 'name', LABELS.DEFAULT_GROUP_PREFIX);
		branches = branchingActivity.branches = PropertyLib.fillNameAndUIIDList(branches.length,
				branches, 'title', LABELS.DEFAULT_BRANCH_PREFIX);
		
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
		
		dialog.modal('show');
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
	
	/**
	 * Checks whether conditions changed in a tool activity.
	 * If so, it checks if it did not break existing mappings.
	 */
	validateConditionMappings : function(activity) {
		var conditionIDs = null,
			brokenActivities = [];
		
		// look for broken mappings
		$.each(layout.activities, function() {
			var consumer = this.branchingActivity || this;
			// check if the modified activity is an input for a gate or branching activity
			if (activity == consumer.input) {
				if (conditionIDs == null) {
					// refresh conditions in the modified activity
					conditionIDs = [];
					ActivityLib.refreshOutputConditions(activity);
					$.each(activity.outputDefinitions, function(){
						if (this.conditions) {
							$.each(this.conditions, function(){
								conditionIDs.push(this.conditionId);
							});
						}
					});
				}
				
				var isBroken = false,
					newMapping = [];
				$.each(consumer.conditionsToBranches, function(){
					// a mapped condition is now missing
					// remove it and inform the user
					if (this.condition.conditionID && conditionIDs.indexOf(this.condition.conditionID) == -1) {
						isBroken = true;
					} else {
						newMapping.push(this);
					}
				});
				if (isBroken) {
					consumer.conditionsToBranches = newMapping;
					brokenActivities.push(consumer);
				}
			}
		});
		
		// build a message
		if (brokenActivities.length > 0) {
			var message = LABELS.CONDITIONS_MAPPING_BROKEN_ERROR + ' ';
			$.each(brokenActivities, function(){
				message += (this.title ? '"' + this.title + '"' : LABELS.ACTIVITY_UNNAMED_DESCRIPTION) + ' ' +
						   (this instanceof ActivityDefs.GateActivity ?
								   LABELS.ACTIVITY_GATE_DESCRIPTION : LABELS.ACTIVITY_BRANCHING_DESCRIPTION)
						   + ', ';
			});
			message = message.substring(0, message.length - 2);
			layout.infoDialog.data('show')(message);
		}
	}
};