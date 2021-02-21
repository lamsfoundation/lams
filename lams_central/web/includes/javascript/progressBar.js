	//------- GLOBAL VARIABLES ----------
	//IMPORTANT: set following variables on the page which imports this JS file
var lessonId = lessonId || null,
	toolSessionId = toolSessionId || null,
	isHorizontalBar = isHorizontalBar || false,
	hasContentFrame = hasContentFrame || true,
	hasDialog = hasDialog || false,
	presenceEnabled = presenceEnabled || false,
	REVIEW_ACTIVITY_TITLE = REVIEW_ACTIVITY_TITLE || 'Review activity',

	//SVG paths for activity shapes
	PATH_START_LINE_HORIZONTAL = 'M 0 18 h 35',
	PATH_START_LINE_VERTICAL = 'M 70 0 v 20',

	PATH_SQUARE = " v16 h16 v-16 z",
	PATH_BIG_SQUARE = " v26 h26 v-26 z",
	PATH_CIRCLE = " m -8 0 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0",
	PATH_QUARTER_CIRCLE = " a16 16 0 0 0 16 16 v-16 z",
	PATH_TRIANGLE = " l8 16 l8 -16 z",
	PATH_OCTAGON = " l-7 7 v12 l7 7 h12 l7 -7 v-12 l-7 -7 z",
	
	// filled in when paper gets created
	GLOW_FILTER = null,
	
	DEFAULT_TEXT_ATTRIBUTES = {
		'text-anchor' : 'middle',
		'font-size'   : 10,
		'font-family' : 'sans-serif'
	},

	isPreview = false,
	controlFramePadding = 0,

	// popup window size
	popupWidth = 1280,
	popupHeight = 720;

// ----- CONTROL FRAME & WINDOW MANIPULATION -----

// generic function for opening a pop up
function openPopUp(url, title, h, w, status, forceNewWindow) {

	var width = screen.width;
	var height = screen.height;

	var left = ((width / 2) - (w / 2));
	var top = ((height / 2) - (h / 2));

	if (forceNewWindow) {
		// opens a new window rather than loading content to existing one
		title += new Date().getTime();
	}

	window.open(url, title, "HEIGHT=" + h + ",WIDTH=" + w
			+ ",resizable=yes,scrollbars=yes,status=" + status
			+ ",menubar=no, toolbar=no"
			+ ",top=" + top + ",left=" + left);
}

// just a short cut to openPopUp function
function openActivity(url) {
	if (hasDialog) {
		var dialog = $('#progressBarDialog');
		if (!dialog.hasClass('ui-dialog')) {
			dialog.dialog({
				'autoOpen' : false,
				'height' : popupHeight,
				'width' : popupWidth,
				'modal' : true,
				'resizable' : false,
				'hide' : 'fold',
				'title' : REVIEW_ACTIVITY_TITLE,
				'open' : function() {
					// load contents after opening the dialog
					$('iframe', this).on('load', function(){
						// remove finish button
						// so user can only close dialog with X button
						$(this).contents().find('#finishButton, #finish').remove();
					}).attr('src', url);
				},
				'beforeClose' : function(){
					$('iframe', this).off('load').attr('src', null);
				}
			});
		}
		
		dialog.dialog('open');
	} else {
		openPopUp(url, "LearnerActivity", popupHeight, popupWidth, "yes");
	}
}

// loads a new activity to main content frame; alternative to opening in pop up
function loadFrame(activity) {
	window.location.href = activity.url;
	
	var displayedActivity = activity.bar.displayedActivity;
	// do not glow current activity
	activity.bar.displayedActivity = activity.status == 0 ? null : activity;
	if (displayedActivity) {
		// remove glow from previusly displayed activity
		ActivityUtils.removeGlow(displayedActivity);
	}
	
	if (activity.bar.displayedActivity) {
		ActivityUtils.addGlow(activity);
	}
}


// double click triggers also single click event, this method helps
function handleClicks(elem, click, dblClick) {
	var handler = function(event) {
		var currentTime = new Date().getTime(),
			clickLength = currentTime - (elem.lastClick ? elem.lastClick : 0),
			dblClicked = clickLength < 500 && clickLength > 0;
		elem.lastClick = currentTime;
			
		if (dblClicked) {
			event.preventDefault();
			if (dblClick) {
				clearTimeout(elem.clickTrigger);
				dblClick();
			}
		} else {
			elem.clickTrigger = setTimeout(function() {
				if (click) {
					click();
				}
			}, 500);
		}
	};
	elem.click(handler);
	if (elem.touchstart) {
		elem.touchstart(handler);
	}
}

