CKEDITOR.config.toolbar_Default = [
	['Source','-','Maximize', 'Preview','PasteFromWord','Undo','Redo','Bold','Italic','Underline', '-','Subscript','Superscript','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath','-'], 
	['Paint_Button','MoviePlayer','Kaltura','Image','Link','Iframe','Table','HorizontalRule','Smiley','SpecialChar','Templates','Format','Font','FontSize','About']
] ;
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
] ;

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
] ;

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
] ;

CKEDITOR.config.toolbar_CustomPedplanner = [
	['Source','-','Maximize','Preview','PasteFromWord','Bold','Italic','Underline', '-','NumberedList','BulletedList','-','Outdent','Indent','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','TextColor','BGColor','Jlatexmath'], 
	['Image','Link','Iframe','Table','Smiley','Font','FontSize']
] ;

CKEDITOR.config.toolbar_LessonDescription = [
    ['Bold','Italic','Underline', '-','Subscript','Superscript'],
	['NumberedList','BulletedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	['TextColor','BGColor'],
	['Table','HorizontalRule','Smiley','SpecialChar'],
	['Format','Font','FontSize']
] ;


CKEDITOR.config.contentsCss = CKEDITOR.basePath + '../css/defaultHTML_learner.css';
//CKEDITOR.config.skin = 'office2013' ;
CKEDITOR.config.disableNativeSpellChecker = false;
CKEDITOR.config.browserContextMenuOnCtrl = true;
CKEDITOR.config.templates = CKEDITOR.basePath + '../www/htmltemplates.xml';
CKEDITOR.config.format_tags	= 'div;h1;h2;h3;h4;h5;h6;pre;address;p' ;
CKEDITOR.config.enterMode = 'div';
CKEDITOR.plugins.addExternal('wikilink', CKEDITOR.basePath + '../tool/lawiki10/wikilink/', 'plugin.js');
CKEDITOR.config.extraPlugins = 'kaltura,wikilink,Jlatexmath,paint,movieplayer,iframe';
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
	e.editor.element.$.style.display = '';
	
	// if iframe src was substituted with NTU redirect page before
	// put back the original URL
	var tempDiv = document.createElement('div');
	tempDiv.innerHTML = e.editor.getData();
	
	var iframes = tempDiv.getElementsByTagName('iframe');
	for (var i = 0; i < iframes.length; i++) {
		var iframe = iframes[i],
			index = iframe.src.indexOf(NTU_AUTH_PAGE);
		if (index >= 0) {
			// get the original URL and decode it
			var url = iframe.src.substring(index + NTU_AUTH_PAGE.length);
			iframe.src = decodeURIComponent(url);
		}
	}
	
	e.editor.setData(tempDiv.innerHTML);
	
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
