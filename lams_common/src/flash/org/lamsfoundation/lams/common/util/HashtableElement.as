import org.lamsfoundation.lams.common.util.*

/**
* HashtableElement
*/
class HashtableElement {
    //Declarations	private var key;
	private var val;
	
    //Constructor
    function HashtableElement(key, val) {
	//trace('Object.prototype.HashtableElement called');
        this.key = key;
        this.val = val;
    }

    public function toString() {
        return "{" + this.key + "=" + this.val + "}";
    }
    //Getters+Setters
}