// This should be the super class for Activities,
// but it's hard to accomplish in JS.
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
		activity.path = 'M ' + (activity.middle - 8) + ' ' + activity.y
				+ PATH_SQUARE;
		activity.cssClass = 'activityCurrent';
		activity.statusTooltip = LABELS.CURRENT_ACTIVITY;
	},

	shapeCompletedActivity : function(activity) {
		// dark blue circle
		activity.path = 'M ' + activity.middle + ' ' + (activity.y + 8)
				+ PATH_CIRCLE;
		activity.cssClass = 'activityCompleted';
		activity.statusTooltip = LABELS.COMPLETED_ACTIVITY;
	},

	shapeAttemptedActivity : function(activity) {
		// green square with dark red arc
		activity.path = 'M ' + (activity.middle - 8) + ' ' + activity.y
				+ PATH_SQUARE;
		activity.cssClass = 'activityNotStarted';
		activity.statusTooltip = LABELS.ATTEMPTED_ACTIVITY;

		// this and similar methods are run when activity shape is drawn for  real
		activity.addDecoration = function(act) {
			var paper = act.bar.paper;
			act.decoration = Snap.set();
			// get exact Y where inner shape was drawn
			// it is different than activity.y in OptionalActivity
			// because of gray square around it
			var y = Snap.parsePathString(act.shape.attr('path'))[0][2],
				arc = paper.path('M ' + (act.middle - 8) + ' ' + y
					+ PATH_QUARTER_CIRCLE),
				transformation = act.shape.transform();
			if (transformation.length > 0) {
				arc.transform(transformation);
			}
			arc.attr({
				'class' : 'activityCurrent',
				'opacity' : 0,
				'cursor' : 'pointer'
			});
			act.decoration.push(arc);
		}
	},

	shapeToStartActivity : function(activity) {
		// green triangle
		activity.path = 'M ' + (activity.middle - 8) + ' ' + activity.y
				+ PATH_TRIANGLE;
		activity.cssClass = 'activityNotStarted';
		activity.statusTooltip = LABELS.TOSTART_ACTIVITY;
	},

	shapeGateActivity : function(activity) {
		// red octagon for STOP road sign
		activity.path = 'M ' + (activity.middle - 6) + ' ' + activity.y
				+ PATH_OCTAGON;
		activity.cssClass = 'gate';

		activity.addDecoration = function(act) {
			var paper = act.bar.paper;
			act.decoration = Snap.set();

			// should be internationalised?
			var text = paper.text(act.middle, act.y + 16, 'STOP')
							.attr(DEFAULT_TEXT_ATTRIBUTES);
			
			text.attr({
				'opacity'   : 0,
				'font-size' : 9,
				'class'     : 'gateText',
				'cursor'    : 'pointer'
			});
			act.decoration.push(text);
			
			
			if (act.status == 0) {
				// add dark red edge when current activity
				act.statusTooltip = LABELS.CURRENT_ACTIVITY;

				var edge = paper.path(act.path);
				edge.attr({
					'opacity'      : 0,
					'class'        : 'activityCurrent',
					'stroke-width' : 3,
					'fill'         : 'none',
					'cursor'       : 'pointer'
				});
				act.decoration.push(edge);
			} else {
				act.statusTooltip = LABELS.TOSTART_ACTIVITY;
			}
		}
	},

	shapeComplexActivityContainer : function(activity) {
		var addDecoration = activity.addDecoration;
		activity.addDecoration = function(act) {
			// run previous addDecoration(), for example defined in Attempted
			// Activity
			if (addDecoration) {
				addDecoration(act);
			}

			var paper = act.bar.paper;
			// gray square in background
			var square = paper.path('M ' + (act.middle - 13) + ' ' + act.y
					+ PATH_BIG_SQUARE);
			square.attr({
				'opacity': 0,
				'class'  : 'activityComplex',
				'cursor' : 'pointer'
			});

			// inform that it goes behind, not to front like other decoration
			act.decorationWraps = true;
			square.decorationWraps = true;

			if (!act.decoration) {
				act.decoration = Snap.set();
			}
			act.decoration.push(square);
		}
	},

	// return some attributes in Snap consumable way
	getShapeAttributes : function(activity) {
		return {
			'path' : activity.path,
			'fill' : activity.fill,
			'class' : activity.cssClass,
			'stroke' : activity.stroke,
			'cursor' : 'pointer'
		}
	},

	// does the actual drawing, based on info in Activity object
	drawActivity : function(activity, quick, isLast) {
		activity.bar.activities[activity.index] = activity;
		// all elements that activity consists of
		// so they all can be moved at once
		var paper = activity.bar.paper;
		activity.elements = Snap.set();
		// only now do the real drawing, add event handlers etc.
		activity.shape = paper.path(activity.path);
		// add Activity attributes
		activity.shape.attr(ActivityUtils.getShapeAttributes(activity));
		activity.elements.push(activity.shape);
		var isLarger = activity.isComplex || activity.type == 'g';
		
		// label underneath the shape
		var label = null,
			content = activity.name || '';
		if (isHorizontalBar) {
			// cut lengthy activity names so they don't overlap
			if (content.length > 23) {
				content = content.substring(0,20) + '...';
			}
			label = paper.text(activity.middle,
							   40 + (activity.index % 2 == 0 ? 0 : 15),
							   content)
						.attr(DEFAULT_TEXT_ATTRIBUTES);
		} else {
			label = ActivityUtils.breakTitle(paper, +paper.attr('width') - 7,
											 activity.middle, 47 + 60 * activity.index + (isLarger ? 10 : 0),
											 content );
		}
		activity.elements.push(label);
		
		if (!isLast) {
			// line between activities; last activity does not have it
			var line = null;
			if (isHorizontalBar) {
				line = paper.path('M ' + (activity.middle + 15) + ' 18 h 30')
							.attr('class', 'transitionLine');
			} else {
				var startY = activity.y + label.getBBox().height + (isLarger ? 28 : 18),
					endY = 20 + 60 * (activity.index + 1);
				if (startY < endY) {
					line = paper.path('M ' + activity.middle + ' ' + startY + ' L ' + activity.middle + ' ' + endY)
								.attr('class', 'transitionLine');
				}
			}
			
			if (line) {
				activity.elements.push(line);
			}
		}

		if (!quick) {
			// slowly show the activity
			activity.elements.forEach(function(elem) {
				// hide first
				elem.attr('opacity', 0);
				// show in 1 second
				elem.animate({
					'opacity' : 1
				}, 1000);
			});
		}

		// add additional elements
		ActivityUtils.addDecoration(activity, null, quick);
		// add hover, click etc. handlers
		ActivityUtils.addEffects(activity);
	},

	// adds handlers to activity for mouse interactions
	// long method with simple actions
	addEffects : function(activity) {
		// remove existing glow
		ActivityUtils.removeGlow(activity, true);
		// add again, for transformed shape
		if (activity.bar.displayedActivity == activity && activity.status != 0){
			ActivityUtils.addGlow(activity);
		}
		
		// remove any existing handlers
		if (activity.shape.events) {
			while (activity.shape.events.length) {
				// iterate over any handlers bound
				activity.shape.events.pop().unbind();
			}
		}

		var mouseover = function(e, x, y) {
				ActivityUtils.addGlow(activity);
	
				// add tooltip
				var tooltipText = '<b>' + activity.name + '</b><br />'
						+ activity.statusTooltip;
				// move to proper place and show
				$('#progress-bar-tooltip').stop(true, true).css("left", x + 10).css("top", y + 20).css("opacity",1)
						.html(tooltipText).delay(700).fadeIn();
			},

			mouseout = function() {
				// remove glow
				ActivityUtils.removeGlow(activity);
				$('#progress-bar-tooltip').stop(true, true).fadeOut();
			},

			isSupportActivity = activity instanceof SupportActivity,
			
			dblclick = activity.url ? function() {
				// open pop up or dialog if it is a support or completed activity or it is monitor
				if (isSupportActivity
						|| (typeof mode === 'undefined' || mode === 'teacher')
						|| activity.status == 1
						|| (!hasContentFrame && activity.status <= 2)) {
					openActivity(activity.url);
	
					if (isSupportActivity) {
						// do not ask server, just mark the activity as attempted
						activity.transformToAttempted();
					}
				} else {
					loadFrame(activity);
				}
			} : null,

			click = activity.isComplex ? function() {
				// show complex (Optional, Branching) activity inner content
				ActivityUtils.showComplexContent(activity);
			} : null;

		// assign handlers
		activity.shape.hover(mouseover, mouseout);
		handleClicks(activity.shape, click, dblclick);
		if (activity.decoration) {
			// add handlers not only to shape, but also to all decoration
			// elements
			activity.decoration.forEach(function(elem) {
				elem.hover(mouseover, mouseout);
				handleClicks(elem, click, dblclick);
			});
		}
	},
	
	addGlow : function(activity) {
		if (!activity.glows) {
			var paper = activity.bar.paper,
				target = null;
			// add glowing effect on hover
			if (activity.decorationWraps) {
				activity.decoration.forEach(function(elem) {
					// check which decoration element should glow
					if (elem.decorationWraps) {
						// glow the wrapping decoration element
						// for example gray square in Optonal Activity container
						// is bigger than inner activity shape, so it should glow
						elem.attr('filter', GLOW_FILTER);
						activity.glows = elem;
						return false;
					}
				});
			} else {
				activity.shape.attr('filter', GLOW_FILTER);
				activity.glows = activity.shape;
			}
		}
	},
	
	// remove glow when mouse leaves shape
	removeGlow : function(activity, force) {
		if (activity.glows && (force || activity.bar.displayedActivity != activity)) {
			activity.glows.attr('filter', null);
			activity.glows = null;
		}
	},

	// copy important properties and morph visible elements
	transform : function(sourceActivity, targetActivity) {
		var gotCompleted = false;

		// modify only if anything changed
		if (sourceActivity.status != targetActivity.status) {
			// was just completed
			gotCompleted = targetActivity.status == 1;

			sourceActivity.path = targetActivity.path;
			sourceActivity.fill = targetActivity.fill;
			sourceActivity.stroke = targetActivity.stroke;
			sourceActivity.url = targetActivity.url;
			sourceActivity.status = targetActivity.status;
			sourceActivity.statusTooltip = targetActivity.statusTooltip;
			sourceActivity.addDecoration = targetActivity.addDecoration;

			// transform current shape to the new one
			ActivityUtils.animate(sourceActivity, sourceActivity.decoration);
		}

		var isCurrent = targetActivity.status == 0;
		if (sourceActivity.childActivities) {
			// run for all inner activities (Optional, Branching)
			$.each(sourceActivity.childActivities,
				function(childActivityIndex, childActivity) {
					var targetChildActivity = targetActivity.childActivities[childActivityIndex];
					// if child activity is current, parent activity
					// is current as well
					isCurrent |= targetChildActivity.status == 0;
					ActivityUtils.transform(childActivity,
						targetChildActivity);
				});
		}

		if (isCurrent) {
			// shows box with inner activities, if not open yet
			ActivityUtils.showComplexContent(sourceActivity);
			if (sourceActivity.toggleChildren) {
				// complex sequence just became current, show it
				sourceActivity.toggleChildren('open');
			}
			// close box with inner activities, if finished
		} else if (gotCompleted) {
			if (sourceActivity.isComplex) {
				ActivityUtils.hideOtherComplexContent();
			} else if (sourceActivity.toggleChildren) {
				sourceActivity.toggleChildren('close');
			}
		}
	},

	animate : function(activity, oldDecoration) {
		if (activity.shape) {
			// remove old decoration and start showin new one
			ActivityUtils.addDecoration(activity, oldDecoration, false);
			// transform the shape
			activity.shape.animate(ActivityUtils.getShapeAttributes(activity),
					2000, undefined, function() {
						if (!(activity instanceof OptionalActivity)) {
							// inner activities do not have glow and tooltip
							// effects
							ActivityUtils.addEffects(activity);
						}
					});
		}
	},

	// adds additional elements to activity basic shape
	// quick is for inital drawing, no nice effect is needed
	addDecoration : function(activity, oldDecoration, quick) {
		if (oldDecoration) {
			// hide existing decoration
			oldDecoration.forEach(function(elem) {
				if (activity.elements) {
					activity.elements.exclude(elem);
				}
				elem.animate({
					'opacity' : 0
				}, quick ? 0 : 1000, undefined, function() {
					elem.remove();
				});
			});
		}

		// run function that draws decoration
		if (activity.addDecoration) {
			var animation = Snap.animation({
				'opacity' : 1
			}, quick ? 0 : 1000);

			activity.addDecoration(activity);
			activity.decoration.forEach(function(elem) {
				if (activity.elements) {
					activity.elements.push(elem);
				}
				if (elem.decorationWraps) {
					// decoration element is bigger that activity shape,
					// put it in background
					$(elem.node).parent().prepend(elem.node);
				} else {
					$(elem.node).parent().append(elem.node);
				}
				
				// delay animation if there is old decroation to remove
				setTimeout(function(){
					elem.animate(animation);
				}, oldDecoration ? 1000 : 0);
				
			});
		}
	},

	// hide all Optional Activities, except for the given one
	hideOtherComplexContent : function(currentOptionalContentId) {
		$('div.optionalActivity').each(function(index, contentDiv) {
			var content = $(contentDiv);
			if (content.attr('id') != currentOptionalContentId) {
				content.slideUp(currentOptionalContentId ? 'fast' : 'slow');
			}
		});
	},

	// hide all Optional Activities instantly - used when the control bar shrinks
	hideComplexContentInstantly : function() {
		$('div.optionalActivity').each(function(index, contentDiv) {
			$(contentDiv).hide();
		});
	},

	// draw box with inner activities
	showComplexContent : function(activity) {
		if (activity.isComplex) {
			// hide glow if shown (IE)
			ActivityUtils.removeGlow(activity);
			// remove other boxes, if shown
			ActivityUtils
					.hideOtherComplexContent(activity.optionalContent ? activity.optionalContent
							.attr('id')
							: null);

			if (!activity.optionalContent) {
				// build box HTML
				var containerName = 'optionalActivityContent' + activity.id;
				activity.optionalContent = $('<div />').attr('id',
						containerName).addClass('optionalActivity').css({
					// a little higher than activity, to cover it
					'top'    : $(activity.shape.node).offset().top - $('#progressBarDiv').offset().top + 30,
					'left'   : $(activity.shape.node).offset().left - 65, 
					'height' : 29 * activity.childActivities.length - 1
				}).appendTo('#' + activity.bar.containerId);

				var optionalContentTable = $('<table cellspacing="0" />')
						.appendTo(activity.optionalContent);

				ActivityUtils.addChildActivitiesRows(activity,
						optionalContentTable, activity.optionalContent, false);
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
				// second tier, a part of optional sequence or
				// branching
				parentId = $('td', parent).attr('id');
				// find last activity from sequence and put
				// current one after it to keep ordering
				row.insertAfter($('td[id^=' + parentId + ']',
						container).last().parent());
				isCurrent |= childActivity.status == 0;
			} else {
				parentId = container.attr('id');
				row.appendTo(parent);
			}

			var cellId = parentId + 'child'
					+ childActivityIndex,
				cell = $('<td />').attr('id', cellId).appendTo(row);
			if (isNested) {
				cell.hide();
			}
			// each row has its own paper
			var paper = childActivity.bar.paper = Snap(145, 23);
			cell.append(paper.node);
			// draw the inner activity
			childActivity.shape = paper
					.path(childActivity.path);
			childActivity.shape.attr(ActivityUtils
					.getShapeAttributes(childActivity));
			ActivityUtils.addDecoration(childActivity, null,
					true);
			
			var label = ActivityUtils.breakTitle(paper, activity.bar.paper.attr('width') - 31,
					 							 35, childActivity.y + 11,
												 (isNested ? '- ' : '') + childActivity.name)
							 // align to left
							 .attr('text-anchor', 'start'),
				labelHeight = label.getBBox().height + 10,
				paperHeight = Math.max(23, labelHeight),
				shift = paperHeight - 23,
				transformationString = 't 0 ' + (shift)/2;
			paper.attr({
				'height' : paperHeight
			});
			childActivity.shape.transform(transformationString);
			if (childActivity.decoration) {
				activity.decoration.forEach(function(elem) {
					elem.transform(transformationString);
				});
			}

			var click = null;
			if (!isNested) {
				container.height(container.height() + shift);
				// only first tier inner activities
				if (childActivityIndex == 0) {
					click = function() {
						// first row is the parent activity
						// itself; hide content box when clicked
						container.slideUp();
					}
				} else if (childActivity.childActivities) {
					// show/hide 2nd tier inner activities
					childActivity.toggleChildren = function(
							forceCommand) {
						if (cell.is(':visible')) {
							var childCells = $('td[id^=' + cellId + 'child]', parent),
								isOpen = childCells.is(':visible');
							if (!forceCommand || (isOpen ? forceCommand == 'close'
														 : forceCommand == 'open')) {
								var containerHeightDelta = 3;
								childCells.each(function(){
									containerHeightDelta += $(this).height();
								});
								childCells.toggle();
								// resize inner content box
								container.height(container.height()
									+ (isOpen ? -containerHeightDelta
											   : containerHeightDelta));
							}
						}
					}

					click = function() {
						// show 2nd tier when 1st tier activity
						// is clicked
						childActivity.toggleChildren();
					}
				}
			}
			var dblclick = function() {
				if (childActivity.url) {
					if (childActivity.status == 1) {
						openActivity(childActivity.url);
					} else {
						loadFrame(childActivity);
					}
				}
			}
			handleClicks(cell, click, dblclick);

			if (childActivity.childActivities) {
				isCurrent |= ActivityUtils
						.addChildActivitiesRows(childActivity,
								row, container, true);
				if (isCurrent && childActivity.toggleChildren) {
					childActivity.toggleChildren('open');
				}
			}
		});

		return isCurrent;
	},

	// replace single Branching activity with list of branch activities
	expandBranch : function(bar, branchIndex, branchActivities) {
		// hide any boxes obstructing the view
		ActivityUtils.hideOtherComplexContent();

		var branchActivity = bar.activities[branchIndex], 
			activityShift = branchActivities.length - 1,
			// how many pixels move subsequent activities down
			pixelShift = 60 * activityShift,
			// activity just after branching
			afterBranchActivity = null,
			activities = bar.activities.slice(0, branchIndex);
		// move down/right existing activities that come after Branching
		// start with the last one
		for (var activityIndex = bar.activities.length - 1; activityIndex > branchIndex; activityIndex--) {
			afterBranchActivity = bar.activities[activityIndex];
			activities[activityIndex + activityShift] = afterBranchActivity;
			var transform = null;
			if (isHorizontalBar) {
				afterBranchActivity.middle += pixelShift;
				transform = 'T ' + pixelShift + ' 0';
			} else {
				afterBranchActivity.y += pixelShift;
				transform = 'T 0 ' + pixelShift;
			}
			
			var paper = afterBranchActivity.bar.paper,
				transformedPathElem = paper.path(afterBranchActivity.path).transform(transform);
			afterBranchActivity.path = transformedPathElem.attr('path');
			transformedPathElem.remove();
			
			afterBranchActivity.elements.forEach(function(elem) {
				var x = +elem.attr('x'),
					targetProperties = null;
				// text, rectangles etc. have 'x', paths have 'path'
				if (x) {
					// move either right or down, depending on bar orientation
					targetProperties = isHorizontalBar ?
							{
						 	 'x' : x + pixelShift
							}
							:
							{
							 'y' : +elem.attr('y') + pixelShift
							};
				} else {
					// get the path after transformation
					var transformMatrix = elem.transform(transform).transform().globalMatrix,
						targetPath = Snap.path.map(elem.attr('path'), transformMatrix);
					elem.transform('');
					targetProperties = {
						'path' : targetPath
					};
				}
				elem.animate(targetProperties, 2000);
			});
		}
		
		bar.activities = activities;

		// smoothly remove Branching activity
		branchActivity.elements.forEach(function(elem) {
			elem.animate({
				'opacity' : 0
			}, 2000, undefined, function() {
				elem.remove();
			});
		});

		// check if it was an empty branch
		if (branchActivities.length > 0) {
			// create branch activities structures
			for (var activityIndex = 0; activityIndex < branchActivities.length; activityIndex++) {
				var activityData = branchActivities[activityIndex],
					activity = new Activity(bar, activityIndex + branchIndex,
						activityData.id, activityData.type, activityData.name,
						activityData.status, activityData.url,
						activityData.childActivities);
				activities[activityIndex + branchIndex] = activity;
				if (activity.status == 0) {
					currentActivityIndex = activityIndex;
				}
			}
	
			// smoothly draw branch activities
			setTimeout(function() {
				for (var activityIndex = 0; activityIndex < branchActivities.length; activityIndex++) {
							ActivityUtils.drawActivity(
								activities[activityIndex + branchIndex],
								false,
								!afterBranchActivity && activityIndex == branchActivities.length - 1
							);
						}
					}, 2000);
		}
	},
	
	// break string into several lines if it is too long
	breakTitle : function(paper, width, x, y, title) {
		var brokenTitle = [title],
			elem = null,
			dummyPaper = Snap(width, 10000);
		$('body').append(dummyPaper.node);
		do {
			elem = ActivityUtils.createLabel(dummyPaper, x, y, brokenTitle);
			if (elem.getBBox().width > width) {
				// break the title into more chunks
				elem.remove();
				var parts = brokenTitle.length + 1,
					chunk = Math.ceil(title.length/parts);
				brokenTitle = [];
				for (var part = 1; part <= parts; part++) {
					var startIndex = (part - 1) * chunk,
						endIndex = part == parts ? undefined : part*chunk;
					brokenTitle.push(title.substring(startIndex, endIndex));
				}
			} else {
				// stop the iteration
				elem = null;
			}
		} while (elem != null);

		dummyPaper.remove();
		return ActivityUtils.createLabel(paper, x, y, brokenTitle);
	},
	
	createLabel : function(paper, x, y, text) {
		var elem = paper.text(x, y, text).attr(DEFAULT_TEXT_ATTRIBUTES);
		// move each line, except the first, a bit lower
		$('tspan', elem.node).each(function(index, tspan){
			if (index > 0) {
				$(tspan).attr({
					'x'  : x,
					'dy' : '1.1em'
				});
			}
		});
		return elem;
	}
}

