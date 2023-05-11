/**
 * This file contains methods for Activity definition and manipulation on canvas.
 */

/**
 * For colouring. See LDEV-5058
 *  CATEGORY_SYSTEM = 1;
    CATEGORY_COLLABORATION = 2;
    CATEGORY_ASSESSMENT = 3;
    CATEGORY_CONTENT = 4;
    CATEGORY_SPLIT = 5;
    CATEGORY_RESPONSE = 6;
 */
ActivityCategories = {
		'Assessment' : 3,
		'Bbb' : 2,
		'Chat' : 2,
		'Data Collection' : 6,
		'doKumaran' : 2,
		'Forum' : 2,
		'Gmap' : 2,
		'Share imageGallery' : 4,
		'Share commonCartridge' : 4,
		'MCQ' : 3,
		'Question and Answer' : 6,
		'Share resources' : 4,
		'Leaderselection' : 2,
		'Mindmap' : 6,
		'Noticeboard' : 4,
		'Notebook' : 6,
		'Peerreview' : 3,
		'Pixlr' : 4,
		'Submit file' : 3,
		'Scratchie' : 3,
		'Scribe' : 2,
		'Spreadsheet' : 4,
		'Survey' : 6,
		'Share taskList' : 4,
		'Voting' : 6,
		'Whiteboard' : 2,
		'Wiki' : 2,
		'Kaltura' : 2,
		'Zoom' : 2,
		'Resources and Forum' : 5,
		'Chat and Scribe' : 5,
		'Forum and Scribe' : 5,
		
		'grouping' : 1,
		'gate'     : 1,
		'branching': 1,
		'optional' : 1,
		'floating' : 1
},

ActivityDefs = {
		
	/**
	 * Either branching (start) or converge (end) point.
	 */
	BranchingEdgeActivity : function(id, uiid, x, y, title, readOnly, branchingType, branchingActivity) {
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (branchingActivity) {
			// branchingActivity already exists, so this is the converge point
			this.isStart = false;
			branchingActivity.end = this;
		} else {
			// this is the branching point
			this.isStart = true;
			branchingActivity = new ActivityDefs.BranchingActivity(id, uiid, this, readOnly);
			branchingActivity.branchingType = branchingType || 'chosen';
			branchingActivity.title = title || LABELS.DEFAULT_BRANCHING_TITLE;
		}
		this.branchingActivity = branchingActivity;
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.branchingProperties;
		}
		
		this.draw = ActivityDraw.branching;
		this.draw(x, y);
	},
	
	
	/**
	 * Represents a set of branches. It is not displayed on canvas, but holds all the vital data.
	 */
	BranchingActivity : function(id, uiid, branchingEdgeStart, readOnly, orderedAsc) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.start = branchingEdgeStart;
		this.readOnly = readOnly;
		this.orderedAsc = orderedAsc;
		this.branches = [];
		// mapping between groups and branches, if applicable
		this.groupsToBranches = [];
		// mapping between tool output and branches, if applicable
		this.conditionsToBranches = [];
		
		this.minOptions = 0;
		this.maxOptions = 0;
	},
	
	
	/**
	 * Represents a subsequence of activities. It is not displayed on canvas, but is the parent activity for its children.
	 */
	BranchActivity : function(id, uiid, title, branchingActivity, transitionFrom, defaultBranch) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title || (LABELS.DEFAULT_BRANCH_PREFIX + (branchingActivity.branches.length + 1));
		this.branchingActivity = branchingActivity;
		this.transitionFrom = transitionFrom;
		if (defaultBranch) {
			this.defaultBranch = true;
			// there can be only one default branch
			$.each(branchingActivity.branches, function(){
				this.defaultBranch = false;
			});
		}
	},
	
	
	/**
	 * Constructor for a Floating Activity.
	 */
	FloatingActivity : function(id, uiid, x, y) {
		DecorationDefs.Container.call(this, id, uiid, LABELS.SUPPORT_ACTIVITY_TITLE);
		
		this.draw = ActivityDraw.floatingActivity;
		this.draw(x, y);
		
		// there can only be one Floating Activity container
		layout.floatingActivity = this;
	},
	
	
	/**
	 * Constructor for a Gate Activity.
	 */
	GateActivity : function(id, uiid, x, y, title, description, readOnly, gateType, startTimeOffset,
						    gateActivityCompletionBased, gateStopAtPrecedingActivity, password) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title;
		this.description = description;
		this.readOnly = readOnly;
		this.gateType = gateType || 'permission';
		this.gateStopAtPrecedingActivity = gateStopAtPrecedingActivity;
		
		if (gateType == 'schedule') {
			var day = 24*60;
			this.offsetDay = Math.floor(startTimeOffset/day);
			startTimeOffset -= this.offsetDay * day;
			this.offsetHour = Math.floor(startTimeOffset/60);
			this.offsetMinute = startTimeOffset - this.offsetHour * 60;
			
			this.gateActivityCompletionBased = gateActivityCompletionBased;
		};
		if (gateType == 'password') {
			this.password = password;
		}
		
		// mapping between tool output and gate states ("branches"), if applicable
		this.conditionsToBranches = [];
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.gateProperties;
		}
		
		this.draw = ActivityDraw.gate;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Grouping Activity.
	 */
	GroupingActivity : function(id, uiid, x, y, title, readOnly, groupingID, groupingUIID, groupingType, groupDivide,
								groupCount, learnerCount, equalSizes, viewLearners, groups) {
		this.id = +id || null;
		this.groupingID = +groupingID || null;
		this.groupingUIID = +groupingUIID  || ++layout.ld.maxUIID;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title || LABELS.DEFAULT_GROUPING_TITLE;
		this.readOnly = readOnly;
		this.groupingType = groupingType || 'monitor';
		this.groupDivide = groupDivide || 'groups';
		this.groupCount = +groupCount || layout.conf.defaultGroupingGroupCount;
		if (groups && groups.length > this.groupCount) {
			// when opening a run sequence, groups created in monitoring can be more numerous then the original setting
			this.groupCount = groups.length;
		}
		this.learnerCount = +learnerCount || layout.conf.defaultGroupingLearnerCount;
		this.equalSizes = equalSizes || false;
		this.viewLearners = viewLearners || false;
		// either groups are already defined or create them with default names
		this.groups = groups || PropertyLib.fillNameAndUIIDList(this.groupCount, [], 'name', LABELS.DEFAULT_GROUP_PREFIX);
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.groupingProperties;
		}
		
		this.draw = ActivityDraw.grouping;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for an Optional Activity.
	 */
	OptionalActivity : function(id, uiid, x, y, title, readOnly, minOptions, maxOptions) {
		DecorationDefs.Container.call(this, id, uiid, title || LABELS.DEFAULT_OPTIONAL_ACTIVITY_TITLE);
		
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.readOnly = readOnly;
		this.minOptions = minOptions || 0;
		this.maxOptions = maxOptions || 0;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.optionalActivityProperties;
		}
		
		this.draw = ActivityDraw.optionalActivity;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Parallel (double) Activity
	 */
	ParallelActivity : function(id, uiid, learningLibraryID, x, y, title, readOnly, childActivities){
		DecorationDefs.Container.call(this, id, uiid, title);
		
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.readOnly = readOnly;
		this.learningLibraryID = +learningLibraryID;
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		if (childActivities){
			this.childActivities = childActivities;
		}
		
		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.parallelProperties;
		}
		
		this.draw = ActivityDraw.parallelActivity;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Tool Activity.
	 */
	ToolActivity : function(id, uiid, toolContentID, toolID, learningLibraryID, authorURL, x, y, title,
							readOnly, evaluation) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.toolContentID = toolContentID;
		this.toolID = +toolID;
		this.learningLibraryID = +learningLibraryID;
		this.authorURL = authorURL;
		this.title = title;
		this.readOnly = readOnly;
		this.requireGrouping = false;
		if (evaluation) {
			this.gradebookToolOutputDefinitionName = evaluation[0];
			this.gradebookToolOutputWeight = evaluation.length > 1 ? evaluation[1] : null;
		}
		
		this.transitions = {
			'from' : [],
			'to'   : []
		};
		
		// set Gradebook output name right now
		ActivityLib.getOutputDefinitions(this);

		if (!isReadOnlyMode){
			this.loadPropertiesDialogContent = PropertyDefs.toolProperties;
		}
		
		this.draw = ActivityDraw.tool;
		this.draw(x, y);
	},
	
	
	/**
	 * Constructor for a Transition
	 */
	Transition : function(id, uiid, fromActivity, toActivity, title) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.fromActivity = fromActivity;
		this.toActivity = toActivity;
		if (title) {
			// only branches have titles
			this.title = title;
			
			if (!isReadOnlyMode){
				this.loadPropertiesDialogContent = PropertyDefs.transitionProperties;
			}
		}
		
		this.draw = ActivityDraw.transition;
		this.draw();
		
		// set up references in edge activities
		fromActivity.transitions.from.push(this);
		toActivity.transitions.to.push(this);
	}
},


