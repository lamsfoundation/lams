import org.lamsfoundation.lams.common.util.*

/**  
* MovieclipUtils
* @usage var ml = new MovieLoader(_activity.libraryActivityUIImage,draw,this,_icon_mc);  
*/  
class MovieLoader {  
	private var _mcl:MovieClipLoader;
	private var _fn:Function;
	private var _scope:Object;
	private var _mcUrl:String;
	private var _target_mc:MovieClip;

	//TODO: Work out a way around this silly non cachable issue
	public static var movieCache:Array;
	

	/**
	 * Constructor  
	 * @usage   var ml = new MovieLoader(movieURL,fnToRunOnLoad,scopeObject,theTarget);  
	 * @param   mcUrl 		URL of MovieCLip or JPEG to load
	 * @param   fn			Funcation to call when onLoadInit
	 * @param   scope   	Scope of function
	 * @param   target_mc 	MC To replace with loaded mc
	 * @return  mcl			The populated MovieClip Loaded
	 */
	  function MovieLoader(mcUrl:String,fn:Function, scope:Object, target_mc:MovieClip) {  
		_mcUrl = mcUrl;
		_fn = fn;
		_scope = scope;
		_target_mc = target_mc;
		_target_mc.cRef = this;
		
		
		_mcl = new MovieClipLoader();
		_mcl.addListener(this);
		_mcl.loadClip(_mcUrl,target_mc);
		
	  }  
	  
	  public function onLoadInit(loaded_mc):Void{
		  Debugger.log('mc:'+loaded_mc,Debugger.COMP,'onLoadInit','org.lamsfoundation.lams.common.util.MovieLoader');
		  //Debugger.log('_fn:'+_fn,Debugger.COMP,'onLoadInit','org.lamsfoundation.lams.common.util.MovieLoader');
		  //TODO: Make this actually cache the movie.
		  MovieLoader.movieCache[_mcUrl] = loaded_mc;
		  var myFn = Proxy.create(_scope,_fn,loaded_mc);
		  myFn.call();
		  //_fn.apply(_scope,[loaded_mc]);
		  
		  
	  }
	  
	 public function onLoadError(loaded_mc:MovieClip,errorCode:String):Void{
		switch(errorCode){
			
			case 'URLNotFound' :
				Debugger.log('TemplateActivity icon failed to load - URL is not found:'+loaded_mc._url,Debugger.CRITICAL,'onLoadInit','TemplateActivity');	
				break;
			case 'LoadNeverCompleted' :
				Debugger.log('TemplateActivity icon failed to load - Load never completed:'+loaded_mc,Debugger.CRITICAL,'onLoadInit','TemplateActivity');	
				break;
		}
		
		//run the handler functions anyway
		var myFn = Proxy.create(_scope,_fn,loaded_mc);
		myFn.call();
	}
	
		
	
		//TODO: Add all the other handlers and methods to set n get them
	


}