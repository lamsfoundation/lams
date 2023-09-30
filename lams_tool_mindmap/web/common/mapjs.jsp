<%-- LAMS code which wraps up the mapjs code into something usable on a page. All pages that want the mindmap should include this file. --%>
<%-- They also need to include the following files:  --%>
<%-- <link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link> --%>
<%-- <link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link> --%>
<%-- <link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link> --%>
<%-- <script src="${lams}includes/javascript/jquery.minicolors.min.js"></script> --%>
<%-- <script src="${tool}includes/javascript/jquery.timer.js"></script> --%>
<%-- <script src="${tool}includes/javascript/mapjs/main.js"></script> --%>
<%-- <script src="${tool}includes/javascript/mapjs/underscore-min.js"></script> --%>
<%-- <script src="${tool}includes/javascript/fullscreen.js"></script> --%>

<lams:JSImport src="includes/javascript/websocket.js" />
<script type="text/javascript">

	var initialActionId = null,  // multiuser request tracking
		lastActionId = null, 	// multiuser request tracking
		// Requests already processed:
		// If we process all requests that come back from the server from a poll we will reprocess our own requests
		// so keep a list and check. If we just kept the lastActionId and update it when we do a save, 
		// we miss any requests from other users sent between the last poll and our save. 
		requestsProcessed = new Array(), 
		contentAggregate = null,	// mindmup variable - dummy root idea that has all visible ideas
		mindMupContent = null; // mindmup content processor - used for reloading map.

	$(document).ready(function(){
		setupFullScreenEvents();
	});

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
	        url: "${url=='author'? 'setMindmapContentJSON.do':'../learning/setMindmapContentJSON.do'}",
	        data: { mindmapId: "${mindmapId}", userId: "${userUid}", 
				sessionId: "${sessionId}", mode: "${mode}" } , 
	        dataType: "json", 
	        success: function (response) {
		        	initialActionId = response.lastActionId;
		        lastActionId = initialActionId;
		        var idea = content(response.mindmap);

		        <c:if  test="${multiMode}">
    				window.mapModel.setLabelGenerator(labelGenerator, "CreatorNameLabels");
    				</c:if>
    				
			    window.mapModel.setIdea(idea);
		        <c:if test="${!contentEditable}">
		    		    window.mapModel.setEditingEnabled(false);
    			    </c:if>
		        <c:if  test="${multiMode && contentEditable}">
			        idea.addEventListener('changed', onIdeaChangedLAMS); 
		        </c:if>
		        contentAggregate = window.mapModel.getIdea();
		        <c:if  test="${multiMode && contentEditable}">

				<%-- Websockets used to get the node updates from the server --%>
				<%-- init the connection with server using server URL but with different protocol --%>
				let websocket = initWebsocket('mindmapNodes${sessionId}',
						'<lams:WebAppURL />'.replace('http', 'ws')
						+ 'learningWebsocket?toolSessionID=${sessionId}&lastActionId=' + initialActionId);

				if (websocket) {
					// when the server pushes new inputs
					websocket.onmessage = function (e) {
						// create JSON object
						var response = JSON.parse(e.data);

						if ( ! response.actions ) {
							return;
						}

						var valuesChanged = false;
						for ( i=0; i<response.actions.length; i++ ) {
							var action = response.actions[i];
							requestId =  action.actionId;
							if ( ! action.actionId ) {
								abortLoad('<fmt:message key="error.unable.to.load.mindmap"/>');
								return;
							}
							// only process requests we have not done already otherwise we would redo any we trigger
							if ( requestId > initialActionId && requestsProcessed.indexOf(requestId) == -1 ) {
								if ( action.type == 0 ) {
									customRemoveSubIdea(action.nodeId);
								} else if ( action.type == 1 ) {
									updateUnsavedNodeIds(action.childNodeId);
									// add node response.nodeId, response.title, response.color
									contentAggregate.addSubIdea(action.nodeId, action.title, action.childNodeId);
									var newChildNode = contentAggregate.findSubIdeaById(action.childNodeId);
									newChildNode.attr = {};
									newChildNode.attr.contentLocked = true;
									newChildNode.creator = action.creator;
									if ( action.color ) {
										newChildNode.attr.style = {};
										newChildNode.attr.style.background = action.color;
									}
								} else if ( action.type == 2 ) {
									// update colour - this call updates the node on screen but also triggers the change
									// action which will call onIdeaChangedLAMS() - so the change to the other user's node
									// will need to be ignored.
									contentAggregate.mergeAttrProperty(action.nodeId, 'style', 'background', action.color);
								} else if ( action.type == 3 ) {
									// update title
									var ideaToUpdate = contentAggregate.findSubIdeaById(action.nodeId);
									ideaToUpdate.title = action.title;
								} else {
									abortLoad('<fmt:message key="error.unable.to.load.mindmap"/>');
								}
								requestsProcessed.push( requestId );
								updateLastActionId( requestId );
								valuesChanged = true;
							}
						}
						if ( valuesChanged ) {
							window.mapModel.rebuildRequired();
						}

						// reset ping timer
						websocketPing('mindmapNodes${sessionId}', true);
					};
				}
			    </c:if>
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

	// set the creator labels for Multimode mindmaps
	labelGenerator = function(idea) {
		var 	labelMap = {};
		addLabel = function(idea) {
			if ( idea.creator ) {
				labelMap[idea.id] = idea.creator;
			}
			if ( idea.ideas ) { 
				Object.keys(idea.ideas).forEach(function(key) {
				    addLabel(idea.ideas[key]);
				});
 	    		}
		};
		addLabel(idea);
	    	return labelMap;
	}

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
			var ideaToUpdate = null,
				updateRequest = null;
			
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
				// change color - need to filter out the color changes triggered by server for other users' nodes.
				ideaToUpdate = contentAggregate.findSubIdeaById(args[0]);
				var locked = ideaToUpdate.attr ? ideaToUpdate.attr.contentLocked : false;
				if ( locked ) 
					return;
				updateRequest = { type: 2, id: args[0], background: args[2].background  };
			} else if ( action == 'updateTitle') {
				// update title
				updateRequest = { type: 3, id: args[0], title: args[1] };
			} else {
				// skip 'addSubIdea' - wait till title initialised
				// do not implement undo, paste - too complicated at this point
				return;
			}
	
			$.ajax({ 
		        method: "POST", 
		        url: "notifyServerActionJSON.do", 
		        data: { mindmapId: "${mindmapId}", userId: "${userUid}", 
					sessionId: "${sessionId}", lastActionId: lastActionId, actionJSON:  JSON.stringify(updateRequest) } , 
		        dataType: "json", 
		        success: function (response) {
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
				        		
		        			} else if ( args[0] > response.nodeId ) {
		        				// unexpected case - tell user to abort so we can't make anything worse.
			        			abortSave();
		        				return;
		        			}
		        			ideaToUpdate.creator = "${currentMindmapUser}";
			        		window.mapModel.rebuildRequired();
		        		}	        		
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
		}
		
 		// Functionality copied from commandProcessors.removeSubIdea = function (...) 
		// Cannot use the standard function as that would be picked up by the onIdeaChangedLAMS listener
		// Return all the elements for which a truth test fails.
		function customRemoveSubIdea(subIdeaId) {
			const parent = contentAggregate.findParent(subIdeaId) || contentAggregate,
				oldRank = parent && parent.findChildRankById(subIdeaId),
				oldIdea = parent && parent.ideas[oldRank],
				oldLinks = contentAggregate.links,
				removedNodeIds = {};
	
			if (!oldRank) {
				return false;
			}
			// LAMS Modification comment out => entries so that it will work on IE11. These features are not being used.
			// oldIdea.traverse((traversed)=> removedNodeIds[traversed.id] = true);
			delete parent.ideas[oldRank];
	
			contentAggregate.links = _.reject(contentAggregate.links, function (link) {
				return removedNodeIds[link.ideaIdFrom]  || removedNodeIds[link.ideaIdTo];
			});
		} 

		function abortLoad(msg) {
			alert(msg);
			window.mapModel.setEditingEnabled(false);
		}

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
			        url: "saveLastMindmapChanges.do", 
			        data: { 
			        		mindmapId: "${mindmapId}", 
			        		userId: "${userUid}", 
			        		toolSessionID: "${sessionId}",
			        		content: JSON.stringify(window.mapModel.getIdea()) 
			        },
			        dataType: "json", 
			        success: function (response) {
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

<c:if test="${param.allowPrinting}">
	function showPrintView(){
    	var printWindow = window.open('<c:url value="/learning/getPrintMindmap.do?toolSessionID=${sessionId}&userUid=${userUid}"/>',
    	    						  'MindmapPrint', 'width=1152,height=900,scrollbars=yes,resizable=yes');
		if (window.focus) {
			printWindow.focus();
		}
	}
</c:if>
</script>	

	<div class="full-screen-content-div">
	<div class="full-screen-flex-div">
	<div class="full-screen-main-div">
	
	<c:if test="${contentEditable and (mode == 'learner' || mode == 'author')}">
		<div>
		<c:if test="${not multiMode}">
			<div class="hint"><fmt:message key="label.your.mindmap.saved.every.minute"/><lams:WaitingSpinner id="spinnerArea_Busy" showInline="true"/></div>
		</c:if>
		</div>
	</c:if>	

	<div id="mindmap-controls">
	
		<!-- Color picker & expand buttons must be outside the next div or it can't float on top of the mindmap. The float is done in javascript. -->
		<div style="display:inline" role="group">
        <a href="#" class="btn btn-default btn-sm full-screen-launch-button pull-right loffset5" id="expand" onclick="javascript:launchIntoFullscreen(this)"><i class="fa fa-arrows-alt" aria-hidden="true"></i></a> 
        <a href="#" class="btn btn-default btn-sm full-screen-exit-button pull-right loffset5" id="shrink" onclick="javascript:exitFullscreen()" style="display: none;"><i class="fa fa-compress" aria-hidden="true"></i></a> 
		
		<c:if test="${param.allowPrinting}">
			<a href="#" class="btn btn-default btn-sm pull-right loffset5" id="print" onclick="javascript:showPrintView()"
			   title="<fmt:message key='button.print'/>"><i class="fa fa-print" aria-hidden="true"></i></a> 
		</c:if>
		
		<input type="text" id="background-color" class='updateStyle form-control input-sm' data-mm-target-property='background' size="7" width="180px">

		</div>
		 
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
	<%--  		<input type="button" class="insertRoot" value="add root node">  --%> 
	<%-- 		<input type="button" class="makeSelectedNodeRoot" value="make root">  --%> 
	 	</c:if>
	 		</div>
 		</div>
	</div>

 	<div id="mindmap-container"></div>
 	<style id="themecss">
	</style>
	
	</div>
	</div>
	</div>