// main activities
function Activity(bar, index, id, type, name, status, url, childActivitiesData) {
	this.bar = bar;
	this.index = index;
	this.id = id;
	this.type = type;
	this.name = name;
	this.status = status;
	this.url = url;

	// Optional Activities, Optional Sequences or Branching in preview mode
	this.isComplex = type == 'o' || (isPreview && type == 'b');

	if (isHorizontalBar) {
		// middle of the activity in X axis
		this.middle = 48 + 60 * index;
		this.y = 10;
	} else {
		// X positioning
		this.middle = 70;
		this.y = 20 + 60 * index;
	}

	// first draw the inner shape, then put back the realY for background gray square
	var finalY = this.y;
	if (!isHorizontalBar && this.isComplex) {
		this.y += 5;
	}

	if (type == 'g') {
		// gate activity
		if (isHorizontalBar) {
			this.y -= 5;
		}
		ActivityUtils.shapeGateActivity(this);
	} else {
		ActivityUtils.shapeByStatus(this);
	}

	// special behaviour for complex activities
	if (this.isComplex) {
		this.y = finalY - (isHorizontalBar ? 5 : 0);
		ActivityUtils.shapeComplexActivityContainer(this);

		this.childActivities = [ new OptionalActivity(bar.paper, name, status, url) ];
		var childActivities = this.childActivities;
		$.each(childActivitiesData, function(childActivityIndex,
				childActivityData) {
			childActivities[childActivityIndex + 1] = new OptionalActivity(bar.paper,
					childActivityData.name, childActivityData.status,
					childActivityData.url, childActivityData.childActivities,
					false);
		});
	}
}

