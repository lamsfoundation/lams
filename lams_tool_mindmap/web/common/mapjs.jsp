<script type="text/javascript">

	var lastActionId = null, 	// multiuser request tracking
		doPoll = false, 			// multiuser - only poll once set up and not while saving a node
		// Requests already processed:
		// If we process all requests that come back from the server from a poll we will reprocess our own requests
		// so keep a list and check. If we just kept the lastActionId and update it when we do a save, 
		// we miss any requests from other users sent between the last poll and our save. 
		requestsProcessed = new Array(), 
		contentAggregate = null,	// mindmup variable - dummy root idea that has all visible ideas
		mindMupContent = null; // mindmup content processor - used for reloading map.

	// *** getMindmapContainerId() and  loadRootIdea(content) called by customised mapjs code ***
	// window.mapModel is set up by mapjs in main.js. 
	// returns the jquery selector for the mindmap container

	function getMindmapContainerId() {
		return "mindmap-container";
	}
	
	function loadRootIdea(content) {
		mindMupContent = content;
		$.ajax({ 
	        method: "POST", 
	        url: "${url=='author'?'authoring.do':'learning.do'}", 
	        data: { dispatch: "setMindmapContentJSON", mindmapId: "${mindmapId}", userId: "${userId}", 
				sessionId: "${sessionId}", mode: "${mode}" } , 
	        dataType: "json", 
	        success: function (response) {
		        lastActionId = response.lastActionId;
		        var idea = content(response.mindmap);
		        window.mapModel.setIdea(idea);
		        <c:if test="${!contentEditable}">
		    		    window.mapModel.setEditingEnabled(false);
    			    </c:if>
		        <c:if  test="${multiMode}">
			        idea.addEventListener('changed', onIdeaChangedLAMS); 
		        </c:if>
		        contentAggregate = window.mapModel.getIdea();
		        doPoll = true;
			},
			error: function (response) {
				alert('<fmt:message key="error.unable.to.load.mindmap"/>')
			}
		});
	}
	
