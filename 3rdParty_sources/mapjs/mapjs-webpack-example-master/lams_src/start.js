/*global require, document, window, console */
const MAPJS = require('mindmup-mapjs'),
	jQuery = require('jquery'),
	themeProvider = require('./theme'),
	ThemeProcessor = require('mindmup-mapjs-layout').ThemeProcessor,
	// testMap = require('./example-map'),				// LAMS Modification
	content = require('mindmup-mapjs-model').content,
	init = function () {
		'use strict';
		// const container = jQuery('#container'),		// LAMS Modification
		//	idea = content(testMap),			// LAMS Modification
		var containerId = getMindmapContainerId();		// LAMS Modification
		const container = jQuery('#'+containerId),		// LAMS Modification
			imageInsertController = new MAPJS.ImageInsertController('http://localhost:4999?u='),
			mapModel = new MAPJS.MapModel(MAPJS.DOMRender.layoutCalculator, []);

		jQuery.fn.attachmentEditorWidget = function (mapModel) {
			return this.each(function () {
				mapModel.addEventListener('attachmentOpened', function (nodeId, attachment) {
					mapModel.setAttachment(
							'attachmentEditorWidget',
							nodeId, {
								contentType: 'text/html',
								content: window.prompt('attachment', attachment && attachment.content)
							}
							);
				});
			});
		};
		window.onerror = window.alert;


		jQuery('#themecss').themeCssWidget(themeProvider, new ThemeProcessor(), mapModel);
		container.domMapWidget(console, mapModel, false, imageInsertController);
		jQuery('body').mapToolbarWidget(mapModel);
		jQuery('body').attachmentEditorWidget(mapModel);
		// mapModel.setIdea(idea);				// LAMS Modification


		jQuery('#linkEditWidget').linkEditWidget(mapModel);
		window.mapModel = mapModel;
		jQuery('.arrow').click(function () {
			jQuery(this).toggleClass('active');
		});
		imageInsertController.addEventListener('imageInsertError', function (reason) {
			console.log('image insert error', reason);
		});

		// LAMS Modification
		/*
		container.on('drop', function (e) {
			const dataTransfer = e.originalEvent.dataTransfer;
			e.stopPropagation();
			e.preventDefault();
			if (dataTransfer && dataTransfer.files && dataTransfer.files.length > 0) {
				const fileInfo = dataTransfer.files[0];
				if (/\.mup$/.test(fileInfo.name)) {
					const oFReader = new window.FileReader();
					oFReader.onload = function (oFREvent) {
						mapModel.setIdea(content(JSON.parse(oFREvent.target.result)));
					};
					oFReader.readAsText(fileInfo, 'UTF-8');
				}
			}
		});
		*/

		loadRootIdea(mapModel);					// LAMS Modification
	};
document.addEventListener('DOMContentLoaded', init);
