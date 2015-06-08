var dialogTemplate = $('<div class="dialogContainer" />').append('<iframe />');

/**
 * Checks if the dialog is already opened.
 * If not, creates a new dialog with the given ID and init parameters.
 */
function showDialog(id, initParams, extraButtons) {
	var dialog = $('#' + id);
	if (dialog.length > 0) {
		// is it open already?
		dialog.dialog('moveToTop');
		return;
	}
	
	// create a new dialog by cloning a template
	dialog = dialogTemplate.clone();
	dialog.attr('id', id);
	
	// use initParams to overwrite default behaviour of the newly created dialog
	dialog.dialog($.extend({
		'autoOpen' : true,
		'modal' : false,
		'draggable' : true,
		'resizable' : false,
		'hide' : 'fold',
		'beforeClose' : function(){
			$('iframe', this).attr('src', null);
		},
		'close' : function() {
			// completely delete the dialog
			$(this).remove();
		}
	}, initParams));
	
	if (extraButtons) {
		dialog.dialogExtend({
	        "closable" : true,
	        "maximizable" : true,
	        "minimizable" : true,
	        "collapsable" : false,
	        "dblclick" : "collapse",
	        "minimizeLocation" : "right",
	        "icons" : {
	          "close" : "ui-icon-close",
	          "maximize" : "ui-icon-arrow-4-diag",
	          "minimize" : "ui-icon-minus",
	          "collapse" : "ui-icon-triangle-1-s",
	          "restore" : "ui-icon-newwin"
	        }
	      });
	}
	
	return dialog;
}


/**
 * Focuses on the dialog. Called from within the contained iframe.
 */
function moveDialogToTop(id) {
	$('#' + id).dialog('moveToTop');
}