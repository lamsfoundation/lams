import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*
import org.lamsfoundation.lams.common.Config
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.authoring.Application
import mx.events.*/**  
* Debug  
* Can be used to print message to a floating windoe and to trace windoe.  SHoudl be used over trace().  
* Usage:
* import org.lamsfoundation.lams.common.util.Debug;
* 
* Debug.log('_toolkit_mc:'+_toolkit_mc,5,'createToolkit','ToolkitView');
* 
* 
*/  
class org.lamsfoundation.lams.common.util.Debugger {  
	
    //Declarations  
	private var _config:Config;
    public static var CRITICAL:Number = 1;      //level constants
    public static var HIGH:Number = 2;
    public static var MED:Number = 3;
    public static var GEN:Number = 4;
    public static var VERBOSE:Number = 4.5;
	public static var COMP:Number = 5;
	
    
    private static var _severityLevel:Number;
	private static var _crashDumpSeverityLevel:Number;
	private static var _allowDebug:Boolean = true;
	
	private static var _currentClass:String;
    private static var _instance:Debugger = null;
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    //private static var _msgLog:Array;
	public static var _msgLog:Array;
	public static var _crashDumpMsgLog:Array;

    //TODO DI 07/06/05 Add code to get dump of flash from _root
    
	//Constructor  
	private function Debugger() {  
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        _msgLog = [];
		_crashDumpMsgLog = [];
		
		
	}  
    
    /**
    * Retrieves an instance of the debugger singleton
    */ 
    public static function getInstance():Debugger{
        if(Debugger._instance == null){
            Debugger._instance = new Debugger();
        }
        return Debugger._instance;
    }
	
	public function set allowDebug(arg:Boolean):Void{
			_allowDebug = arg;
	}
	
	public function get allowDebug():Boolean{
			return _allowDebug;
	}
	/*
	* Method to Set the Message Log - Severity Level through Application once config file has been loaded
	*/
	public function set severityLevel(sLevel:Number):Void{
			_severityLevel = sLevel;
	}
	
	public function get severityLevel():Number{
			return _severityLevel;
	}
    
	/*
	* Method to Set the Crash Data Message Log - Crash Dump Severity Level through Application once config file has been loaded
	*/
	public function set crashDumpSeverityLevel(cdsLevel:Number):Void{
			_crashDumpSeverityLevel = cdsLevel;
	}
	public function get crashDumpSeverityLevel():Number{
			return _crashDumpSeverityLevel;
	}
	/**
	* Method to get the Design Data from canvas & debugger log and store it in an object to send it to the server. 
	*/
	public static function crashDataDump():Void{
        //getInstance();
		var dto:Object = new Object()
		var _instance = getInstance()
		
		
		var crashDumpLevel = _instance.crashDumpSeverityLevel;
		
		/*
		* //Write to Crash Dump log based on the Crash Dump Severity Level set through the Congif class
		*/
		for(var i=0;i<_msgLog.length;i++) {
			if(_msgLog[i].level <= _crashDumpSeverityLevel){ 
				_crashDumpMsgLog.push(_msgLog[i])
			}
        }
		
		//trace("Length of _crashDumpMsgLog:- "+_crashDumpMsgLog.length)	
		var sendDumpMsgLog = _crashDumpMsgLog;
		//_global.breakpoint();
		//Debugger.log('HI there: '+mytest,Debugger.CRITICAL,'crashDataDump','Debugger');
		dto.ddm = Application.getInstance().getDesignDataModel().getDesignForSaving();
		dto.crashDataLog = sendDumpMsgLog;   
		var callback:Function = Proxy.create(Debugger,onCrashDumpResponse);
		
		var comms = Application.getInstance().getComms();
        comms.sendAndReceive(dto, 'flashCrashDump',callback,false)
		
    }	
			
