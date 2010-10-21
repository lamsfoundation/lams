CKEDITOR.config.toolbar_Default = [
	['Source','-','Maximize', 'Preview','PasteFromWord','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Equation'], 
	['Paint_Button','MoviePlayer','VideoRecorder','Image','Link','Table','HorizontalRule','Smiley','SpecialChar','Templates','Format','Font','FontSize','About']
] ;

CKEDITOR.config.toolbar_DefaultLearner = [
	['Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['Equation','About'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
] ;

CKEDITOR.config.toolbar_CustomWiki = [
	['Preview','PasteFromWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['WikiLink','Link','Image'],
	['Equation','About'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
] ;

CKEDITOR.config.toolbar_CustomPedplanner = [
	['Source','-','Maximize','Preview','PasteFromWord','Bold','Italic','Underline', '-','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Equation'], 
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
// CKEDITOR.plugins.add('equation') ;
// CKEDITOR.plugins.add('movieplayer') ;
// CKEDITOR.plugins.add('videorecorder') ;
// CKEDITOR.plugins.add('paint') ;
CKEDITOR.plugins.addExternal('WikiLink', CKEDITOR.basePath + '../tool/lawiki10/wikilink/', 'plugin.js') ;
//CKEDITOR.config.extraPlugins = 'equation,movieplayer,videorecorder,paint,wikilink' ;
CKEDITOR.config.extraPlugins = 'WikiLink' ;

// ---- Additional scripts -----
// Hides editor instaces until they are fully initialized

CKEDITOR.on('instanceCreated', function(e){
	e.editor.element.$.style.display = 'none';
});

CKEDITOR.on('instanceReady', function(e){
	e.editor.element.$.style.display = '';
});