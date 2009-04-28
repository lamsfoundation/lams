package {
	public class NoPlayer extends SinglePlayer {
		override protected function startApplication(originalXml:XML, xmlLabels:XML):void {
			super.startApplication(originalXml, xmlLabels);
			this.mindMap.block();
		}
	}
}