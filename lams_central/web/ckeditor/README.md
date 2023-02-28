CKEditor 4
==========

Copyright (c) 2003-2021, CKSource - Frederico Knabben. All rights reserved.
https://ckeditor.com - See LICENSE.md for license information.

CKEditor 4 is a text editor to be used inside web pages. It's not a replacement
for desktop text editors like Word or OpenOffice, but a component to be used as
part of web applications and websites.

## Documentation

The full editor documentation is available online at the following address:
https://ckeditor.com/docs/

## Installation

Installing CKEditor is an easy task. Just follow these simple steps:

 1. **Download** the latest version from the CKEditor website:
    https://ckeditor.com.  
    You should have already completed this step, but be
    sure you have the very latest version.
    
    Include the following plugins with latest version: uploadwidget, uploadimage
    
 2. **Extract** (decompress) the downloaded file into the root of your website.
 
 3. Copy plugins developed by LAMS from the old instance of CKEditor  
/ckeditor/filemanager  
/ckeditor/plugins/jlatexmath  
/ckeditor/plugins/bootsnippets  
/ckeditor/plugins/bootpanel  
/ckeditor/plugins/confighelper  
/ckeditor/plugins/wavepanel  

 4. Apply all customizations listed below in LAMS Customizations
 
**Note:** CKEditor is by default installed in the `ckeditor` folder. You can
place the files in whichever you want though.

## Checking Your Installation

The editor comes with a few sample pages that can be used to verify that
installation proceeded properly. Take a look at the `samples` directory.

To test your installation, just call the following page at your website:

	http://<your site>/<CKEditor installation path>/samples/index.html

For example:

	http://www.example.com/ckeditor/samples/index.html
	
## LAMS Customizations

