import org.lamsfoundation.lams.common.util.*

/**
* Util methods for number manipulation
* @class	NumberUtils
* @author	DI
*/
class NumberUtils {

	//Declarations
	private var _className = 'NumberUtils';

	//Constructor
	function NumberUtils() {
	}

	//Getters+Setters
	function get className():String{
		return _className;
	}

    /**
    * @returns a random number between a and b
    */
    public static function randomBetween(a,b):Number {
        return (a + Math.floor(Math.random()*(b-a+1)));
    }

}