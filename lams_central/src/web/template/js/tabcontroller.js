
var tabsContents = new Array();
var tab_lefts = new Array();
//TODO:we need to set this variable, maybe from the praent or direct from JSP/Java
var themeName = "aqua";
var currentTab = "";

function initTabs(){
	//put all the tab contents in an array
	tabsContents.push(document.getElementById('content_b'));
	tabsContents.push(document.getElementById('content_a'));
	tabsContents.push(document.getElementById('content_i'));
	
	//position the advanced (a) and instructions (i) layers over the basic (b) layer
	var bTabC_x = findPosX(tabsContents[0]);
	var bTabC_y = findPosY(tabsContents[0]);
	
	tabsContents[1].style.left = bTabC_x+'px';
	tabsContents[1].style.top = bTabC_y+'px';
	tabsContents[2].style.left = bTabC_x+'px';
	tabsContents[2].style.top = bTabC_y+'px';
	tabsContents[1].style.visibility="hidden";
	tabsContents[2].style.visibility="hidden";
}

function deSelectTab(tabId){
	//swap images of side parts
	var tl = document.getElementById("tab_left_"+tabId);
	tl.src="../images/"+themeName+"_tab_left.gif";
	tl.height = 22;
	var tr = document.getElementById("tab_right_"+tabId);
	tr.src="../images/"+themeName+"_tab_right.gif";
	tr.height = 22;
	//swap css of centre class
	var tc = document.getElementById("tab_tbl_centre_"+tabId);
	tc.className="tab tabcentre";	
}

function selectTab(tabId){
	//alert('selecttab');
	//swap images of side parts
	var tl = document.getElementById("tab_left_"+tabId);
	tl.src="../images/"+themeName+"_tab_s_left.gif";
	tl.height = 25;
	var tr = document.getElementById("tab_right_"+tabId);
	tr.src="../images/"+themeName+"_tab_s_right.gif";
	tr.height = 25;
	//swap css of centre class
	var tc = document.getElementById("tab_tbl_centre_"+tabId);
	tc.className="tab tabcentre_selected";

	//if(tabId=="a"){
		//enableFCKEditor();
	//}
	
}

function enableFCKEditor(){
	//alert('enableFCKEditor');
	// This is a hack for Gecko... it stops editing when the editor is hidden.
	if ( !document.all ){
		var oEditor = FCKeditorAPI.GetInstance( 'FCKeditor1' ) ;
		
		if (  oEditor.EditMode == FCK_EDITMODE_WYSIWYG )
		{
			oEditor.SwitchEditMode() ;
			oEditor.SwitchEditMode() ;
		}
	}	
}


function showTab(tabId){
	if (currentTab == tabId){
		return;
	}else{
		currentTab = tabId;
	}
	
	selectTab(tabId);

	if(tabId == "i"){
		deSelectTab("a");
		deSelectTab("b");
	}else if(tabId=="a"){
		deSelectTab("i");
		deSelectTab("b");
	}else{
		deSelectTab("i");
		deSelectTab("a");
	}
	
	//sort out the content
	var contentId = "content_"+tabId;
	for(var i=0; i < tabsContents.length; i++){
		if(tabsContents[i].id==contentId){
			//document.getElementById(tabsContents[i].id).style.display	= '' ;
			tabsContents[i].style.visibility="visible";
		}else{
			//document.getElementById(tabsContents[i].id).style.display	= 'none' ;
			tabsContents[i].style.visibility="hidden";
		}
	}
	
	
	
}

function findPosX(obj)  {
    var curleft = 0;
    if(obj.offsetParent)
        while(1) 
        {
          curleft += obj.offsetLeft;
          if(!obj.offsetParent)
            break;
          obj = obj.offsetParent;
        }
    else if(obj.x)
        curleft += obj.x;
    return curleft;
  }

function findPosY(obj)  {
    var curtop = 0;
    if(obj.offsetParent)
        while(1)
        {
          curtop += obj.offsetTop;
          if(!obj.offsetParent)
            break;
          obj = obj.offsetParent;
        }
    else if(obj.y)
        curtop += obj.y;
    return curtop;
  }
