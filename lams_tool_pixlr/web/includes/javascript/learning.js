
var origImageHeight;
var origImageWidth;

function init()
{
	//resizing image to thumbnail size
	var image = document.getElementById("image");
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

function openImage(url)
{
	openPopup(url, origImageHeight, origImageWidth)
}

function disableFinishButton() 
{
	document.getElementById("finishButton").disabled = true;
}

function validateForm() 
{
}