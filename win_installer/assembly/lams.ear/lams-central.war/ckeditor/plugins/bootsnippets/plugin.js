/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

( function() {
	
	//add all commands, dialogs and buttons specified in ckconfig_custom.js
	CKEDITOR.plugins.add( 'bootsnippets', {
		requires: 'dialog',
		lang: 'en,es,gr,ru', 
		hidpi: false, 
		init: function( editor ) {
			var snippetNames = (CKEDITOR.config.bootsnippets).split( ',' );
			var snippetIcons = (CKEDITOR.config.bootsnippets_icons).split( ',' );
			var snippetLabels = (CKEDITOR.config.bootsnippets_labels).split( ',' );

			if ( snippetNames.length ) {
				for ( var i = 0, snippetNamesCount = snippetNames.length; i < snippetNamesCount; i++ ) {
					var snippetName = snippetNames[i].trim();
					var snippetIcon = snippetIcons[i].trim();
					var snippetLabel = snippetLabels[i].trim();
					
					CKEDITOR.dialog.add( snippetName, bootsnippetDialogHandler );
					editor.addCommand( snippetName, new CKEDITOR.dialogCommand( snippetName ) );
					
					editor.ui.addButton && editor.ui.addButton( 
						snippetName,
						{
							label: snippetLabel,
							command: snippetName,
							icon : snippetIcon,
							toolbar: 'bootsnippets,'+i
						} 
					);
				}
			}
		}
	} );

	var bootsnippets = {},
		loadedSnippetsFiles = {};

	CKEDITOR.addSnippets = function( name, definition ) {
		bootsnippets[ name ] = definition;
	};

	CKEDITOR.getSnippets = function( name ) {
		return bootsnippets[ name ];
	};

	CKEDITOR.loadSnippets = function( snippetFiles, callback ) {
		// Holds the bootsnippets files to be loaded.
		var toLoad = [];

		// Look for pending snippet files to get loaded.
		for ( var i = 0, count = snippetFiles.length; i < count; i++ ) {
			if ( !loadedSnippetsFiles[ snippetFiles[ i ] ] ) {
				toLoad.push( snippetFiles[ i ] );
				loadedSnippetsFiles[ snippetFiles[ i ] ] = 1;
			}
		}

		if ( toLoad.length )
			CKEDITOR.scriptLoader.load( toLoad, callback );
		else
			setTimeout( callback, 0 );
	};
	
	//creates dialog for each snippet category
	function bootsnippetDialogHandler(editor) {
		
		// Constructs the HTML view of the specified snippets data.
		function renderSnippetsList( container, snippetsDefinitions ) {
			// clear loading wait text.
			container.setHtml( '' );

			for ( var i = 0, totalDefs = snippetsDefinitions.length; i < totalDefs; i++ ) {
				var definition = CKEDITOR.getSnippets( snippetsDefinitions[ i ] ),
					imagesPath = definition.imagesPath,
					snippets = definition.snippets,
					count = snippets.length;

				for ( var j = 0; j < count; j++ ) {
					var snippet = snippets[ j ],
						item = createSnippetItem( snippet, editor, imagesPath );
					item.setAttribute( 'aria-posinset', j + 1 );
					item.setAttribute( 'aria-setsize', count );
					container.append( item );
				}
			}
		}

		function createSnippetItem( snippet, editor, imagesPath ) {
			var item = CKEDITOR.dom.element.createFromHtml( '<a href="javascript:void(0)" tabIndex="-1" role="option" >' +
				'<div class="cke_tpl_item"></div>' +
				'</a>' );
			
			var lang = editor.lang.bootsnippets,
				title = lang[snippet.title] ? lang[snippet.title] : snippet.title;

			// Build the inner HTML of our new item DIV.
			var html = '<table style="width:350px;" class="cke_tpl_preview" role="presentation">';
			html += '<tr><td style="white-space:normal;"><span class="cke_tpl_title">' + title + '</span><br/>';
			if ( snippet.description ) {
				html += '<span>' + snippet.description + '</span>';
			}
			html += '</td></tr>';

			if ( snippet.image && imagesPath ) {
				html += '<tr><td class="cke_tpl_preview_img"><img src="' +
					CKEDITOR.getUrl( imagesPath + snippet.image ) + '"' +
					( CKEDITOR.env.ie6Compat ? ' onload="this.width=this.width"' : '' ) + ' alt="" title=""></td></tr>';
			}

			html += '</table>';

			item.getFirst().setHtml( html );

			item.on( 'click', function() {
				var htmlToInsert = "";
				if (snippet.css) {
					htmlToInsert += '<link rel="stylesheet" href="/lams/www/public/ckeditor-templates/css/' + snippet.css + '">';
				}
				htmlToInsert += snippet.html;
				htmlToInsert += '<div>&nbsp;</div>';
				
				insertSnippet(htmlToInsert);
			} );

			return item;
		}

		// Insert the specified snippet content into editor.
		// @param {Number} index
		function insertSnippet( html ) {
			var dialog = CKEDITOR.dialog.getCurrent(),
				isReplace = dialog.getValueOf( 'selectTpl', 'chkInsertOpt' );

			if ( isReplace ) {
				editor.fire( 'saveSnapshot' );
				// Everything should happen after the document is loaded (#4073).
				editor.setData( html, function() {
					dialog.hide();

					// Place the cursor at the first editable place.
					var range = editor.createRange();
					range.moveToElementEditStart( editor.editable() );
					range.select();
					setTimeout( function() {
						editor.fire( 'saveSnapshot' );
					}, 0 );

				} );
			} else {
				editor.insertHtml( html );
				dialog.hide();
			}
		}

		// Load skin at first.
		var plugin = CKEDITOR.plugins.get( 'bootsnippets' );
		CKEDITOR.document.appendStyleSheet( CKEDITOR.getUrl( plugin.path + 'dialogs/snippets.css' ) );

		var snippetListLabelId = 'cke_tpl_list_label_' + CKEDITOR.tools.getNextNumber(),
			lang = editor.lang.bootsnippets,
			config = editor.config,
			listContainer;
		return {
			title: lang.title,

			minWidth: CKEDITOR.env.ie ? 440 : 420,
			minHeight: 600,

			contents: [ {
				id: 'selectTpl',
				style: 'margin-top: -15px;',
				//label: lang.title,
				elements: [ 
					{
					type: 'vbox',
					children: [
					{
						className : 'fake-object-to-focus',
						type: 'html',
						focus: true,
						html: '<div></div>'
					},
					{
						id: 'snippetsList',
						type: 'html',
						focus: false,
						html: '<div class="cke_tpl_list" tabIndex="-1" role="listbox" aria-labelledby="' + snippetListLabelId + '">' +
								'<div class="cke_tpl_loading"><span></span></div>' +
							'</div>' +
							'<span class="cke_voice_label" id="' + snippetListLabelId + '">' + lang.options + '</span>'
					},
					{
						id: 'chkInsertOpt',
						type: 'checkbox',
						label: lang.insertOption,
						'default': config.bootsnippets_replaceContent,
						focus: false
					} ]
				} ]
			} ],

			buttons: [ CKEDITOR.dialog.cancelButton ],

			onShow: function() {
				var snippetsListField = this.getContentElement( 'selectTpl', 'snippetsList' );
				listContainer = snippetsListField.getElement();

				CKEDITOR.loadSnippets( config.bootsnippets_files, function() {
					//snippetsCategory is the same as the dialog's name (e.g. "bootsnippets-image")
					var snippetsCategory = CKEDITOR.dialog.getCurrent().getName();
					var snippets = ( snippetsCategory || 'default' ).split( ',' );
					
					//change dialog's title
					var snippetNames = (CKEDITOR.config.bootsnippets).split( ',' );
					var snippetLabels = (CKEDITOR.config.bootsnippets_labels).split( ',' );
					var snippetLabel = "";
					if ( snippetNames.length ) {
						for ( var i = 0, snippetNamesCount = snippetNames.length; i < snippetNamesCount; i++ ) {
							var snippetName = snippetNames[i].trim();
							if (snippetName == snippets) {
								snippetLabel = snippetLabels[i].trim();
							}
						}
					}
					CKEDITOR.dialog.getCurrent().getElement().getFirst().find('.cke_dialog_title').getItem(0).setText(snippetLabel);

					if ( snippets.length ) {
						renderSnippetsList( listContainer, snippets );
					} else {
						listContainer.setHtml( '<div class="cke_tpl_empty">' +
							'<span>' + lang.emptyListMsg + '</span>' +
							'</div>' );
					}
				} );
			}
		};
	}
} )();



/**
 * The bootsnippets definition set to use. It accepts a list of names separated by
 * comma. It must match definitions loaded with the {@link #snippets_files} setting.
 *
 *		config.bootsnippets = 'my_snippets';
 *
 * @cfg {String} [bootsnippets='default']
 * @member CKEDITOR.config
 */

/**
 * The list of bootsnippets definition files to load.
 *
 *		config.snippets_files = [
 *			'/editor_snippets/site_default.js',
 *			'http://www.example.com/user_snippets.js
 *		];
 *
 * @cfg
 * @member CKEDITOR.config
 */
// *LAMS* commented out by LAMS as it was overwriting properties from the main config file 
//CKEDITOR.config.bootsnippets_files = [
//	CKEDITOR.getUrl( 'plugins/bootsnippets/bootsnippets/default.js' )
//];

/**
 * Whether the "Replace actual contents" checkbox is checked by default in the
 * bootsnippets dialog.
 *
 *		config.snippets_replaceContent = false;
 *
 * @cfg
 * @member CKEDITOR.config
 */
CKEDITOR.config.bootsnippets_replaceContent = false;
