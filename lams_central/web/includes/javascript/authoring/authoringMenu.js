/**
 * This file contains methods for menu in Authoring.
 */

var MenuLib = {
		
	init : function(){
		// add jQuery UI button functionality
		$('.ui-button').button();
		$(".split-ui-button").each(function(){
			// drop down buttons
			var buttonContainer = $(this);
			var buttons = buttonContainer.children();
			
			buttons.first().button()
				   .next().button({
				text : false,
				icons : {
					primary : "ui-icon-triangle-1-s"
				}
			});
			
			buttons.each(function(){
				var button = $(this);
				if (!button.attr('onclick')) {
					button.click(function() {
						var menu = $(this).parent().next().show().position({
							my : "left top+2px",
							at : "left bottom",
							of : $(this).parent()
						});
						$(document).one("click", function() {
							menu.hide();
						});
						return false;
					});
				}
			});
			
			buttonContainer.buttonset().next().hide().menu().children().each(function(){
				var menuItem = $(this),
					subMenu = menuItem.children('ul');
				if (subMenu.length > 0){
					menuItem.click(function(){
						// do not show the submenu when the button is disabled
						if ($(this).attr('disabled') == 'disabled') {
							return;
						}
						var menu = $(this).children('ul').show().position({
							my : "left+2px top",
							at : "right top",
							of : this
						});
						$(document).one("click", function() {
							menu.hide();
						});
						return false;
					});
				}
			});
		});
		
		
		// dialog allowing to save canvas as SVG or PNG image
		layout.exportImageDialog = $('#exportImageDialog').dialog({
			'autoOpen' : false,
			'width'	   : 350,
			'height'   : 75,
			'show'     : 'fold',
			'hide'     : 'fold',
			'draggable': false,
			'resizable': false,
			'modal'	   : true,
			'title'	   : LABELS.EXPORT_IMAGE_DIALOG_TITLE
		}).click(function(){
			layout.exportImageDialog.dialog('close');
		});
		
		layout.dialogs.push(layout.exportImageDialog);
		
		
		// dialog for downloading the sequence as ZIP
		layout.exportLDDialog = $('#exportLDDialog').dialog({
			'autoOpen' : false,
			'width'	   : 320,
			'height'   : 120,
			'hide'     : 'fold',
			'draggable': false,
			'resizable': false,
			'modal'	   : true,
			'title'	   : LABELS.EXPORT_SEQUENCE_DIALOG_TITLE,
			'beforeClose' : function(){
				$('iframe', layout.exportLDDialog).attr('src', null);
			}
		}).click(function(){
			layout.exportLDDialog.dialog('close');
		});
		layout.dialogs.push(layout.exportLDDialog);
	},
	
	
	/**
	 * Creates a new annotation label.
	 */
	addAnnotationLabel : function() {
		HandlerLib.resetCanvasMode();
		
		var dialog = layout.infoDialog.text(LABELS.ANNOTATION_LABEL_PLACE_PROMPT);
		dialog.dialog('open');
	
		canvas.css('cursor', 'pointer').click(function(event){
			dialog.text('');
			dialog.dialog('close');


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
		
		var dialog = layout.infoDialog.text(LABELS.ANNOTATION_REGION_PLACE_PROMPT);
		dialog.dialog('open');
	
		canvas.css('cursor', 'crosshair').mousedown(function(event){
			dialog.text('');
			dialog.dialog('close');
			
			var	targetElement = paper.getElementByPoint(event.pageX, event.pageY);
			
			if (targetElement) {
				// cancel
				HandlerLib.resetCanvasMode(true);
			} else {
				HandlerDecorationLib.drawRegionStartHandler(event);
			}
		});
	},
	
	
	/**
	 * Run when branching is selected from menu. Allows placing branching and converge points on canvas.
	 */
	addBranching : function(){
		HandlerLib.resetCanvasMode();
		var dialog = layout.infoDialog.text(LABELS.BRANCHING_START_PLACE_PROMPT);
		dialog.dialog('open');
		
		var branchingActivity = null;
		canvas.css('cursor', 'pointer').click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var translatedEvent = GeneralLib.translateEventOnCanvas(event),
				x = translatedEvent[0] - 6,
				y = translatedEvent[1] - 8;
			
			// if it is start point, branchingActivity is null and constructor acts accordingly
			var branchingEdge = new ActivityDefs.BranchingEdgeActivity(null, null, x, y, null, null, branchingActivity);
			layout.activities.push(branchingEdge);
			
			if (branchingActivity) {
				// converge point was just place, end of function
				HandlerLib.resetCanvasMode(true);
				
				dialog.text('').dialog('close');
				
				GeneralLib.setModified(true);
			} else {
				// extract main branchingActivity structure from created start point
				branchingActivity = branchingEdge.branchingActivity;
				dialog.text(LABELS.BRANCHING_END_PLACE_PROMPT);
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
		
		var dialog = layout.infoDialog.text(LABELS.SUPPORT_ACTIVITY_PLACE_PROMPT);
		dialog.dialog('open');
	
		canvas.css('cursor', 'pointer').click(function(event){
			dialog.text('');
			dialog.dialog('close');


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
		HandlerLib.resetCanvasMode();
		
		canvas.css('cursor', 'url("' + layout.toolMetadata.grouping.iconPath + '"), move')
			  .click(function(event){
			// pageX and pageY tell event coordinates relative to the whole page
			// we need relative to canvas
			var translatedEvent = GeneralLib.translateEventOnCanvas(event),
				x = translatedEvent[0] - 47,
				y = translatedEvent[1] -  2;

			layout.activities.push(new ActivityDefs.GroupingActivity(null, null, x, y));
			
			GeneralLib.setModified(true);
			HandlerLib.resetCanvasMode(true);
		});
	},
	

	/**
	 * Creates a new optional activity.
	 */
	addOptionalActivity : function() {
		HandlerLib.resetCanvasMode();
		
		var dialog = layout.infoDialog.text(LABELS.OPTIONAL_ACTIVITY_PLACE_PROMPT);
		dialog.dialog('open');
	
		canvas.css('cursor', 'pointer').click(function(event){
			dialog.text('');
			dialog.dialog('close');


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
		HandlerLib.resetCanvasMode();
		
		var dialog = layout.infoDialog.text(LABELS.TRANSITION_PLACE_PROMPT);
		dialog.dialog('open');
		
		canvas.css('cursor', 'pointer').click(function(event){
			dialog.text('');
			dialog.dialog('close');
			
			var startActivity = null,
				targetElement = paper.getElementByPoint(event.pageX, event.pageY);
			if (targetElement) {
				startActivity = targetElement.data('parentObject');
				if (startActivity) {
					HandlerTransitionLib.drawTransitionStartHandler(startActivity, null, event.pageX, event.pageY);
				}
			}
		});
	},
	
	
	/**
	 * Mark an activity as ready for pasting.
	 */
	copyActivity : function(){
		layout.copiedActivity = layout.selectedObject;
	},
	

	/**
	 * Opens a pop up for exporting LD.
	 */
	exportLearningDesign : function(format){
		if (layout.modified || layout.activities.length == 0) {
			return;
		}
		
		$('iframe', layout.exportLDDialog)
			.attr('src', LAMS_URL + 'authoring/exportToolContent.do?method=export&format=' + format + '&learningDesignID='
								  + layout.ld.learningDesignID);
		layout.exportLDDialog.dialog('open');
	},

	
	/**
	 * Creates a PNG image out of current SVG contents.
	 */
	exportPNG : function(download){
		ActivityLib.removeSelectEffect();
		
		var imageCode = null;
		if (!download || layout.conf.supportsDownloadAttribute) {
			var crop = MenuLib.getCanvasCrop();
			if (crop.x >= crop.x2) {
				return;
			}
			
			var ctx = crop.workspace.getContext('2d'),
			w = crop.x2 - crop.x,
			h = crop.y2 - crop.y,
			cut = ctx.getImageData(crop.x, crop.y, w, h);
	
			crop.workspace.width = w;
			crop.workspace.height = h;
			ctx.putImageData(cut, 0, 0);
			
			imageCode = crop.workspace.toDataURL("image/png");
		}
		if (download) {
			var anchor = $('a', layout.exportImageDialog);
			if (layout.conf.supportsDownloadAttribute) {
				anchor.attr({
					'href'	   : imageCode,
					'download' : (layout.ld.title ? layout.ld.title : 'Untitled') + '.png'
				});
			} else {
				anchor.attr({
					'href'	   : LD_THUMBNAIL_URL_BASE + layout.ld.learningDesignID 
								 + '&svgFormat=2&download=true&_=' + new Date().getTime()
				});
			}
			layout.exportImageDialog.dialog('open');
		} else {
			return imageCode;
		}
	},
	
	
	/**
	 * Creates a SVG image out of current SVG contents.
	 */
	exportSVG : function(download){
		ActivityLib.removeSelectEffect();
		
		var imageCode = null;
		if (!download || layout.conf.supportsDownloadAttribute) {
			var crop = MenuLib.getCanvasCrop();
			if (crop.x >= crop.x2) {
				return;
			}
			
			// replace image links with PNG code
			crop.canvasClone.find('image').each(function(){
				var image = $(this),
					attributeName = 'xlink:href',
					iconPath = image.attr(attributeName);
				if (!iconPath) {
					attributeName = 'href',
					iconPath = image.attr(attributeName);
				}
				
				var iconCode = null, 
					extensionIndex = iconPath.indexOf('.svg');
			
				if (extensionIndex > -1) {
					var pngPath = iconPath.substring(0, extensionIndex) + '.png';
					// PNG images got precached when dropping tools on canvas
					iconCode = layout.iconLib[pngPath];
					
					if (!iconCode) {
						// no precached PNG image, generate one from SVG
						// the PNG image can not be fetched using Ajax(!):
						// it can be done only asynchronously or the binary stream has to be converted to text 
						$.ajax({
							url : iconPath,
							async: false,
							dataType : 'text',
							success : function(response) {
								var workspace = $('<canvas />')[0];
								workspace.width = image.width();
								workspace.height = image.height();
								canvg(workspace, response);
								iconCode = layout.iconLib[pngPath] = workspace.toDataURL('image/png');
							}
						});
					}
				} else {
					iconCode = layout.iconLib[iconPath];
				}
				 
				if (iconCode) {
					image.attr(attributeName, iconCode);
				}
			});
			
			
			// set viewBox so content is nicely aligned
			var width = crop.x2 - crop.x + 2,
				height = crop.y2 - crop.y + 2,
				svg = $('svg', crop.canvasClone).attr({
					'width'               : width,
					'height'              : height
				})[0];
			
			// need to set attributes using pure JS as jQuery sets them with lower case, which is unacceptable in SVG
			svg.setAttribute('viewBox', crop.x + ' ' + crop.y + ' ' + width + ' ' + height);
			svg.setAttribute('preserveAspectRatio', 'xMinYMin slice');
			
			// reset any cursor=pointer styles
			$('*[style*="cursor"]', svg).css('cursor', 'default');
			
			
			imageCode = crop.canvasClone.html();
			// otherwise there will be syntax erros in IE11
			imageCode = imageCode.replace(/xmlns=\"http:\/\/www\.w3\.org\/2000\/svg\"/, '');
			imageCode = imageCode.replace(/xmlns:NS1=\"\"/, '');
			imageCode = imageCode.replace(/NS1:xmlns:xlink=\"http:\/\/www\.w3\.org\/1999\/xlink\"/, 'xmlns:xlink=\"http:\/\/www\.w3\.org\/1999\/xlink\"');
			imageCode = imageCode.replace(/xmlns:xlink=\"http:\/\/www\.w3\.org\/1999\/xlink\" xlink:href/g, 'xlink:href');
		}
		
		if (download) {
			var anchor = $('a', layout.exportImageDialog);
			if (layout.conf.supportsDownloadAttribute) {
				anchor.attr({
					'href'	   : 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(imageCode),
					'download' : (layout.ld.title ? layout.ld.title : 'Untitled') + '.svg'
				});
			} else {
				anchor.attr({
					'href'	   : LD_THUMBNAIL_URL_BASE + layout.ld.learningDesignID
								 + '&svgFormat=1&download=true&_=' + new Date().getTime()
				});
			}
			layout.exportImageDialog.dialog('open');
		} else {
			return imageCode;
		}
	},
	
	
	/**
	 * Finds coordinates of canvas content, minus surrounding whitespace.
	 */
	getCanvasCrop : function(){
		var canvasClone = canvas.clone();
		// Raphael does not add this and it's needed by Firefox
		$('svg', canvasClone).attr('xmlns:xlink', 'http://www.w3.org/1999/xlink');
		// remove the rubbish bin icon
		canvasClone.find('#rubbishBin').remove();
		// create HTML5 canvas element and fill it with SVG code using canvg library
		var workspace = $('<canvas />')[0];
		canvg(workspace, canvasClone.html());

		// trim the image from white space
		var ctx = workspace.getContext('2d'),
		w = workspace.width,
		h = workspace.height,
		imageData = ctx.getImageData(0, 0, w, h),
		result = {
			x : w,
			y : h,
			x2 : 0,
			y2 : 0,
			workspace : workspace,
			canvasClone : canvasClone
		};

		for (y = 0; y < h; y++) {
		    for (x = 0; x < w; x++) {
		        var index = (y * w + x) * 4,
		        	a = imageData.data[index + 3];

		        if (a > 0) {
		            if (x < result.x) {
		            	result.x = x;
		            }
		            if (y < result.y) {
		            	result.y = y;
		            }
		            if (x > result.x2) {
		            	result.x2 = x;
		            }
		            if (y > result.y2) {
		            	result.y2 = y;
		            }
		        }
		    }
		}
		
		return result;
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
					$.each(response.folders, function(){
						result.push({'type'            : 'text',
								  	 'label'           : this.isRunSequencesFolder ?
								  			 				LABELS.RUN_SEQUENCES_FOLDER : this.name,
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
	 * Opens a pop up for importing LD. Loads the imported LD to canvas.
	 */
	importLearningDesign : function(){
		var importWindow = window.open(LAMS_URL + 'authoring/importToolContent.do?method=import','Import',
					'width=800,height=298,resize=yes,status=yes,scrollbar=no,menubar=no,toolbar=no'),
			currentLearningDesignID = null,
			regEx = /learningDesignID=(\d+)/g,
			// since window.onload does not really work after submitting a form inside the window,
			// this trick checks periodically for changes
			loadCheckInterval = setInterval(function(){
				if (!importWindow){
					// window was closed
					clearInterval(loadCheckInterval);
					return;
				}
				var body = $('body', importWindow.document).html(),
					match = regEx.exec(body);
				// check if ID was found and it's not the same as previously set
				if (match && match[1] != currentLearningDesignID) {
					currentLearningDesignID = match[1];
					// load the imported LD
					GeneralLib.openLearningDesign(currentLearningDesignID);
					
					// generate images of the imported LD
					GeneralLib.saveLearningDesignImages();
				}
			}, 1000);
	},
	
	
	/**
	 * Opens "Import activities" dialog where an user can choose activities from an existing Learning Design. 
	 */
	importPartLearningDesign : function(){
		// remove the directory tree, if it remained for last dialog opening
		layout.ldStoreDialog.dialog('option', {
			'title'  			  : LABELS.IMPORT_PART_DIALOG_TITLE,
			'learningDesignTitle' : null,
			'buttons' 			  : layout.ldStoreDialog.dialog('option', 'buttonsImportPart'),
			// it informs widgets that it is the import part dialog
			'dialogClass'		  : 'ldStoreDialogImportPart'
		})			   
		.dialog('open');
		
		MenuLib.loadLearningDesignTree();
	},
	
	
	/**
	 * Loads Learning Design Tree from DB
	 */
	loadLearningDesignTree : function(){
		var tree = layout.ldStoreDialog.dialog('option', 'ldTree'),
			rootNode = tree.getRoot();
		// remove existing folders
		$.each(rootNode.children, function(){
			tree.removeNode(this);
		});
		// (re)load user's folders and LDs
		tree.buildTreeFromObject(MenuLib.getFolderContents());
		tree.render();
		
		// expand the first (user) folder
		tree.getRoot().children[0].expand();
		
		return tree;
	},
	
	
	/**
	 * Opens "Open sequence" dialog where an user can choose a Learning Design to load.
	 */
	openLearningDesign : function(){
		// remove the directory tree, if it remained for last dialog opening
		layout.ldStoreDialog.dialog('option', {
			'title'  			  : LABELS.OPEN_DIALOG_TITLE,
			'learningDesignTitle' : null,
			'buttons' 			  : layout.ldStoreDialog.dialog('option', 'buttonsLoad'),
			// it informs widgets that it is load dialog
			'dialogClass'		  : 'ldStoreDialogLoad'
		})	   
		.dialog('open');
		
		MenuLib.loadLearningDesignTree();
	},

	
	openPreview : function(){
		if (layout.modified) {
			// disabling the button does not do the trick, so we have to check it here
			return;
		}
		
		// initialize, create and enter the preview lesson
		$.ajax({
			url : LAMS_URL + 'monitoring/monitoring.do',
			data : {
				'method' : 'initializeLesson',
				'learningDesignID' : layout.ld.learningDesignID,
				'copyType' : 3,
				'lessonName' : LABELS.PREVIEW_LESSON_DEFAULT_TITLE
			},
			cache : false,
			dataType : 'text',
			success : function(lessonID) {
				if (!lessonID) {
					alert(LABELS.PREVIEW_ERROR);
					return;
				}
				
				$.ajax({
					url : LAMS_URL + 'monitoring/monitoring.do',
					data : {
						'method' : 'startPreviewLesson',
						'lessonID' : lessonID
					},
					cache : false,
					dataType : 'text',
					success : function() {
						// open preview pop up window
						window.open(LAMS_URL + 'home.do?method=learner&mode=preview&lessonID='+lessonID,'Preview',
									'width=920,height=700,resizable,status=yes');
					}
				});

			}
		});
	},
	
	
	/**
	 * Make a copy of an existing activity.
	 */
	pasteActivity : function(){
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
			alert(LABELS.PASTE_ERROR);
			return;
		}
		
		// find an unqiue title for the new activity
		var copyCount = 1, 
			title = LABELS.ACTIVITY_COPY_TITLE_PREFIX.replace('[0]', '') + activity.title;
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
				title = LABELS.ACTIVITY_COPY_TITLE_PREFIX.replace('[0]', '(' + copyCount + ')') + activity.title;
			} else {
				break;
			}
		};
		
		// draw the new activity next to the existing one
		var x = activity.items.shape.getBBox().x + 10,
			y = activity.items.shape.getBBox().y + 10,
			newActivity = new ActivityDefs.ToolActivity(null, null, null, activity.toolID, activity.learningLibraryID,
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
		if (!showDialog && layout.ld.learningDesignID) {
			GeneralLib.saveLearningDesign(layout.ld.folderID, layout.ld.learningDesignID, layout.ld.title);
			return;
		}
		
		// remove the directory tree, if it remained for last dialog opening
		layout.ldStoreDialog.dialog('option', {
			'title'				  : LABELS.SAVE_DIALOG_TITLE,
			'learningDesignTitle' : layout.ld.title,
			'buttons'			  : layout.ldStoreDialog.dialog('option', 'buttonsSave'),
			// it informs widgets that it is saved dialog
			'dialogClass'		  : 'ldStoreDialogSave'
		})			   
		.dialog('open');
		
		var tree = MenuLib.loadLearningDesignTree();
		tree.getRoot().children[0].highlight();
	},
	
	
	/**
	 * Expands/collapses description field.
	 */
	toggleDescriptionDiv: function() {
		$('#ldDescriptionDetails').slideToggle(function(){
			$('#ldDescriptionHideTip').text($(this).is(':visible') ? '▲' : '▼');
			$('#templateContainer').height($('#ldDescriptionDiv').height() + $('#canvas').height() - 10);
		});
	}
	
	
	/*
	,zoom : function(){
		var zoomButton = $('#zoomButton > span');
		if (layout.isZoomed) {
			paper.setViewBox(0, 0, paper.width, paper.height, true);
			layout.isZoomed = false;
			zoomButton.text('Zoom out');
		} else {
			paper.setViewBox(-paper.width / 2, -paper.height / 2, paper.width * 2, paper.height * 2, true);
			layout.isZoomed = true;
			zoomButton.text('Cancel zoom');
		}
	}
	*/
};