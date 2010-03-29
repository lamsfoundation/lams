package org.lamsfoundation.lams.author.model.transition
{
	import org.lamsfoundation.lams.author.model.activity.Activity;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class TransitionFactory
	{
		public function TransitionFactory(){}
		
		public static function createTransition(fromActivity:Activity, toActivity:Activity, type:int, UIID:int):Transition {
			switch (type) {
				case Constants.TRANSITION_TYPE_NORMAL:
					return new Transition(fromActivity, toActivity, UIID);
					break;
				default:
					return null;
			}
		}
	}
}