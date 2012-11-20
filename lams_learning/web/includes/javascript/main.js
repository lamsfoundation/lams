var isInternetExplorer = navigator.appName.indexOf("Microsoft") != -1;

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

// open/clos Notebook or Support frames
function toggleBarPart(name) {
	var part = $('#' + name + 'Part');
	var togglerCell = $('#' + name + 'TogglerCell');
	if (part.is(':visible')) {
		part.hide();
		togglerCell.html('â–˛');
	} else {
		part.show();
		togglerCell.html('â–Ľ');
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

//------------- RAPHAEL --------------
var PATH_CURRENT_ACTIVITY = " v16 h16 v-16 z";
var PATH_COMPLETED_ACTIVITY = " m -8 0 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0";
var PATH_ATTEMPTED_ACTIVITY = " v16 h16 v-16 z m6 0 10 10";
var	PATH_TOSTART_ACTIVITY = " l8 16 l8 -16 z";

var COLOR_CURRENT_ACTIVITY = "rgb(187,0,0)";
var COLOR_COMPLETED_ACTIVITY = "rgb(0,0,153)";
var COLOR_ATTEMPTED_ACTIVITY = "rgb(187,0,0)";
var	COLOR_TOSTART_ACTIVITY = "rgb(0,153,0)";

var paper = null;
var controlFramePadding = null;
var currentActivityId = null;
var activities = [];

function Activity(index, name, id, status, url) {
	this.index = index;
	this.name = name;
	this.id = id;
	this.status = status;
	this.url = url;
	
	// 20 is the first line segment and following activities take 60 px each
	this.y = 20 + 60*index;
	
	if (id == currentActivityId) {
		// red square
		this.path = "M62 " + this.y + PATH_CURRENT_ACTIVITY;
		this.fill = COLOR_CURRENT_ACTIVITY;
		this.statusTooltip = LABEL_CURRENT_ACTIVITY;
	}  else if (status == 1) {
		// blue circle
		this.path = "M70 " + (this.y + 8) + PATH_COMPLETED_ACTIVITY;
		this.fill = COLOR_COMPLETED_ACTIVITY;
		this.statusTooltip = LABEL_COMPLETED_ACTIVITY;
	} else if (status == 2) {
		// red square with green edge
		this.path = "M62 " + this.y + PATH_ATTEMPTED_ACTIVITY;
		this.fill = COLOR_ATTEMPTED_ACTIVITY;
		this.stroke = COLOR_TOSTART_ACTIVITY;
		this.statusTooltip = LABEL_ATTEMPTED_ACTIVITY;
	} else if (status == 3) {
		// green triangle
		this.path = "M62 " + this.y + PATH_TOSTART_ACTIVITY;
		this.fill = COLOR_TOSTART_ACTIVITY;
		this.statusTooltip = LABEL_TOSTART_ACTIVITY;
	}
	
	this.transform = function(targetActivity) {
		this.path = targetActivity.path;
		this.fill = targetActivity.fill;
		this.stroke = targetActivity.stroke;
		this.status = targetActivity.status;
		this.statusTooltip = targetActivity.statusTooltip;
		
		var thisActivity = this;
		
		this.shape.animate(getShapeAttributes(thisActivity), 2000, "linear", function() {
			addEffects(thisActivity);
		});
	}
}

function SupportActivity(index, name, id, status, url) {
	this.index = index;
	this.name = name;
	this.id = id;
	this.status = status;
	this.url = url;
	
	this.y = 17 + 33*index;
	this.statusTooltip = LABEL_SUPPORT_ACTIVITY;
	
	if (status <= 2) {
		this.path = "M16 " + this.y + PATH_ATTEMPTED_ACTIVITY;
		this.fill = COLOR_ATTEMPTED_ACTIVITY;
		this.stroke = COLOR_TOSTART_ACTIVITY;
	} else if (status == 3) {
		this.path = "M16 " + this.y + PATH_TOSTART_ACTIVITY;
		this.fill = COLOR_TOSTART_ACTIVITY;
	}
	
	this.accessTransform = function(){
		this.path = "M16 " + this.y + PATH_ATTEMPTED_ACTIVITY;
		this.fill = COLOR_ATTEMPTED_ACTIVITY;
		this.stroke = COLOR_TOSTART_ACTIVITY;
		
		var thisActivity = this;
		
		this.shape.animate(getShapeAttributes(thisActivity), 2000, "linear", function() {
			addEffects(thisActivity, true);
		});
	}
}

function getShapeAttributes(activity) {
	return {
		'path'         : activity.path,
		'fill'         : activity.fill,
		'stroke' 	   : activity.stroke ? activity.stroke : "#000000",
		'stroke-width' : activity.stroke ? 2 : 1,
		'cursor'       : 'pointer'
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
				
				if (!paper) {
					// create paper only the first time
					paper = Raphael('progressBarDiv', 140, 60*result.activities.length);
					// first line on the top
					paper.path("M70 0 v20");
				}
				
				// we need this to scroll to the current activity
				var currentActivityIndex = 0;
				
				$.each(result.activities, function(activityIndex, activityData) {
					var activity = new Activity(activityIndex, activityData.name,
											    activityData.id, activityData.status, activityData.url);
					if (activity.id == currentActivityId) {
						currentActivityIndex = activityIndex;
					}
					
					var existingActivity = activities[activityIndex];
					if (existingActivity) {
						// we are refreshing existing bar, so animate if needed
						if (activity.status != existingActivity.status) {
							existingActivity.transform(activity);
						}
					} else {
						// we are drawing bar from the scratch
						activities[activityIndex] = activity;
						activity.shape = paper.path(activity.path);
						activity.shape.attr(getShapeAttributes(activity));
						addEffects(activity);
						// label underneath the shape
						paper.text(70, 43 + 60 * activityIndex, activity.name);
						if (activityIndex < result.activities.length - 1) {
							// line between activities
							paper.path("M70 " + (50 + 60 * activityIndex) + " v30");
						}
					}
				});
				
				// draw support activities if they exist
				if (result.support && !supportSeparatorRow.is(':visible')) {
					supportSeparatorRow.show();
					supportPart.height(17 + 33 * result.support.length).show();
					
					var supportPaper = Raphael('supportPart');
					
					jQuery.each(result.support, function(activityIndex, activityData) {
						var activity = new SupportActivity(activityIndex, activityData.name,
							    activityData.id, activityData.status, activityData.url);
						activity.shape = supportPaper.path(activity.path);
						activity.shape.attr(getShapeAttributes(activity));
						addEffects(activity, true);
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

// adds handlers to activity for mouse interactions
function addEffects(activity, isSupportActivity) {
	// remove any existing handlers
	if (activity.shape.glowRef) {
		activity.shape.glowRef.remove();
		activity.shape.glowRef = null;
	}
	if (activity.shape.glowOn) {
		activity.shape.unmouseover(activity.shape.glowOn);
	}
	if (activity.shape.glowOff) {
		activity.shape.unmouseout(activity.shape.glowOff);
	}
	if (activity.shape.doubleclick) {
		activity.shape.undblclick(activity.shape.doubleclick);
	}
	
	activity.shape.glowOn = function(e, x, y) {
		// add glowing effect on hover
		this.glowRef = this.glow({
			color : this.attr('fill')
		});
		
		// add tooltip
		var tooltipText = '<b>' + activity.name + '</b><br />' + activity.statusTooltip;
	    tooltipDiv.stop(true, true).css("left", 30).css("top", y + 20)
	    		  .html(tooltipText)
	    		  .delay(1000).fadeIn();
	}

	activity.shape.glowOff = function() {
		if (activity.shape.glowRef) {
			this.glowRef.remove();
			this.glowRef = null;
		}
		
		tooltipDiv.stop(true, true).fadeOut();
	}
	
	activity.shape.hover(activity.shape.glowOn, activity.shape.glowOff);
	
	// open pop up if it is a support or completed activity
	if (activity.url && (isSupportActivity || activity.status == 1 && activity.url)) {
		activity.shape.doubleclick = function(){
			if (isSupportActivity) {
				activity.accessTransform();
			}
			openActivity(activity.url);
		}
		
		activity.shape.dblclick(activity.shape.doubleclick);
	}
}