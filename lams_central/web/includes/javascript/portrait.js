// Functions for displaying portraits using Javascript. The logic should match PortraitTag.jsp except that this uses all divs and the tag uses an image. 

var NUM_COLORS = 7,
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

// Add a portrait to an existing div as specified by selector
function addPortrait( selector, portraitId, userId, size, round, LAMS_URL ) {
    var isRound = round == null ? true : round;
    if ( portraitId  && portraitId > 0) {
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

// Get the colour that would be used for the generic portrait. Useful when you need a consistent colour for a user. 
function getPortraitColourClass(userId) {
	return PORTRAIT_VERSION_SUFFIX + userId % NUM_COLORS;
}

// Create a popover/tooltip to display the learner's portrait. Put it in bind('pagerInitialized pagerComplete', function(event, options){})
// (tablesorter) or within loadComplete (jqgrid). Elements to be processed should have class new-popover and dataset values for 'portrait' 
// and 'fullname'. Use with definePortraitPopover. Size is optional. 
function initializePortraitPopover(LAMS_URL, size) {
	if ( ! size ) 
		size = STYLE_LARGE;
	
	$('.new-popover').each(function( index, element ) {
		var idString = element.dataset.id;
		var url = LAMS_URL + '/download?preferDownload=false&uuid='+element.dataset.portrait+_getSizeVersion(size);
			$(element).popover({
				content: '<img src="'+url+'"/>',  
				html: true,
		        trigger: 'hover focus',
		        title: element.dataset.fullname,
		        container: 'body' // ensures popovers are not clipped within jqgrid tables
		});
			element.classList.remove('new-popover');
	});
	}


// Define the element to have a popover/tooltip to display the learner's portrait. Call in the ajaxProcessing (tablesorter) or 
// in a formatter function (jqgrid). Use with initializePortraitPopover. Displays the fullName and portrait in the popover when the 
// user hovers on linkText. Usually linkText === fullName
function definePortraitPopover(portraitId, userId, fullName, linkText) {
	if ( ! linkText )
		linkText = fullName;
	
	if ( portraitId ) {
		return '<a tabindex="0" class="popover-link new-popover" role="button" data-toggle="popover" data-id="popover-'+userId
			+'" data-portrait="'+portraitId+'" data-fullname="'+fullName+'">'+linkText+'</a>';
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