// Support (floating) activities are show in separate box and behave differently
function SupportActivity(paper, index, name, status, url) {
	this.name = name;
	this.status = status;
	this.url = url;
	this.bar = {
			'paper' : paper
	}

	this.middle = 24;
	this.y = 17 + 33 * index;

	if (status <= 2) {
		ActivityUtils.shapeAttemptedActivity(this);
	} else if (status == 3) {
		ActivityUtils.shapeToStartActivity(this);
	}
	this.statusTooltip = LABELS.SUPPORT_ACTIVITY;

	this.transformToAttempted = function() {
		var oldDecoration = this.decoration;
		ActivityUtils.shapeAttemptedActivity(this);
		this.statusTooltip = LABELS.SUPPORT_ACTIVITY;

		ActivityUtils.animate(this, oldDecoration);
	}
}

// Optional and Branching inner activities
function OptionalActivity(paper, name, status, url, childActivitiesData, isNested) {
	this.name = name;
	this.status = status;
	this.url = url;
	this.bar = {
			'paper' : paper
	}

	this.middle = isNested ? 22 : 15;
	this.y = 5;

	ActivityUtils.shapeByStatus(this);

	// if Sequence or Branching, this is the 2nd tier of inner activities
	if (childActivitiesData) {
		this.childActivities = [];
		var childActivities = this.childActivities;
		$.each(childActivitiesData, function(childActivityIndex,
				childActivityData) {
			childActivities[childActivityIndex] = new OptionalActivity(paper,
					childActivityData.name, childActivityData.status,
					childActivityData.url, null, true);
		});
	}
}

