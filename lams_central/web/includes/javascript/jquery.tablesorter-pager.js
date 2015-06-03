/*!
 * tablesorter (FORK) pager plugin
 * updated 5/17/2015 (v2.22.0)
 */
!function(a){"use strict";var b=a.tablesorter;a.extend({tablesorterPager:new function(){this.defaults={container:null,ajaxUrl:null,customAjaxUrl:function(a,b){return b},ajaxObject:{dataType:"json"},processAjaxOnInit:!0,ajaxProcessing:function(a){return[0,[],null]},output:"{startRow} to {endRow} of {totalRows} rows",updateArrows:!0,page:0,pageReset:0,size:10,maxOptionSize:20,savePages:!0,storageKey:"tablesorter-pager",fixedHeight:!1,countChildRows:!1,removeRows:!1,cssFirst:".first",cssPrev:".prev",cssNext:".next",cssLast:".last",cssGoto:".gotoPage",cssPageDisplay:".pagedisplay",cssPageSize:".pagesize",cssErrorRow:"tablesorter-errorRow",cssDisabled:"disabled",totalRows:0,totalPages:0,filteredRows:0,filteredPages:0,ajaxCounter:0,currentFilters:[],startRow:0,endRow:0,$size:null,last:{}};var c="filterInit filterStart filterEnd sortEnd disable enable destroy updateComplete pageSize pageSet pageAndSize pagerUpdate refreshComplete ",d=this,e=function(a,b){var c="addClass",d="removeClass",e=a.cssDisabled,f=!!b,g=f||0===a.page,h=Math.min(a.totalPages,a.filteredPages),i=f||a.page===h-1||0===h;a.updateArrows&&(a.$container.find(a.cssFirst+","+a.cssPrev)[g?c:d](e).attr("aria-disabled",g),a.$container.find(a.cssNext+","+a.cssLast)[i?c:d](e).attr("aria-disabled",i))},f=function(b,c){var d,e,f,g=b.config,h=g.$table.hasClass("hasFilters");if(h&&!c.ajaxUrl)if(a.isEmptyObject(g.cache))c.filteredRows=c.totalRows=g.$tbodies.eq(0).children("tr").not(c.countChildRows?"":"."+g.cssChildRow).length;else for(c.filteredRows=0,d=g.cache[0].normalized,f=d.length,e=0;f>e;e++)c.filteredRows+=c.regexRows.test(d[e][g.columns].$row[0].className)?0:1;else h||(c.filteredRows=c.totalRows)},g=function(c,d,g){if(!d.initializing){var j,k,l,m,n,o,p=c.config,q=d.size||d.settings.size||10;if(d.countChildRows&&k.push(p.cssChildRow),d.totalPages=Math.ceil(d.totalRows/q),p.totalRows=d.totalRows,f(c,d),p.filteredRows=d.filteredRows,d.filteredPages=Math.ceil(d.filteredRows/q)||0,Math.min(d.totalPages,d.filteredPages)>=0){if(k=d.size*d.page>d.filteredRows&&g,d.page=k?d.pageReset||0:d.page,d.startRow=k?d.size*d.page+1:0===d.filteredRows?0:d.size*d.page+1,d.endRow=Math.min(d.filteredRows,d.totalRows,d.size*(d.page+1)),l=d.$container.find(d.cssPageDisplay),j=(d.ajaxData&&d.ajaxData.output?d.ajaxData.output||d.output:d.output).replace(/\{page([\-+]\d+)?\}/gi,function(a,b){return d.totalPages?d.page+(b?parseInt(b,10):1):0}).replace(/\{\w+(\s*:\s*\w+)?\}/gi,function(a){var b,c,e=a.replace(/[{}\s]/g,""),f=e.split(":"),g=d.ajaxData,h=/(rows?|pages?)$/i.test(e)?0:"";return/(startRow|page)/.test(f[0])&&"input"===f[1]?(b=(""+("page"===f[0]?d.totalPages:d.totalRows)).length,c="page"===f[0]?d.page+1:d.startRow,'<input type="text" class="ts-'+f[0]+'" style="max-width:'+b+'em" value="'+c+'"/>'):f.length>1&&g&&g[f[0]]?g[f[0]][f[1]]:d[e]||(g?g[e]:h)||h}),d.$goto.length){for(k="",o=h(d),n=o.length,m=0;n>m;m++)k+='<option value="'+o[m]+'">'+o[m]+"</option>";d.$goto.html(k).val(d.page+1)}l.length&&(l["INPUT"===l[0].nodeName?"val":"html"](j),l.find(".ts-startRow, .ts-page").unbind("change.pager").bind("change.pager",function(){var b=a(this).val(),c=a(this).hasClass("ts-startRow")?Math.floor(b/d.size)+1:b;p.$table.trigger("pageSet.pager",[c])}))}e(d),i(c,d),d.initialized&&g!==!1&&(p.debug&&b.log("Pager: Triggering pagerComplete"),p.$table.trigger("pagerComplete",d),d.savePages&&b.storage&&b.storage(c,d.storageKey,{page:d.page,size:d.size}))}},h=function(b){var c,d,e,f,g,h,i=Math.min(b.totalPages,b.filteredPages)||1,j=5*Math.ceil(i/b.maxOptionSize/5),k=i>b.maxOptionSize,l=b.page+1,m=j,n=i-j,o=[1],p=k?j:1;for(c=p;i>=c;)o.push(c),c+=k?j:1;if(o.push(i),k){for(e=[],d=Math.max(Math.floor(b.maxOptionSize/j)-1,5),m=l-d,1>m&&(m=1),n=l+d,n>i&&(n=i),c=m;n>=c;c++)e.push(c);o=a.grep(o,function(b,c){return a.inArray(b,o)===c}),g=o.length,h=e.length,g-h>j/2&&g+h>b.maxOptionSize&&(f=Math.floor(g/2)-Math.floor(h/2),Array.prototype.splice.apply(o,[f,h])),o=o.concat(e)}return o=a.grep(o,function(b,c){return a.inArray(b,o)===c}).sort(function(a,b){return a-b})},i=function(b,c){var d,e,f=b.config,g=f.$tbodies.eq(0);g.find("tr.pagerSavedHeightSpacer").remove(),c.fixedHeight&&!c.isDisabled&&(e=a.data(b,"pagerSavedHeight"),e&&(d=e-g.height(),d>5&&a.data(b,"pagerLastSize")===c.size&&g.children("tr:visible").length<c.size&&g.append('<tr class="pagerSavedHeightSpacer '+f.selectorRemove.slice(1)+'" style="height:'+d+'px;"></tr>')))},j=function(b,c){var d,e=b.config,f=e.$tbodies.eq(0);f.find("tr.pagerSavedHeightSpacer").remove(),f.children("tr:visible").length||f.append('<tr class="pagerSavedHeightSpacer '+e.selectorRemove.slice(1)+'"><td>&nbsp</td></tr>'),d=f.children("tr").eq(0).height()*c.size,a.data(b,"pagerSavedHeight",d),i(b,c),a.data(b,"pagerLastSize",c.size)},k=function(a,c){if(!c.ajaxUrl){var d,e=0,f=a.config,g=f.$tbodies.eq(0).children("tr"),h=g.length,i=c.page*c.size,j=i+c.size,k=f.widgetOptions&&f.widgetOptions.filter_filteredRow||"filtered",l=0,m=0;for(c.cacheIndex=[],d=0;h>d;d++)g[d].className.match(k)||(m===i&&g[d].className.match(f.cssChildRow)?g[d].style.display="none":(g[d].style.display=m>=i&&j>m?"":"none",l!==m&&m>=i&&j>m&&(c.cacheIndex.push(d),l=m),m+=g[d].className.match(f.cssChildRow+"|"+f.selectorRemove.slice(1))&&!c.countChildRows?0:1,m===j&&"none"!==g[d].style.display&&g[d].className.match(b.css.cssHasChild)&&(e=d)));if(e>0&&g[e].className.match(b.css.cssHasChild))for(;++e<h&&g[e].className.match(f.cssChildRow);)g[e].style.display=""}},l=function(b,c){c.size=parseInt(c.$size.val(),10)||c.size||c.settings.size||10,a.data(b,"pagerLastSize",c.size),e(c),c.removeRows||(k(b,c),a(b).bind("sortEnd.pager filterEnd.pager",function(){k(b,c)}))},m=function(c,d,e,f,h){if("function"==typeof e.ajaxProcessing){var i,j,k,l,m,n,o,p,q,r,s,t,u,v,w=d.config,x=w.$table,y="",z=e.ajaxProcessing(c,d,f)||[0,[]],A=x.find("thead th").length;if(b.showError(d),h)w.debug&&b.log("Pager: >> Ajax Error",f,h),b.showError(d,0===f.status?"Not connected, verify Network":404===f.status?"Requested page not found [404]":500===f.status?"Internal Server Error [500]":"parsererror"===h?"Requested JSON parse failed":"timeout"===h?"Time out error":"abort"===h?"Ajax Request aborted":"Uncaught error: "+f.statusText+" ["+f.status+"]"),w.$tbodies.eq(0).children("tr").detach(),e.totalRows=0;else{if(a.isArray(z)?(k=isNaN(z[0])&&!isNaN(z[1]),u=z[k?1:0],e.totalRows=isNaN(u)?e.totalRows||0:u,w.totalRows=w.filteredRows=e.filteredRows=e.totalRows,s=0===e.totalRows?[""]:z[k?0:1]||[],r=z[2]):(e.ajaxData=z,w.totalRows=e.totalRows=z.total,w.filteredRows=e.filteredRows="undefined"!=typeof z.filteredRows?z.filteredRows:z.total,r=z.headers,s=z.rows),t=s&&s.length,s instanceof jQuery)e.processAjaxOnInit&&(w.$tbodies.eq(0).children("tr").detach(),w.$tbodies.eq(0).append(s));else if(t){for(i=0;t>i;i++){for(y+="<tr>",j=0;j<s[i].length;j++)y+=/^\s*<td/.test(s[i][j])?a.trim(s[i][j]):"<td>"+s[i][j]+"</td>";y+="</tr>"}e.processAjaxOnInit&&w.$tbodies.eq(0).html(y)}if(e.processAjaxOnInit=!0,r&&r.length===A)for(l=x.hasClass("hasStickyHeaders"),n=l?w.widgetOptions.$sticky.children("thead:first").children("tr").children():"",m=x.find("tfoot tr:first").children(),o=w.$headers.filter("th "),v=o.length,j=0;v>j;j++)p=o.eq(j),p.find("."+b.css.icon).length?(q=p.find("."+b.css.icon).clone(!0),p.find(".tablesorter-header-inner").html(r[j]).append(q),l&&n.length&&(q=n.eq(j).find("."+b.css.icon).clone(!0),n.eq(j).find(".tablesorter-header-inner").html(r[j]).append(q))):(p.find(".tablesorter-header-inner").html(r[j]),l&&n.length&&n.eq(j).find(".tablesorter-header-inner").html(r[j])),m.eq(j).html(r[j])}w.showProcessing&&b.isProcessing(d),e.totalPages=Math.ceil(e.totalRows/(e.size||e.settings.size||10)),e.last.totalRows=e.totalRows,e.last.currentFilters=e.currentFilters,e.last.sortList=(w.sortList||[]).join(","),g(d,e,!1),x.trigger("updateCache",[function(){e.initialized&&setTimeout(function(){w.debug&&b.log("Pager: Triggering pagerChange"),x.trigger("applyWidgets").trigger("pagerChange",e),g(d,e,!0)},0)}])}e.initialized||(e.initialized=!0,e.initializing=!1,d.config.debug&&b.log("Pager: Triggering pagerInitialized"),a(d).trigger("applyWidgets").trigger("pagerInitialized",e),g(d,e))},n=function(c,d){var e,f=o(c,d),g=a(document),h=c.config;""!==f&&(h.showProcessing&&b.isProcessing(c,!0),g.bind("ajaxError.pager",function(a,b,e,f){m(null,c,d,b,f),g.unbind("ajaxError.pager")}),e=++d.ajaxCounter,d.last.ajaxUrl=f,d.ajaxObject.url=f,d.ajaxObject.success=function(a,b,f){e<d.ajaxCounter||(m(a,c,d,f),g.unbind("ajaxError.pager"),"function"==typeof d.oldAjaxSuccess&&d.oldAjaxSuccess(a))},h.debug&&b.log("Pager: Ajax initialized",d.ajaxObject),a.ajax(d.ajaxObject))},o=function(c,d){var e,f,g=c.config,h=d.ajaxUrl?d.ajaxUrl.replace(/\{page([\-+]\d+)?\}/,function(a,b){return d.page+(b?parseInt(b,10):0)}).replace(/\{size\}/g,d.size):"",i=g.sortList,j=d.currentFilters||a(c).data("lastSearch")||[],k=h.match(/\{\s*sort(?:List)?\s*:\s*(\w*)\s*\}/),l=h.match(/\{\s*filter(?:List)?\s*:\s*(\w*)\s*\}/),m=[];if(k){for(k=k[1],f=i.length,e=0;f>e;e++)m.push(k+"["+i[e][0]+"]="+i[e][1]);h=h.replace(/\{\s*sort(?:List)?\s*:\s*(\w*)\s*\}/g,m.length?m.join("&"):k),m=[]}if(l){for(l=l[1],f=j.length,e=0;f>e;e++)j[e]&&m.push(l+"["+e+"]="+encodeURIComponent(j[e]));h=h.replace(/\{\s*filter(?:List)?\s*:\s*(\w*)\s*\}/g,m.length?m.join("&"):l),d.currentFilters=j}return"function"==typeof d.customAjaxUrl&&(h=d.customAjaxUrl(c,h)),g.debug&&b.log("Pager: Ajax url = "+h),h},p=function(c,d,e){var f,h,i,j,l=a(c),m=c.config,n=m.$table.hasClass("hasFilters"),o=d&&d.length||0,p=e.page*e.size,q=e.size;if(1>o)return void(m.debug&&b.log("Pager: >> No rows for pager to render"));if(e.page>=e.totalPages&&v(c,e),e.cacheIndex=[],e.isDisabled=!1,e.initialized&&(m.debug&&b.log("Pager: Triggering pagerChange"),l.trigger("pagerChange",e)),e.removeRows){for(b.clearTableBody(c),f=b.processTbody(c,m.$tbodies.eq(0),!0),h=n?0:p,i=n?0:p,j=0;q>j&&h<d.length;)n&&/filtered/.test(d[h][0].className)||(i++,i>p&&q>=j&&(j++,e.cacheIndex.push(h),f.append(d[h]))),h++;b.processTbody(c,f,!1)}else k(c,e);g(c,e),c.isUpdating&&(m.debug&&b.log("Pager: Triggering updateComplete"),l.trigger("updateComplete",[c,!0]))},q=function(c,d){var f,g,h;for(d.ajax?e(d,!0):(d.isDisabled=!0,a.data(c,"pagerLastPage",d.page),a.data(c,"pagerLastSize",d.size),d.page=0,d.size=d.totalRows,d.totalPages=1,a(c).addClass("pagerDisabled").removeAttr("aria-describedby").find("tr.pagerSavedHeightSpacer").remove(),p(c,c.config.rowsCopy,d),a(c).trigger("applyWidgets"),c.config.debug&&b.log("Pager: Disabled")),g=d.$size.add(d.$goto).add(d.$container.find(".ts-startRow, .ts-page")),h=g.length,f=0;h>f;f++)g.eq(f).attr("aria-disabled","true").addClass(d.cssDisabled)[0].disabled=!0},r=function(a){var b=a.config,c=b.pager;b.$table.trigger("updateCache",[function(){var d,e=[],f=a.config.cache[0].normalized;for(c.totalRows=f.length,d=0;d<c.totalRows;d++)e.push(f[d][b.columns].$row);b.rowsCopy=e,s(a,c,!0)}])},s=function(c,d,e){if(!d.isDisabled){var g,h=c.config,i=a(c),j=d.last;return e!==!1&&d.initialized&&a.isEmptyObject(h.cache)?r(c):void(d.ajax&&b.hasWidget(c,"filter")&&!h.widgetOptions.filter_initialized||(f(c,d),g=Math.min(d.totalPages,d.filteredPages),d.page<0&&(d.page=0),d.page>g-1&&0!==g&&(d.page=g-1),j.currentFilters=""===(j.currentFilters||[]).join("")?[]:j.currentFilters,d.currentFilters=""===(d.currentFilters||[]).join("")?[]:d.currentFilters,(j.page!==d.page||j.size!==d.size||j.totalRows!==d.totalRows||(j.currentFilters||[]).join(",")!==(d.currentFilters||[]).join(",")||(j.ajaxUrl||"")!==(d.ajaxObject.url||"")||(j.optAjaxUrl||"")!==(d.ajaxUrl||"")||j.sortList!==(h.sortList||[]).join(","))&&(h.debug&&b.log("Pager: Changing to page "+d.page),d.last={page:d.page,size:d.size,sortList:(h.sortList||[]).join(","),totalRows:d.totalRows,currentFilters:d.currentFilters||[],ajaxUrl:d.ajaxObject.url||"",optAjaxUrl:d.ajaxUrl||""},d.ajax?n(c,d):d.ajax||p(c,h.rowsCopy,d),a.data(c,"pagerLastPage",d.page),d.initialized&&e!==!1&&(h.debug&&b.log("Pager: Triggering pageMoved"),i.trigger("pageMoved",d).trigger("applyWidgets"),c.isUpdating&&(h.debug&&b.log("Pager: Triggering updateComplete"),i.trigger("updateComplete",[c,!0]))))))}},t=function(b,c,d){d.size=c||d.size||d.settings.size||10,d.$size.val(d.size),a.data(b,"pagerLastPage",d.page),a.data(b,"pagerLastSize",d.size),d.totalPages=Math.ceil(d.totalRows/d.size),d.filteredPages=Math.ceil(d.filteredRows/d.size),s(b,d)},u=function(a,b){b.page=0,s(a,b)},v=function(a,b){b.page=Math.min(b.totalPages,b.filteredPages)-1,s(a,b)},w=function(a,b){b.page++,b.page>=Math.min(b.totalPages,b.filteredPages)-1&&(b.page=Math.min(b.totalPages,b.filteredPages)-1),s(a,b)},x=function(a,b){b.page--,b.page<=0&&(b.page=0),s(a,b)},y=function(d,e){q(d,e),e.$container.hide(),d.config.appender=null,e.initialized=!1,delete d.config.rowsCopy,a(d).unbind(c.split(" ").join(".pager ").replace(/\s+/g," ")),b.storage&&b.storage(d,e.storageKey,"")},z=function(c,d,e){var f,g=c.config;d.$size.add(d.$goto).add(d.$container.find(".ts-startRow, .ts-page")).removeClass(d.cssDisabled).removeAttr("disabled").attr("aria-disabled","false"),d.isDisabled=!1,d.page=a.data(c,"pagerLastPage")||d.page||0,d.size=a.data(c,"pagerLastSize")||parseInt(d.$size.find("option[selected]").val(),10)||d.size||d.settings.size||10,d.$size.val(d.size),d.totalPages=Math.ceil(Math.min(d.totalRows,d.filteredRows)/d.size),c.id&&(f=c.id+"_pager_info",d.$container.find(d.cssPageDisplay).attr("id",f),g.$table.attr("aria-describedby",f)),j(c,d),e&&(g.$table.trigger("updateRows"),t(c,d.size,d),l(c,d),g.debug&&b.log("Pager: Enabled"))};d.appender=function(b,c){var d=b.config,e=d.pager;e.ajax||(d.rowsCopy=c,e.totalRows=e.countChildRows?d.$tbodies.eq(0).children("tr").length:c.length,e.size=a.data(b,"pagerLastSize")||e.size||e.settings.size||10,e.totalPages=Math.ceil(e.totalRows/e.size),p(b,c,e),g(b,e,!1))},d.construct=function(e){return this.each(function(){if(this.config&&this.hasInitialized){var f,h,i,m=this,n=m.config,o=n.widgetOptions,p=n.pager=a.extend(!0,{},a.tablesorterPager.defaults,e),A=n.$table,B=p.$container=a(p.container).addClass("tablesorter-pager").show();p.settings=a.extend(!0,{},a.tablesorterPager.defaults,e),n.debug&&b.log("Pager: Initializing"),p.oldAjaxSuccess=p.oldAjaxSuccess||p.ajaxObject.success,n.appender=d.appender,p.initializing=!0,p.savePages&&b.storage&&(f=b.storage(m,p.storageKey)||{},p.page=isNaN(f.page)?p.page:f.page,p.size=(isNaN(f.size)?p.size:f.size)||p.settings.size||10,a.data(m,"pagerLastSize",p.size)),p.regexRows=new RegExp("("+(o.filter_filteredRow||"filtered")+"|"+n.selectorRemove.slice(1)+"|"+n.cssChildRow+")"),A.unbind(c.split(" ").join(".pager ").replace(/\s+/g," ")).bind("filterInit.pager filterStart.pager",function(b,c){p.currentFilters=a.isArray(c)?c:n.$table.data("lastSearch"),"filterStart"===b.type&&p.pageReset!==!1&&(n.lastCombinedFilter||"")!==(p.currentFilters||[]).join("")&&(p.page=p.pageReset)}).bind("filterEnd.pager sortEnd.pager",function(){p.currentFilters=n.$table.data("lastSearch"),(p.initialized||p.initializing)&&(n.delayInit&&n.rowsCopy&&0===n.rowsCopy.length&&r(m),g(m,p,!1),s(m,p,!1),n.$table.trigger("applyWidgets"))}).bind("disable.pager",function(a){a.stopPropagation(),q(m,p)}).bind("enable.pager",function(a){a.stopPropagation(),z(m,p,!0)}).bind("destroy.pager",function(a){a.stopPropagation(),y(m,p)}).bind("updateComplete.pager",function(a,b,c){if(a.stopPropagation(),b&&!c&&!p.ajax){var d=n.$tbodies.eq(0).children("tr").not(n.selectorRemove);p.totalRows=d.length-(p.countChildRows?0:d.filter("."+n.cssChildRow).length),p.totalPages=Math.ceil(p.totalRows/p.size),d.length&&n.rowsCopy&&0===n.rowsCopy.length&&r(b),p.page>=p.totalPages&&v(b,p),k(b,p),j(b,p),g(b,p,!0)}}).bind("pageSize.pager refreshComplete.pager",function(a,b){a.stopPropagation(),t(m,parseInt(b,10)||p.settings.size||10,p),k(m,p),g(m,p,!1)}).bind("pageSet.pager pagerUpdate.pager",function(a,b){a.stopPropagation(),"pagerUpdate"===a.type&&(b="undefined"==typeof b?p.page+1:b,p.last.page=!0),p.page=(parseInt(b,10)||1)-1,s(m,p,!0),g(m,p,!1)}).bind("pageAndSize.pager",function(a,b,c){a.stopPropagation(),p.page=(parseInt(b,10)||1)-1,t(m,parseInt(c,10)||p.settings.size||10,p),s(m,p,!0),k(m,p),g(m,p,!1)}),h=[p.cssFirst,p.cssPrev,p.cssNext,p.cssLast],i=[u,x,w,v],n.debug&&!B.length&&b.log("Pager: >> Container not found"),B.find(h.join(",")).attr("tabindex",0).unbind("click.pager").bind("click.pager",function(b){b.stopPropagation();var c,d=a(this),e=h.length;if(!d.hasClass(p.cssDisabled))for(c=0;e>c;c++)if(d.is(h[c])){i[c](m,p);break}}),p.$goto=B.find(p.cssGoto),p.$goto.length?p.$goto.unbind("change.pager").bind("change.pager",function(){p.page=a(this).val()-1,s(m,p,!0),g(m,p,!1)}):n.debug&&b.log("Pager: >> Goto selector not found"),p.$size=B.find(p.cssPageSize),p.$size.length?(p.$size.find("option").removeAttr("selected"),p.$size.unbind("change.pager").bind("change.pager",function(){return p.$size.val(a(this).val()),a(this).hasClass(p.cssDisabled)||(t(m,parseInt(a(this).val(),10),p),j(m,p)),!1})):n.debug&&b.log("Pager: >> Size selector not found"),p.initialized=!1,A.trigger("pagerBeforeInitialized",p),z(m,p,!1),"string"==typeof p.ajaxUrl?(p.ajax=!0,n.widgetOptions.filter_serversideFiltering=!0,n.serverSideSorting=!0,s(m,p)):(p.ajax=!1,a(this).trigger("appendCache",!0),l(m,p)),p.ajax||p.initialized||(p.initializing=!1,p.initialized=!0,s(m,p),n.debug&&b.log("Pager: Triggering pagerInitialized"),n.$table.trigger("pagerInitialized",p),n.widgetOptions.filter_initialized&&b.hasWidget(m,"filter")||g(m,p,!1)),n.widgetInit.pager=!0}})}}}),b.showError=function(b,c){var d,e,f,g,h=a(b),i=h.length;for(d=0;i>d;d++)f=h[d].config,f&&(g=f.pager&&f.pager.cssErrorRow||f.widgetOptions.pager_css&&f.widgetOptions.pager_css.errorRow||"tablesorter-errorRow","undefined"==typeof c?f.$table.find("thead").find(f.selectorRemove).remove():e=a(/tr\>/.test(c)?c:'<tr><td colspan="'+f.columns+'">'+c+"</td></tr>").click(function(){a(this).remove()}).appendTo(f.$table.find("thead:first")).addClass(g+" "+f.selectorRemove.slice(1)).attr({role:"alert","aria-live":"assertive"}))},a.fn.extend({tablesorterPager:a.tablesorterPager.construct})}(jQuery);