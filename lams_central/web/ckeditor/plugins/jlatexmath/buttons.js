function insertFormula() {
	var text = $(this).attr('title') + ' ',
		textarea = $('#latex-formula').focus(), 
		cursorPos = textarea.prop('selectionStart'),
		v = textarea.val(),
		textBefore = v.substring(0, cursorPos),
		textAfter = v.substring(cursorPos, v.length);
    textarea.val(textBefore + text + textAfter);
    cursorPos = cursorPos + text.length;
    textarea.prop('selectionEnd', cursorPos);
    textarea.prop('selectionStart', cursorPos);
    formulaTextareaChanged();
}

$(document).ready(function(){
	$('#formulaTabs').tabs().removeClass('ui-widget-content');
	$('#formulaTabs > ul').removeClass('ui-widget-header');
	$('#formulaTabs button').click(insertFormula).button();
});