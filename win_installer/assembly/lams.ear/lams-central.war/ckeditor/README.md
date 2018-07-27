CKEditor 4
==========

Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
http://ckeditor.com - See LICENSE.md for license information.

## Installation

1. Go to https://ckeditor.com/cke4/builder. Select Full. Upload /lams_central/web/ckeditor/build-config.js

2. Download CKEditor

3. Copy plugins developed by LAMS from the old instance of CKEditor
/ckeditor/plugins/jlatexmath
/ckeditor/plugins/bootstrapTabs
/ckeditor/plugins/kaltura
/ckeditor/plugins/bootsnippets

4. Apply all customizations listed below in ## LAMS Customizations


## LAMS Customizations

1. In order to make Preview feature available in inline mode, Preview plugin needed to be tweaked. To achieve it the next command
"a.elementMode!=CKEDITOR.ELEMENT_MODE_INLINE&&(" .. ")" was removed from the ckeditor.js; precisely from that line:
CKEDITOR.plugins.add("preview",{init:function(a){a.elementMode!=CKEDITOR.ELEMENT_MODE_INLINE&&(h=this.path,a.addCommand

(As in https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=3f3b286c8daddd86677d64fcd08366c33fbfd927)

2. Emojione button.
When adding Emojione plugin as integrated one, its icon gets incorporated into icons.png incorrectly (the size of the icon is much bigger than 16 px) that's why it doesn't get displayed at all. 
So we need to tweak editor.css and replace CSS rule for .cke_button__emojione_icon with the following:
.cke_button__emojione_icon {background: url(../../plugins/emojione/icons/emojione.png?t=dee79e2) no-repeat 0 0 !important; background-size: 100% !important;}
Also do not delete /ckeditor/plugins/emojione/icons/emojione.png

(As in https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=781f0147a8670550edaf7a482d85a4504f93e564)

3. Preview should show text on a white background
Don't add class to the resulted preview body. It can be achieved by
removing the following line from the ckeditor.js:
,d.getAttribute("class")&&(b+='class\x3d"'+d.getAttribute("class")+'" ')

(As in https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=f8ccd872d79996df2a3d0e8485e2c95ccbef044b)

4. bootstrapTabs plugin. The "element.getAscendant"'s function that was specified on Double click and contextMenu listener methods were replaced with the following "return !( element instanceof CKEDITOR.dom.document ) && element.hasClass('bootstrap-tabs') ;". As otherwise it always false positively determined there is a tabs element in CKEditor canvas (due to LAMS header containing a tab).

Modify ckeditor.js.
	* replace 
	return!(a instanceof CKEDITOR.dom.document)&&(a.hasClass("nav-tabs")||a.hasClass("tab-content"))
	with the following
	return!(a instanceof CKEDITOR.dom.document)&&a.hasClass("bootstrap-tabs")

	* replace 
	return!(a instanceof CKEDITOR.dom.document)&&("a"==a.name&&"tab"==a.attributes.role||a.hasClass("nav-tabs")||a.hasClass("tab-content"))
	with the following
	return!(a instanceof CKEDITOR.dom.document)&&a.hasClass("bootstrap-tabs")

(Similar to https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=2fbab8924ec9cef2ab6ed231386042fc8ab17fe4)

5. Modify html5audio plugin.
Add support for double click event. Also add padding around widget to increate area where user can double click and right click when trying to open html5audio dialog.

	*after
	this.element.getChild(0).setAttribute("controlslist","nodownload"))
	insert
	;
	//*LAMS* add padding around widget to increase area where user can double click and right click when trying to open html5audio dialog
	var elementCssPadding = this.element.getStyle( 'padding').replace('px', '');
	if ( elementCssPadding < 10) {
		this.element.setStyle( 'padding', '10px' );
	}
	
	*after 
	CKEDITOR.dialog.add("html5audio",this.path+"dialogs/html5audio.js")
	insert
	;
	b.on( 'doubleclick', function( evt ) {
		var element = evt.data.element;
	
		//check if it has parent with class .lams-bootpanel
		if ( element && b.elementPath( element ).contains( function( element ) {        
		 	if ( element.is( 'div' ) && element.hasClass( 'ckeditor-html5-audio' )) {
		 		return true;
		 	}
		})) {
			evt.data.dialog = 'html5audio';
		} 
	});

(Similar to https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=091a2b538d13f42a153dc5d0137ea6caffbd983b)

6. Open JLatexmath dialog when clicking on its image
After
!a.attributes["data-cke-realelement"]
Add
&&!a.attributes["data-jlatexmath"]

(Similar to https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=423d88c3f279da62c8802e3f9df86654a659fcb3)

7. Including bootstrap tables CSS styles.

Replace /lams_central/web/ckeditor/plugins/table/dialogs/table.js entirely with the following file https://code.lamsfoundation.org/fisheye/browse/~raw,r=2b9f93362e5be1cb3a8718e7f8f26bda31bd4a60/lams-github/lams_central/web/ckeditor/plugins/table/dialogs/table.js

(as in https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=2b9f93362e5be1cb3a8718e7f8f26bda31bd4a60)

8. image2 plugin. Add 'classes' field to its dialog.

	* Edit /lams_central/web/ckeditor/plugins/image2/dialogs/image2.js
	Before
	{type:"hbox",widths:["25%","25%","50%"]
	Add
	//**LAMS** the following classes chunk added by LAMS 
	{
	    id: 'classes',
	    type: 'text',
	    label: 'Classes',
	    setup: function( widget ) {
	    	var classesObj = widget.getClasses();
	    	var classesNames = [];
	    	for (var classesName in classesObj) {
	            if (classesObj.hasOwnProperty(classesName)) {
	            	classesNames.push(classesName);
	            }
	        }
	        this.setValue( classesNames.join(',') );
	        
	    },
	    commit: function( widget ) {
	    	var oldClasses = widget.getClasses();
	    	
	    	//add new classes
	    	var userEnteredClasses = this.getValue().split(',');
	    	for (var i = 0; i < userEnteredClasses.length; i++) {
	    		var userEnteredClass = userEnteredClasses[i];
	    	    if (!widget.hasClass(userEnteredClass)) {
	    	    	widget.addClass(userEnteredClass);
	    	    }
	    	}
	        
	    	//remove obsolete classes
	        for (var oldClass in oldClasses) {
	            if (oldClasses.hasOwnProperty(oldClass) && !userEnteredClasses.includes(oldClass)) {
	            	widget.removeClass(oldClass);
	            }
	        }
	    }
	},
	
	* Edit /ckeditor/ckeditor.js
	Replace two instances of 
	\x3cimg alt\x3d"" src\x3d
	With the following
	\x3cimg class\x3d"img-responsive" alt\x3d"" src\x3d
	
(As in https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=62a52d851ecb5878433ca8ecd5ef422375619f47)


9. Add jqeury.js and bootstrap.min.js scripts to preview page
After
"\x3c/title\x3e"+CKEDITOR.tools.buildStyleHtml(a.config.contentsCss)+
Insert the following:
"<script type='text/javascript' src='/lams/includes/javascript/jquery.js'></script><script type='text/javascript' src='/lams/includes/javascript/bootstrap.min.js'></script>"+

(As in https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=cc93c9f975e956a0f5e70032c0dbc1beb106e2fb)

10. CKEditor's Upload image dialog doesn't work correctly (LDEV-4557)
...
