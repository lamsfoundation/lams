CKEDITOR.config.toolbar_Default = [
	['Source','-','Maximize', 'Preview','PasteFromWord','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','equation'], 
	['Paint_Button','MoviePlayer','VideoRecorder','Kaltura','Image','Link','Table','HorizontalRule','Smiley','SpecialChar','Templates','Format','Font','FontSize','About']
] ;

CKEDITOR.config.toolbar_DefaultLearner = [
	['Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['equation','About'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
] ;

CKEDITOR.config.toolbar_CustomWiki = [
	['Source','-','Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['WikiLink','Link','Image'],
	['equation','About'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
] ;

CKEDITOR.config.toolbar_CustomPedplanner = [
	['Source','-','Maximize','Preview','PasteFromWord','Bold','Italic','Underline', '-','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','equation'], 
	['Image','Link','Table','Smiley','Font','FontSize']
] ;


CKEDITOR.config.contentsCss = CKEDITOR.basePath + '../css/defaultHTML_learner.css' ;
CKEDITOR.config.skin = 'office2003' ;
CKEDITOR.config.disableNativeSpellChecker = false;
CKEDITOR.config.browserContextMenuOnCtrl = true;
// CKEDITOR.config.DefaultLinkTarget = "_blank";
CKEDITOR.config.templates = CKEDITOR.basePath + '../www/htmltemplates.xml';
CKEDITOR.config.format_tags	= 'div;h1;h2;h3;h4;h5;h6;pre;address;p' ;
CKEDITOR.config.enterMode = 'div' ;
CKEDITOR.plugins.addExternal('WikiLink', CKEDITOR.basePath + '../tool/lawiki10/wikilink/', 'plugin.js') ;
CKEDITOR.config.extraPlugins = 'videorecorder,kaltura,WikiLink,equation,paint,movieplayer' ;
CKEDITOR.config.enterMode = CKEDITOR.ENTER_DIV; 
CKEDITOR.config.removePlugins = 'elementspath';

// ---- Additional scripts -----
// Hides editor instaces until they are fully initialized

CKEDITOR.on('instanceCreated', function(e){
	e.editor.element.$.style.display = 'none';
});

CKEDITOR.on('instanceReady', function(e){
	e.editor.element.$.style.display = '';
});
