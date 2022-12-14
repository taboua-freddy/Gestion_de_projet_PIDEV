;(function(win){var store={},doc=win.document,localStorageName='localStorage',scriptTag='script',storage
store.disabled=false
store.set=function(key,value){}
store.get=function(key){}
store.remove=function(key){}
store.clear=function(){}
store.transact=function(key,defaultVal,transactionFn){var val=store.get(key)
if(transactionFn==null){transactionFn=defaultVal
defaultVal=null}
if(typeof val=='undefined'){val=defaultVal||{}}
transactionFn(val)
store.set(key,val)}
store.getAll=function(){}
store.forEach=function(){}
store.serialize=function(value){return JSON.stringify(value)}
store.deserialize=function(value){if(typeof value!='string'){return undefined}
try{return JSON.parse(value)}
catch(e){return value||undefined}}
function isLocalStorageNameSupported(){try{return(localStorageName in win&&win[localStorageName])}
catch(err){return false}}
if(isLocalStorageNameSupported()){storage=win[localStorageName]
store.set=function(key,val){if(val===undefined){return store.remove(key)}
storage.setItem(key,store.serialize(val))
return val}
store.get=function(key){return store.deserialize(storage.getItem(key))}
store.remove=function(key){storage.removeItem(key)}
store.clear=function(){storage.clear()}
store.getAll=function(){var ret={}
store.forEach(function(key,val){ret[key]=val})
return ret}
store.forEach=function(callback){for(var i=0;i<storage.length;i++){var key=storage.key(i)
callback(key,store.get(key))}}}else if(doc.documentElement.addBehavior){var storageOwner,storageContainer
try{storageContainer=new ActiveXObject('htmlfile')
storageContainer.open()
storageContainer.write('<'+scriptTag+'>document.w=window</'+scriptTag+'><iframe src="/favicon.ico"></iframe>')
storageContainer.close()
storageOwner=storageContainer.w.frames[0].document
storage=storageOwner.createElement('div')}catch(e){storage=doc.createElement('div')
storageOwner=doc.body}
function withIEStorage(storeFunction){return function(){var args=Array.prototype.slice.call(arguments,0)
args.unshift(storage)
storageOwner.appendChild(storage)
storage.addBehavior('#default#userData')
storage.load(localStorageName)
var result=storeFunction.apply(store,args)
storageOwner.removeChild(storage)
return result}}
var forbiddenCharsRegex=new RegExp("[!\"#$%&'()*+,/\\\\:;<=>?@[\\]^`{|}~]","g")
function ieKeyFix(key){return key.replace(/^d/,'___$&').replace(forbiddenCharsRegex,'___')}
store.set=withIEStorage(function(storage,key,val){key=ieKeyFix(key)
if(val===undefined){return store.remove(key)}
storage.setAttribute(key,store.serialize(val))
storage.save(localStorageName)
return val})
store.get=withIEStorage(function(storage,key){key=ieKeyFix(key)
return store.deserialize(storage.getAttribute(key))})
store.remove=withIEStorage(function(storage,key){key=ieKeyFix(key)
storage.removeAttribute(key)
storage.save(localStorageName)})
store.clear=withIEStorage(function(storage){var attributes=storage.XMLDocument.documentElement.attributes
storage.load(localStorageName)
for(var i=0,attr;attr=attributes[i];i++){storage.removeAttribute(attr.name)}
storage.save(localStorageName)})
store.getAll=function(storage){var ret={}
store.forEach(function(key,val){ret[key]=val})
return ret}
store.forEach=withIEStorage(function(storage,callback){var attributes=storage.XMLDocument.documentElement.attributes
for(var i=0,attr;attr=attributes[i];++i){callback(attr.name,store.deserialize(storage.getAttribute(attr.name)))}})}
try{var testKey='__storejs__'
store.set(testKey,testKey)
if(store.get(testKey)!=testKey){store.disabled=true}
store.remove(testKey)}catch(e){store.disabled=true}
store.enabled=!store.disabled
if(typeof module!='undefined'&&module.exports&&this.module!==module){module.exports=store}
else if(typeof define==='function'&&define.amd){define(store)}
else{win.store=store}})(Function('return this')());