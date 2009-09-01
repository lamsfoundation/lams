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
	/**
	 * Class provides utility metods for XML objects.
	 */
	public class XMLUtils
	{
		
		/**
		 * Check for attribute existence in XML node.
		 *
		 * @param node XML node to be checked.
		 * @param attributeName Name of the attribute which needs to be found.
		 * @param caseSensitive Set to true if search should be case-sensitive.
		 * @return Boolean; true if attribute was found, false if not.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public static function attributeExists(node:XML, attributeName:String, caseInsensitive:Boolean=false):Boolean
		{
			if (caseInsensitive)
				attributeName = attributeName.toLowerCase();
			for ( var i:int=0; i<node.attributes().length(); i++ )
			{
				var xNodeAttr:String = (node.attributes()[i] as XML).name().toString();
				if (caseInsensitive)
					xNodeAttr = xNodeAttr.toLowerCase();
				if ( xNodeAttr == attributeName )
					return true;
			}
			return false;
		}
		
	}
}