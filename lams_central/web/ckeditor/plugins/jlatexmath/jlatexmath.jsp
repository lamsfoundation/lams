<!DOCTYPE html>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-core" prefix="c"%>

<c:set var="SERVER_URL_CONTEXT_PATH"><%=Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH)%></c:set>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta content="noindex, nofollow" name="robots">
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="buttons.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="buttons.js"></script>
	<script type="text/javascript">
	var CKEDITOR = window.parent.CKEDITOR;
	var thisDialog = CKEDITOR.dialog.getCurrent();
	var CK = thisDialog.getParentEditor();
	var SERVER_URL_CONTEXT_PATH = '/${SERVER_URL_CONTEXT_PATH}/servlet/jlatexmath';

	/** Initial setup */
	window.onload = function() {

		// Translate dialog box
		document.getElementById("JlatexmathLatexFormula").innerHTML = CK.lang.jlatexmath.JlatexmathLatexFormula;
		document.getElementById("JlatexmathFontSize").innerHTML = CK.lang.jlatexmath.JlatexmathFontSize;
		
		// remove our previously registered listeners and reregister a new one
		var okButton = thisDialog.getButton('ok');
		var newListeners = [];
		var oldListeners = okButton._.events.click.listeners;
		for (var i = 0; i < okButton._.events.click.listeners.length; i++){
			if (okButton._.events.click.listeners[i].priority != 1) {
				newListeners.push(okButton._.events.click.listeners[i]);
			}
		}
		okButton._.events.click.listeners = newListeners;
		okButton.on('click', triggerOK, null, null, 1);
		
		// Load selected latex formula
		loadLatexFormulaSelection();
	}
	
	/** Changing preview image on the fly */
	function formulaTextareaChanged() {

		var latexFormula = document.getElementById('latex-formula').value;
		if(latexFormula.length == 0) {
			return;	
		}

		var fontSize = document.getElementById('font-size').value;
		document.getElementById('preview').src = SERVER_URL_CONTEXT_PATH + '?formula=' + encodeURIComponent(latexFormula) + "&fontSize=" + fontSize;
	}
	
	function getParameterByName(string, name) {
	    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
	        results = regex.exec(string);
	    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}

	/** Load FCKEditor selection */
	function loadLatexFormulaSelection() {

		var oSel = CK.getSelection().getSelectedElement();
		
		if (oSel != null && oSel.is('img')) {
			
			var imageSrc = oSel.getAttribute('src');
			if (imageSrc != 'undefined') {
				
				var formula = getParameterByName(imageSrc, 'formula');
				var fontSize = getParameterByName(imageSrc, 'fontSize');

				// Read current settings (existing movie)
				document.getElementById('latex-formula').value = formula;
				document.getElementById('font-size').value = fontSize;
			}

		}
	}

	/** Start processing */
	function triggerOK(ev) {

		if(document.getElementById('latex-formula').value.length == 0) {

			document.getElementById('latex-formula').focus();
			alert(CK.lang.jlatexmath.JlatexmathNoFormula) ;
			ev.stop();
			return;
		}

		CK.fire('saveSnapshot', null, CK);
		
		var latexFormula = document.getElementById('latex-formula').value;
		var fontSize = document.getElementById('font-size').value;
		
		// Flash video (FLV)
		var imgSrc = SERVER_URL_CONTEXT_PATH + '?formula=' + encodeURIComponent(latexFormula) + "&fontSize=" + fontSize;
		var resultHtml = '<img data-jlatexmath="true" src="' + imgSrc + '" ';
		resultHtml    += '        alt="' + latexFormula + '" ';
		resultHtml    += '>';

		CK.insertHtml(resultHtml);

		return true;
	}
	</script>
	
</head>

<body style="background: #fff;">
	<%@ include file="buttons.jsp" %>
	
	<table cellspacing="5" cellpadding="0" width="100%" border="0">
	   <tr>
		  <td>
		      <span id="JlatexmathLatexFormula"></span>
		  </td>
		  <td style="text-align: right;">
	  		<span id="JlatexmathFontSize"></span>
          	<select id="font-size" onChange="formulaTextareaChanged()">
          		<option>8</option>
          		<option>9</option>
          		<option>10</option>
          		<option>11</option>
          		<option>12</option>
          		<option>14</option>
          		<option>16</option>
          		<option>18</option>
          		<option selected="selected">20</option>
          		<option>22</option>
          		<option>24</option>
          		<option>26</option>
          		<option>28</option>
          		<option>36</option>
          		<option>48</option>
          		<option>72</option>
          	</select>
	      </td>
	  </tr>
	  
	  <tr>
          <td style="vertical-align: top; width: 75%" colspan="2">
			  <div class="form-group">
          	<textarea id="latex-formula" class="form-control" style="width: 100%" rows="4" cols="50" 
          		onkeyup="formulaTextareaChanged()" 
          		onChange="formulaTextareaChanged()"
          		onpaste="formulaTextareaChanged()"
          		oncut="formulaTextareaChanged()"></textarea>
			  </div>
		  </td>
	   </tr>

		<tr>
			<td>
	       	   <img id="preview" src="" alt="" colspan="2">
			</td>
		</tr>
	</table>

</body>
</html>
