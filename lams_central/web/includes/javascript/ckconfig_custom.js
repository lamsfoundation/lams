// The Inline versions are the default with the inline CKEditor.tag. If using the old (replace) version of the tag then then the non-inline ones are used.
// Main difference Source became Sourcedialog and Maximize and Preview were dropped.

CKEDITOR.config.toolbar_Default = [
	['Source','Preview','-','Maximize','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath','-'], 
	['Link','Table','HorizontalRule','-','Emojione','bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','bootsnippets-advanced-layout','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_DefaultInline = [
	['Sourcedialog','Preview','-','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath','-'], 
    ['Link','Table','HorizontalRule','-','Emojione','bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','bootsnippets-advanced-layout','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_DefaultDokuInline = [
	['Bold','Italic','Underline','StrikeThrough','NumberedList','BulletedList','Indent','Outdent','Undo','Redo']
];

// removing Video Recorder from default tool bar LDEV-2961
// To include it back, just add 'VideoRecorder' in between the MoviePlayer and Kaltura

CKEDITOR.config.toolbar_DefaultLearner = [
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['Jlatexmath'],
	['TextColor','BGColor'],
	['Table','HorizontalRule'],
	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_DefaultLearnerInline = [
  	['Undo','Redo'],
  	['Bold','Italic','Underline', '-','Subscript','Superscript'],
  	['NumberedList','BulletedList','-','Outdent','Indent'],
  	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  	['Jlatexmath','-','Link'],
  	['TextColor','BGColor'],
  	['Table','HorizontalRule'],
  	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','Format','Font','FontSize']
  ];

CKEDITOR.config.toolbar_DefaultMonitor = [
	['Preview'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['Jlatexmath'],
	['TextColor','BGColor'],
	['Table','HorizontalRule'],
	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_DefaultMonitorInline = [
  	['Undo','Redo'],
  	['Bold','Italic','Underline', '-','Subscript','Superscript'],
  	['NumberedList','BulletedList','-','Outdent','Indent'],
  	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  	['Jlatexmath'],
  	['TextColor','BGColor'],
  	['Table','HorizontalRule'],
  	['Format','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomWiki = [
	['Source','-','Preview'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['wikilink','Link'],
	['Jlatexmath'],
	['TextColor','BGColor'],
	['Table','HorizontalRule'],
	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomWikiInline = [
  	['Sourcedialog'],
  	['Undo','Redo'],
  	['Bold','Italic','Underline', '-','Subscript','Superscript'],
  	['NumberedList','BulletedList','-','Outdent','Indent'],
  	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  	['wikilink','Link'],
  	['Jlatexmath'],
  	['TextColor','BGColor'],
  	['Table','HorizontalRule'],
  	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomWikiLearner = [
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['wikilink','Link'],
	['Jlatexmath'],
	['TextColor','BGColor'],
	['Table','HorizontalRule'],
	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_CustomWikiLearnerInline = CKEDITOR.config.toolbar_CustomWikiLearner;

CKEDITOR.config.toolbar_LessonDescription = [
    ['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['TextColor','BGColor'],
	['Table','HorizontalRule'],
	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','-','Format','Font','FontSize']
];

CKEDITOR.config.toolbar_LessonDescriptionInline = [
    ['Bold','Italic','Underline', '-','Subscript','Superscript'],
 	['NumberedList','BulletedList','-','Outdent','Indent'],
 	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
 	['TextColor','BGColor'],
 	['Table','HorizontalRule'],
 	['bootsnippets-text','bootsnippets-image','bootsnippets-multimedia','-','Format','Font','FontSize']
 ];


CKEDITOR.config.contentsCss = CKEDITOR.basePath + '../css/defaultHTML_learner.css';
CKEDITOR.config.disableNativeSpellChecker = false;
CKEDITOR.config.browserContextMenuOnCtrl = true;
CKEDITOR.config.bootsnippets = 'bootsnippets-text, bootsnippets-image, bootsnippets-multimedia, bootsnippets-advanced-layout';
CKEDITOR.config.bootsnippets_icons = CKEDITOR.basePath + '/plugins/bootsnippets/icons/ico_text.ico, ' + CKEDITOR.basePath + '/plugins/bootsnippets/icons/ico_image.ico, ' + CKEDITOR.basePath + '/plugins/bootsnippets/icons/ico_video.png, form';
CKEDITOR.config.bootsnippets_labels = 'Text Templates, Image Templates, Multimedia Templates, Advanced Layout Templates';
CKEDITOR.config.bootsnippets_files = [CKEDITOR.basePath + '../www/public/ckeditor-templates/bootsnippets.js'];
CKEDITOR.config.format_tags	= 'div;h1;h2;h3;h4;h5;h6;pre;address;p' ;
CKEDITOR.plugins.addExternal('wikilink', CKEDITOR.basePath + '../tool/lawiki10/wikilink/', 'plugin.js');
// html5audio is available but not used anymore; it probably needs fixes as in CKEditor README doc
CKEDITOR.config.extraPlugins = 'wikilink,jlatexmath,image2,confighelper,bootstrapTabs,bootpanel,bootsnippets,wavepanel,wordcount,notification,oembed';
CKEDITOR.config.enterMode = CKEDITOR.ENTER_DIV; 
CKEDITOR.config.removePlugins = 'elementspath,about,specialchar';
CKEDITOR.config.allowedContent = true;
CKEDITOR.config.toolbarCanCollapse = true;
CKEDITOR.config.filebrowserUploadMethod	 = 'form';
//allow empty i and span tags (for font awesome)
CKEDITOR.dtd.$removeEmpty['i'] = false;
CKEDITOR.dtd.$removeEmpty['span'] = false;
// with autoinline enabled there is sometimes a situation when CKEditor double initialises editors created from textarea
CKEDITOR.disableAutoInline = true;

// ---- Additional scripts -----

CKEDITOR.on('instanceReady', function(e){
	//add custom classes
	var classes = e.editor.config.classes;
	if (classes) {
		for (classIter of classes.split(' ')) {
			if (classIter) {
				e.editor._.editable.$.classList.add(classIter);
			}
	    }
	}
	
	//set min-height CSS property
	var height = e.editor.config.height;
	if ( ! height ) {
		height = "60px";
		
	// convert to string, just in case it is a number
	} else if ( ("" + height).indexOf("px") == -1 ){
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
			if (!anchors[i].classList.contains('skip-auto-target')) {
				anchors[i].setAttribute('target', '_blank');
			}
		}
		
		f.data.dataValue = tempDiv.innerHTML;
	});
	
	//function adds "cke_filled" class to CKEditor, if it's not empty. And removes it otherwise.
	var placeholderLessenHandler = function(editor) {
		var ckeditorData = editor.getData();
		
		var isEmpty = (ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0);
		if (isEmpty) {
			editor._.editable.$.classList.remove("cke_filled");
		} else {
			editor._.editable.$.classList.add("cke_filled");
		}
	}
	//add cke_filled class on editor's instanceReady
	placeholderLessenHandler(e.editor);
	//add cke_filled class on editor gets changed
	e.editor.on('change', function(event) {
		placeholderLessenHandler(event.editor);
	});
});

CKEDITOR.config.embed_provider = '//ckeditor.iframe.ly/api/oembed?url={url}&callback={callback}'; //default URL for 'embed' plugin
/* If you want to use LAMS embedding proxy:
Set
CKEDITOR.config.embed_provider = '/lams/embed.do?url={url}&callback={callback}';
You can use absolute URL instead, for example
CKEDITOR.config.embed_provider = 'https://demo.lamsfoundation.org/lams/embed.do?url={url}&callback={callback}';

Then set the real embed provider URL into this property
lams_embed_provider = '//ckeditor.iframe.ly/api/oembed?consent=0';
For iframely remember to use "consent=0" parameter, otherwise each user will be asked whether to trust this content.
You can also append your API key:
lams_embed_provider = '//ckeditor.iframe.ly/api/oembed?consent=0&api_key=YOUR_API_KEY_HERE';

Then put a comma-separated list of domains for which you want to use LAMS embedding, for example
lams_embed_custom_domains = 'video.someuni.ac.uk,media.anotheruni.sg';
*/