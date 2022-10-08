/*!
* FullCalendar v2.0.3
* Docs & License: http://arshaw.com/fullcalendar/
* (c) 2013 Adam Shaw
*/(function(factory){if(typeof define==='function'&&define.amd){define(['jquery','moment'],factory);}
else{factory(jQuery,moment);}})(function($,moment){;;var defaults={lang:'en',defaultTimedEventDuration:'02:00:00',defaultAllDayEventDuration:{days:1},forceEventDuration:false,nextDayThreshold:'09:00:00',defaultView:'month',aspectRatio:1.35,header:{left:'title',center:'',right:'today prev,next'},weekends:true,weekNumbers:false,weekNumberTitle:'W',weekNumberCalculation:'local',lazyFetching:true,startParam:'start',endParam:'end',timezoneParam:'timezone',timezone:false,titleFormat:{month:'MMMM YYYY',week:'ll',day:'LL'},columnFormat:{month:'ddd',week:generateWeekColumnFormat,day:'dddd'},timeFormat:{'default':generateShortTimeFormat},displayEventEnd:{month:false,basicWeek:false,'default':true},isRTL:false,defaultButtonText:{prev:"prev",next:"next",prevYear:"prev year",nextYear:"next year",today:'today',month:'month',week:'week',day:'day'},buttonIcons:{prev:'left-single-arrow',next:'right-single-arrow',prevYear:'left-double-arrow',nextYear:'right-double-arrow'},theme:false,themeButtonIcons:{prev:'circle-triangle-w',next:'circle-triangle-e',prevYear:'seek-prev',nextYear:'seek-next'},unselectAuto:true,dropAccept:'*',handleWindowResize:true,windowResizeDelay:200};function generateShortTimeFormat(options,langData){return langData.longDateFormat('LT').replace(':mm','(:mm)').replace(/(\Wmm)$/,'($1)').replace(/\s*a$/i,'t');}
function generateWeekColumnFormat(options,langData){var format=langData.longDateFormat('L');format=format.replace(/^Y+[^\w\s]*|[^\w\s]*Y+$/g,'');if(options.isRTL){format+=' ddd';}
else{format='ddd '+format;}
return format;}
var langOptionHash={en:{columnFormat:{week:'ddd M/D'}}};var rtlDefaults={header:{left:'next,prev today',center:'',right:'title'},buttonIcons:{prev:'right-single-arrow',next:'left-single-arrow',prevYear:'right-double-arrow',nextYear:'left-double-arrow'},themeButtonIcons:{prev:'circle-triangle-e',next:'circle-triangle-w',nextYear:'seek-prev',prevYear:'seek-next'}};;;var fc=$.fullCalendar={version:"2.0.3"};var fcViews=fc.views={};$.fn.fullCalendar=function(options){var args=Array.prototype.slice.call(arguments,1);var res=this;this.each(function(i,_element){var element=$(_element);var calendar=element.data('fullCalendar');var singleRes;if(typeof options==='string'){if(calendar&&$.isFunction(calendar[options])){singleRes=calendar[options].apply(calendar,args);if(!i){res=singleRes;}
if(options==='destroy'){element.removeData('fullCalendar');}}}
else if(!calendar){calendar=new Calendar(element,options);element.data('fullCalendar',calendar);calendar.render();}});return res;};function setDefaults(d){mergeOptions(defaults,d);}
function mergeOptions(target){function mergeIntoTarget(name,value){if($.isPlainObject(value)&&$.isPlainObject(target[name])&&!isForcedAtomicOption(name)){target[name]=mergeOptions({},target[name],value);}
else if(value!==undefined){target[name]=value;}}
for(var i=1;i<arguments.length;i++){$.each(arguments[i],mergeIntoTarget);}
return target;}
function isForcedAtomicOption(name){return /(Time|Duration)$/.test(name);};;fc.langs=langOptionHash;fc.datepickerLang=function(langCode,datepickerLangCode,options){var langOptions=langOptionHash[langCode];if(!langOptions){langOptions=langOptionHash[langCode]={};}
mergeOptions(langOptions,{isRTL:options.isRTL,weekNumberTitle:options.weekHeader,titleFormat:{month:options.showMonthAfterYear?'YYYY['+options.yearSuffix+'] MMMM':'MMMM YYYY['+options.yearSuffix+']'},defaultButtonText:{prev:stripHTMLEntities(options.prevText),next:stripHTMLEntities(options.nextText),today:stripHTMLEntities(options.currentText)}});if($.datepicker){$.datepicker.regional[datepickerLangCode]=$.datepicker.regional[langCode]=options;$.datepicker.regional.en=$.datepicker.regional[''];$.datepicker.setDefaults(options);}};fc.lang=function(langCode,options){var langOptions;if(options){langOptions=langOptionHash[langCode];if(!langOptions){langOptions=langOptionHash[langCode]={};}
mergeOptions(langOptions,options||{});}
defaults.lang=langCode;};;;function Calendar(element,instanceOptions){var t=this;instanceOptions=instanceOptions||{};var options=mergeOptions({},defaults,instanceOptions);var langOptions;if(options.lang in langOptionHash){langOptions=langOptionHash[options.lang];}
else{langOptions=langOptionHash[defaults.lang];}
if(langOptions){options=mergeOptions({},defaults,langOptions,instanceOptions);}
if(options.isRTL){options=mergeOptions({},defaults,rtlDefaults,langOptions||{},instanceOptions);}
t.options=options;t.render=render;t.destroy=destroy;t.refetchEvents=refetchEvents;t.reportEvents=reportEvents;t.reportEventChange=reportEventChange;t.rerenderEvents=rerenderEvents;t.changeView=changeView;t.select=select;t.unselect=unselect;t.prev=prev;t.next=next;t.prevYear=prevYear;t.nextYear=nextYear;t.today=today;t.gotoDate=gotoDate;t.incrementDate=incrementDate;t.getDate=getDate;t.getCalendar=getCalendar;t.getView=getView;t.option=option;t.trigger=trigger;function getLocaleData(langCode){var f=moment.localeData||moment.langData;return f.call(moment,langCode)||f.call(moment,'en');}
var localeData=createObject(getLocaleData(options.lang));if(options.monthNames){localeData._months=options.monthNames;}
if(options.monthNamesShort){localeData._monthsShort=options.monthNamesShort;}
if(options.dayNames){localeData._weekdays=options.dayNames;}
if(options.dayNamesShort){localeData._weekdaysShort=options.dayNamesShort;}
if(options.firstDay!=null){var _week=createObject(localeData._week);_week.dow=options.firstDay;localeData._week=_week;}
t.defaultAllDayEventDuration=moment.duration(options.defaultAllDayEventDuration);t.defaultTimedEventDuration=moment.duration(options.defaultTimedEventDuration);t.moment=function(){var mom;if(options.timezone==='local'){mom=fc.moment.apply(null,arguments);if(mom.hasTime()){mom.local();}}
else if(options.timezone==='UTC'){mom=fc.moment.utc.apply(null,arguments);}
else{mom=fc.moment.parseZone.apply(null,arguments);}
if('_locale'in mom){mom._locale=localeData;}
else{mom._lang=localeData;}
return mom;};t.getIsAmbigTimezone=function(){return options.timezone!=='local'&&options.timezone!=='UTC';};t.rezoneDate=function(date){return t.moment(date.toArray());};t.getNow=function(){var now=options.now;if(typeof now==='function'){now=now();}
return t.moment(now);};t.calculateWeekNumber=function(mom){var calc=options.weekNumberCalculation;if(typeof calc==='function'){return calc(mom);}
else if(calc==='local'){return mom.week();}
else if(calc.toUpperCase()==='ISO'){return mom.isoWeek();}};t.getEventEnd=function(event){if(event.end){return event.end.clone();}
else{return t.getDefaultEventEnd(event.allDay,event.start);}};t.getDefaultEventEnd=function(allDay,start){var end=start.clone();if(allDay){end.stripTime().add(t.defaultAllDayEventDuration);}
else{end.add(t.defaultTimedEventDuration);}
if(t.getIsAmbigTimezone()){end.stripZone();}
return end;};t.formatRange=function(m1,m2,formatStr){if(typeof formatStr==='function'){formatStr=formatStr.call(t,options,localeData);}
return formatRange(m1,m2,formatStr,null,options.isRTL);};t.formatDate=function(mom,formatStr){if(typeof formatStr==='function'){formatStr=formatStr.call(t,options,localeData);}
return formatDate(mom,formatStr);};EventManager.call(t,options);var isFetchNeeded=t.isFetchNeeded;var fetchEvents=t.fetchEvents;var _element=element[0];var header;var headerElement;var content;var tm;var currentView;var elementOuterWidth;var suggestedViewHeight;var resizeUID=0;var ignoreWindowResize=0;var date;var events=[];var _dragElement;if(options.defaultDate!=null){date=t.moment(options.defaultDate);}
else{date=t.getNow();}
function render(inc){if(!content){initialRender();}
else if(elementVisible()){calcSize();_renderView(inc);}}
function initialRender(){tm=options.theme?'ui':'fc';element.addClass('fc');if(options.isRTL){element.addClass('fc-rtl');}
else{element.addClass('fc-ltr');}
if(options.theme){element.addClass('ui-widget');}
content=$("<div class='fc-content' />").prependTo(element);header=new Header(t,options);headerElement=header.render();if(headerElement){element.prepend(headerElement);}
changeView(options.defaultView);if(options.handleWindowResize){$(window).resize(windowResize);}
if(!bodyVisible()){lateRender();}}
function lateRender(){setTimeout(function(){if(!currentView.start&&bodyVisible()){renderView();}},0);}
function destroy(){if(currentView){trigger('viewDestroy',currentView,currentView,currentView.element);currentView.triggerEventDestroy();}
$(window).unbind('resize',windowResize);if(options.droppable){$(document).off('dragstart',droppableDragStart).off('dragstop',droppableDragStop);}
if(currentView.selectionManagerDestroy){currentView.selectionManagerDestroy();}
header.destroy();content.remove();element.removeClass('fc fc-ltr fc-rtl ui-widget');}
function elementVisible(){return element.is(':visible');}
function bodyVisible(){return $('body').is(':visible');}
function changeView(newViewName){if(!currentView||newViewName!=currentView.name){_changeView(newViewName);}}
function _changeView(newViewName){ignoreWindowResize++;if(currentView){trigger('viewDestroy',currentView,currentView,currentView.element);unselect();currentView.triggerEventDestroy();freezeContentHeight();currentView.element.remove();header.deactivateButton(currentView.name);}
header.activateButton(newViewName);currentView=new fcViews[newViewName]($("<div class='fc-view fc-view-"+newViewName+"' />").appendTo(content),t);renderView();unfreezeContentHeight();ignoreWindowResize--;}
function renderView(inc){if(!currentView.start||inc||!date.isWithin(currentView.intervalStart,currentView.intervalEnd)){if(elementVisible()){_renderView(inc);}}}
function _renderView(inc){ignoreWindowResize++;if(currentView.start){trigger('viewDestroy',currentView,currentView,currentView.element);unselect();clearEvents();}
freezeContentHeight();if(inc){date=currentView.incrementDate(date,inc);}
currentView.render(date.clone());setSize();unfreezeContentHeight();(currentView.afterRender||noop)();updateTitle();updateTodayButton();trigger('viewRender',currentView,currentView,currentView.element);ignoreWindowResize--;getAndRenderEvents();}
function updateSize(){if(elementVisible()){unselect();clearEvents();calcSize();setSize();renderEvents();}}
function calcSize(){if(options.contentHeight){suggestedViewHeight=options.contentHeight;}
else if(options.height){suggestedViewHeight=options.height-(headerElement?headerElement.height():0)-vsides(content);}
else{suggestedViewHeight=Math.round(content.width()/Math.max(options.aspectRatio,.5));}}
function setSize(){if(suggestedViewHeight===undefined){calcSize();}
ignoreWindowResize++;currentView.setHeight(suggestedViewHeight);currentView.setWidth(content.width());ignoreWindowResize--;elementOuterWidth=element.outerWidth();}
function windowResize(ev){if(!ignoreWindowResize&&ev.target===window){if(currentView.start){var uid=++resizeUID;setTimeout(function(){if(uid==resizeUID&&!ignoreWindowResize&&elementVisible()){if(elementOuterWidth!=(elementOuterWidth=element.outerWidth())){ignoreWindowResize++;updateSize();currentView.trigger('windowResize',_element);ignoreWindowResize--;}}},options.windowResizeDelay);}else{lateRender();}}}
function refetchEvents(){clearEvents();fetchAndRenderEvents();}
function rerenderEvents(modifiedEventID){clearEvents();renderEvents(modifiedEventID);}
function renderEvents(modifiedEventID){if(elementVisible()){currentView.renderEvents(events,modifiedEventID);currentView.trigger('eventAfterAllRender');}}
function clearEvents(){currentView.triggerEventDestroy();currentView.clearEvents();currentView.clearEventData();}
function getAndRenderEvents(){if(!options.lazyFetching||isFetchNeeded(currentView.start,currentView.end)){fetchAndRenderEvents();}
else{renderEvents();}}
function fetchAndRenderEvents(){fetchEvents(currentView.start,currentView.end);}
function reportEvents(_events){events=_events;renderEvents();}
function reportEventChange(eventID){rerenderEvents(eventID);}
function updateTitle(){header.updateTitle(currentView.title);}
function updateTodayButton(){var now=t.getNow();if(now.isWithin(currentView.intervalStart,currentView.intervalEnd)){header.disableButton('today');}
else{header.enableButton('today');}}
function select(start,end){currentView.select(start,end);}
function unselect(){if(currentView){currentView.unselect();}}
function prev(){renderView(-1);}
function next(){renderView(1);}
function prevYear(){date.add(-1,'years');renderView();}
function nextYear(){date.add(1,'years');renderView();}
function today(){date=t.getNow();renderView();}
function gotoDate(dateInput){date=t.moment(dateInput);renderView();}
function incrementDate(delta){date.add(moment.duration(delta));renderView();}
function getDate(){return date.clone();}
function freezeContentHeight(){content.css({width:'100%',height:content.height(),overflow:'hidden'});}
function unfreezeContentHeight(){content.css({width:'',height:'',overflow:''});}
function getCalendar(){return t;}
function getView(){return currentView;}
function option(name,value){if(value===undefined){return options[name];}
if(name=='height'||name=='contentHeight'||name=='aspectRatio'){options[name]=value;updateSize();}}
function trigger(name,thisObj){if(options[name]){return options[name].apply(thisObj||_element,Array.prototype.slice.call(arguments,2));}}
if(options.droppable){$(document).on('dragstart',droppableDragStart).on('dragstop',droppableDragStop);}
function droppableDragStart(ev,ui){var _e=ev.target;var e=$(_e);if(!e.parents('.fc').length){var accept=options.dropAccept;if($.isFunction(accept)?accept.call(_e,e):e.is(accept)){_dragElement=_e;currentView.dragStart(_dragElement,ev,ui);}}}
function droppableDragStop(ev,ui){if(_dragElement){currentView.dragStop(_dragElement,ev,ui);_dragElement=null;}}};;function Header(calendar,options){var t=this;t.render=render;t.destroy=destroy;t.updateTitle=updateTitle;t.activateButton=activateButton;t.deactivateButton=deactivateButton;t.disableButton=disableButton;t.enableButton=enableButton;var element=$([]);var tm;function render(){tm=options.theme?'ui':'fc';var sections=options.header;if(sections){element=$("<table class='fc-header' style='width:100%'/>").append($("<tr/>").append(renderSection('left')).append(renderSection('center')).append(renderSection('right')));return element;}}
function destroy(){element.remove();}
function renderSection(position){var e=$("<td class='fc-header-"+position+"'/>");var buttonStr=options.header[position];if(buttonStr){$.each(buttonStr.split(' '),function(i){if(i>0){e.append("<span class='fc-header-space'/>");}
var prevButton;$.each(this.split(','),function(j,buttonName){if(buttonName=='title'){e.append("<span class='fc-header-title'><h2>&nbsp;</h2></span>");if(prevButton){prevButton.addClass(tm+'-corner-right');}
prevButton=null;}else{var buttonClick;if(calendar[buttonName]){buttonClick=calendar[buttonName];}
else if(fcViews[buttonName]){buttonClick=function(){button.removeClass(tm+'-state-hover');calendar.changeView(buttonName);};}
if(buttonClick){var themeIcon=smartProperty(options.themeButtonIcons,buttonName);var normalIcon=smartProperty(options.buttonIcons,buttonName);var defaultText=smartProperty(options.defaultButtonText,buttonName);var customText=smartProperty(options.buttonText,buttonName);var html;if(customText){html=htmlEscape(customText);}
else if(themeIcon&&options.theme){html="<span class='ui-icon ui-icon-"+themeIcon+"'></span>";}
else if(normalIcon&&!options.theme){html="<span class='fc-icon fc-icon-"+normalIcon+"'></span>";}
else{html=htmlEscape(defaultText||buttonName);}
var button=$("<span class='fc-button fc-button-"+buttonName+" "+tm+"-state-default'>"+
html+
"</span>").click(function(){if(!button.hasClass(tm+'-state-disabled')){buttonClick();}}).mousedown(function(){button.not('.'+tm+'-state-active').not('.'+tm+'-state-disabled').addClass(tm+'-state-down');}).mouseup(function(){button.removeClass(tm+'-state-down');}).hover(function(){button.not('.'+tm+'-state-active').not('.'+tm+'-state-disabled').addClass(tm+'-state-hover');},function(){button.removeClass(tm+'-state-hover').removeClass(tm+'-state-down');}).appendTo(e);disableTextSelection(button);if(!prevButton){button.addClass(tm+'-corner-left');}
prevButton=button;}}});if(prevButton){prevButton.addClass(tm+'-corner-right');}});}
return e;}
function updateTitle(html){element.find('h2').html(html);}
function activateButton(buttonName){element.find('span.fc-button-'+buttonName).addClass(tm+'-state-active');}
function deactivateButton(buttonName){element.find('span.fc-button-'+buttonName).removeClass(tm+'-state-active');}
function disableButton(buttonName){element.find('span.fc-button-'+buttonName).addClass(tm+'-state-disabled');}
function enableButton(buttonName){element.find('span.fc-button-'+buttonName).removeClass(tm+'-state-disabled');}};;fc.sourceNormalizers=[];fc.sourceFetchers=[];var ajaxDefaults={dataType:'json',cache:false};var eventGUID=1;function EventManager(options){var t=this;t.isFetchNeeded=isFetchNeeded;t.fetchEvents=fetchEvents;t.addEventSource=addEventSource;t.removeEventSource=removeEventSource;t.updateEvent=updateEvent;t.renderEvent=renderEvent;t.removeEvents=removeEvents;t.clientEvents=clientEvents;t.mutateEvent=mutateEvent;var trigger=t.trigger;var getView=t.getView;var reportEvents=t.reportEvents;var getEventEnd=t.getEventEnd;var stickySource={events:[]};var sources=[stickySource];var rangeStart,rangeEnd;var currentFetchID=0;var pendingSourceCnt=0;var loadingLevel=0;var cache=[];$.each((options.events?[options.events]:[]).concat(options.eventSources||[]),function(i,sourceInput){var source=buildEventSource(sourceInput);if(source){sources.push(source);}});function isFetchNeeded(start,end){return!rangeStart||start.clone().stripZone()<rangeStart.clone().stripZone()||end.clone().stripZone()>rangeEnd.clone().stripZone();}
function fetchEvents(start,end){rangeStart=start;rangeEnd=end;cache=[];var fetchID=++currentFetchID;var len=sources.length;pendingSourceCnt=len;for(var i=0;i<len;i++){fetchEventSource(sources[i],fetchID);}}
function fetchEventSource(source,fetchID){_fetchEventSource(source,function(events){var isArraySource=$.isArray(source.events);var i;var event;if(fetchID==currentFetchID){if(events){for(i=0;i<events.length;i++){event=events[i];if(!isArraySource){event=buildEvent(event,source);}
if(event){cache.push(event);}}}
pendingSourceCnt--;if(!pendingSourceCnt){reportEvents(cache);}}});}
function _fetchEventSource(source,callback){var i;var fetchers=fc.sourceFetchers;var res;for(i=0;i<fetchers.length;i++){res=fetchers[i].call(t,source,rangeStart.clone(),rangeEnd.clone(),options.timezone,callback);if(res===true){return;}
else if(typeof res=='object'){_fetchEventSource(res,callback);return;}}
var events=source.events;if(events){if($.isFunction(events)){pushLoading();events.call(t,rangeStart.clone(),rangeEnd.clone(),options.timezone,function(events){callback(events);popLoading();});}
else if($.isArray(events)){callback(events);}
else{callback();}}else{var url=source.url;if(url){var success=source.success;var error=source.error;var complete=source.complete;var customData;if($.isFunction(source.data)){customData=source.data();}
else{customData=source.data;}
var data=$.extend({},customData||{});var startParam=firstDefined(source.startParam,options.startParam);var endParam=firstDefined(source.endParam,options.endParam);var timezoneParam=firstDefined(source.timezoneParam,options.timezoneParam);if(startParam){data[startParam]=rangeStart.format();}
if(endParam){data[endParam]=rangeEnd.format();}
if(options.timezone&&options.timezone!='local'){data[timezoneParam]=options.timezone;}
pushLoading();$.ajax($.extend({},ajaxDefaults,source,{data:data,success:function(events){events=events||[];var res=applyAll(success,this,arguments);if($.isArray(res)){events=res;}
callback(events);},error:function(){applyAll(error,this,arguments);callback();},complete:function(){applyAll(complete,this,arguments);popLoading();}}));}else{callback();}}}
function addEventSource(sourceInput){var source=buildEventSource(sourceInput);if(source){sources.push(source);pendingSourceCnt++;fetchEventSource(source,currentFetchID);}}
function buildEventSource(sourceInput){var normalizers=fc.sourceNormalizers;var source;var i;if($.isFunction(sourceInput)||$.isArray(sourceInput)){source={events:sourceInput};}
else if(typeof sourceInput==='string'){source={url:sourceInput};}
else if(typeof sourceInput==='object'){source=$.extend({},sourceInput);if(typeof source.className==='string'){source.className=source.className.split(/\s+/);}}
if(source){if($.isArray(source.events)){source.events=$.map(source.events,function(eventInput){return buildEvent(eventInput,source);});}
for(i=0;i<normalizers.length;i++){normalizers[i].call(t,source);}
return source;}}
function removeEventSource(source){sources=$.grep(sources,function(src){return!isSourcesEqual(src,source);});cache=$.grep(cache,function(e){return!isSourcesEqual(e.source,source);});reportEvents(cache);}
function isSourcesEqual(source1,source2){return source1&&source2&&getSourcePrimitive(source1)==getSourcePrimitive(source2);}
function getSourcePrimitive(source){return((typeof source=='object')?(source.events||source.url):'')||source;}
function updateEvent(event){event.start=t.moment(event.start);if(event.end){event.end=t.moment(event.end);}
mutateEvent(event);propagateMiscProperties(event);reportEvents(cache);}
var miscCopyableProps=['title','url','allDay','className','editable','color','backgroundColor','borderColor','textColor'];function propagateMiscProperties(event){var i;var cachedEvent;var j;var prop;for(i=0;i<cache.length;i++){cachedEvent=cache[i];if(cachedEvent._id==event._id&&cachedEvent!==event){for(j=0;j<miscCopyableProps.length;j++){prop=miscCopyableProps[j];if(event[prop]!==undefined){cachedEvent[prop]=event[prop];}}}}}
function renderEvent(eventData,stick){var event=buildEvent(eventData);if(event){if(!event.source){if(stick){stickySource.events.push(event);event.source=stickySource;}
cache.push(event);}
reportEvents(cache);}}
function removeEvents(filter){var eventID;var i;if(filter==null){filter=function(){return true;};}
else if(!$.isFunction(filter)){eventID=filter+'';filter=function(event){return event._id==eventID;};}
cache=$.grep(cache,filter,true);for(i=0;i<sources.length;i++){if($.isArray(sources[i].events)){sources[i].events=$.grep(sources[i].events,filter,true);}}
reportEvents(cache);}
function clientEvents(filter){if($.isFunction(filter)){return $.grep(cache,filter);}
else if(filter!=null){filter+='';return $.grep(cache,function(e){return e._id==filter;});}
return cache;}
function pushLoading(){if(!(loadingLevel++)){trigger('loading',null,true,getView());}}
function popLoading(){if(!(--loadingLevel)){trigger('loading',null,false,getView());}}
function buildEvent(data,source){var out={};var start;var end;var allDay;var allDayDefault;if(options.eventDataTransform){data=options.eventDataTransform(data);}
if(source&&source.eventDataTransform){data=source.eventDataTransform(data);}
start=t.moment(data.start||data.date);if(!start.isValid()){return;}
end=null;if(data.end){end=t.moment(data.end);if(!end.isValid()){return;}}
allDay=data.allDay;if(allDay===undefined){allDayDefault=firstDefined(source?source.allDayDefault:undefined,options.allDayDefault);if(allDayDefault!==undefined){allDay=allDayDefault;}
else{allDay=!start.hasTime()&&(!end||!end.hasTime());}}
if(allDay){if(start.hasTime()){start.stripTime();}
if(end&&end.hasTime()){end.stripTime();}}
else{if(!start.hasTime()){start=t.rezoneDate(start);}
if(end&&!end.hasTime()){end=t.rezoneDate(end);}}
$.extend(out,data);if(source){out.source=source;}
out._id=data._id||(data.id===undefined?'_fc'+eventGUID++:data.id+'');if(data.className){if(typeof data.className=='string'){out.className=data.className.split(/\s+/);}
else{out.className=data.className;}}
else{out.className=[];}
out.allDay=allDay;out.start=start;out.end=end;if(options.forceEventDuration&&!out.end){out.end=getEventEnd(out);}
backupEventDates(out);return out;}
function mutateEvent(event,newStart,newEnd){var oldAllDay=event._allDay;var oldStart=event._start;var oldEnd=event._end;var clearEnd=false;var newAllDay;var dateDelta;var durationDelta;var undoFunc;if(!newStart&&!newEnd){newStart=event.start;newEnd=event.end;}
if(event.allDay!=oldAllDay){newAllDay=event.allDay;}
else{newAllDay=!(newStart||newEnd).hasTime();}
if(newAllDay){if(newStart){newStart=newStart.clone().stripTime();}
if(newEnd){newEnd=newEnd.clone().stripTime();}}
if(newStart){if(newAllDay){dateDelta=dayishDiff(newStart,oldStart.clone().stripTime());}
else{dateDelta=dayishDiff(newStart,oldStart);}}
if(newAllDay!=oldAllDay){clearEnd=true;}
else if(newEnd){durationDelta=dayishDiff(newEnd||t.getDefaultEventEnd(newAllDay,newStart||oldStart),newStart||oldStart).subtract(dayishDiff(oldEnd||t.getDefaultEventEnd(oldAllDay,oldStart),oldStart));}
undoFunc=mutateEvents(clientEvents(event._id),clearEnd,newAllDay,dateDelta,durationDelta);return{dateDelta:dateDelta,durationDelta:durationDelta,undo:undoFunc};}
function mutateEvents(events,clearEnd,forceAllDay,dateDelta,durationDelta){var isAmbigTimezone=t.getIsAmbigTimezone();var undoFunctions=[];$.each(events,function(i,event){var oldAllDay=event._allDay;var oldStart=event._start;var oldEnd=event._end;var newAllDay=forceAllDay!=null?forceAllDay:oldAllDay;var newStart=oldStart.clone();var newEnd=(!clearEnd&&oldEnd)?oldEnd.clone():null;if(newAllDay){newStart.stripTime();if(newEnd){newEnd.stripTime();}}
else{if(!newStart.hasTime()){newStart=t.rezoneDate(newStart);}
if(newEnd&&!newEnd.hasTime()){newEnd=t.rezoneDate(newEnd);}}
if(!newEnd&&(options.forceEventDuration||+durationDelta)){newEnd=t.getDefaultEventEnd(newAllDay,newStart);}
newStart.add(dateDelta);if(newEnd){newEnd.add(dateDelta).add(durationDelta);}
if(isAmbigTimezone){if(+dateDelta||+durationDelta){newStart.stripZone();if(newEnd){newEnd.stripZone();}}}
event.allDay=newAllDay;event.start=newStart;event.end=newEnd;backupEventDates(event);undoFunctions.push(function(){event.allDay=oldAllDay;event.start=oldStart;event.end=oldEnd;backupEventDates(event);});});return function(){for(var i=0;i<undoFunctions.length;i++){undoFunctions[i]();}};}}
function backupEventDates(event){event._allDay=event.allDay;event._start=event.start.clone();event._end=event.end?event.end.clone():null;};;fc.applyAll=applyAll;function createObject(proto){var f=function(){};f.prototype=proto;return new f();}
function extend(a,b){for(var i in b){if(b.hasOwnProperty(i)){a[i]=b[i];}}}
var dayIDs=['sun','mon','tue','wed','thu','fri','sat'];function dayishDiff(d1,d0){return moment.duration({days:d1.clone().stripTime().diff(d0.clone().stripTime(),'days'),ms:d1.time()-d0.time()});}
function isNativeDate(input){return Object.prototype.toString.call(input)==='[object Date]'||input instanceof Date;}
function lazySegBind(container,segs,bindHandlers){container.unbind('mouseover').mouseover(function(ev){var parent=ev.target,e,i,seg;while(parent!=this){e=parent;parent=parent.parentNode;}
if((i=e._fci)!==undefined){e._fci=undefined;seg=segs[i];bindHandlers(seg.event,seg.element,seg);$(ev.target).trigger(ev);}
ev.stopPropagation();});}
function setOuterWidth(element,width,includeMargins){for(var i=0,e;i<element.length;i++){e=$(element[i]);e.width(Math.max(0,width-hsides(e,includeMargins)));}}
function setOuterHeight(element,height,includeMargins){for(var i=0,e;i<element.length;i++){e=$(element[i]);e.height(Math.max(0,height-vsides(e,includeMargins)));}}
function hsides(element,includeMargins){return hpadding(element)+hborders(element)+(includeMargins?hmargins(element):0);}
function hpadding(element){return(parseFloat($.css(element[0],'paddingLeft',true))||0)+
(parseFloat($.css(element[0],'paddingRight',true))||0);}
function hmargins(element){return(parseFloat($.css(element[0],'marginLeft',true))||0)+
(parseFloat($.css(element[0],'marginRight',true))||0);}
function hborders(element){return(parseFloat($.css(element[0],'borderLeftWidth',true))||0)+
(parseFloat($.css(element[0],'borderRightWidth',true))||0);}
function vsides(element,includeMargins){return vpadding(element)+vborders(element)+(includeMargins?vmargins(element):0);}
function vpadding(element){return(parseFloat($.css(element[0],'paddingTop',true))||0)+
(parseFloat($.css(element[0],'paddingBottom',true))||0);}
function vmargins(element){return(parseFloat($.css(element[0],'marginTop',true))||0)+
(parseFloat($.css(element[0],'marginBottom',true))||0);}
function vborders(element){return(parseFloat($.css(element[0],'borderTopWidth',true))||0)+
(parseFloat($.css(element[0],'borderBottomWidth',true))||0);}
function noop(){}
function dateCompare(a,b){return a-b;}
function arrayMax(a){return Math.max.apply(Math,a);}
function smartProperty(obj,name){obj=obj||{};if(obj[name]!==undefined){return obj[name];}
var parts=name.split(/(?=[A-Z])/),i=parts.length-1,res;for(;i>=0;i--){res=obj[parts[i].toLowerCase()];if(res!==undefined){return res;}}
return obj['default'];}
function htmlEscape(s){return(s+'').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/'/g,'&#039;').replace(/"/g,'&quot;').replace(/\n/g,'<br />');}
function stripHTMLEntities(text){return text.replace(/&.*?;/g,'');}
function disableTextSelection(element){element.attr('unselectable','on').css('MozUserSelect','none').bind('selectstart.ui',function(){return false;});}
function markFirstLast(e){e.children().removeClass('fc-first fc-last').filter(':first-child').addClass('fc-first').end().filter(':last-child').addClass('fc-last');}
function getSkinCss(event,opt){var source=event.source||{};var eventColor=event.color;var sourceColor=source.color;var optionColor=opt('eventColor');var backgroundColor=event.backgroundColor||eventColor||source.backgroundColor||sourceColor||opt('eventBackgroundColor')||optionColor;var borderColor=event.borderColor||eventColor||source.borderColor||sourceColor||opt('eventBorderColor')||optionColor;var textColor=event.textColor||source.textColor||opt('eventTextColor');var statements=[];if(backgroundColor){statements.push('background-color:'+backgroundColor);}
if(borderColor){statements.push('border-color:'+borderColor);}
if(textColor){statements.push('color:'+textColor);}
return statements.join(';');}
function applyAll(functions,thisObj,args){if($.isFunction(functions)){functions=[functions];}
if(functions){var i;var ret;for(i=0;i<functions.length;i++){ret=functions[i].apply(thisObj,args)||ret;}
return ret;}}
function firstDefined(){for(var i=0;i<arguments.length;i++){if(arguments[i]!==undefined){return arguments[i];}}};;var ambigDateOfMonthRegex=/^\s*\d{4}-\d\d$/;var ambigTimeOrZoneRegex=/^\s*\d{4}-(?:(\d\d-\d\d)|(W\d\d$)|(W\d\d-\d)|(\d\d\d))((T| )(\d\d(:\d\d(:\d\d(\.\d+)?)?)?)?)?$/;fc.moment=function(){return makeMoment(arguments);};fc.moment.utc=function(){var mom=makeMoment(arguments,true);if(mom.hasTime()){mom.utc();}
return mom;};fc.moment.parseZone=function(){return makeMoment(arguments,true,true);};function makeMoment(args,parseAsUTC,parseZone){var input=args[0];var isSingleString=args.length==1&&typeof input==='string';var isAmbigTime;var isAmbigZone;var ambigMatch;var output;if(moment.isMoment(input)){output=moment.apply(null,args);if(input._ambigTime){output._ambigTime=true;}
if(input._ambigZone){output._ambigZone=true;}}
else if(isNativeDate(input)||input===undefined){output=moment.apply(null,args);}
else{isAmbigTime=false;isAmbigZone=false;if(isSingleString){if(ambigDateOfMonthRegex.test(input)){input+='-01';args=[input];isAmbigTime=true;isAmbigZone=true;}
else if((ambigMatch=ambigTimeOrZoneRegex.exec(input))){isAmbigTime=!ambigMatch[5];isAmbigZone=true;}}
else if($.isArray(input)){isAmbigZone=true;}
if(parseAsUTC){output=moment.utc.apply(moment,args);}
else{output=moment.apply(null,args);}
if(isAmbigTime){output._ambigTime=true;output._ambigZone=true;}
else if(parseZone){if(isAmbigZone){output._ambigZone=true;}
else if(isSingleString){output.zone(input);}}}
return new FCMoment(output);}
function FCMoment(internalData){extend(this,internalData);}
FCMoment.prototype=createObject(moment.fn);FCMoment.prototype.clone=function(){return makeMoment([this]);};FCMoment.prototype.time=function(time){if(time==null){return moment.duration({hours:this.hours(),minutes:this.minutes(),seconds:this.seconds(),milliseconds:this.milliseconds()});}
else{delete this._ambigTime;if(!moment.isDuration(time)&&!moment.isMoment(time)){time=moment.duration(time);}
var dayHours=0;if(moment.isDuration(time)){dayHours=Math.floor(time.asDays())*24;}
return this.hours(dayHours+time.hours()).minutes(time.minutes()).seconds(time.seconds()).milliseconds(time.milliseconds());}};FCMoment.prototype.stripTime=function(){var a=this.toArray();moment.fn.utc.call(this);this.year(a[0]).month(a[1]).date(a[2]).hours(0).minutes(0).seconds(0).milliseconds(0);this._ambigTime=true;this._ambigZone=true;return this;};FCMoment.prototype.hasTime=function(){return!this._ambigTime;};FCMoment.prototype.stripZone=function(){var a=this.toArray();var wasAmbigTime=this._ambigTime;moment.fn.utc.call(this);this.year(a[0]).month(a[1]).date(a[2]).hours(a[3]).minutes(a[4]).seconds(a[5]).milliseconds(a[6]);if(wasAmbigTime){this._ambigTime=true;}
this._ambigZone=true;return this;};FCMoment.prototype.hasZone=function(){return!this._ambigZone;};FCMoment.prototype.zone=function(tzo){if(tzo!=null){delete this._ambigTime;delete this._ambigZone;}
return moment.fn.zone.apply(this,arguments);};FCMoment.prototype.local=function(){var a=this.toArray();var wasAmbigZone=this._ambigZone;delete this._ambigTime;delete this._ambigZone;moment.fn.local.apply(this,arguments);if(wasAmbigZone){this.year(a[0]).month(a[1]).date(a[2]).hours(a[3]).minutes(a[4]).seconds(a[5]).milliseconds(a[6]);}
return this;};FCMoment.prototype.utc=function(){delete this._ambigTime;delete this._ambigZone;return moment.fn.utc.apply(this,arguments);};FCMoment.prototype.format=function(){if(arguments[0]){return formatDate(this,arguments[0]);}
if(this._ambigTime){return momentFormat(this,'YYYY-MM-DD');}
if(this._ambigZone){return momentFormat(this,'YYYY-MM-DD[T]HH:mm:ss');}
return momentFormat(this);};FCMoment.prototype.toISOString=function(){if(this._ambigTime){return momentFormat(this,'YYYY-MM-DD');}
if(this._ambigZone){return momentFormat(this,'YYYY-MM-DD[T]HH:mm:ss');}
return moment.fn.toISOString.apply(this,arguments);};FCMoment.prototype.isWithin=function(start,end){var a=commonlyAmbiguate([this,start,end]);return a[0]>=a[1]&&a[0]<a[2];};$.each(['isBefore','isAfter','isSame'],function(i,methodName){FCMoment.prototype[methodName]=function(input,units){var a=commonlyAmbiguate([this,input]);return moment.fn[methodName].call(a[0],a[1],units);};});function commonlyAmbiguate(inputs){var outputs=[];var anyAmbigTime=false;var anyAmbigZone=false;var i;for(i=0;i<inputs.length;i++){outputs.push(fc.moment(inputs[i]));anyAmbigTime=anyAmbigTime||outputs[i]._ambigTime;anyAmbigZone=anyAmbigZone||outputs[i]._ambigZone;}
for(i=0;i<outputs.length;i++){if(anyAmbigTime){outputs[i].stripTime();}
else if(anyAmbigZone){outputs[i].stripZone();}}
return outputs;};;function momentFormat(mom,formatStr){return moment.fn.format.call(mom,formatStr);}
function formatDate(date,formatStr){return formatDateWithChunks(date,getFormatStringChunks(formatStr));}
function formatDateWithChunks(date,chunks){var s='';var i;for(i=0;i<chunks.length;i++){s+=formatDateWithChunk(date,chunks[i]);}
return s;}
var tokenOverrides={t:function(date){return momentFormat(date,'a').charAt(0);},T:function(date){return momentFormat(date,'A').charAt(0);}};function formatDateWithChunk(date,chunk){var token;var maybeStr;if(typeof chunk==='string'){return chunk;}
else if((token=chunk.token)){if(tokenOverrides[token]){return tokenOverrides[token](date);}
return momentFormat(date,token);}
else if(chunk.maybe){maybeStr=formatDateWithChunks(date,chunk.maybe);if(maybeStr.match(/[1-9]/)){return maybeStr;}}
return '';}
function formatRange(date1,date2,formatStr,separator,isRTL){var localeData;date1=fc.moment.parseZone(date1);date2=fc.moment.parseZone(date2);localeData=(date1.localeData||date1.lang).call(date1);formatStr=localeData.longDateFormat(formatStr)||formatStr;separator=separator||' - ';return formatRangeWithChunks(date1,date2,getFormatStringChunks(formatStr),separator,isRTL);}
fc.formatRange=formatRange;function formatRangeWithChunks(date1,date2,chunks,separator,isRTL){var chunkStr;var leftI;var leftStr='';var rightI;var rightStr='';var middleI;var middleStr1='';var middleStr2='';var middleStr='';for(leftI=0;leftI<chunks.length;leftI++){chunkStr=formatSimilarChunk(date1,date2,chunks[leftI]);if(chunkStr===false){break;}
leftStr+=chunkStr;}
for(rightI=chunks.length-1;rightI>leftI;rightI--){chunkStr=formatSimilarChunk(date1,date2,chunks[rightI]);if(chunkStr===false){break;}
rightStr=chunkStr+rightStr;}
for(middleI=leftI;middleI<=rightI;middleI++){middleStr1+=formatDateWithChunk(date1,chunks[middleI]);middleStr2+=formatDateWithChunk(date2,chunks[middleI]);}
if(middleStr1||middleStr2){if(isRTL){middleStr=middleStr2+separator+middleStr1;}
else{middleStr=middleStr1+separator+middleStr2;}}
return leftStr+middleStr+rightStr;}
var similarUnitMap={Y:'year',M:'month',D:'day',d:'day',A:'second',a:'second',T:'second',t:'second',H:'second',h:'second',m:'second',s:'second'};function formatSimilarChunk(date1,date2,chunk){var token;var unit;if(typeof chunk==='string'){return chunk;}
else if((token=chunk.token)){unit=similarUnitMap[token.charAt(0)];if(unit&&date1.isSame(date2,unit)){return momentFormat(date1,token);}}
return false;}
var formatStringChunkCache={};function getFormatStringChunks(formatStr){if(formatStr in formatStringChunkCache){return formatStringChunkCache[formatStr];}
return(formatStringChunkCache[formatStr]=chunkFormatString(formatStr));}
function chunkFormatString(formatStr){var chunks=[];var chunker=/\[([^\]]*)\]|\(([^\)]*)\)|(LT|(\w)\4*o?)|([^\w\[\(]+)/g;var match;while((match=chunker.exec(formatStr))){if(match[1]){chunks.push(match[1]);}
else if(match[2]){chunks.push({maybe:chunkFormatString(match[2])});}
else if(match[3]){chunks.push({token:match[3]});}
else if(match[5]){chunks.push(match[5]);}}
return chunks;};;fcViews.month=MonthView;function MonthView(element,calendar){var t=this;t.incrementDate=incrementDate;t.render=render;BasicView.call(t,element,calendar,'month');function incrementDate(date,delta){return date.clone().stripTime().add(delta,'months').startOf('month');}
function render(date){t.intervalStart=date.clone().stripTime().startOf('month');t.intervalEnd=t.intervalStart.clone().add(1,'months');t.start=t.intervalStart.clone();t.start=t.skipHiddenDays(t.start);t.start.startOf('week');t.start=t.skipHiddenDays(t.start);t.end=t.intervalEnd.clone();t.end=t.skipHiddenDays(t.end,-1,true);t.end.add((7-t.end.weekday())%7,'days');t.end=t.skipHiddenDays(t.end,-1,true);var rowCnt=Math.ceil(t.end.diff(t.start,'weeks',true));if(t.opt('weekMode')=='fixed'){t.end.add(6-rowCnt,'weeks');rowCnt=6;}
t.title=calendar.formatDate(t.intervalStart,t.opt('titleFormat'));t.renderBasic(rowCnt,t.getCellsPerWeek(),true);}};;fcViews.basicWeek=BasicWeekView;function BasicWeekView(element,calendar){var t=this;t.incrementDate=incrementDate;t.render=render;BasicView.call(t,element,calendar,'basicWeek');function incrementDate(date,delta){return date.clone().stripTime().add(delta,'weeks').startOf('week');}
function render(date){t.intervalStart=date.clone().stripTime().startOf('week');t.intervalEnd=t.intervalStart.clone().add(1,'weeks');t.start=t.skipHiddenDays(t.intervalStart);t.end=t.skipHiddenDays(t.intervalEnd,-1,true);t.title=calendar.formatRange(t.start,t.end.clone().subtract(1),t.opt('titleFormat'),' \u2014 ');t.renderBasic(1,t.getCellsPerWeek(),false);}};;fcViews.basicDay=BasicDayView;function BasicDayView(element,calendar){var t=this;t.incrementDate=incrementDate;t.render=render;BasicView.call(t,element,calendar,'basicDay');function incrementDate(date,delta){var out=date.clone().stripTime().add(delta,'days');out=t.skipHiddenDays(out,delta<0?-1:1);return out;}
function render(date){t.start=t.intervalStart=date.clone().stripTime();t.end=t.intervalEnd=t.start.clone().add(1,'days');t.title=calendar.formatDate(t.start,t.opt('titleFormat'));t.renderBasic(1,1,false);}};;setDefaults({weekMode:'fixed'});function BasicView(element,calendar,viewName){var t=this;t.renderBasic=renderBasic;t.setHeight=setHeight;t.setWidth=setWidth;t.renderDayOverlay=renderDayOverlay;t.defaultSelectionEnd=defaultSelectionEnd;t.renderSelection=renderSelection;t.clearSelection=clearSelection;t.reportDayClick=reportDayClick;t.dragStart=dragStart;t.dragStop=dragStop;t.getHoverListener=function(){return hoverListener;};t.colLeft=colLeft;t.colRight=colRight;t.colContentLeft=colContentLeft;t.colContentRight=colContentRight;t.getIsCellAllDay=function(){return true;};t.allDayRow=allDayRow;t.getRowCnt=function(){return rowCnt;};t.getColCnt=function(){return colCnt;};t.getColWidth=function(){return colWidth;};t.getDaySegmentContainer=function(){return daySegmentContainer;};View.call(t,element,calendar,viewName);OverlayManager.call(t);SelectionManager.call(t);BasicEventRenderer.call(t);var opt=t.opt;var trigger=t.trigger;var renderOverlay=t.renderOverlay;var clearOverlays=t.clearOverlays;var daySelectionMousedown=t.daySelectionMousedown;var cellToDate=t.cellToDate;var dateToCell=t.dateToCell;var rangeToSegments=t.rangeToSegments;var formatDate=calendar.formatDate;var calculateWeekNumber=calendar.calculateWeekNumber;var table;var head;var headCells;var body;var bodyRows;var bodyCells;var bodyFirstCells;var firstRowCellInners;var firstRowCellContentInners;var daySegmentContainer;var viewWidth;var viewHeight;var colWidth;var weekNumberWidth;var rowCnt,colCnt;var showNumbers;var coordinateGrid;var hoverListener;var colPositions;var colContentPositions;var tm;var colFormat;var showWeekNumbers;disableTextSelection(element.addClass('fc-grid'));function renderBasic(_rowCnt,_colCnt,_showNumbers){rowCnt=_rowCnt;colCnt=_colCnt;showNumbers=_showNumbers;updateOptions();if(!body){buildEventContainer();}
buildTable();}
function updateOptions(){tm=opt('theme')?'ui':'fc';colFormat=opt('columnFormat');showWeekNumbers=opt('weekNumbers');}
function buildEventContainer(){daySegmentContainer=$("<div class='fc-event-container' style='position:absolute;z-index:8;top:0;left:0'/>").appendTo(element);}
function buildTable(){var html=buildTableHTML();if(table){table.remove();}
table=$(html).appendTo(element);head=table.find('thead');headCells=head.find('.fc-day-header');body=table.find('tbody');bodyRows=body.find('tr');bodyCells=body.find('.fc-day');bodyFirstCells=bodyRows.find('td:first-child');firstRowCellInners=bodyRows.eq(0).find('.fc-day > div');firstRowCellContentInners=bodyRows.eq(0).find('.fc-day-content > div');markFirstLast(head.add(head.find('tr')));markFirstLast(bodyRows);bodyRows.eq(0).addClass('fc-first');bodyRows.filter(':last').addClass('fc-last');bodyCells.each(function(i,_cell){var date=cellToDate(Math.floor(i/colCnt),i%colCnt);trigger('dayRender',t,date,$(_cell));});dayBind(bodyCells);}
function buildTableHTML(){var html="<table class='fc-border-separate' style='width:100%' cellspacing='0'>"+
buildHeadHTML()+
buildBodyHTML()+
"</table>";return html;}
function buildHeadHTML(){var headerClass=tm+"-widget-header";var html='';var col;var date;html+="<thead><tr>";if(showWeekNumbers){html+="<th class='fc-week-number "+headerClass+"'>"+
htmlEscape(opt('weekNumberTitle'))+
"</th>";}
for(col=0;col<colCnt;col++){date=cellToDate(0,col);html+="<th class='fc-day-header fc-"+dayIDs[date.day()]+" "+headerClass+"'>"+
htmlEscape(formatDate(date,colFormat))+
"</th>";}
html+="</tr></thead>";return html;}
function buildBodyHTML(){var contentClass=tm+"-widget-content";var html='';var row;var col;var date;html+="<tbody>";for(row=0;row<rowCnt;row++){html+="<tr class='fc-week'>";if(showWeekNumbers){date=cellToDate(row,0);html+="<td class='fc-week-number "+contentClass+"'>"+
"<div>"+
htmlEscape(calculateWeekNumber(date))+
"</div>"+
"</td>";}
for(col=0;col<colCnt;col++){date=cellToDate(row,col);html+=buildCellHTML(date);}
html+="</tr>";}
html+="</tbody>";return html;}
function buildCellHTML(date){var month=t.intervalStart.month();var today=calendar.getNow().stripTime();var html='';var contentClass=tm+"-widget-content";var classNames=['fc-day','fc-'+dayIDs[date.day()],contentClass];if(date.month()!=month){classNames.push('fc-other-month');}
if(date.isSame(today,'day')){classNames.push('fc-today',tm+'-state-highlight');}
else if(date<today){classNames.push('fc-past');}
else{classNames.push('fc-future');}
html+="<td"+
" class='"+classNames.join(' ')+"'"+
" data-date='"+date.format()+"'"+
">"+
"<div>";if(showNumbers){html+="<div class='fc-day-number'>"+date.date()+"</div>";}
html+="<div class='fc-day-content'>"+
"<div style='position:relative'>&nbsp;</div>"+
"</div>"+
"</div>"+
"</td>";return html;}
function setHeight(height){viewHeight=height;var bodyHeight=Math.max(viewHeight-head.height(),0);var rowHeight;var rowHeightLast;var cell;if(opt('weekMode')=='variable'){rowHeight=rowHeightLast=Math.floor(bodyHeight/(rowCnt==1?2:6));}else{rowHeight=Math.floor(bodyHeight/rowCnt);rowHeightLast=bodyHeight-rowHeight*(rowCnt-1);}
bodyFirstCells.each(function(i,_cell){if(i<rowCnt){cell=$(_cell);cell.find('> div').css('min-height',(i==rowCnt-1?rowHeightLast:rowHeight)-vsides(cell));}});}
function setWidth(width){viewWidth=width;colPositions.clear();colContentPositions.clear();weekNumberWidth=0;if(showWeekNumbers){weekNumberWidth=head.find('th.fc-week-number').outerWidth();}
colWidth=Math.floor((viewWidth-weekNumberWidth)/colCnt);setOuterWidth(headCells.slice(0,-1),colWidth);}
function dayBind(days){days.click(dayClick).mousedown(daySelectionMousedown);}
function dayClick(ev){if(!opt('selectable')){var date=calendar.moment($(this).data('date'));trigger('dayClick',this,date,ev);}}
function renderDayOverlay(overlayStart,overlayEnd,refreshCoordinateGrid){if(refreshCoordinateGrid){coordinateGrid.build();}
var segments=rangeToSegments(overlayStart,overlayEnd);for(var i=0;i<segments.length;i++){var segment=segments[i];dayBind(renderCellOverlay(segment.row,segment.leftCol,segment.row,segment.rightCol));}}
function renderCellOverlay(row0,col0,row1,col1){var rect=coordinateGrid.rect(row0,col0,row1,col1,element);return renderOverlay(rect,element);}
function defaultSelectionEnd(start){return start.clone().stripTime().add(1,'days');}
function renderSelection(start,end){renderDayOverlay(start,end,true);}
function clearSelection(){clearOverlays();}
function reportDayClick(date,ev){var cell=dateToCell(date);var _element=bodyCells[cell.row*colCnt+cell.col];trigger('dayClick',_element,date,ev);}
function dragStart(_dragElement,ev,ui){hoverListener.start(function(cell){clearOverlays();if(cell){var d1=cellToDate(cell);var d2=d1.clone().add(calendar.defaultAllDayEventDuration);renderDayOverlay(d1,d2);}},ev);}
function dragStop(_dragElement,ev,ui){var cell=hoverListener.stop();clearOverlays();if(cell){trigger('drop',_dragElement,cellToDate(cell),ev,ui);}}
coordinateGrid=new CoordinateGrid(function(rows,cols){var e,n,p;headCells.each(function(i,_e){e=$(_e);n=e.offset().left;if(i){p[1]=n;}
p=[n];cols[i]=p;});p[1]=n+e.outerWidth();bodyRows.each(function(i,_e){if(i<rowCnt){e=$(_e);n=e.offset().top;if(i){p[1]=n;}
p=[n];rows[i]=p;}});p[1]=n+e.outerHeight();});hoverListener=new HoverListener(coordinateGrid);colPositions=new HorizontalPositionCache(function(col){return firstRowCellInners.eq(col);});colContentPositions=new HorizontalPositionCache(function(col){return firstRowCellContentInners.eq(col);});function colLeft(col){return colPositions.left(col);}
function colRight(col){return colPositions.right(col);}
function colContentLeft(col){return colContentPositions.left(col);}
function colContentRight(col){return colContentPositions.right(col);}
function allDayRow(i){return bodyRows.eq(i);}};;function BasicEventRenderer(){var t=this;t.renderEvents=renderEvents;t.clearEvents=clearEvents;DayEventRenderer.call(t);function renderEvents(events,modifiedEventId){t.renderDayEvents(events,modifiedEventId);}
function clearEvents(){t.getDaySegmentContainer().empty();}};;fcViews.agendaWeek=AgendaWeekView;function AgendaWeekView(element,calendar){var t=this;t.incrementDate=incrementDate;t.render=render;AgendaView.call(t,element,calendar,'agendaWeek');function incrementDate(date,delta){return date.clone().stripTime().add(delta,'weeks').startOf('week');}
function render(date){t.intervalStart=date.clone().stripTime().startOf('week');t.intervalEnd=t.intervalStart.clone().add(1,'weeks');t.start=t.skipHiddenDays(t.intervalStart);t.end=t.skipHiddenDays(t.intervalEnd,-1,true);t.title=calendar.formatRange(t.start,t.end.clone().subtract(1),t.opt('titleFormat'),' \u2014 ');t.renderAgenda(t.getCellsPerWeek());}};;fcViews.agendaDay=AgendaDayView;function AgendaDayView(element,calendar){var t=this;t.incrementDate=incrementDate;t.render=render;AgendaView.call(t,element,calendar,'agendaDay');function incrementDate(date,delta){var out=date.clone().stripTime().add(delta,'days');out=t.skipHiddenDays(out,delta<0?-1:1);return out;}
function render(date){t.start=t.intervalStart=date.clone().stripTime();t.end=t.intervalEnd=t.start.clone().add(1,'days');t.title=calendar.formatDate(t.start,t.opt('titleFormat'));t.renderAgenda(1);}};;setDefaults({allDaySlot:true,allDayText:'all-day',scrollTime:'06:00:00',slotDuration:'00:30:00',axisFormat:generateAgendaAxisFormat,timeFormat:{agenda:generateAgendaTimeFormat},dragOpacity:{agenda:.5},minTime:'00:00:00',maxTime:'24:00:00',slotEventOverlap:true});function generateAgendaAxisFormat(options,langData){return langData.longDateFormat('LT').replace(':mm','(:mm)').replace(/(\Wmm)$/,'($1)').replace(/\s*a$/i,'a');}
function generateAgendaTimeFormat(options,langData){return langData.longDateFormat('LT').replace(/\s*a$/i,'');}
function AgendaView(element,calendar,viewName){var t=this;t.renderAgenda=renderAgenda;t.setWidth=setWidth;t.setHeight=setHeight;t.afterRender=afterRender;t.computeDateTop=computeDateTop;t.getIsCellAllDay=getIsCellAllDay;t.allDayRow=function(){return allDayRow;};t.getCoordinateGrid=function(){return coordinateGrid;};t.getHoverListener=function(){return hoverListener;};t.colLeft=colLeft;t.colRight=colRight;t.colContentLeft=colContentLeft;t.colContentRight=colContentRight;t.getDaySegmentContainer=function(){return daySegmentContainer;};t.getSlotSegmentContainer=function(){return slotSegmentContainer;};t.getSlotContainer=function(){return slotContainer;};t.getRowCnt=function(){return 1;};t.getColCnt=function(){return colCnt;};t.getColWidth=function(){return colWidth;};t.getSnapHeight=function(){return snapHeight;};t.getSnapDuration=function(){return snapDuration;};t.getSlotHeight=function(){return slotHeight;};t.getSlotDuration=function(){return slotDuration;};t.getMinTime=function(){return minTime;};t.getMaxTime=function(){return maxTime;};t.defaultSelectionEnd=defaultSelectionEnd;t.renderDayOverlay=renderDayOverlay;t.renderSelection=renderSelection;t.clearSelection=clearSelection;t.reportDayClick=reportDayClick;t.dragStart=dragStart;t.dragStop=dragStop;View.call(t,element,calendar,viewName);OverlayManager.call(t);SelectionManager.call(t);AgendaEventRenderer.call(t);var opt=t.opt;var trigger=t.trigger;var renderOverlay=t.renderOverlay;var clearOverlays=t.clearOverlays;var reportSelection=t.reportSelection;var unselect=t.unselect;var daySelectionMousedown=t.daySelectionMousedown;var slotSegHtml=t.slotSegHtml;var cellToDate=t.cellToDate;var dateToCell=t.dateToCell;var rangeToSegments=t.rangeToSegments;var formatDate=calendar.formatDate;var calculateWeekNumber=calendar.calculateWeekNumber;var dayTable;var dayHead;var dayHeadCells;var dayBody;var dayBodyCells;var dayBodyCellInners;var dayBodyCellContentInners;var dayBodyFirstCell;var dayBodyFirstCellStretcher;var slotLayer;var daySegmentContainer;var allDayTable;var allDayRow;var slotScroller;var slotContainer;var slotSegmentContainer;var slotTable;var selectionHelper;var viewWidth;var viewHeight;var axisWidth;var colWidth;var gutterWidth;var slotDuration;var slotHeight;var snapDuration;var snapRatio;var snapHeight;var colCnt;var slotCnt;var coordinateGrid;var hoverListener;var colPositions;var colContentPositions;var slotTopCache={};var tm;var rtl;var minTime;var maxTime;var colFormat;disableTextSelection(element.addClass('fc-agenda'));function renderAgenda(c){colCnt=c;updateOptions();if(!dayTable){buildSkeleton();}
else{buildDayTable();}}
function updateOptions(){tm=opt('theme')?'ui':'fc';rtl=opt('isRTL');colFormat=opt('columnFormat');minTime=moment.duration(opt('minTime'));maxTime=moment.duration(opt('maxTime'));slotDuration=moment.duration(opt('slotDuration'));snapDuration=opt('snapDuration');snapDuration=snapDuration?moment.duration(snapDuration):slotDuration;}
function buildSkeleton(){var s;var headerClass=tm+"-widget-header";var contentClass=tm+"-widget-content";var slotTime;var slotDate;var minutes;var slotNormal=slotDuration.asMinutes()%15===0;buildDayTable();slotLayer=$("<div style='position:absolute;z-index:2;left:0;width:100%'/>").appendTo(element);if(opt('allDaySlot')){daySegmentContainer=$("<div class='fc-event-container' style='position:absolute;z-index:8;top:0;left:0'/>").appendTo(slotLayer);s="<table style='width:100%' class='fc-agenda-allday' cellspacing='0'>"+
"<tr>"+
"<th class='"+headerClass+" fc-agenda-axis'>"+
(opt('allDayHTML')||htmlEscape(opt('allDayText')))+
"</th>"+
"<td>"+
"<div class='fc-day-content'><div style='position:relative'/></div>"+
"</td>"+
"<th class='"+headerClass+" fc-agenda-gutter'>&nbsp;</th>"+
"</tr>"+
"</table>";allDayTable=$(s).appendTo(slotLayer);allDayRow=allDayTable.find('tr');dayBind(allDayRow.find('td'));slotLayer.append("<div class='fc-agenda-divider "+headerClass+"'>"+
"<div class='fc-agenda-divider-inner'/>"+
"</div>");}else{daySegmentContainer=$([]);}
slotScroller=$("<div style='position:absolute;width:100%;overflow-x:hidden;overflow-y:auto'/>").appendTo(slotLayer);slotContainer=$("<div style='position:relative;width:100%;overflow:hidden'/>").appendTo(slotScroller);slotSegmentContainer=$("<div class='fc-event-container' style='position:absolute;z-index:8;top:0;left:0'/>").appendTo(slotContainer);s="<table class='fc-agenda-slots' style='width:100%' cellspacing='0'>"+
"<tbody>";slotTime=moment.duration(+minTime);slotCnt=0;while(slotTime<maxTime){slotDate=t.start.clone().time(slotTime);minutes=slotDate.minutes();s+="<tr class='fc-slot"+slotCnt+' '+(!minutes?'':'fc-minor')+"'>"+
"<th class='fc-agenda-axis "+headerClass+"'>"+
((!slotNormal||!minutes)?htmlEscape(formatDate(slotDate,opt('axisFormat'))):'&nbsp;')+
"</th>"+
"<td class='"+contentClass+"'>"+
"<div style='position:relative'>&nbsp;</div>"+
"</td>"+
"</tr>";slotTime.add(slotDuration);slotCnt++;}
s+="</tbody>"+
"</table>";slotTable=$(s).appendTo(slotContainer);slotBind(slotTable.find('td'));}
function buildDayTable(){var html=buildDayTableHTML();if(dayTable){dayTable.remove();}
dayTable=$(html).appendTo(element);dayHead=dayTable.find('thead');dayHeadCells=dayHead.find('th').slice(1,-1);dayBody=dayTable.find('tbody');dayBodyCells=dayBody.find('td').slice(0,-1);dayBodyCellInners=dayBodyCells.find('> div');dayBodyCellContentInners=dayBodyCells.find('.fc-day-content > div');dayBodyFirstCell=dayBodyCells.eq(0);dayBodyFirstCellStretcher=dayBodyCellInners.eq(0);markFirstLast(dayHead.add(dayHead.find('tr')));markFirstLast(dayBody.add(dayBody.find('tr')));}
function buildDayTableHTML(){var html="<table style='width:100%' class='fc-agenda-days fc-border-separate' cellspacing='0'>"+
buildDayTableHeadHTML()+
buildDayTableBodyHTML()+
"</table>";return html;}
function buildDayTableHeadHTML(){var headerClass=tm+"-widget-header";var date;var html='';var weekText;var col;html+="<thead>"+
"<tr>";if(opt('weekNumbers')){date=cellToDate(0,0);weekText=calculateWeekNumber(date);if(rtl){weekText+=opt('weekNumberTitle');}
else{weekText=opt('weekNumberTitle')+weekText;}
html+="<th class='fc-agenda-axis fc-week-number "+headerClass+"'>"+
htmlEscape(weekText)+
"</th>";}
else{html+="<th class='fc-agenda-axis "+headerClass+"'>&nbsp;</th>";}
for(col=0;col<colCnt;col++){date=cellToDate(0,col);html+="<th class='fc-"+dayIDs[date.day()]+" fc-col"+col+' '+headerClass+"'>"+
htmlEscape(formatDate(date,colFormat))+
"</th>";}
html+="<th class='fc-agenda-gutter "+headerClass+"'>&nbsp;</th>"+
"</tr>"+
"</thead>";return html;}
function buildDayTableBodyHTML(){var headerClass=tm+"-widget-header";var contentClass=tm+"-widget-content";var date;var today=calendar.getNow().stripTime();var col;var cellsHTML;var cellHTML;var classNames;var html='';html+="<tbody>"+
"<tr>"+
"<th class='fc-agenda-axis "+headerClass+"'>&nbsp;</th>";cellsHTML='';for(col=0;col<colCnt;col++){date=cellToDate(0,col);classNames=['fc-col'+col,'fc-'+dayIDs[date.day()],contentClass];if(date.isSame(today,'day')){classNames.push(tm+'-state-highlight','fc-today');}
else if(date<today){classNames.push('fc-past');}
else{classNames.push('fc-future');}
cellHTML="<td class='"+classNames.join(' ')+"'>"+
"<div>"+
"<div class='fc-day-content'>"+
"<div style='position:relative'>&nbsp;</div>"+
"</div>"+
"</div>"+
"</td>";cellsHTML+=cellHTML;}
html+=cellsHTML;html+="<td class='fc-agenda-gutter "+contentClass+"'>&nbsp;</td>"+
"</tr>"+
"</tbody>";return html;}
function setHeight(height){if(height===undefined){height=viewHeight;}
viewHeight=height;slotTopCache={};var headHeight=dayBody.position().top;var allDayHeight=slotScroller.position().top;var bodyHeight=Math.min(height-headHeight,slotTable.height()+allDayHeight+1);dayBodyFirstCellStretcher.height(bodyHeight-vsides(dayBodyFirstCell));slotLayer.css('top',headHeight);slotScroller.height(bodyHeight-allDayHeight-1);var slotHeight0=slotTable.find('tr:first').height()+1;var slotHeight1=slotTable.find('tr:eq(1)').height();slotHeight=(slotHeight0+slotHeight1)/2;snapRatio=slotDuration/snapDuration;snapHeight=slotHeight/snapRatio;}
function setWidth(width){viewWidth=width;colPositions.clear();colContentPositions.clear();var axisFirstCells=dayHead.find('th:first');if(allDayTable){axisFirstCells=axisFirstCells.add(allDayTable.find('th:first'));}
axisFirstCells=axisFirstCells.add(slotTable.find('th:first'));axisWidth=0;setOuterWidth(axisFirstCells.width('').each(function(i,_cell){axisWidth=Math.max(axisWidth,$(_cell).outerWidth());}),axisWidth);var gutterCells=dayTable.find('.fc-agenda-gutter');if(allDayTable){gutterCells=gutterCells.add(allDayTable.find('th.fc-agenda-gutter'));}
var slotTableWidth=slotScroller[0].clientWidth;gutterWidth=slotScroller.width()-slotTableWidth;if(gutterWidth){setOuterWidth(gutterCells,gutterWidth);gutterCells.show().prev().removeClass('fc-last');}else{gutterCells.hide().prev().addClass('fc-last');}
colWidth=Math.floor((slotTableWidth-axisWidth)/colCnt);setOuterWidth(dayHeadCells.slice(0,-1),colWidth);}
function resetScroll(){var top=computeTimeTop(moment.duration(opt('scrollTime')))+1;function scroll(){slotScroller.scrollTop(top);}
scroll();setTimeout(scroll,0);}
function afterRender(){resetScroll();}
function dayBind(cells){cells.click(slotClick).mousedown(daySelectionMousedown);}
function slotBind(cells){cells.click(slotClick).mousedown(slotSelectionMousedown);}
function slotClick(ev){if(!opt('selectable')){var col=Math.min(colCnt-1,Math.floor((ev.pageX-dayTable.offset().left-axisWidth)/colWidth));var date=cellToDate(0,col);var match=this.parentNode.className.match(/fc-slot(\d+)/);if(match){var slotIndex=parseInt(match[1],10);date.add(minTime+slotIndex*slotDuration);date=calendar.rezoneDate(date);trigger('dayClick',dayBodyCells[col],date,ev);}else{trigger('dayClick',dayBodyCells[col],date,ev);}}}
function renderDayOverlay(overlayStart,overlayEnd,refreshCoordinateGrid){if(refreshCoordinateGrid){coordinateGrid.build();}
var segments=rangeToSegments(overlayStart,overlayEnd);for(var i=0;i<segments.length;i++){var segment=segments[i];dayBind(renderCellOverlay(segment.row,segment.leftCol,segment.row,segment.rightCol));}}
function renderCellOverlay(row0,col0,row1,col1){var rect=coordinateGrid.rect(row0,col0,row1,col1,slotLayer);return renderOverlay(rect,slotLayer);}
function renderSlotOverlay(overlayStart,overlayEnd){overlayStart=overlayStart.clone().stripZone();overlayEnd=overlayEnd.clone().stripZone();for(var i=0;i<colCnt;i++){var dayStart=cellToDate(0,i);var dayEnd=dayStart.clone().add(1,'days');var stretchStart=dayStart<overlayStart?overlayStart:dayStart;var stretchEnd=dayEnd<overlayEnd?dayEnd:overlayEnd;if(stretchStart<stretchEnd){var rect=coordinateGrid.rect(0,i,0,i,slotContainer);var top=computeDateTop(stretchStart,dayStart);var bottom=computeDateTop(stretchEnd,dayStart);rect.top=top;rect.height=bottom-top;slotBind(renderOverlay(rect,slotContainer));}}}
coordinateGrid=new CoordinateGrid(function(rows,cols){var e,n,p;dayHeadCells.each(function(i,_e){e=$(_e);n=e.offset().left;if(i){p[1]=n;}
p=[n];cols[i]=p;});p[1]=n+e.outerWidth();if(opt('allDaySlot')){e=allDayRow;n=e.offset().top;rows[0]=[n,n+e.outerHeight()];}
var slotTableTop=slotContainer.offset().top;var slotScrollerTop=slotScroller.offset().top;var slotScrollerBottom=slotScrollerTop+slotScroller.outerHeight();function constrain(n){return Math.max(slotScrollerTop,Math.min(slotScrollerBottom,n));}
for(var i=0;i<slotCnt*snapRatio;i++){rows.push([constrain(slotTableTop+snapHeight*i),constrain(slotTableTop+snapHeight*(i+1))]);}});hoverListener=new HoverListener(coordinateGrid);colPositions=new HorizontalPositionCache(function(col){return dayBodyCellInners.eq(col);});colContentPositions=new HorizontalPositionCache(function(col){return dayBodyCellContentInners.eq(col);});function colLeft(col){return colPositions.left(col);}
function colContentLeft(col){return colContentPositions.left(col);}
function colRight(col){return colPositions.right(col);}
function colContentRight(col){return colContentPositions.right(col);}
function getIsCellAllDay(cell){return opt('allDaySlot')&&!cell.row;}
function realCellToDate(cell){var date=cellToDate(0,cell.col);var snapIndex=cell.row;if(opt('allDaySlot')){snapIndex--;}
if(snapIndex>=0){date.time(moment.duration(minTime+snapIndex*snapDuration));date=calendar.rezoneDate(date);}
return date;}
function computeDateTop(date,startOfDayDate){return computeTimeTop(moment.duration(date.clone().stripZone()-startOfDayDate.clone().stripTime()));}
function computeTimeTop(time){if(time<minTime){return 0;}
if(time>=maxTime){return slotTable.height();}
var slots=(time-minTime)/slotDuration;var slotIndex=Math.floor(slots);var slotPartial=slots-slotIndex;var slotTop=slotTopCache[slotIndex];if(slotTop===undefined){slotTop=slotTopCache[slotIndex]=slotTable.find('tr').eq(slotIndex).find('td div')[0].offsetTop;}
var top=slotTop-1+
slotPartial*slotHeight;top=Math.max(top,0);return top;}
function defaultSelectionEnd(start){if(start.hasTime()){return start.clone().add(slotDuration);}
else{return start.clone().add(1,'days');}}
function renderSelection(start,end){if(start.hasTime()||end.hasTime()){renderSlotSelection(start,end);}
else if(opt('allDaySlot')){renderDayOverlay(start,end,true);}}
function renderSlotSelection(startDate,endDate){var helperOption=opt('selectHelper');coordinateGrid.build();if(helperOption){var col=dateToCell(startDate).col;if(col>=0&&col<colCnt){var rect=coordinateGrid.rect(0,col,0,col,slotContainer);var top=computeDateTop(startDate,startDate);var bottom=computeDateTop(endDate,startDate);if(bottom>top){rect.top=top;rect.height=bottom-top;rect.left+=2;rect.width-=5;if($.isFunction(helperOption)){var helperRes=helperOption(startDate,endDate);if(helperRes){rect.position='absolute';selectionHelper=$(helperRes).css(rect).appendTo(slotContainer);}}else{rect.isStart=true;rect.isEnd=true;selectionHelper=$(slotSegHtml({title:'',start:startDate,end:endDate,className:['fc-select-helper'],editable:false},rect));selectionHelper.css('opacity',opt('dragOpacity'));}
if(selectionHelper){slotBind(selectionHelper);slotContainer.append(selectionHelper);setOuterWidth(selectionHelper,rect.width,true);setOuterHeight(selectionHelper,rect.height,true);}}}}else{renderSlotOverlay(startDate,endDate);}}
function clearSelection(){clearOverlays();if(selectionHelper){selectionHelper.remove();selectionHelper=null;}}
function slotSelectionMousedown(ev){if(ev.which==1&&opt('selectable')){unselect(ev);var dates;hoverListener.start(function(cell,origCell){clearSelection();if(cell&&cell.col==origCell.col&&!getIsCellAllDay(cell)){var d1=realCellToDate(origCell);var d2=realCellToDate(cell);dates=[d1,d1.clone().add(snapDuration),d2,d2.clone().add(snapDuration)].sort(dateCompare);renderSlotSelection(dates[0],dates[3]);}else{dates=null;}},ev);$(document).one('mouseup',function(ev){hoverListener.stop();if(dates){if(+dates[0]==+dates[1]){reportDayClick(dates[0],ev);}
reportSelection(dates[0],dates[3],ev);}});}}
function reportDayClick(date,ev){trigger('dayClick',dayBodyCells[dateToCell(date).col],date,ev);}
function dragStart(_dragElement,ev,ui){hoverListener.start(function(cell){clearOverlays();if(cell){var d1=realCellToDate(cell);var d2=d1.clone();if(d1.hasTime()){d2.add(calendar.defaultTimedEventDuration);renderSlotOverlay(d1,d2);}
else{d2.add(calendar.defaultAllDayEventDuration);renderDayOverlay(d1,d2);}}},ev);}
function dragStop(_dragElement,ev,ui){var cell=hoverListener.stop();clearOverlays();if(cell){trigger('drop',_dragElement,realCellToDate(cell),ev,ui);}}};;function AgendaEventRenderer(){var t=this;t.renderEvents=renderEvents;t.clearEvents=clearEvents;t.slotSegHtml=slotSegHtml;DayEventRenderer.call(t);var opt=t.opt;var trigger=t.trigger;var isEventDraggable=t.isEventDraggable;var isEventResizable=t.isEventResizable;var eventElementHandlers=t.eventElementHandlers;var setHeight=t.setHeight;var getDaySegmentContainer=t.getDaySegmentContainer;var getSlotSegmentContainer=t.getSlotSegmentContainer;var getHoverListener=t.getHoverListener;var computeDateTop=t.computeDateTop;var getIsCellAllDay=t.getIsCellAllDay;var colContentLeft=t.colContentLeft;var colContentRight=t.colContentRight;var cellToDate=t.cellToDate;var getColCnt=t.getColCnt;var getColWidth=t.getColWidth;var getSnapHeight=t.getSnapHeight;var getSnapDuration=t.getSnapDuration;var getSlotHeight=t.getSlotHeight;var getSlotDuration=t.getSlotDuration;var getSlotContainer=t.getSlotContainer;var reportEventElement=t.reportEventElement;var showEvents=t.showEvents;var hideEvents=t.hideEvents;var eventDrop=t.eventDrop;var eventResize=t.eventResize;var renderDayOverlay=t.renderDayOverlay;var clearOverlays=t.clearOverlays;var renderDayEvents=t.renderDayEvents;var getMinTime=t.getMinTime;var getMaxTime=t.getMaxTime;var calendar=t.calendar;var formatDate=calendar.formatDate;var getEventEnd=calendar.getEventEnd;t.draggableDayEvent=draggableDayEvent;function renderEvents(events,modifiedEventId){var i,len=events.length,dayEvents=[],slotEvents=[];for(i=0;i<len;i++){if(events[i].allDay){dayEvents.push(events[i]);}else{slotEvents.push(events[i]);}}
if(opt('allDaySlot')){renderDayEvents(dayEvents,modifiedEventId);setHeight();}
renderSlotSegs(compileSlotSegs(slotEvents),modifiedEventId);}
function clearEvents(){getDaySegmentContainer().empty();getSlotSegmentContainer().empty();}
function compileSlotSegs(events){var colCnt=getColCnt(),minTime=getMinTime(),maxTime=getMaxTime(),cellDate,i,j,seg,colSegs,segs=[];for(i=0;i<colCnt;i++){cellDate=cellToDate(0,i);colSegs=sliceSegs(events,cellDate.clone().time(minTime),cellDate.clone().time(maxTime));colSegs=placeSlotSegs(colSegs);for(j=0;j<colSegs.length;j++){seg=colSegs[j];seg.col=i;segs.push(seg);}}
return segs;}
function sliceSegs(events,rangeStart,rangeEnd){rangeStart=rangeStart.clone().stripZone();rangeEnd=rangeEnd.clone().stripZone();var segs=[],i,len=events.length,event,eventStart,eventEnd,segStart,segEnd,isStart,isEnd;for(i=0;i<len;i++){event=events[i];eventStart=event.start.clone().stripZone();eventEnd=getEventEnd(event).stripZone();if(eventEnd>rangeStart&&eventStart<rangeEnd){if(eventStart<rangeStart){segStart=rangeStart.clone();isStart=false;}
else{segStart=eventStart;isStart=true;}
if(eventEnd>rangeEnd){segEnd=rangeEnd.clone();isEnd=false;}
else{segEnd=eventEnd;isEnd=true;}
segs.push({event:event,start:segStart,end:segEnd,isStart:isStart,isEnd:isEnd});}}
return segs.sort(compareSlotSegs);}
function renderSlotSegs(segs,modifiedEventId){var i,segCnt=segs.length,seg,event,top,bottom,columnLeft,columnRight,columnWidth,width,left,right,html='',eventElements,eventElement,triggerRes,titleElement,height,slotSegmentContainer=getSlotSegmentContainer(),isRTL=opt('isRTL');for(i=0;i<segCnt;i++){seg=segs[i];event=seg.event;top=computeDateTop(seg.start,seg.start);bottom=computeDateTop(seg.end,seg.start);columnLeft=colContentLeft(seg.col);columnRight=colContentRight(seg.col);columnWidth=columnRight-columnLeft;columnRight-=columnWidth*.025;columnWidth=columnRight-columnLeft;width=columnWidth*(seg.forwardCoord-seg.backwardCoord);if(opt('slotEventOverlap')){width=Math.max((width-(20/2))*2,width);}
if(isRTL){right=columnRight-seg.backwardCoord*columnWidth;left=right-width;}
else{left=columnLeft+seg.backwardCoord*columnWidth;right=left+width;}
left=Math.max(left,columnLeft);right=Math.min(right,columnRight);width=right-left;seg.top=top;seg.left=left;seg.outerWidth=width;seg.outerHeight=bottom-top;html+=slotSegHtml(event,seg);}
slotSegmentContainer[0].innerHTML=html;eventElements=slotSegmentContainer.children();for(i=0;i<segCnt;i++){seg=segs[i];event=seg.event;eventElement=$(eventElements[i]);triggerRes=trigger('eventRender',event,event,eventElement);if(triggerRes===false){eventElement.remove();}else{if(triggerRes&&triggerRes!==true){eventElement.remove();eventElement=$(triggerRes).css({position:'absolute',top:seg.top,left:seg.left}).appendTo(slotSegmentContainer);}
seg.element=eventElement;if(event._id===modifiedEventId){bindSlotSeg(event,eventElement,seg);}else{eventElement[0]._fci=i;}
reportEventElement(event,eventElement);}}
lazySegBind(slotSegmentContainer,segs,bindSlotSeg);for(i=0;i<segCnt;i++){seg=segs[i];if((eventElement=seg.element)){seg.vsides=vsides(eventElement,true);seg.hsides=hsides(eventElement,true);titleElement=eventElement.find('.fc-event-title');if(titleElement.length){seg.contentTop=titleElement[0].offsetTop;}}}
for(i=0;i<segCnt;i++){seg=segs[i];if((eventElement=seg.element)){eventElement[0].style.width=Math.max(0,seg.outerWidth-seg.hsides)+'px';height=Math.max(0,seg.outerHeight-seg.vsides);eventElement[0].style.height=height+'px';event=seg.event;if(seg.contentTop!==undefined&&height-seg.contentTop<10){eventElement.find('div.fc-event-time').text(formatDate(event.start,opt('timeFormat'))+' - '+event.title);eventElement.find('div.fc-event-title').remove();}
trigger('eventAfterRender',event,event,eventElement);}}}
function slotSegHtml(event,seg){var html="<";var url=event.url;var skinCss=getSkinCss(event,opt);var classes=['fc-event','fc-event-vert'];if(isEventDraggable(event)){classes.push('fc-event-draggable');}
if(seg.isStart){classes.push('fc-event-start');}
if(seg.isEnd){classes.push('fc-event-end');}
classes=classes.concat(event.className);if(event.source){classes=classes.concat(event.source.className||[]);}
if(url){html+="a href='"+htmlEscape(event.url)+"'";}else{html+="div";}
html+=" class='"+classes.join(' ')+"'"+
" style="+
"'"+
"position:absolute;"+
"top:"+seg.top+"px;"+
"left:"+seg.left+"px;"+
skinCss+
"'"+
">"+
"<div class='fc-event-inner'>"+
"<div class='fc-event-time'>"+
htmlEscape(t.getEventTimeText(event))+
"</div>"+
"<div class='fc-event-title'>"+
htmlEscape(event.title||'')+
"</div>"+
"</div>"+
"<div class='fc-event-bg'></div>";if(seg.isEnd&&isEventResizable(event)){html+="<div class='ui-resizable-handle ui-resizable-s'>=</div>";}
html+="</"+(url?"a":"div")+">";return html;}
function bindSlotSeg(event,eventElement,seg){var timeElement=eventElement.find('div.fc-event-time');if(isEventDraggable(event)){draggableSlotEvent(event,eventElement,timeElement);}
if(seg.isEnd&&isEventResizable(event)){resizableSlotEvent(event,eventElement,timeElement);}
eventElementHandlers(event,eventElement);}
function draggableDayEvent(event,eventElement,seg){var isStart=seg.isStart;var origWidth;var revert;var allDay=true;var dayDelta;var hoverListener=getHoverListener();var colWidth=getColWidth();var minTime=getMinTime();var slotDuration=getSlotDuration();var slotHeight=getSlotHeight();var snapDuration=getSnapDuration();var snapHeight=getSnapHeight();eventElement.draggable({opacity:opt('dragOpacity','month'),revertDuration:opt('dragRevertDuration'),start:function(ev,ui){trigger('eventDragStart',eventElement[0],event,ev,ui);hideEvents(event,eventElement);origWidth=eventElement.width();hoverListener.start(function(cell,origCell){clearOverlays();if(cell){revert=false;var origDate=cellToDate(0,origCell.col);var date=cellToDate(0,cell.col);dayDelta=date.diff(origDate,'days');if(!cell.row){renderDayOverlay(event.start.clone().add(dayDelta,'days'),getEventEnd(event).add(dayDelta,'days'));resetElement();}
else{if(isStart){if(allDay){eventElement.width(colWidth-10);setOuterHeight(eventElement,calendar.defaultTimedEventDuration/slotDuration*slotHeight);eventElement.draggable('option','grid',[colWidth,1]);allDay=false;}}
else{revert=true;}}
revert=revert||(allDay&&!dayDelta);}
else{resetElement();revert=true;}
eventElement.draggable('option','revert',revert);},ev,'drag');},stop:function(ev,ui){hoverListener.stop();clearOverlays();trigger('eventDragStop',eventElement[0],event,ev,ui);if(revert){resetElement();eventElement.css('filter','');showEvents(event,eventElement);}
else{var eventStart=event.start.clone().add(dayDelta,'days');var snapTime;var snapIndex;if(!allDay){snapIndex=Math.round((eventElement.offset().top-getSlotContainer().offset().top)/snapHeight);snapTime=moment.duration(minTime+snapIndex*snapDuration);eventStart=calendar.rezoneDate(eventStart.clone().time(snapTime));}
eventDrop(eventElement[0],event,eventStart,ev,ui);}}});function resetElement(){if(!allDay){eventElement.width(origWidth).height('').draggable('option','grid',null);allDay=true;}}}
function draggableSlotEvent(event,eventElement,timeElement){var coordinateGrid=t.getCoordinateGrid();var colCnt=getColCnt();var colWidth=getColWidth();var snapHeight=getSnapHeight();var snapDuration=getSnapDuration();var origPosition;var origCell;var isInBounds,prevIsInBounds;var isAllDay,prevIsAllDay;var colDelta,prevColDelta;var dayDelta;var snapDelta,prevSnapDelta;var eventStart,eventEnd;eventElement.draggable({scroll:false,grid:[colWidth,snapHeight],axis:colCnt==1?'y':false,opacity:opt('dragOpacity'),revertDuration:opt('dragRevertDuration'),start:function(ev,ui){trigger('eventDragStart',eventElement[0],event,ev,ui);hideEvents(event,eventElement);coordinateGrid.build();origPosition=eventElement.position();origCell=coordinateGrid.cell(ev.pageX,ev.pageY);isInBounds=prevIsInBounds=true;isAllDay=prevIsAllDay=getIsCellAllDay(origCell);colDelta=prevColDelta=0;dayDelta=0;snapDelta=prevSnapDelta=0;eventStart=null;eventEnd=null;},drag:function(ev,ui){var cell=coordinateGrid.cell(ev.pageX,ev.pageY);isInBounds=!!cell;if(isInBounds){isAllDay=getIsCellAllDay(cell);colDelta=Math.round((ui.position.left-origPosition.left)/colWidth);if(colDelta!=prevColDelta){var origDate=cellToDate(0,origCell.col);var col=origCell.col+colDelta;col=Math.max(0,col);col=Math.min(colCnt-1,col);var date=cellToDate(0,col);dayDelta=date.diff(origDate,'days');}
if(!isAllDay){snapDelta=Math.round((ui.position.top-origPosition.top)/snapHeight);}}
if(isInBounds!=prevIsInBounds||isAllDay!=prevIsAllDay||colDelta!=prevColDelta||snapDelta!=prevSnapDelta){if(isAllDay){eventStart=event.start.clone().stripTime().add(dayDelta,'days');eventEnd=eventStart.clone().add(calendar.defaultAllDayEventDuration);}
else{eventStart=event.start.clone().add(snapDelta*snapDuration).add(dayDelta,'days');eventEnd=getEventEnd(event).add(snapDelta*snapDuration).add(dayDelta,'days');}
updateUI();prevIsInBounds=isInBounds;prevIsAllDay=isAllDay;prevColDelta=colDelta;prevSnapDelta=snapDelta;}
eventElement.draggable('option','revert',!isInBounds);},stop:function(ev,ui){clearOverlays();trigger('eventDragStop',eventElement[0],event,ev,ui);if(isInBounds&&(isAllDay||dayDelta||snapDelta)){eventDrop(eventElement[0],event,eventStart,ev,ui);}
else{isInBounds=true;isAllDay=false;colDelta=0;dayDelta=0;snapDelta=0;updateUI();eventElement.css('filter','');eventElement.css(origPosition);showEvents(event,eventElement);}}});function updateUI(){clearOverlays();if(isInBounds){if(isAllDay){timeElement.hide();eventElement.draggable('option','grid',null);renderDayOverlay(eventStart,eventEnd);}
else{updateTimeText();timeElement.css('display','');eventElement.draggable('option','grid',[colWidth,snapHeight]);}}}
function updateTimeText(){if(eventStart){timeElement.text(t.getEventTimeText(eventStart,event.end?eventEnd:null));}}}
function resizableSlotEvent(event,eventElement,timeElement){var snapDelta,prevSnapDelta;var snapHeight=getSnapHeight();var snapDuration=getSnapDuration();var eventEnd;eventElement.resizable({handles:{s:'.ui-resizable-handle'},grid:snapHeight,start:function(ev,ui){snapDelta=prevSnapDelta=0;hideEvents(event,eventElement);trigger('eventResizeStart',eventElement[0],event,ev,ui);},resize:function(ev,ui){snapDelta=Math.round((Math.max(snapHeight,eventElement.height())-ui.originalSize.height)/snapHeight);if(snapDelta!=prevSnapDelta){eventEnd=getEventEnd(event).add(snapDuration*snapDelta);var text;if(snapDelta){text=t.getEventTimeText(event.start,eventEnd);}
else{text=t.getEventTimeText(event);}
timeElement.text(text);prevSnapDelta=snapDelta;}},stop:function(ev,ui){trigger('eventResizeStop',eventElement[0],event,ev,ui);if(snapDelta){eventResize(eventElement[0],event,eventEnd,ev,ui);}
else{showEvents(event,eventElement);}}});}}
function placeSlotSegs(segs){var levels=buildSlotSegLevels(segs);var level0=levels[0];var i;computeForwardSlotSegs(levels);if(level0){for(i=0;i<level0.length;i++){computeSlotSegPressures(level0[i]);}
for(i=0;i<level0.length;i++){computeSlotSegCoords(level0[i],0,0);}}
return flattenSlotSegLevels(levels);}
function buildSlotSegLevels(segs){var levels=[];var i,seg;var j;for(i=0;i<segs.length;i++){seg=segs[i];for(j=0;j<levels.length;j++){if(!computeSlotSegCollisions(seg,levels[j]).length){break;}}
(levels[j]||(levels[j]=[])).push(seg);}
return levels;}
function computeForwardSlotSegs(levels){var i,level;var j,seg;var k;for(i=0;i<levels.length;i++){level=levels[i];for(j=0;j<level.length;j++){seg=level[j];seg.forwardSegs=[];for(k=i+1;k<levels.length;k++){computeSlotSegCollisions(seg,levels[k],seg.forwardSegs);}}}}
function computeSlotSegPressures(seg){var forwardSegs=seg.forwardSegs;var forwardPressure=0;var i,forwardSeg;if(seg.forwardPressure===undefined){for(i=0;i<forwardSegs.length;i++){forwardSeg=forwardSegs[i];computeSlotSegPressures(forwardSeg);forwardPressure=Math.max(forwardPressure,1+forwardSeg.forwardPressure);}
seg.forwardPressure=forwardPressure;}}
function computeSlotSegCoords(seg,seriesBackwardPressure,seriesBackwardCoord){var forwardSegs=seg.forwardSegs;var i;if(seg.forwardCoord===undefined){if(!forwardSegs.length){seg.forwardCoord=1;}
else{forwardSegs.sort(compareForwardSlotSegs);computeSlotSegCoords(forwardSegs[0],seriesBackwardPressure+1,seriesBackwardCoord);seg.forwardCoord=forwardSegs[0].backwardCoord;}
seg.backwardCoord=seg.forwardCoord-
(seg.forwardCoord-seriesBackwardCoord)/(seriesBackwardPressure+1);for(i=0;i<forwardSegs.length;i++){computeSlotSegCoords(forwardSegs[i],0,seg.forwardCoord);}}}
function flattenSlotSegLevels(levels){var segs=[];var i,level;var j;for(i=0;i<levels.length;i++){level=levels[i];for(j=0;j<level.length;j++){segs.push(level[j]);}}
return segs;}
function computeSlotSegCollisions(seg,otherSegs,results){results=results||[];for(var i=0;i<otherSegs.length;i++){if(isSlotSegCollision(seg,otherSegs[i])){results.push(otherSegs[i]);}}
return results;}
function isSlotSegCollision(seg1,seg2){return seg1.end>seg2.start&&seg1.start<seg2.end;}
function compareForwardSlotSegs(seg1,seg2){return seg2.forwardPressure-seg1.forwardPressure||(seg1.backwardCoord||0)-(seg2.backwardCoord||0)||compareSlotSegs(seg1,seg2);}
function compareSlotSegs(seg1,seg2){return seg1.start-seg2.start||(seg2.end-seg2.start)-(seg1.end-seg1.start)||(seg1.event.title||'').localeCompare(seg2.event.title);};;function View(element,calendar,viewName){var t=this;t.element=element;t.calendar=calendar;t.name=viewName;t.opt=opt;t.trigger=trigger;t.isEventDraggable=isEventDraggable;t.isEventResizable=isEventResizable;t.clearEventData=clearEventData;t.reportEventElement=reportEventElement;t.triggerEventDestroy=triggerEventDestroy;t.eventElementHandlers=eventElementHandlers;t.showEvents=showEvents;t.hideEvents=hideEvents;t.eventDrop=eventDrop;t.eventResize=eventResize;var reportEventChange=calendar.reportEventChange;var eventElementsByID={};var eventElementCouples=[];var options=calendar.options;var nextDayThreshold=moment.duration(options.nextDayThreshold);function opt(name,viewNameOverride){var v=options[name];if($.isPlainObject(v)&&!isForcedAtomicOption(name)){return smartProperty(v,viewNameOverride||viewName);}
return v;}
function trigger(name,thisObj){return calendar.trigger.apply(calendar,[name,thisObj||t].concat(Array.prototype.slice.call(arguments,2),[t]));}
function isEventDraggable(event){var source=event.source||{};return firstDefined(event.startEditable,source.startEditable,opt('eventStartEditable'),event.editable,source.editable,opt('editable'));}
function isEventResizable(event){var source=event.source||{};return firstDefined(event.durationEditable,source.durationEditable,opt('eventDurationEditable'),event.editable,source.editable,opt('editable'));}
function clearEventData(){eventElementsByID={};eventElementCouples=[];}
function reportEventElement(event,element){eventElementCouples.push({event:event,element:element});if(eventElementsByID[event._id]){eventElementsByID[event._id].push(element);}else{eventElementsByID[event._id]=[element];}}
function triggerEventDestroy(){$.each(eventElementCouples,function(i,couple){t.trigger('eventDestroy',couple.event,couple.event,couple.element);});}
function eventElementHandlers(event,eventElement){eventElement.click(function(ev){if(!eventElement.hasClass('ui-draggable-dragging')&&!eventElement.hasClass('ui-resizable-resizing')){return trigger('eventClick',this,event,ev);}}).hover(function(ev){trigger('eventMouseover',this,event,ev);},function(ev){trigger('eventMouseout',this,event,ev);});}
function showEvents(event,exceptElement){eachEventElement(event,exceptElement,'show');}
function hideEvents(event,exceptElement){eachEventElement(event,exceptElement,'hide');}
function eachEventElement(event,exceptElement,funcName){var elements=eventElementsByID[event._id],i,len=elements.length;for(i=0;i<len;i++){if(!exceptElement||elements[i][0]!=exceptElement[0]){elements[i][funcName]();}}}
t.getEventTimeText=function(event){var start;var end;if(arguments.length===2){start=arguments[0];end=arguments[1];}
else{start=event.start;end=event.end;}
if(end&&opt('displayEventEnd')){return calendar.formatRange(start,end,opt('timeFormat'));}
else{return calendar.formatDate(start,opt('timeFormat'));}};function eventDrop(el,event,newStart,ev,ui){var mutateResult=calendar.mutateEvent(event,newStart,null);trigger('eventDrop',el,event,mutateResult.dateDelta,function(){mutateResult.undo();reportEventChange(event._id);},ev,ui);reportEventChange(event._id);}
function eventResize(el,event,newEnd,ev,ui){var mutateResult=calendar.mutateEvent(event,null,newEnd);trigger('eventResize',el,event,mutateResult.durationDelta,function(){mutateResult.undo();reportEventChange(event._id);},ev,ui);reportEventChange(event._id);}
t.isHiddenDay=isHiddenDay;t.skipHiddenDays=skipHiddenDays;t.getCellsPerWeek=getCellsPerWeek;t.dateToCell=dateToCell;t.dateToDayOffset=dateToDayOffset;t.dayOffsetToCellOffset=dayOffsetToCellOffset;t.cellOffsetToCell=cellOffsetToCell;t.cellToDate=cellToDate;t.cellToCellOffset=cellToCellOffset;t.cellOffsetToDayOffset=cellOffsetToDayOffset;t.dayOffsetToDate=dayOffsetToDate;t.rangeToSegments=rangeToSegments;var hiddenDays=opt('hiddenDays')||[];var isHiddenDayHash=[];var cellsPerWeek;var dayToCellMap=[];var cellToDayMap=[];var isRTL=opt('isRTL');(function(){if(opt('weekends')===false){hiddenDays.push(0,6);}
for(var dayIndex=0,cellIndex=0;dayIndex<7;dayIndex++){dayToCellMap[dayIndex]=cellIndex;isHiddenDayHash[dayIndex]=$.inArray(dayIndex,hiddenDays)!=-1;if(!isHiddenDayHash[dayIndex]){cellToDayMap[cellIndex]=dayIndex;cellIndex++;}}
cellsPerWeek=cellIndex;if(!cellsPerWeek){throw 'invalid hiddenDays';}})();function isHiddenDay(day){if(moment.isMoment(day)){day=day.day();}
return isHiddenDayHash[day];}
function getCellsPerWeek(){return cellsPerWeek;}
function skipHiddenDays(date,inc,isExclusive){var out=date.clone();inc=inc||1;while(isHiddenDayHash[(out.day()+(isExclusive?inc:0)+7)%7]){out.add(inc,'days');}
return out;}
function cellToDate(){var cellOffset=cellToCellOffset.apply(null,arguments);var dayOffset=cellOffsetToDayOffset(cellOffset);var date=dayOffsetToDate(dayOffset);return date;}
function cellToCellOffset(row,col){var colCnt=t.getColCnt();var dis=isRTL?-1:1;var dit=isRTL?colCnt-1:0;if(typeof row=='object'){col=row.col;row=row.row;}
var cellOffset=row*colCnt+(col*dis+dit);return cellOffset;}
function cellOffsetToDayOffset(cellOffset){var day0=t.start.day();cellOffset+=dayToCellMap[day0];return Math.floor(cellOffset/cellsPerWeek)*7+
cellToDayMap[(cellOffset%cellsPerWeek+cellsPerWeek)%cellsPerWeek]-
day0;}
function dayOffsetToDate(dayOffset){return t.start.clone().add(dayOffset,'days');}
function dateToCell(date){var dayOffset=dateToDayOffset(date);var cellOffset=dayOffsetToCellOffset(dayOffset);var cell=cellOffsetToCell(cellOffset);return cell;}
function dateToDayOffset(date){return date.clone().stripTime().diff(t.start,'days');}
function dayOffsetToCellOffset(dayOffset){var day0=t.start.day();dayOffset+=day0;return Math.floor(dayOffset/7)*cellsPerWeek+
dayToCellMap[(dayOffset%7+7)%7]-
dayToCellMap[day0];}
function cellOffsetToCell(cellOffset){var colCnt=t.getColCnt();var dis=isRTL?-1:1;var dit=isRTL?colCnt-1:0;var row=Math.floor(cellOffset/colCnt);var col=((cellOffset%colCnt+colCnt)%colCnt)*dis+dit;return{row:row,col:col};}
function rangeToSegments(start,end){var rowCnt=t.getRowCnt();var colCnt=t.getColCnt();var segments=[];var rangeDayOffsetStart=dateToDayOffset(start);var rangeDayOffsetEnd=dateToDayOffset(end);var endTimeMS=+end.time();if(endTimeMS&&endTimeMS>=nextDayThreshold){rangeDayOffsetEnd++;}
rangeDayOffsetEnd=Math.max(rangeDayOffsetEnd,rangeDayOffsetStart+1);var rangeCellOffsetFirst=dayOffsetToCellOffset(rangeDayOffsetStart);var rangeCellOffsetLast=dayOffsetToCellOffset(rangeDayOffsetEnd)-1;for(var row=0;row<rowCnt;row++){var rowCellOffsetFirst=row*colCnt;var rowCellOffsetLast=rowCellOffsetFirst+colCnt-1;var segmentCellOffsetFirst=Math.max(rangeCellOffsetFirst,rowCellOffsetFirst);var segmentCellOffsetLast=Math.min(rangeCellOffsetLast,rowCellOffsetLast);if(segmentCellOffsetFirst<=segmentCellOffsetLast){var segmentCellFirst=cellOffsetToCell(segmentCellOffsetFirst);var segmentCellLast=cellOffsetToCell(segmentCellOffsetLast);var cols=[segmentCellFirst.col,segmentCellLast.col].sort();var isStart=cellOffsetToDayOffset(segmentCellOffsetFirst)==rangeDayOffsetStart;var isEnd=cellOffsetToDayOffset(segmentCellOffsetLast)+1==rangeDayOffsetEnd;segments.push({row:row,leftCol:cols[0],rightCol:cols[1],isStart:isStart,isEnd:isEnd});}}
return segments;}};;function DayEventRenderer(){var t=this;t.renderDayEvents=renderDayEvents;t.draggableDayEvent=draggableDayEvent;t.resizableDayEvent=resizableDayEvent;var opt=t.opt;var trigger=t.trigger;var isEventDraggable=t.isEventDraggable;var isEventResizable=t.isEventResizable;var reportEventElement=t.reportEventElement;var eventElementHandlers=t.eventElementHandlers;var showEvents=t.showEvents;var hideEvents=t.hideEvents;var eventDrop=t.eventDrop;var eventResize=t.eventResize;var getRowCnt=t.getRowCnt;var getColCnt=t.getColCnt;var allDayRow=t.allDayRow;var colLeft=t.colLeft;var colRight=t.colRight;var colContentLeft=t.colContentLeft;var colContentRight=t.colContentRight;var getDaySegmentContainer=t.getDaySegmentContainer;var renderDayOverlay=t.renderDayOverlay;var clearOverlays=t.clearOverlays;var clearSelection=t.clearSelection;var getHoverListener=t.getHoverListener;var rangeToSegments=t.rangeToSegments;var cellToDate=t.cellToDate;var cellToCellOffset=t.cellToCellOffset;var cellOffsetToDayOffset=t.cellOffsetToDayOffset;var dateToDayOffset=t.dateToDayOffset;var dayOffsetToCellOffset=t.dayOffsetToCellOffset;var calendar=t.calendar;var getEventEnd=calendar.getEventEnd;function renderDayEvents(events,modifiedEventId){var segments=_renderDayEvents(events,false,true);segmentElementEach(segments,function(segment,element){reportEventElement(segment.event,element);});attachHandlers(segments,modifiedEventId);segmentElementEach(segments,function(segment,element){trigger('eventAfterRender',segment.event,segment.event,element);});}
function renderTempDayEvent(event,adjustRow,adjustTop){var segments=_renderDayEvents([event],true,false);var elements=[];segmentElementEach(segments,function(segment,element){if(segment.row===adjustRow){element.css('top',adjustTop);}
elements.push(element[0]);});return elements;}
function _renderDayEvents(events,doAppend,doRowHeights){var finalContainer=getDaySegmentContainer();var renderContainer=doAppend?$("<div/>"):finalContainer;var segments=buildSegments(events);var html;var elements;calculateHorizontals(segments);html=buildHTML(segments);renderContainer[0].innerHTML=html;elements=renderContainer.children();if(doAppend){finalContainer.append(elements);}
resolveElements(segments,elements);segmentElementEach(segments,function(segment,element){segment.hsides=hsides(element,true);});segmentElementEach(segments,function(segment,element){element.width(Math.max(0,segment.outerWidth-segment.hsides));});segmentElementEach(segments,function(segment,element){segment.outerHeight=element.outerHeight(true);});setVerticals(segments,doRowHeights);return segments;}
function buildSegments(events){var segments=[];for(var i=0;i<events.length;i++){var eventSegments=buildSegmentsForEvent(events[i]);segments.push.apply(segments,eventSegments);}
return segments;}
function buildSegmentsForEvent(event){var segments=rangeToSegments(event.start,getEventEnd(event));for(var i=0;i<segments.length;i++){segments[i].event=event;}
return segments;}
function calculateHorizontals(segments){var isRTL=opt('isRTL');for(var i=0;i<segments.length;i++){var segment=segments[i];var leftFunc=(isRTL?segment.isEnd:segment.isStart)?colContentLeft:colLeft;var rightFunc=(isRTL?segment.isStart:segment.isEnd)?colContentRight:colRight;var left=leftFunc(segment.leftCol);var right=rightFunc(segment.rightCol);segment.left=left;segment.outerWidth=right-left;}}
function buildHTML(segments){var html='';for(var i=0;i<segments.length;i++){html+=buildHTMLForSegment(segments[i]);}
return html;}
function buildHTMLForSegment(segment){var html='';var isRTL=opt('isRTL');var event=segment.event;var url=event.url;var classNames=['fc-event','fc-event-hori'];if(isEventDraggable(event)){classNames.push('fc-event-draggable');}
if(segment.isStart){classNames.push('fc-event-start');}
if(segment.isEnd){classNames.push('fc-event-end');}
classNames=classNames.concat(event.className);if(event.source){classNames=classNames.concat(event.source.className||[]);}
var skinCss=getSkinCss(event,opt);if(url){html+="<a href='"+htmlEscape(url)+"'";}else{html+="<div";}
html+=" class='"+classNames.join(' ')+"'"+
" style="+
"'"+
"position:absolute;"+
"left:"+segment.left+"px;"+
skinCss+
"'"+
">"+
"<div class='fc-event-inner'>";if(!event.allDay&&segment.isStart){html+="<span class='fc-event-time'>"+
htmlEscape(t.getEventTimeText(event))+
"</span>";}
html+="<span class='fc-event-title'>"+
htmlEscape(event.title||'')+
"</span>"+
"</div>";if(event.allDay&&segment.isEnd&&isEventResizable(event)){html+="<div class='ui-resizable-handle ui-resizable-"+(isRTL?'w':'e')+"'>"+
"&nbsp;&nbsp;&nbsp;"+
"</div>";}
html+="</"+(url?"a":"div")+">";return html;}
function resolveElements(segments,elements){for(var i=0;i<segments.length;i++){var segment=segments[i];var event=segment.event;var element=elements.eq(i);var triggerRes=trigger('eventRender',event,event,element);if(triggerRes===false){element.remove();}
else{if(triggerRes&&triggerRes!==true){triggerRes=$(triggerRes).css({position:'absolute',left:segment.left});element.replaceWith(triggerRes);element=triggerRes;}
segment.element=element;}}}
function setVerticals(segments,doRowHeights){var rowContentHeights=calculateVerticals(segments);var rowContentElements=getRowContentElements();var rowContentTops=[];var i;if(doRowHeights){for(i=0;i<rowContentElements.length;i++){rowContentElements[i].height(rowContentHeights[i]);}}
for(i=0;i<rowContentElements.length;i++){rowContentTops.push(rowContentElements[i].position().top);}
segmentElementEach(segments,function(segment,element){element.css('top',rowContentTops[segment.row]+segment.top);});}
function calculateVerticals(segments){var rowCnt=getRowCnt();var colCnt=getColCnt();var rowContentHeights=[];var segmentRows=buildSegmentRows(segments);var colI;for(var rowI=0;rowI<rowCnt;rowI++){var segmentRow=segmentRows[rowI];var colHeights=[];for(colI=0;colI<colCnt;colI++){colHeights.push(0);}
for(var segmentI=0;segmentI<segmentRow.length;segmentI++){var segment=segmentRow[segmentI];segment.top=arrayMax(colHeights.slice(segment.leftCol,segment.rightCol+1));for(colI=segment.leftCol;colI<=segment.rightCol;colI++){colHeights[colI]=segment.top+segment.outerHeight;}}
rowContentHeights.push(arrayMax(colHeights));}
return rowContentHeights;}
function buildSegmentRows(segments){var rowCnt=getRowCnt();var segmentRows=[];var segmentI;var segment;var rowI;for(segmentI=0;segmentI<segments.length;segmentI++){segment=segments[segmentI];rowI=segment.row;if(segment.element){if(segmentRows[rowI]){segmentRows[rowI].push(segment);}
else{segmentRows[rowI]=[segment];}}}
for(rowI=0;rowI<rowCnt;rowI++){segmentRows[rowI]=sortSegmentRow(segmentRows[rowI]||[]);}
return segmentRows;}
function sortSegmentRow(segments){var sortedSegments=[];var subrows=buildSegmentSubrows(segments);for(var i=0;i<subrows.length;i++){sortedSegments.push.apply(sortedSegments,subrows[i]);}
return sortedSegments;}
function buildSegmentSubrows(segments){segments.sort(compareDaySegments);var subrows=[];for(var i=0;i<segments.length;i++){var segment=segments[i];for(var j=0;j<subrows.length;j++){if(!isDaySegmentCollision(segment,subrows[j])){break;}}
if(subrows[j]){subrows[j].push(segment);}
else{subrows[j]=[segment];}}
return subrows;}
function getRowContentElements(){var i;var rowCnt=getRowCnt();var rowDivs=[];for(i=0;i<rowCnt;i++){rowDivs[i]=allDayRow(i).find('div.fc-day-content > div');}
return rowDivs;}
function attachHandlers(segments,modifiedEventId){var segmentContainer=getDaySegmentContainer();segmentElementEach(segments,function(segment,element,i){var event=segment.event;if(event._id===modifiedEventId){bindDaySeg(event,element,segment);}else{element[0]._fci=i;}});lazySegBind(segmentContainer,segments,bindDaySeg);}
function bindDaySeg(event,eventElement,segment){if(isEventDraggable(event)){t.draggableDayEvent(event,eventElement,segment);}
if(event.allDay&&segment.isEnd&&isEventResizable(event)){t.resizableDayEvent(event,eventElement,segment);}
eventElementHandlers(event,eventElement);}
function draggableDayEvent(event,eventElement){var hoverListener=getHoverListener();var dayDelta;var eventStart;eventElement.draggable({delay:50,opacity:opt('dragOpacity'),revertDuration:opt('dragRevertDuration'),start:function(ev,ui){trigger('eventDragStart',eventElement[0],event,ev,ui);hideEvents(event,eventElement);hoverListener.start(function(cell,origCell,rowDelta,colDelta){eventElement.draggable('option','revert',!cell||!rowDelta&&!colDelta);clearOverlays();if(cell){var origCellDate=cellToDate(origCell);var cellDate=cellToDate(cell);dayDelta=cellDate.diff(origCellDate,'days');eventStart=event.start.clone().add(dayDelta,'days');renderDayOverlay(eventStart,getEventEnd(event).add(dayDelta,'days'));}
else{dayDelta=0;}},ev,'drag');},stop:function(ev,ui){hoverListener.stop();clearOverlays();trigger('eventDragStop',eventElement[0],event,ev,ui);if(dayDelta){eventDrop(eventElement[0],event,eventStart,ev,ui);}
else{eventElement.css('filter','');showEvents(event,eventElement);}}});}
function resizableDayEvent(event,element,segment){var isRTL=opt('isRTL');var direction=isRTL?'w':'e';var handle=element.find('.ui-resizable-'+direction);var isResizing=false;disableTextSelection(element);element.mousedown(function(ev){ev.preventDefault();}).click(function(ev){if(isResizing){ev.preventDefault();ev.stopImmediatePropagation();}});handle.mousedown(function(ev){if(ev.which!=1){return;}
isResizing=true;var hoverListener=getHoverListener();var elementTop=element.css('top');var dayDelta;var eventEnd;var helpers;var eventCopy=$.extend({},event);var minCellOffset=dayOffsetToCellOffset(dateToDayOffset(event.start));clearSelection();$('body').css('cursor',direction+'-resize').one('mouseup',mouseup);trigger('eventResizeStart',element[0],event,ev,{});hoverListener.start(function(cell,origCell){if(cell){var origCellOffset=cellToCellOffset(origCell);var cellOffset=cellToCellOffset(cell);cellOffset=Math.max(cellOffset,minCellOffset);dayDelta=cellOffsetToDayOffset(cellOffset)-
cellOffsetToDayOffset(origCellOffset);eventEnd=getEventEnd(event).add(dayDelta,'days');if(dayDelta){eventCopy.end=eventEnd;var oldHelpers=helpers;helpers=renderTempDayEvent(eventCopy,segment.row,elementTop);helpers=$(helpers);helpers.find('*').css('cursor',direction+'-resize');if(oldHelpers){oldHelpers.remove();}
hideEvents(event);}
else{if(helpers){showEvents(event);helpers.remove();helpers=null;}}
clearOverlays();renderDayOverlay(event.start,eventEnd);}},ev);function mouseup(ev){trigger('eventResizeStop',element[0],event,ev,{});$('body').css('cursor','');hoverListener.stop();clearOverlays();if(dayDelta){eventResize(element[0],event,eventEnd,ev,{});}
setTimeout(function(){isResizing=false;},0);}});}}
function isDaySegmentCollision(segment,otherSegments){for(var i=0;i<otherSegments.length;i++){var otherSegment=otherSegments[i];if(otherSegment.leftCol<=segment.rightCol&&otherSegment.rightCol>=segment.leftCol){return true;}}
return false;}
function segmentElementEach(segments,callback){for(var i=0;i<segments.length;i++){var segment=segments[i];var element=segment.element;if(element){callback(segment,element,i);}}}
function compareDaySegments(a,b){return(b.rightCol-b.leftCol)-(a.rightCol-a.leftCol)||b.event.allDay-a.event.allDay||a.event.start-b.event.start||(a.event.title||'').localeCompare(b.event.title);};;function SelectionManager(){var t=this;t.select=select;t.unselect=unselect;t.reportSelection=reportSelection;t.daySelectionMousedown=daySelectionMousedown;t.selectionManagerDestroy=destroy;var calendar=t.calendar;var opt=t.opt;var trigger=t.trigger;var defaultSelectionEnd=t.defaultSelectionEnd;var renderSelection=t.renderSelection;var clearSelection=t.clearSelection;var selected=false;if(opt('selectable')&&opt('unselectAuto')){$(document).on('mousedown',documentMousedown);}
function documentMousedown(ev){var ignore=opt('unselectCancel');if(ignore){if($(ev.target).parents(ignore).length){return;}}
unselect(ev);}
function select(start,end){unselect();start=calendar.moment(start);if(end){end=calendar.moment(end);}
else{end=defaultSelectionEnd(start);}
renderSelection(start,end);reportSelection(start,end);}
function unselect(ev){if(selected){selected=false;clearSelection();trigger('unselect',null,ev);}}
function reportSelection(start,end,ev){selected=true;trigger('select',null,start,end,ev);}
function daySelectionMousedown(ev){var cellToDate=t.cellToDate;var getIsCellAllDay=t.getIsCellAllDay;var hoverListener=t.getHoverListener();var reportDayClick=t.reportDayClick;if(ev.which==1&&opt('selectable')){unselect(ev);var dates;hoverListener.start(function(cell,origCell){clearSelection();if(cell&&getIsCellAllDay(cell)){dates=[cellToDate(origCell),cellToDate(cell)].sort(dateCompare);renderSelection(dates[0],dates[1].clone().add(1,'days'));}else{dates=null;}},ev);$(document).one('mouseup',function(ev){hoverListener.stop();if(dates){if(+dates[0]==+dates[1]){reportDayClick(dates[0],ev);}
reportSelection(dates[0],dates[1].clone().add(1,'days'),ev);}});}}
function destroy(){$(document).off('mousedown',documentMousedown);}};;function OverlayManager(){var t=this;t.renderOverlay=renderOverlay;t.clearOverlays=clearOverlays;var usedOverlays=[];var unusedOverlays=[];function renderOverlay(rect,parent){var e=unusedOverlays.shift();if(!e){e=$("<div class='fc-cell-overlay' style='position:absolute;z-index:3'/>");}
if(e[0].parentNode!=parent[0]){e.appendTo(parent);}
usedOverlays.push(e.css(rect).show());return e;}
function clearOverlays(){var e;while((e=usedOverlays.shift())){unusedOverlays.push(e.hide().unbind());}}};;function CoordinateGrid(buildFunc){var t=this;var rows;var cols;t.build=function(){rows=[];cols=[];buildFunc(rows,cols);};t.cell=function(x,y){var rowCnt=rows.length;var colCnt=cols.length;var i,r=-1,c=-1;for(i=0;i<rowCnt;i++){if(y>=rows[i][0]&&y<rows[i][1]){r=i;break;}}
for(i=0;i<colCnt;i++){if(x>=cols[i][0]&&x<cols[i][1]){c=i;break;}}
return(r>=0&&c>=0)?{row:r,col:c}:null;};t.rect=function(row0,col0,row1,col1,originElement){var origin=originElement.offset();return{top:rows[row0][0]-origin.top,left:cols[col0][0]-origin.left,width:cols[col1][1]-cols[col0][0],height:rows[row1][1]-rows[row0][0]};};};;function HoverListener(coordinateGrid){var t=this;var bindType;var change;var firstCell;var cell;t.start=function(_change,ev,_bindType){change=_change;firstCell=cell=null;coordinateGrid.build();mouse(ev);bindType=_bindType||'mousemove';$(document).bind(bindType,mouse);};function mouse(ev){_fixUIEvent(ev);var newCell=coordinateGrid.cell(ev.pageX,ev.pageY);if(Boolean(newCell)!==Boolean(cell)||newCell&&(newCell.row!=cell.row||newCell.col!=cell.col)){if(newCell){if(!firstCell){firstCell=newCell;}
change(newCell,firstCell,newCell.row-firstCell.row,newCell.col-firstCell.col);}else{change(newCell,firstCell);}
cell=newCell;}}
t.stop=function(){$(document).unbind(bindType,mouse);return cell;};}
function _fixUIEvent(event){if(event.pageX===undefined){event.pageX=event.originalEvent.pageX;event.pageY=event.originalEvent.pageY;}};;function HorizontalPositionCache(getElement){var t=this,elements={},lefts={},rights={};function e(i){return(elements[i]=(elements[i]||getElement(i)));}
t.left=function(i){return(lefts[i]=(lefts[i]===undefined?e(i).position().left:lefts[i]));};t.right=function(i){return(rights[i]=(rights[i]===undefined?t.left(i)+e(i).width():rights[i]));};t.clear=function(){elements={};lefts={};rights={};};};;});