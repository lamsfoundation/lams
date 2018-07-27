/*
 ### jQuery Star Rating Plugin v1.1 - 2008-02-21 ###
 By Diego A., http://www.fyneworks.com, diego@fyneworks.com
 
 Project: http://plugins.jquery.com/project/MultipleFriendlyStarRating
 Website: http://www.fyneworks.com/jquery/star-rating/
	
	This is a modified version of the star rating plugin from:
 http://www.phpletter.com/Demo/Jquery-Star-Rating-Plugin/
*/
// ORIGINAL COMMENTS:
/*************************************************
 This is hacked version of star rating created by <a href="http://php.scripts.psu.edu/rja171/widgets/rating.php">Ritesh Agrawal</a>
 It thansform a set of radio type input elements to star rating type and remain the radio element name and value,
 so could be integrated with your form. It acts as a normal radio button.
 modified by : Logan Cai (cailongqun[at]yahoo.com.cn)
 website:www.phpletter.com
************************************************/


/*# AVOID COLLISIONS #*/
;if(jQuery) (function($){
/*# AVOID COLLISIONS #*/

$.fn.rating = function(settings) {
 settings = jQuery.extend({}, {
   cancel:'Cancel Rating'
 }, settings || {});
 
 // multiple star ratings on one page
 var groups = {};
 
 var prevElem = null;
 var valueElem = null;
 //var container = $(this);
 var CancelElem = null;
 
 var event = {
  fill: function(n, el, style){ // fill to the current mouse position.
   //if(console) console.log(style || 'star_hover', arguments);
   var stars = $(groups[n].valueElem).siblings('.star');
   var index = stars.index(el) + 1;
   $(stars)
    .children('a').css('width', '100%').end()
    .removeClass('star_hover').removeClass('star_on')
    .slice(0,index).addClass( style || 'star_hover' ).end();
//    .slice(0,index).addClass('star_on').end();
  },
  drain: function(n) { // drain all the stars.
   //if(console) console.log(groups);
   //if(console) console.log([ 'drain', n, groups[n] ] );
   var stars = $(groups[n].valueElem).siblings('.star');
   $(stars)
    .filter('.star_on').removeClass('star_on').end()
    .filter('.star_hover').removeClass('star_hover').end();
  },
  reset: function(n){ // Reset the stars to the default index.
   //if(console) console.log(groups);
   //if(console) console.log([ 'reset', n, groups[n] ] );
   var stars = $(groups[n].valueElem).siblings('.star');
   $(stars).slice(0,groups[n].currentValue).addClass('star_on').end();
  }
 };
 
 this.each(function (i)
     {
      
      var n = this.name;
      if(!groups[n]) groups[n] = {count:0, currentValue: 0};
      groups[n].count += 1;
      i = groups[n].count - 1;
      
      if(i == 0)//prepend cancel option at the begining
      {
            
       groups[n].valueElem = $('<input type="hidden" name="' + this.name + '" value="" >');
       $(this).before(groups[n].valueElem);
        
        
        var CancelElem = $('<div class="cancel"><a href="#" title="' + settings.cancel + '">' + settings.cancel + '</a></div>');
        prevElem = CancelElem;
        $(this).before(prevElem); 

          $(CancelElem)
             .mouseover(function(){
               event.drain(n);
               $(this).addClass('star_on');
             })
             .mouseout(function(){
               event.reset(n);
               $(this).removeClass('star_on');
             })
             .click(function(){
               groups[n].currentValue = 0;//$(this).children('a').attr('title');
               $(groups[n].valueElem).val(groups[n].currentValue);
               event.drain(n);
               return false;
             });    
      };
      
      //insert rating option right after preview element
      if (settings.readOnly) {
          preElemTemp = $('<div class="star star_readonly"><a href="javascript:;" title="' + this.title + '">' + this.value + '</a></div>');
          $(prevElem).after(preElemTemp);
      } else {
          preElemTemp = $('<div class="star"><a href="javascript:;" title="' + this.title + '">' + this.value + '</a></div>');
          $(prevElem).after(preElemTemp);
   	  
          $(preElemTemp)
          .mouseover(function(){
             event.drain(n);
             event.fill(n, this);
             
           })
           .mouseout(function(){
             event.drain(n);
             event.reset(n);
           })
           .click(function(){
             groups[n].currentValue = $(this).children('a').text();
             $(groups[n].valueElem).val(groups[n].currentValue);
             event.drain(n);
             
             // callback function, as requested here: http://plugins.jquery.com/node/1655
             if(settings.callback) settings.callback.apply($(groups[n].valueElem)[0], [groups[n].currentValue, this]);
             
             //event.reset();
             event.fill(n, this);
           });      
      }
      
      //if(console) console.log(['###', n, this.checked, groups[n].initial]);
      if(this.checked) groups[n].initial = preElemTemp;
      
      prevElem = preElemTemp;
      preElemTemp = null;
      
      //remove this checkbox
      $(this).remove();
      if(i + 1 == this.length)//{
       event.reset(n);         
      //};

    }
    
   );
  
  // initialize groups...
  for(n in groups){
   if(groups[n].initial){
    $(groups[n].initial).each(function(){
     event.fill(n, this, 'star_on');
     groups[n].currentValue = $(this).children('a').text();
     $(groups[n].valueElem).val(groups[n].currentValue);
    });
   }
  };
  
  return this; // don't break the chain...
};



/*# AVOID COLLISIONS #*/
})(jQuery);
/*# AVOID COLLISIONS #*/