function hideProgressBars() {
	ActivityUtils.hideComplexContentInstantly();
}
// refresh progress bar on first/next activity load
function fillProgressBar(barId) {
	var bar = bars[barId];
	if (!bar) {
		// bar should have been initialised first by another script
		return false;
	}

	$.ajax({
		url : LAMS_URL + 'learning/learner/getLearnerProgress.do',
		data : {
			'lessonID' : lessonId,
			'toolSessionID' : toolSessionId,
			'userID'   : bar.userId
		},
		cache : false,
		dataType : 'json',
		success : function(result) {
			// check if container still exists
			// it may happen if user quickly changes pages in Monitor
			var barContainer = $('#' + bar.containerId);
			
			// if the bar is gone
			if (barContainer.length == 0){
				return;
			}
			// if there is no progress returned for the given user
			// in monitoring we show not started, in learning just do not show anything.
			if (!result) {
				if ( typeof LABELS != "undefined" && typeof LABELS.PROGRESS_NOT_STARTED != "undefined")
					barContainer.text(LABELS.PROGRESS_NOT_STARTED);
				return;
			}
			// if nothing changed, don't do any calculations
			if (bar.currentActivityId && (result.currentActivityId == bar.currentActivityId)) {
				return;
			}
			
			if (typeof LABELS == "undefined" && result.messages) {
				LABELS = {
					CURRENT_ACTIVITY : result.messages["label.learner.progress.activity.current.tooltip"],
					COMPLETED_ACTIVITY : result.messages["label.learner.progress.activity.completed.tooltip"],
					ATTEMPTED_ACTIVITY : result.messages["label.learner.progress.activity.attempted.tooltip"],
					TOSTART_ACTIVITY : result.messages["label.learner.progress.activity.tostart.tooltip"],
					SUPPORT_ACTIVITY : result.messages["label.learner.progress.activity.support.tooltip"],
					CONFIRM_RESTART : result.messages["message.learner.progress.restart.confirm"],
					RESTART: result.messages["message.lesson.restart.button"],
					NOTEBOOK: result.messages["label.learner.progress.notebook"],
					EXIT : result.messages["button.exit"],
					SUPPORT_ACTIVITIES : result.messages["label.learner.progress.support"],
					PROGRESS_BAR: result.messages["label.my.progress"]
				};
			}
			
			bar.currentActivityId = result.currentActivityId;
			isPreview = result.isPreview;

			var paper = bar.paper;
			if (!paper) {
				// create paper only the first time
				paper = bar.paper = Snap(isHorizontalBar ? 40 + 60 * result.activities.length : 140,
						isHorizontalBar ? 60 : 60 * result.activities.length + 25);
				barContainer.append(paper.node);
				// first line on the top
				paper.path(isHorizontalBar ? PATH_START_LINE_HORIZONTAL : PATH_START_LINE_VERTICAL);
				
				if (!GLOW_FILTER) {
					GLOW_FILTER = paper.filter(Snap.filter.blur(1.5));
				}
			}

			// we need this to scroll to the current activity
			var currentActivityIndex = 0;

			for (var activityIndex = 0; activityIndex < result.activities.length; activityIndex++) {
				var activityData = result.activities[activityIndex],
				// prepare the Activity descriptor, but do not draw yet
					activity = new Activity(bar, activityIndex,
						activityData.id, activityData.type,
						activityData.name, activityData.status,
						activityData.url,
						activityData.childActivities);
				if (activity.status == 0) {
					currentActivityIndex = activityIndex;
				}

				var activities = bar.activities;
				if (!activities) {
					activities = bar.activities = [];
				}

				var existingActivity = activities[activityIndex];
				if (existingActivity) {
					// if in preview mode, always display all inner
					// activities, i.e. never expand
					if (!isPreview && existingActivity.type == 'b' && existingActivity.id != activity.id) {
						var branchActivityId = activityIndex,
							afterBranchActivityId = activityIndex + 1 < activities.length ?
									activities[activityIndex + 1].id : null,
							branchActivities = [];
									
						// check if it is an empty branch
						if (activity.id != afterBranchActivityId) {
							branchActivities.push(activity);
							activityIndex++;

							// find which activities are new (branch)
							// and which ones already existed
							while (activityIndex < result.activities.length) {
								activityData = result.activities[activityIndex];
								var activity = new Activity(bar,
										activityIndex,
										activityData.id,
										activityData.type,
										activityData.name,
										activityData.status,
										activityData.url,
										activityData.childActivities);
								if (activity.id == afterBranchActivityId) {
									// prepare for the next big loop
									// iteration, which executes
									// normally
									activityIndex--;
									break;
								} else {
									branchActivities.push(activity);
									activityIndex++;
								}
							}

							// resize main paper to accomodate new activities
							paper.attr({
								'width' : isHorizontalBar ? 40 + 60 * (activities.length	+ branchActivities.length - 1) : 140,
								'height' : isHorizontalBar ? 60 : 60 * (activities.length + branchActivities.length - 1) + 25
							});
						}
						
						ActivityUtils.expandBranch(bar,
								branchActivityId, branchActivities);
					} else {
						// refresh existing bar, transform
						// activities if needed
						ActivityUtils.transform(existingActivity,
								activity);
					}
				} else {
					// draw new activity
					ActivityUtils.drawActivity(activity, true, activityIndex == result.activities.length - 1);
				}
			}

			// draw support activities if they exist
			if (result.support && $('#supportPart').length == 1) {
				var svgheight = 17 + 33 * result.support.length;
				// separate paper for Support Activities frame
				var supportPaper = Snap();
				$('#supportPart').height(svgheight+5)
								 .append(supportPaper.node)
								 .show();
				$.each(result.support, function(activityIndex,
						activityData) {
					var activity = new SupportActivity(
							supportPaper, activityIndex,
							activityData.name, activityData.status,
							activityData.url);
					activity.shape = supportPaper
							.path(activity.path);
					activity.shape.attr(ActivityUtils
							.getShapeAttributes(activity));
					ActivityUtils.addDecoration(activity, null,
							true);
					ActivityUtils.addEffects(activity);
					supportPaper.text(90, 24 + 33 * activityIndex,
							activity.name).attr(DEFAULT_TEXT_ATTRIBUTES);
					supportPaper.attr(
							{'height' : svgheight}
					)
				});
				$("#supportitem").show();
			}

			// scroll to the current activity
			if (!isHorizontalBar) {
				$('#' + bar.containerId).scrollTop(
							Math.max((currentActivityIndex - 1) * 60,0)
						);
			}
			
			//callback function
	 	    if (typeof onProgressBarLoaded === "function") { 
		        // safe to use the function
	 	    	onProgressBarLoaded();
		    }
		}
	});

	return true;
}
