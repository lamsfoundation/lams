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
	

	/**
	 * Constructor  
	 * @usage   var ml = new MovieLoader(_activity.libraryActivityUIImage,draw,this,_icon_mc);  
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
		  var myFn = Proxy.create(_scope,_fn,loaded_mc);
		  myFn.call();
		  //_fn.apply(_scope,[loaded_mc]);
		  
		  
	  }
	
		
	
		//TODO: Add all the other handlers and methods to set n get them
	


}