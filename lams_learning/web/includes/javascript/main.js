// ----- WINDOW MANIPULATION -----

function openPopUp(args, title, h, w, status) {
	window.open(args, title, "HEIGHT=" + h + ",WIDTH=" + w
			+ ",resizable=yes,scrollbars=yes,status="
			+ status + ",menubar=no, toolbar=no");
}

function exportPortfolio(){
	openPopUp(APP_URL + "exportWaitingPage.jsp?mode=learner&lessonID=" + lessonId,
			"ExportPortfolioLearner",
			410,640,
			"no");
}

function openActivity(url) {
	openPopUp(url,
			"LearnerActivity",
			600,800,
			"yes");
}

function loadFrame(url) {
	$('#contentFrame').attr('src', url);
}

function viewNotebookEntries(){
	openPopUp(APP_URL + "notebook.do?method=viewAll&lessonID=" + lessonId,
			"Notebook",
			570,796,
			"no");
}

function closeWindow() {
	// refresh the parent window
	if (parentURL != "") {
		if (window.parent.opener == null
				|| '${param.noopener}' == 'true'
				|| parentURL.indexOf('noopener=true') >= 0) {
			window.location.href = parentURL;
		} else {
			window.parent.opener.location.href = parentURL;
		}
		return true;
	}

	top.window.close();
}


function resizeElements() {
	var width = $(window).width() - 160;
	var height = $(window).height();
	$('#contentFrame').css({
		'width' : width + "px",
		'height' : height + "px",
		'position' : 'fixed'
	});

	if (progressPanelEnabled) {
		if (!controlFramePadding) {
			// calculate only once
			// there can be miscalculations when trying to repeat this in the middle of resizing
			 controlFramePadding = $('#controlFrame').outerHeight(true) - $('#controlFrame').height();
		}
		
		// calculate immutable chunks and what is left goes for progress bar
		var progressBarHeight = height - controlFramePadding;
		$('.progressStaticHeight').each(function(){
			var elem = $(this);
			if (elem.is(':visible')) {
				progressBarHeight -= elem.outerHeight(true);
			}
		});
		
		$('#progressBarDiv').height(progressBarHeight);
	}
	
	if (presenceEnabled){
		resizeChat();
	}
}

// open/close Notebook or Support frames
function toggleBarPart(name) {
	var part = $('#' + name + 'Part');
	var togglerCell = $('#' + name + 'TogglerCell');
	if (part.is(':visible')) {
		part.hide();
		togglerCell.html('▲');
	} else {
		part.show();
		togglerCell.html('▼');
	}
	resizeElements();
}

function validateNotebookForm(fields) {
	var formFilled = false;
	$.each(fields,function(index, field){
		if ((field.name == 'title' || field.name == 'entry') 
				&& field.value) {
			formFilled = true;
			return false;
		}
	});
	return formFilled;
}

// double click triggers also single click event, this method prevents it
function handleClicks(elem, click, dblclick) {
	if (click) {
		elem.click(function(e) {
		    setTimeout(function() {
		        if (elem.clickcounter) {
		        	elem.clickcounter--;
		        } else {
		            click.call();
		        }
		    }, 300);
		});
	}
	if (dblclick) {
		elem.dblclick(function() {
			elem.clickcounter = 2;
			dblclick.call();
		});
	}
}

//------------- RAPHAEL --------------
var PATH_SQUARE = " v16 h16 v-16 z";
var PATH_BIG_SQUARE = " v26 h26 v-26 z";
var PATH_CIRCLE = " m -8 0 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0";
var	PATH_TRIANGLE = " l8 16 l8 -16 z";
var	PATH_OCTAGON = " l-7 7 v12 l7 7 h12 l7 -7 v-12 l-7 -7 z";

// dark red
var COLOR_CURRENT_ACTIVITY = "rgb(187,0,0)";
// dark blue
var COLOR_COMPLETED_ACTIVITY = "rgb(0,0,153)";
// green
var	COLOR_TOSTART_ACTIVITY = "rgb(0,153,0)";
// black
var COLOR_STROKE_ACTIVITY = "rgb(0,0,0)";
// red
var	COLOR_GATE = "rgb(255,0,0)";
// white
var	COLOR_GATE_TEXT = "rgb(255,255,255)";
// gray
var COLOR_COMPLEX_BACKGROUND = "rgb(153,153,153)";

