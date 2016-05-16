
var popupWindow = null;
function openPopup(url, height, width) {	
	if (popupWindow && popupWindow.open && !popupWindow.closed) {
		popupWindow.close();
	}
	popupWindow = window.open(url,'popupWindow','resizable,width=' +width+ ',height=' +height+ ',scrollbars');
	popupWindow.window.focus();
}