import org.lamsfoundation.lams.common.ui.*

/**
* Grid  - For positioning and calulcating relative to a grid in canvas
* @author   DI
*
*/ 

class Grid {
	//constants
	private static var LINE_THICKNESS:Number = 1;				//lines
	private static var LINE_ALPHA:Number = 30;
	private static var LINE_COLOR:Number = 0x000000;

	public static var SMALL_GRID_H_SPACING:Number = 21;				           //Small grid dimensions
	public static var SMALL_GRID_V_SPACING:Number = 28;
	public static var LARGE_GRID_H_SPACING:Number = 5 * SMALL_GRID_H_SPACING;  //Large grid dimensions
	public static var LARGE_GRID_V_SPACING:Number = 5 * SMALL_GRID_V_SPACING;
		 
	//Vars
    private var gridWidth:Number;
    private var gridHeight:Number
	
	/**
    * Constructor
    */
	function Grid(){
	}
    
	
	/**
    * Calculates screen x&y from grid coords
    */
	public static function pixelsToLargeGrid(x:Number,y:Number):Point{
		var point:Point = new Point(x,y);
		point.x *= LARGE_GRID_H_SPACING;
		point.y *= LARGE_GRID_V_SPACING;
		return point;
	}
	
	/**
    * Calculates screen x&y from grid coords
    */ 
	public static function largeGridToPixels(x:Number,y:Number):Point{
		var point:Point = new Point(x,y);
		point.x /= LARGE_GRID_H_SPACING;
		point.y /= LARGE_GRID_V_SPACING;
		return point;
	}
	
	/**
    * Calculate pixels for small grid coords
    */
	public static function smallGridToPixels(x:Number,y:Number):Point{
		var point:Point = new Point(x,y);
		point.x *= SMALL_GRID_H_SPACING;
		point.y *= SMALL_GRID_V_SPACING;
		return point;
	}

	/**
    * Calculate small grid coords from pixels
    */
	public static function pixelsToSmallGrid(x:Number,y:Number):Point{
		var point:Point = new Point(x,y);
		point.x *= SMALL_GRID_H_SPACING;
		point.y *= SMALL_GRID_V_SPACING;
		return point;
	}
	 
	/**
    * Draw the grid
    */
	public static function drawGrid(target_mc,gridWidth,gridHeight,hGap,vGap):Object{
		//if the gird already exists - remove it
		if(target_mc.grid != null){
			target_mc.grid.removeMovieClip();
		}
  		//Create the clip
		var _mc:MovieClip = target_mc.createEmptyMovieClip('grid',target_mc.getNextHighestDepth());

		if (target_mc==undefined){
			//first make sure that the reference is valid
			return false;
		}
		//draw horizontal lines
		var hLines:Number = Math.floor(gridWidth/hGap);
		_mc.lineStyle(LINE_THICKNESS,LINE_COLOR,LINE_ALPHA);
		for (var i=0; i<= hLines;i++){
			var x:Number = i*hGap;
			_mc.moveTo(x,0);
			_mc.lineTo(x,gridHeight);
		}
		//draw vertical lines
		var vLines:Number = Math.floor(gridHeight/vGap);
		for (i=0; i<= vLines;i++){
			var y = i*vGap;
			_mc.moveTo(0,y);
			_mc.lineTo(gridWidth,y);
		}
		return _mc;
	}

}
 
 