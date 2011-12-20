CKEDITOR.plugins.add('WikiLink',
       {
          requires : ['iframedialog'],
		  lang : [ 'en' ],
          init : function(editor) {
             var pluginPath = CKEDITOR.plugins.getPath('WikiLink');
             
             editor.ui.addButton(
                'WikiLink',
                {
                   label   : "WikiLink",
                   command : 'WikiLink',
                   icon    : pluginPath + 'wikilink.gif',
                   title   : editor.lang.wikilink.WikiLinkToolTip
                }
             );
             
             editor.addCommand(
            	'WikiLink', 
            	{
            	   exec : function(editor){
            	   			editor.openDialog('WikiLink');
             			  }
            	}
             );
             
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