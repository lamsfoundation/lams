// Register the related command.
FCKCommands.RegisterCommand( 'WikiLink', new FCKDialogCommand( 'WikiLink', FCKLang.WikiLinkDlgTitle, FCKPlugins.Items['wikilink'].Path + 'fck_wikilink.html', 300, 220 ) ) ;

// Create the "WikiLink" toolbar button.
var oWikiLinkItem = new FCKToolbarButton( 'WikiLink', FCKLang.WikiLinkBtn ) ;
oWikiLinkItem.IconPath = FCKPlugins.Items['wikilink'].Path + 'wikilink.gif' ;

FCKToolbarItems.RegisterItem( 'WikiLink', oWikiLinkItem ) ;

// The object used for all WikiLink operations.
var FCKWikiLinks = new Object() ;

FCKWikiLinks.InsertWikiLink = function ( linkName, wikiName )
{
	FCK.InsertHtml( '<a href="' + wikiName + '">' + linkName + '</a>' );
}





