package {
	import flash.display.*;
	import flash.events.*;
	import flash.external.*;
	import flash.geom.*;
	import flash.errors.*;
	import flash.utils.*;
	public class Design extends Sprite {
		protected var mindMap:MindMap;
		protected var frame:WhiteFrame;
		protected var edgeOutline:Sprite;
		protected var navigationBar:Sprite;
		protected var navigate:Boolean;
		protected var moving:Boolean;
		protected var speedX:Number, speedY:Number;
		protected var indicator:ArrowHead;
		protected var toolTip:ToolTip;
		protected var connectionIcon:ConnectionIcon;
		protected var ground:Sprite;
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
			this.navigationBar = new Sprite();
			this.navigationBar.addChild(this.frame);
			this.navigationBar.addChild(this.indicator);
			this.edgeOutline = new Sprite();
			this.edgeOutline.graphics.lineStyle(2, 0x888888);
			this.edgeOutline.graphics.drawRect(0, 0, 800, 600);
			this.navigationBar.addChild(this.edgeOutline);
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
			this.addEventListener(Event.ENTER_FRAME, onFrame);
			world.addChild(this);
			this.mindMap.addEventListener(InformationEvent.INFORMATION, onGetInfo);
			this.mindMap.addEventListener(InformationEvent.NO_INFORMATION, onNoInfo);
			try{
				ExternalInterface.addCallback("getMindmap", this.toString);
			}catch(error:Error){
			}
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
				if(!slowDownY || !slowDownX){
					this.edgeOutline.visible = true;
				}else{
					this.edgeOutline.visible = false;
				}
				this.mindMap.x += this.speedX;
				this.mindMap.y += this.speedY;
				if(this.mindMap.x<0 || this.mindMap.x>this.frame.width || this.mindMap.y<0 || this.mindMap.y>this.frame.height){
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
				}else{
					this.indicator.visible = false;
				}
			}else{
				this.edgeOutline.visible = false;
			}
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