var paper = null;
var controlFramePadding = null;
var currentActivityId = null;
var isPreview = false;
var activities = [];

// This should be the super class for Activities, but it's hard to accomplish in JS
// It is a set of common methods instead.
var ActivityUtils = {
		// shape* methods are just preparing data, there is no actual drawing yet
		shapeByStatus : function(activity) {
			if (activity.status == 0) {
				ActivityUtils.shapeCurrentActivity(activity);
			} else if (activity.status == 1) {
				ActivityUtils.shapeCompletedActivity(activity);
			} else if (activity.status == 2) {
				ActivityUtils.shapeAttemptedActivity(activity);
			} else if (activity.status == 3) {
				ActivityUtils.shapeToStartActivity(activity);
			}
		},
		
		shapeCurrentActivity : function(activity) {
			// dark red square
			activity.path = "M" + (activity.middle - 8) + " " + activity.y + PATH_SQUARE;
			activity.fill = COLOR_CURRENT_ACTIVITY;
			activity.stroke = COLOR_STROKE_ACTIVITY;
			activity.statusTooltip = LABEL_CURRENT_ACTIVITY;
		},
		
		shapeCompletedActivity : function(activity) {
			// dark blue circle
			activity.path = "M" + activity.middle + " " + (activity.y + 8) + PATH_CIRCLE;
			activity.fill = COLOR_COMPLETED_ACTIVITY;
			activity.stroke = COLOR_STROKE_ACTIVITY;
			activity.statusTooltip = LABEL_COMPLETED_ACTIVITY;
		},
		
		shapeAttemptedActivity : function(activity) {
			// green square with dark red arc
			activity.path =  "M" + (activity.middle - 8) + " "  + activity.y + PATH_SQUARE;
			activity.fill = COLOR_TOSTART_ACTIVITY;
			activity.stroke = COLOR_STROKE_ACTIVITY;
			activity.statusTooltip = LABEL_ATTEMPTED_ACTIVITY;
			
			// this and similar methods are run when activity shape is drawn for real
			activity.addDecoration = function(act) {
				act.decoration = act.paper.set();
				// get exact Y where square was drawn
				// it is different than activity.y in OptionalActivity because of gray square behind it
				var y = act.shape.attr('path')[0][2];
				var arc = act.paper.path("M" + (act.middle - 8) + " " + y + " a16 16 0 0 0 16 16 v-16 z");
				arc.attr({
					'fill'    : COLOR_CURRENT_ACTIVITY,
					'opacity' : 0,
					'cursor'  : 'pointer'
				 });
				act.decoration.push(arc);
			}
		},
		
		shapeToStartActivity : function(activity) {
			// green triangle
			activity.path = "M" + (activity.middle - 8) + " " + activity.y + PATH_TRIANGLE;
			activity.fill = COLOR_TOSTART_ACTIVITY;
			activity.stroke = COLOR_STROKE_ACTIVITY;
			activity.statusTooltip = LABEL_TOSTART_ACTIVITY;
		},
		
		shapeGateActivity : function(activity) {
			// red octagon
			activity.path = "M" + (activity.middle - 6) + " " + activity.y + PATH_OCTAGON;
			activity.fill = COLOR_GATE;
			
			activity.addDecoration = function(act) {
				act.decoration = activity.paper.set();
				
				var text = act.paper.text(act.middle, act.y + 13, "STOP");
				text.attr({
					'opacity'   : 0,
					'font-size' : 9,
					'font'      : 'sans-serif',
					'stroke'    : COLOR_GATE_TEXT,
					'cursor'    : 'pointer'
				 });
				act.decoration.push(text);
				
				if (act.status == 0) {
					// dark red edge when current activity
					act.statusTooltip = LABEL_CURRENT_ACTIVITY;
					
					var edge = act.paper.path(act.path);
					edge.attr({
						'opacity'      : 0,
						'stroke'       : COLOR_CURRENT_ACTIVITY,
						'stroke-width' : 3,
						'cursor'       : 'pointer'
					});
					act.decoration.push(edge);
				} else {
					act.statusTooltip = LABEL_TOSTART_ACTIVITY;
				}
			}
		},
		
		shapeComplexActivityContainer : function(activity) {
			var addDecoration = activity.addDecoration;
			activity.addDecoration = function(act) {
				// run previous addDecoration(), for example defined in Attempted Activity
				if (addDecoration) {
					addDecoration(act);
				}
				
				// gray squar in background
				var square = act.paper.path("M" + (act.middle - 13) + " " + act.y + PATH_BIG_SQUARE);
				square.attr({
					'opacity'      : 0,
					'fill'       : COLOR_COMPLEX_BACKGROUND,
					'cursor'       : 'pointer'
				});
				
				// so it goes behind, not to front like other decoration
				act.decorationWraps = true;
				square.decorationWraps = true;
				
				if (!act.decoration) {
					act.decoration = act.paper.set();
				}
				act.decoration.push(square);
			}
		},
		
		// return some attributes in Raphael consumable way
		getShapeAttributes : function(activity) {
			return {
				'path'         : activity.path,
				'fill'         : activity.fill,
				'stroke' 	   : activity.stroke,
				'cursor'       : 'pointer'
			}
		},
		
		drawActivity : function(activity, quick, isLast) {
			activities[activity.index] = activity;
			activity.elements = activity.paper.set();
			// only now do the read drawing, add event handlers etc.
			activity.shape = activity.paper.path(activity.path);
			activity.shape.attr(ActivityUtils.getShapeAttributes(activity));
			activity.elements.push(activity.shape);
			// label underneath the shape
			var label = paper.text(activity.middle, 43 + 60 * (activity.index - 1) + activity.height,
					               activity.name);
			activity.elements.push(label);
			if (!isLast) {
				// line between activities
				var line = paper.path("M " + activity.middle + " " + (50 + 60 * (activity.index - 1) + activity.height)
						   + " v" + (90 - activity.height));
				activity.elements.push(line);
			}
			
			if (!quick) {
				activity.elements.forEach(function(elem) {
					elem.attr('opacity', 0);
					elem.animate({'opacity' : 1}, 1000, "linear");
				});
			}
			
			ActivityUtils.addDecoration(activity, null, quick);
			ActivityUtils.addEffects(activity);
		},
		

		// adds handlers to activity for mouse interactions
		// long method with simple actions
		addEffects : function(activity) {
			// remove any existing handlers
			ActivityUtils.removeHover(activity.shape);
			if (activity.shape.events) {
		        while (activity.shape.events.length){
		        	activity.shape.events.pop().unbind();
		        }
			}
			
			var mouseover = function(e, x, y) {
				// add glowing effect on hover
				if (activity.decorationWraps) {
					activity.decoration.forEach(function(elem){
						if (elem.decorationWraps) {
							// glow the wrapping decoration element
							// for example gray square in Optonal Activity container
							// bigger than inner activity shape
							activity.shape.glowRef = elem.glow({
								color : elem.attr('fill')
							});
							return false;
						}
					});
				} else {
					activity.shape.glowRef = activity.shape.glow({
						color : activity.shape.attr('fill')
					});
				}
				
				// add tooltip
				var tooltipText = '<b>' + activity.name + '</b><br />' + activity.statusTooltip;
				// move to proper place and show
			    tooltipDiv.stop(true, true).css("left", 30).css("top", y + 20)
			    		  .html(tooltipText)
			    		  .delay(1000).fadeIn();
			}

			var mouseout = function() {
				ActivityUtils.removeHover(activity.shape);
			}
			
			var isSupportActivity = activity instanceof SupportActivity;
			var dblclick = activity.url ? function(){
				// open pop up if it is a support or completed activity
				if (isSupportActivity || activity.status == 1) {
					openActivity(activity.url);
					
					if (isSupportActivity) {
						activity.transformToAttempted();
					}
				} else {
					loadFrame(activity.url);
				}
			} : null;
			
			
			var click = activity.isComplex ? function() {
				ActivityUtils.showComplexContent(activity);
			} : null;
			
			activity.shape.hover(mouseover, mouseout);
			handleClicks(activity.shape, click, dblclick);
			if (activity.decoration) {
				activity.decoration.forEach(function(elem){
					elem.hover(mouseover, mouseout);
					handleClicks(elem, click, dblclick);
				});
			}
		},
		
		removeHover : function(shape){
			if (shape.glowRef) {
				shape.glowRef.remove();
				shape.glowRef = null;
			}
			tooltipDiv.stop(true, true).fadeOut();
		},
		
		// copy important properties and morph visible elements
		transform : function(sourceActivity, targetActivity) {
			var gotCompleted = false;
			
			if (sourceActivity.status != targetActivity.status) {
				gotCompleted = targetActivity.status == 1;
				
				sourceActivity.height = targetActivity.height;
				sourceActivity.path = targetActivity.path;
				sourceActivity.fill = targetActivity.fill;
				sourceActivity.stroke = targetActivity.stroke;
				sourceActivity.url = targetActivity.url;
				sourceActivity.status = targetActivity.status;
				sourceActivity.statusTooltip = targetActivity.statusTooltip;
				sourceActivity.addDecoration = targetActivity.addDecoration;
				
				ActivityUtils.animate(sourceActivity, sourceActivity.decoration);
			}
			
			var isCurrent = targetActivity.status == 0;
			if (sourceActivity.childActivities) {	
				$.each(sourceActivity.childActivities, function(childActivityIndex, childActivity){
					var targetChildActivity = targetActivity.childActivities[childActivityIndex];
					isCurrent |= targetChildActivity.status == 0;
					ActivityUtils.transform(childActivity, targetChildActivity);
				});
			}
			
			if (isCurrent) {
				ActivityUtils.showComplexContent(sourceActivity);
				 if (sourceActivity.toggleChildren) {
					// complex sequence just became current, show it
					sourceActivity.toggleChildren('open');
				 }
			} else if (gotCompleted) {
				if (sourceActivity.isComplex) {
					ActivityUtils.hideOtherComplexContent();
				} else if (sourceActivity.toggleChildren) {
					sourceActivity.toggleChildren('close');
				}
			}
		},
		
		animate : function(activity, oldDecoration){
			if (activity.shape) {
				ActivityUtils.addDecoration(activity, oldDecoration, false);
				activity.shape.animate(ActivityUtils.getShapeAttributes(activity), 2000, "linear", function() {
					if(!(activity instanceof OptionalActivity)){
						ActivityUtils.addEffects(activity);
					}
				});
			}
		},
		
		// quick is for inital drawing, no nice effect is needed
		addDecoration : function(activity, oldDecoration, quick){
			if (oldDecoration) {
				oldDecoration.forEach(function(elem){
					if (activity.elements) {
						activity.elements.exclude(elem);
					}
					elem.animate({'opacity' : 0}, quick ? 0 : 1000, "linear", function(){
						elem.remove();
					});
				});
			}
			
			if (activity.addDecoration) {
				var animation = Raphael.animation({'opacity' : 1}, quick ? 0 : 1000, "linear");
				
				activity.addDecoration(activity);
				activity.decoration.forEach(function(elem){
					if (activity.elements) {
						activity.elements.push(elem);
					}
					if (elem.decorationWraps) {
						// decoration element is bigger that activity shape, put it in background
						elem.toBack();
					} else {
						elem.toFront();
					}
					
					elem.animate(animation.delay(oldDecoration ? 1000 : undefined));
				});
			}
		},
		
		// hide all Optional Activities, except for the given one
		hideOtherComplexContent : function(currentOptionalContentId) {
			$('div.optionalActivity').each(function(index, contentDiv){
				var content = $(contentDiv);
				if (content.attr('id') != currentOptionalContentId) {
					content.slideUp(currentOptionalContentId ? 'fast' : 'slow');
				}
			});
		},
		
		// draw pop up with optional activities
		showComplexContent : function(activity) {
			if (activity.isComplex) {
				// hide glow if shown (IE)
				ActivityUtils.removeHover(activity.shape);
				ActivityUtils.hideOtherComplexContent
					(activity.optionalContent ? activity.optionalContent.attr('id') : null);
						
				if (!activity.optionalContent) {
					var containerName = 'optionalActivityContent' + activity.y;
					activity.optionalContent = $('<div />')
											  .attr('id', containerName)
											  .addClass('optionalActivity')
											  .css({
												  // a little higher than activity, to cover it
												  'top'    : $(activity.shape.node).offset().top - 8,
												  'height' : 27 * activity.childActivities.length - 1
												  })
											  .appendTo('#progressBarDiv');
					
					var optionalContentTable = $('<table cellspacing="0" />').appendTo(activity.optionalContent);
					
					ActivityUtils.addChildActivitiesRows(activity, optionalContentTable, activity.optionalContent, false);
				}
				
				activity.optionalContent.slideDown('slow');
			}
		},
		
		addChildActivitiesRows : function(activity, parent, container, isNested) {
			var isCurrent = false;
			
			$.each(activity.childActivities, function(childActivityIndex, childActivity) {
				var row = $('<tr />');
				var parentId = null;
				if (isNested) {
					// second tier, a part of optional sequence
					parentId = $('td', parent).attr('id');
					// find last activity from sequence and put current after it to keep ordering
					row.insertAfter($('td[id^=' + parentId + ']', container).last().parent());
					isCurrent |= childActivity.status == 0;
				} else {
					parentId = container.attr('id');
					row.appendTo(parent);
				}
				
				var cellId = parentId + 'child' + childActivityIndex;
				var cell = $('<td />').attr('id', cellId).appendTo(row);
				if (isNested) {
					cell.hide();
				}
				
				childActivity.paper = Raphael(cellId, 145, 23);
				childActivity.shape = childActivity.paper.path(childActivity.path);
				childActivity.shape.attr(ActivityUtils.getShapeAttributes(childActivity));
				ActivityUtils.addDecoration(childActivity, null, true);
				var label =	childActivity.paper.text(35,
			                childActivity.y + 11,
			                (isNested ? '- ' : '') + childActivity.name)
			                .attr('text-anchor', 'start');
				$('tspan', label.node).attr('dy', 0);
				
				var click = null;
				if (!isNested) {
					if (childActivityIndex == 0) {
						click = function(){
							container.slideUp();
						}
					} else if (childActivity.childActivities){
						childActivity.toggleChildren = function(forceCommand){
							if (cell.is(':visible')) {
								// show/hide optional sequence content
								var childCells = $('td[id^=' + cellId + 'child]', parent);
								var isOpen = childCells.is(':visible');
								if (!forceCommand || (isOpen ? forceCommand == 'close' : forceCommand == 'open')) {
									var containerHeightDelta = 27 * childCells.length;
									childCells.toggle();
									container.height(container.height() + 
											        (isOpen ? -containerHeightDelta : containerHeightDelta));
								}
							}
						}
						
						click = function(){
							childActivity.toggleChildren();
						}
					} 
				}
				var dblclick = function() {
					if (childActivity.url) {
						if (childActivity.status == 1) {
							openActivity(childActivity.url);
						} else {
							loadFrame(childActivity.url);
						}
					}
				}
				handleClicks(cell, click, dblclick);

				if (childActivity.childActivities) {
					isCurrent |= ActivityUtils.addChildActivitiesRows(childActivity, row, container, true);
					if (isCurrent && childActivity.toggleChildren) {
						childActivity.toggleChildren('open');
					}
				}
			});
			
			return isCurrent;
		},
		
		// replace single Branching activity with list of branch activities
		expandBranch : function(branchIndex, branchActivities) {
			// hide any boxes obstructing the view
			ActivityUtils.hideOtherComplexContent();
			
			var activityShift = branchActivities.length - 1;
			var yShift = 60*activityShift;
			var afterBranchActivity = null;
			// move down existing activities that come after Branching
			for (var activityIndex = activities.length - 1; activityIndex > branchIndex; activityIndex--) {
				afterBranchActivity = activities[activityIndex];
				activities[activityIndex + activityShift] = afterBranchActivity;
				afterBranchActivity.y += yShift;
				afterBranchActivity.path = Raphael.transformPath(afterBranchActivity.path, 'T0,' + yShift);
				afterBranchActivity.elements.forEach(function(elem){
					var y = elem.attr('y');	
					var targetProperties = null;
					// text, rectangles etc. have 'y', paths have 'path'
					if (y) {
						targetProperties = {'y' : elem.attr('y') + yShift};
					} else {
						var path = elem.attr('path');
						targetProperties = {'path' : Raphael.transformPath(path, 'T0,' + yShift)};	
					}
					elem.animate(targetProperties, 2000, "linear");
				});
			}
			
			// remove Branching activity
			activities[branchIndex].elements.forEach(function(elem) {
				elem.animate({'opacity' : 0} , 2000, "linear" , function() {
					elem.remove();
				});
			});
			
			// create branch activities structure
			for (var activityIndex = 0; activityIndex < branchActivities.length; activityIndex ++){
				var activityData = branchActivities[activityIndex];
				var activity = new Activity(paper, activityIndex + branchIndex, activityData.id,
										    activityData.type, activityData.name,
										    activityData.status, activityData.url,
										    activityData.childActivities);
				activities[activityIndex + branchIndex] = activity;
				if (activity.status == 0) {
					currentActivityIndex = activityIndex;
				}
			}
			
			// draw branch activities
			setTimeout(function(){
				for (var activityIndex = 0; activityIndex < branchActivities.length; activityIndex ++){			
					ActivityUtils.drawActivity(activities[activityIndex + branchIndex], false,
							!afterBranchActivity && activityIndex == branchActivities.length - 1);
				}
			}, 2000);
		}
}

