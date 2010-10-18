CKEDITOR.plugins.add('WikiLink',
       {
          requires : ['iframedialog'],
		  lang : [ 'en' ],
          init : function(editor) {
             var pluginPath = CKEDITOR.plugins.getPath('WikiLink');
             
             editor.ui.addButton(
                'WikiLink',
                {
                   label : "WikiLink",
                   command : 'WikiLink',
                   icon : pluginPath + 'wikilink.gif',
                   title : editor.lang.wikilink.WikiLinkToolTip
                }
             );
             var current = editor.name;
             var cmd = editor.addCommand('WikiLink', {exec:showDialogPlugin});
             CKEDITOR.dialog.addIframe(
                'WikiLink',
                'WikiLink',
                pluginPath + 'wikilink.jsp',
                280,
                230,
                function(){
                }
             );
          }
       }
    );

function showDialogPlugin(e){
	e.openDialog('WikiLink');
}
