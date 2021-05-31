﻿﻿﻿﻿﻿/**
 * This file contains methods for Activity definition and manipulation on canvas.
 */

/**
 * Stores different Activity types structures.
 */
var ActivityIcons = {
		'grouping' : 'data:image/png;base64,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',
		'gateClosed' : 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAG1UlEQVRYR82ZWUxUVxjH/+feOwPM6AAKOGhAquJI2qYlqURjq0/VSIyJimhq0li3Jj64vxnE5cUHl7r1wUbxwaVqIw/GpfpibWpja6RJ21DcKhh1BArMKOtdTvPdM2c6DDPMDEjSkxByz733nN/99vMNQ3KDAVABGPLx2S5X6RyXa870jIyy4rQ0X56qjtMAN903gM5m03z1WNcf/NzZeffHrq4fbnd11UVspQEwAfBE29PGiQaB0WKYCOSvysv7bLHHU/me01kKRXHYL/PQPvI/Cy0r/1uW/kdfX92lYPBCTXPzmaeAP7RpeO14EIkA7QXGAJ4qr3fbuuzs9S5NG2sDWRZRW4wxzjindSSOFAvnjHHOOVMBxb6rKOgyjLbj7e3H9vj9+9qAYEgztgBijXiA9oYArEqPp/yA13toQlraFJgmDM4NhTFFEff7v0+zNCwrei9uAdzi3NIY06CqeN7b+2iL37/xQjB4FfQBQt0DVB4LMAx30OvduSk3t5o2NCzLUBlTWTSURCG47m5xlZERC1JYA8BNzk1NUTSS6FctLbs2+/0740FGA9I1fY15qaDg5KKsrC9MwzAZY0wR87EHqbynB/D5BNjDh0B6eoTSB75mARbpX9U0tbajo2bxs2erQuom8YclGQkY9lQJpxuG7iCVxJMa7Uu2RXBVVcCKFcJhTp0C9u4FXK64kgwhc51zw+FwOGrb2yVkPw+PBKQbxiGvd+eGvLxqXdcJTnhpvEFqffMGmDkTOHs2/JRtTBUVUO7fB9zuRJDQOdcJ8nBz866NQt02i/39ke5ekZk592Jh4fe6YRgORs4X5QTRoKoKBALAvHnA8eOArgs7czjAV66EcusW4PGAnCvBIEmaDk3TljY1zfsuELghvVs6BMsB3H/6fL/naFohOOeD2lzkbqRSkuTJGqBsum085k93oK5dAxYOPIn4ALJJeqHVMJrebWh4vxXopG8lQFucB73e3Ztyc6sMwzDsUJDs0DSgtxdwOmHOnWurk924CcXQgbQ0wAgnn4QrUgjTNE073NKyZ6Pfv4PYbBV7gdynJSWPHIyNojASN5REb0FPBoPCY51O4PVr8cTo0UBfn3AeUrHMMIn0LJ4kdb8pqq+f4gdabMBdOTlbd+Tn70tJegRHcW/5cqCiAhgzBhSA7UFSa2sDzp8HLl4U3pwkpJTi7pcvt1W3tu6341795Mm/TsvIKKVIr3Ae2mWQzyWQjg5g9WqgunpwuWzfDpw+DWRmJuMslDtNsv+/enrqSh4/ns4+drs/uD1x4r2wvhNaSugBkuD168D48SKMyDQn35dzjY1AefnA+4PsE4rSxuzGxo/Yjpyczbu83gOUftRkpEdgFE7GjgWuXROSIfVFe6yca20VgGSr5FBJqNpkzKS0WuX3b2FXCgq+Lc/MXJYyINkcSTAZwPnzhQOlCHg1EDjPGqZMqZualvYh59yigiihhqUERxCQijlK/w+6u39jbdOmPc9WlPGhBJ2oPhSqJBWPIKBkaTfNFyzg87V7VDXr/wgYNM2O4QFeuSJs0C5fo4QfqrpBTrJgQUo2KIVlA7b5fM+zVTV1FZMX37kjTFZ6bOSZRM6ROcyalZIX91Nxw6RJdVMzMpJ3EgKiGEfZgeq/yZOBhQsFaKQUCbC2FnjyBDhzRqS96FgZxyPDTtLTU5d6mJGLEsyLF0I6ly+L2WPHAIcDWLdOXFMZdu8ekJ+fVPyTS8s4aIeZlAO1XEWeQWbMEKmMBkmUCocTJ8R1ZSVQVyfmBh6k4ka0foF6yKlOVtMEeO6c2IwkRyXWkSPieskSAZhEVR1J2y/VhYuF9PRSKhqTKhZotViAa9YIQFL1EAEHFAu0zpDKrRECjFVu2QXr3yUlD52MjU66YB0BQCpW6a+P89fv1NcXy4J1aCX/CABK6R1qadm9ye+nQtMu+e2/lA9NbxlwsEMTmaHdJFqUmfnppcLCG0kdO98uYPjYubipaW5tIHAz8tgpPTy1g3skoDy0r10rvPjoUbEmnVWSCDPJHNxpudRaH7G6CgRIQVnGQQIcvLuQUutDQsrm0YlFWVmrTMsyqf8X8yBP2WHUKJGPKfVR3iXwoiLhj9RE6uqKmYOH0jySqk6t/UaQVAjQIPXSoIM8DZJmVIEw3PbbAMhlHs/8/fn5hyY4ncVxG5gSIroF/F/+jdnA3Or3bzgfDF5LtYEZmRbDLeDtXu+2L7Oz17uH2gIOFbWdhvHPN+3tXw+3BTwAkiaKAO/nOTkrlmZlDa2J3tFxoaa19a020SNVPuBniE9crjllbndZscPhGxf1M8Qr03z1SNcb7nZ2/jKcnyH+BSVhFESM1+APAAAAAElFTkSuQmCC',
		'branchingStart' : 'data:image/png;base64,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',
		'branchingEnd' : 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAFHklEQVRIS71Wa2wUVRQ+d2Z2dmffu21otQX6iAWk0BaRhxVIpKmJCYoKvsBfBhMS5RFTfAQMKoZENMSYSPylGIyJiSgC/lEiDW6BpmkKpYUqrBu6JV1gd2a7s/O8M2PudhaXTWGpabnJJJt77p7vPL9zEAAwAGDaH9yvgwqAyG/rvgG7XNUbFSV5EkCOAwB1vzxHwfCqUcvE6bQQWQgAGgAQz0nop/Ugf2DJaQqxYdNShsfSPW12zvPgxIBpCT8KhFqjktj/oour/wDAMjJjfWuKXKXtCEypASgQWnE1mxl4CePUGX9w6WnTVK/omI8YhnQFqzciAJCxDZnS/KNgeOWwONb/Msb8nx7/omMM5aw0sHiJot31iHIEdZ3/QxIHdgJACgCI98ZUJJ8Ajwip7se93rq3aSawNC2cbvlPsW+Ox1fb4XCE28WxvlcxFjonAU4iRL68ofmU5QoX+YLLzoGlxynKWZfmuxYDgGQXGM4XFstWPOPxNX6XSfc+iTFPwl/K87vJczIUKnvCsJCZEm72PAwg3ijqZWIxYTaNZWesdXvnfSWkOusAQLGNmqjt7FrwzaWcs990MO75LgfiwFKikhT7TNfTPcRw5PM1HlTV2D5NEy/cxROWgHt8jQcR0B4xc27dHah2HJR+4Lny8NwDs8sTJ8XMzaNDCeNvMIIrysKzOsTM5d2qOnzgXimTvCMhYgKh1j6Mk8ezmUtv2bVA7omcgGIAf/2sqkcu/Lgthlpq0tdVlabPj+Dend/yHScGuEy4fOn5MaH/+bxC0qOl2CrP5eFAcPlvloV5Rb64VdPEgduqnGr88otNzOY3no1jlXcyDGUB7UJgAYI1u+ILjg9UN4f81dsLPb6XLrk1SJxczRY3V/OOYUr/GIYcpSlnuaRc3e9kZ7579iNh1bxZoqmrFEVTADpGmPMgx+VhuW9Bh7TWyS2JTBY4TyKc29uwk3VUvoDNdL+B5SjFOMNqNv41563f2/1hqnVOVdbUdYqibATTAotlLbR8R/bpnpGmffncEG9LhdoGdc0Olj16AutijzhG8iyP3BYquunQoa3Ghg2rR3GWdzIsM65WNxC4vbq85mP3N8f6KpsLPb4bJeYN9AXDKwdU+ernshz7xAYk7UYOKTIdoGxx40MLz/7+3l9mxQzNtCSa3FuIM6wMzyjLdjcwg7GhjcjnX/SrLMX2YJzqKiCO4nzn2snnbzlimUpCFC++DgDkTi+aXuPGUzXbWhpq9+9eNwpL6rJAgQV9w27Y89ODEBm8tsfUhnahUNlq8meNT0YWACjRol4minKeuFzVr7m42vcF/lS9bRWhwokmls1aFW2InbmlqpxtohBQI0k8aCjxAwDXfs4RSCD02KChpyMMW/aUkOptARCvA4DD9iaH4XJVb+I8DZ9mM92tNtGUmlSFcs7uc0LF5ORkZAO5IaQ6GzhPww4nW7Fe4E/NBwCVWMUwoWWcu66Dot1z5Wzvek3L9k9iPcrltqBoCSCpldzQIMAJSexpI0q9/qYfGCaweHwsclXkgaomDivS5b32WlRqOEzEBfkCvi0tBDgppLqbAWQpEGzt0vTkUUMXzliWHNX1dG+BplLhvRcCuvWGzOO4JPa2c56mI1i7eSybHdpepIG0y50KaVJghY/JzhVDiOWwljgsioOb7cLKL3kkPFO6a+XBUbi83dKU0e9F8fwrdh9PuXcTJp5sF5qW+KVAOC0eFoNPdkj875xOBDwtxVPKwn8BbCc3nMHhPAgAAAAASUVORK5CYII=',
		'bin' : 'data:image/png;base64,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',
}, 

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
		'Leaderselection' : 6,
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
		'Forum and Scribe' : 5
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
	GateActivity : function(id, uiid, x, y, title, description, readOnly, gateType, startTimeOffset, gateActivityCompletionBased, password) {
		this.id = +id || null;
		this.uiid = +uiid || ++layout.ld.maxUIID;
		this.title = title;
		this.description = description;
		this.readOnly = readOnly;
		this.gateType = gateType || 'permission';
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
		this.groupingType = groupingType || 'random';
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
		
		x = GeneralLib.snapToGrid(x) + layout.snapToGrid.offset * 2;
		y = GeneralLib.snapToGrid(y);

		// create activity SVG elements
		var shape = paper.circle(x, y, 20)
						 .addClass('svg-branching svg-branching-' + (this.isStart ? 'start' : 'end')),
			icon = paper.image(this.isStart ? ActivityIcons.branchingStart : ActivityIcons.branchingEnd, x - 15, y - 15, 30, 30);

		this.items = paper.g(shape, icon);
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		if (this.isStart) {
			// uiid is needed in Monitoring
			this.items.attr('uiid', this.branchingActivity.uiid);
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
							   box.y2 + layout.conf.containerActivityPadding,
							   layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight,
							   layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		}
		
		this.items.data('parentObject', this);
	},
	
	
	/**
	 * Draws a Gate activity
	 */
	gate : function(x, y) {
		if (x == undefined || y == undefined) {
			x = this.items.getBBox().x;
			y = this.items.getBBox().y;
		}

		if (this.items) {
			this.items.remove();
		}
		
		x = GeneralLib.snapToGrid(x);
		y = GeneralLib.snapToGrid(y) - layout.snapToGrid.offset * 2;
		
		// create activity SVG elements
		var shape = paper.image(ActivityIcons.gateClosed, x, y, 40, 40);

		this.items = paper.g(shape);
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		// uiid is needed in Monitoring
		this.items.attr('uiid', this.uiid);
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
						 .addClass('svg-tool-activity-background'),
			shapeBorder = paper.path(shapePath)
							 .addClass('svg-tool-activity-border' + (this.requireGrouping ? '-require-grouping' : '')),
			// check for icon in the library
			icon = paper.image(ActivityIcons.grouping, x + 20, y + 3, 30, 30),
			label = paper.text(x + 55, y + 25, ActivityLib.shortenActivityTitle(this.title))
			 			 .attr(layout.defaultTextAttributes)
			 			 .attr({
			 				 'id'   : 'toolActivityTitle',
			 				 'fill' : layout.colors.activityText,
			 				 'text-anchor' : 'start'
			 			 });
		
		this.items = paper.g(shape, shapeBorder, label, icon);
		
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		// uiid is needed in Monitoring
		this.items.attr('uiid', this.uiid);
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
			
			this.drawContainer(x, y,
							  box.x2 + layout.conf.containerActivityPadding,
							  box.y2 + layout.conf.containerActivityPadding,
							  layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight,
							   layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		}
		
		if (!isReadOnlyMode){
			// allow transition drawing and other activity behaviour
			this.items.unmousedown().mousedown(HandlerActivityLib.activityMousedownHandler);
		}
		
		this.items.data('parentObject', this);
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
			
			this.drawContainer(x, y,
							  box.x2 + layout.conf.containerActivityPadding,
							  box.y2 + layout.conf.containerActivityPadding,
							  layout.colors.optionalActivity, layout.colors.optionalActivityBorder, 0.5);
		} else {
			this.drawContainer(x, y,
							   x + layout.conf.containerActivityEmptyWidth,
							   y + layout.conf.containerActivityEmptyHeight,
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
			bannerWidth = layout.activity.bannerWidth,
			bannerPath = 'M ' + (x + curve) + ' ' + (y + height) + ' q ' + -curve + ' 0 ' + -curve + ' ' + -curve + 
						 ' v ' + (-height + 2 * curve) + ' q 0 ' + -curve + ' ' + curve + ' ' + -curve,
			shapePath = bannerPath + ' h ' + (width - 2 * curve) + ' q ' + curve + ' 0 ' + curve + ' ' + curve +
						' v ' + (height - 2 * curve) + ' q 0 ' + curve + ' ' + -curve + ' ' + curve + ' z',
			shape = paper.path(shapePath)
						 .addClass('svg-tool-activity-background'),
			shapeBorder = paper.path(shapePath)
							 .addClass('svg-tool-activity-border' + (this.requireGrouping ? '-require-grouping' : '')),
			learningLibraryID = this.learningLibraryID,
			label = paper.text(x + 55, y + 23, ActivityLib.shortenActivityTitle(this.title))
			 			 .attr(layout.defaultTextAttributes)
			 			 .attr({
			 				 'id'   : 'toolActivityTitle',
			 				 'fill' : layout.colors.activityText,
			 				 'text-anchor' : 'start'
			 			 });
		
		bannerPath += ' h ' + bannerWidth + ' v ' + height + ' z';
		var banner = paper.path(bannerPath)
						  .addClass('svg-tool-activity-category-' + layout.toolMetadata[learningLibraryID].activityCategoryID);
		this.items = paper.g(shape, banner, shapeBorder, label);
		
		// check for icon SVG cache in the library
		var iconData = layout.toolMetadata[learningLibraryID].iconData;
		if (!iconData) {
			// if SVG is not cached, get it synchronously
			$.ajax({
				url : LAMS_URL + layout.toolMetadata[learningLibraryID].iconPath,
				async : false,
				dataType : 'text',
				success : function(response) {
					iconData = response;
					layout.toolMetadata[learningLibraryID].iconData = iconData;
				}
			});		
		}
		
		if (iconData) {
			// build a SVG fragment and position it
			var fragment = Snap.parse(iconData),
				icon = Snap(fragment.node);
			icon.select('svg').attr({
				'x'     : x + 20,
				'y'     : y + 3,
				'width' : '30px',
				'height': '30px'
			});
			this.items.add(icon);
		}
		
		if (this.readOnly && !isReadOnlyMode) {
			this.items.attr('filter', layout.conf.readOnlyFilter);
		}
		// uiid is needed in Monitoring
		this.items.attr('uiid', this.uiid);
        this.items.attr('id' , 'toolActivity');
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
		
		var isBranching = this.fromActivity instanceof ActivityDefs.BranchingEdgeActivity || this.toActivity instanceof ActivityDefs.BranchingEdgeActivity,
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
				   .addClass('svg-tool-activity-border');
			
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
	
	adjustTransitionPoint : function(bottomLimit, topLimit, target) {
		// find a good point inside the grid, then make sure it is within bounds
		return Math.max(bottomLimit + layout.transition.adjustStep, Math.min(topLimit - layout.transition.adjustStep,
				Math.floor(target / layout.transition.adjustStep) * layout.transition.adjustStep + layout.snapToGrid.offset));
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
						'startX'    : ActivityLib.adjustTransitionPoint(fromActivityBox.x, fromActivityBox.x2, toActivityBox.x + toActivityBox.width / 2)
										- (fromActivity.items.groupingEffect ? 0.5 * layout.conf.groupingEffectPadding : 0),
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
				points.endX = ActivityLib.adjustTransitionPoint(toActivityBox.x, toActivityBox.x2, points.startX) 
								- (toActivity.items.groupingEffect ? 0.5 * layout.conf.groupingEffectPadding : 0);
			}
		} else {
			if (fromActivityBox.cx < toActivityBox.cx) {
				points = {
						'startX'    : fromActivityBox.x2 + layout.transition.dotRadius,
						'startY'    : ActivityLib.adjustTransitionPoint(fromActivityBox.y, fromActivityBox.y2, toActivityBox.y + toActivityBox.height / 2)
										- (fromActivity.items.groupingEffect ? 0.5 * layout.conf.groupingEffectPadding : 0),
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
				points.endY = ActivityLib.adjustTransitionPoint(toActivityBox.y, toActivityBox.y2, points.startY) 
								- (toActivity.items.groupingEffect ? 0.5 * layout.conf.groupingEffectPadding : 0);
			}
		}
		
		if (points) {
			// middle point of the transition
			points.middleX = points.startX + (points.endX - points.startX)/2;
			points.middleY = points.startY + (points.endY - points.startY)/2;
		}
		
		return points;
	},
	
	
	/**
	 * Finds activity/region this shape is bound with.
	 */
	getParentObject : function(item) {
		var parentObject = item.data('parentObject');
		if (!parentObject) {
			var parentNode = item.parent();
			if (parentNode.type == 'g') {
				parentObject = parentNode.data('parentObject');
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
			$('#floatingActivityButton').attr('disabled', null)
									 	.css('opacity', 1);
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
	 			    	'stroke' : layout.colors.activityBorder,
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
	removeTransition : function(transition, redraw) {
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
	 * Reduce length of activity's title so it fits in its SVG shape.
	 */
	shortenActivityTitle : function(title) {
		if (title.length > 23) {
			title = title.substring(0, 22) + '...';
		}
		return title;
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
	}
};
