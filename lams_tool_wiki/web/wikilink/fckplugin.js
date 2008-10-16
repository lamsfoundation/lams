// Register the related command.

FCKCommands.RegisterCommand( 'WikiLink', new FCKDialogCommand( 'WikiLink', FCKLang.WikiLinkBtn, FCKPlugins.Items['wikilink'].Path + 'fck_wikilink.jsp', 280, 230 ) ) ;

// Create the "WikiLink" toolbar button.
var oWikiLinkItem = new FCKToolbarButton( 'WikiLink', FCKLang.WikiLinkToolTip ) ;
oWikiLinkItem.IconPath = FCKPlugins.Items['wikilink'].Path + 'wikilink.gif' ;

FCKToolbarItems.RegisterItem( 'WikiLink', oWikiLinkItem ) ;

// The object used for all WikiLink operations.
var FCKWikiLinks = new Object() ;

FCKWikiLinks.InsertWikiLink = function ( linkAlias, wikiName )
{
	FCK.InsertHtml( '<a href="' + wikiName + '">' + linkAlias + '</a>' );
}


