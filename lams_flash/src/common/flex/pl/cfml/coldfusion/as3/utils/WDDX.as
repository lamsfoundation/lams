/*
THIS SOURCE CODE IS PROVIDED "AS IS" AND "WITH ALL FAULTS", WITHOUT
ANY TECHNICAL SUPPORT OR ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. ALSO, THERE IS NO WARRANTY OF
NON-INFRINGEMENT, TITLE OR QUIET ENJOYMENT. IN NO EVENT SHALL MACROMEDIA
OR ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOURCE CODE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package pl.cfml.coldfusion.as3.utils
{
	import mx.collections.ArrayCollection;
	
	/**
	 * Class providing WDDX parsing and compiling functionality.
	 */
	public class WDDX
	{
		
		/**
		 * Compiles AS3 objects to WDDX.
		 * Type are mapped against these rules:
		 * 
		 * <ul>
		 * <li>String to string</li>
		 * <li>Number to number</li>
		 * <li>Boolean to boolean</li>
		 * <li>Date to date</li>
		 * <li>Array to array</li>
		 * <li>ArrayCollection to array of structs</li>
		 * <li>Recordset to query</li>
		 * <li>Object to struct</li>
		 * </ul>
		 *
		 * @param data AS3 object.
		 * @return WDDX representation of the object.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 * @see pl.cfml.coldfusion.as3.utils.Recordset
		 */
		public static function toWDDX(data:Object):String
		{
			var o:String = "<wddxPacket version='1.0'><header/><data>";
			o += _toWDDX(data);
			o += "</data></wddxPacket>";
			return o;
		}
		
		private static function _toWDDX(data:Object):String
		{
			var o:String = "";
			if ( data is String )
				o = _toString(data);
			else if ( data is Number || data is uint || data is int)
				o = _toNumber(data);
			else if ( data is Boolean )
				o = _toBoolean(data);
			else if ( data is Date )
				o = _toDate(data);
			else if ( data is Array )
				o = _toArray(data);
			else if ( data is ArrayCollection )
				o = _toArrayCollection(data);
			else if ( data is Recordset )
				o = _toRecordset(data);
			else if ( data is Object )
				o = _toStruct(data);
			return o;
		}
		
		private static function _toString(data:Object):String
		{
			return "<string>"+data.toString()+"</string>";
		}
		private static function _toNumber(data:Object):String
		{
			return "<number>"+data.toString()+"</number>";
		}
		private static function _toBoolean(data:Object):String
		{
			return "<boolean value='"+data.toString()+"' />";
		}
		private static function _toDate(data:Object):String
		{
			return "<dateTime>"+data.toString()+"</dateTime>";
		}
		private static function _toArray(data:Object):String
		{
			var o:Array = data as Array;
			var s:String = "<array length='"+o.length+"'>";
			for (var i:Number=0; i<o.length; i++)
			{
				s += _toWDDX( o[i] );
			}
			s += "</array>";
			return s;
		}
		private static function _toArrayCollection(data:Object):String
		{
			var o:ArrayCollection = data as ArrayCollection;
			var s:String = "<array length='"+o.length+"'>";
			for (var i:Number=0; i<o.length; i++)
			{
				s += _toWDDX( o.getItemAt(i) );
			}
			s += "</array>";
			return s;
		}
		private static function _toRecordset(data:Object):String
		{
			var o:Recordset = data as Recordset;
			o.reset();
			var s:String = "<recordset rowCount='"+o.recordCount+"' fieldNames='"+o.columnList+"'>";
			var _cl:Array = o.columnList.split(",");
			var i:Number = -1;
			for (var j:Number=0; j<_cl.length; j++)
			{
				s += "<field name='"+_cl[j]+"'>";
				while ((i=o.next())>-1)
				{
					s += ((o.getValue(_cl[j]) == "") ? "<null/>" : WDDX._toString(o.getValue(_cl[j])) );
				}
				s += "</field>";
				o.reset();
			}
			s += "</recordset>";
			return s;
		}
		private static function _toStruct(data:Object):String
		{
			var s:String = "<struct>";
			for (var i:String in data)
			{
				s += "<var name='"+i+"'>";
				s += _toWDDX( data[i] );
				s += "</var>";
			}
			s += "</struct>";
			return s;
		}
		
		/**
		 * Decompiles WDDX to AS3 objects.
		 * Type are mapped against these rules:
		 * 
		 * <ul>
		 * <li>string, number to String</li>
		 * <li>boolean to Boolean</li>
		 * <li>array to Array</li>
		 * <li>struct to Object</li>
		 * <li>query to Recordset</li>
		 * </ul>
		 *
		 * @param data WDDX representation as XML.
		 * @return AS3 WDDX representation.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 * @see pl.cfml.coldfusion.as3.utils.Recordset
		 */
		public static function fromWDDX(wddx:String):Object
		{
			var x:XML = new XML(wddx);
			return WDDX._fromWDDX(x.data.children()[0]);
		}
		
		private static function _fromWDDX(data:XML):Object
		{
			var o:Object = {};
			switch (data.name().toString().toLowerCase())
			{
				case "string":
					o = _fromString(data);
					break;
				case "boolean":
					o = _fromBoolean(data);
					break;
				case "array":
					o = _fromArray(data);
					break;
				case "struct":
					o = _fromStruct(data);
					break;
				case "dateTime":
					o = _fromString(data);
					break;
				case "recordset":
					o = Recordset.fromWDDXXML(data);
					break;
				default:
					o = _fromString(data);
					break;
			}
			return o;
		}
		
		private static function _fromString(data:XML):String
		{
			return data.text();
		}
		private static function _fromBoolean(data:XML):Boolean
		{
			return data.@value;
		}
		private static function _fromArray(data:XML):ArrayCollection
		{
			var o:ArrayCollection = new ArrayCollection();
			for (var i:int=0; i<parseInt(data.@length.toString()); i++)
			{
				o.addItem( _fromWDDX(data.children()[i]) );
			}
			return o;
		}
		private static function _fromStruct(data:XML):Object
		{
			var o:Object = {};
			var xList:XMLList = data["var"];
			for each (var v:XML in xList)
			{
				o[v.@name] = _fromWDDX(v.children()[0]);
			}
			return o;
		}
	}
}