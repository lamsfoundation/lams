/*
 Copyright CodeCogs 2007-2008
 Written by Will Bateman.
	
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
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
// --- FCKEditor Integration ----
var oEditor = window.opener;
var FCKEquation=null;
var eSelected=null;

// Loads the equations from the fckeditor, or create a default equations for an example
function LoadSelected()
{
  // Look for fckeditor
	if(oEditor && typeof(oEditor.FCKEquation)!='undefined')
	{
    FCKEquation = oEditor.FCKEquation;
		if(FCKEquation) 
			eSelected = oEditor.FCKSelection.GetSelectedElement();
	
		if ( eSelected && eSelected.tagName == 'IMG' && eSelected._fckequation )	{
			var comm = unescape( eSelected._fckequation );
			var parts = comm.match( /\\f([\[\$])(.*?)\\f[\]\$]/ );
			
			document.getElementById('latex_formula').value = parts[2];
	
			if(parts[1]=='[')
				document.getElementById('eqstyle2').checked=true;
			else
				document.getElementById('eqstyle1').checked=true;	
			renderEqn(null);	
		}
		else	{
			// Put any default equations in the line below...
			document.getElementById('latex_formula').value = '';
			eSelected == null ;
		}	
	}
}