// main activities
function Activity(paper, index, id, type, name, status, url, childActivitiesData) {
	this.paper = paper;
	this.index = index;
	this.id = id;
	this.type = type;
	this.name = name;
	this.status = status;
	this.url = url;
	
	// Optional Activities, Optional Sequences or Branching in preview mode
	this.isComplex = type == 'o' || (isPreview && type == 'b');
	this.middle = 70;
	this.height = 60;
	// 20 is the first line segment and following activities take 60 px each
	this.y = 20 + this.height * index;
	
	// first draw the inner shape, then put back the realY for background gray square
	var finalY = this.y;
	if (this.isComplex) {
		this.y += 5;
	}
	
	if (type == 'g') {
		this.height = 70;
		ActivityUtils.shapeGateActivity(this);
	} else {
		ActivityUtils.shapeByStatus(this);
	}
	
	
	// special behaviour for optional activities
	if (this.isComplex) {
		this.height = 70;
		this.y = finalY;
		ActivityUtils.shapeComplexActivityContainer(this);
		
		this.childActivities = [new OptionalActivity(name, status, url)];
		var childActivities = this.childActivities;
		$.each(childActivitiesData, function(childActivityIndex, childActivityData) {
			childActivities[childActivityIndex + 1] = new OptionalActivity(childActivityData.name, 
				    childActivityData.status, childActivityData.url,
				    childActivityData.childActivities, false);
		});
	}
}

