/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

/**
 * @fileOverview The Image plugin.
 */

( function() {

	CKEDITOR.plugins.add( 'bootpanel', {
		requires: 'dialog',
		lang: 'en', 
		hidpi: true, 
		init: function( editor ) {
			var PLUGIN_NAME = 'bootpanel';

			// Register the dialog
			CKEDITOR.dialog.add( PLUGIN_NAME, this.path + 'dialogs/element.js' );

			// Register the command
			editor.addCommand( PLUGIN_NAME, new CKEDITOR.dialogCommand(PLUGIN_NAME) );
			
			editor.on( 'doubleclick', function( evt ) {
				var element = evt.data.element;
				
				//check if it has parent with class .lams-bootpanel
				if ( element && editor.elementPath( element ).contains( function( element ) {        
				    	if ( element.is( 'div' ) && element.hasClass( 'lams-bootpanel' )) {
				    		return true;
				    	}
					} )) {
					evt.data.dialog = 'bootpanel';
				}
			} );
			

		}
	} );

} )();