<c:choose> 
<c:when test="${multiMode}">

    <c:choose>
    <c:when test="${contentEditable}">
	 	function updateLastActionId( newActionId ) {
	 		if ( newActionId > lastActionId )
	 			lastActionId = newActionId;
	 	}
		// when we create a node based on data from the server, we need to reset the mindmup counter so it starts after the new node id.
		function resetIdCounter() {
			content.cachedId = null;
		}
	 
		function onIdeaChangedLAMS(action, args, sessionId) {
			console.log("onIdeaChangedLAMS: action "+action+" args "+args);
			var ideaToUpdate = null,
				updateRequest = null;
			
	        doPoll = false;
			if ( action == 'removeSubIdea') {
				// delete node
				updateRequest = { type: 0, id: args[0] };
			} else if ( action == 'initialiseTitle') {
				// create node
				ideaToUpdate = contentAggregate.findSubIdeaById(args[0]);
				if ( ! ideaToUpdate ) 
					alert('<fmt:message key="error.occured.during.save"/>');
				var parentIdea = contentAggregate.findParent(args[0]);
				if ( ! parentIdea ) 
					alert('<fmt:message key="error.occured.during.save"/>');
				updateRequest = { type: 1, id: args[0], parentId: parentIdea.id, title: args[1] };
			} else if ( action == 'updateAttr' && args[1] == 'style') {
				// change color
				updateRequest = { type: 2, id: args[0], background: args[2].background  };
			} else if ( action == 'updateTitle') {
				// update title
				updateRequest = { type: 3, id: args[0], title: args[1] };
			} else {
				// skip 'addSubIdea' - wait till title initialised
				// do not implement undo, paste - too complicated at this point
		        doPoll = true;
				return;
			}
	
			$.ajax({ 
		        method: "POST", 
		        url: "learning.do", 
		        data: { dispatch: "notifyServerActionJSON", mindmapId: "${mindmapId}", userId: "${userId}", 
					sessionId: "${sessionId}", lastActionId: lastActionId, actionJSON:  JSON.stringify(updateRequest) } , 
		        dataType: "json", 
		        success: function (response) {
		        		console.log(response);
		        		if ( ! response.ok  || ( action == 'initialiseTitle' && ! response.nodeId) ) {
		        			abortSave();
		        			return;
		        		}
		        		requestsProcessed.push(response.requestId);
		        		
		        		// if it is a new node make sure we are using the server id
		        		if ( action == 'initialiseTitle' ) {
		        			// someone else has saved a node on the server and bumped up the unique node id
		        			if ( args[0] < response.nodeId ) {
		        				updateUnsavedNodeIds(response.nodeId);
		        				ideaToUpdate.id = response.nodeId;    
				        		contentAggregate.invalidateIdCache();
				        		window.mapModel.rebuildRequired();
				        		
		        			} else if ( args[0] > response.nodeId ) {
		        				// unexpected case - tell user to abort so we can't make anything worse.
			        			abortSave();
		        				return;
		        			}
		        		}	        		
		        		doPoll = true;
				},
				error: function (response) {
					// would be nice to trigger undo, but undo what? the UI may have moved on a done more stuff in the meantime.
		    			abortSave();
		    			return;
				}
	
			});
	
		}

		// user may have conflicting node numbers due to some else creating nodes so move them to new numbers.
		function updateUnsavedNodeIds(newServerNodeId) {
	    		var nodesToFix = new Array(),
				i=0,
	    			checkNodeId = newServerNodeId,
				fixNode = contentAggregate.findSubIdeaById(checkNodeId++);
			while ( fixNode ) {
				nodesToFix[i++] = fixNode;
				fixNode = contentAggregate.findSubIdeaById(checkNodeId++);
			}
			for ( i = i-1; i>-1; i--) {
				nodesToFix[i].id = nodesToFix[i].id + 1;
			}
		}

		function abortSave() {
			alert('<fmt:message key="error.occured.during.save"/>');
			doPoll = true;
		}
		
 		// Functionality copied from commandProcessors.removeSubIdea = function (...) 
		// Cannot use the standard function as that would be picked up by the onIdeaChangedLAMS listener
		// Return all the elements for which a truth test fails.
		function customRemoveSubIdea(subIdeaId) {
			parent = contentAggregate.findParent(subIdeaId) || contentAggregate,
				oldRank = parent && parent.findChildRankById(subIdeaId),
				oldIdea = parent && parent.ideas[oldRank],
				oldLinks = contentAggregate.links,
				removedNodeIds = {};
	
			if (!oldRank) {
				return false;
			}
			oldIdea.traverse((traversed)=> removedNodeIds[traversed.id] = true);
			delete parent.ideas[oldRank];
	
			contentAggregate.links = _.reject(contentAggregate.links, function (link) {
				return removedNodeIds[link.ideaIdFrom]  || removedNodeIds[link.ideaIdTo];
			});
		} 
		
		function abortLoad(msg) {
			alert(msg);
			doPoll = false;
			window.mapModel.setEditingEnabled(false);
		}

		// get updates from server every one minute. If it fails then quit polling and need to manually reload the page.
	 	$.timer(60000, function (timer) {
	 		if ( doPoll ) {
	 			doPoll = false;
		 		$.ajax({ 
			        method: "POST", 
			        url: "learning.do", 
			        data: { dispatch: "pollServerActionJSON", mindmapId: "${mindmapId}", userId: "${userId}", 
						sessionId: "${sessionId}", lastActionID: lastActionId } , 
			        dataType: "json", 
			        success: function (response) {
			        		// need to work out how to update model
			        		console.log(response);
			        		if ( ! response.actions ) {
			        			doPoll = true;
			        			return;
			        		}
			        		
			        		var requestId = false;
			        		for ( i=0; i<response.actions.length; i++ ) {
			        			var action = response.actions[i];
		        				requestId =  action.actionId;
			        			if ( ! action.actionId ) {
			        				abortLoad('<fmt:message key="error.unable.to.load.mindmap"/>');
			        				return;
			        			}
			        			// only process requests we have not done already otherwise we would redo any we trigger
			        			if ( requestsProcessed.indexOf(requestId) == -1 ) {
					        		if ( action.type == 0 ) {
					        			customRemoveSubIdea(action.nodeId);
					        		} else if ( action.type == 1 ) {
	 								updateUnsavedNodeIds(action.childNodeId);
					        			// add node response.nodeId, response.title, response.color
	 								contentAggregate.addSubIdea(action.nodeId, action.title, action.childNodeId);
					        			if ( action.color ) {
						        			var newChildNode = contentAggregate.findSubIdeaById(action.childNodeId);
						        			newChildNode = action.color;
					        			}
					        		} else if ( action.type == 2 ) {
					        			var ideaToUpdate = contentAggregate.findSubIdeaById(action.nodeId);
					        			if ( ! ideaToUpdate.attr ) {
					        				ideaToUpdate.attr = {};
					        			}
					        			if ( ! ideaToUpdate.style ) {
					        				ideaToUpdate.attr.style = {};
					        			}
									ideaToUpdate.attr.style.background = action.color;
					        		} else if ( action.type == 3 ) {
					        			// update title
					        			var ideaToUpdate = contentAggregate.findSubIdeaById(action.nodeId);
					        			ideaToUpdate.title = action.title;    		
					        		} else {
					        			console.log("Unexpected change received from server. Type "+action.type+" Full action: "+action);
					        		}
					        		requestsProcessed.push( requestId );
			        				updateLastActionId( requestId );
				        		}
			        		}
			        		if ( requestId ) {
				        		window.mapModel.rebuildRequired();
			        		}
			        		doPoll = true;
					},
					error: function (response) {
						abortLoad('<fmt:message key="error.unable.to.load.mindmap"/>');
					}
				});
	 		}
		});
	</c:when>
	<c:otherwise>
		function onIdeaChangedLAMS(action, args, sessionId) {
			debugger;
			if ( ! (action == 'updateAttr' && args[1] == 'collapsed') ) {
				alert("label.no.changes.can.be.made.reloading.ideas");
				loadRootIdea(mindMupContent);
			}
		}
	</c:otherwise>
	</c:choose>

