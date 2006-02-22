/**
 * Draw is a static toolbox class that provides a number of functions
 * to draw shapes that are not part of the standard ActionScript Drawing
 * API.
 * 
 * To call this code from the timeline you must include this class in the
 * classpath in the publish settings menu.
 * 
 * File -> Publish Settings (Ctrl+Shift+F12 on windows)
 *   - Click "Flash" tab
 *   - Set ActionScript Version to "ActionScript 2.0" or higher...
 *   - Click "Settings"
 * 		- click the target icon and select the directory you've extracted
 *      - this code into.  You should select the directory that has
 * 		- the <em>com</em> directory in it.  <em>NOT</em> the directory
 * 		- that has the Draw.as file.
 * 
 * based on source code found at:
 * http://www.macromedia.com/devnet/mx/flash/articles/adv_draw_methods.html
 * 
 * @author Ric Ewing - version 1.4 - 4.7.2002
 * @author Kevin Williams - version 2.0 - 4.7.2005
 */
class com.polymercode.Draw
{
	/**
	 * 
	 * This class has all static functions.  To call them simply call
	 * com.polymercode.Draw.functionName(parameters); 
	 * 
	 * You should never have to call new Draw();
	 * 
	 */
	private function Draw(){}
	
	/**
	 * dashTo
	 * Draws a dashed line from the point x1,y1 to the point x2,y2
	 * The line may is drawn on the movie clips specified by the 
	 * <em>target</em> parameter (default is _root).  The line may 
	 * be any color or transparency and width, length of dashes, 
	 * and space between dashes may all be specified.
	 * 
	 * @param target MovieClip the movie clip on which the dashed 
	 * line will be drawn.
	 * @param x1 Number starting position on x axis - <strong></strong>required</strong>
	 * @param y1 Number starting position on y axis - <strong></strong>required</strong>
	 * @param x2 Number finishing position on x axis - <strong></strong>required</strong>
	 * @param y2 Number finishing position on y axis - <strong></strong>required</strong>
	 * @param dashLength [optional] Number the number of pixels long each dash 
	 * will be.  Default = 5
	 * @param spaceLength [optional] Number the number of pixels between each 
	 * dash.  Default = 5
	 * @param strokWidth [optional] Number the width of each dash. 
	 * Default = 1
	 * @param rgbHex [optional] the Hex color value of the dashed line.  
	 * Default = 0x000000
	 * @param alpha [optional] Number the alpha transparancy that the dashed 
	 * line will have. Default = 100;
	 */
	public static function dashTo(target:MovieClip, x1:Number, 
		y1:Number,x2:Number, y2:Number, dashLength:Number, 
		spaceLength:Number,strokeWidth:Number,rgbHex:Number ):Void
	{
		trace("com.polymercode.Draw.dashTo - arguments:"+arguments.toString());
		if ( arguments < 5 ){
			trace("com.polymercode.Draw.dashTo - too few parameters.");
			return;
		}
		if ( spaceLength == undefined )
			spaceLength = 5;
		if ( dashLength == undefined )
			dashLength = 5;
			
		//added to this
		target.lineStyle(strokeWidth,rgbHex);
		
		var x = x2 - x1;
		var y = y2 - y1;
		var hyp = Math.sqrt((x)*(x) + (y)*(y));
		var units = hyp/(dashLength+spaceLength);
		var dashSpaceRatio = dashLength/(dashLength+spaceLength);
		var dashX = (x/units)*dashSpaceRatio;
		var spaceX = (x/units)-dashX;
		var dashY = (y/units)*dashSpaceRatio;
		var spaceY = (y/units)-dashY;
		target.moveTo(x1, y1);
		while (hyp > 0) {
			x1 += dashX;
			y1 += dashY;
			hyp -= dashLength;
			if (hyp < 0) {
			   x1 = x2;
			   y1 = y2;
			}
			target.lineTo(x1, y1);
			x1 += spaceX;
			y1 += spaceY;
			target.moveTo(x1, y1);
			hyp -= spaceLength;
		}
		target.moveTo(x2, y2);
	}
	

