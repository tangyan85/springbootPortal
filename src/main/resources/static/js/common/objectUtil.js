function isEmpty(obj) {
    if(obj==undefined||obj== null||jQuery.trim(obj)==""){
		return true;
    }
    if (!obj && typeof (obj) != "undefined" && obj !== '' && obj !== 0) {　　　　　　　
        return true;
    }
    if (Array.prototype.isPrototypeOf(obj) && obj.length === 0) {
        return true;
    }
    if (Object.prototype.isPrototypeOf(obj) && Object.keys(obj).length === 0) {
        return true;　
    }　　
    return false;
}