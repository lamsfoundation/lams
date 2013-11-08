/**
 * This file contains methods for menu in Authoring.
 */

var MenuLib = {
		
	addBranching : function(){
		var dialog = $('#infoDialog').text('Place the branching point');
		dialog.dialog('open');
		
		var branchingActivity = null;
		canvas.css('cursor', 'pointer').click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var x = event.pageX - canvas.offset().left;
			var y = event.pageY - canvas.offset().top;
			
			var branchingEdge = new ActivityLib.BranchingEdgeActivity(null, x, y, branchingActivity);
			activities.push(branchingEdge);
			
			if (branchingActivity) {
				HandlerLib.resetCanvasMode();
				
				dialog.text('');
				dialog.dialog('close');
			} else {
				branchingActivity = branchingEdge.branchingActivity;
				dialog.text('Place the converge point');
			}
		});
	},
	
	/**
	 * Run when grouping is dropped on canvas. Creates a new grouping activity.
	 */
	addGrouping : function() {
		canvas.css('cursor', 'url("../images/grouping.gif"), move').click(function(event){
			HandlerLib.resetCanvasMode();
			
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var x = event.pageX - canvas.offset().left;
			var y = event.pageY - canvas.offset().top;
			
			activities.push(new ActivityLib.GroupingActivity(null, x, y));
		});
	},
	
	
	/**
	 * Run when gate is dropped on canvas. Creates a new gate activity.
	 */
	addGate : function() {
		canvas.css('cursor', 'url("../images/stop.gif"), move').click(function(event){
			HandlerLib.resetCanvasMode();
			
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var x = event.pageX - canvas.offset().left;
			var y = event.pageY - canvas.offset().top;
			
			activities.push(new ActivityLib.GateActivity(null, x, y));
		});
	},
	
	
	/**
	 * Opens "Open sequence" dialog where an user can choose a Learning Design to load.
	 */
	openLearningDesign : function(){
		var dialog = $('#openLearningDesignDialog');
		// remove the directory tree, if it remained for last dialog opening
		$('#learningDesignTree', dialog).empty();
		dialog.dialog('open');
		
		// get users' folders and LDs
		var treeNodes = MenuLib.getFolderContents();
		// genearate folder tree for the widget
		var tree = new YAHOO.widget.TreeView('learningDesignTree', treeNodes);
		// store the tree in the dialog's data
		dialog.dialog('option', 'ldTree', tree);
		// make folder contents load dynamically on open
		tree.setDynamicLoad(function(node, callback){
			// load subfolder contents
			var childNodeData = MenuLib.getFolderContents(node.data.folderID);
			if (childNodeData) {
			$.each(childNodeData, function(){
					// create and add a leaf
					new YAHOO.widget.TextNode(this, node);
				});
			}
			
			// required by YUI
			callback();
		});
		
		tree.singleNodeHighlight = true;
		tree.subscribe('clickEvent', function(event){
	
			if (!event.node.data.learningDesignId){
				// it is a folder
				return false;
			}

			// display "loading" animation and finally LD thumbnail
			$('.ldChoiceDependentCanvasElement').css('display', 'none');
			if (event.node.highlightState == 0) {
				$('#ldScreenshotLoading').css('display', 'inline');
				// get the image of the chosen LD
				$('#ldScreenshotAuthor').attr('src', LD_THUMBNAIL_URL_BASE + event.node.data.learningDesignId);
				$('#ldScreenshotAuthor').css('width', 'auto').css('height', 'auto');
			} else {
				// toggleCanvasResize(CANVAS_RESIZE_OPTION_NONE);
			}
		});
		tree.subscribe('clickEvent',tree.onEventToggleHighlight);
		tree.render();
		
		// expand the first (user) folder
		tree.getRoot().children[0].expand();
	},
	
	
	/**
	 * Loads subfolders and LDs from the server.
	 */
	getFolderContents : function(folderID) {
		var result = null;
			
		$.ajax({
			url : LAMS_URL + 'home.do',
			data : {
				'method' : 'getFolderContents',
				'folderID' : folderID
			},
			cache : false,
			async: false,
			dataType : 'json',
			success : function(response) {
				result = [];

				// parse the response; extract folders and LDs
				if (response.folders) {
					$.each(response.folders, function(){
						result.push({'type'            : 'text',
								  	 'label'           : this.isRunSequencesFolder ?
								  			 				LABEL_RUN_SEQUENCES_FOLDER : this.name,
								  	 'folderID'		   : this.folderID
									 });
					});
				}
				if (response.learningDesigns) {
					$.each(response.learningDesigns, function(){
						result.push({'type'             : 'text',
						  	         'label'            : this.name,
						  	         'isLeaf'           : true,
						  	         'learningDesignId' : this.learningDesignId
							        });
					});
				}
			}
		});
		
		return result;
	},
	
	
	/**
	 * Sorts activities on canvas.
	 */
	arrangeActivities : function(){
		// just to refresh the state of canvas
		HandlerLib.resetCanvasMode();

		if (activities.length == 0) {
			// no activities, nothing to do
			return;
		}
		
		// activities are arranged in a grid
		var row = 0,
			column = 0,
			// check how many columns current paper can hold
			maxColumns = Math.floor((paper.width - layout.conf.arrangeHorizontalPadding)
					                 / layout.conf.arrangeHorizontalSpace),
			// the initial max length of subsequences is limited by paper space
			subsequenceMaxLength = maxColumns,
			// check how many rows current paper can hold
			maxRows = Math.floor((paper.height - layout.conf.arrangeVerticalPadding)
	                 			  / layout.conf.arrangeVerticalSpace),
	        // make a shallow copy of activities array
			activitiesCopy = activities.slice(),
			// just to speed up processing when there are only activities with no transitions left
			onlyDetachedLeft = false;
		
		// branches will not be broken into few rows; if they are long, paper will be resized
		// find the longes branch to find the new paper size
		$.each(activities, function(){
			if (this.type == 'branchingEdge' && this.isStart) {
				// refresh branching metadata
				ActivityLib.updateBranchesLength(this.branchingActivity);
				// add start and end edges to the result
				var longestBranchLength = this.branchingActivity.longestBranchLength + 2;
				if (longestBranchLength > subsequenceMaxLength) {
					subsequenceMaxLength = longestBranchLength;
				}
			}
		});
		// resize paper horizontally, if needed
		if (subsequenceMaxLength > maxColumns) {
			maxColumns = subsequenceMaxLength;
			resizePaper(layout.conf.arrangeHorizontalPadding
					      + maxColumns * layout.conf.arrangeHorizontalSpace,
					      paper.height);
		}
		
		// main loop; iterate over whatever is left in the array
		while (activitiesCopy.length > 0) {
			// resize paper vertically, if needed
			if (row > maxRows) {
				maxRows++;
				resizePaper(paper.width, layout.conf.arrangeVerticalPadding
		                   				   + maxColumns * layout.conf.arrangeVerticalSpace);
			}
			
			// look for activities with transitions first; detached ones go to the very end
			var activity = null;
			if (!onlyDetachedLeft) {
				$.each(activitiesCopy, function(){
					if (this.transitions.to.length > 0) {
						activity = this;
						while (activity.transitions.to.length > 0) {
							// check if previous activity was not drawn already
							// it can happen for branching edges
							var activityLookup = activity.transitions.to[0].fromActivity;
							if (activitiesCopy.indexOf(activityLookup) == -1) {
								break;
							}
							activity = activityLookup; 
						};
						return false;
					}
				});
			}

			if (!activity) {
				// no activities with transitions left, take first detached one
				onlyDetachedLeft = true;
				activity = activitiesCopy[0];
			}
				
			// markers for complex activity processing
			var branchIndex = null,
				complexActivityEnd = null;
			
			// crawl through a sequence of activities
			while (activity) {
				if (activity.type == 'branchingEdge') {
					// draw branching edges straight away and remove them from normall processing
					branchingActivity = activity.branchingActivity;
					var start = branchingActivity.start,
						end = branchingActivity.end,
						complexActivityEnd = end,
						// edge points go to middle of rows with branches
						branchingRow = row + 1 + Math.floor(branchingActivity.branches.length / 2);
					
					activitiesCopy.splice(activitiesCopy.indexOf(start), 1);
					activitiesCopy.splice(activitiesCopy.indexOf(end), 1);
					
					// start point goes to very left, end goes wherever the longes branch ends
					start.draw(layout.conf.arrangeHorizontalPadding + 62,
						       layout.conf.arrangeVerticalPadding + branchingRow * layout.conf.arrangeVerticalSpace + 25);
					end.draw(layout.conf.arrangeHorizontalPadding + (activity.branchingActivity.longestBranchLength + 1)
                                                     					 * layout.conf.arrangeHorizontalSpace + 62,
                             layout.conf.arrangeVerticalPadding + branchingRow * layout.conf.arrangeVerticalSpace + 25);
					
					row++;
					if (branchingActivity.branches.length > 0) {
						// set up branch drawing
						branchIndex = 0;
						activity = branchingActivity.branches[branchIndex].transitionFrom.toActivity;
						// next activity for normal processing will be first one from the first branch
						continue;
					} else {
						// no branches, nothing to do, carry on with normall activity processing
						activity = complexActivityEnd;
						complexActivityEnd = null;
						// this will be incremented in the same iteration by normal activity processing
						column = 1;
					}
				} else {
					// it is a simple activity, so redraw it
					var x = layout.conf.arrangeHorizontalPadding + column * layout.conf.arrangeHorizontalSpace,
						y = layout.conf.arrangeVerticalPadding + row * layout.conf.arrangeVerticalSpace;
					
					activity.draw(x, y);
					// remove the activity so we do not process it twice
					activitiesCopy.splice(activitiesCopy.indexOf(activity), 1);
				}
				
				// find the next row and column
				column = (column + 1) % maxColumns;
				if (column == 0) {
					row++;
				}
				
				// does the activity has further activities?
				if (activity.transitions.from.length > 0) {
					activity = activity.transitions.from[0].toActivity;
				} else {
					activity = null;
				}
				
				if (complexActivityEnd && (!activity || activity == complexActivityEnd)) {
					// end of branch
					branchIndex++;
					row++;
					if (complexActivityEnd.branchingActivity.branches.length > branchIndex) {
						// there is another branch to process
						activity = complexActivityEnd.branchingActivity.branches[branchIndex].transitionFrom.toActivity;
						// go back to left side of canvas and draw next branch
						column = 1;
						continue;
					} else {
						// no more branches, return to normal activity processing
						activity = complexActivityEnd;
						branchIndex = null;
						complexActivityEnd = null;
						column = 0;
						if (activity.transitions.from.length == 0) {
							break;
						}
					}
				}
				
				if (!activity || activitiesCopy.indexOf(activity) == -1) {
					// next activity was already processed, so stop crawling
					break;
				}
			};
		};
		
		// redraw transitions one by one
		$.each(activities, function(){
			$.each(this.transitions.from.slice(), function(){
				ActivityLib.drawTransition(this.fromActivity, this.toActivity, true);
			});
		});
	},
	
	
	/**
	 * Removes existing activities and prepares canvas for a new sequence.
	 */
	newLearningDesign : function(force){
		if (!force && activities.length > 0 && !confirm('Are you sure you want to remove all existing activities?')){
			return;
		}
		
		activities = [];
		if (paper) {
			paper.clear();
		} else {
			paper = Raphael('canvas');
		}
		
		resizePaper();
	}
};