import org.lamsfoundation.lams.common.comms.*       //communications

var comms:Communication = new Communication('localhost');

loadListener = new Object();
loadListener.click = function(eventObject){
	//load the source path
	var s = source_txt.text;
	
	comms.getRequest(s,sourceLoaded,true);
	

 

}

saveListener = new Object();
saveListener.click = function(eventObject){
	
	
	

 

}

//will recive the message value
function sourceLoaded(dto):Void{
	var rXML:XML = new XML();
	trace(dto.toString());
	rXML = comms.serializeObj(dto);
	output_txa.text = rXML.toString();
}





load_btn.addEventListener("click", loadListener);
save_btn.addEventListener("click", saveListener);

