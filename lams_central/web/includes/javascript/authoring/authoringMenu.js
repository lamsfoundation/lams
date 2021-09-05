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
	exportSVG : function(){
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
		$(svg).addClass('svg-learning-design');
		
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
	
	openQuestionBank : function(){
		window.open(LAMS_URL + 'qb/collection/show.do', '_blank');
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
		var x = activity.items.shape.getBBox().x + layout.snapToGrid.step,
			y = activity.items.shape.getBBox().y + layout.snapToGrid.step,
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
		$('#ldDescriptionHideTip').toggleClass('fa-chevron-circle-down fa-chevron-circle-up')
		$('#ldDescriptionDetails').slideToggle(function(){
			$('.templateContainer').height($('#ldDescriptionDiv').height() + $('#canvas').height() - 10);
		});
	},
	
	/**
	 * Hide / show contents of all ativity categories in the toolbar on the left
	 */
	toggleExpandTemplateCategories : function(){
		var collapseCategoriesButton = $('#template-container-collapse #template-categories-collapse-button'),
			isExpanded = collapseCategoriesButton.hasClass('fa-chevron-circle-up');
		$('#template-container-panel-group .collapse').collapse(isExpanded ? 'hide' : 'show');
		collapseCategoriesButton.toggleClass('fa-chevron-circle-down fa-chevron-circle-up');
	},
	
	/**
	 * Hide / show activity toolbar on the left
	 */
	toggleTemplateContainer : function(){
		var templateContainerCell = $('#templateContainerCell'),
			isExpanded = templateContainerCell.width(),
			rubbishBin = $('#rubbishBin', canvas).hide();

		if (isExpanded) {
			$('#template-container-collapse #template-categories-collapse-button', templateContainerCell).css('visibility', 'hidden');
		} else {
			templateContainerCell.show();
		}

		templateContainerCell.animate({
			'width': isExpanded > 0 ? 0 : 200
		}, function(){
			$('#template-container-collapse #template-container-collapse-button', templateContainerCell).toggleClass('fa-chevron-circle-left fa-chevron-circle-right');
			if (isExpanded) {
				templateContainerCell.hide();
			} else {
				$('#template-container-collapse #template-categories-collapse-button', templateContainerCell).css('visibility', '');
				
			}
			
			GeneralLib.resizePaper();
			rubbishBin.show();
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
					
					// use standard layout as if the user clicked "Arrange" button
					GeneralLib.arrangeActivities();
					
					// save learning design after rearrange
					MenuLib.saveLearningDesign();
				}
			}, 1000);
	},
};
