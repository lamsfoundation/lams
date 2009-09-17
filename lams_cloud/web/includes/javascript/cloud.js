			function openCopyRight() {
				var height = 388;
				var width = 750;
				var left = 0;
				var top = 0;
				if(self.screen){
					top = self.screen.height/2-height/2;
					left = self.screen.width/2-width/2;
				}
				if(isMac){
					copyrightWin = window.open('copyright.jsp','copyright','resizable,left='+left+',top='+top+',width=750,height=388,scrollbars');
				}else{
					if(copyrightWin && !copyrightWin.closed ){
						copyrightWin.focus();
					}else{
						copyrightWin = window.open('copyright.jsp','copyright','resizable,left='+left+',top='+top+',width=750,height=388,scrollbars');
						copyrightWin.focus();
					}
				}
			}