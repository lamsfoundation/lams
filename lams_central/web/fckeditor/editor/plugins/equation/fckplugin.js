/* ------------------
 Written by Will Bateman
 GNU General Public License Agreement
 Copyright (C) 2004-2008 CodeCogs, Zyba Ltd, Broadwood, Holford, TA5 1DU, England.
 This program is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.
 You must retain a copy of this licence in all copies.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY
 WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 PARTICULAR PURPOSE. See the GNU General Public License for more details.

 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>
 ----------------- */

var latexpath='/latexrender/pictures/';

// Latex equation editor

var InsertEquationCommand=function(){};

InsertEquationCommand.prototype.Execute=function(){ }

InsertEquationCommand.GetState=function() {
        return FCK_TRISTATE_OFF; //we dont want the button to be toggled
}

var popupEqnwin = null;
InsertEquationCommand.Execute=function() 
{
  //open a popup window when the button is clicked
	if (popupEqnwin==null || popupEqnwin.closed || !popupEqnwin.location) 
	{
    popupEqnwin= window.open( FCKConfig.PluginsPath + 'equation/editor/editor.html', 'Equation', 'width=700,height=450,status=1,scrollbars=no,resizable=1,location=no,toolbar=no');
	}
  popupEqnwin.focus();
}

// Register the related command.
FCKCommands.RegisterCommand( 'Equation', InsertEquationCommand ) ;

// Create the "Placeholder" toolbar button.
var oEquationItem = new FCKToolbarButton( 'Equation', FCKLang['EquationBtn'] ) ;
oEquationItem.IconPath =  FCKConfig.PluginsPath + 'equation/equation.gif' ;

FCKToolbarItems.RegisterItem( 'Equation', oEquationItem ) ;


// The object used for all Placeholder operations.
var FCKEquation= new Object() ;

// Add a new placeholder at the actual selection.
// 
FCKEquation.Add = function( name )
{
//	var oImage = FCK.CreateElement( 'img' ) ;
	var oImage = FCK.InsertElement( 'img' ) ;
	this.SetupImage( oImage, name ) ;
// Then select the new placeholder
	FCKSelection.SelectNode(oImage);
}

FCKEquation.SetupImage = function( image, name )
{
	var sName = name.match( /\\f([\[\$])(.*?)\\f[\]\$]/ );
	var eq='';
	
	eq = escape(sName[2].replace(/\+/g,'&plus;'));
	if(sName[1]=='$') 
	{	eq='\\inline&space;'+eq; }
	else
	{
	  image.style.display='block';
		image.style.margin='0 auto';
		image.style.textalign='center';
	}
	
	image.src = 'http://www.codecogs.com/png.latex?'+eq;
	image.style.backgroundColor = '#ffffdd' ;
	image.align='absmiddle';
 	image.alt=name;
	image.title=name;

	if ( FCKBrowserInfo.IsGecko )
		image.style.cursor = 'default' ;

		image._fckequation = name;
		image.contentEditable = false ;

		// To avoid it to be resized.
		image.onresizestart = function()
	{
		FCK.EditorWindow.event.returnValue = false ;
		return false ;
	}
}

// On Gecko we must do this trick so the user select all the IMG when clicking on it.
FCKEquation._SetupClickListener = function()
{
	FCKEquation._ClickListener = function( e )
	{
		if ( e.target.tagName == 'IMG' && e.target._fckequation )
			FCKSelection.SelectNode( e.target ) ;
	}

	FCK.EditorDocument.addEventListener( 'click', FCKEquation._ClickListener, true ) ;
}

// Open the Placeholder dialog on double click.
FCKEquation.OnDoubleClick = function( image )
{
	if ( image.tagName == 'IMG' && image._fckequation )
		FCKCommands.GetCommand( 'Equation' ).Execute() ;
}

FCK.RegisterDoubleClickHandler( FCKEquation.OnDoubleClick, 'IMG' ) ;

// Check if a Placholder name is already in use.
FCKEquation.Exist = function( name )
{
	var aImages = FCK.EditorDocument.getElementsByTagName( 'IMG' )

	for ( var i = 0 ; i < aImages.length ; i++ )
	{
		if ( aIMGs[i]._fckequation == name )
			return true ;
	}
}

if ( FCKBrowserInfo.IsIE )
{
	FCKEquation.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;
		
		var aEquations = FCK.EditorDocument.body.innerText.match( /\\f[\[\$].*?\\f[\]\$]/g ) ;
		if ( !aEquations )
			return ;

		var oRange = FCK.EditorDocument.body.createTextRange() ;

		for ( var i = 0 ; i < aEquations.length ; i++ )
		{
			if ( oRange.findText( aEquations[i] ) )
			{
				var sName = aEquations[i].match( /\\f([\[\$])(.*?)\\f[\]\$]/ );
				
				var eq = escape(sName[2].replace(/\+/g,'&plus;'));
				var extrastyle='';
      	if(sName[1]=='$') 
				  eq='\\inline&space;'+eq;
			//	else 
			//	  extrastyle=' display:block; margin:0 auto;'; 
					
        imgsrc = '/eq.latex?'+eq;
				
				oRange.pasteHTML( '<img src="'+imgsrc+'" style="background-color:#ffffdd; display:block; margin:0 auto; text-align:center;" align="absmiddle" contenteditable="false" _fckequation="'+ escape(aEquations[i]) +'" />') ;
			}
		}
	}
}
else
{
	FCKEquation.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;
			
		var oInteractor = FCK.EditorDocument.createTreeWalker( FCK.EditorDocument.body, NodeFilter.SHOW_TEXT, FCKEquation._AcceptNode, true ) ;

		var	aNodes = new Array() ;

		while ( (oNode = oInteractor.nextNode()) )
		{
			aNodes[ aNodes.length ] = oNode ;
		}

		for ( var n = 0 ; n < aNodes.length ; n++ )
		{
			var aPieces = aNodes[n].nodeValue.split( /(\\f[\[\$].*?\\f[\]\$])/g ) ;

			for ( var i = 0 ; i < aPieces.length ; i++ )
			{
				if ( aPieces[i].length > 0 )
				{
					if ( aPieces[i].indexOf( '\\f[' ) == 0 || aPieces[i].indexOf( '\\f$' ) == 0)
					{
						var sName = aPieces[i].match( /(\\f[\[\$].*?\\f[\]\$])/ )[1] ;

						var oImage = FCK.EditorDocument.createElement( 'img' ) ;
						FCKEquation.SetupImage( oImage, sName ) ;

						aNodes[n].parentNode.insertBefore( oImage, aNodes[n] ) ;
					}
					else
						aNodes[n].parentNode.insertBefore( FCK.EditorDocument.createTextNode( aPieces[i] ) , aNodes[n] ) ;
				}
			}

			aNodes[n].parentNode.removeChild( aNodes[n] ) ;
		}
		
		FCKEquation._SetupClickListener() ;
	}

	FCKEquation._AcceptNode = function( node )
	{
		if ( /\\f[\[\$].*?\\f[\]\$]/.test( node.nodeValue ) )
			return NodeFilter.FILTER_ACCEPT ;
		else
			return NodeFilter.FILTER_SKIP ;
	}
}

FCK.Events.AttachEvent( 'OnAfterSetHTML', FCKEquation.Redraw ) ;

// Must include the seperate plugin called TagProcessors; that contains
/*

	if ( htmlNode._fckequation )
	{
		alert('Bye Bye Equation' + htmlNode._fckeauation);
		node = FCKXHtml.XML.createTextNode( unescape(htmlNode._fckequation) ) ;
	}

*/