function SupportActivity(paper, index, name, status, url) {
	this.paper = paper;
	this.name = name;
	this.status = status;
	this.url = url;
	
	this.middle = 24;
	this.y = 17 + 33*index;
	
	if (status <= 2) {
		ActivityUtils.shapeAttemptedActivity(this);
	} else if (status == 3) {
		ActivityUtils.shapeToStartActivity(this);
	}
	this.statusTooltip = LABEL_SUPPORT_ACTIVITY;
	
	this.transformToAttempted = function(){
		var oldDecoration = this.decoration;
		ActivityUtils.shapeAttemptedActivity(this);
		this.statusTooltip = LABEL_SUPPORT_ACTIVITY;
		
		ActivityUtils.animate(this, oldDecoration);
	}
}

function OptionalActivity(name, status, url, childActivitiesData, isNested) {
	this.name = name;
	this.status = status;
	this.url = url;
	
	this.middle = isNested ? 22 : 15;
	this.y = 5;
	
	ActivityUtils.shapeByStatus(this);
	
	if (childActivitiesData) {
		this.childActivities = [];
		var childActivities = this.childActivities;
		$.each(childActivitiesData, function(childActivityIndex, childActivityData) {
			childActivities[childActivityIndex] = new OptionalActivity(childActivityData.name, 
				    childActivityData.status, childActivityData.url, null, true);
		});
	}
}


