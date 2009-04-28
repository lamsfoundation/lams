package {
	public class RequestTools {
		static public function continueURL(url:String):String {
			return url.indexOf('?')==-1?url+"?":url+"&";
		}
		static public function antiCache(url:String):String {
			return continueURL(url)+("noCache="+(new Date()).time)+Math.floor(Math.random()*100);
		}
	}
}