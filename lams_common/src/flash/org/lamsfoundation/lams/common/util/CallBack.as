/**
 *
 * @author  DI
 * @version 12/04/05
 * 
 */
 class org.lamsfoundation.lams.common.util.CallBack {
    
    private var scope:Object;
    private var fn:Function;
    private var args:Array;
    
    
    function CallBack(scope:Object,fn:Function){
        this.scope = scope;
        this.fn = fn;
        args = [];
        
        //Get arguments from the arguments object ignoring first two as they are scope+function
		var l = arguments.length;
		for (var i = 2; i < l; i++) {
			args[i - 2] = arguments[i];
		}
    }
    
    /**
    * Apply the callback
    */
    public function call(){
        fn.apply(scope,args);
    }
    
    /**
    * Adds additional arguments to args array for dynamic runtime callback modification
    */
    public function addArgs(extraArgs:Array){
        args = args.concat(extraArgs);
    }
}