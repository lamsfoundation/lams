/*
- Script for collapse or expand the parent message and all his childs
- It uses the scriptaculous libraries for the effects

Autor: Veronica De La Cruz

*/

// Libraries to be included in this script are the
// scriptaculuos's libraries of ajaxhelper


// Some global variables, used in dynamicExpand() 

var expand_symbol ='<img src="/resources/forums/Expand16.gif" width="16" height="16" ALT="+" border="0">';
var collapse_symbol ='<img src="/resources/forums/Collapse16.gif" width="16" height="16" ALT="-" border="0">';

// This function only creates an array by a giving string of characters
// separated by blank space

function getChildsArray(childrenString) {

	var childrenArray = new Array();
        var aux  = childrenString;
        var aux2 = "";
        var pos = childrenString.indexOf(" ");
        var childrenStringLength = childrenString.length;       
        var i = 0;
        var j = 0;

 	 // Separate all the children

  	 while(j <= childrenStringLength) {
    
  		 if (pos == -1){
			aux2= aux.substring(j);
	        	childrenArray[i] = aux2;
	        	break;
		 }	

   	 childrenArray[i]= aux.substring(j,pos);         
      	 j=pos+1;
         pos = childrenString.indexOf(" ",j);
         i++;  
  	 }


	  return childrenArray;

 }


// Function that return the element properties and id

function getId(id) {
       if(document.getElementById)
		return document.getElementById(id);
       else if(document.all)
		return document.all(id);
	return false;
}


/*
  FUNCTION: 
	expandChilds

  PARAMETERS:
	- parentID: contains only the message_id
	- childrenString: contains a string of all the children of the current message separate by
			  blank space

  FORMAT OF PARAMETERS:

  	- parentID : message_id ; Example: 2343
        - childrenString : childrenmessage_id childrenmessage_id ; Example: 23324 32 31 321 

*/



function expandChilds(parentID, childrenString){

 //Get the parentID in a portable manner

    var objectParent = getId('content'+parentID);
    var actionObjectParent = getId('actions'+parentID);
    var parentSymbol = getId('toggle'+parentID);
    var subject = getId('subject' +parentID);
    var joinAll = getId('join'+parentID);
    var parentStyle = Element.getStyle(objectParent,'display');

 
  //Only if the parentID message has any children

  if (childrenString != null) {
  
    	 var childArray = getChildsArray(childrenString); 
 

   // Make that the parent message and all his children 
   // take part on the effect
 
   
	   for (var i=0;i< childArray.length;i++){         	       	 		 
	        
    	
        	var objectChild = getId('content'+childArray[i]);
	        var objectAction= getId('actions'+childArray[i]);
                var objectSymbol= getId('toggle'+childArray[i]);
                var objectSubject= getId('subject'+childArray[i]);
                var objectJoin = getId('join'+childArray[i]);

                var objectStyle = Element.getStyle(objectChild,'display');
	             
		// Change the state of the individual collapse/expand

                if (objectStyle == 'none') {
                        objectSymbol.innerHTML = collapse_symbol;
                 }

                
                 Element.show(objectChild);
		 Element.show(objectAction);       	                              		 
		 Element.hide(objectJoin);
                 Element.show(objectSubject);

        
 	   }	

    } 
           // Change the state of the individual collapse/expand

           if (parentStyle == 'none') {
                        parentSymbol.innerHTML = collapse_symbol;
           }
         
           Element.show(objectParent);
           Element.show(actionObjectParent);        
	   Element.show(subject);
	   Element.hide(joinAll);
           

         
 }




// The same as expandsChilds but collapse the contents of the message

