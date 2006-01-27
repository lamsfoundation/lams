var activeEditorIndex = 0;
var commentFlag = "<!-- HTML -->";
var commentFlagLength = 13;
var oFCKeditor;


// FCKeditor_OnComplete is a special function that is called when an editor
// instance is loaded ad available to the API. It must be named exactly in
// this way.
function FCKeditor_OnComplete( editorInstance )
{
    //hideElementById("wyswygEditorScreen");
}


function SetContents(content)
{
	// Get the editor instance that we want to interact with.
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;

	// Set the editor contents (replace the actual one).
	oEditor.SetHTML(content) ;
}


function doWYSWYGEdit(index){

    var oEditor;
    try {
        oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
    }
    catch(error) {
        //browsers like opera can't resolve the FCKeditorAPI classes
        alert("The browser you are using doesn't support Rich Text Editor, Please use a supported browser instead.");
        return;
    }

    if(activeEditorIndex != index && activeEditorIndex != 0){
        saveWYSWYGEdittedText(activeEditorIndex); //save the existing content
        doPreview(activeEditorIndex); //update preview panel
    }
    
    activeEditorIndex = index;
    
    //hide html editor
    doPreview(index);
    
    var previewElement = document.getElementById("preview" + index + ".text");
    var posX = findPosX(previewElement);
    var posY = findPosY(previewElement);
                   
    var text = document.getElementById("tx" + index + ".textarea").value;
    
    oEditor.SetHTML(text) ;
    
    wyswygEditorScreenElement = document.getElementById("wyswygEditorScreen");
    wyswygEditorScreenElement.style.top = posY + "px";
    wyswygEditorScreenElement.style.left = posX + "px";
    
    
    showElementById("wyswygEditorScreen");
}


function saveWYSWYGEdittedText(index){
    var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
    var text = oEditor.GetXHTML( true )


	// add in <!-- HTML --> to indicate that we have used the wyswyg editor 
	var testText = text.substring(0,commentFlagLength);
	if ( commentFlag != testText ) {
		text = commentFlag + text;
	}

    var htmlEditorElement = document.getElementById("tx" + index + ".textarea");
    htmlEditorElement.value = text;
    
}

function doEdit(index){
    hideElementById("wyswygEditorScreen");
    hideElementById("preview"+index);
    showElementById("tx"+index);
}

function doPreview(index){
    var previewTextElement = document.getElementById("preview" + index + ".text");
    var previewText = document.getElementById("tx" + index + ".textarea").value;
    previewTextElement.innerHTML = previewText;

    hideElementById("wyswygEditorScreen");
    hideElementById("tx"+index);
    showElementById("preview"+index);
}




function showElement(element) {
    element.style.visibility = 'visible';
    element.style.display = "block";
}
function hideElement(element) {
    element.style.visibility = 'hidden';
    element.style.display = "none";
}

function showElementById(id) {
    var element = document.getElementById(id);
    showElement(element);
}

function hideElementById(id) {
    var element = document.getElementById(id);
    hideElement(element);
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


/*** implement the event onSelectTab() which gets trigger when tabs is changed ***/
function onSelectTab(tabID){
    //hide all active editors
    doPreview(activeEditorIndex);
}