(function($){"use strict";var defaultOptions={tagClass:function(item){return 'label label-info';},itemValue:function(item){return item?item.toString():item;},itemText:function(item){return this.itemValue(item);},freeInput:true,addOnBlur:true,maxTags:undefined,maxChars:undefined,confirmKeys:[13,44],onTagExists:function(item,$tag){$tag.hide().fadeIn();},trimValue:false,allowDuplicates:false};function TagsInput(element,options){var tagsId,tagClass;this.itemsArray=[];this.$element=$(element);this.$element.hide();this.isSelect=(element.tagName==='SELECT');this.multiple=(this.isSelect&&element.hasAttribute('multiple'));this.objectItems=options&&options.itemValue;this.placeholderText=element.hasAttribute('placeholder')?this.$element.attr('placeholder'):'';this.inputSize=Math.max(1,this.placeholderText.length);this.$container=$('<div class="bootstrap-tagsinput"></div>');this.$input=$('<input type="text" placeholder="'+this.placeholderText+'"/>').appendTo(this.$container);if(!!element.id){tagsId=element.id+'tags-input';this.$input.attr('id',tagsId);$('label[for="'+element.id+'"]').attr('for',tagsId);}
this.$element.after(this.$container);options=options||{};if(options.tagClass===undefined){tagClass=this.$element.data('tag-class');if(tagClass){options.tagClass=function(){return tagClass;};}}
var inputWidth=(this.inputSize<3?3:this.inputSize)+"em";this.$input.get(0).style.cssText="width: "+inputWidth+" !important;";this.build(options);}
TagsInput.prototype={constructor:TagsInput,add:function(item,dontPushVal){var self=this;if(self.options.maxTags&&self.itemsArray.length>=self.options.maxTags)
return;if(item!==false&&!item)
return;if(typeof item==="string"&&self.options.trimValue){item=$.trim(item);}
if(typeof item==="object"&&!self.objectItems)
throw("Can't add objects when itemValue option is not set");if(item.toString().match(/^\s*$/))
return;if(self.isSelect&&!self.multiple&&self.itemsArray.length>0)
self.remove(self.itemsArray[0]);if(typeof item==="string"&&this.$element[0].tagName==='INPUT'){var items=item.split(',');if(items.length>1){for(var i=0;i<items.length;i++){this.add(items[i],true);}
if(!dontPushVal)
self.pushVal();return;}}
var itemValue=self.options.itemValue(item),itemText=self.options.itemText(item),tagClass=self.options.tagClass(item);var existing=$.grep(self.itemsArray,function(item){return self.options.itemValue(item)===itemValue;})[0];if(existing&&!self.options.allowDuplicates){if(self.options.onTagExists){var $existingTag=$(".tag",self.$container).filter(function(){return $(this).data("item")===existing;});self.options.onTagExists(item,$existingTag);}
return;}
if(self.items().toString().length+item.length+1>self.options.maxInputLength)
return;var beforeItemAddEvent=$.Event('beforeItemAdd',{item:item,cancel:false});self.$element.trigger(beforeItemAddEvent);if(beforeItemAddEvent.cancel)
return;self.itemsArray.push(item);var $tag=$('<span class="tag '+htmlEncode(tagClass)+'">'+htmlEncode(itemText)+'<span data-role="remove"></span></span>');$tag.data('item',item);self.findInputWrapper().before($tag);$tag.after(' ');if(self.isSelect&&!$('option[value="'+encodeURIComponent(itemValue)+'"]',self.$element)[0]){var $option=$('<option selected>'+htmlEncode(itemText)+'</option>');$option.data('item',item);$option.attr('value',itemValue);self.$element.append($option);}
if(!dontPushVal)
self.pushVal();if(self.options.maxTags===self.itemsArray.length||self.items().toString().length===self.options.maxInputLength)
self.$container.addClass('bootstrap-tagsinput-max');self.$element.trigger($.Event('itemAdded',{item:item}));},remove:function(item,dontPushVal){var self=this;if(self.objectItems){if(typeof item==="object")
item=$.grep(self.itemsArray,function(other){return self.options.itemValue(other)==self.options.itemValue(item);});else
item=$.grep(self.itemsArray,function(other){return self.options.itemValue(other)==item;});item=item[item.length-1];}
if(item){var beforeItemRemoveEvent=$.Event('beforeItemRemove',{item:item,cancel:false});self.$element.trigger(beforeItemRemoveEvent);if(beforeItemRemoveEvent.cancel)
return;$('.tag',self.$container).filter(function(){return $(this).data('item')===item;}).remove();$('option',self.$element).filter(function(){return $(this).data('item')===item;}).remove();if($.inArray(item,self.itemsArray)!==-1)
self.itemsArray.splice($.inArray(item,self.itemsArray),1);}
if(!dontPushVal)
self.pushVal();if(self.options.maxTags>self.itemsArray.length)
self.$container.removeClass('bootstrap-tagsinput-max');self.$element.trigger($.Event('itemRemoved',{item:item}));},removeAll:function(){var self=this;$('.tag',self.$container).remove();$('option',self.$element).remove();while(self.itemsArray.length>0)
self.itemsArray.pop();self.pushVal();},refresh:function(){var self=this;$('.tag',self.$container).each(function(){var $tag=$(this),item=$tag.data('item'),itemValue=self.options.itemValue(item),itemText=self.options.itemText(item),tagClass=self.options.tagClass(item);$tag.attr('class',null);$tag.addClass('tag '+htmlEncode(tagClass));$tag.contents().filter(function(){return this.nodeType==3;})[0].nodeValue=htmlEncode(itemText);if(self.isSelect){var option=$('option',self.$element).filter(function(){return $(this).data('item')===item;});option.attr('value',itemValue);}});},items:function(){return this.itemsArray;},pushVal:function(){var self=this,val=$.map(self.items(),function(item){return self.options.itemValue(item).toString();});self.$element.val(val,true).trigger('change');},build:function(options){var self=this;self.options=$.extend({},defaultOptions,options);if(self.objectItems)
self.options.freeInput=false;makeOptionItemFunction(self.options,'itemValue');makeOptionItemFunction(self.options,'itemText');makeOptionFunction(self.options,'tagClass');if(self.options.typeahead){var typeahead=self.options.typeahead||{};makeOptionFunction(typeahead,'source');self.$input.typeahead($.extend({},typeahead,{source:function(query,process){function processItems(items){var texts=[];for(var i=0;i<items.length;i++){var text=self.options.itemText(items[i]);map[text]=items[i];texts.push(text);}
process(texts);}
this.map={};var map=this.map,data=typeahead.source(query);if($.isFunction(data.success)){data.success(processItems);}else if($.isFunction(data.then)){data.then(processItems);}else{$.when(data).then(processItems);}},updater:function(text){self.add(this.map[text]);},matcher:function(text){return(text.toLowerCase().indexOf(this.query.trim().toLowerCase())!==-1);},sorter:function(texts){return texts.sort();},highlighter:function(text){var regex=new RegExp('('+this.query+')','gi');return text.replace(regex,"<strong>$1</strong>");}}));}
if(self.options.typeaheadjs){var typeaheadjs=self.options.typeaheadjs||{};self.$input.typeahead(null,typeaheadjs).on('typeahead:selected',$.proxy(function(obj,datum){if(typeaheadjs.valueKey)
self.add(datum[typeaheadjs.valueKey]);else
self.add(datum);self.$input.typeahead('val','');},self));}
self.$container.on('click',$.proxy(function(event){if(!self.$element.attr('disabled')){self.$input.removeAttr('disabled');}
self.$input.focus();},self));if(self.options.addOnBlur&&self.options.freeInput){self.$input.on('focusout',$.proxy(function(event){if($('.typeahead, .twitter-typeahead',self.$container).length===0){self.add(self.$input.val());self.$input.val('');}},self));}
self.$container.on('keydown','input',$.proxy(function(event){var $input=$(event.target),$inputWrapper=self.findInputWrapper();if(self.$element.attr('disabled')){self.$input.attr('disabled','disabled');return;}
switch(event.which){case 8:if(doGetCaretPosition($input[0])===0){var prev=$inputWrapper.prev();if(prev){self.remove(prev.data('item'));}}
break;case 46:if(doGetCaretPosition($input[0])===0){var next=$inputWrapper.next();if(next){self.remove(next.data('item'));}}
break;case 37:var $prevTag=$inputWrapper.prev();if($input.val().length===0&&$prevTag[0]){$prevTag.before($inputWrapper);$input.focus();}
break;case 39:var $nextTag=$inputWrapper.next();if($input.val().length===0&&$nextTag[0]){$nextTag.after($inputWrapper);$input.focus();}
break;default:}
var textLength=$input.val().length,wordSpace=Math.ceil(textLength/5),size=textLength+wordSpace+1;$input.attr('size',Math.max(this.inputSize,$input.val().length));},self));self.$container.on('keypress','input',$.proxy(function(event){var $input=$(event.target);if(self.$element.attr('disabled')){self.$input.attr('disabled','disabled');return;}
var text=$input.val(),maxLengthReached=self.options.maxChars&&text.length>=self.options.maxChars;if(self.options.freeInput&&(keyCombinationInList(event,self.options.confirmKeys)||maxLengthReached)){self.add(maxLengthReached?text.substr(0,self.options.maxChars):text);$input.val('');event.preventDefault();}
var textLength=$input.val().length,wordSpace=Math.ceil(textLength/5),size=textLength+wordSpace+1;$input.attr('size',Math.max(this.inputSize,$input.val().length));},self));self.$container.on('click','[data-role=remove]',$.proxy(function(event){if(self.$element.attr('disabled')){return;}
self.remove($(event.target).closest('.tag').data('item'));},self));if(self.options.itemValue===defaultOptions.itemValue){if(self.$element[0].tagName==='INPUT'){self.add(self.$element.val());}else{$('option',self.$element).each(function(){self.add($(this).attr('value'),true);});}}},destroy:function(){var self=this;self.$container.off('keypress','input');self.$container.off('click','[role=remove]');self.$container.remove();self.$element.removeData('tagsinput');self.$element.show();},focus:function(){this.$input.focus();},input:function(){return this.$input;},findInputWrapper:function(){var elt=this.$input[0],container=this.$container[0];while(elt&&elt.parentNode!==container)
elt=elt.parentNode;return $(elt);}};$.fn.tagsinput=function(arg1,arg2){var results=[];this.each(function(){var tagsinput=$(this).data('tagsinput');if(!tagsinput){tagsinput=new TagsInput(this,arg1);$(this).data('tagsinput',tagsinput);results.push(tagsinput);if(this.tagName==='SELECT'){$('option',$(this)).attr('selected','selected');}
$(this).val($(this).val());}else if(!arg1&&!arg2){results.push(tagsinput);}else if(tagsinput[arg1]!==undefined){var retVal=tagsinput[arg1](arg2);if(retVal!==undefined)
results.push(retVal);}});if(typeof arg1=='string'){return results.length>1?results:results[0];}else{return results;}};$.fn.tagsinput.Constructor=TagsInput;function makeOptionItemFunction(options,key){if(typeof options[key]!=='function'){var propertyName=options[key];options[key]=function(item){return item[propertyName];};}}
function makeOptionFunction(options,key){if(typeof options[key]!=='function'){var value=options[key];options[key]=function(){return value;};}}
var htmlEncodeContainer=$('<div />');function htmlEncode(value){if(value){return htmlEncodeContainer.text(value).html();}else{return '';}}
function doGetCaretPosition(oField){var iCaretPos=0;if(document.selection){oField.focus();var oSel=document.selection.createRange();oSel.moveStart('character',-oField.value.length);iCaretPos=oSel.text.length;}else if(oField.selectionStart||oField.selectionStart=='0'){iCaretPos=oField.selectionStart;}
return(iCaretPos);}
function keyCombinationInList(keyPressEvent,lookupList){var found=false;$.each(lookupList,function(index,keyCombination){if(typeof(keyCombination)==='number'&&keyPressEvent.which===keyCombination){found=true;return false;}
if(keyPressEvent.which===keyCombination.which){var alt=!keyCombination.hasOwnProperty('altKey')||keyPressEvent.altKey===keyCombination.altKey,shift=!keyCombination.hasOwnProperty('shiftKey')||keyPressEvent.shiftKey===keyCombination.shiftKey,ctrl=!keyCombination.hasOwnProperty('ctrlKey')||keyPressEvent.ctrlKey===keyCombination.ctrlKey;if(alt&&shift&&ctrl){found=true;return false;}}});return found;}
$(function(){$("input[data-role=tagsinput], select[multiple][data-role=tagsinput]").tagsinput();});})(window.jQuery);