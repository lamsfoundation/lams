import com.polymercode.Draw;

class com.polymercode.DrawMC extends MovieClip
{
	/**
	 *
	 */
	public function dashTo( x1:Number,y1:Number,x2:Number,y2:Number,
		dashLength:Number,spaceLength:Number):Void
	{
		Draw.dashTo(this, x1, y1, x2, y2, dashLength, spaceLength );
	}

	public function arcTo( x:Number, y:Number, radius:Number,
		arc:Number, startAngle:Number, yRadius:Number):Void
	{
		Draw.arcTo( this, x, y, radius, arc, startAngle, yRadius );
	}
	
	public function drawBurst( x:Number, y:Number, sides:Number, 
		innerRadius:Number, outerRadius:Number, angle:Number ):Void
	{
		Draw.burst(this,x,y,sides,innerRadius, outerRadius, angle );
	}

	public function drawGear(x:Number, y:Number, sides:Number, 
		innerRadius:Number, outerRadius:Number, angle:Number, 
		holeSides:Number, holeRadius:Number ):Void 
	{
		Draw.gear( this, x,y,sides,innerRadius,outerRadius,angle,
			holsSides, holeRadius);
	}

	public function drawOval(x:Number, y:Number, radius:Number, 
		yRadius:Number):Void
	{
		Draw.oval(this,x,y,radius,yRadius);
	}

	public function drawPolygon(x:Number, y:Number, sides:Number, 
		radius:Number, angle:Number):Void
	{
		Draw.polygon(this, x,y,sides,radius,angle);
	}

	public function drawRoundedRectangle(x:Number, y:Number, 
		width:Number, height:Number,cornerRadius:Number ):Void
	{
		Draw.roundedRectangle(this,x,y,width,height,cornerRadius);
	}
	
	public function drawStar( x:Number, y:Number,points:Number, 
		innerRadius:Number, outerRadius:Number, angle:Number ):Void
	{
		Draw.star(this,x,y,points,innerRadius,outerRadius,angle);
	}

	public function drawWedge(x:Number, y:Number, startAngle:Number, 
		arc:Number, radius:Number,yRadius:Number):Void
	{
		Draw.wedge(this,x,y,startAngle,arc,radius,yRadius);
	}
}