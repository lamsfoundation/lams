/**
 * @fileOverview The Wave Header plugin.
 */

( function() {

	CKEDITOR.plugins.add( 'wavepanel', {
		requires: 'dialog',
		lang: 'en', 
		hidpi: true, 
		init: function( editor ) {
			var PLUGIN_NAME = 'wavepanel';

			// Register the dialog
			CKEDITOR.dialog.add( PLUGIN_NAME, this.path + 'dialogs/wave.js' );

			// Register the command
			editor.addCommand( PLUGIN_NAME, new CKEDITOR.dialogCommand(PLUGIN_NAME) );
			
			editor.on( 'doubleclick', function( evt ) {
				var element = evt.data.element;
				
				//check if it has parent with class .lams-wavepanel
				if ( element && editor.elementPath( element ).contains( function( element ) {        
				    	if ( element.is( 'div' ) && element.hasClass( 'lams-wavepanel' )) {
				    		return true;
				    	}
					} )) {
					evt.data.dialog = 'wavepanel';
				}
			} );
			

		}
	} );

} )();
