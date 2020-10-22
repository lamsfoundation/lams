/**
 * This file contains methods for menu in Authoring.
 */

var MenuLib = {
	/**
	 * Creates a new annotation label.
	 */
	addAnnotationLabel : function() {
		HandlerLib.resetCanvasMode();
		
		layout.infoDialog.data('show')(LABELS.ANNOTATION_LABEL_PLACE_PROMPT, true);

		canvas.css('cursor', 'pointer').click(function(event){
			layout.infoDialog.modal('hide');
			var translatedEvent = GeneralLib.translateEventOnCanvas(event),
				x = translatedEvent[0],
				y = translatedEvent[1];
			
			HandlerLib.resetCanvasMode(true);
			
			DecorationLib.addLabel(x, y);
		});
	},
	
	
	/**
	 * Creates a new annotation Region.
	 */
	addAnnotationRegion : function() {
		HandlerLib.resetCanvasMode();
		
		layout.infoDialog.data('show')(LABELS.ANNOTATION_REGION_PLACE_PROMPT, true);
	
		canvas.css('cursor', 'crosshair').mousedown(function(event){
			layout.infoDialog.modal('hide');
			var	targetElement = Snap.getElementByPoint(event.pageX, event.pageY);
			
			if (targetElement.type == 'svg') {
				HandlerDecorationLib.drawRegionStartHandler(event);
			} else {
				// cancel if clicked on some existing element, not canvas
				HandlerLib.resetCanvasMode(true);
			}
		});
	},
	
	
	/**
	 * Run when branching is selected from menu. Allows placing branching and converge points on canvas.
	 */
	addBranching : function(){
		HandlerLib.resetCanvasMode();
		
		layout.infoDialog.data('show')(LABELS.BRANCHING_START_PLACE_PROMPT, true);
		
		layout.addBranchingStart = true;
		
		var branchingActivity = null;
		canvas.css('cursor', 'pointer').click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var translatedEvent = GeneralLib.translateEventOnCanvas(event),
				x = translatedEvent[0] - 6,
				y = translatedEvent[1] - 8;
			
			// if it is start point, branchingActivity is null and constructor acts accordingly
			var branchingEdge = new ActivityDefs.BranchingEdgeActivity(null, null, x, y, null, false, null, branchingActivity);
			layout.activities.push(branchingEdge);
			
			if (branchingActivity) {
				// converge point was just place, end of function
				layout.addBranchingStart = null;
				HandlerLib.resetCanvasMode(true);
				
				layout.infoDialog.modal('hide');
				
				GeneralLib.setModified(true);
			} else {
				// extract main branchingActivity structure from created start point
				branchingActivity = branchingEdge.branchingActivity;
				layout.addBranchingStart = branchingEdge;
				layout.infoDialog.data('show')(LABELS.BRANCHING_END_PLACE_PROMPT, true);
			}
		});
	},
	

	/**
	 * Creates a new floating activity.
	 */
	addFloatingActivity : function() {
		if (layout.floatingActivity) {
			// there can be only one
			return;
		}
		HandlerLib.resetCanvasMode();
		
		layout.infoDialog.data('show')(LABELS.SUPPORT_ACTIVITY_PLACE_PROMPT, true);
	
		canvas.css('cursor', 'pointer').click(function(event){
			layout.infoDialog.modal('hide');
			var translatedEvent = GeneralLib.translateEventOnCanvas(event),
				x = translatedEvent[0],
				y = translatedEvent[1];
			
			GeneralLib.setModified(true);
			HandlerLib.resetCanvasMode(true);

			// do not add it to layout.activities as it behaves differently
			new ActivityDefs.FloatingActivity(null, null, x, y);
			
			// there can be only one, so disable the button
			$('#floatingActivityButton').attr('disabled', 'disabled')
									 	.css('opacity', 0.2);
		});
	},
	

	/**
	 * Creates a new gate activity.
	 */
	addGate : function() {
		HandlerLib.resetCanvasMode();
		
		canvas.css('cursor', 'url("' + layout.toolMetadata.gate.iconPath + '"), move').click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var translatedEvent = GeneralLib.translateEventOnCanvas(event),
				x = translatedEvent[0],
				y = translatedEvent[1] + 2;
			
			layout.activities.push(new ActivityDefs.GateActivity(null, null, x, y));
			
			GeneralLib.setModified(true);
			HandlerLib.resetCanvasMode(true);
		});
	},
	
	
	/**
	 * Creates a new grouping activity.
	 */
	addGrouping : function() {
		if (layout.isGroupingStarted) {
			layout.isGroupingStarted = false;
			HandlerLib.resetCanvasMode(true);
			$('#groupButton').blur();
		} else {
			layout.isGroupingStarted = true;
			HandlerLib.resetCanvasMode();
			
			canvas.css('cursor', 'url("' + layout.toolMetadata.grouping.iconPath + '"), move')
				.click(function(event){
					layout.isGroupingStarted = false;
					// pageX and pageY tell event coordinates relative to the whole page
					// we need relative to canvas
					var translatedEvent = GeneralLib.translateEventOnCanvas(event),
						x = translatedEvent[0] - 47,
						y = translatedEvent[1] -  2;
		
					layout.activities.push(new ActivityDefs.GroupingActivity(null, null, x, y));
					
					GeneralLib.setModified(true);
					HandlerLib.resetCanvasMode(true);
				});
		}
	},
	

	/**
	 * Creates a new optional activity.
	 */
	addOptionalActivity : function() {
			HandlerLib.resetCanvasMode();
			
			layout.infoDialog.data('show')(LABELS.OPTIONAL_ACTIVITY_PLACE_PROMPT, true);
		
			canvas.css('cursor', 'pointer').click(function(event){
				layout.infoDialog.modal('hide');
				var translatedEvent = GeneralLib.translateEventOnCanvas(event),
					x = translatedEvent[0],
					y = translatedEvent[1];
				
				GeneralLib.setModified(true);
				HandlerLib.resetCanvasMode(true);
	
				layout.activities.push(new ActivityDefs.OptionalActivity(null, null, x, y));
			});
	},
	

	/**
	 * Creates a new transition.
	 */
	addTransition : function() {
		if (layout.isTransitionStarted) {
			layout.isTransitionStarted = false;
			HandlerLib.resetCanvasMode(true);
			layout.infoDialog.modal('hide');
			$('#transitionButton').blur();
		} else {
			layout.isTransitionStarted = true;
			HandlerLib.resetCanvasMode();
			
			layout.infoDialog.data('show')(LABELS.TRANSITION_PLACE_PROMPT, true);
			
			canvas.css('cursor', 'pointer').click(function(event){
				layout.isTransitionStarted = false;
				
				layout.infoDialog.modal('hide');
				
				var startActivity = null,
					targetElement = Snap.getElementByPoint(event.pageX, event.pageY);
				if (targetElement) {
					startActivity = ActivityLib.getParentObject(targetElement);
					if (startActivity) {
						HandlerTransitionLib.drawTransitionStartHandler(startActivity, null, event.pageX, event.pageY);
					}
				}
			});
		}
	},
	
	
	/**
	 * Mark an activity as ready for pasting.
	 */
	copyActivity : function(){
		$('#copyButton').blur();
		layout.copiedActivity = layout.selectedObject;
	},
	

	/**
	 * Opens a pop up for exporting LD.
	 */
	exportLearningDesign : function(){
		if (layout.modified || layout.activities.length == 0) {
			return;
		}
		
		// dialog for downloading the sequence as ZIP
		var exportLDDialog = showDialog('exportLDDialog',{
			'autoOpen' : false,
			'width'	   : 320,
			'height'   : 90,
			'draggable': false,
			'resizable': false,
			'modal'	   : true,
			'title'	   : LABELS.EXPORT_SEQUENCE_DIALOG_TITLE,
			'open'	   : function() {
				//dynamically create a form and submit it
				var exportExcelUrl = LAMS_URL + 'authoring/exportToolContent/export.do?learningDesignID=' + layout.ld.learningDesignID;
				var form = $('<form method="post" action="' + exportExcelUrl + '"></form>');
			    var hiddenInput = $('<input type="hidden" name="' + csrfTokenName + '" value="' + csrfTokenValue + '"></input>');
			    form.append(hiddenInput);
			    $(document.body).append(form);
			    form.submit();
			}
		}, false)
		.addClass('smallHeader')
		.click(function(){
			exportLDDialog.modal('hide');
		});
		$('#exportLDDialogContents').clone().attr('id', null).appendTo($('.modal-body', exportLDDialog).empty()).show();
		exportLDDialog.modal('show');
	},

	
	/**
	 * Creates a SVG image out of current SVG contents.
	 */
	exportSVG : function(download){
		ActivityLib.removeSelectEffect();
		
		var crop = MenuLib.getCanvasCrop();
		if (crop.x >= crop.x2) {
			return;
		}
		// set viewBox so content is nicely aligned
		var width = crop.x2 - crop.x + 2,
			height = crop.y2 - crop.y + 2;
			svg = $('svg', crop.canvasClone)[0];
			
		// need to set attributes using pure JS as jQuery sets them with lower case, which is unacceptable in SVG
		svg.setAttribute('viewBox', crop.x + ' ' + crop.y + ' ' + width + ' ' + height);
		svg.setAttribute('preserveAspectRatio', 'xMinYMin slice');
		svg.setAttribute('width', width);
		svg.setAttribute('height', height);
		
		// reset any cursor=pointer styles
		$('*[style*="cursor"]', svg).css('cursor', 'default');
		
		// append SVG CSS straight into SVG file
		$.ajax({
			'url'     : LAMS_URL + 'css/authoring-svg.css',
			'dataType': 'text',
			'async'   : false,
			'success' : function(css) {
				let styleElement = document.createElement("style"),
					styleContent = document.createTextNode(css);
				styleElement.appendChild(styleContent);
				svg.appendChild(styleElement);
			}
		});
		
		return crop.canvasClone.html();
	},
	
	
	/**
	 * Finds coordinates of canvas content, minus surrounding whitespace.
	 */
	getCanvasCrop : function(){
		var result = {
			x  : Number.MAX_VALUE,
			y  : Number.MAX_VALUE,
			x2 : 0,
			y2 : 0
		};
		
		var boxes = layout.activities.concat(layout.regions).concat(layout.labels);
		if (layout.floatingActivity) {
			boxes.push(layout.floatingActivity);
		}
		$.each(boxes, function() {
			var box = this.items.getBBox();
			if (box.x < result.x) {
				result.x = box.x;
			}
			if (box.y < result.y) {
				result.y = box.y;
			}
			if (box.x2 > result.x2) {
				result.x2 = box.x2;
			}
			if (box.y2 > result.y2) {
				result.y2 = box.y2;
			}
		});
		
		var canvasClone = result.canvasClone = canvas.clone();
		// remove the rubbish bin icon
		canvasClone.find('#rubbishBin').remove();
		// IE needs this. There are 2 xmlns declarations and no xmlns:xlink
		// jQuery attr() or prop() do not work
		var canvasHTML = canvasClone.html().replace(/xmlns="http:\/\/www.w3.org\/2000\/svg"/g, '').replace('systemLanguage=""','')
										   .replace('<svg ', '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" ');
		canvasClone.html(canvasHTML);
		
		return result;
	},
	
	
	/**
	 * Loads subfolders and LDs from the server.
	 */
	getFolderContents : function(folderID, canSave, canHaveReadOnly) {
		var result = null;
			
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
				result = [];
				// parse the response; extract folders and LDs
				if (response.folders) {
					$.each(response.folders, function(index){
						// folderID == -2 is courses folder
						var canSave = this.folderID > 0 && !this.isRunSequencesFolder;
						result.push({'type'				   : 'html',
									 'html'                : (this.isRunSequencesFolder ? LABELS.RUN_SEQUENCES_FOLDER : this.name)
																+ (canSave ? '' : ' <i class="fa fa-lock"></i>'),
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
						result.push({'type'				: 'html',
									 'label'			: this.name,
									 'html'             : this.name + (this.readOnly || !canModify ? ' <i class="fa fa-lock"></i>' : ''),
						  	         'isLeaf'           : true,
						  	         'learningDesignId' : this.learningDesignId,
						  	         'canHaveReadOnly'	: canHaveReadOnly,
						  	         'canModify'		: canModify,
						  	         'readOnly'			: this.readOnly
							        });
					});
				}
			}
		});
		
		return result;
	},
	
	/**
	 * Opens a pop up for importing LD. Loads the imported LD to canvas.
	 */
	importLearningDesign : function(){
		if (layout.modified && !confirm(LABELS.CLEAR_CANVAS_CONFIRM)) {
			return;
		}
		var dialog = showDialog("dialogImportLearningDesign", {
						'modal' : true,
						'height' : 500,
						'width' : 850,
						'title' : LABELS.IMPORT_DIALOG_TITLE,
						'open' : function() {
							var dialog = $(this);
							// load contents after opening the dialog
							$('iframe', dialog).attr('src', LAMS_URL + 'authoring/importToolContent/import.do').on('load', function(){
								// override the close function so it works with the dialog, not window
								this.contentWindow.closeWin = function(){
									dialog.modal('hide');
								}
							});
						},
						'close' : function(){
							// stop checking in LD was 
							clearInterval(loadCheckInterval);
							// completely delete the dialog
							$(this).remove();
						}
					}, false),
			currentLearningDesignID = null,
			regEx = /learningDesignID=(\d+)/g,
			// since window.onload does not really work after submitting a form inside the window,
			// this trick checks periodically for changes
			loadCheckInterval = setInterval(function(){
				var body = $('body', $('iframe', dialog).contents()).html(),
					match = regEx.exec(body);
				// check if ID was found and it's not the same as previously set
				if (match && match[1] != currentLearningDesignID) {
					currentLearningDesignID = match[1];
					// load the imported LD
					GeneralLib.openLearningDesign(currentLearningDesignID);
					
					// generate images of the imported LD
					GeneralLib.saveLearningDesignImage();
				}
			}, 1000);
	},
	
	/**
	 * Opens "Import activities" dialog where an user can choose activities from an existing Learning Design. 
	 */
	importPartLearningDesign : function(){
		layout.ldStoreDialog.data('prepareForOpen')(LABELS.IMPORT_PART_DIALOG_TITLE, null,
				'#ldStoreDialogImportPartButton, #ldStoreDialogCancelButton, #ldStoreDialogImportPartFrame', false);
		layout.ldStoreDialog.modal('show');
	},
	
	/**
	 * Opens "Open sequence" dialog where an user can choose a Learning Design to load.
	 */
	openLearningDesign : function(){
		if (layout.modified && !confirm(LABELS.CLEAR_CANVAS_CONFIRM)) {
			return;
		}
		layout.ldStoreDialog.data('prepareForOpen')(LABELS.OPEN_DIALOG_TITLE, null,
				'#ldStoreDialogOpenButton, #ldStoreDialogCancelButton', false);
		layout.ldStoreDialog.modal('show');
	},

	
	openPreview : function(){
		if (layout.modified) {
			// disabling the button does not do the trick, so we have to check it here
			return;
		}
		
		var previewButton = $('#previewButton').button('loading');
		
		// initialize, create and enter the preview lesson
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring/initializeLesson.do',
			data : {
				'learningDesignID' : layout.ld.learningDesignID,
				'copyType' : 3,
				'lessonName' : LABELS.PREVIEW_LESSON_DEFAULT_TITLE
			},
			cache : false,
			dataType : 'text',
			success : function(lessonID) {
				if (!lessonID) {
					layout.infoDialog.data('show')(LABELS.PREVIEW_ERROR);
					previewButton.button('reset');
					return;
				}
				
				$.ajax({
					url : LAMS_URL + 'monitoring/monitoring/startPreviewLesson.do',
					data : {
						'lessonID' : lessonID
					},
					cache : false,
					dataType : 'text',
					success : function() {
						// open preview pop up window
						var left = ((screen.width / 2) - (1280 / 2)),
							// open the window a bit higher than center
							top = ((screen.height / 2) - (720 / 2)) / 2;
						window.open(LAMS_URL + 'home/learner.do?mode=preview&lessonID='+lessonID,'Preview',
									'width=1280,height=720,resizable,scrollbars=yes,status=yes,top=' + top + ',left=' + left);
						previewButton.button('reset');
					}
				});

			}
		});
	},
	
	openWeights : function(){
		layout.weightsDialog.data('prepareForOpen')();
		layout.weightsDialog.modal('show');
	},
	
	
	/**
	 * Make a copy of an existing activity.
	 */
	pasteActivity : function(){
		$('#pasteButton').blur();
		
		var activity = layout.copiedActivity;
		if (!activity) {
			return;
		}
		// check if the activity was not removed
		if (layout.activities.indexOf(activity) < 0){
			layout.copiedActivity = null;
			return;
		}
		// only tool activities can be copied (todo?)
		if (!(activity instanceof ActivityDefs.ToolActivity)) {
			layout.infoDialog.data('show')(LABELS.PASTE_ERROR);
			return;
		}
		
		// find an unqiue title for the new activity
		var copyCount = 1, 
			title = LABELS.ACTIVITY_COPY_TITLE_PREFIX.replace('[0]', '') + ' ' + activity.title;
		while (true) {
			var sameTitleFound = false;
			$.each(layout.activities, function(){
				if (title == this.title) {
					sameTitleFound = true;
					return false;
				}
			});
			
			if (sameTitleFound) {
				copyCount++;
				title = LABELS.ACTIVITY_COPY_TITLE_PREFIX.replace('[0]', '(' + copyCount + ') ') + ' ' + activity.title;
			} else {
				break;
			}
		};
		
		var toolContentID = null;
		// tool content ID can be null if the activity had the default content, i.e. was not edited yet
		if (activity.toolContentID) {
			$.ajax({
				cache : false,
				async : false,
				url : LAMS_URL + "authoring/copyToolContent.do",
				data : {
					'toolContentID' : activity.toolContentID
				},
				dataType : 'text',
				success : function(response) {
					toolContentID = response;
				}
			});
		}
		
		// draw the new activity next to the existing one
		var x = activity.items.shape.getBBox().x + 10,
			y = activity.items.shape.getBBox().y + 10,
			newActivity = new ActivityDefs.ToolActivity(null, null, toolContentID, activity.toolID, activity.learningLibraryID,
													   null, x, y, title);
		layout.activities.push(newActivity);
		
		if (activity.grouping) {
			// add grouping effect if the existing activity had one
			newActivity.grouping = activity.grouping;
			newActivity.draw();
		}
		
		GeneralLib.setModified(true);
	},
	
	
	/**
	 * Opens "Save sequence" dialog where an user can choose where to save the Learning Design.
	 */
	saveLearningDesign : function(showDialog){
		var saveButton = $('#saveButton').blur();
		saveButton.button('loading');
		
		if (!showDialog && layout.ld.learningDesignID) {
			if (!layout.ld.canModify || (!canSetReadOnly && layout.ld.readOnly)) {
				alert(LABELS.READONLY_FORBIDDEN_ERROR);
			} else {
				GeneralLib.saveLearningDesign(layout.ld.folderID, layout.ld.learningDesignID, layout.ld.title, layout.ld.readOnly);
			}
			saveButton.button('reset');
			return;
		}
		
		layout.ldStoreDialog.data('prepareForOpen')(LABELS.SAVE_DIALOG_TITLE, layout.ld.title,
				'#ldStoreDialogSaveButton, #ldStoreDialogCancelButton, #ldStoreDialogNameContainer', true);
		layout.ldStoreDialog.on('shown.bs.modal', function(){
			$('#ldStoreDialogNameContainer input', layout.ldStoreDialog).focus();
		});
		saveButton.button('reset');
		layout.ldStoreDialog.modal('show');
	},
	
	
	/**
	 * Expands/collapses description field.
	 */
	toggleDescriptionDiv: function() {
		$('#ldDescriptionDetails').slideToggle(function(){
			$('#ldDescriptionHideTip').text($(this).is(':visible') ? '▲' : '▼');
			$('.templateContainer').height($('#ldDescriptionDiv').height() + $('#canvas').height() - 10);
		});
	},
	
	
	/**
	 * Opens a pop up for template window that generates a learning design
	 */
	useTemplateToCreateLearningDesign : function(){
		if (layout.modified && !confirm(LABELS.CLEAR_CANVAS_CONFIRM)) {
			return;
		}

		var dialog = showDialog("ldTemplate", {
				'height' : Math.max(300, $(window).height() - 30),
				'width' :  Math.max(380, Math.min(1024, $(window).width() - 60)),
				'draggable' : true,
				'resizable' : true,
				'title' : LABELS.TEMPLATES,
				'beforeClose' : function(event){
					// ask the user if he really wants to exit before saving his work
					var iframe = $('iframe', this);
					if (iframe[0].contentWindow.doCancel) {
						iframe[0].contentWindow.doCancel();
						return false;
					}
				},
				'close' : function(){
					// stop checking in LD was 
					clearInterval(loadCheckInterval);
					$(this).remove();
				},
				'open' : function() {
					var dialog = $(this);
					// load contents after opening the dialog
					$('iframe', dialog).attr('id','templateModal').attr('src', LAMS_URL + '/authoring/template/list.jsp').on('load', function(){
						// override the close function so it works with the dialog, not window
						this.contentWindow.closeWindow = function(){
							// detach the 'beforeClose' handler above, attach the standard one and close the dialog
							dialog.off('hide.bs.modal').on('hide.bs.modal', function(){
								$('iframe', this).attr('src', null);
							}).modal('hide');
						}
					});
				},
			}, true),
			currentLearningDesignID = null,
			regEx = /learningDesignID=(\d+)/g,
			// since window.onload does not really work after submitting a form inside the window,
			// this trick checks periodically for changes
			loadCheckInterval = setInterval(function(){
				var body = $('body', $('iframe', dialog).contents()).html(),
					match = regEx.exec(body);
				// check if ID was found and it's not the same as previously set
				if (match && match[1] != currentLearningDesignID) {
					currentLearningDesignID = match[1];
					// load the imported LD
					GeneralLib.openLearningDesign(currentLearningDesignID);
					
					// generate images of the imported LD
					GeneralLib.saveLearningDesignImage();
				}
			}, 1000);
	},
};
