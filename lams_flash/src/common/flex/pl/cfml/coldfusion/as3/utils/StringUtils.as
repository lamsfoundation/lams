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
	import mx.utils.StringUtil;
	
	/**
	 * Class provides utility metods for String.
	 */
	public class StringUtils
	{
		
		/**
		 * Wraps text at given position or nearest space position.
		 *
		 * @param s String to be wrapped.
		 * @param n Desired wrap position.
		 * @return Wrapped string.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function wordWrap(s:String, n:int):String
		{
			var _s:String = s;
			var result:Array = [];
			var start:int = 0;
			var i:int = 0;
			while (true)
			{
				var canContinue:Boolean = false;
				for (i=n; i<_s.length; i++)
					if (_s.charCodeAt(i)==32)
					{
						result.push( _s.substr(start, i ) );
						_s = _s.substr(i, _s.length);
						canContinue = true;
						break;
					}
				if (!canContinue)
				{
					if (_s.length>0)
						result.push(_s);
					break;
				}
			}
			for (var j:int=0; j<result.length; j++)
				result[j] = StringUtil.trim( result[j] );
			return result.join(String.fromCharCode(10));
		}
		
	}
}