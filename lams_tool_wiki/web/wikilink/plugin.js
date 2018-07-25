CKEDITOR.plugins.add('wikilink',
       {
          requires : ['iframedialog'],
		  lang : [ 'en' ],
          init : function(editor) {
             var pluginPath = CKEDITOR.plugins.getPath('wikilink');
             
             editor.ui.addButton(
                'wikilink',
                {
                   label   : "wikilink",
                   command : 'wikilink',
                   icon    : pluginPath + 'wikilink.gif',
                   title   : editor.lang.wikilink.WikiLinkToolTip
                }
             );
             
             editor.addCommand(
            	'wikilink', 
            	{
            	   exec : function(editor){
            	   			editor.openDialog('wikilink');
             			  }
            	}
             );
             
             CKEDITOR.dialog.addIframe(
                'wikilink',
                'wikilink',
                pluginPath + 'wikilink.jsp',
                280,
                230,
                function(){
                }
             );
          }
       }
);