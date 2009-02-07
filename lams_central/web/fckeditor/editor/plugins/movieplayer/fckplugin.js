/* 
 *  FCKPlugin.js for Movie Player
 *  ------------
 */

// Register the related commands.
FCKCommands.RegisterCommand(
	'MoviePlayer', new FCKDialogCommand('MoviePlayer', FCKLang.DlgMoviePlayerTitle, FCKConfig.PluginsPath + 'movieplayer/movieplayer.html', 450, 260) );
 
// Create the toolbar button.
var oMoviePlayerItem = new FCKToolbarButton(
	'MoviePlayer', 
	FCKLang['MoviePlayerBtn'], 
	FCKLang['MoviePlayerTooltip'],
	null, 
	false, true);
oMoviePlayerItem.IconPath = FCKConfig.PluginsPath + 'movieplayer/filmreel.gif'; 
FCKToolbarItems.RegisterItem('MoviePlayer', oMoviePlayerItem);
