var ldTreeview = {
		DOUBLE_CLICK_PERIOD : 1000,
		nodeLastSelectedTime : null,
		nodeLastSelectedId : null,
		ldTree : null,
		
		init : function(targetElementSelector, onNodeClick, onNodeDblClick) {
			var ldTree = $(targetElementSelector);
			this.ldTree = ldTree;
			
			var mainFolders = ldTreeview.getFolderContents();
			mainFolders[0].nodes = ldTreeview.getFolderContents(mainFolders[0]);
		
			ldTree.treeview({
				data : mainFolders,
				showBorder : false,
				expandIcon : 'fa fa-folder',
				collapseIcon : 'fa fa-folder-open',
				lazyLoad : true,
				lazyLoadFunction : function(parentNode, callback, options) {
					callback(parentNode, ldTreeview.getFolderContents(parentNode), options);
					ldTreeview.selectFolder(parentNode);
				},
				onNodeSelected   : function(event, node){ldTreeview.onClickInternal(event, node, onNodeClick, onNodeDblClick)},
				onNodeUnselected : function(event, node){ldTreeview.onClickInternal(event, node, onNodeClick, onNodeDblClick)}
			});
		},
		
		getFolderContents : function(folder) {
			var folderID = folder ? folder.folderID : null,
				canSave = folder ? folder.canSave : null,
				canHaveReadOnly = folder ? folder.canHaveReadOnly : null,
				result = [];
				
			$.ajax({
				url : LAMS_URL + 'home/getFolderContents.do',
				data : {
					'folderID' : folderID,
					'allowInvalidDesigns' : true
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
							result.push({'text'                : (this.isRunSequencesFolder ? LABELS.RUN_SEQUENCES_FOLDER : this.name)
																	+ (canSave ? '' : ' <i class="fa fa-lock"></i>'),
										 'nodes'			   : [],
									  	 'folderID'		       : this.folderID,
									  	 'isRunSequenceFolder' : this.isRunSequencesFolder,
									  	 // either take parent's setting or take 2nd (courses) and 3rd (public) folder 
									  	 'canHaveReadOnly'	   : folderID ? canHaveReadOnly : index > 0,
									  	 'canSave'		       : canSave,
							  	         'canModify'	       : this.canModify && !this.isRunSequenceFolder
										});
						});
					}
					if (response.learningDesigns) {
						$.each(response.learningDesigns, function(){
							var canModify = canSave && this.canModify;
							result.push({'label'            : this.name,
										 'text'             : this.name + (this.readOnly || !canModify ? ' <i class="fa fa-lock"></i>' : ''),
							  	         'learningDesignId' : this.learningDesignId,
							  	         'canHaveReadOnly'	: canHaveReadOnly,
							  	         'canModify'		: canModify,
							  	         'readOnly'			: this.readOnly
								        });
						});
					}
				}
			});
			
			if (result.length === 0) {
				folder.icon = 'fa fa-folder-open roffset10';
			}
			return result;
		},
		
		refresh : function(ldTree, node) {
			node = ldTree.treeview('getNode', node.nodeId);
			node.nodes = [];
			ldTree.treeview('collapseNode', node);
			ldTree.treeview('toggleNode', ['refresh', node]);
		},
		
		onClickInternal : function(event, node, onNodeClick, onNodeDblClick) {
			if (event.type != 'nodeSelected' && this.nodeLastSelectedId !== node.nodeId){
				this.nodeLastSelectedTime = null;
			}
			if (!onNodeDblClick || event.type != 'nodeSelected') {
				if (onNodeClick) {
					onNodeClick(event, node);
				}
				return;
			}
			
			var currentTimestamp = event.timeStamp;
			if (this.nodeLastSelectedId == node.nodeId &&
				this.nodeLastSelectedTime && currentTimestamp - this.nodeLastSelectedTime < this.DOUBLE_CLICK_PERIOD) {
				onNodeDblClick(event, node);
			} else if (onNodeClick){
				onNodeClick(event, node);
			}
			this.nodeLastSelectedTime = currentTimestamp;
			this.nodeLastSelectedId = node.nodeId;
		},
		
		/**
		 * Highlights the folder where existing LD already resides
		 */
		selectFolder :  function(folder) {
			// if there are no children or stop condition (no path) was reached
			if (!folder.nodes || folder.nodes.length === 0 || layout.folderPathCurrent == null) {
				return;
			}
			var chosenFolder = null;
			// are there any steps left?
			if (layout.folderPathCurrent.length > 0) {
				// look for target folder in children
				var folderID = layout.folderPathCurrent.shift();
				$.each(folder.nodes, function(index, child){
					if (folderID == child.folderID) {
						chosenFolder = child;
						return false;
					}
				});
			}
			// if last piece of folder path was consumed, set stop condition
			if (layout.folderPathCurrent.length === 0) {
				layout.folderPathCurrent = null;
			}
			// no folder found or path was empty from the beginning
			if (!chosenFolder) {
				return;
			}
			// if folder, highlight and expand
			if (!chosenFolder.isLeaf) {
				ldTree.treeview('selectNode', chosenFolder);
			}
		}
}