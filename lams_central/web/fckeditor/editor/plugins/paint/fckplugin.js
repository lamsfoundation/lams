FCKCommands.RegisterCommand( 'Paint canvas' , new FCKDialogCommand( FCKLang.Paint.DialogName , FCKLang.Paint.DialogTitle , FCKConfig.PluginsPath + 'paint/content.jsp' , 852, 610 ) ) ;

var oPaintItem = new FCKToolbarButton( 'Paint canvas', FCKLang.Paint.Button ) ;
oPaintItem.IconPath = FCKConfig.PluginsPath + 'paint/icon.png';

FCKToolbarItems.RegisterItem( 'Paint_Button', oPaintItem ) ;

var PaintCommand = new Object();
PaintCommand.Add=function(source) {
	FCK.InsertHtml('<img alt="FCKPainting" src="' + FCKConfig.PluginsPath + 'paint/' + source + '" style="border: none;"/>');
	FCK.Focus();
}
PaintCommand.startingImage = "";
PaintCommand.OnDoubleClick = function( image )
{
	if(image.alt == "FCKPainting"){
		PaintCommand.startingImage = image.src;
		FCKCommands.GetCommand('Paint canvas').Execute();
	}
}
FCK.RegisterDoubleClickHandler( PaintCommand.OnDoubleClick, 'IMG' ) ;