	/**
	 * Draws an arc from the starting position of x,y.
	 * 
	 * @param target the movie clip that the Arc is drawn on.
	 * @param x x coordinate of the starting pen position
	 * @param y y coordinate of the starting pen position 
	 * @param radius radius of Arc.
	 * @param arc = sweep of the arc. Negative values draw clockwise.
	 * @param startAngle = [optional] starting offset angle in degrees.
	 * @param yRadius = [optional] y radius of arc. if different than 
	 * radius, then the arc will draw as the arc of an oval.  
	 * default = radius.
	 *
	 * Based on mc.drawArc by Ric Ewing.
	 * the version by Ric assumes that the pen is at x:y before this
	 * method is called.  I explictily move the pen to x:y to be 
	 * consistent with the behaviour of the other methods.
	 */
	public static function arcTo(target:MovieClip,  x:Number, 
		y:Number, radius:Number, arc:Number, startAngle:Number, 
		yRadius:Number):Void
	{
		if (arguments.length<5) {
			trace("com.polymercode.Draw.arcTo - too few parameters.");
			return;
		}
		
		// if startAngle is undefined, startAngle = 0
		if( startAngle == undefined ){
			startAngle = 0;
		}
		// if yRadius is undefined, yRadius = radius
		if (yRadius == undefined) {
			yRadius = radius;
		}
		
		// Init vars
		var segAngle, theta, angle, angleMid, segs, ax, ay, bx, by, cx, cy;
		// no sense in drawing more than is needed :)
		if (Math.abs(arc)>360) {
			arc = 360;
		}
		// Flash uses 8 segments per circle, to match that, we draw in a maximum
		// of 45 degree segments. First we calculate how many segments are needed
		// for our arc.
		segs = Math.ceil(Math.abs(arc)/45);
		// Now calculate the sweep of each segment
		segAngle = arc/segs;
		// The math requires radians rather than degrees. To convert from degrees
		// use the formula (degrees/180)*Math.PI to get radians. 
		theta = -(segAngle/180)*Math.PI;
		// convert angle startAngle to radians
		angle = -(startAngle/180)*Math.PI;
		// find our starting points (ax,ay) relative to the secified x,y
		ax = x-Math.cos(angle)*radius;
		ay = y-Math.sin(angle)*yRadius;
		// if our arc is larger than 45 degrees, draw as 45 degree segments
		// so that we match Flash's native circle routines.
		if (segs>0) {
			target.moveTo(x,y);
			// Loop for drawing arc segments
			for (var i = 0; i<segs; i++) {
				// increment our angle
				angle += theta;
				// find the angle halfway between the last angle and the new
				angleMid = angle-(theta/2);
				// calculate our end point
				bx = ax+Math.cos(angle)*radius;
				by = ay+Math.sin(angle)*yRadius;
				// calculate our control point
				cx = ax+Math.cos(angleMid)*(radius/Math.cos(theta/2));
				cy = ay+Math.sin(angleMid)*(yRadius/Math.cos(theta/2));
				// draw the arc segment
				target.curveTo(cx, cy, bx, by);
			}
		}
	}
	
