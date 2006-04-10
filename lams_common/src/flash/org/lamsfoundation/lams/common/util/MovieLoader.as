/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

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
		
		Debugger.log('MovieLoader loading mcUrl:'+mcUrl,Debugger.COMP,'Constructor','org.lamsfoundation.lams.common.util.MovieLoader');
		
		_mcl = new MovieClipLoader();
		_mcl.addListener(this);
		_mcl.loadClip(_mcUrl,target_mc);
		
	  }  
	  
	  public function onLoadInit(loaded_mc):Void{
		  Debugger.log('mc:'+loaded_mc,Debugger.COMP,'onLoadInit','org.lamsfoundation.lams.common.util.MovieLoader');
		  
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