import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
/**  
* MovieclipUtils  
*/  
class MovieClipUtils {  
  
  //Declarations  
  //Constructor  
  function MovieClipUtils() {  

  }  
	/**  
	* Checks if one mc intersects or overlaps another by comparing the x&y co-ords
	* @param a_mc target movie clip
	* @param b_mc the movie clip that may be interesceting a_mc
	*/  
	public static function LFHitTest(a_mc:MovieClip,b_mc:MovieClip):Boolean{
		trace("Method not implemented");
		return false;
	}

	public static function getCenterOfMC(mc:MovieClip):Point{
		var x:Number = mc._x + (mc._width / 2);
		var y:Number = mc._y + (mc._height / 2);
		var p:Point = new Point(x,y);
		return p;
	
	}

    /**
    * Schedules a function to be executed after one frame
    * @usage
    *       import org.lamsfoundation.lams.common.util.*
    *       doLater(Proxy.create(<scope>,<fn>,arg1,arg2.....);
	*/
	public static function doLater(fn:Function):Void{
        //Create the clip and attach to root at next available depth
		var doLater_mc:MovieClip = _root.createEmptyMovieClip('LFDoLater_mc',_root.getNextHighestDepth());
        //Assign function to clip and set up onEnterFrame
        doLater_mc.fn = fn;
        doLater_mc.onEnterFrame = function () {
            //trace('doLater.onEnterFrame');
            //Call the fn, kill the enterframe and remove the clip
            fn.apply();
            delete this.onEnterFrame;
            this.removeMovieClip();
        }
	}
	



}