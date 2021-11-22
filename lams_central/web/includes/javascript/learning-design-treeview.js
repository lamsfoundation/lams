var ldTreeview = {
		// for authoring, we show invalid designs
		// for add lesson and other places we need to choose a real LD, we hide them
		allowInvalidDesigns : false,
		// for simulating double click as treeview does not support it
		DOUBLE_CLICK_PERIOD : 1000,
		nodeLastSelectedTime : null,
		nodeLastSelectedId : null,
		// reference to the rendered treeview
		ldTree : null,
		// can be i18n
		LABEL_RUN_SEQUENCES_FOLDER : 'Run sequences',
		
		
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
				url : LAMS_URL + 'home/getFolderContents.do',
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
							  	         'readOnly'			: this.readOnly
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
			// node deselected or other node selected
			if (event.type != 'nodeSelected' && this.nodeLastSelectedId !== node.nodeId){
				this.nodeLastSelectedTime = null;
			}
			// if there is no double click handler or the node was deselected, just run the single click
			if (!onNodeDblClick || event.type != 'nodeSelected') {
				if (onNodeClick) {
					onNodeClick(event, node);
				}
				return;
			}
			
			// if user selected the same node again withing a given timeout, simulate double click
			var currentTimestamp = event.timeStamp;
			if (this.nodeLastSelectedId == node.nodeId &&
				this.nodeLastSelectedTime && currentTimestamp - this.nodeLastSelectedTime < this.DOUBLE_CLICK_PERIOD) {
				onNodeDblClick(event, node);
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