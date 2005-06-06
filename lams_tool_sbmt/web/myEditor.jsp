<%@ taglib uri="fck-editor" prefix="FCK" %>
<!--
 * FCKeditor - The text editor for internet
 * Copyright (C) 2003-2005 Frederico Caldeira Knabben
 * 
 * Licensed under the terms of the GNU Lesser General Public License:
 * 		http://www.opensource.org/licenses/lgpl-license.php
 * 
 * For further information visit:
 * 		http://www.fckeditor.net/
 * 
 * File Name: sample02.jsp
 * 	FCKeditor sample file 2.
 * 
 * Version:  2.1
 * Modified: 2005-03-29 21:30:00
 * 
 * File Authors:
 * 		Simone Chiaretta (simo@users.sourceforge.net)
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<html>
	<head>
		<title>FCKeditor - Sample</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="robots" content="noindex, nofollow">
		<link href="../sample.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../../fckeditor.js"></script>
	</head>
	<body>
		<h1>FCKeditor - JSP - Sample 7</h1>
		In this sample the user can edit the complete page contents and header (from 
		&lt;HTML&gt; to &lt;/HTML&gt;).
		<hr>
		<form action="/../sampleposteddata.jsp" method="post" target="_blank">
			<FCK:editor id="EditorDefault" basePath="../../fckEditor/"
				fullPage="true"
				imageBrowserURL="../../fckEditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=connectors/jsp/connector"
				linkBrowserURL="../../fckEditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector">
				<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><head><title>Full Page Test</title><meta content="text/html; charset=utf-8" http-equiv="Content-Type"/></head><body>This is some <strong>sample text</strong>. You are using <a href="http://www.fckeditor.net/">FCKeditor</a>.</body></html>
			</FCK:editor>
			<br>
			<input type="submit" value="Submit">
		</form>
	</body>
</html>
