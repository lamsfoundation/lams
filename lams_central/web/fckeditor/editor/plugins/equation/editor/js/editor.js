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
var changed=false;
var orgtxt='';
var EQUATION_ENGINE='http://codecogs.izyba.com';

// Clears the main editor window
function cleartext() { 
 var id=document.getElementById('latex_formula'); id.value = ""; id.focus(); 
 changed=false;
 document.getElementById('copybutton').className='greybutton'; 
 document.getElementById('renderbutton').className='greybutton';

}

function textchanged() {
	var txt=getEquationStr();
	if(txt!=orgtxt)
	{
		orgtxt=txt;
	  document.getElementById('copybutton').className='lightbluebutton'; 
	  document.getElementById('renderbutton').className='bluebutton';
	  changed=true;
	}
}

function formatchanged()
{
	var action=false;
	var format=document.getElementById('format');
	if(format) { var type=format.value;
		switch(type) {
			case 'gif' : action=false; break;
			case 'png' : action=false; break;
			case 'pdf' : action=true; break;
			case 'swf' : action=true; break;
		}
	}
	document.getElementById('dpi').disabled=action;
	document.getElementById('dpi').readonly=action;
	changed=true;
	renderEqn();
}

// Tries to inserts text at the cursor position of text area
//  wind = document                <- when inserting text into the current equation editor box  
//  wind = window.opener.document  <- when inserting text into a parent window box
function addText(wind, textbox, txt) 
{
	myField = wind.getElementById(textbox);
  // IE 
  if (wind.selection) {
    myField.focus();
    sel = wind.selection.createRange();
    sel.text = txt;
  }
  // MOZILLA
  else 
  {
		var scrolly=myField.scrollTop;
	  if (myField.selectionStart || myField.selectionStart == '0') 
    {
      var startPos = myField.selectionStart;
      var endPos = myField.selectionEnd;
			var cursorPos = startPos + txt.length;
      myField.value = myField.value.substring(0, startPos) + txt 
					+ myField.value.substring(endPos, myField.value.length);
			pos = txt.length + endPos - startPos;

			myField.selectionStart = cursorPos;
			myField.selectionEnd = cursorPos;
	
			myField.focus();
			myField.setSelectionRange(startPos + pos,startPos + pos);	
    } 
    else myField.value += txt;
			
	  myField.scrollTop=scrolly;
  }
}

function insertText( txt, pos, inspos )
{
	var insert_pos=(inspos==null)?pos:inspos;
	textchanged()
	
	// pos = optional parameter defining where in inserted text to put the caret
	// if undefined put at end of inserted text
	// if pos=1000 then using style options and move to just before final }
	// startPos = final position of caret in complete text
	if (pos==1000) {pos=txt.length-1};
	if (pos==undefined) { txt+=' '; pos=txt.length;}; // always insert a space after
	
	// my textarea is called latex_formula
	myField = document.getElementById('latex_formula');
	if (document.selection) {
		// IE
		myField.focus();
		var sel = document.selection.createRange();
		// find current caret position
		var i = myField.value.length+1; 
		theCaret = sel.duplicate(); 
		while (theCaret.parentElement()==myField 
		&& theCaret.move("character",1)==1) --i; 
	
		// take account of line feeds
		var startPos = i - myField.value.split('\n').length + 1 ; 
	
		if ((txt.substring(1,5) == "left" || txt.substring(insert_pos-1,insert_pos)=='{') && sel.text.length)	{ 
			// allow highlighted text to be bracketed
			if(txt.substring(1,5) == "left") ins_point=7;
			else ins_point=insert_pos;
				
			if(inspos==null) pos = txt.length + sel.text.length + 1;
			else if(inspos<pos) pos+=sel.text.length;
			
			sel.text = txt.substring(0,ins_point) + sel.text + txt.substr(ins_point);	     
		} else { sel.text = txt; }
		// put caret in correct position to start editing
		var range = myField.createTextRange();
		range.collapse(true);
		range.moveEnd('character', startPos + pos);
		range.moveStart('character', startPos + pos);
		range.select();
	}
	else
	{
		// MOZILLA
		if (myField.selectionStart || myField.selectionStart == '0')	{
			var startPos = myField.selectionStart;
			var endPos = myField.selectionEnd;
			var cursorPos = startPos + txt.length;
			if ((txt.substring(1,5) == "left" || txt.substr(insert_pos-1,1)=='{') && endPos > startPos)	{ 
				// allow highlighted text to be bracketed
				
				if(txt.substring(1,5) == "left") ins_point=7;
				else ins_point=insert_pos;
				
				if(inspos==null) pos = txt.length + endPos - startPos + 1;
				else if(inspos<pos) pos+=endPos - startPos;
				
				txt = txt.substring(0,ins_point) + myField.value.substring(startPos, endPos) + txt.substr(ins_point);			
			}
			myField.value = myField.value.substring(0, startPos) + txt + myField.value.substring(endPos, myField.value.length);
		
			myField.selectionStart = cursorPos;
			myField.selectionEnd = cursorPos;
					
			// put caret in correct position to start editing
			myField.focus();
			myField.setSelectionRange(startPos + pos,startPos + pos);	
		}
		else myField.value += txt;
	}
	myField.focus();
}

