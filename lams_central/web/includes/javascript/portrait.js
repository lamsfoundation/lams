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

function addPortrait( selector, portraitId, userId, size, round, LAMS_URL ) {
    var isRound = round == null ? true : round;
    if ( portraitId  && portraitId > 0) {
    		selector.css('background-image', 'url(' + LAMS_URL + 'download?preferDownload=false&uuid=' + portraitId + getSizeVersion(size) + ')');
    		selector.addClass(getSizeCSS(size));
    		if ( isRound ) {
	    		selector.addClass(CSS_ROUND);
    		}
    } else {
    		selector.addClass(getGenericSizeClass( size ));
    		selector.addClass(PORTRAIT_VERSION_SUFFIX + userId % NUM_COLORS );
    }
}

function getSizeCSS(size) {
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

function getSizeVersion(size) {
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

function getGenericSizeClass(size) {
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