function collapseChilds(parentID, childrenString){

 //Get the parentID in a portable manner

    var objectParent = getId('content'+parentID);
    var actionObjectParent = getId('actions'+parentID);
    var parentSymbol = getId('toggle'+parentID);
    var subject = getId('subject'+parentID);    
    var joinAll = getId('join'+parentID);

    var parentStyle = Element.getStyle(objectParent,'display');
     
  //Only if the parentID message has any children

  if (childrenString != null) {
  
     var childArray = getChildsArray(childrenString); 
 

   // Make that the parent message and all his children 
   // take part on the efect
 
   
	   for (var i=0;i< childArray.length;i++){    	       		 
	        	         	
    	        var objectChild = getId('content'+childArray[i]);
	        var objectAction= getId('actions'+childArray[i]);
                var objectSymbol= getId('toggle'+childArray[i]);
                var objectSubject= getId('subject'+childArray[i]);              
                var objectJoin = getId('join'+childArray[i]);
                var objectStyle = Element.getStyle(objectChild,'display');               

		
		// Change the state of the individual collapse/expand

                  if (objectStyle == 'block') {
                        objectSymbol.innerHTML = expand_symbol;
                 }
	        
	         Element.hide(objectChild);
		 Element.hide(objectAction);       	
                 Element.hide(objectSubject); 
                 Element.show(objectJoin);  
		 Element.setStyle(objectJoin,{display:'inline-block'});
 	   }	

   } 
	// Change the state of the individual collapse/expand

         if (parentStyle == 'block') {
                        parentSymbol.innerHTML = expand_symbol;
         }
   
           Element.hide(objectParent);
           Element.hide(actionObjectParent);
           Element.hide(subject);
           Element.show(joinAll);
	   Element.setStyle(joinAll,{display:'inline-block'});    
}

/*
  FUNCTION: dynamicExpand

  PARAMETERS: the id of the element to be expanded or collapsed

  USE CASE: for individual functionality for expand or collapse 
	    messages
*/


 function dynamicExpand(targetID) {

   var objectTargetID = getId('content'+targetID);
   var objectAction= getId('actions'+targetID);
   var symbolLink  = getId('toggle'+targetID);
   var objectSubject = getId('subject'+targetID);
   var objectJoin = getId('join'+targetID);
   var objectStyle = Element.getStyle(objectTargetID,'display');
   
   
   if(!symbolLink || !objectTargetID) return; 
     
	 // If the message is collapse we want that
	 // the symbol to be displayed is the expand one.

	    if (objectStyle == 'none') {		
		  
	    	 symbolLink.innerHTML = collapse_symbol;
                 Element.hide(objectJoin);
		 Element.show(objectTargetID);
		 Element.show(objectAction);
                 Element.show(objectSubject);
		
                 
   	    } 
	    else {
		 
		 symbolLink.innerHTML = expand_symbol;

                 Element.show(objectJoin);
		 Element.setStyle(objectJoin,{display:'inline-block'});
       		 Element.hide(objectTargetID);
		 Element.hide(objectAction);             
		 Element.hide(objectSubject);
                 


		
   	    }
  
 }

/*Show expanded only the direct children of the main message when the
  page is loaded */

 function showExpandedOnLoad( parentID, directChildrenString, allChildrenString) {    	

	var directChildren = getChildsArray(directChildrenString);    
        var  allChildren = getChildsArray(allChildrenString);
        
         for (var i=0;i< directChildren.length;i++){
 		for (var j=0;j< allChildren.length;j++){
			if (allChildren[j] == directChildren[i]){
				allChildren[j]= undefined;	
				break;
                        }	
			      continue;		
		}
         }

	for (var j=0;j< allChildren.length;j++) {
            if(allChildren[j] != undefined){
		collapseOnLoad(allChildren[j]);
                loadContent(allChildren[j]);
            }
	}
 }

 
//------------------------------


/* The same as expandsChilds but collapse the contents of the message*/

function collapseOnLoad(parentID){

 //Get the parentID in a portable manner
  
    var actionObjectParent = getId('actions'+parentID);
    var parentSymbol = getId('toggle'+parentID);
    var subject = getId('subject'+parentID);    
    var joinAll = getId('join'+parentID); 
     
     
	// Change the state of the individual collapse/expand

        
           parentSymbol.innerHTML = expand_symbol;
           
       // Collapse the message contents

           Element.hide(actionObjectParent);
           Element.hide(subject);
           Element.show(joinAll);
	   Element.setStyle(joinAll,{display:'inline-block'});    
           
}

/*
  Loads the content of a given message. 
  parameters: messageID (the ID of the message that the content is required)
*/

function loadContent(messageID) {
    
   var containerID = getId('content'+ messageID);
   var symbol = getId('toggle'+ messageID);
   var url = 'load-message-content';
   var par = 'message_id='+messageID;

   
   Element.setStyle(containerID, {display:'none'});

   // This is the function that preloads the message's contents   
   var ajax =  new Ajax.Updater(containerID, url, {asynchronous:'true', method:'post', parameters: par});   
  
}  