- (NOT REQUIRED ANYMORE AS OF CKEDITOR 4.16.0) In order to make Preview feature available in inline mode, Preview plugin needed to be tweaked. To achieve it the next command
"a.elementMode!=CKEDITOR.ELEMENT_MODE_INLINE&&(" .. ")" was removed from the ckeditor.js; precisely from that line:
CKEDITOR.plugins.add("preview",{init:function(a){a.elementMode!=CKEDITOR.ELEMENT_MODE_INLINE&&(h=this.path,a.addCommand  
(As in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=3f3b286c8daddd86677d64fcd08366c33fbfd927](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=3f3b286c8daddd86677d64fcd08366c33fbfd927))  

- Preview should show text on a white background  
Don't add class to the resulted preview body. It can be achieved by
removing the following line from the ckeditor.js:
,d.getAttribute("class")&&(b+='class\x3d"'+d.getAttribute("class")+'" ')
(As in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=f8ccd872d79996df2a3d0e8485e2c95ccbef044b](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=f8ccd872d79996df2a3d0e8485e2c95ccbef044b))  
For CKEditor 4.16.2 the code to remove is  
b.getAttribute("class")&&(c=c.replace("\x3e",' class\x3d"'+b.getAttribute("class")+'"\x3e'));
  
- bootstrapTabs plugin   
The "element.getAscendant"'s function that was specified on Double click and contextMenu listener methods were replaced with the following "return !( element instanceof CKEDITOR.dom.document ) && element.hasClass('bootstrap-tabs') ;". As otherwise it always false positively determined there is a tabs element in CKEditor canvas (due to LAMS header containing a tab).  
Modify plugins/bootstrapTabs/dialogs/plugin.js  
or optimised ckeditor.js

```
	replace 
	return!(a instanceof CKEDITOR.dom.document)&&(a.hasClass("nav-tabs")||a.hasClass("tab-content"))
	with the following
	return!(a instanceof CKEDITOR.dom.document)&&a.hasClass("bootstrap-tabs")
```

(Similar to [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=2fbab8924ec9cef2ab6ed231386042fc8ab17fe4](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=2fbab8924ec9cef2ab6ed231386042fc8ab17fe4))  
It also seems that CKEditor optimiser breaks brackets, so pay attention to what is in the non-optimised plugin.js and reproduce the same logical operators (with modifications) in the optimised ckeditor.js  
So it should be

```
return!(a instanceof CKEDITOR.dom.document)&&(("a"==a.name&&"tab"==a.attributes.role)||a.hasClass("bootstrap-tabs"))
instead of
return!(a instanceof CKEDITOR.dom.document)&&("a"==a.name&&"tab"==a.attributes.role||a.hasClass("bootstrap-tabs"))
```
  
- Modify html5audio plugin.  
(NOT USED IN ANY TOOLBAR SO IGNORED FOR CKEDITOR UPGRADE TO 4.16.0)  
Add support for double click event. Also add padding around widget to increate area where user can double click and right click when trying to open html5audio dialog.

```
	*after
	this.element.getChild(0).setAttribute("controlslist","nodownload"))
	insert
	;
	//*LAMS* add padding around widget to increase area where user can double click and right click when trying to open html5audio dialog
	var elementCssPadding = this.element.getStyle( 'padding').replace('px', '');
	if ( elementCssPadding < 10) {
		this.element.setStyle( 'padding', '10px' );
	}
```
```
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
```

(Similar to [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=091a2b538d13f42a153dc5d0137ea6caffbd983b](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=091a2b538d13f42a153dc5d0137ea6caffbd983b))

- Open JLatexmath dialog when clicking on its image  
In ckeditor.js
After
!a.attributes["data-cke-realelement"]
Add
&&!a.attributes["data-jlatexmath"]

(Similar to [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=423d88c3f279da62c8802e3f9df86654a659fcb3](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=423d88c3f279da62c8802e3f9df86654a659fcb3))

- Maximise JLatexmath content after load  
The JLatexmath iframe in dialog is not maximised in height on first load.
Subsequent loads put correct height but first load is confusing, the iframe is very short.
We need to add onLoad function as the last parameter when adding the iframe and manually set its height.
Modify /plugins/jlatexmath/plugin.js and add the last argument (function) to the following code

```
CKEDITOR.dialog.addIframe(
	'Jlatexmath',
	editor.lang.jlatexmath.DlgJlatexmathTitle,
	pluginPath + 'jlatexmath.jsp',
	950,
	530,
	function() {
		$('#' + this.domId).height(530);
	}
);
```

- Including bootstrap tables CSS styles.  
Replace /lams_central/web/ckeditor/plugins/table/dialogs/table.js entirely with the following file [https://code.lamsfoundation.org/fisheye/browse/~raw,r=2b9f93362e5be1cb3a8718e7f8f26bda31bd4a60/lams-github/lams_central/web/ckeditor/plugins/table/dialogs/table.js](https://code.lamsfoundation.org/fisheye/browse/~raw,r=2b9f93362e5be1cb3a8718e7f8f26bda31bd4a60/lams-github/lams_central/web/ckeditor/plugins/table/dialogs/table.js)  
(as in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=2b9f93362e5be1cb3a8718e7f8f26bda31bd4a60](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=2b9f93362e5be1cb3a8718e7f8f26bda31bd4a60))  
For 4.16.0 upgrade following code has been added

```
* after
if (!table.getAttribute('style'))
	table.removeAttribute('style');
		
* added		
						
var bootstrapClass = 'table';
bootstrapClass += this.getValueOf('bootstrapCssTab', 'stripedRows') ? ' table-striped' : '';
bootstrapClass += this.getValueOf('bootstrapCssTab', 'bordered') ? ' table-bordered' : '';
bootstrapClass += this.getValueOf('bootstrapCssTab', 'hover') ? ' table-hover' : '';
bootstrapClass += this.getValueOf('bootstrapCssTab', 'condensed') ? ' table-condensed' : '';
table.$.className = bootstrapClass;

* then in "contents" table after "info" added
{
	id: 'bootstrapCssTab',
	label: 'Bootstrap CSS',
	elements: [
		{
			type: 'hbox',
			widths: ['50%', '50%'],
			children: [
				{
					id: 'stripedRows',
					label: 'Striped Rows',
					type: 'checkbox',
					value: 'table-striped',
					setup: function(a) {
						var tableClasses = (a.getAttribute('class')).split(' ');
						this.setValue(mj_in_array('table-striped', tableClasses));
					}
				},
				{
					id: 'bordered',
					label: 'Bordered',
					type: 'checkbox',
					value: 'table-bordered',
					setup: function(a) {
						var tableClasses = (a.getAttribute('class')).split(' ');
						this.setValue(mj_in_array('table-bordered', tableClasses));
					}
				}
			]
		},
		{
			type: 'hbox',
			widths: ['50%', '50%'],
			children: [
				{
					id: 'hover',
					label: 'Hover',
					type: 'checkbox',
					value: 'table-hover',
					setup: function(a) {
						var tableClasses = (a.getAttribute('class')).split(' ');
						this.setValue(mj_in_array('table-hover', tableClasses));
					}
				},
				{
					id: 'condensed',
					label: 'Condensed',
					type: 'checkbox',
					value: 'table-condensed',
					setup: function(a) {
						var tableClasses = (a.getAttribute('class')).split(' ');
						this.setValue(mj_in_array('table-condensed', tableClasses));
					}
				}
			]
		}
	]
},
```

- image2 plugin. Add 'classes' field to its dialog.

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
	
(As in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=62a52d851ecb5878433ca8ecd5ef422375619f47](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=62a52d851ecb5878433ca8ecd5ef422375619f47))


- Add jquery.js and bootstrap.min.js scripts to preview page  
After  
"\x3c/title\x3e"+CKEDITOR.tools.buildStyleHtml(d.contentsCss)+  
Insert the following:  

```
"<script type='text/javascript' src='/lams/includes/javascript/jquery.js'></script><script type='text/javascript' src='/lams/includes/javascript/bootstrap.min.js'></script>"+
```
(As in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=cc93c9f975e956a0f5e70032c0dbc1beb106e2fb](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=cc93c9f975e956a0f5e70032c0dbc1beb106e2fb))  

- Do not set default tooltip to CKEditor instances  
By default, CKEditor adds a "title" attribute to its instances, which results in annoying tooltip shown.  
We remove it, but may have to review it when working on accessibility.  
Remove from ckeditor.js

```
a.changeAttr("aria-label",c);c&&a.changeAttr("title",c)
```
(As in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=4f3c21320d4784bc94f86b946f1fd304ce442b01](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=4f3c21320d4784bc94f86b946f1fd304ce442b01))

- Fix error when editing image properties and there are no classes set  
The error in JS console: "Failed to execute 'add' on 'DOMTokenList': The token provided must not be empty."  
In ckeditor.js change

```
type:CKEDITOR.NODE_ELEMENT,addClass:g?function(a){this.$.classList.(a);return this}
```
to

```
type:CKEDITOR.NODE_ELEMENT,addClass:g?function(a){if(a)this.$.classList.(a);return this}
```
(As in [https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=cfec5d4c0ffe681e5e2604dc1163b309974e21f0](https://code.lamsfoundation.org/fisheye/changelog/lams-github?cs=cfec5d4c0ffe681e5e2604dc1163b309974e21f0))  

- Add wordcount and notification plugins   
These plugins are required for checking max word count in essay questions.   
They are a part of CKEditor build file already, but they can be added manually too.   
The sking office2013 does not have a notification.css file nor its import is added to editor.css. It can be taken from existing LAMS code or directly from another skin.