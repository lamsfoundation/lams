// ------- GLOBAL VARIABLES ----------
// IMPORTANT: set following variables on the page which imports this JS file
var isHorizontalBar = isHorizontalBar || false;
var hasContentFrame = hasContentFrame || true;
var hasDialog = hasDialog || false;
var presenceEnabled = presenceEnabled || false;
var REVIEW_ACTIVITY_TITLE = REVIEW_ACTIVITY_TITLE || 'Review activity';

// colors used in shapes
// dark red
var COLOR_CURRENT_ACTIVITY = "rgb(187,0,0)";
// dark blue
var COLOR_COMPLETED_ACTIVITY = "rgb(0,0,153)";
// green
var COLOR_TOSTART_ACTIVITY = "rgb(0,153,0)";
// black
var COLOR_STROKE_ACTIVITY = "rgb(0,0,0)";
// red
var COLOR_GATE = "rgb(255,0,0)";
// white
var COLOR_GATE_TEXT = "rgb(255,255,255)";
// gray
var COLOR_COMPLEX_BACKGROUND = "rgb(153,153,153)";

// SVG paths for activity shapes
var PATH_START_LINE_HORIZONTAL = 'M 0 18 h 35';
var PATH_START_LINE_VERTICAL = 'M 70 0 v 20';

var PATH_SQUARE = " v16 h16 v-16 z";
var PATH_BIG_SQUARE = " v26 h26 v-26 z";
var PATH_CIRCLE = " m -8 0 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0";
var PATH_QUARTER_CIRCLE = " a16 16 0 0 0 16 16 v-16 z";
var PATH_TRIANGLE = " l8 16 l8 -16 z";
var PATH_OCTAGON = " l-7 7 v12 l7 7 h12 l7 -7 v-12 l-7 -7 z";

var isPreview = false;
var controlFramePadding = 0;

// ----- CONTROL FRAME & WINDOW MANIPULATION -----

// generic function for opening a pop up
function openPopUp(url, title, h, w, status, forceNewWindow) {
	if (forceNewWindow) {
		// opens a new window rather than loading content to existing one
		title += new Date().getTime();
	}
	window.open(url, title, "HEIGHT=" + h + ",WIDTH=" + w
			+ ",resizable=yes,scrollbars=yes,status=" + status
			+ ",menubar=no, toolbar=no");
}