// refresh progress bar on first/next activity load
function fillProgressBar() {
	$.ajax({
		url : APP_URL + 'learner.do',
		data : {
				'method'   : 'displayProgressJSON',
				'lessonID' : lessonId
				},
		cache : false,
		dataType : 'json',
		success : function(result) {
			// if nothing changed, don't do any calculations
			if (!currentActivityId || result.currentActivityId != currentActivityId) {
				currentActivityId = result.currentActivityId;
				isPreview = result.isPreview;
				
				if (!paper) {
					// create paper only the first time
					paper = Raphael('progressBarDiv', 140, 60*result.activities.length);
					// first line on the top
					paper.path("M70 0 v20");
				}
				
				// we need this to scroll to the current activity
				var currentActivityIndex = 0;
				
				
				for (var activityIndex = 0; activityIndex < result.activities.length; activityIndex++) {
					var activityData = result.activities[activityIndex];
					var activity = new Activity(paper, activityIndex, activityData.id,
											    activityData.type, activityData.name,
											    activityData.status, activityData.url,
											    activityData.childActivities);
					if (activity.status == 0) {
						currentActivityIndex = activityIndex;
					}
					
					var existingActivity = activities[activityIndex];
					if (existingActivity) {
						// if in preview mode, always display all options, i.e. never expand
						if (!isPreview && existingActivity.type == 'b' && existingActivity.id != activity.id) {
							
							var branchActivityId = activityIndex;
							var afterBranchActivityId = activityIndex + 1 < activities.length 
													  ? activities[activityIndex + 1].id : null;
							var branchActivities = [activity];
							activityIndex++;
							
							while (activityIndex < result.activities.length) {
								activityData = result.activities[activityIndex];
								var activity = new Activity(paper, activityIndex, activityData.id,
														    activityData.type, activityData.name,
														    activityData.status, activityData.url,
														    activityData.childActivities);
								if (activity.id == afterBranchActivityId) {
									activityIndex--;
									break;
								} else {
									branchActivities.push(activity);
									activityIndex++;
								}
							}
							
							paper.setSize(140, 60 * (activities.length + branchActivities.length - 1));
							ActivityUtils.expandBranch(branchActivityId, branchActivities);
						} else {
							// we are refreshing existing bar, so animate if needed
							ActivityUtils.transform(existingActivity, activity);
						}
					} else {
						ActivityUtils.drawActivity(activity, true,
												   activityIndex == result.activities.length - 1);
					}
				}
				
				// draw support activities if they exist
				if (result.support && !supportSeparatorRow.is(':visible')) {
					supportSeparatorRow.show();
					supportPart.height(17 + 33 * result.support.length).show();
					
					var supportPaper = Raphael('supportPart');
					
					jQuery.each(result.support, function(activityIndex, activityData) {
						var activity = new SupportActivity(supportPaper, activityIndex,
								activityData.name, activityData.status, activityData.url);
						activity.shape = supportPaper.path(activity.path);
						activity.shape.attr(ActivityUtils.getShapeAttributes(activity));
						ActivityUtils.addDecoration(activity, null, true);
						ActivityUtils.addEffects(activity);
						supportPaper.text(90, 24 +  33 * activityIndex, activity.name);
					});
				}
				
				resizeElements();
				// scroll to the current activity
				$('#progressBarDiv').scrollTop(Math.max((currentActivityIndex-1)*60, 0));
			}
		}
	});
}