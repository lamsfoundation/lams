<%-- LAMS code which wraps up the mapjs code into something usable on a page. All pages that want the mindmap should include this file. --%>
<%-- They also need to include the following files:  --%>
<%-- <link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link> --%>
<%-- <link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link> --%>
<%-- <link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link> --%>
<%-- <script src="${lams}includes/javascript/jquery.minicolors.min.js"></script> --%>
<%-- <script src="${tool}includes/javascript/jquery.timer.js"></script> --%>
<%-- <script src="${tool}includes/javascript/mapjs/main.js"></script> --%>
<%-- <script src="${tool}includes/javascript/mapjs/underscore-min.js"></script> --%>

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
		
		// color picker
   		$('#background-color').minicolors({
			control: 'wheel',
			theme: 'bootstrap',
			swatches: ['#ff0000', '#ffff00', '#0000ff', '#008000', '#00ff00', '#800080', '#ff00ff', '#00ffff', '#87ceeb', '#ffd700', '#ffa500', '#ffffff', '#9e9e9e', '#000000'],
  			change: function(value, opacity) {
			    window.mapModel['updateStyle']('toolbar', 'background', value);
			}
 			<c:if test="${multiMode}">
			, changeDelay: 1500  			// Wait a bit before updating node sending to server.
			</c:if>
		});
		$('.minicolors').css('float','right');
		$('#background-color').minicolors({
		});

	}
	
	// Greys out the buttons to discourage people from using them. The mindmap javascript code won't let them update the values.
	function disableEditButtons() {
		$(".editNode").attr("disabled", true);
		$(".removeSubIdea").attr("disabled", true);
		$(".updateStyle").attr("disabled", true);		
	}

	function enableEditButtons() {
		$(".editNode").attr("disabled", false);
		$(".removeSubIdea").attr("disabled", false);
		$(".updateStyle").attr("disabled", false);		
	}

	function updateColorPicker() {
	    swatch = $('.minicolors').find('.minicolors-input-swatch').find('span').css("backgroundColor",$('#background-color').val());
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
			// console.log("onIdeaChangedLAMS: action "+action+" args "+args);
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
					        			debugger;
	 								contentAggregate.addSubIdea(action.nodeId, action.title, action.childNodeId);
					        			var newChildNode = contentAggregate.findSubIdeaById(action.childNodeId);
	 								newChildNode.attr = {};
	 								newChildNode.attr.contentLocked = true;
					        			if ( action.color ) {
		 								newChildNode.attr.style = {};
						        			newChildNode.attr.style.background = action.color;
					        			}
					        		} else if ( action.type == 2 ) {
					        			var ideaToUpdate = contentAggregate.findSubIdeaById(action.nodeId);
					        			if ( ! ideaToUpdate.attr ) {
					        				ideaToUpdate.attr = {};
					        			}
					        			if ( ! ideaToUpdate.attr.style ) {
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
			if ( ! (action == 'updateAttr' && args[1] == 'collapsed') ) {
				alert("label.no.changes.can.be.made.reloading.ideas");
				loadRootIdea(mindMupContent);
			}
		}
	</c:otherwise>
	</c:choose>

</c:when>
<c:otherwise>
	
	<c:if test="${contentEditable and (mode == 'learner' || mode == 'author')}">

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
	 		if ( !savingNow ) {
				saveSingleUserMindmap();
	 		}
		});
	
	</c:if>


</c:otherwise>
</c:choose> 
	
</script>	

	<c:if test="${contentEditable and (mode == 'learner' || mode == 'author')}">
		<div>
		<c:if test="${not multiMode}">
			<div class="hint"><fmt:message key="label.your.mindmap.saved.every.minute"/><lams:WaitingSpinner id="spinnerArea_Busy" showInline="true"/></div>
		</c:if>
		</div>
	</c:if>	

	<div id="mindmap-controls">
	
		<!-- Color picker must be outside the next div or it can't float on top of the mindmap. The float is done in javascript. -->
		<input type="text" id="background-color" class='updateStyle form-control input-sm' data-mm-target-property='background' size="7" width="180px"></input>
 
		<div>
			<div class="btn-group btn-group-sm" role="group">
			<a href="#" class="resetView btn btn-default btn-sm"><fmt:message key='label.zoom'/>:</a>
			<a href="#" class="resetView btn btn-default btn-sm"><fmt:message key='label.zoom.reset'/></a>
			<a href="#" class="scaleUp btn btn-default btn-sm" title="<fmt:message key='label.zoom.increase'/>"><i class="fa fa-lg fa-search-plus"></i></a>
			<a href="#" class="scaleDown btn btn-default btn-sm" title="<fmt:message key='label.zoom.decrease'/>"><i class="fa fa-lg fa-search-minus"></i></a>
			</div>
			
			<div style="display:inline-block" role="group">
		 	<a href="#" class="toggleCollapse btn btn-default btn-sm" title="<fmt:message key='label.expand.collapse.idea'/>"><i class="fa fa-lg fa-navicon"></i><span class="hidden-xs">&nbsp;<fmt:message key='label.expand.collapse.idea'/></span></a>
		<c:if test="${contentEditable}">
			<a href="#" class="addSubIdea btn btn-default btn-sm" title="<fmt:message key='label.add.idea'/>"><i class="fa fa-lg fa-plus-square-o"></i><span class="hidden-xs">&nbsp;<fmt:message key='label.add.idea'/></span></a>
			<a href="#" class="editNode btn btn-default btn-sm" title="<fmt:message key='label.edit.idea.text'/>"><i class="fa fa-lg fa-pencil-square-o"></i><span class="hidden-xs">&nbsp;<fmt:message key='label.edit.idea.text'/></span></a>		
			<a href="#" class="removeSubIdea btn btn-default btn-sm" title="<fmt:message key='label.delete.idea'/>"><i class="fa fa-lg fa-trash-o"></i><span class="hidden-xs">&nbsp;<fmt:message key='label.delete.idea'/></span></a>
	<%-- Not yet implemented in back end  --%> 
	<%-- 		<input type="button" data-mm-action="export-image" value="Export To Image"/>  --%> 
	<%--  		<input type="button" class="insertRoot" value="add root node"></input>  --%> 
	<%-- 		<input type="button" class="makeSelectedNodeRoot" value="make root"></input>  --%> 
	 	</c:if>
	 		</div>
 		</div>
	</div>

 	<div id="mindmap-container"></div>
 	<style id="themecss">
	</style>

