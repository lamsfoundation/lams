/* 
 *   CKEditior plugin.js for Movie Player
 *  ------------
 */CKEDITOR.plugins.add('movieplayer', {	requires : ['iframedialog'],	lang : [ 'en' ],	init : function(editor) {	             var pluginPath = CKEDITOR.plugins.getPath('movieplayer');	             	             editor.ui.addButton(	                'MoviePlayer',	                {
	                	label   : editor.lang.movieplayer.MoviePlayerBtn,		                command : 'MoviePlayer',		                icon    : pluginPath + 'filmreel.gif',		                title   : editor.lang.movieplayer.MoviePlayerTooltip	                }	             );	             editor.addCommand(
	            	'MoviePlayer', 
	                 {
	                 	exec : function(editor){
	                 		editor.openDialog('MoviePlayer');
	                  	}
	                 }
	             );
	             	             CKEDITOR.dialog.addIframe(	            	'MoviePlayer',	            	editor.lang.movieplayer.DlgMoviePlayerTitle,	                pluginPath + 'movieplayer.html',	                450,	                260	             );	}});