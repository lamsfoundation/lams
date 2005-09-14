import org.lamsfoundation.lams.common.comms.*       //communications
import org.lamsfoundation.lams.common.util.*;

var comms:Communication = new Communication('localhost:8080/lams/');

loadListener = new Object();
loadListener.click = function(eventObject){
	//set new serverURL if its changed:
	//comms.serverUrl = server_txt.text;
	
	//turn the xml into an object
	var myXML:XML = new XML(input_txa.text);
	
	var dto:Object = comms.deserializeObj(myXML);
	
	//post it:
	comms.sendAndReceive(dto, server_txt.text,sourceLoaded,true);
	

 

}

saveListener = new Object();
saveListener.click = function(eventObject){
	
	
	

 

}

//will recive the message value
function sourceLoaded(dto):Void{
	
	output_txa.text = "Object recieved:" + ObjectUtils.printObject(dto);
	
	
	
}





load_btn.addEventListener("click", loadListener);


