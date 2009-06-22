package org.lamsfoundation.lams.business
{
	import org.lamsfoundation.lams.vos.Activity;
	
	public class LearnerFactory
	{
		public function LearnerFactory()
		{
		}

		public function createActivity(xml:XML):Activity{
			var activity:Activity = new Activity();
			activity.setFromXML(xml);
			return activity;
		}
	}
}