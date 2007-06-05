
/**
******************************************

	@class: LoadQue Class
	@author: Kenneth Bunch (krb0723@hotmail.com)

	IMPLEMENTS
		ASBroadcaster

	PUBLIC METHODS
		addItem
		clear
		start

	PRIVATE METHODS
		$preloadNext
		$loadItem
		$onItemData
		$onTotalData

	EVENT
		onItemData
		onTotalData
		onLoadFailure
		onLoad

******************************************
*/


/**
	@class LoadQue
*/

import mx.events.*
import mx.utils.*

class LoadQue {
	
	var _$que;
	var _$root;
	var _$index;
	var _$currItem;
	var _$interval;
	var _$container;
	var addEventListener:Function;
	var dispatchEvent:Function;
	//var broadcastMessage:Function;
	
	function LoadQue(container) {
		this._$root = container;
		this._$que = new Array();
		this._$container = new LoadVars();
		this._$index = 0;
		this._$currItem = null;
		this._$interval = null;
		
		EventDispatcher.initialize(this);
		
		this.addEventListener("onItemData", container);
		this.addEventListener("onTotalData", container);
		this.addEventListener("onLoadFailure", container);
		this.addEventListener("onLoad", container);
	}

	/**
		@method (PRIVATE): $preloadNext
	
		@description
			-  preloads the next item in the que
	*/
	private function $preloadNext(){
	
		var controller;
	
		controller = this;
		this._$currItem = this._$que[this._$index++];
		this._$container.load(this._$currItem.url);
		// monitor load of item
		clearInterval(this._$interval);
		// monitor immediately
		this.$onItemData();
		// monitor on interval
		this._$interval = setInterval(this, "$onItemData", 100);
		// handle success or failure of complete load
		this._$container.onLoad = function(bSuccess){
			controller.$onLoad(bSuccess);
		};
	}
	
	
	/**
		@method (PRIVATE): $onItemData
	
		@description
			-  handles data as it is received for an item
	*/
	private function $onItemData(){
	
		var itemPercent;
	
		itemPercent =Math.round(( this._$container.getBytesLoaded() / this._$container.getBytesTotal() )*100);
	
		if(!isNaN(itemPercent)){
			dispatchEvent({target:this, type:"onItemData", iPercent:itemPercent, sID:this._$currItem.id});
			//this.broadcastMessage("onItemData", itemPercent, this._$currItem.id);
		}
	}
	
	
	/**
		@method (PRIVATE): $onTotalData
	
		@description
			-  handles data as each item is completely loaded
	*/
	private function $onTotalData(){
	
		var  totalPercent;
	
		// report total percent loaded
		totalPercent = Math.round((this._$index/this._$que.length) * 100);
	
		if (!isNaN(totalPercent)){
			dispatchEvent({target:this, type:"onTotalData", iPercent:totalPercent});
			//this.broadcastMessage("onTotalData", totalPercent);
		}
	};
	
	
	/**
		@method (PRIVATE): $onLoad
	
		@description
			-  fired when all data for an item is loaded
	*/
	private function $onLoad(bSuccess){
	
		clearInterval(this._$interval);
	
		if (bSuccess){
			// broadcast that item was loaded
			this.$onItemData();
			// load the item
			if (this._$currItem.item != null){
				this.$loadItem();
			}
		} else {
			// report non loaded items
			dispatchEvent({target:this, type:"onLoadFailure", sID:this._$currItem.id});
			//this.broadcastMessage("onLoadFailure", this._$currItem.id);
		}
	
		this.$onTotalData();
	
		// que next or report completed preload
		if ( this._$que.length > (this._$index) ){
			this.$preloadNext();
		} else{
			dispatchEvent({target:this, type:"onLoad"});
			//this.broadcastMessage("onLoad");
		}
	}
	
	
	/**
		@method (PRIVATE): $loadItem
	
		@description
			-  loads current item into container
	*/
	public function $loadItem() {
		// load item into assigned holder
		if (typeof this._$currItem.item == "movieclip"){
			this._$currItem.item.loadMovie(this._$currItem.url);
		} else if(this._$currItem.item instanceof Sound) {
			this._$currItem.item.loadSound(this._$currItem.url,false)
		} else {
			this._$currItem.item.load(this._$currItem.url);
		}
	}
	
	/**
		@method (PUBLIC): addItem
	
		@param : sUrl
			- url of item to load
		@param : [oTarget, sID]
			- [OPTIONAL] target to load item into OR id to associate with item
		@param : [sID]
			- [OPTIONAL] id to be associated with item
	
		@description
			- adds items to preload into movie
			method is overload and can be passed args in the following combos
			addItem(sUrl);
			addItem(sUrl, oTarget);
			addItem(sUrl, sID);
			addItem(sUrl, oTarget, sID);
	*/
	public function addItem(sUrl){
	
		var target, idString;
		if (arguments.length < 3){
			target = (typeof arguments[1] != "string") ? arguments[1] : null;
			idString = (typeof arguments[1] == "string") ? arguments[1] : this._$que.length+1;
		} else{
			target = arguments[1];
			idString = (typeof arguments[2] != null) ? arguments[2] : this._$que.length+1;
		}
		this._$que.push({url:sUrl, item:target,  id:idString});
	}
	
	
	/**
		@method (PUBLIC): clear
	
		@description
		-  clears the que
	*/
	public function clear(){
	
		this._$que = new Array();
		this._$index = 0;
	}
	
	/**
		@method (PUBLIC): start
	
		@description
		-  starts loading the elements of the que
	*/
	public function start(){
	
		this.$preloadNext();
	}

}