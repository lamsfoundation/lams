function validateForm() {
	//in case main characters restriction is ON check it's been fullfilled
	var isMinCharactersEnabled = $("#min-characters-enabled").val()  == "true";
	var charsMissing = $("#char-required-div").html();
				
	var isValid = !isMinCharactersEnabled || isMinCharactersEnabled && (charsMissing == "0");
	if (!isValid) {
		var warningMsg = warning.replace("{0}", charsMissing); 
		alert(warningMsg);
	}
				
	return isValid;
}

function getNumberOfCharacters(value, isRemoveHtmlTags) {
	
    //HTML tags stripping 
	if (isRemoveHtmlTags) {
		value = value.replace(/&nbsp;/g, ' ').replace(/\n/gi, '').replace(/<\/?[a-z][^>]*>/gi, '');
	}
	
    var wordCount = value ? (value).length : 0;
    return wordCount;
}

