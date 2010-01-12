package org.lamsfoundation.lams.common.conn
{
	import flash.display.BitmapData;
	import flash.utils.ByteArray;
	
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	import mx.rpc.http.HTTPService;
	import mx.utils.Base64Encoder;
	
	public class ImageService extends HTTPService
	{
		public var PNG_EXT:String = "png";
		public var JPG_EXT:String = "jpg";
		
		public function ImageService(url:String, method:String = null)
		{
			this.url = url;
			this.method = "POST";
			this.resultFormat = "e4x";
			
			if(method){
				this.request.method = method;
			}
		}
		
		public function sendImageToServer(dir:String, filename:String, ext:String, image:BitmapData):void{
			var rawBytes:ByteArray = new ByteArray();
			if(ext == PNG_EXT){
				var pngEncoder:PNGEncoder = new PNGEncoder();
				rawBytes = pngEncoder.encode(image);
			}
			else if(ext == JPG_EXT){
				var jpegEncoder:JPEGEncoder = new JPEGEncoder();
				rawBytes = jpegEncoder.encode(image);	
			}
			
			var encoder:Base64Encoder = new Base64Encoder(); 
   			encoder.encodeBytes(rawBytes); 
   			
   			this.request.dir = dir;
   			this.request.filename = filename;
			this.request.ext = ext;
			this.request.data = encoder.flush();
			
			this.send();
		}
	}
}