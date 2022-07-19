// Functions for displaying portraits using Javascript. The logic should match PortraitTag.jsp except that this uses all divs and the tag uses an image. 

var NUM_COLORS = 6,
   CSS_ROUND = 'portrait-round',
   PORTRAIT_VERSION_SUFFIX = ' portrait-color-',
   CSS_GENERIC_SMALL = 'portrait-generic-sm',
   CSS_GENERIC_MEDIUM = 'portrait-generic-md',
   CSS_GENERIC_LARGE = 'portrait-generic-lg',
   CSS_GENERIC_XLARGE = 'portrait-generic-xl',
   CSS_SMALL = 'portrait-sm',
   CSS_MEDIUM = 'portrait-md', 
   CSS_LARGE = 'portrait-lg',
   CSS_XLARGE = 'portrait-xl', 
   VERSION_SMALL = '&version=4',
   VERSION_MEDIUM = '&version=3',
   VERSION_LARGE = '&version=2',
   VERSION_XLARGE = '&version=1',
   STYLE_SMALL = 'small',
   STYLE_MEDIUM = 'medium',
   STYLE_LARGE = 'large',
   STYLE_XLARGE = 'xlarge';

// Add a portrait to an existing div as specified by selector.
function addPortrait( selector, portraitId, userId, size, round, LAMS_URL ) {
    var isRound = round == null ? true : round;
    if ( portraitId ) {
    		selector.css('background-image', 'url(' + LAMS_URL + 'download?preferDownload=false&uuid=' + portraitId + _getSizeVersion(size) + ')');
    		selector.addClass(_getSizeCSS(size));
    		if ( isRound ) {
	    		selector.addClass(CSS_ROUND);
    		}
    } else {
    		selector.addClass(_getGenericSizeClass( size ));
    		selector.addClass(getPortraitColourClass(userId));
    }
}

// Define a DIV element that is the learner's portrait. Call in the ajaxProcessing (tablesorter) or 
// in a formatter function (jqgrid). Is the direct eqivalent of the Portrait tag. Use this method if you need to return
// the HTML which defines a portrait. Use addPortrait if you need to add a portrait to an existing DIV.
function definePortrait( portraitId, userId, size, round, LAMS_URL, portraitLocationClasses ) {
    var isRound = round == null ? true : round;
    if ( portraitId ) {
    		var retValue = '<div style="background-image:url(' + LAMS_URL + 'download?preferDownload=false&uuid='
    			+ portraitId + _getSizeVersion(size) + ')" class="' + _getSizeCSS(size);
    		if ( isRound ) {
    			retValue += ' ' + CSS_ROUND;
    		}
    		if ( portraitLocationClasses ) {
    			retValue += ' ' + portraitLocationClasses;
    		}
    		retValue += '"></div>';
    		return retValue;
    } else {
		var retValue = '<div class="' + _getGenericSizeClass(size) + ' ' + getPortraitColourClass(userId);
		if ( portraitLocationClasses ) {
			retValue += ' ' + portraitLocationClasses;
		}
		retValue += '"></div>';
		return retValue;
    }
}

// define a small header with the portrait and two lines of text next to portrait
function definePortraitMiniHeader(portraitId, userId, LAMS_URL, line1, line2, portraitOnRight, otherClasses) {
	var html = '<div';
	if ( otherClasses )
		html += ' class="' + otherClasses + '"';
	html += '>';
	html += definePortrait(portraitId, userId, "small", true, LAMS_URL, portraitOnRight ? "pull-right" : "pull-left roffset5");
	html += '<span>' + line1 + '</span>';
	html += '<br/><span style="font-size: smaller">' + line2 + '</span>';
	html += '</div>';
	return html;
}

// Get the colour that would be used for the generic portrait. Useful when you need a consistent colour for a user. 
function getPortraitColourClass(userId) {
	return PORTRAIT_VERSION_SUFFIX + userId % NUM_COLORS;
}

