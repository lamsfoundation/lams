package {
	import fl.motion.*;
	public class BasicColor extends Color{
		public function get R():Number {
			return this.redOffset/255;
		}
		public function set R(value:Number){
			if(value<0)value=0;
			if(value>1)value=1;
			this.redOffset = (int)(value*255);
		}
		public function get G():Number {
			return this.greenOffset/255;
		}
		public function set G(value:Number){
			if(value<0)value=0;
			if(value>1)value=1;
			this.greenOffset = (int)(value*255);
		}
		public function get B():Number {
			return this.blueOffset/255;
		}
		public function set B(value:Number){
			if(value<0)value=0;
			if(value>1)value=1;
			this.blueOffset = (int)(value*255);
		}
		public function get H():Number {
			if(this.maxShade == this.minShade) return 0;
			switch(this.maxShade){
				case this.R:
					if(this.B>this.G) return ((this.G-this.B)/(this.maxShade-this.minShade)+6)/6;
					else return (this.G-this.B)/(this.maxShade-this.minShade)/6;
				case this.G:
					return (2+(this.B-this.R)/(this.maxShade-this.minShade))/6;
				case this.B:
					return (4+(this.R-this.G)/(this.maxShade-this.minShade))/6;
			}
			return 0;
		}
		public function set H(value:Number){
			if(value<0)value=0;
			if(value>1)value=1;
			this.setParameters(value, this.S, this.L);
		}
		public function get S():Number{
			if(this.maxShade == this.minShade) return 0;
			if(this.L<0.5){
				return (this.maxShade-this.minShade)/(this.maxShade+this.minShade);
			}else{
				return (this.maxShade-this.minShade)/(2-this.maxShade-this.minShade);
			}
		}
		public function set S(value:Number){
			if(value<0)value=0;
			if(value>1)value=1;
			this.setParameters(this.H, value, this.L);
		}
		public function get L():Number{
			return (this.maxShade+this.minShade)/2;
		}
		public function set L(value:Number){
			if(value<0)value=0;
			if(value>1)value=1;
			this.setParameters(this.H, this.S, value);
		}
		protected function get minShade():Number {
			return Math.min(this.R,this.G,this.B);
		}
		protected function get maxShade():Number {
			return Math.max(this.R,this.G,this.B);
		}
		public function BasicColor(R:Number=0, G:Number=0, B:Number=0){
			super(1,1,1,1,R,G,B,0);
		}
		protected function setParameters(H:Number, S:Number, L:Number):void {
			if(S==0){
				this.R=this.G=this.B=L;
			}else{
				var temp1:Number, temp2:Number, temp3:Number;
				if(L<0.5) temp2 = L*(1+S);
				else temp2 = L+S-L*S;
				temp1 = 2*L - temp2;
				temp3 = H+1/3;
				if(temp3>1)temp3-=1;
				if(6*temp3<1){
					this.R = temp1+(temp2-temp1)*6*temp3;
				}else{
					if(2*temp3<1) this.R = temp2;
					else{
						if(3*temp3<2) this.R = temp1+(temp2-temp1)*((2/3)-temp3)*6;
						else this.R = temp1;
					}
				}				
				temp3 = H;
				if(6*temp3<1){
					this.G = temp1+(temp2-temp1)*6*temp3;
				}else{
					if(2*temp3<1) this.G = temp2;
					else{
						if(3*temp3<2) this.G = temp1+(temp2-temp1)*((2/3)-temp3)*6;
						else this.G = temp1;
					}
				}				
				temp3 = H-1/3;
				if(temp3<0)temp3+=1;
				if(6*temp3<1){
					this.B = temp1+(temp2-temp1)*6*temp3;
				}else{
					if(2*temp3<1) this.B = temp2;
					else{
						if(3*temp3<2) this.B = temp1+(temp2-temp1)*((2/3)-temp3)*6;
						else this.B = temp1;
					}
				}
			}
		}
	}
}