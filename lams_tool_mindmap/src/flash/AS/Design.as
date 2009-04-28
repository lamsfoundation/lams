package {
	import flash.display.*;
	import flash.events.*;
	import flash.external.*;
	import flash.geom.*;
	import flash.errors.*;
	import flash.utils.*;
	import zoomer.*;
	public class Design extends Sprite {
		protected static var MIN_SCALE:Number = 0.4;
		protected static var MAX_SCALE:Number = 2.5;
		protected var mindMap:MindMap;
		protected var frame:WhiteFrame;
		protected var navigationBar:Sprite;
		protected var navigate:Boolean;
		protected var moving:Boolean;
		protected var speedX:Number, speedY:Number;
		protected var indicator:ArrowHead;
		protected var toolTip:ToolTip;
		protected var connectionIcon:ConnectionIcon;
		protected var ground:Sprite;
		protected var zoomControl:Zoomer;
		protected var zooming:Number;
		public function Design(mindMap:MindMap, world:Stage) {
			this.ground = new Sprite();
			this.ground.graphics.beginFill(0,0);
			this.ground.graphics.drawRect(0,0,800,600);
			this.ground.graphics.endFill();
			this.addChild(this.ground);
			this.ground.addEventListener(MouseEvent.MOUSE_DOWN, this.onPressGround);
			this.ground.addEventListener(MouseEvent.MOUSE_UP, this.onReleaseGround);
			this.ground.addEventListener(MouseEvent.MOUSE_OUT, this.onLeaveGround);
			this.mindMap = mindMap;
			this.mindMap.x = 400;
			this.mindMap.y = 300;
			this.addChild(this.mindMap);
			this.mindMap.addEventListener(ServerMindMapEvent.WIN_INCOMING_CONNECTION, this.onWinServerResponse);
			this.mindMap.addEventListener(ServerMindMapEvent.FAILED_INCOMING_CONNECTION, this.onLostServerResponse);
			this.mindMap.addEventListener(ServerMindMapEvent.WIN_OUTGOING_CONNECTION, this.onWinServerRequest);
			this.mindMap.addEventListener(ServerMindMapEvent.FAILED_OUTGOING_CONNECTION, this.onLostServerRequest);
			this.frame = new WhiteFrame();
			this.frame.width = 800;
			this.frame.height = 600;
			this.frame.addEventListener(MouseEvent.MOUSE_OVER, onNavigationHover);
			this.frame.addEventListener(MouseEvent.MOUSE_OUT, onNavigationOut);
			this.indicator = new ArrowHead();
			this.indicator.addEventListener(MouseEvent.CLICK, this.onIndicatorClick);
			this.navigationBar = new Sprite();
			this.navigationBar.addChild(this.frame);
			this.navigationBar.addChild(this.indicator);
			this.addChild(this.navigationBar);
			this.connectionIcon = new ConnectionIcon();
			this.connectionIcon.y = 50;
			this.connectionIcon.x = 400;
			this.connectionIcon.scaleX = 0.7;
			this.connectionIcon.scaleY = 0.7;
			this.addChild(this.connectionIcon);
			this.toolTip = new ToolTip();
			this.toolTip.y = 600 - 16;
			this.navigationBar.addChild(this.toolTip);
			this.navigate = false;
			this.speedX = 0;
			this.speedY = 0;
			this.zoomControl = new Zoomer();
			this.zoomControl.scaleX = this.zoomControl.scaleY = 28/Math.max(this.zoomControl.width, this.zoomControl.height);
			this.addChild(this.zoomControl);
			this.zoomControl.scaleX = this.zoomControl.scaleY = 1.3;
			this.zoomControl.x = 400-this.zoomControl.width/2-this.zoomControl.getRect(this).left;
			this.zoomControl.y = 600-this.zoomControl.getRect(this).bottom;
			this.zoomControl.addEventListener(ZoomerEvent.ZOOM_IN, this.onStartZoomIn);
			this.zoomControl.addEventListener(ZoomerEvent.ZOOM_OUT, this.onStartZoomOut);
			this.zoomControl.addEventListener(ZoomerEvent.STOP_ZOOMING, this.onEndZooming);
			this.zooming = 0;
			this.addEventListener(Event.ENTER_FRAME, onFrame);
			world.addChild(this);
			this.mindMap.addEventListener(InformationEvent.INFORMATION, onGetInfo);
			this.mindMap.addEventListener(InformationEvent.NO_INFORMATION, onNoInfo);
			try{
				ExternalInterface.addCallback("getMindmap", this.toString);
			}catch(error:Error){
				
			}
		}
		protected function indicate():void {
			this.indicator.visible = true;
			var dx:Number, dy:Number;
			var tg:Number = (this.frame.width/2-this.mindMap.x)/(this.frame.height/2-this.mindMap.y);
			if(Math.abs(tg) <= this.frame.width/this.frame.height){
				dy = this.frame.height/2 * (this.mindMap.y>this.frame.height/2?1:-1);
				dx = tg*dy;
			}else{
				dx = this.frame.width/2 * (this.mindMap.x>this.frame.width/2?1:-1);
				if(tg == Number.POSITIVE_INFINITY){
					dy = 0;
				}else dy = dx/tg;
			}
			var px:Number, py:Number;
			px = this.frame.width/2+dx;
			py = this.frame.height/2+dy;
			var distance:Number = Point.distance(new Point(this.mindMap.x, this.mindMap.y), new Point(px, py));
			this.indicator.alpha = distance/500;
			var ax:Number, ay:Number;
			if(this.frame.height/2 != py){
				ay = 20*(this.frame.height/2-py)/Math.sqrt(Math.pow(this.frame.height/2-py,2)+Math.pow(this.frame.width/2-px,2)) + py;
				ax = (ay-py)*(this.frame.width/2-px)/(this.frame.height/2-py)+px;
			}else{
				ay = py;
				ax = px+20*(px<this.frame.width/2?1:-1);
			}
			this.indicator.x = ax;
			this.indicator.y = ay;
			this.indicator.rotation = 180 - Math.atan2(-this.indicator.y+this.frame.height/2, this.indicator.x-this.frame.width/2)*180/Math.PI;
		}
		protected function onGetInfo(event:InformationEvent):void {
			this.toolTip.show(event.message);
			this.toolTip.x = this.frame.width-this.toolTip.width;
		}
		protected function onNoInfo(event:InformationEvent):void {
			this.toolTip.hide();
		}
		protected function onNavigationHover(event:MouseEvent):void {
			this.navigate = true;
		}
		protected function onNavigationOut(event:MouseEvent):void {
			this.navigate = false;
		}
		protected function onFrame(event:Event):void {
			var slowDownX:Boolean = true;
			var slowDownY:Boolean = true;
			if (navigate) {
				var bounds:Rectangle = this.mindMap.getBounds(this.navigationBar);
				var maxSpeed:Number = 8;
				var minDistance:Number = 60;
				if (this.navigationBar.mouseX <= minDistance) {
					if(bounds.left<=100){
						slowDownX = false;
						if (Math.abs(this.speedX)<0.1) {
							this.speedX = 0.1;
						}
						this.speedX = 1.5 * Math.abs(this.speedX);
					}
				}
				if (this.navigationBar.mouseX >= this.frame.width-minDistance) {
					if(bounds.right>=this.frame.width-100){
						slowDownX = false;
						if (Math.abs(this.speedX)<0.1) {
							this.speedX = -0.1;
						}
						this.speedX = 1.5 * -Math.abs(this.speedX);
					}
				}
				if (this.navigationBar.mouseY <= minDistance) {
					if(bounds.top<=100){
						slowDownY = false;
						if (Math.abs(this.speedY)<0.1) {
							this.speedY = 0.1;
						}
						this.speedY = 1.5 * Math.abs(this.speedY);
					}
				}
				if (this.navigationBar.mouseY >= this.frame.height-minDistance) {
					if(bounds.bottom>=this.frame.height-100){
						slowDownY = false;
						if (Math.abs(this.speedY)<0.1) {
							this.speedY = -0.1;
						}
						this.speedY = 1.5 * -Math.abs(this.speedY);
					}
				}
			}
			if(slowDownX){
				this.speedX *= 0.8;
			}
			if(slowDownY){
				this.speedY *= 0.8;
			}
			if (Math.abs(this.speedX) < 0.001) {
				this.speedX = 0;
			}
			if (Math.abs(this.speedY) < 0.001) {
				this.speedY = 0;
			}
			if (Math.abs(this.speedX) > maxSpeed) {
				this.speedX = this.speedX/Math.abs(this.speedX)*maxSpeed;
			}
			if (Math.abs(this.speedY) > maxSpeed) {
				this.speedY = this.speedY/Math.abs(this.speedY)*maxSpeed;
			}
			if(this.speedX!=0 || this.speedY!=0){
				this.mindMap.x += this.speedX;
				this.mindMap.y += this.speedY;
			}
			if(this.zooming!=0){
				var oldScale:Number = this.mindMap.scaleY;
				this.mindMap.scaleX = this.mindMap.scaleY = Math.min(Math.max(MIN_SCALE, this.mindMap.scaleY*(1+this.zooming*0.05)), MAX_SCALE);
				this.mindMap.x = (this.mindMap.x-400)*(this.mindMap.scaleY/oldScale)+400;
				this.mindMap.y = (this.mindMap.y-300)*(this.mindMap.scaleY/oldScale)+300;
			}
			if(this.mindMap.x<0 || this.mindMap.x>this.frame.width || this.mindMap.y<0 || this.mindMap.y>this.frame.height){
				this.indicate();
			}else{
				this.indicator.visible = false;
			}
		}
		protected function onIndicatorClick(event:MouseEvent):void {
			this.mindMap.x = 400;
			this.mindMap.y = 300;
		}
		protected function onPressGround(event:MouseEvent):void {
			this.mindMap.startDrag();
		}
		protected function onReleaseGround(event:MouseEvent):void {
			this.mindMap.stopDrag();
		}
		protected function onLeaveGround(event:MouseEvent):void {
			this.mindMap.stopDrag();
		}
		protected function onStartZoomIn(event:ZoomerEvent):void {
			this.zooming = 1;
		}
		protected function onStartZoomOut(event:ZoomerEvent):void {
			this.zooming = -1;
		}
		protected function onEndZooming(event:ZoomerEvent):void {
			this.zooming = 0;
		}
		protected function onWinServerResponse(event:ServerMindMapEvent):void {
			this.connectionIcon.incoming = true;
		}
		protected function onLostServerResponse(event:ServerMindMapEvent):void {
			this.connectionIcon.incoming = false;
		}
		protected function onWinServerRequest(event:ServerMindMapEvent):void {
			this.connectionIcon.outgoing = true;
		}
		protected function onLostServerRequest(event:ServerMindMapEvent):void {
			this.connectionIcon.outgoing = false;
		}
		override public function toString():String {
			return this.mindMap.toXml().toXMLString().replace(/\\/g, "\\\\");
		}
	}
}