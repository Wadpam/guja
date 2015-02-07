var basicUsername = 'kh', basicPassword = 'Bassac';

//Get request parameters from url with start by #
function getParameterByName(name) {
	console.log(window.location);
    var match = RegExp('[?#&]' + name + '=([^&]*)').exec(window.location);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

/**
 * redirect to login success page, currently to localoffers.html
 */
function redirectLoginSuccess(){
	var urls = getEndPoints();
	
	window.location.href = urls.apiUrl.replace("/api/","/") + '/home.html';
}

/**
 * get screen width
 */
function getScreenWidth(){
	var screenWidth;
	// the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight
    if (typeof window.innerWidth != undefined) {
        screenWidth = window.innerWidth;
    }
    // IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
    else if (typeof document.documentElement != undefined && typeof document.documentElement.clientWidth != undefined
    		&& typeof document.documentElement.clientWidth != undefined) {
        screenWidth = document.documentElement.clientWidth;
    }
    //Older version of IE
    else{
    	screenWidth = document.getElementsByTagName('body')[0].clientWidth;
    }
    return screenWidth;
}

/**
 * for landing page usering only, to detect for different browser screen
 */
function getImageSize(){
	var imageSize = "s";
	var screenWidth = getScreenWidth();
	if(screenWidth >= 320){
		imageSize =  "l";
	}
	else if(screenWidth > 160 && screenWidth <= 240){
		imageSize = "s";
	}else{
		imageSize = "m";
	}
	
	return imageSize;
}

