
function init() {
	// open the current tab    
	var tag = document.getElementById("currentTab");
	if (tag.value != "") {
		selectTab(tag.value);
	} else {
		selectTab(1);
	}
	
	image = document.getElementById("image");
	if (image != null)
	{
		origImageHeight = image.height;
		origImageWidth =  image.width;
	
		if (image.height >= image.width)
		{
			if (image.height > 300)
			{
				image.height = 300;
			}
		}
		else
		{
			if (image.width > 300)
			{
				image.width = 300;
			}
		}		
	}		
}
function doSelectTab(tabId) {
	var tag = document.getElementById("currentTab");
	tag.value = tabId;
	selectTab(tabId);
}
function doSubmit(method) {
	document.forms.authoringForm.action= method+".do";
	document.forms.authoringForm.submit();
}
