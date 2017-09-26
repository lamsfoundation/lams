// The Inline versions are the default with the inline CKEditor.tag. If using the old (replace) version of the tag then then the non-inline ones are used.
// Main difference Source became Sourcedialog and Maximize and Preview were dropped.

CKEDITOR.config.toolbar_Default = [
	['Source','-','Maximize', 'Preview','PasteFromWord','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath','-'], 
	['Embed','Image','Link','Iframe','Table','HorizontalRule','Smiley','SpecialChar','Format','Font','FontSize','About']
];

CKEDITOR.config.toolbar_DefaultInline = [
	['Sourcedialog','-','PasteFromWord','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath','-'], 
    ['Embed','Image','Link','Iframe','Table','HorizontalRule','Smiley','SpecialChar','Format','Font','FontSize','About']
];

CKEDITOR.config.toolbar_DefaultDokuInline = [
	['Bold','Italic','Underline','StrikeThrough','NumberedList','BulletedList','Indent','Outdent','Undo','Redo']
];

// removing Video Recorder from default tool bar LDEV-2961
// To include it back, just add 'VideoRecorder' in between the MoviePlayer and Kaltura

CKEDITOR.config.toolbar_DefaultLearner = [
	['Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['Jlatexmath','About'],
	['TextColor','BGColor'],
	['Image','Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_DefaultLearnerInline = [
  	['PasteFromWord'],
  	['Undo','Redo'],
  	['Bold','Italic','Underline', '-','Subscript','Superscript'],
  	['NumberedList','BulletedList','-','Outdent','Indent'],
  	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  	['Jlatexmath','About'],
  	['TextColor','BGColor'],
  	['Image','Table','HorizontalRule','Smiley','SpecialChar'],
  	['Format','Font','FontSize']
  ];

CKEDITOR.config.toolbar_DefaultMonitor = [
	['Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['Jlatexmath','About'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_DefaultMonitorInline = [
  	['PasteFromWord'],
  	['Undo','Redo'],
  	['Bold','Italic','Underline', '-','Subscript','Superscript'],
  	['NumberedList','BulletedList','-','Outdent','Indent'],
  	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  	['Jlatexmath','About'],
  	['TextColor','BGColor'],
  	['Table','HorizontalRule','Smiley','SpecialChar'],
  	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomWiki = [
	['Source','-','Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['wikilink','Link','Image'],
	['Jlatexmath','About'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomWikiInline = [
  	['Sourcedialog','-','PasteFromWord'],
  	['Undo','Redo'],
  	['Bold','Italic','Underline', '-','Subscript','Superscript'],
  	['NumberedList','BulletedList','-','Outdent','Indent'],
  	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  	['wikilink','Link','Image'],
  	['Jlatexmath','About'],
  	['TextColor','BGColor'],
  	['Table','HorizontalRule','Smiley','SpecialChar'],
  	['Format','Font','FontSize']
];


CKEDITOR.config.toolbar_CustomPedplanner = [
	['Source','-','Maximize','Preview','PasteFromWord','Bold','Italic','Underline', '-','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath'], 
	['Image','Link','Iframe','Table','Smiley','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomPedplannerInline = [
	['Sourcedialog','-','PasteFromWord','Bold','Italic','Underline', '-','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath'], 
	['Image','Link','Iframe','Table','Smiley','Font','FontSize']
];

CKEDITOR.config.toolbar_LessonDescription = [
    ['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_LessonDescriptionInline = [
    ['Bold','Italic','Underline', '-','Subscript','Superscript'],
 	['NumberedList','BulletedList','-','Outdent','Indent'],
 	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
 	['TextColor','BGColor'],
 	['Table','HorizontalRule','Smiley','SpecialChar'],
 	['Format','Font','FontSize']
 ];


CKEDITOR.config.contentsCss = CKEDITOR.basePath + '../css/defaultHTML_learner.css';
//CKEDITOR.config.skin = 'office2013' ;
CKEDITOR.config.disableNativeSpellChecker = false;
CKEDITOR.config.browserContextMenuOnCtrl = true;
CKEDITOR.config.format_tags	= 'div;h1;h2;h3;h4;h5;h6;pre;address;p' ;
CKEDITOR.config.enterMode = 'div';
CKEDITOR.plugins.addExternal('wikilink', CKEDITOR.basePath + '../tool/lawiki10/wikilink/', 'plugin.js');
CKEDITOR.config.extraPlugins = 'wikilink,jlatexmath,iframe,lineutils,widget,embed,sourcedialog,dialog';
CKEDITOR.config.enterMode = CKEDITOR.ENTER_DIV; 
CKEDITOR.config.removePlugins = 'elementspath';
CKEDITOR.config.allowedContent = true;
CKEDITOR.config.toolbarCanCollapse = true;

// ---- Additional scripts -----
// Hides editor instaces until they are fully initialized

CKEDITOR.on('instanceCreated', function(e){
	e.editor.element.$.style.display = 'none';
});

CKEDITOR.on('instanceReady', function(e){
	var height = e.editor.config.height;
	if ( ! height ) {
		height = "60px";
	} else if ( height.indexOf("px") == -1 ){
		height = height + "px";
	}
	e.editor._.editable.$.style.minHeight = height;
	
	// make all links open in new window
	e.editor.on('getData', function(f){
		// create a DOM element for easier manipulation
		var tempDiv = document.createElement('div');
		tempDiv.innerHTML = f.data.dataValue;
		// this has to be here for parsing to be commenced!
		tempDiv.childNodes;
		
		var anchors = tempDiv.getElementsByTagName('a');
		for (var i = 0; i < anchors.length; i++) {
			anchors[i].setAttribute('target', '_blank');
		}
		
		f.data.dataValue = tempDiv.innerHTML;
	});
});
