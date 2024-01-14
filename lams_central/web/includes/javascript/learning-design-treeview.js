var ldTreeview = {
	// for authoring, we show invalid designs
	// for add lesson and other places we need to choose a real LD, we hide them
	allowInvalidDesigns : false,
	// for simulating double click as treeview does not support it
	DOUBLE_CLICK_PERIOD : 600,
	nodeLastSelectedTime : null,
	nodeLastSelectedId : null,
	// reference to the rendered treeview
	ldTree : null,
	// can be i18n
	LABEL_RUN_SEQUENCES_FOLDER : 'Run sequences',
	FOLDER_CONTENTS_FETCH_URL : 'home/getFolderContents.do',


	/**
	 * Initialises a Learning Design treeview
	 */
	init : function(targetElementSelector, onNodeClick, onNodeDblClick) {
		this.ldTree = $(targetElementSelector);

		// get top level folders
		var mainFolders = ldTreeview.getFolderContents();

		// initialise the treeview
		this.ldTree.treeview({
			data : mainFolders,
			showBorder : false,
			// use Font Awesome icons
			expandIcon : 'fa fa-folder',
			collapseIcon : 'fa fa-folder-open',
			emptyIcon : '',
			// this has been added to treeview in pull request #350
			// the JS script was then additionally customised for LAMS
			lazyLoad : true,
			lazyLoadFunction : function(parentNode, callback, options) {
				callback(parentNode, ldTreeview.getFolderContents(parentNode), options);
			},
			// run same function on any click
			onNodeSelected   : function(event, node){ldTreeview.onClickInternal(event, node, onNodeClick, onNodeDblClick)},
			onNodeUnselected : function(event, node){ldTreeview.onClickInternal(event, node, onNodeClick, onNodeDblClick)}
		});

		ldTreeview.refresh(this.ldTree, this.ldTree.treeview('getNode', 0));
		this.ldTree.treeview('expandNode', 0);
	},

	/**
	 * Fetches folders and LDs from back end and parses them for treeview
	 */
	getFolderContents : function(folder) {
		// if folder is NULL, then we fetch top folders
		var folderID = folder ? folder.folderID : null,
			canSave = folder ? folder.canSave : null,
			canHaveReadOnly = folder ? folder.canHaveReadOnly : null,
			allowInvalidDesigns = this.allowInvalidDesigns,
			runSequencesFolderLabel = this.LABEL_RUN_SEQUENCES_FOLDER,
			result = [];

		$.ajax({
			url : LAMS_URL + this.FOLDER_CONTENTS_FETCH_URL,
			data : {
				'folderID' : folderID,
				'allowInvalidDesigns' : allowInvalidDesigns
			},
			cache : false,
			async: false,
			dataType : 'json',
			success : function(response) {
				// parse the response; extract folders and LDs
				if (response.folders) {
					$.each(response.folders, function(index){
						// folderID == -2 is courses folder
						var canSave = this.folderID > 0 && !this.isRunSequencesFolder;
						result.push({'text'                : (this.isRunSequencesFolder ? runSequencesFolderLabel
									: ldTreeview.escapeHtml(this.name))
								+ (canSave ? '' : '&nbsp;<i class="fa fa-lock read-only-folder"></i>'),
							'nodes'			   : [],
							'folderID'		       : this.folderID,
							'isRunSequenceFolder' : this.isRunSequencesFolder,
							// either take parent's setting or take 2nd (courses) and 3rd (public) folder
							'canHaveReadOnly'	   : folderID ? canHaveReadOnly : index > 0,
							'canSave'		       : canSave,
							'canModify'	       : this.canModify && !this.isRunSequenceFolder										});
					});
				}
				if (response.learningDesigns) {
					$.each(response.learningDesigns, function(){
						var canModify = canSave && this.canModify,
							name = ldTreeview.escapeHtml(this.name);
						result.push({'label'            : name,
							'text'             : name + (this.readOnly ? ' <i class="fa fa-lock"></i>' : ''),
							'learningDesignId' : this.learningDesignId,
							'canHaveReadOnly'	: canHaveReadOnly,
							'canModify'		: canModify,
							'readOnly'			: this.readOnly,
							'modifiedDate'     : this.formattedDate
						});
					});
				}
			}
		});

		// if folder is empty, we need to shift its icon a bit to the right
		if (result.length === 0) {
			folder.icon = 'fa fa-folder-open treeview-empty';
		} else if (folder) {
			// remove previously set folder-open etc. classes so two folder icons are not displayed
			folder.icon = '';
		}
		return result;
	},

	/**
	 * Closes a folder and opens it again, fetching fresh data from server.
	 * Treeview library had to be customised for this.
	 */
	refresh : function(ldTree, node) {
		node = ldTree.treeview('getNode', node.nodeId);
		node.nodes = [];
		ldTree.treeview('collapseNode', node);
		// toggleNode was hidded in the original script
		// 'refresh' was not recognised in the original script
		ldTree.treeview('toggleNode', ['refresh', node]);
	},

	/**
	 * Handler for a click.
	 * It detects and simulates a double click too.
	 */
	onClickInternal : function(event, node, onNodeClick, onNodeDblClick) {

		if (event.type == 'nodeSelected'){
			// on select, deselect all other nodes as we mute the default behaviour doing this
			let selectedNodes = ldTreeview.ldTree.treeview('getSelected');
			selectedNodes.forEach(function(selectedNode) {
				if (selectedNode.nodeId != node.nodeId) {
					//deselect all other nodes
					ldTreeview.ldTree.treeview('unselectNode', [selectedNode.nodeId, { silent: true }]);
				}
			});
		} else {
			// deselect event
			if (ldTreeview.nodeLastSelectedId == node.nodeId) {
				// prevent deselecting nodes
				ldTreeview.ldTree.treeview('selectNode', [node.nodeId, {silent: false}]);
			}
			// do nothing more. Only real or simulated select events do anything.
			return;
		}

		// if there is no double click handler, just run the single click
		if (!onNodeDblClick) {
			if (onNodeClick) {
				onNodeClick(event, node);
			}
			return;
		}

		// if user selected the same node again withing a given timeout, simulate double click
		var currentTimestamp = event.timeStamp;
		if (this.nodeLastSelectedId == node.nodeId &&
			this.nodeLastSelectedTime && currentTimestamp - this.nodeLastSelectedTime < this.DOUBLE_CLICK_PERIOD) {
			setTimeout(function() {
				// this has to be run in a timeout as "deselect" goes before "select" event
				if (ldTreeview.nodeLastSelectedId == node.nodeId) {
					onNodeDblClick(event, node);
				}
			}, 100);
		} else if (onNodeClick){
			onNodeClick(event, node);
		}
		// update counters for next click
		this.nodeLastSelectedTime = currentTimestamp;
		this.nodeLastSelectedId = node.nodeId;
	},

	/**
	 * Escapes HTML tags to prevent XSS injection.
	 */
	escapeHtml : function(unsafe) {
		return unsafe
			.replace(/&/g, "&amp;")
			.replace(/</g, "&lt;")
			.replace(/>/g, "&gt;")
			.replace(/"/g, "&quot;")
			.replace(/'/g, "&#039;");
	}
}