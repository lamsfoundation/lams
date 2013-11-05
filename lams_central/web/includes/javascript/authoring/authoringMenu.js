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
			
			var isStart = !branchingActivity;
			var branchingEdge = new ActivityLib.BranchingEdgeActivity(null, x, y, branchingActivity);
			activities.push(branchingEdge);
			
			if (isStart) {
				branchingActivity = branchingEdge.branchingActivity;
				dialog.text('Place the converge point');
			} else {
				dialog.text('');
				dialog.dialog('close');
				
				HandlerLib.resetCanvasMode();
			}
		});
	},
	
	/**
	 * Run when grouping is dropped on canvas. Creates a new grouping activity.
	 */
	addGrouping : function() {
		canvas.css('cursor', 'url("../images/grouping.gif"), move').click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var x = event.pageX - canvas.offset().left;
			var y = event.pageY - canvas.offset().top;
			
			activities.push(new ActivityLib.GroupingActivity(null, x, y));
			HandlerLib.resetCanvasMode();
		});
	},
	
	
	/**
	 * Run when gate is dropped on canvas. Creates a new gate activity.
	 */
	addGate : function() {
		canvas.css('cursor', 'url("../images/stop.gif"), move').click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var x = event.pageX - canvas.offset().left;
			var y = event.pageY - canvas.offset().top;
			
			activities.push(new ActivityLib.GateActivity(null, x, y));
			HandlerLib.resetCanvasMode();
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
	}
};