</c:when>
<c:otherwise>
	
	<c:if test="${contentEditable and not mode eq 'author'}">

		var savingNow = false;
	
		function saveSingleUserMindmap() {
			if ( contentAggregate && !savingNow ) {
				savingNow = true;
				disableButtons();
		 		$.ajax({ 
			        method: "POST", 
			        url: "learning.do", 
			        data: { 
			        		dispatch: "saveLastMindmapChanges", 
			        		mindmapId: "${mindmapId}", 
			        		userId: "${userId}", 
			        		toolSessionID: "${sessionId}",
			        		content: JSON.stringify(window.mapModel.getIdea()) 
			        },
			        dataType: "json", 
			        success: function (response) {
			        		console.log(response);
			        		if ( ! response.ok ) {
			        			alert('<fmt:message key="error.occured.during.save"/>');
			        			return;
			        		}
					   enableButtons();
					   savingNow = false;
			        },
					error: function (response) {
						alert('<fmt:message key="error.occured.during.save"/>');
			    			return;
					}
		 		});
	 		}
		}
	
		// saving Mindmap every one minute - do not bank up saves if server is taking too long.
	 	$.timer(60000, function (timer) {
	 		if ( !savingNow )
				saveSingleUserMindmap();
		});
	
	</c:if>


</c:otherwise>
</c:choose> 
	
</script>	

	<c:if test="${contentEditable and not mode eq 'author'}">
		<div>
		<c:if test="${not multiMode}">
			<div class="hint"><fmt:message key="label.your.mindmap.saved.every.minute"/><lams:WaitingSpinner id="spinnerArea_Busy" showInline="true"/></div>
		</c:if>
		</div>
	</c:if>	

	<div id="mindmap-controls">
		<div class="btn-group btn-group-sm" role="group">
		<input type="button" class="resetView btn btn-default btn-sm" value="<fmt:message key='label.zoom'/>:"></input>
		<input type="button" class="resetView btn btn-default btn-sm" value="<fmt:message key='label.zoom.reset'/>"></input>
		<input type="button" class="scaleUp btn btn-default btn-sm" value="<fmt:message key='label.zoom.increase'/>"></input>
		<input type="button" class="scaleDown btn btn-default btn-sm" value="<fmt:message key='label.zoom.decrease'/>"></input>
		</div>
	 	<input type="button" class="toggleCollapse btn btn-default btn-sm" value="<fmt:message key='label.expand.collapse.idea'/>"></input>
	<c:if test="${contentEditable}">
		<input type="button" class="addSubIdea btn btn-default btn-sm" value="<fmt:message key='label.add.idea'/>"></input>
		<input type="button" class="editNode btn btn-default btn-sm" value="<fmt:message key='label.edit.idea.text'/>"></input> 
		<input type="button" class="removeSubIdea btn-default btn btn-sm" value="<fmt:message key='label.delete.idea'/>"></input>
<!-- Not yet implemented in back end
		<input type="button" data-mm-action="export-image" value="Export To Image"/>
 		<input type="button" class="insertRoot" value="add root node"></input>
		<input type="button" class="makeSelectedNodeRoot" value="make root"></input>
 	Background needs to be turned into a colour picker.
 -->		Background: <input type="text" class='updateStyle' data-mm-target-property='background'></input> 
 	</c:if>
	</div>
 	<div id="mindmap-container"></div>
 	<style id="themecss">
	</style>

