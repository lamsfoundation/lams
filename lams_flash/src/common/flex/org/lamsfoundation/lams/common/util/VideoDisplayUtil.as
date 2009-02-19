package org.lamsfoundation.lams.common.util
{
	import flash.media.Camera;
	import flash.media.Microphone;
	
	public class VideoDisplayUtil
	{
		public function VideoDisplayUtil() {}
		
		// prints an info object
	 	public static function printInfoObject(caller:String, infoObject:Object):void{
        	trace("caller: " + caller);
       		for (var prop in infoObject) {
    			trace("\t"+prop+":\t"+infoObject[prop]);
			}
        }
	    
	    // create a filename for the video recorder tool
       	public static function createFilename(toolSessionId:int, userId:int):String{
       		
			return "lamsRecording_session" + toolSessionId + "_user" + userId + "_" + randomNumber(1, 1000000000);
       	}
       	
       	// create a filename for use in the fck editor
       	public static function createFilenameForFCK(userId:int):String{
       		
			return "lamsRecording_forFCK" + "_user" + userId + "_" + randomNumber(1, 1000000000);
       	}
       	
       	public static function createFilenameForAuthor(toolContentId:int):String{
       		
       		return "lamsRecording_tool" + toolContentId + "_" + randomNumber(1, 1000000000);
       	}
	       
	   	// creates a random number between bounds
       	private static function randomNumber(low:Number=NaN, high:Number=NaN):Number {
		  var low:Number = low;
		  var high:Number = high;
		
		  if(isNaN(low))
		  {
		    throw new Error("low must be defined");
		  }
		  if(isNaN(high))
		  {
		    throw new Error("high must be defined");
		  }
		
		  return Math.round(Math.random() * (high - low)) + low;
		}

		// gets a camera and sets it up
		public static function setupCamera():Camera {
			// get cam
			var cam:Camera = new Camera;
			cam = Camera.getCamera();
				
			// setting dimensions and framerate
			cam.setMode(320, 240, 30);
			// set to minimum of 70% quality
			cam.setQuality(0,70);
			
			return cam;
		}
			
		// sets up mic
		public static function setupMic():Microphone{
			// get mic
			var mic:Microphone = new Microphone();
			mic = Microphone.getMicrophone();
				
			// setup rate
			mic.rate = 44;
			
			return mic;
		}
		
		// creates a string with the correct time formatting
		public static function secondsToString(val:String):String{
			var valInt:Number = Number(val);
			var minutes:int = valInt / 60;
			var seconds:int = valInt % 60;
			
			return String(minutes) + ":" + String(seconds);
		}
	}
}