// Create a popover/tooltip to display the learner's portrait. Put it in bind('pagerInitialized pagerComplete', function(event, options){})
// (tablesorter) or within loadComplete (jqgrid). Elements to be processed should have class new-popover and dataset values for 'portrait' 
// and 'fullname'. Use with definePortraitPopover. Size is optional. 
function initializePortraitPopover(LAMS_URL, size, placement) {
	if ( ! size ) 
		size = STYLE_LARGE;
	
	if (!placement) {
		placement = 'right';
	}
	
	$('.new-popover').each(function( index, element ) {
		if ( ! element.dataset ) {
			console.log("Warning: portrait dataset missing. Unable to display portrait. "+element.id);
			return;
		}
		
		var fullName = element.dataset.fullname;
		if (fullName) {
			// apostrophe is the only character from monitorLesson.js#escapeHtml() which is allowed in user name 
			fullName = fullName.replace('&#039;', "'");
		}
		if ( element.dataset.portrait ) {
			var url =  LAMS_URL + '/download?preferDownload=false&uuid='+element.dataset.portrait+_getSizeVersion(size)
			// uses custom template to set the size of the portrait area
			var template = '<div class="popover" role="tooltip"><div class="popover-arrow"></div><h3 class="popover-header"></h3>' + 
						   '<div class="popover-body popover-content-with-portrait"></div></div>';
			$(element).popover({
				template: template, 
				content: '<img src="'+url+'"/>',  
				html: true,
		        trigger: 'hover focus',
		        title: fullName,
		        delay: { "show": 400, "hide": 100 },
		        container: 'body',  // ensures popovers are not clipped within jqgrid tables
		        placement: placement
			});
		} else {
			$(element).popover({
				content: fullName,
		        trigger: 'hover focus',
		        delay: { "show": 400, "hide": 100 },
		        container: 'body', // ensures popovers are not clipped within jqgrid tables
		        placement: placement
			});
		}
		element.classList.remove('new-popover');
	});
	}


// Define the element to have a popover/tooltip to display the learner's portrait. Call in the ajaxProcessing (tablesorter) or 
// in a formatter function (jqgrid). Use with initializePortraitPopover. Displays the fullName and portrait in the popover when the 
// user hovers on linkText. Usually linkText === fullName.  
//
// If showNameNoPortrait and portraitId is missing then assume just want to show the name as a hover area. Useful for showing 
// the portraits in the learner progress. 
function definePortraitPopover(portraitId, userId, fullName, linkText, showNameNoPortrait) {
	if ( ! linkText )
		linkText = fullName;
	
	if ( portraitId ) {
		return '<a tabindex="0" class="popover-link new-popover" role="button" data-toggle="popover" data-id="popover-'+userId
			+'" data-portrait="'+portraitId+'" data-fullname="'+fullName+'">'+linkText+'</a>';
	} else if ( showNameNoPortrait ) {
		return '<a tabindex="0" class="popover-link new-popover" role="button" data-toggle="popover" data-id="popover-'+userId
			+'" data-fullname="'+fullName+'">'+linkText+'</a>';
	} else {
		return linkText;
	}
}

function _getSizeCSS(size) {
    	if ( size ) {
    	    if (size == STYLE_MEDIUM)
	    	    	return CSS_MEDIUM;
    	    if (size == STYLE_LARGE)
    	    		return CSS_LARGE;
	    		if (size == STYLE_XLARGE)
	    			return CSS_XLARGE;
		} 
    	return CSS_SMALL;
}

function _getSizeVersion(size) {
    	if ( size ) {
    	    if (size == STYLE_MEDIUM)
	    	    	return VERSION_MEDIUM;
    	    if (size == STYLE_LARGE)
    	    		return VERSION_LARGE;
	    		if (size == STYLE_XLARGE)
	    			return VERSION_XLARGE;
		} 
    	return VERSION_SMALL;
}

function _getGenericSizeClass(size) {
  	if ( size ) {
    	    if (size == STYLE_MEDIUM)
	    	    	return CSS_GENERIC_MEDIUM;
    	    if (size == STYLE_LARGE)
    	    		return CSS_GENERIC_LARGE;
	    		if (size == STYLE_XLARGE)
	    			return CSS_GENERIC_XLARGE;
		} 
 	return CSS_GENERIC_SMALL;
}