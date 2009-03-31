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
	import mx.utils.ArrayUtil;
	import mx.collections.ArrayCollection;
	
	/**
	 * Class provides utility metods for Array and ArrayCollection.
	 */
	public class ArrayUtils
	{
		
		/**
		 * Check for element existence in Array.
		 *
		 * @param array The array which will be checked.
		 * @param value Object to be found.
		 * @return Boolean; true if element was found, false if not.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function inArray(array:Array, value:Object):Boolean
		{
			if (ArrayUtil.getItemIndex(value, array) == -1)
				return false;
			return true;
		}
		
		/**
		 * Check for element existence in ArrayCollection by key name of ArrayCollection items.
		 *
		 * @param array The ArrayCollection which will be checked.
		 * @param key Key to be checked.
		 * @param value Object to be found.
		 * @return Boolean; true if element was found, false if not.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function inArrayCollection(array:ArrayCollection, key:String, value:String):Boolean
		{
			for (var i:int=0; i<array.length; i++)
				if ( array.getItemAt(i)[key] == value )
					return true;
			return false;
		}
		
		/**
		 * Check for int value existence in Array.
		 *
		 * @param array The array which will be checked.
		 * @param value int value to be found.
		 * @return Boolean; true if int was found, false if not.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function intInArray(array:Array, value:int):Boolean
		{
			for (var i:int=0; i<array.length; i++)
				if ( parseInt(array[i]) == value )
					return true;
			return false;
		}
		
		/**
		 * Gets element index in ArrayCollection by key name of ArrayCollection items.
		 *
		 * @param array The ArrayCollection which will be checked.
		 * @param key Key to be checked.
		 * @param value String to be found.
		 * @return -1 if element not found or item index.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function getArrayCollectionItemIndexByKey(array:ArrayCollection, key:String, value:String):int
		{
			for (var i:int=0; i<array.length; i++)
				if ( (array.getItemAt( i )[key] as String) == value )
					return i;
			return -1;
		}
		
		/**
		 * Converts Array of Objects to ArrayCollection.
		 *
		 * @param source The Array to be converted.
		 * @return ArrayCollection containing given Array as source.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function toArrayCollection(source:Array):ArrayCollection
		{
			return new ArrayCollection(source);
		}
		
	}
}