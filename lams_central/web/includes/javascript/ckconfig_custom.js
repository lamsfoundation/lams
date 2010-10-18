CKEDITOR.config.toolbar_Default = [
	['Source','-','FitWindow', 'Preview','PasteWord','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','OrderedList','UnorderedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyFull','TextColor','BGColor','Equation'], 
	['Paint_Button','MoviePlayer','VideoRecorder','Image','Link','Table','Rule','Smiley','SpecialChar','Templates','FontFormat','FontName','FontSize','About']
] ;

CKEDITOR.config.toolbar_DefaultLearner = [
	['Preview','PasteWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['Equation','About'],
	['TextColor','BGColor'],
	['Table','Rule','Smiley','SpecialChar'],
	['FontFormat','FontName','FontSize']
] ;

CKEDITOR.config.toolbar_CustomWiki = [
	['Preview','PasteWord'],
	['Undo','Redo'],
	['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['WikiLink','Link','Image'],
	['Equation','About'],
	['TextColor','BGColor'],
	['Table','Rule','Smiley','SpecialChar'],
	['FontFormat','FontName','FontSize']
] ;

CKEDITOR.config.toolbar_CustomPedplanner = [
					      ['Source','-','FitWindow','Preview','PasteWord','Bold','Italic','Underline', '-','OrderedList','UnorderedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyFull','TextColor','BGColor','Equation'], 
	['Image','Link','Table','Smiley','FontName','FontSize','WikiLink']
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