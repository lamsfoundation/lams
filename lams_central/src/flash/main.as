import org.lamsfoundation.lams.authoring.Application

//Temp values to be removed / repplaced at deployment
if(serverURL == null){
	_root.serverURL = "http://dolly.uklams.net:8080/lams/";
}
var serverURL;

//Set stage alignment to top left and prent scaling
Stage.align = "TL";
Stage.scaleMode = "noScale";


//Start the application, passing in the top level clip, i.e. _root
var app:Application = Application.getInstance();
app.main(this);

//Make app listener for stage resize events
Stage.addListener(app);




