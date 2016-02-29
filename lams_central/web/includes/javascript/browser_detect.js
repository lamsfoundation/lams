/*
Script Name: Full Featured Javascript Browser/OS detection
Authors: Harald Hope, Tapio Markula, Websites: http://techpatterns.com/
http://www.nic.fi/~tapio1/Teaching/index1.php3
Script Source URI: http://techpatterns.com/downloads/javascript_browser_detection.php
Version 4.2.4
Copyright (C) 29 June 2007

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT 
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

Get the full text of the GPL here: http://www.gnu.org/licenses/gpl.txt

Coding conventions:
http://cvs.sourceforge.net/viewcvs.py/phpbb/phpBB2/docs/codingstandards.htm?rev=1.3
*/

/*************************************************************
Full version, use it if you are pushing css to its functional limits, and/or are using 
specialized javascript.

Remember, always use method or object testing as your first choice, for example, if ( dom ) { statement; };

This browser detection includes all possibilities I think for most browsers.
Let me know if you find an error or a failure to properly detect, or if there
is a relevant browser that has special needs for detection at our tech forum:
http://techpatterns.com/forums/forum-11.html
The main script is separated from the initial netscape 4 detection due to certain bugs in
netscape 4 when it comes to unknown things like d.getElementById. The variable declarations
of course are made first to make sure that all the variables are global through the page, 
otherwise a javascript error will occur because you are trying to use an undeclared variable.

We test for both browser type (ie, op, or moz/netscape > 6) and version number, then place 
the version number into a variable which can be tested for < or > values, such as 
if (moz && nu> 1.1){....statement....;}
This seems quite reliable, especially for Opera and Mozilla, where there is no other
easy way to get the actual version number.

For more in depth discussion of css and browser issues go to:
http://www.nic.fi/~tapio1/Teaching/DynamicMenusb.php#detections
http://www.nic.fi/~tapio1/Teaching/FAQ.php3

***************************************************************/
//initialization, browser, os detection
var d, dom, nu='', brow='', ie, ie4, ie5, ie5x, ie6, ie7, ie8, ie9, ie10;
var ns4, moz, moz_rv_sub, release_date='', moz_brow, moz_brow_nu='', moz_brow_nu_sub='', rv_full=''; 
var mac, win, old, lin, ie5mac, ie5xwin, konq, saf, saf4, op, op4, op5, op6, op7, chrome;

d=document;
n=navigator;
nav=n.appVersion;
nan=n.appName;
nua=n.userAgent;
old=(nav.substring(0,1)<4);
mac=(nav.indexOf('Mac')!=-1);
win=( ( (nav.indexOf('Win')!=-1) || (nav.indexOf('NT')!=-1) ) && !mac)?true:false;
lin=(nua.indexOf('Linux')!=-1);
// begin primary dom/ns4 test
// this is the most important test on the page
if ( !document.layers )
{
	dom = ( d.getElementById ) ? d.getElementById : false;
}
else { 
	dom = false; 
	ns4 = true;// only netscape 4 supports document layers
}
// end main dom/ns4 test