	/**
	 * Burst is a method for drawing star bursts.  If you've ever worked
	 * with an advertising department, you know what they are ;-)
	 * Clients tend to want them, Developers tend to hate them...
	 *
	 * @param target MovieClip where the Burst is to be drawn.
	 * @param x x coordinate of the center of the burst
	 * @param y y coordinate of the center of the burst
	 * @param sides number of sides or points
	 * @param innerRadius radius of the indent of the curves
	 * @param outerRadius radius of the outermost points
	 * @param angle [optional] starting angle in degrees. (defaults to 0)
	 * 
	 * based on mc.drawBurst() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function burst(target:MovieClip, x:Number, y:Number,
		sides:Number, innerRadius:Number, outerRadius:Number, 
		angle:Number ):Void
	{
		if(arguments<6){
			trace("com.polymercode.Draw.burst - too few parameters");
			return;
		}
		if ( angle == undefined )
			angle = 0;

		if (sides>2) {
			// init vars
			var step, halfStep, qtrStep, start, n, dx, dy, cx, cy;
			// calculate length of sides
			step = (Math.PI*2)/sides;
			halfStep = step/2;
			qtrStep = step/4;
			// calculate starting angle in radians
			start = (angle/180)*Math.PI;
			target.moveTo(x+(Math.cos(start)*outerRadius), y-(Math.sin(start)*outerRadius));
			// draw curves
			for (n=1; n<=sides; n++) {
				cx = x+Math.cos(start+(step*n)-(qtrStep*3))*(innerRadius/Math.cos(qtrStep));
				cy = y-Math.sin(start+(step*n)-(qtrStep*3))*(innerRadius/Math.cos(qtrStep));
				dx = x+Math.cos(start+(step*n)-halfStep)*innerRadius;
				dy = y-Math.sin(start+(step*n)-halfStep)*innerRadius;
				target.curveTo(cx, cy, dx, dy);
				cx = x+Math.cos(start+(step*n)-qtrStep)*(innerRadius/Math.cos(qtrStep));
				cy = y-Math.sin(start+(step*n)-qtrStep)*(innerRadius/Math.cos(qtrStep));
				dx = x+Math.cos(start+(step*n))*outerRadius;
				dy = y-Math.sin(start+(step*n))*outerRadius;
				target.curveTo(cx, cy, dx, dy);
			}
		}
	}
	
	/**
	 * draws a gear shape on the MovieClip target.  The gear position 
	 * is indicated by the x and y arguments.
	 * 
	 * @param target MovieClip on which the gear is to be drawn.
	 * @param x x coordinate of the center of the gear
	 * @param y y coordinate of the center of the gear
	 * @param sides number of teeth on gear. (must be > 2)
	 * @param innerRadius radius of the indent of the teeth.
	 * @param outerRadius outer radius of the teeth.
	 * @param angle = [optional] starting angle in degrees. Defaults to 0.
	 * @param holeSides [optional] draw a polygonal hole with this many sides (must be > 2)
	 * @param holeRadius [optional] size of hole. Default = innerRadius/3.
	 * 
	 * based on mc.drawGear() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function gear(target:MovieClip, x:Number, y:Number, 
		sides:Number, innerRadius:Number, outerRadius:Number, 
		angle:Number, holeSides:Number, holeRadius:Number ):Void 
	{
		if(arguments<6){
			trace("com.polymercode.Draw.gear - too few parameters");
			return;
		}
		if ( angle == undefined )
			angle = 0;
		if ( holeSides == undefined )
			holeSides = 0;

		if (sides>2) {
			// init vars
			var step, qtrStep, start, n, dx, dy;
			// calculate length of sides
			step = (Math.PI*2)/sides;
			qtrStep = step/4;
			// calculate starting angle in radians
			start = (angle/180)*Math.PI;
			target.moveTo(x+(Math.cos(start)*outerRadius), y-(Math.sin(start)*outerRadius));
			// draw lines
			for (n=1; n<=sides; n++) {
				dx = x+Math.cos(start+(step*n)-(qtrStep*3))*innerRadius;
				dy = y-Math.sin(start+(step*n)-(qtrStep*3))*innerRadius;
				target.lineTo(dx, dy);
				dx = x+Math.cos(start+(step*n)-(qtrStep*2))*innerRadius;
				dy = y-Math.sin(start+(step*n)-(qtrStep*2))*innerRadius;
				target.lineTo(dx, dy);
				dx = x+Math.cos(start+(step*n)-qtrStep)*outerRadius;
				dy = y-Math.sin(start+(step*n)-qtrStep)*outerRadius;
				target.lineTo(dx, dy);
				dx = x+Math.cos(start+(step*n))*outerRadius;
				dy = y-Math.sin(start+(step*n))*outerRadius;
				target.lineTo(dx, dy);
			}
			// This is complete overkill... but I had it done already. :)
			if (holeSides>2) {
				if(holeRadius == undefined) {
					holeRadius = innerRadius/3;
				}
				step = (Math.PI*2)/holeSides;
				target.moveTo(x+(Math.cos(start)*holeRadius), y-(Math.sin(start)*holeRadius));
				for (n=1; n<=holeSides; n++) {
					dx = x+Math.cos(start+(step*n))*holeRadius;
					dy = y-Math.sin(start+(step*n))*holeRadius;
					target.lineTo(dx, dy);
				}
			}
		}
	}
	
	/**
	 * oval draws used to draw circles or ovals.  
	 * According to Ric Ewing - <blockquote>This method, like most of 
	 * the others, is not as optimized as it could be. This was a
	 * conscious decision to keep the code as accessible as
	 * possible for those either new to AS or to the math
	 * involved in plotting points on a curve.</blockquote>
	 * 
	 * @param target the MovieClip that the oval/circle is drawn on.
	 * @param x x coordinate of the center of the oval/circle
	 * @param y y coordinate of the center of the oval/circle
	 * @param radius the radius of the oval/circle
	 * @param yRadius [optional] y radius of the oval.  if not defined
	 * then yRadius = radius and therefore, it draws a circle.
	 * 
	 * based on mc.drawOval() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function oval(target:MovieClip, x:Number, y:Number, 
			radius:Number, yRadius:Number):Void
	{
		if (arguments.length<4)
		{
			trace("com.polymercode.Draw.oval - too few parameters");
			return;
		}
		// if only yRadius is undefined, yRadius = radius
		if (yRadius == undefined) {
			yRadius = radius;
		}
			
		// init variables
		var theta, xrCtrl, yrCtrl, angle, angleMid, px, py, cx, cy;
		// covert 45 degrees to radians for our calculations
		theta = Math.PI/4;
		// calculate the distance for the control point
		xrCtrl = radius/Math.cos(theta/2);
		yrCtrl = yRadius/Math.cos(theta/2);
		// start on the right side of the circle
		angle = 0;
		target.moveTo(x+radius, y);
		// this loop draws the circle in 8 segments
		for (var i = 0; i<8; i++) {
			// increment our angles
			angle += theta;
			angleMid = angle-(theta/2);
			// calculate our control point
			cx = x+Math.cos(angleMid)*xrCtrl;
			cy = y+Math.sin(angleMid)*yrCtrl;
			// calculate our end point
			px = x+Math.cos(angle)*radius;
			py = y+Math.sin(angle)*yRadius;
			// draw the circle segment
			target.curveTo(cx, cy, px, py);
		}
	}

	/**
	 * a method for creating polygon shapes.  Negative values will draw 
	 * the polygon in reverse direction.  Negative drawing may be useful 
	 * for creating knock-outs in masks.
	 * 
	 * @param target the MovieClip that the polygon is to be drawn on
	 * @param x x coordinate of the center of the polygon
	 * @param y y coordinate of the center of the polygon
	 * @param sides the number of sides (must be > 2)
	 * @param radius the radius from the center point to the points
	 * on the polygon
	 * @param angle [optional] the starting offset angle (degrees) from
	 * 0. Default = 0
	 * 
	 * based on mc.drawPoly() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function polygon(target:MovieClip, x:Number, 
		y:Number, sides:Number, radius:Number, angle:Number):Void 
	{
		if (arguments.length<5)
		{
			trace("com.polymercode.Draw.polygon - too few parameters");
			return;
		}
		if ( angle == undefined )
			angle = 0;
		
		// convert sides to positive value
		var count = Math.abs(sides);
		// check that count is sufficient to build polygon
		if (count>2) {
			// init vars
			var step, start, n, dx, dy;
			// calculate span of sides
			step = (Math.PI*2)/sides;
			// calculate starting angle in radians
			start = (angle/180)*Math.PI;
			target.moveTo(x+(Math.cos(start)*radius), y-(Math.sin(start)*radius));
			// draw the polygon
			for (n=1; n<=count; n++) {
				dx = x+Math.cos(start+(step*n))*radius;
				dy = y-Math.sin(start+(step*n))*radius;
				target.lineTo(dx, dy);
			}
		}
	}
	
	/**
	 * roundedRectangle draws rectangles with rounded corners.
	 * This is similar to the Flash rectangle tool. If the
	 * rectangle is smaller in either dimension than the
	 * rounding permits then rounding is scaled down to fit
	 * 
	 * @param target the MovieClip that the rouded rectangle is to be 
	 * drawn on.
	 * @param x x coordinate of the top left corner of the rectangle
	 * @param y y coordinate of the top right corner of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param cornerRadius the radius of the rounding of the corners of
	 * the rectangle. [optional]
	 * 
	 * based on mc.drawRect() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function roundedRectangle(target:MovieClip, 
			x:Number, y:Number, width:Number, height:Number, 
			cornerRadius:Number ):Void
	{
		if ( arguments.length<5 ) 
		{
			trace("com.polymercode.Draw.roundedRectangle - too few parameters");
			return;
		}
		if ( cornerRadius == undefined )
			cornerRadius = 0;
		
		// if the user has defined cornerRadius our task is a bit more complex. :)
		if (cornerRadius>0) {
			// init vars
			var theta, angle, cx, cy, px, py;
			// make sure that w + h are larger than 2*cornerRadius
			if (cornerRadius>Math.min(width, height)/2) {
				cornerRadius = Math.min(width, height)/2;
			}
			// theta = 45 degrees in radians
			theta = Math.PI/4;
			// draw top line
			target.moveTo(x+cornerRadius, y);
			target.lineTo(x+width-cornerRadius, y);
			//angle is currently 90 degrees
			angle = -Math.PI/2;
			// draw tr corner in two parts
			cx = x+width-cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+width-cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			
			target.curveTo(cx, cy, px, py);
			angle += theta;
			cx = x+width-cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+width-cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
			// draw right line
			target.lineTo(x+width, y+height-cornerRadius);
			// draw br corner
			angle += theta;
			cx = x+width-cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+height-cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+width-cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+height-cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
			angle += theta;
			cx = x+width-cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+height-cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+width-cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+height-cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
			// draw bottom line
			target.lineTo(x+cornerRadius, y+height);
			// draw bl corner
			angle += theta;
			cx = x+cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+height-cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+height-cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
			angle += theta;
			cx = x+cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+height-cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+height-cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
			// draw left line
			target.lineTo(x, y+cornerRadius);
			// draw tl corner
			angle += theta;
			cx = x+cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
			angle += theta;
			cx = x+cornerRadius+(Math.cos(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			cy = y+cornerRadius+(Math.sin(angle+(theta/2))*cornerRadius/Math.cos(theta/2));
			px = x+cornerRadius+(Math.cos(angle+theta)*cornerRadius);
			py = y+cornerRadius+(Math.sin(angle+theta)*cornerRadius);
			target.curveTo(cx, cy, px, py);
		} else {
			// cornerRadius was not defined or = 0. This makes it easy.
			target.moveTo(x, y);
			target.lineTo(x+width, y);
			target.lineTo(x+width, y+height);
			target.lineTo(x, y+height);
			target.lineTo(x, y);
		}
	}
	
	/**
	 * start draws a star shaped polygon.
	 * 
	 * <blockquote>Note that the stars by default 'point' to
	 * the right. This is because the method starts drawing
	 * at 0 degrees by default, putting the first point to
	 * the right of center. Negative values for points
	 * draws the star in reverse direction, allowing for
	 * knock-outs when used as part of a mask.</blockquote>
	 *  
	 * @param x x coordinate of the center of the star
	 * @param y y coordinate of the center of the star
	 * @param points the number of points on the star
	 * @param innerRadius the radius of the inside angles of the star
	 * @param outerRadius the radius of the outside angles of the star
	 * @param angle [optional] the offet angle that the start is rotated
	 * @param target the MovieClip that the star is drawn on
	 * 
	 * based on mc.drawStar() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function star(target:MovieClip, x:Number, y:Number, 
		points:Number, innerRadius:Number, outerRadius:Number, 
		angle:Number ):Void
	{
		if( arguments.length < 6)
		{
			trace("com.polymercode.Draw.star - too few parameters");
			return;
		}
		if( angle == undefined )
			angle = 0;
		
		var count = Math.abs(points);
		if (count>2) {
			// init vars
			var step, halfStep, start, n, dx, dy;
			// calculate distance between points
			step = (Math.PI*2)/points;
			halfStep = step/2;
			// calculate starting angle in radians
			start = (angle/180)*Math.PI;
			target.moveTo(x+(Math.cos(start)*outerRadius), y-(Math.sin(start)*outerRadius));
			// draw lines
			for (n=1; n<=count; n++) {
				dx = x+Math.cos(start+(step*n)-halfStep)*innerRadius;
				dy = y-Math.sin(start+(step*n)-halfStep)*innerRadius;
				target.lineTo(dx, dy);
				dx = x+Math.cos(start+(step*n))*outerRadius;
				dy = y-Math.sin(start+(step*n))*outerRadius;
				target.lineTo(dx, dy);
			}
		}
	}

	/**
	 * draws pie shaped wedges.  Could be employeed to draw pie charts.
	 * 
	 * @param target the MovieClip on which the wedge is to be drawn.
	 * @param x x coordinate of the center point of the wedge
	 * @param y y coordinate of the center point of the wedge
	 * @param startAngle the starting angle in degrees
	 * @param arc the sweep of the wedge. negative values draw clockwise
	 * @param radius the radius of the wedge
	 * @param yRadius [optional] the y axis radius of the wedge.  
	 * If not defined, then yRadius = radius.
	 * 
	 * based on mc.drawWedge() - by Ric Ewing (ric@formequalsfunction.com) - version 1.4 - 4.7.2002
	 */
	public static function wedge(target:MovieClip, x:Number, y:Number, 
			startAngle:Number, arc:Number, radius:Number, 
			yRadius:Number):Void
	{
		if (arguments.length<5) {
			trace("com.polymercode.Draw.wedge - too few parameters");
			return;
		}
		// move to x,y position
		target.moveTo(x, y);
		// if yRadius is undefined, yRadius = radius
		if (yRadius == undefined) {
			yRadius = radius;
		}
		// Init vars
		var segAngle, theta, angle, angleMid, segs, ax, ay, bx, by, cx, cy;
		// limit sweep to reasonable numbers
		if (Math.abs(arc)>360) {
			arc = 360;
		}
		// Flash uses 8 segments per circle, to match that, we draw in a maximum
		// of 45 degree segments. First we calculate how many segments are needed
		// for our arc.
		segs = Math.ceil(Math.abs(arc)/45);
		// Now calculate the sweep of each segment.
		segAngle = arc/segs;
		// The math requires radians rather than degrees. To convert from degrees
		// use the formula (degrees/180)*Math.PI to get radians.
			theta = -(segAngle/180)*Math.PI;
		// convert angle startAngle to radians
		angle = -(startAngle/180)*Math.PI;
		// draw the curve in segments no larger than 45 degrees.
		if (segs>0) {
			// draw a line from the center to the start of the curve
			ax = x+Math.cos(startAngle/180*Math.PI)*radius;
			ay = y+Math.sin(-startAngle/180*Math.PI)*yRadius;
			target.lineTo(ax, ay);
			// Loop for drawing curve segments
			for (var i = 0; i<segs; i++) {
				angle += theta;
				angleMid = angle-(theta/2);
				bx = x+Math.cos(angle)*radius;
				by = y+Math.sin(angle)*yRadius;
				cx = x+Math.cos(angleMid)*(radius/Math.cos(theta/2));
				cy = y+Math.sin(angleMid)*(yRadius/Math.cos(theta/2));
				target.curveTo(cx, cy, bx, by);
			}
			// close the wedge by drawing a line to the center
			target.lineTo(x, y);
		}
	}
}