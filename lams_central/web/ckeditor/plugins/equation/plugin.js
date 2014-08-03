/* 
 Copyright CodeCogs 2006-2011
 Written by Will Bateman.
 
 Version 1: CK Editor Plugin that insert LaTeX into HTML
*/



/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

var currentEdit=null; 

(function()
{
	var popupEqnwin=null;
  var createEqnDefinition = 
	{
		preserveState:true,
		editorFocus:false,
		exec : function(editor, latex)
		{
			// LAMS fix; in original file was: currentEdit=CKEDITOR.currentInstance;
	  		currentEdit=editor;
			
			//open a popup window when the button is clicked
			if (popupEqnwin==null || popupEqnwin.closed || !popupEqnwin.location) 
			{
				// LDEV-3281: fixing protocol
				var protocol = "http:";
				if (window.location.protocol == "https:") {
					var protocol = "https:";
				}
				var url=protocol+'//latex.codecogs.com/editor_json3.php?type=url&editor=CKEditor';
		
				//if(language!='') url+='&lang='+language;
				if(latex!==undefined) 
				{	
					latex=unescape(latex);
					latex=latex.replace(/\+/g,'&plus;');
					url+='&latex='+escape(latex);
				}
				
				popupEqnwin=window.open('','LaTexEditor','width=700,height=450,status=1,scrollbars=yes,resizable=1');
				if (!popupEqnwin.opener) popupEqnwin.opener = self;
				popupEqnwin.document.open();
				popupEqnwin.document.write('<!DOCTYPE html><head><script src="'+url+'" type="text/javascript"></script></head><body></body></html>');
				popupEqnwin.document.close();
			}
			else if (window.focus) 
			{ 
				popupEqnwin.focus()
				if(latex!==undefined)
				{
					latex=unescape(latex);
					latex = latex.replace(/\\/g,'\\\\');
					latex = latex.replace(/\'/g,'\\\'');
					latex = latex.replace(/\"/g,'\\"');
					latex = latex.replace(/\0/g,'\\0');
					
					eval("var old = popupEqnwin.document.getElementById('JSONload')");
					if (old != null) {
						old.parentNode.removeChild(old);
						delete old;
					}
					
					var head = popupEqnwin.document.getElementsByTagName("head")[0];
					var script = document.createElement("script"); 
          script.type = "text/javascript";  
					script.id = 'JSONload';
			    script.innerHTML = 'EqEditor.load(\''+(latex)+'\');';
					head.appendChild(script);
				}
			}
		}
	};

	CKEDITOR.plugins.add( 'equation',
	{
	  lang : ['en'],
		
		init : function( editor )
		{
			var com="equation";
			
			// Add the link and unlink buttons.
			editor.addCommand( com, createEqnDefinition);
							
			editor.ui.addButton( 'equation',
				{
					label : editor.lang.equation.title,
					command : com,
					icon: this.path + 'images/equation.gif'
				});
	
			// If the "menu" plugin is loaded, register the menu items.
			if ( editor.addMenuItems )
			{
				editor.addMenuItems(
					{
						equation :
						{
							label : 'Edit Equation',
							command : 'equation',
							group : 'equation'
						}
					});
			}	
			
			editor.on( 'doubleclick', function(evt) 
			  {
					var element = evt.data.element;
					if (element && element.is('img')) 
					{
				  	var sName = element.getAttribute('src').match( /(gif|svg)\.latex\?(.*)/ );
				 	  if(sName!=null)
						{
				 		  // LAMS fix; a special field in global CKEDITOR object so later call to function CKEditor_Add()
				 		  // knows which editor to update; no other approach (currentEdit,CKEDITOR.currentInstance) was working;
				 		  	CKEDITOR.equationInstance=evt.editor;
				 		  	
							createEqnDefinition.exec(null, sName[2]);	
							evt.cancelBubble = true; 
              evt.returnValue = false; 
              evt.stopPropagation();
						}
			    }
				}, null, null, 1);
	
		}
		

	});
	
	
	// Add a new placeholder at the actual selection.
	CKEditor_Add = function( name )
	{
		var sName = name.match( /(gif|svg)\.latex\?(.*)/ );
		var latex= unescape(sName[2]);
		latex = latex.replace(/@plus;/g,'+');
		latex = latex.replace(/&plus;/g,'+');
		latex = latex.replace(/&space;/g,' ');
		
		// LAMS fix; currentEdit is probably null so try to get it from currentInstance
		if (currentEdit == null) {
			currentEdit = CKEDITOR.currentInstance;
		}
		
		// if it's still null, try to get it from special field
		if (currentEdit == null) {
			currentEdit = CKEDITOR.equationInstance;
		}
		
		currentEdit.insertHtml('<img src="'+name+'" alt="'+latex+'" align="absmiddle" />');
	}		
	
})();	
	