op=(nua.indexOf('Opera')!=-1);
saf=(nua.indexOf('Safari')!=-1);
konq=(!saf && (nua.indexOf('Konqueror')!=-1) ) ? true : false;
moz=( (!saf && !konq ) && ( nua.indexOf('Gecko')!=-1 ) ) ? true : false;
chrome=(nua.indexOf('Chrome')!=-1);
ie=((nua.indexOf('MSIE')!=-1)&&!op);
if (op)
{
	str_pos=nua.indexOf('Opera');
	nu=nua.substr((str_pos+6),4);
	brow = 'Opera';
}
else if (saf)
{
	str_pos=nua.indexOf('Safari');
	nu=nua.substr((str_pos+7),5);
	// checks for webkit 528 or higher introduced
	// in safari4
	saf4=(saf&&(nu.substring(0,3)>=528));
	brow = 'Safari';
}
else if (konq)
{
	str_pos=nua.indexOf('Konqueror');
	nu=nua.substr((str_pos+10),3);
	brow = 'Konqueror';
}
// this part is complicated a bit, don't mess with it unless you understand regular expressions
// note, for most comparisons that are practical, compare the 3 digit rv nubmer, that is the output
// placed into 'nu'.
else if (moz)
{
	// regular expression pattern that will be used to extract main version/rv numbers
	pattern = /[(); \n]/;
	// moz type array, add to this if you need to
	moz_types = new Array( 'Firebird', 'Phoenix', 'Firefox', 'Iceweasel', 'Galeon', 'K-Meleon', 'Camino', 'Epiphany', 'Netscape6', 'Netscape', 'MultiZilla', 'Gecko Debian', 'rv' );
	rv_pos = nua.indexOf( 'rv' );// find 'rv' position in nua string
	rv_full = nua.substr( rv_pos + 3, 6 );// cut out maximum size it can be, eg: 1.8a2, 1.0.0 etc
	// search for occurance of any of characters in pattern, if found get position of that character
	rv_slice = ( rv_full.search( pattern ) != -1 ) ? rv_full.search( pattern ) : '';
	//check to make sure there was a result, if not do  nothing
	// otherwise slice out the part that you want if there is a slice position
	( rv_slice ) ? rv_full = rv_full.substr( 0, rv_slice ) : '';
	// this is the working id number, 3 digits, you'd use this for 
	// number comparison, like if nu >= 1.3 do something
	nu = rv_full.substr( 0, 3 );
	for (i=0; i < moz_types.length; i++)
	{
		if ( nua.indexOf( moz_types[i]) !=-1 )
		{
			moz_brow = moz_types[i];
			break;
		}
	}
	if ( moz_brow )// if it was found in the array
	{
		str_pos=nua.indexOf(moz_brow);// extract string position
		moz_brow_nu = nua.substr( (str_pos + moz_brow.length + 1 ) ,3);// slice out working number, 3 digit
		// if you got it, use it, else use nu
		moz_brow_nu = ( isNaN( moz_brow_nu ) ) ? moz_brow_nu = nu: moz_brow_nu;
		moz_brow_nu_sub = nua.substr( (str_pos + moz_brow.length + 1 ), 8);
		// this makes sure that it's only the id number
		sub_nu_slice = ( moz_brow_nu_sub.search( pattern ) != -1 ) ? moz_brow_nu_sub.search( pattern ) : '';
		//check to make sure there was a result, if not do  nothing
		( sub_nu_slice ) ? moz_brow_nu_sub = moz_brow_nu_sub.substr( 0, sub_nu_slice ) : '';
	}
	if ( moz_brow == 'Netscape6' )
	{
		moz_brow = 'Netscape';
	}
	else if ( moz_brow == 'rv' || moz_brow == '' )// default value if no other gecko name fit
	{
		moz_brow = 'Mozilla';
	} 
	if ( !moz_brow_nu )// use rv number if nothing else is available
	{
		moz_brow_nu = nu;
		moz_brow_nu_sub = nu;
	}
	if (n.productSub)
	{
		release_date = n.productSub;
	}
}
else if (ie)
{
	str_pos=nua.indexOf('MSIE');
	nu=nua.substr((str_pos+5),3);
	brow = 'Microsoft Internet Explorer';
}
// default to navigator app name
else 
{
	brow = nan;
}
op5=(op&&(nu.substring(0,1)==5));
op6=(op&&(nu.substring(0,1)==6));
op7=(op&&(nu.substring(0,1)==7));
op8=(op&&(nu.substring(0,1)==8));
op9=(op&&(nu.substring(0,1)==9));
ie4=(ie&&!dom);
ie5=(ie&&(nu.substring(0,1)==5));
ie6=(ie&&(nu.substring(0,1)==6));
ie7=(ie&&(nu.substring(0,1)==7));
ie8=(ie&&(nu.substring(0,1)==8));
ie9=(ie&&(nu.substring(0,1)==9));
ie10=(ie&&(nu.substring(0,2)==10));
ie11=(ie&&(nu.substring(0,2)==11));
// default to get number from navigator app version.
if(!nu) 
{
	nu = nav.substring(0,1);
}
/*ie5x tests only for functionavlity. dom or ie5x would be default settings. 
Opera will register true in this test if set to identify as IE 5*/
ie5x=(d.all&&dom);
ie5mac=(mac&&ie5);
ie5xwin=(win&&ie5x);

/********************************************************
here is a sample use of the browser detector, it would load a browser specific stylesheet
for certain unsupported or improperly supported mac ie 5 css styles. The depth variable
is used so that the javascript library file can be used from anywhere in the website, you simply
insert the depth of the file like this, 
...
 <head>
 <title>Browser information Page</title>

 <meta http-equiv = "Content-Type" content = "text/html; charset = iso-8859-1" />
 <link rel = "stylesheet" type = "text/css" href = "css/main.css" />
 <script type = "text/javascript" src = "/js/browser_detection.js"> </script>
 <script type = "text/javascript>browser_css( ); </script>
 </head>

in the head of the web page after the js file is loaded.
Or if you are always referring your site to the root, you wouldn't need that
 and could delete the depth variable and just use the absolute path to the root.

function browser_css( ) {
	d = document;
	if ( ie5mac ) {
		d.write('<link rel = "stylesheet" type = "text\/css" href = "/css/ie5mac.css" />');
	}
	else if ( document.layers ){
		d.write('<link rel = "stylesheet" type = "text\/css" href = "/css/ns4x.css" />');
	}
	else if ( ie4 ){
		d.write('<link rel = "stylesheet" type = "text\/css" href = "/css/ie4.css" />');
	}
	else if ( moz && ( nu < 1 ) ){
		d.write('<link rel = "stylesheet" type = "text\/css" href = "/css/moz_pre1-0.css" />');
	}
	else {
		d.write('<link rel = "stylesheet" type = "text\/css" href = "/css/moz5.css" />');
	}
}
********************************************************/