// just a short cut to openPopUp function
function openActivity(url) {
	if (hasDialog) {
		var dialog = $('#progressBarDialog');
		if (!dialog.hasClass('ui-dialog')) {
			dialog.dialog({
				'autoOpen' : false,
				'height' : 600,
				'width' : 800,
				'modal' : true,
				'resizable' : false,
				'hide' : 'fold',
				'title' : REVIEW_ACTIVITY_TITLE,
				'open' : function() {
					// load contents after opening the dialog
					$('iframe', this).load(function(){
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
		openPopUp(url, "LearnerActivity", 600, 800, "yes");
	}
}

// loads a new activity to main content frame; alternative to opening in pop up
function loadFrame(activity) {
	$('#contentFrame').attr('src', activity.url);
	
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

// adjusts elements after window resize
function resizeElements() {
	var width = $(window).width() - 160;
	var height = $(window).height();

	if (hasContentFrame) {
		// resize main content frame, if it is present
		$('#contentFrame').css({
			'width' : width + "px",
			'height' : height + "px",
			'position' : 'fixed'
		});
	}

	// do the real resizing; applicable only for Learner page
	if (!isHorizontalBar && progressPanelEnabled) {
		if (hasContentFrame && !controlFramePadding) {
			// calculate only once in the beginning
			// there will be miscalculations when trying to repeat this
			// in the middle of resizing
			controlFramePadding = $('#controlFrame').outerHeight(true)
					- $('#controlFrame').height();
		}

		// calculate immutable chunks and what is left goes for progress bar
		var progressBarHeight = height - controlFramePadding;
		$('.progressStaticHeight').each(function() {
			var elem = $(this);
			// are notebook and/or support activities hidden?
			if (elem.is(':visible')) {
				progressBarHeight -= elem.outerHeight(true);
			}
		});

		$('#progressBarDiv').height(progressBarHeight);
	}

	if (presenceEnabled) {
		// resize chat frame only if it exists
		resizeChat();
	}
}

// double click triggers also single click event, this method helps
function handleClicks(elem, click, dblclick) {
	if (click) {
		elem.click(function(e) {
			setTimeout(function() {
				// if double clicked, just reduce the counter
				if (elem.clickcounter) {
					elem.clickcounter--;
				} else {
					// no double click, so execute
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

// ------------- RAPHAEL --------------

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
		activity.fill = COLOR_CURRENT_ACTIVITY;
		activity.stroke = COLOR_STROKE_ACTIVITY;
		activity.statusTooltip = CURRENT_ACTIVITY_LABEL;
	},

	shapeCompletedActivity : function(activity) {
		// dark blue circle
		activity.path = 'M ' + activity.middle + ' ' + (activity.y + 8)
				+ PATH_CIRCLE;
		activity.fill = COLOR_COMPLETED_ACTIVITY;
		activity.stroke = COLOR_STROKE_ACTIVITY;
		activity.statusTooltip = COMPLETED_ACTIVITY_LABEL;
	},

	shapeAttemptedActivity : function(activity) {
		// green square with dark red arc
		activity.path = 'M ' + (activity.middle - 8) + ' ' + activity.y
				+ PATH_SQUARE;
		activity.fill = COLOR_TOSTART_ACTIVITY;
		activity.stroke = COLOR_STROKE_ACTIVITY;
		activity.statusTooltip = ATTEMPTED_ACTIVITY_LABEL;

		// this and similar methods are run when activity shape is drawn for
		// real
		activity.addDecoration = function(act) {
			var paper = act.bar.paper;
			act.decoration = paper.set();
			// get exact Y where inner shape was drawn
			// it is different than activity.y in OptionalActivity
			// because of gray square around it
			var y = act.shape.attr('path')[0][2];
			var arc = paper.path('M ' + (act.middle - 8) + ' ' + y
					+ PATH_QUARTER_CIRCLE);
			arc.attr({
				'fill' : COLOR_CURRENT_ACTIVITY,
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
		activity.fill = COLOR_TOSTART_ACTIVITY;
		activity.stroke = COLOR_STROKE_ACTIVITY;
		activity.statusTooltip = TOSTART_ACTIVITY_LABEL;
	},

	shapeGateActivity : function(activity) {
		// red octagon for STOP road sign
		activity.path = 'M ' + (activity.middle - 6) + ' ' + activity.y
				+ PATH_OCTAGON;
		activity.fill = COLOR_GATE;

		activity.addDecoration = function(act) {
			var paper = act.bar.paper;
			act.decoration = paper.set();

			// should be internationalised?
			// and for some reason, in horizontal bars the text must be slightly shifted, why?
			var text = paper.text(act.middle, act.y + (isHorizontalBar ? 16 : 13), 'STOP');
			
			text.attr({
				'opacity' : 0,
				'font-size' : 9,
				'font' : 'sans-serif',
				'stroke' : COLOR_GATE_TEXT,
				'cursor' : 'pointer'
			});
			// correct Raphael bug
			$('tspan', text.node).attr('dy', 0);
			act.decoration.push(text);
			
			
			if (act.status == 0) {
				// add dark red edge when current activity
				act.statusTooltip = CURRENT_ACTIVITY_LABEL;

				var edge = paper.path(act.path);
				edge.attr({
					'opacity' : 0,
					'stroke' : COLOR_CURRENT_ACTIVITY,
					'stroke-width' : 3,
					'cursor' : 'pointer'
				});
				act.decoration.push(edge);
			} else {
				act.statusTooltip = TOSTART_ACTIVITY_LABEL;
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
				'opacity' : 0,
				'fill' : COLOR_COMPLEX_BACKGROUND,
				'cursor' : 'pointer'
			});

			// inform that it goes behind, not to front like other decoration
			act.decorationWraps = true;
			square.decorationWraps = true;

			if (!act.decoration) {
				act.decoration = paper.set();
			}
			act.decoration.push(square);
		}
	},

	// return some attributes in Raphael consumable way
	getShapeAttributes : function(activity) {
		return {
			'path' : activity.path,
			'fill' : activity.fill,
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
		activity.elements = paper.set();
		// only now do the real drawing, add event handlers etc.
		activity.shape = paper.path(activity.path);
		// add Activity attributes
		activity.shape.attr(ActivityUtils.getShapeAttributes(activity));
		activity.elements.push(activity.shape);
		var isLarger = activity.isComplex || activity.type == 'g';
		
		// label underneath the shape
		var label = null;
		if (isHorizontalBar) {
			// cut lengthy activity names so they don't overlap
			var content = activity.name;
			if (content.length > 23) {
				content = content.substring(0,20) + '...';
			}
			label = paper.text(activity.middle,
					40 + (activity.index % 2 == 0 ? 0 : 15),
					content);
		} else {
			label = paper.text(activity.middle,
				47 + 60 * activity.index + (isLarger ? 10 : 0),
				activity.name);
		}
		// correct Raphael bug
		$('tspan', label.node).attr('dy', 0);
		activity.elements.push(label);
		
		if (!isLast) {
			// line between activities; last activity does not have it
			var line = null;
			if (isHorizontalBar) {
				line = paper.path('M ' + (activity.middle + 15) + ' 18 h 30');
			} else {
				line = paper.path('M ' + activity.middle + ' '
						+ (50 + 60 * activity.index  + (isLarger ? 10 : 0))
						+ ' v ' + (isLarger ? 20 : 30));
			}
			
			activity.elements.push(line);
		}

		if (!quick) {
			// slowly show the activity
			activity.elements.forEach(function(elem) {
				// hide first
				elem.attr('opacity', 0);
				// show in 1 second
				elem.animate({
					'opacity' : 1
				}, 1000, "linear");
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
			$('#tooltip').stop(true, true).css("left", x + 10).css("top", y + 20)
					.html(tooltipText).delay(1000).fadeIn();
		}

		var mouseout = function() {
			// remove glow
			ActivityUtils.removeGlow(activity);
			$('#tooltip').stop(true, true).fadeOut();
		}

		var isSupportActivity = activity instanceof SupportActivity;
		var dblclick = activity.url ? function() {
			// open pop up or dialog if it is a support or completed activity
			if (isSupportActivity
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
		} : null;

		var click = activity.isComplex ? function() {
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
		if (!activity.shape.glowRef) {
			// add glowing effect on hover
			if (activity.decorationWraps) {
				activity.decoration.forEach(function(elem) {
					// check which decoration element should glow
					if (elem.decorationWraps) {
						// glow the wrapping decoration element
						// for example gray square in Optonal Activity container
						// is bigger than inner activity shape, so it should glow
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
		}
	},
	
	// remove glow when mouse leaves shape
	removeGlow : function(activity, force) {
		if (activity.shape.glowRef && (force || activity.bar.displayedActivity != activity)) {
			activity.shape.glowRef.remove();
			activity.shape.glowRef = null;
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
					2000, 'linear', function() {
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
				}, quick ? 0 : 1000, 'linear', function() {
					elem.remove();
				});
			});
		}

		// run function that draws decoration
		if (activity.addDecoration) {
			var animation = Raphael.animation({
				'opacity' : 1
			}, quick ? 0 : 1000, "linear");

			activity.addDecoration(activity);
			activity.decoration.forEach(function(elem) {
				if (activity.elements) {
					activity.elements.push(elem);
				}
				if (elem.decorationWraps) {
					// decoration element is bigger that activity shape,
					// put it in background
					elem.toBack();
				} else {
					elem.toFront();
				}

				elem.animate(animation.delay(oldDecoration ? 1000
					: undefined));
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
					'top'    : $(activity.shape.node).offset().top - 8,
					'left'   : $(activity.shape.node).offset().left - 65, 
					'height' : 27 * activity.childActivities.length - 1
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
					+ childActivityIndex;
			var cell = $('<td />').attr('id', cellId).appendTo(
					row);
			if (isNested) {
				cell.hide();
			}

			// each row has its own paper
			var paper = childActivity.bar.paper = Raphael(
					cellId, 145, 23);
			// draw the inner activity
			childActivity.shape = paper
					.path(childActivity.path);
			childActivity.shape.attr(ActivityUtils
					.getShapeAttributes(childActivity));
			ActivityUtils.addDecoration(childActivity, null,
					true);
			
			var label = paper.text(35, childActivity.y + 11,
			// add dash before name
			(isNested ? '- ' : '') + childActivity.name)
			// align to left
			.attr('text-anchor', 'start');
			// fix a bug in FF layout
			$('tspan', label.node).attr('dy', 0);

			var click = null;
			if (!isNested) {
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
							var childCells = $('td[id^='
									+ cellId + 'child]', parent);
							var isOpen = childCells
									.is(':visible');
							if (!forceCommand
									|| (isOpen ? forceCommand == 'close'
											: forceCommand == 'open')) {
								var containerHeightDelta = 27 * childCells.length;
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

		var activityShift = branchActivities.length - 1;
		// how many pixels move subsequent activities down
		var pixelShift = 60 * activityShift;
		// activity just after branching
		var afterBranchActivity = null;
		// move down/right existing activities that come after Branching
		// start with the last one
		var activities = bar.activities;
		for ( var activityIndex = activities.length - 1; activityIndex > branchIndex; activityIndex--) {
			afterBranchActivity = activities[activityIndex];
			activities[activityIndex + activityShift] = afterBranchActivity;
			var transform = null;
			if (isHorizontalBar) {
				afterBranchActivity.middle += pixelShift;
				transform = 'T ' + pixelShift + ' 0';
			} else {
				afterBranchActivity.y += pixelShift;
				transform = 'T 0 ' + pixelShift;
			}
			
			afterBranchActivity.path = Raphael.transformPath(
					afterBranchActivity.path, transform);
			afterBranchActivity.elements.forEach(function(elem) {
				var x = elem.attr('x');
				var targetProperties = null;
				// text, rectangles etc. have 'x', paths have 'path'
				if (x) {
					// move either right or down, depending on bar orientation
					targetProperties = isHorizontalBar ?
							{
						 	 'x' : x + pixelShift
							}
							:
							{
							 'y' : elem.attr('y') + pixelShift
							};
				} else {
					var path = elem.attr('path');
					targetProperties = {
						'path' : Raphael.transformPath(path, transform)
					};
				}
				elem.animate(targetProperties, 2000, "linear", function(){
					$('tspan', elem.node).attr('dy', 0);
				});
			});
		}

		// smoothly remove Branching activity
		activities[branchIndex].elements.forEach(function(elem) {
			elem.animate({
				'opacity' : 0
			}, 2000, "linear", function() {
				elem.remove();
			});
		});

		// create branch activities structures
		for ( var activityIndex = 0; activityIndex < branchActivities.length; activityIndex++) {
			var activityData = branchActivities[activityIndex];
			var activity = new Activity(bar, activityIndex + branchIndex,
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
	this.statusTooltip = SUPPORT_ACTIVITY_LABEL;

	this.transformToAttempted = function() {
		var oldDecoration = this.decoration;
		ActivityUtils.shapeAttemptedActivity(this);
		this.statusTooltip = SUPPORT_ACTIVITY_LABEL;

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

// refresh progress bar on first/next activity load
function fillProgressBar(barId) {
	var bar = bars[barId];
	if (!bar) {
		// bar should have been initialised first by another script
		return false;
	}

	$.ajax({
		url : LAMS_URL + 'monitoring/monitoring.do',
		data : {
			'method' : 'getLearnerProgress',
			'lessonID' : lessonId,
			'userID' : bar.userId
		},
		cache : false,
		dataType : 'json',
		success : function(result) {
			// check if container still exists
			// it may happen if user quickly changes pages in Monitor
			var barContainer = $('#' + bar.containerId);
			// if nothing changed, don't do any calculations
			if (barContainer.length > 0
					&& (!bar.currentActivityId
							|| result.currentActivityId != bar.currentActivityId)) {
				bar.currentActivityId = result.currentActivityId;
				isPreview = result.isPreview;

				var paper = bar.paper;
				if (!paper) {
					// create paper only the first time
					paper = bar.paper = Raphael(bar.containerId,
							isHorizontalBar ? 40 + 60 * result.activities.length : 140,
							isHorizontalBar ? 60 : 60 * result.activities.length);
					// first line on the top
					paper.path(isHorizontalBar ? PATH_START_LINE_HORIZONTAL : PATH_START_LINE_VERTICAL);
				}

				// we need this to scroll to the current activity
				var currentActivityIndex = 0;

				for (var activityIndex = 0; activityIndex < result.activities.length; activityIndex++) {
					var activityData = result.activities[activityIndex];
					// prepare the Activity descriptor, but do not draw
					// yet
					var activity = new Activity(bar, activityIndex,
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
						if (!isPreview && existingActivity.type == 'b'
								&& existingActivity.id != activity.id) {

							var branchActivityId = activityIndex;
							var afterBranchActivityId = activityIndex + 1 < activities.length ? activities[activityIndex + 1].id
									: null;
							var branchActivities = [ activity ];
							activityIndex++;

							// find which activities are new (branch)
							// and which ones already existed
							while (activityIndex < result.activities.length) {
								activityData = result.activities[activityIndex];
								var activity = new Activity(bar,
										activityIndex, activityData.id,
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
							if (isHorizontalBar) {
								paper.setSize(40 + 60 * (activities.length
										+ branchActivities.length - 1), 60);
							} else {
								paper.setSize(140, 60 * (activities.length
										+ branchActivities.length - 1));
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
						ActivityUtils
								.drawActivity(
										activity,
										true,
										activityIndex == result.activities.length - 1);
					}
				}

				// draw support activities if they exist
				if (result.support
						&& !supportSeparatorRow.is(':visible')) {
					supportSeparatorRow.show();
					supportPart.height(17 + 33 * result.support.length)
							.show();

					// separate paper for Suppor Activities frame
					var supportPaper = Raphael('supportPart');

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
								activity.name);
					});
				}

				resizeElements();
				// scroll to the current activity
				if (!isHorizontalBar) {
					$('#' + bar.containerId).scrollTop(
								Math.max((currentActivityIndex - 1) * 60,0)
							);
				}
			}
		}
	});

	return true;
}