/**
 * Mehtods for drawing various kinds of activities.
 * They are not defined in constructors so there is a static reference, 
 * not a separate definition for each object instance.
 */
ActivityDraw = {
	
	/**
	 * Draws a Branching Activity
	 */
	branching : function(x, y) {
		if (x == undefined || y == undefined) {
			// just redraw the activity
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}
		
		if (this.items) {
			this.items.remove();
		}
		
		// make the icon more centred
		x = GeneralLib.snapToGrid(x - 40) + 40;
		y = GeneralLib.snapToGrid(y - 20) + 20;

		// create activity SVG elements
		var shape = paper.circle(x + 20, y + 20, 20)
						 .addClass('svg-branching svg-shadow svg-branching-' + (this.isStart ? 'start' : 'end')),
			icon = ActivityLib.getActivityIcon(this.isStart ? 'branching' : 'branchingEnd');
		icon.select('svg').attr({
			'x'     : x + 5,
			'y'     : y + 5,
			'width' : '30px',
			'height': '30px'
		});
	
		this.items = paper.g(shape, icon);
		this.items.addClass('svg-activity svg-activity-branching');
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		if (this.isStart) {
			// these are needed in monitoring
			this.items.attr({
				'uiid'   : this.branchingActivity.uiid,
				'data-x' : x,
				'data-y' : y,
				'data-width' : 40,
				'data-height': 40
			});
		}
		this.items.shape = shape;
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws a Floating (support) Activity container
	 */
	floatingActivity : function(x, y, ignoredParam1, ignoredParam2, childActivities) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}
		
		x = GeneralLib.snapToGrid(x);
		y = GeneralLib.snapToGrid(y);
		
		// either check what children are on canvas or use the priovided parameter
		if (childActivities) {
			this.childActivities = childActivities;
		}
		
		if (this.childActivities && this.childActivities.length > 0) {
			// draw one by one, horizontally
			var activityX = x + layout.conf.containerActivityPadding,
				allElements = Snap.set(),
				floatingActivity = this,
				box = this.items.getBBox();
			$.each(this.childActivities, function(orderID){
				this.parentActivity = floatingActivity;
				this.orderID = orderID;
				var childBox = this.items.shape.getBBox();
				this.draw(activityX, y + Math.max(layout.conf.containerActivityPadding + 10, (box.height - childBox.height)/2), true);
				childBox = this.items.shape.getBBox();
				activityX = childBox.x2 + layout.conf.containerActivityChildrenPadding;
				allElements.push(this.items.shape);
			});
			// area containing all drawn child activities
			box = allElements.getBBox();
			
			this.drawContainer(x, y,
							   box.x2 + layout.conf.containerActivityPadding,
							   box.y2 + layout.conf.containerActivityPadding);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight);
		}
		
		this.items.data('parentObject', this);
		this.items.addClass('svg-activity svg-activity-floating svg-shadow');
	},
	
	
	/**
	 * Draws a Gate activity
	 */
	gate : function(x, y) {
		
		if (x == undefined || y == undefined) {
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}
		
		x = Math.round(x);
		y = Math.round(y);

		if (this.items) {
			this.items.remove();
		}
		
		x = GeneralLib.snapToGrid(x);
		// make the icon more centred
		y = GeneralLib.snapToGrid(y);
		
		// create activity SVG elements
		var shape = ActivityLib.getActivityIcon('gate');
		shape.select('svg').attr({
			'x'     : x,
			'y'     : y,
			'width' : '40px',
			'height': '40px'
		});

		this.items = paper.g(shape);
		this.items.addClass('svg-activity svg-activity-gate svg-shadow');
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		// these are needed in monitoring
		this.items.attr({
			'uiid'   : this.uiid,
			'data-x' : x,
			'data-y' : y,
			'data-width' : 40,
			'data-height': 40
		});
		this.items.shape = shape;
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws a Grouping activity
	 */
	grouping : function(x, y) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}
		
		if (this.items) {
			this.items.remove();
		}
		
		x = GeneralLib.snapToGrid(x);
		y = GeneralLib.snapToGrid(y);
		
		// create activity SVG elements
		var curve = layout.activity.borderCurve,
			width = layout.activity.width,
			height = layout.activity.height,			
			shapePath = ' M ' + (x + curve) + ' ' + y + ' h ' + (width - 2 * curve) + ' q ' + curve + ' 0 ' + curve + ' ' + curve +
						' v ' + (height - 2 * curve) + ' q 0 ' + curve + ' ' + -curve + ' ' + curve + 
						' h ' + (-width + 2 * curve) + ' q ' + -curve + ' 0 ' + -curve + ' ' + -curve +
						' v ' + (-height + 2 * curve) + ' q 0 ' + -curve + ' ' + curve + ' ' + -curve,
			shape = paper.path(shapePath)
						 .addClass('svg-tool-activity-background svg-shadow'),
			shapeBorder = paper.path(shapePath)
							   .addClass('svg-tool-activity-border'),
			// check for icon in the library
			icon = ActivityLib.getActivityIcon('grouping'),
			label = ActivityLib.getActivityTitle(this.title, x, y);
			
		icon.select('svg').attr({
			'x'     : x + 20,
			'y'     : y + 15,
			'width' : '50px',
			'height': '50px'
		});
		
		this.items = paper.g(shape, shapeBorder, label, icon);
		this.items.attr({
			'uiid'   : this.uiid,
			'data-x' : x,
			'data-y' : y,
			'data-width' : width,
			'data-height': height
		});
		
		this.items.addClass('svg-activity svg-activity-grouping');
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		this.items.shape = shape;
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws an Optional Activity container
	 */
	optionalActivity : function(x, y, ignoredParam1, ignoredParam2, childActivities) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}
		
		x = GeneralLib.snapToGrid(x);
		y = GeneralLib.snapToGrid(y);
		
		// either check what children are on canvas or use the priovided parameter
		if (childActivities) {
			this.childActivities = childActivities;
		}
		
		var width = null,
			height = null;
			
		if (this.childActivities && this.childActivities.length > 0) {
			// draw one by one, vertically
			var activityY = y + layout.conf.containerActivityPadding + 10,
				allElements = Snap.set(),
				optionalActivity = this,
				box = this.items.getBBox(),
				boxWidth = box.width;
			$.each(this.childActivities, function(orderID){
				this.parentActivity = optionalActivity;
				this.orderID = orderID + 1;
				// for some reason, this.items.getBBox() can't be used here
				var childBox = this.items.shape.getBBox();
				this.draw(x + Math.max(layout.conf.containerActivityPadding, (boxWidth - childBox.width)/2), activityY, true);
				childBox = this.items.shape.getBBox();
				activityY = childBox.y2 + layout.conf.containerActivityChildrenPadding;
				allElements.push(this.items.shape);
			});
			// area containing all drawn child activities
			box = allElements.getBBox();
			
			width = box.x2 + layout.conf.containerActivityPadding - x;
			height = box.y2 + layout.conf.containerActivityPadding - y;
			
			this.drawContainer(x, y, x + width, y + height,
							  layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		} else {
			width  = layout.conf.containerActivityEmptyWidth;
			height = layout.conf.containerActivityEmptyHeight;
	
			this.drawContainer(x, y, x + width, y + height,
							   layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		}
		
		if (!isReadOnlyMode){
			// allow transition drawing and other activity behaviour
			this.items.unmousedown().mousedown(HandlerActivityLib.activityMousedownHandler);
		}
		
		this.items.data('parentObject', this);
		this.items.addClass('svg-activity svg-activity-optional svg-shadow');
		// these are needed in monitoring
		this.items.attr({
			'uiid'   : this.uiid,
			'data-x' : x,
			'data-y' : y,
			'data-width' : width,
			'data-height': height
		});
	},
	
	
	/**
	 * Draws a Parallel (double) Activity container
	 */
	parallelActivity : function(x, y) {
		// if no new coordinates are given, just redraw the activity or give default value
		if (x == undefined) {
			x = this.items ? this.items.getBBox().x : 0;
		}
		if (y == undefined) {
			y = this.items ? this.items.getBBox().y : 0;
		}
		
		x = GeneralLib.snapToGrid(x);
		y = GeneralLib.snapToGrid(y);
				
		var width = null,
			height = null;
			
		if (this.childActivities && this.childActivities.length > 0) {
			// draw one by one, vertically
			var activityY = y + layout.conf.containerActivityPadding + 10,
				allElements = Snap.set(),
				optionalActivity = this;
			$.each(this.childActivities, function(orderID){
				this.parentActivity = optionalActivity;
				this.orderID = orderID + 1;
				this.draw(x + layout.conf.containerActivityPadding, activityY, true);
				activityY = this.items.getBBox().y2 + layout.conf.containerActivityChildrenPadding;
				allElements.push(this.items.shape);
			});
			// area containing all drawn child activities
			var box = allElements.getBBox();
			
			width = box.x2 + layout.conf.containerActivityPadding - x;
			height = box.y2 + layout.conf.containerActivityPadding - y;
			
			this.drawContainer(x, y, x + width, y + height,
							  layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		} else {
			width  = layout.conf.containerActivityEmptyWidth;
			height = layout.conf.containerActivityEmptyHeight;
			
			this.drawContainer(x, y, x + width, y + height,
							   layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		}
		
		if (this.grouping) {
			ActivityLib.addGroupingEffect(this);
		}
		
		if (!isReadOnlyMode){
			// allow transition drawing and other activity behaviour
			this.items.unmousedown().mousedown(HandlerActivityLib.activityMousedownHandler);
		}
		
		this.items.data('parentObject', this);
		this.items.addClass('svg-activity svg-activity-parallel svg-shadow');
		// these are needed in monitoring
		this.items.attr({
			'uiid'   : this.uiid,
			'data-x' : x,
			'data-y' : y,
			'data-width' : width,
			'data-height': height
		});
	},
	
	
	/**
	 * Draws a Tool activity
	 */
	tool : function(x, y, skipSnapToGrid) {
		if (x == undefined || y == undefined) {
			// if no new coordinates are given, just redraw the activity
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}
		
		if (this.items) {
			this.items.remove();
		}
		
		if (!skipSnapToGrid) {
			x = GeneralLib.snapToGrid(x);
			y = GeneralLib.snapToGrid(y);
		}
		
		// create activity SVG elements
		var curve = layout.activity.borderCurve,
			width = layout.activity.width,
			height = layout.activity.height,
			bannerPath = 'M ' + (x + curve) + ' ' + (y + height) + ' q ' + -curve + ' 0 ' + -curve + ' ' + -curve + 
						 ' v ' + (-height + 2 * curve) + ' q 0 ' + -curve + ' ' + curve + ' ' + -curve,
			// by default the wide banner is displayed,
			// but when there are learners in monitoring, the narrow one is shown instead
			bannerWidePath = bannerPath + ' h ' + layout.activity.bannerWideWidth + ' v ' + height + ' z',
			bannerNarrowPath = bannerPath + ' h ' + layout.activity.bannerNarrowWidth + ' v ' + height + ' z',
			bannerWide = paper.path(bannerWidePath)
						  .addClass('svg-tool-banner-wide svg-tool-activity-category-' + layout.toolMetadata[this.learningLibraryID].activityCategoryID),
			bannerNarrow = paper.path(bannerNarrowPath)
						  .addClass('svg-tool-banner-narrow svg-tool-activity-category-' + layout.toolMetadata[this.learningLibraryID].activityCategoryID),
			shapePath = bannerPath + ' h ' + (width - 2 * curve) + ' q ' + curve + ' 0 ' + curve + ' ' + curve +
						' v ' + (height - 2 * curve) + ' q 0 ' + curve + ' ' + -curve + ' ' + curve + ' z',
			shape = paper.path(shapePath)
						 .addClass('svg-tool-activity-background ' + (this.grouping ? '' : 'svg-shadow')),
			shapeBorder = paper.path(shapePath)
							 .addClass('svg-tool-activity-border' + (this.requireGrouping ? '-require-grouping' : '')),
			label = ActivityLib.getActivityTitle(this.title, x, y),
			icon = ActivityLib.getActivityIcon(this.learningLibraryID);
				
		$(bannerNarrow.node).hide();
		this.items = paper.g(shape, bannerWide, bannerNarrow, shapeBorder, label);
		
		if (icon) {
			icon.select('svg').attr({
				'x'     : x + 15,
				'y'     : y + 20,
				'width' : '40px',
				'height': '40px'
			});
			this.items.add(icon);	
		}
		
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		// these are needed in monitoring
		this.items.attr({
			'uiid'   : this.uiid,
			'data-x' : x,
			'data-y' : y,
			'data-width' : width,
			'data-height': height
		});
        this.items.addClass('svg-activity svg-activity-tool');
		this.items.shape = shape;
		
		if (this.grouping) {
			ActivityLib.addGroupingEffect(this);
		}
		
		ActivityLib.activityHandlersInit(this);
	},
	
	
	/**
	 * Draws a Transition
	 */
	transition : function() {
		if (this.items) {
			this.items.remove();
		}
		this.items = paper.g();
		
		var isBranching = (this.fromActivity instanceof ActivityDefs.BranchingEdgeActivity && this.fromActivity.isStart) || 
					      (this.toActivity instanceof ActivityDefs.BranchingEdgeActivity   && !this.toActivity.isStart),
			points = ActivityLib.findTransitionPoints(this.fromActivity, this.toActivity),
			curve = layout.transition.curve,
			straightLineThreshold = 2 * curve + 2;
		
		if (points) {
			var path = Snap.format('M {startX} {startY}', points),	
				horizontalDelta = points.endX - points.startX,
				verticalDelta = points.endY - points.startY;
	
			
			// if activities are too close for curves, draw a straight line instead of bezier
			if (isBranching || Math.abs(horizontalDelta) < straightLineThreshold || Math.abs(verticalDelta) < straightLineThreshold) {
				path += Snap.format(' L {endX} {endY}', points);
				points.arrowAngle = 90 + Math.atan2(points.endY - points.startY, points.endX - points.startX) * 180 / Math.PI;
			} else {
				// adjust according to whether it is right/left and down/up
				var horizontalModifier = horizontalDelta > 0 ? 1 : -1,
					verticalModifier = verticalDelta > 0 ? 1 : -1;
					
				switch (points.direction) {
					case 'up' :
					case 'down' :
						
						// go to almost the middle of the activities
						path += ' V ' + (points.middleY - verticalModifier * curve);
						// first curve
						path += ' q 0 ' + verticalModifier * curve + ' ';
						path += horizontalModifier * curve + ' ' + verticalModifier * curve;
						// straight long line
						path += ' l ' + (points.endX - points.startX - 2 * horizontalModifier * curve) + ' 0';
						// second curve
						path += ' q ' + horizontalModifier * curve + ' 0 ' + horizontalModifier * curve + ' ' + verticalModifier * curve;
					
						break;
						
					case 'left' :
					case 'right' :
						
						path += ' H ' + (points.middleX - horizontalModifier * curve);
						path += ' q ' + horizontalModifier * curve + ' 0 ';
						path += horizontalModifier * curve + ' ' + verticalModifier * curve;
						path += ' l 0 ' + (points.endY - points.startY - 2 * verticalModifier * curve);
						path += ' q 0 ' + verticalModifier * curve + ' ' + horizontalModifier * curve + ' ' + verticalModifier * curve;
						
						break;
					}

				// finish the path
				path += Snap.format(' L {endX} {endY}', points);
			}
			
			this.items.shape = paper.path(path).addClass('svg-transition');
			this.items.append(this.items.shape);
			
			var dot = paper.circle(points.startX, points.startY, layout.transition.dotRadius).addClass('svg-transition-element'),
				side = layout.transition.arrowLength,
				triangle = paper.polygon(0, 0, side, 2 * side, -side, 2 * side)
								.addClass('svg-transition-element')
								.transform(Snap.format('translate({endX} {endY}) rotate({arrowAngle})', points));
			this.items.append(dot);
			this.items.append(triangle);
		
			
			
			this.items.attr('uiid', this.uiid);
			if (this.title) {
				// adjust X & Y, so the label does not overlap with the transition;
				var label = paper.text(points.middleX, points.middleY, this.title)
						   	     .attr(layout.defaultTextAttributes);
					labelBox = label.getBBox(),
					labelBackground = paper.rect(labelBox.x, labelBox.y, labelBox.width, labelBox.height)
										   .attr({
											   	'stroke' : 'none',
											   	'fill'   : 'white'
												});
				label = paper.g(label, labelBackground);
				GeneralLib.toBack(labelBackground);
				this.items.append(label);
			}
	
			GeneralLib.toBack(this.items);
			
			// region annotations could cover grouping effect
			$.each(layout.regions, function(){
				GeneralLib.toBack(this.items);
			});
		}
		
		this.items.data('parentObject', this);
		
		if (!isReadOnlyMode){
			this.items.attr('cursor', 'pointer')
					  .mousedown(HandlerTransitionLib.transitionMousedownHandler)
					  .click(HandlerLib.itemClickHandler);
		}
	}
},



/**
 * Contains utility methods for Activity manipulation.
 */
ActivityLib = {
		
	/**
	 * Make a new activity fully functional on canvas.
	 */
	activityHandlersInit : function(activity) {
		activity.items.data('parentObject', activity);
		
		if (isReadOnlyMode) {
			if (activitiesOnlySelectable) {
				activity.items.attr('cursor', 'pointer')
				  			  .click(HandlerLib.itemClickHandler);
			}
		} else {
			// set all the handlers
			activity.items.attr('cursor', 'pointer')
						  .mousedown(HandlerActivityLib.activityMousedownHandler)
	  		      		  .click(HandlerActivityLib.activityClickHandler);
			
			if (activity instanceof ActivityDefs.BranchingEdgeActivity
					&& activity.branchingActivity.end) {
				// highligh branching edges on hover
				
				activity.branchingActivity.start.items.hover(HandlerActivityLib.branchingEdgeMouseoverHandler,
															 HandlerActivityLib.branchingEdgeMouseoutHandler);
				activity.branchingActivity.end.items.hover(HandlerActivityLib.branchingEdgeMouseoverHandler,
						 HandlerActivityLib.branchingEdgeMouseoutHandler);
			}
		}

	},

	
	
	/**
	 * Adds branching activity when user draws an extra outbout transition from.
	 */
	addBranching : function(fromActivity, toActivity1) {
		// find the other toActivity
		var existingTransition = fromActivity.transitions.from[0],
			toActivity2 = existingTransition.toActivity,
			branchingEdgeStart = null,
			branchingEdgeEnd = null,
			convergeActivity1 = toActivity1,
		    convergeActivity2 = toActivity2;
		// find converge activity of the new branch
		while (convergeActivity1.transitions.from.length > 0) {
			convergeActivity1 = convergeActivity1.transitions.from[0].toActivity;
		};
		
		if (toActivity2 instanceof ActivityDefs.BranchingEdgeActivity && toActivity2.isStart) {
			// there is already a branching activity, reuse existing items
			branchingEdgeStart = toActivity2;
			branchingEdgeEnd = toActivity2.branchingActivity.end;
		} else {
			// add new branching
			ActivityLib.removeTransition(existingTransition);
			
			// calculate position of branching point
			var branchPoints1 = ActivityLib.findTransitionPoints(fromActivity, toActivity1),
			    branchPoints2 = ActivityLib.findTransitionPoints(fromActivity, toActivity2),
			    branchEdgeStartX = branchPoints1.middleX + (branchPoints2.middleX - branchPoints1.middleX)/2,
			    branchEdgeStartY = branchPoints1.middleY + (branchPoints2.middleY - branchPoints1.middleY)/2,
			    branchingEdgeStart = new ActivityDefs.BranchingEdgeActivity(null, null, branchEdgeStartX,
			    		branchEdgeStartY, null, false, null, null);
			layout.activities.push(branchingEdgeStart);
			
			// find last activities in subsequences and make an converge point between them
			while (convergeActivity2.transitions.from.length > 0) {
				convergeActivity2 = convergeActivity2.transitions.from[0].toActivity;
			};

			var convergePoints = ActivityLib.findTransitionPoints(convergeActivity1, convergeActivity2), 
				branchingEdgeEnd = new ActivityDefs.BranchingEdgeActivity(null, null, convergePoints.middleX,
					convergePoints.middleY, null, false, null, branchingEdgeStart.branchingActivity);
			layout.activities.push(branchingEdgeEnd);
			
			// draw all required transitions
			ActivityLib.addTransition(fromActivity, branchingEdgeStart);
			ActivityLib.addTransition(branchingEdgeStart, toActivity2);
			ActivityLib.addTransition(convergeActivity2, branchingEdgeEnd);
		}

		ActivityLib.addTransition(branchingEdgeStart, toActivity1);
		ActivityLib.addTransition(convergeActivity1, branchingEdgeEnd);
		GeneralLib.setModified(true);
	},
	

	
	/**
	 * Adds visual grouping effect on an activity.
	 */
	addGroupingEffect : function(activity) {
		// do not draw twice if it already exists
		if (!activity.items.groupingEffect) {
			var shape = activity.items.shape,
				activityBox = activity.items.getBBox();
			
			activity.items.groupingEffect = paper.rect(
						activityBox.x + layout.conf.groupingEffectPadding,
						activityBox.y + layout.conf.groupingEffectPadding,
						activityBox.width,
						activityBox.height,
						5, 5)
				   .addClass('svg-tool-activity-border svg-tool-activity-border-grouped svg-shadow');
			
			activity.items.prepend(activity.items.groupingEffect);
			
			// region annotations could cover grouping effect
			$.each(layout.regions, function(){
				GeneralLib.toBack(this.items);
			});
		}
	},
	
	
	/**
	 * Adds visual select effect around an activity.
	 */
	addSelectEffect : function (object, markSelected) {
		// do not draw twice
		if (!object.items.selectEffect) {
			// different effects for different types of objects
			if (object instanceof DecorationDefs.Region) {
				object.items.shape.attr({
					'stroke'           : layout.colors.selectEffect,
					'stroke-dasharray' : '5,3'
				});
				object.items.selectEffect = true;
				
				if (!isReadOnlyMode) {
					object.items.resizeButton.attr('display', null);
					GeneralLib.toFront(object.items.resizeButton);
					// also select encapsulated activities
					var childActivities = DecorationLib.getChildActivities(object.items.shape);
					if (childActivities.length > 0) {
						object.items.fitButton.attr('display', null);
						
						$.each(childActivities, function(){
							if (!this.parentActivity || !(this.parentActivity instanceof DecorationDefs.Container)) {
								ActivityLib.addSelectEffect(this, false);
							}
						});
					}
				}
			} else if (object instanceof ActivityDefs.Transition) {
				// show only if Transition is selectable, i.e. is a branch, has a title
				if (object.loadPropertiesDialogContent) {
					object.items.attr({
						'stroke' : layout.colors.selectEffect,
						'fill'   : layout.colors.selectEffect
					 });
					
					object.items.selectEffect = true;
				}
			} else {
				// this goes for ActivityDefs and Labels
				var box = object.items.getBBox();
				
				// a simple rectange a bit wider than the actual activity boundaries
				object.items.selectEffect = paper.path(Snap.format('M {x} {y} h {width} v {height} h -{width} z',
								   {
									'x'      : box.x - layout.conf.selectEffectPadding,
									'y'      : box.y - layout.conf.selectEffectPadding,
									'width'  : box.width + 2*layout.conf.selectEffectPadding,
									'height' : box.height + 2*layout.conf.selectEffectPadding
								   }))
							.attr({
									'stroke'           : layout.colors.selectEffect,
									'stroke-dasharray' : '5,3',
									'fill' : 'none'
								});
				
				// if it's "import part" select children activities
				if (activitiesOnlySelectable) {
					if (object instanceof ActivityDefs.BranchingEdgeActivity) {
						if (object.isStart){
							ActivityLib.addSelectEffect(object.branchingActivity.end);
							
							$.each(object.branchingActivity.branches, function(){
								var transition = this.transitionFrom;
								while (transition) {
									var activity = transition.toActivity;
									if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
										return true;
									}
									ActivityLib.addSelectEffect(activity);
									transition = activity.transitions.from.length > 0 ? activity.transitions.from[0] : null;
								}
							});
						} else {
							ActivityLib.addSelectEffect(object.branchingActivity.start);
						}
					} else if (object instanceof DecorationDefs.Container){
						$.each(object.childActivities, function(){
							ActivityLib.addSelectEffect(this);
						});
					}
				}
			}
			
			// make it officially marked?
			if (markSelected && object.items.selectEffect){
				layout.selectedObject = object;
				// show the properties dialog for the selected object
				if (object.loadPropertiesDialogContent) {
					PropertyLib.openPropertiesDialog(object);
				}
				
				/* This will become useful if weights dialog get non-modal
				if (object instanceof ActivityDefs.ToolActivity
						&& object.gradebookToolOutputDefinitionName
						&& layout.weightsDialog.hasClass('in')) {
					$('tbody tr', layout.weightsDialog).each(function(){
						if ($(this).data('activity') == object) {
							$(this).addClass('selected');
						} else {
							$(this).removeClass('selected');
						}
					});
				}
				*/
			}
		}
	},
	
	
	/**
	 * Draws a transition between two activities.
	 */
	addTransition : function(fromActivity, toActivity, redraw, id, uiid, branchData) {
		// check if a branching's start does not connect with another branching's end
		if (fromActivity instanceof ActivityDefs.BranchingEdgeActivity
				&& toActivity instanceof ActivityDefs.BranchingEdgeActivity
				&& fromActivity.isStart && !toActivity.isStart
				&& fromActivity.branchingActivity != toActivity.branchingActivity) {
			layout.infoDialog.data('show')(LABELS.CROSS_BRANCHING_ERROR);
			return;
		}
		
		// if a child activity was detected, use the parent activity as the target
		if (toActivity.parentActivity && toActivity.parentActivity instanceof DecorationDefs.Container){
			toActivity = toActivity.parentActivity;
		}
		if (fromActivity.parentActivity && fromActivity.parentActivity instanceof DecorationDefs.Container){
			fromActivity = fromActivity.parentActivity;
		}
		// no transitions to/from support activities
		if (toActivity instanceof ActivityDefs.FloatingActivity
			|| fromActivity instanceof ActivityDefs.FloatingActivity){
			layout.infoDialog.data('show')(LABELS.SUPPORT_TRANSITION_ERROR);
			return;
		}
		
		// only converge points are allowed to have few inbound transitions
		if (!redraw
				&& toActivity.transitions.to.length > 0
				&& !(toActivity instanceof ActivityDefs.BranchingEdgeActivity && !toActivity.isStart)) {
			layout.infoDialog.data('show')(LABELS.TRANSITION_TO_EXISTS_ERROR);
			return;
		}

		// check for circular sequences
		var activity = fromActivity;
		do {
			if (activity.transitions && activity.transitions.to.length > 0) {
				activity = activity.transitions.to[0].fromActivity;
			} else if (activity.branchingActivity && !activity.isStart) {
				activity = activity.branchingActivity.start;
			} else {
				activity = null;
			}
			
			if (toActivity == activity) {
				layout.infoDialog.data('show')(LABELS.CIRCULAR_SEQUENCE_ERROR);
				return;
			}
		} while (activity);

		// user chose to create outbound transition from an activity that already has one
		if (!redraw
				&& fromActivity.transitions.from.length > 0
				&& !(fromActivity instanceof ActivityDefs.BranchingEdgeActivity && fromActivity.isStart)) {
			if (confirm(LABELS.BRANCHING_CREATE_CONFIRM)) {
				ActivityLib.addBranching(fromActivity, toActivity);
			}
			return;
		}
		
		// start building the transition
		
		// branchData can be either an existing branch or a title for the new branch
		var branch = branchData && branchData instanceof ActivityDefs.BranchActivity ? branchData : null,
			transition = null;
		// remove the existing transition
		$.each(fromActivity.transitions.from, function(index) {
			if (this.toActivity == toActivity) {
				id = this.id;
				uiid = this.uiid;
				transition = this;
				if (!branch){
					branch = this.branch;
				}

				return false;
			}
		});
		
		if (!branch && fromActivity instanceof ActivityDefs.BranchingEdgeActivity && fromActivity.isStart) {
			// if a title was provided, try to find the branch based on this information
			$.each(fromActivity.branchingActivity.branches, function(){
				if (branchData == this.title) {
					branch = this;
					return false;
				}
			});
			if (!branch) {
				// create a new branch
				branch = new ActivityDefs.BranchActivity(null, null, branchData, fromActivity.branchingActivity, false);
			}
		}
		
		if (transition) {
			ActivityLib.removeTransition(transition, redraw);
		}
		
		// finally add the new transition
		transition = new ActivityDefs.Transition(id, uiid, fromActivity, toActivity,
						 branch ? branch.title : null);

		if (branch) {
			// set the corresponding branch (again)
			branch.transitionFrom = transition;
			transition.branch = branch;
			fromActivity.branchingActivity.branches.push(branch);
			if (fromActivity.branchingActivity.branches.length == 1) {
				branch.defaultBranch = true;
			}
		}
		
		
		// after adding the transition, check for self-nested branching
		activity = fromActivity;
		var branchingActivity = null;
		// find the top-most enveloping branching activity, if any
		do {
			if (activity.transitions && activity.transitions.to.length > 0) {
				activity = activity.transitions.to[0].fromActivity;
				
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					if (activity.isStart) {
						// found the top branching the activity belongs to
						branchingActivity = activity.branchingActivity;
					} else {
						// jump over nested branching
						activity = activity.branchingActivity.start;
					}
				}
			} else {
				activity = null;
			}
		} while (activity);
		
		
		if (branchingActivity) {
			// look for all nested branchings
			var nestedBranchings = ActivityLib.findNestedBranching(branchingActivity);
			// check each of them
			$.each(nestedBranchings, function(){
				var branching = this;
				// check if one branching's end does not match with another branching's start
				$.each(branching.end.transitions.to, function(){
					// crawl from end to start
					var activity = this.fromActivity;
					while (activity) {
						if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
							if (activity.isStart) {
								// this branching's end matches with its own start, all OK
								if (activity.branchingActivity == branching) {
									break;
								}
								// this branching's end does not match with own start, error
								layout.infoDialog.data('show')(LABELS.CROSS_BRANCHING_ERROR);
								// remove the just added transition
								ActivityLib.removeTransition(transition);
								// tell the outer iteration loop to quit
								transition = null;
								return false;
							}
							// a nested branching encountered when crawling, just jump over it
							activity = activity.branchingActivity.start;
						}
						// keep crawling
						if (activity.transitions && activity.transitions.to.length > 0) {
							activity = activity.transitions.to[0].fromActivity;
						} else {
							activity = null;
						}
					}
				});
				
				if (!transition) {
					// there was an error, do not carry on
					return false;
				}
			});
		}
		
		GeneralLib.setModified(true);
		return transition;
	},
		
	adjustTransitionPoint : function(bottomLimit, topLimit, target) {
		bottomLimit = Math.round(bottomLimit);
		topLimit = Math.round(topLimit);
		target = Math.round(target);
		// find a good point inside the grid, then make sure it is within bounds
		return Math.max(bottomLimit + layout.transition.adjustStep, Math.min(topLimit - layout.transition.adjustStep,
				Math.floor(target / layout.transition.adjustStep) * layout.transition.adjustStep));
	},

	/**
	 * It is run from authoringConfirm.jsp
	 * It closes the dialog with activity authoring 
	 */
	closeActivityAuthoring : function(dialogID){
		$("#" + dialogID).off('hide.bs.modal').on('hide.bs.modal', function(){
			$('iframe', this).attr('src', null);
		}).modal('hide');
	},
	
	
	/**
	 * Drop the dragged activity on the canvas.
	 */
	dropActivity : function(activity, x, y) {
		if (!(activity instanceof ActivityDefs.OptionalActivity || activity instanceof ActivityDefs.FloatingActivity)) {
			// check if it was removed from an Optional or Floating Activity
			if (activity.parentActivity && activity.parentActivity instanceof DecorationDefs.Container) {
				var existingChildActivities = activity.parentActivity.childActivities,
					childActivities = DecorationLib.getChildActivities(activity.parentActivity.items.shape);
				if ($.inArray(activity, childActivities) == -1) {
					if (activity.readOnly || activity.parentActivity.readOnly) {
						// put the activity back
						activity.parentActivity.childActivities = existingChildActivities;
						
						layout.infoDialog.data('show')(LABELS.LIVEEDIT_READONLY_MOVE_PARENT_ERROR);
						return false;
					}
					
					activity.parentActivity.draw();
					ActivityLib.redrawTransitions(activity.parentActivity);
					activity.parentActivity = null;
				}
			}
			
			// check if it was added to an Optional or Floating Activity
			var container = layout.floatingActivity
							&& Snap.path.isPointInsideBBox(layout.floatingActivity.items.getBBox(),x,y)
							? layout.floatingActivity : null;
			if (!container) {
				$.each(layout.activities, function(){
					if (this instanceof ActivityDefs.OptionalActivity
						&& Snap.path.isPointInsideBBox(this.items.getBBox(),x,y)) {
						container = this;
						return false;
					}
				});
			}
			if (container) {
				// system activities can not be added to optional and support activities
				if (activity instanceof ActivityDefs.GateActivity
					|| activity instanceof ActivityDefs.GroupingActivity
					|| activity instanceof ActivityDefs.BranchingEdgeActivity){
					layout.infoDialog.data('show')(LABELS.ACTIVITY_IN_CONTAINER_ERROR);
					return false;
				}
				if (activity.readOnly || container.readOnly) {
					layout.infoDialog.data('show')(LABELS.LIVEEDIT_READONLY_ACTIVITY_ERROR);
					return false;
				}
				
				$.each(activity.transitions.from, function(){
					ActivityLib.removeTransition(this);
				});
				$.each(activity.transitions.to, function(){
					ActivityLib.removeTransition(this);
				});

				// for properties dialog to reload
				ActivityLib.removeSelectEffect(container);
				
				// check if the activity is already detected by the container
				// if not, add it manually
				var childActivities = DecorationLib.getChildActivities(container.items.shape);
				if ($.inArray(activity, container.childActivities) == -1) {
					childActivities.push(activity);
				}
				container.draw(null, null, null, null, childActivities);
				ActivityLib.redrawTransitions(container);
			}
		}
		
		ActivityLib.redrawTransitions(activity);
		
		$.each(layout.regions, function(){
			// redraw all annotation regions so they are pushed to back
			this.draw();
		});
		
		GeneralLib.setModified(true);
		return true;
	},
	
	
	findNestedBranching : function(branchingActivity) {
		var nestedBranching = [];
		$.each(branchingActivity.branches, function(){
			var activity = this.transitionFrom.toActivity;
			while (activity) {
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					if (activity.branchingActivity == branchingActivity){
						break;
					}
					if (nestedBranching.indexOf(activity.branchingActivity) == -1) {
						nestedBranching.push(activity.branchingActivity);
					}
					if (activity.isStart) {
						nestedBranching = nestedBranching.concat(ActivityLib.findNestedBranching(activity.branchingActivity));
						activity = activity.branchingActivity.end;
					}
				}
				
				if (activity.transitions && activity.transitions.from.length > 0) {
					activity = activity.transitions.from[0].toActivity;
				} else {
					activity = null;
				}
			}
		});
		
		return nestedBranching;
	},
	
	/**
	 * Calculates start, middle and end points of a line between two activities. 
	 */
	findTransitionPoints : function(fromActivity, toActivity) {
		var fromActivityBox = fromActivity.items.getBBox(),
			toActivityBox = toActivity.items.getBBox(),
			// vertical direction takes priority
			// horizontal is used only if activities are in the same line
			direction =    (fromActivityBox.y >= toActivityBox.y && fromActivityBox.y <= toActivityBox.y2) 
					    || (fromActivityBox.y2 >= toActivityBox.y && fromActivityBox.y2 <= toActivityBox.y2)
					    || (toActivityBox.y >= fromActivityBox.y && toActivityBox.y <= fromActivityBox.y2) 
					    || (toActivityBox.y2 >= fromActivityBox.y && toActivityBox.y2 <= fromActivityBox.y2)
					    ? 'horizontal' : 'vertical',
			points = null;

		if (direction === 'vertical') {
			if (fromActivityBox.cy < toActivityBox.cy) {
				points = {
						'startX'    : ActivityLib.adjustTransitionPoint(fromActivityBox.x, fromActivityBox.x2, toActivityBox.x + toActivityBox.width / 2),
						'startY'    : fromActivityBox.y2 + layout.transition.dotRadius,
						'endY'      : toActivityBox.y,
						'direction' : 'down',
						'arrowAngle': 180
					};
				points.endX = ActivityLib.adjustTransitionPoint(toActivityBox.x, toActivityBox.x2, points.startX);
			} else {
				points = {
						'startX'    : ActivityLib.adjustTransitionPoint(fromActivityBox.x, fromActivityBox.x2, toActivityBox.x + toActivityBox.width / 2),
						'startY'    : fromActivityBox.y - layout.transition.dotRadius,
						'endY'      : toActivityBox.y2,
						'direction' : 'up',
						'arrowAngle': 0
					};
				points.endX = ActivityLib.adjustTransitionPoint(toActivityBox.x, toActivityBox.x2, points.startX);
			}
		} else {
			if (fromActivityBox.cx < toActivityBox.cx) {
				points = {
						'startX'    : fromActivityBox.x2 + layout.transition.dotRadius,
						'startY'    : ActivityLib.adjustTransitionPoint(fromActivityBox.y, fromActivityBox.y2, toActivityBox.y + toActivityBox.height / 2),
						'endX'      : toActivityBox.x,
						'direction' : 'right',
						'arrowAngle': 90
					};
				points.endY = ActivityLib.adjustTransitionPoint(toActivityBox.y, toActivityBox.y2, points.startY);
			} else {
				// left
				points = {
						'startX'    : fromActivityBox.x - layout.transition.dotRadius,
						'startY'    : ActivityLib.adjustTransitionPoint(fromActivityBox.y, fromActivityBox.y2, toActivityBox.y + toActivityBox.height / 2),
						'endX'      : toActivityBox.x2,
						'direction' : 'left',
						'arrowAngle': 270
					};
				points.endY = ActivityLib.adjustTransitionPoint(toActivityBox.y, toActivityBox.y2, points.startY);
			}
		}
		
		if (points) {
			// middle point of the transition
			points.middleX = points.startX + (points.endX - points.startX)/2;
			points.middleY = points.startY + (points.endY - points.startY)/2;
		}
		
		return points;
	},
	
	getActivityIcon : function(activityName) {
		// check for icon SVG cache in the library
		var iconData = layout.toolMetadata[activityName].iconData;
		if (!iconData) {
			if (!layout.toolMetadata[activityName].iconPath) {
				return;
			}
			// if SVG is not cached, get it synchronously
			$.ajax({
				url : LAMS_URL + layout.toolMetadata[activityName].iconPath,
				async : false,
				dataType : 'text',
				success : function(response) {
					iconData = response;
					layout.toolMetadata[activityName].iconData = iconData;
				}
			});		
		}
		
		if (iconData) {
			// build a SVG fragment
			var fragment = Snap.parse(iconData);
			return Snap(fragment.node);
		}
	},
	
	
	/**
	 * Finds activity/region this shape is bound with.
	 */
	getParentObject : function(item) {
		var parentObject = null;
		
		while (!parentObject) {
			parentObject = item.data('parentObject');
			if (!parentObject) {
				item = item.parent();
				if (!item || item.attr('id') == 'canvas') {
					break;
				}
			}
		}
		
		return parentObject;
	},
	
	
	/**
	 * Get output definitions from Tool activity
	 */
	getOutputDefinitions : function(activity){
		if (!activity.toolID) {
			return;
		}
		$.ajax({
			url : LAMS_URL + 'authoring/getToolOutputDefinitions.do',
			data : {
				'toolContentID' : activity.toolContentID 
								|| layout.toolMetadata[activity.learningLibraryID].defaultToolContentID
			},
			cache : false,
			async: true,
			dataType : 'json',
			success : function(response) {
				activity.outputDefinitions = response;
				$.each(activity.outputDefinitions, function() {
					if (activity.gradebookToolOutputDefinitionName) {
						if (this.name == activity.gradebookToolOutputDefinitionName) {
							activity.gradebookToolOutputDefinitionDescription = this.description;
							activity.gradebookToolOutputDefinitionWeightable = this.weightable;
							return false;
						}
					} else {
						if (this.isDefaultGradebookMark){
							activity.gradebookToolOutputDefinitionName = this.name;
							activity.gradebookToolOutputDefinitionDescription = this.description;
							activity.gradebookToolOutputDefinitionWeightable = this.weightable;
							return false;
						}
					}
				});
			}
		});
	},
	
	
	/**
	 * Open separate window with activity authoring on double click.
	 */
	openActivityAuthoring : function(activity){
		if (activity.isAuthoringOpening) {
			return;
		}
		
		activity.isAuthoringOpening = true;
		if (activity.authorURL) {
			var dialogID = "dialogActivity" + activity.toolContentID;
			showDialog(dialogID, {
				'height' : Math.max(300, $(window).height() - 30),
				'width' :  Math.max(380, Math.min(1024, $(window).width() - 60)),
				'draggable' : true,
				'resizable' : true,
				'title' : activity.title + ' ' + LABELS.ACTIVITY_DIALOG_TITLE_SUFFIX,
				'beforeClose' : function(event){
					// ask the user if he really wants to exit before saving his work
					var iframe = $('iframe', this);
					// if X button was clicked, currentTarget is set
					// if it is not the last Re-Edit/Close page, doCancel() exists
					if (iframe[0].contentWindow.doCancel) {
						iframe[0].contentWindow.doCancel();
						return false;
					}
				},
				'close' : function(){
					$(this).remove();
					PropertyLib.validateConditionMappings(activity);
				},
				'open' : function() {
					var dialog = $(this);
					// load contents after opening the dialog
					$('iframe', dialog).attr('id', dialogID).attr('src', activity.authorURL).on('load', function(){
						// override the close function so it works with the dialog, not window
						this.contentWindow.closeWindow = function(){
							// detach the 'beforeClose' handler above, attach the standard one and close the dialog
							ActivityLib.closeActivityAuthoring(dialogID);
						}
					});
				}
			}, true);
			
			GeneralLib.setModified(true);
			activity.isAuthoringOpening = false;
			return;
		}
		
		// if there is no authoring URL, fetch it for a Tool Activity
		if (activity.toolID) {
			$.ajax({
				async : true,
				cache : false,
				url : LAMS_URL + "authoring/createToolContent.do",
				dataType : 'json',
				data : {
					'toolID'          : activity.toolID,
					// if toolContentID exists, a new content will not be created, only authorURL will be fetched
					'toolContentID'   : activity.toolContentID,
					'contentFolderID' : layout.ld.contentFolderID
				},
				success : function(response) {
					// make sure that response contains valid data
					if (response.authorURL) {
						activity.authorURL = response.authorURL;
						activity.toolContentID = response.toolContentID;
						// the response should always return a correct content folder ID,
						// but just to make sure use it only when it is needed
						if (!layout.ld.contentFolderID) {
							layout.ld.contentFolderID = response.contentFolderID;
						}
						
						activity.isAuthoringOpening = false;
						// this time open it properly
						ActivityLib.openActivityAuthoring(activity);
					}
				},
				complete : function(){
					activity.isAuthoringOpening = false;
				}
			});
		} else {
			activity.isAuthoringOpening = false;
		}
	},
	
	
	/**
	 * Draw each of activity's inbound and outbound transitions again.
	 */
	redrawTransitions : function(activity) {
		if (activity.transitions) {
			$.each(activity.transitions.from.slice(), function(){
				ActivityLib.addTransition(activity, this.toActivity, true);
			});
			$.each(activity.transitions.to.slice(), function(){
				ActivityLib.addTransition(this.fromActivity, activity, true);
			});
		}
	},
	
	/**
	 * Refresh conditions of complex output definitions from Tool activity
	 */
	refreshOutputConditions : function(activity){
		if (!activity.toolID) {
			return;
		}
			
		$.ajax({
			url : LAMS_URL + 'authoring/getToolOutputDefinitions.do',
			data : {
				'toolContentID' : activity.toolContentID 
								|| layout.toolMetadata[activity.learningLibraryID].defaultToolContentID
			},
			cache : false,
			async: false,
			dataType : 'json',
			success : function(response) {
				// find the matching existing output and replace its conditions
				$.each(response, function(){
					var output = this;
					$.each(activity.outputDefinitions, function(){
						if (output.name == this.name) {
							this.conditions = output.conditions;
						}
					});
				});
			}
		});
	},
	
	
	/**
	 * Deletes the given activity.
	 */
	removeActivity : function(activity, forceRemove) {
		var coreActivity =  activity.branchingActivity || activity;
		if (!forceRemove && activity instanceof ActivityDefs.BranchingEdgeActivity){
			// user removes one of the branching edges, so remove the whole activity
			if (!confirm(LABELS.REMOVE_ACTIVITY_CONFIRM)){
				return;
			}
			var otherEdge = activity.isStart ? coreActivity.end
					                         : coreActivity.start;
			ActivityLib.removeActivity(otherEdge, true);
		}
		
		if (activity instanceof ActivityDefs.FloatingActivity) {
			layout.floatingActivity = null;
			// re-enable the button, as the only possible Floating Activity is gone now
			$('.template[learningLibraryId="floating"]').slideDown();
		} else {
			// remove the transitions
			// need to use slice() to copy the array as it gets modified in removeTransition()
			$.each(activity.transitions.from.slice(), function() {
				ActivityLib.removeTransition(this);
			});
			$.each(activity.transitions.to.slice(), function() {
				ActivityLib.removeTransition(this);
			});
			
			// remove the activity from reference tables
			layout.activities.splice(layout.activities.indexOf(activity), 1);
			if (layout.copiedActivity = activity) {
				layout.copiedActivity = null;
			}

			// find references of this activity as grouping or input
			$.each(layout.activities, function(){
				var candidate = this.branchingActivity || this;
				if (candidate.grouping == coreActivity) {
					candidate.grouping = null;
					this.propertiesContent = null;
					this.draw();
				} else if (candidate.input == coreActivity) {
					candidate.input = null;
					this.propertiesContent = null;
				}
			});
		}
		
		// remove the activity from parent activity
		if (activity.parentActivity && activity.parentActivity instanceof DecorationDefs.Container) {
			activity.parentActivity.childActivities.splice(activity.parentActivity.childActivities.indexOf(activity), 1);
		}
		
		// remove child activities
		if (activity instanceof DecorationDefs.Container) {
			$.each(activity.childActivities.slice(), function(){
				ActivityLib.removeActivity(this);
			});
		}
		
		// visually remove the activity
		activity.items.remove();
		GeneralLib.setModified(true);
	},
	
	/**
	 * Deletes an item (activity, annotation etc.) as a result of user pressing a button on properties box
	 */
	removeItemWithButton : function(item) {
		if ((item instanceof ActivityDefs.BranchingEdgeActivity) || confirm(LABELS.REMOVE_BUTTON_CONFIRM)) {
			ActivityLib.removeSelectEffect(item);
			if (item instanceof DecorationDefs.Label) {
				DecorationLib.removeLabel(item);
			} else if (item instanceof DecorationDefs.Region) {
				DecorationLib.removeRegion(item);
			} else if (item instanceof ActivityDefs.Transition) {
				ActivityLib.removeTransition(item);
			} else {
				ActivityLib.removeActivity(item);
			}
		}
	},
	
	
	/**
	 * Deselects an activity/transition/annotation
	 */
	removeSelectEffect : function(object) {
		// remove the effect from the given object or the selected one, whatever it is
		if (!object) {
			object = layout.selectedObject;
		}
		
		if (object) {
			var selectEffect = object.items.selectEffect;
			if (selectEffect) {
				object.items.selectEffect = null;
				// different effects for different types of objects
				if (object instanceof DecorationDefs.Region) {
					object.items.shape.attr({
	 			    	'stroke' : 'black',
						'stroke-dasharray' : null
					});
					object.items.fitButton.attr('display','none');
					object.items.resizeButton.attr('display','none');
					
					var childActivities = DecorationLib.getChildActivities(object.items.shape);
					$.each(childActivities, function(){
						ActivityLib.removeSelectEffect(this);
					});
				} else if (object instanceof ActivityDefs.Transition) {
					// just redraw the transition, it's easier
					object.draw();
				} else {
					selectEffect.remove();
					
					// if it's "import part" do special processing for branching
					if (activitiesOnlySelectable) {
						if (object instanceof ActivityDefs.BranchingEdgeActivity) {
							if (object.isStart) {
								ActivityLib.removeSelectEffect(object.branchingActivity.end);
								
								// deselect all children in branches
								$.each(object.branchingActivity.branches, function(){
									var transition = this.transitionFrom;
									while (transition) {
										var activity = transition.toActivity;
										if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
											return true;
										}
										ActivityLib.removeSelectEffect(activity);
										transition = activity.transitions.from.length > 0 ? activity.transitions.from[0] : null;
									}
								});
							} else {
								ActivityLib.removeSelectEffect(object.branchingActivity.start);
							}
						}

						// deselect Parallel Activity children
						$.each(layout.activities, function(){
							if (this instanceof ActivityDefs.ParallelActivity && this.childActivities.indexOf(object) > -1){
								ActivityLib.removeSelectEffect(this);
								$.each(this.childActivities, function(){
									if (this != object) {
										this.items.selectEffect.remove();
										this.items.selectEffect = null;
									}
								});
							}
						});
					}
				}
			}
			
			if (layout.propertiesDialog) {
				// no selected activity = no properties dialog
				layout.propertiesDialog.css('visibility', 'hidden');
			}
			layout.selectedObject = null;
		}
	},
	
	
	/**
	 * Removes the given transition.
	 */
	removeTransition : function(transition) {
		// find the transition and remove it
		var transitions = transition.fromActivity.transitions.from;
		transitions.splice(transitions.indexOf(transition), 1);
		transitions = transition.toActivity.transitions.to;
		transitions.splice(transitions.indexOf(transition), 1);
		
		if (transition.branch) {
			// remove corresponding branch
			var branches = transition.branch.branchingActivity.branches;
			branches.splice(branches.indexOf(transition.branch), 1);
			
			if (transition.branch.defaultBranch && branches.length > 0) {
				// reset the first branch as the default one
				branches[0].defaultBranch = true;
			}
		}
		
		transition.items.remove();
		GeneralLib.setModified(true);
	},
	
	
	/**
	 * Crawles through branches setting their lengths and finding the longest one.
	 */
	updateBranchesLength : function(branchingActivity) {
		var longestBranchLength = 0;
		$.each(branchingActivity.branches, function(){
			// include the first activity
			var branchLength = 1,
				activity = this.transitionFrom.toActivity;
			if (activity instanceof ActivityDefs.BranchingEdgeActivity
					&& branchingActivity == activity.branchingActivity){
				// branch with no activities
				return true;
			}
			
			while (activity.transitions.from.length > 0) {
				activity = activity.transitions.from[0].toActivity;
				// check if reached the end of branch
				if (activity instanceof ActivityDefs.BranchingEdgeActivity) {
					break;
				} else {
					branchLength++;
				}
			};
			this.branchLength = branchLength;
			if (branchLength > longestBranchLength) {
				longestBranchLength = branchLength;
			}
		});
		
		branchingActivity.longestBranchLength = longestBranchLength;
	},
	
	getActivityTitle : function(title, x, y) {
		
		if (title.length > 35) {
			title = title.substring(0, 35) + '...';
		}
		var label = $('<div />').addClass('svg-activity-title-label svg-tool-activity-title-box')
								.attr('xmlns', 'http://www.w3.org/1999/xhtml')
								.text(title),
			wrapper = $('<foreignObject />').append(label).attr({
				'x'     : x + 75,
				'y'     : y,
				'width' : layout.activity.width - 75,
				'height': layout.activity.height
			});
		return Snap.parse(wrapper[0].outerHTML);
	}
}; 