/* ----------- Handle rendering example equation --------------------------- */

// Returns the complete string that describes this particular equation with font sizes etc.
function getEquationStr()
{
	var val=document.getElementById('latex_formula').value;	
	val=val.replace(/^\s+|\s+$/g,"");
	val=val.replace(/\s+/g," ");

			
	var size = document.getElementById('fontsize');
	if(size) { var txt=size.options[size.selectedIndex].value;
		if(txt!='')	val=txt+' '+val; }
	
	if(document.getElementById('compressed').checked) val='\\inline '+val; 
	
	if(document.getElementById('dpi')) { var dpi=document.getElementById('dpi').value;
		if(dpi!='100') val='\\'+dpi+'dpi '+val; }
		
	if(document.getElementById('bg')) { var bg=document.getElementById('bg').value; if(bg!='transparent') val='\\bg_'+bg+' '+val; }
	return val;
}



/* Turns off the wait message, once equation is loaded */
var initmessage=true;
function processEquationChange() 
{
	if(initmessage) initmessage=false;
	else { var div = document.getElementById('equationcomment'); div.innerHTML = ""; }
}

// Triggers the rendering of the equations within the iframe
function renderEqn(callback)
{
	if(!changed) return;
	
	/* First check we have a matching set of brackets */
	var val=document.getElementById('latex_formula').value;	
	val=val.replace(/^\s+|\s+$/g,"");
	if(val.length==0) return;
	var bracket=0;
  for(i=0;i<val.length;i++) 
	{ 
	  switch(val[i])
		{
			case '{': bracket++; break;
		  case '}': bracket--; break;
		}
	}

	if(bracket==0)
	{		
		/* Set render button to grey, since we're now rendering.. */
		document.getElementById('renderbutton').className='greybutton';
	
		var val=document.getElementById('latex_formula').value;
		
		/* Add to history */
		var sel=document.getElementById('history');
		var j=sel.length;
		sel.length=j+1;
		sel.options[j].text=val.substr(0,15);
		sel.options[j].value=val;
		sel.options[j].title=val;
		
		var img = document.getElementById('equationview');
		val=getEquationStr();
		val=val.replace(/(\r\n|[\r\n])/g, " ");	
//		alert(val);
		sval = val.replace(/"/g,'\\"');

		// Figure out the format
		if(document.getElementById('format'))
		{
			var format_index=document.getElementById('format').selectedIndex;
			var format=document.getElementById('format').options[format_index].value;
		} else { var format_index=0; var format='gif'; }
		
		switch(format_index)
		{
			case 0 : // gif
			case 1 : // png
			{	
				var div = document.getElementById('equationcomment');
				div.innerHTML = "Rendering Equation <img src=\"http://www.codecogs.com/components/equationeditor/images/wait.gif\" width=\"13\" height =\"13\"/>";	
				img.onload = processEquationChange;
				img.src=EQUATION_ENGINE+'/'+format+'.latex?' + val;
				changed=false;
			} break;
			case 2 : // pdf
			{
				img.onload = '';
				img.src='http://www.codecogs.com/components/equationeditor/images/pdf.jpg';
				document.getElementById('download').src='http://www.codecogs.com/pdf.latex?'+val;
				var div = document.getElementById('equationcomment');
				div.innerHTML = '<br/><a target="download" href="http://www.codecogs.com/pdf.latex?'+sval+'"><strong>Click here if the PDF does not downloading.</strong></a>';	
			} break;
			case 3 : // swf
			{
				img.onload ='';
				img.src='http://www.codecogs.com/components/equationeditor/images/spacer.gif';
				var div = document.getElementById('equationcomment');
				AC_FL_RunContent('codebase', 'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0','width', '600','height', '100','src', ('http://www.codecogs.com/swf.latex?'+val),'quality','high','pluginspage','http://www.macromedia.com/go/getflashplayer','align','top','scale','showall','wmode','window','devicefont','false','bgcolor','#ffffff','menu','true','allowFullScreen','true',
'movie', (EQUATION_ENGINE+'/swf.latex?'+val) ); 
			  changed=false;
			} break;
		}
	} 
	else 
	{
		var div = document.getElementById('equationcomment');
		if(bracket<0)	div.innerHTML = "<br/><span class=\"orange\">You have more <strong>closed '}' brackets</strong> than open '{' brackets</span>";
		else div.innerHTML = "<br/><span class=\"orange\">You have more <strong>open '{' brackets</strong> than closed '}' brackets</span>";
	}
}


/* We don't want to render with every keystroke, so we delay this part 
This function will be called with each keystroke
 - n is a tens of second unit
*/
var auton=0;
function renderCountdown()
{
	if(auton>0) 
	{ auton--; setTimeout('renderCountdown()', 100); }
	else renderEqn(null);
}


function autorenderEqn(n) 
{
	if(auton>0 && n>0) auton=n;
	else { auton=n; renderCountdown(); }
}

/* The following code adds a favorite to the favorite drop down. 
To achieve this, we must update the users cookies */

function addfavorite(dropdown)
{
	text=document.getElementById('latex_formula').value;
	var sel=document.getElementById(dropdown);
	var num=sel.selectedIndex
  if(num==0 || !confirm("Click Yes to replace the current equation or 'Cancel' to add a new equation"))
	{
		var name = prompt('Please enter a short name for this equation:', '')
		if(name==null || name=='') name=text.substr(0,15); 
		/* Add to favorite */
		var j=sel.length;
		sel.length=j+1;
		sel.options[j].text=name;
		sel.options[j].value=text;
		sel.options[j].title=text;
		/* Make sure cookie name is unique to website */
		document.cookie = "eq_"+escape(name.replace(/\+/g,"&plus;").replace(/\s/g,"&space;"))+"="+escape(text.replace(/\+/g,"&plus;"))+"; path=/";
	}
	else
	{
	  sel.options[num].value=text;
	  sel.options[num].title=text;
	  /* Make sure cookie name is unique to website */
	  document.cookie = "eq_"+escape(name.replace(/\+/g,"&plus;").replace(/\s/g,"&space;"))+"="+escape(text.replace(/\+/g,"&plus;"))+"; path=/";
	}
}

function deletefavorite(dropdown)
{
	var sel=document.getElementById(dropdown);
	var num=sel.selectedIndex
	if(num>0)
	{
		name=sel.options[num].text;
		
		var mydate = new Date(); 
		mydate.setTime(mydate.getTime() - 1); 
		document.cookie = "eq_"+escape(name.replace(/\+/g,"&plus;").replace(/\s/g,"&space;"))+"=; expires="+mydate.toGMTString()+"; path=/";
		sel.options[num]=null;
	}
}


/* Help to generate a Matrix */
// generate a set of aligned equations - thornahawk
// isNumbered: switches between numbered and not numbered equations
function makeEquationsMatrix(type, isNumbered, isConditional)
{
	if (isNumbered==undefined) isNumbered=false;
	if (isConditional==undefined) isNumbered=false;

  var eqns="\\begin{"+type+((isNumbered)?"":"*")+"}";
	var eqi="\n &"+((isNumbered)?" ":"= ")+((isConditional)?"\\text{ if } x=  ":"");
	var eqEnd="\n\\end{"+type+((isNumbered)?"":"*")+"}";
	var i=0;

	var dim = prompt('Enter the number of lines:', '');

	if (dim != '' && dim != null)	{
		n=parseInt(dim);
		if (!isNaN(n)) {
			for (i=1;i<=n-1;i++) eqns=eqns+(eqi+"\\\\ ");
			eqns=(eqns+eqi)+eqEnd;
			insertText(eqns,type.length+((isNumbered)?0:1)+9);
		}
	}
}

// generate an array of specified dimensions - thornahawk
// type: sets the type of array, e.g. pmatrix
function makeArrayMatrix( type, start, end )
{
	var matr=start+'\\begin{'+type+'matrix}';
	var row="\n";
	var mend="\n\\end{"+type+"matrix}"+end;
	var i=0;
	var dim = prompt('Enter the array dimensions separated by a comma (e.g. "2,3" for 2 rows and 3 columns):', '')

	if (dim!='' && dim!=null)	
	{
		dim=dim.split(',');
		m=parseInt(dim[0]);
		n=parseInt(dim[1]);
		
		if (!isNaN(m) && !isNaN(n)) 
		{
			for (i=2;i<=n;i++) row=row+' & ';
			for (i=1;i<=m-1;i++) matr=matr+row+'\\\\ ';
		
			matr=matr+row+mend;
			insertText(matr,type.length+start.length+15);
		}
	}
}

function hover(elm, e)
{
	div=document.getElementById('hover');
	div.innerHTML='<img src="http://www.codecogs.com/gif.latex?\\200dpi '+elm.title+'"/>';
		
	if(document.all) { div.style.top = (event.clientY-10)+'px'; div.style.left = (event.clientX+20)+'px';	}
	else { if(!e) var e=window.event; div.style.top = (e.pageY-10)+'px'; div.style.left = (e.pageX+20)+'px'; }
  div.style.display='block';
	elm.onmouseout= function() { document.getElementById('hover').style.display='none';}
}


/* This script and many more are available free online at
The JavaScript Source!! http://javascript.internet.com
Created by: Turnea Iulian :: http://www.eurografic.ro */
function iObject() { this.i; return this; }

var myObject=new iObject();
myObject.i=0;
var myObject2=new iObject();
myObject2.i=0;
store_text=new Array();

//store_text[0] store initial textarea value
store_text[0]="";

function countclik(tag) {
  var x=tag.value;
	if(myObject.i==0 || store_text[myObject.i]!=x)
	{
    myObject.i++;
    var y=myObject.i;
    store_text[y]=x;
	}
	myObject2.i=0;
	document.getElementById('redobutton').src="http://www.codecogs.com/images/buttons/redo-x.gif";
	document.getElementById('undobutton').src="http://www.codecogs.com/images/buttons/undo.gif";
}

function undo(box) {
	tag=document.getElementById(box);
  if ((myObject2.i)<(myObject.i)) {
    myObject2.i++;
		if(myObject2.i==myObject.i) {
		document.getElementById('undobutton').src="http://www.codecogs.com/images/buttons/undo-x.gif"; }
		document.getElementById('redobutton').src="http://www.codecogs.com/images/buttons/redo.gif";
  } else {
    alert("Finish Undo Action");
  }
  var z=store_text.length;
  z=z-myObject2.i;
  if (store_text[z]) { tag.value=store_text[z]; } 
	else { tag.value=store_text[0]; }
	tag.focus();
}

function redo(box) {
	tag=document.getElementById(box);
  if((myObject2.i)>1) {
    myObject2.i--;
		if(myObject2.i==1){
		document.getElementById('redobutton').src="http://www.codecogs.com/images/buttons/redo-x.gif"; }
		document.getElementById('undobutton').src="http://www.codecogs.com/images/buttons/undo.gif";
  } else {
    alert("Finish Redo Action");
  }
  var z=store_text.length;
  z=z-myObject2.i;
  if (store_text[z]) { tag.value=store_text[z]; } 
	else { tag.value=store_text[0]; }
	tag.focus();
}


function updateOpener(target, type)
{
	if(target!='') 
	{ // *** Traditional mode with plane HTML text box ***
	  var text;
		if(type=='phpBB') 
		{ // Create LaTeX string for insertion into phpBB forum
			text=getEquationStr();
			text = '[tex]' + text + '[/tex]\n';
		}
		else if(type=='html')
		{ // Create <img> tag for insertion into html
			text=getEquationStr();
			text = '<img src="'+EQUATION_ENGINE+'/gif.latex?'+escape(text)+'" />';
		} 
		else 
		{ // Create LaTeX string for insertion into CodeCogs of DOxygen C/C++ markup language
			// Note size and 
			text=document.getElementById('latex_formula').value;
			var size = document.getElementById('fontsize');
			if(size && size.selectedIndex!=2)
				text=size.options[size.selectedIndex].value+' '+text;
				
			if(document.getElementById('inline').checked)
			{
				if(!document.getElementById('compressed').checked) text='\\displaystyle '+text;
				text = '$' + text + '$ '; 
			}
			else
			{
				if(document.getElementById('compressed').checked) text='\\inline '+text;
				text = '\\[' + text + '\\]\n'; 
			}
		}
		addText(window.opener.document,target,text);
	}
	else 
	{ // *** Advanced mode with FCKEditor *** 
		var text=document.getElementById('latex_formula').value;
		var size = document.getElementById('fontsize');
		if(size && size.selectedIndex!=2)
			text=size.options[size.selectedIndex].value+' '+text;
	
		if (text.length == 0) {
			alert(FCKLang.EquationErrNoEqn) ;
			return false ;
		}	
		
		if(document.getElementById('inline').checked) 
		{
			if(!document.getElementById('compressed').checked) 	
				text='\\displaystyle '+text;
			text = '\\f$' + text + '\\f$ ';
		}
		else
		{
			if(document.getElementById('compressed').checked) 	
				text='\\inline '+text;
			text = '\\f[' + text + '\\f]\n'; 
		}
		
		if ( eSelected && eSelected._fckequation == text )
			return true ;

		// inserted this so it puts the whole thing in one line (ernieg)
		text=text.replace(/(\r\n|[\r\n])/g, " ");
		FCKEquation.Add(text);
	}	
	return true ;
}