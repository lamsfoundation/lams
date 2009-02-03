package org.lamsfoundation.lams.common.conn
{
	import flash.display.BitmapData;
	import flash.utils.ByteArray;
	
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	import mx.rpc.http.HTTPService;
	
	public class ImageService extends HTTPService
	{
		public var PNG_EXT:String = ".png";
		public var JPG_EXT:String = ".jpg";
		
		public function ImageService(url:String, method:String)
		{
			this.url = url;
			this.method = "POST";
			this.resultFormat = "e4x";
			this.request.method = method;
		}
		
		public function sendImageToServer(image:BitmapData, ext:String, filename:String):void{
			var rawBytes:ByteArray;
			if(ext == PNG_EXT){
				var pngEncoder:PNGEncoder = new PNGEncoder();
				rawBytes = pngEncoder.encode(image);
			}
			else if(ext == JPG_EXT){
				var jpegEncoder:JPEGEncoder = new JPEGEncoder();
				rawBytes = jpegEncoder.encode(image);
			}
			
			this.request.ext = ext;
			this.request.filname = filename;
			this.request.rawBytes = rawBytes;
			this.send();
		}
	}
}