import org.lamsfoundation.lams.common.comms.*       //communications

var comms:Communication = new Communication('localhost');

loadListener = new Object();
loadListener.click = function(eventObject){
	//load the source path
	var s = source_txt.text;
	comms.getRequest(s,sourceLoaded,true);
}

createXMLListener = {};
createXMLListener.click = function(eventObject){
	//get the data
	var theData = dict_dtg.dataProvider;
	var rXML:XML = new XML();
	var dto = {messageKey:"getDictionary",messageType:3.0,messageValue:theData};
	rXML = comms.serializeObj(dto);
	output_txa.text = rXML.toString();

}

addRowListener = {};
addRowListener.click = function(evt){
	_global.breakpoint();
	var r = dict_dtg.addItem({key:"",value:"",description:""});
	
	dict_dtg.selectedIndex = r;
}

delRowListener = {};
delRowListener.click = function(evt){
	var i:Number = dict_dtg.selectedIndex;
	dict_dtg.removeItemAt(i);
}

clearListener = {};
clearListener.click = function(evt){
	dict_dtg.dataProvider = new Array();
	dict_dtg.removeAllColumns();
	output_txa.text = "";
}




//will recive the message value
function sourceLoaded(dto):Void{
	//populate the datagrid
	dict_dtg.dataProvider = dto;
}







load_btn.addEventListener("click", loadListener);
createXML_btn.addEventListener("click", createXMLListener);
addRow_btn.addEventListener("click", addRowListener);
delRow_btn.addEventListener("click", delRowListener);
clear_btn.addEventListener("click", clearListener);
source_txt.text = "X:\\lams_central\\web\\flashxml\\en_dictionary.xml";
