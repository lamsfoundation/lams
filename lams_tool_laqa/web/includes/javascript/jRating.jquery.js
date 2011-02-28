/************************************************************************
*************************************************************************
@Name :       	jRating - jQuery Plugin
@Revison :    	2.0
@Date : 		26/01/2011
@Author:     	 Surrel Mickael (www.myjqueryplugins.com - www.msconcept.fr) 
@License :		 Open Source - MIT License : http://www.opensource.org/licenses/mit-license.php
 
**************************************************************************
*************************************************************************/
jQuery.jRating = {
	build : function(options)
	{
        var defaults = {
			/** String vars **/
			bigStarsPath : pathToImageFolder + '/stars.png', // path of the icon stars.png
			smallStarsPath : pathToImageFolder + '/small.png', // path of the icon small.png
			phpPath : 'php/jRating.php', // path of the php file jRating.php
			type : 'big', // can be set to 'small' or 'big'
			
			/** Boolean vars **/
			step:false, // if true,  mouseover binded star by star,
			isDisabled:false,
			
			/** Integer vars **/
			length:5, // number of star to display
			decimalLength : 0, // number of decimals.. Max 3, but you can complete the function 'getNote'
			rateMax : 20, // maximal rate - integer from 0 to 9999 (or more)
			rateInfosX : -45, // relative position in X axis of the info box when mouseover
			rateInfosY : 5, // relative position in Y axis of the info box when mouseover
			
			/** Functions **/
			onSuccess : null,
			onError : null
        };   
		
		if(this.length>0)
		return jQuery(this).each(function(i) {
			var opts = $.extend(defaults, options);       
			var $this = $(this);
			var newWidth = 0;
			var starWidth = 0;
			var starHeight = 0;
			var bgPath = '';
			
			if($this.hasClass('jDisabled') || opts.isDisabled)
				var jDisabled = true;
			else
				var jDisabled = false;
				
			getStarWidth();
			$this.height(starHeight); // set the Height main container to starHeight
			
			var average = parseFloat($this.attr('data').split('_')[0]); // get the average
			var idBox = parseInt($this.attr('data').split('_')[1]); // get the id of the box
			var widthRatingContainer = starWidth*opts.length; // Width of the Container
			var widthColor = average/opts.rateMax*widthRatingContainer; // Width of the color Container
			
			
			var $quotient = 
				jQuery('<div>', 
				{
					'class' : 'jRatingColor',
					css:{
						width:widthColor
					}
				}).appendTo($this);
			
			var $average = 
				jQuery('<div>', 
				{
					'class' : 'jRatingAverage',
					css:{
						width:0,
						top:- starHeight
					}
				}).appendTo($this);
				
			var $jstar =
				jQuery('<div>', 
				{
					'class' : 'jStar',
					css:{
						width:widthRatingContainer,
						height:starHeight,
						top:- (starHeight*2),
						background: 'url('+bgPath+') repeat-x'
					}
				}).appendTo($this);
			
			$this.css({width: widthRatingContainer,overflow:'hidden',zIndex:1,position:'relative'});
			
			/** Events & functions **/
			if(!jDisabled)
			$this.bind(
			{
				mouseenter : function(e){
					var realOffsetLeft = findRealLeft(this);
					var relativeX = e.pageX - realOffsetLeft;
					var tooltip = 
					jQuery('<p>',{
						'class' : 'jRatingInfos',
						html : getNote(relativeX)+' <span class="maxRate">/ '+opts.rateMax+'</span>',
						css : {
							top: (e.pageY + opts.rateInfosY),
							left: (e.pageX + opts.rateInfosX)
						}
					}).appendTo('body').show();
				},
				mouseover : function(e){
					$this.css('cursor','pointer');	
				},
				mouseout : function(){
					$this.css('cursor','default');
					$average.width(0);
				},
				mousemove : function(e){
					/** Very *important* hack to make jRating work with IE **/
					$("p.jRatingInfos").html("aa");
					
					var realOffsetLeft = findRealLeft(this);
					var relativeX = e.pageX - realOffsetLeft;
					if(opts.step) newWidth = Math.floor(relativeX/starWidth)*starWidth + starWidth;
					else newWidth = relativeX;
					$average.width(newWidth);					
					$("p.jRatingInfos")
					.css({
						left: (e.pageX + opts.rateInfosX)
					})
					.html(getNote(newWidth) +' <span class="maxRate">/ '+opts.rateMax+'</span>');
				},
				mouseleave : function(){
					$("p.jRatingInfos").remove();
				},
				click : function(e){
					$this.unbind().css('cursor','default').addClass('jDisabled');
					$("p.jRatingInfos").fadeOut('fast',function(){$(this).remove();});
					e.preventDefault();
					var rate = getNote(newWidth);
					$average.width(newWidth);
						
					$.post(opts.phpPath,{
							idBox : idBox,
							rate : rate,
							action : 'rating'
						},
						function(data) {
							if(!data.error)
							{
								/** Here you can display an alert box, 
									or use the jNotify Plugin :) http://www.myqjqueryplugins.com/jNotify
									exemple :	*/
								if(opts.onSuccess) opts.onSuccess(data, idBox);
							}
							else
							{
								/** Here you can display an alert box, 
									or use the jNotify Plugin :) http://www.myqjqueryplugins.com/jNotify
									exemple :	*/
								if(opts.onError) opts.onError();
							}
						},
						'json'
					);
				}
			});
			
			function getNote(relativeX) {
				var noteBrut = parseFloat((relativeX*100/widthRatingContainer)*opts.rateMax/100);
				switch(opts.decimalLength) {
					case 1 :
						var note = Math.round(noteBrut*10)/10;
						break;
					case 2 :
						var note = Math.round(noteBrut*100)/100;
						break;
					case 3 :
						var note = Math.round(noteBrut*1000)/1000;
						break;
					default :
						var note = Math.round(noteBrut*1)/1;
				}
				return note;
			};
			
			function getStarWidth(){
				switch(opts.type) {
					case 'small' :
						starWidth = 12; // width of the picture small.png
						starHeight = 10; // height of the picture small.png
						bgPath = opts.smallStarsPath;
					break;
					default :
						starWidth = 23; // width of the picture stars.png
						starHeight = 20; // height of the picture stars.png
						bgPath = opts.bigStarsPath;
				}
			};
			
			function findRealLeft(obj) {
			  if( !obj ) return 0;
			  return obj.offsetLeft + findRealLeft( obj.offsetParent );
			};
		});
	}
}; jQuery.fn.jRating = jQuery.jRating.build;