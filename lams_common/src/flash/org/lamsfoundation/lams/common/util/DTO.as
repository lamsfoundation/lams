 /**
* DTO  Generic data transfer obj
*/
class DTO 
{
	//Declarations
	//signifies update or errrr another type
	public var _type : String;
	public var _body : Object;
	//Constructor
	function DTO (t : String, b : Object)
	{
		_type = t;
		_body = b;
	}
	//Getters+Setters
	public function get type () : String
	{
		return _type;
	}
	public function get _body () : Object
	{
		return body;
	}
	public function set type (t : String) : Void
	{
		_type = t;
	}
	
		public function set body (b : Object) : Void
	{
		_body = b;
	}
}