	public static function onCrashDumpResponse(r):Void{
		if(r instanceof LFError){
			r.showErrorAlert();
			
		}else{
					
			var msg:String = Dictionary.getValue('db_datasend_confirm')+r;
			LFMessage.showMessageAlert(msg);
			
		}
	}	/**
	* Method to print a message to the output - trace or window...
	* @param msg 			The actual message to be printed
	* @param level 			(Optional) Severity of this messgae:
	* 						1=critical error > 4 = general debugging message, 5 = component debug message
	* @param fname 			(Optional) Name of the function calling this log message
	* @param currentClass 	(Optional)Name of the class
	*/
	public static function log(msg:Object,level:Number,fname:String,currentClass:Object):Void{
        //Ensure we have an instance
        getInstance();
		var rawMsgStr = String(msg);
        
		if(_allowDebug){
			if(arguments.length == 1){
				level = 4;
			}			
			if(_severityLevel >= level){
                var date:Date = new Date();
				
				//Build debug log object for this entry
				var obj = {date:date,msg:msg,level:level,func:fname};
				
				//if the class name has changed, then print it out
				if(_currentClass != currentClass){
					_currentClass = String(currentClass);
					trace("-----------In:"+_currentClass+"-----------");
					//Add the scope to the log object
					obj.scope = currentClass;
				}
				
				//Write to trace(NOT USED??)
                msg = "["+fname+"]"+msg
				
				//chuck it to the trace window
				trace(msg);
				
                
                //Write to log
                _msgLog.push(obj);
					
				//trace("Length of _msgLog:- "+_msgLog.length)
                //Dispatch update event
                _instance.dispatchEvent({type:'update',target:_instance});
			}
		}
	}
	
	public static function error(e:Object):Void{
		if(e instanceof LFError){
			var lfe = LFError(e);
			trace("/////////////////////// ERROR ///////////////////");
			log(lfe.message,Debugger.CRITICAL,lfe.fname,"LFError");
			trace("/////////////////////////////////////////////////");
		}else{
			log("Recieved unsupported error type",Debugger.CRITICAL,"error","Debugger");
		}
		
	}
	/**
	* Legacy Method to print a message to the output - trace or window...
	* @param msg 			The actual message to be printed
	* 						1=critical error > 4 = general debugging message, 5 = component debug message
	* @param fname 			(Optional) Name of the function calling this log message
	*/
	public static function debug(msg:String,fname:String):Void{
		if(_allowDebug){
			if(arguments.length == 1){
				fname = "";
			}
			log(msg,4,fname,undefined);
		}
	}
    
    /**
    * @param format:Object  An object containing various format options for viewing the log
    * @returns the message log formatted
    */
    public function getFormattedMsgLog(format:Object):String {
        var ret:String;
        if(!format){
            format = {};
            format.date = false;
        }
        //Loop through messages and build return string
        //for(var i=0;i<_msgLog.length;i++) {
        //    ret += buildMessage(format,_msgLog[i]);
        //}
		//Loop through messages and build return string
		
        for(var i=0;i<_msgLog.length;i++) {
            ret += buildMessage(format,_msgLog[i]);
        }
        return ret;
    }
	
	/**
    * @param format:Object  An object containing various format options for viewing the log
    * @returns the message log formatted
    */
    public function getLatestMsg(format:Object):String {
        var ret:String;
        if(!format){
            format = {};
            format.date = false;
        }
        return buildMessage(format,_msgLog[_msgLog.length-1]);
    }
    
    /**
    * Construct a message from the msgObject using the format object
    * 
    * @returns String - the formatted string
    */
    private function buildMessage(format:Object,msgObj:Object):String {
        var ret:String;
        var colour:String;
        //Get the color for the level
        switch (msgObj.level) {
            case CRITICAL :
                colour='#ff0000';
                break;
            case GEN :
                colour='#0000ff';
                break;
            case COMP :
                colour='#00ff00';
                break;
            case HIGH :
                colour='#E3D209';
                break;
            case MED :
                colour='#CF1D92';
                break;
            default:
                colour='#555555';
        }

        //Build font tags
        var fontOpenTag:String = '<font color= "' + colour + '">'
        var fontCloseTag:String = '</font>';
		
		ret = fontOpenTag;
		
		//Add scope?
		if(msgObj.scope){
			ret += '*--- ' + msgObj.scope + ' ---*' +'<BR>' + ret;				
		}
		
        //Include date?
        if(format.date) {
            ret += 'date : ' + msgObj.date.toString() + ' ';
        } 
		
		//escape the message incase we asent XML in
		var theMsg = Object(StringUtils.escapeAngleBrackets(String(msgObj.msg)));
		
		//Add function/method + message
		ret += '[' + msgObj.func + ']' +  theMsg;
		
		//Add closing font tag and line break
		ret += fontCloseTag + '<BR>'		
        return ret;
    }
    
    /**
    */
    function get msgLog(){
        return _msgLog; 
		return _crashDumpMsgLog;
    }
    
}