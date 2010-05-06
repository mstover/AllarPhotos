NS4 = (document.layers) ? 1 : 0;
IE4 = (document.all && !document.getElementById) ? 1 : 0;
IE5 = (document.all && document.getElementById) ? 1 : 0;
NS6 = (!document.all && document.getElementById) ? 1 : 0

function displayNotification()
{
	setLayerVisible(true,'ajax_notification');
}

function undisplayNotification()
{
	setLayerVisible(false,'ajax_notification');
}

function displayFailure(req)
{
	failureDiv = document.getElementById("ajax_failure_notification");
	setLayerVisible(true,"ajax_failure_notification");
	failureDiv.innerHTML='<h2>Action Failed</h2>' +
					req.responseText + 
					'<div style="text-align:center;" onclick="javascript:setLayerVisible(false,\'ajax_failure_notification\');>Close</div>';
}


function sendRequest(url)
{
	displayNotification();
	req = createXmlHttpRequest();
	req.open("GET",url,true);
	req.onreadystatechange= function()
	{
		if(req.readyState == 4)
		{
			undisplayNotification();
		}
	}
	req.send(null);
}

function sendRequestWithFunc(url,func)
{
	displayNotification();
	req = createXmlHttpRequest();
	req.open("GET",url,true);
	req.onreadystatechange= function()
	{
		if(req.readyState == 4)
		{
	    		undisplayNotification();
			func(req);
		}
	}
	req.send(null);
}

function sendRequestWithFailFunc(url,successFunc,failFunc)
{
	displayNotification();
	req = createXmlHttpRequest();
	req.open("GET",url,true);
	req.onreadystatechange= function()
	{
		if(req.readyState == 4)
		{
			undisplayNotification();
			if(req.responseText.indexOf("success_success") > -1)
			{
				if(successFunc != null)
					successFunc(req);
			}
			else
			{
				displayFailure(req);
				if(failFunc != null)
					failFunc(req);
			}
		}
	}
	req.send(null);
}

function getToFunction(url,func)
{
	displayNotification();
	req=createXmlHttpRequest();
	req.open("GET",url,false);
	req.send(null);
	func(req.responseText);
}

function postToFunction(url,tmpl,func)
{
	displayNotification();
	req=createXmlHttpRequest();
	req.open("POST",url,false);
	req.send(tmpl);
	func(req.responseText);
	    undisplayNotification();
}


function getToDiv(divId,url)
{
	displayNotification();
	req=createXmlHttpRequest();
	req.open("GET",url,false);
	req.send(null);
	writeDiv(divId,req.responseText);
	    undisplayNotification();
}

function postToDiv(divId,url,tmpl)
{
	displayNotification();
	req=createXmlHttpRequest();
	req.open("POST",url,false);
	req.send(tmpl);
	writeDiv(divId,req.responseText);
	    undisplayNotification();
}

function writeDiv(divId,text)
{
	document.getElementById(divId).innerHTML=text;
}
		
function toggleView(divId)
{
	var viewEl = document.getElementById(divId+"_view");
	var baseEl = document.getElementById(divId);
	if(viewEl.innerHTML.length > 0)
	{
		baseEl.innerHTML=viewEl.innerHTML;
		viewEl.innerHTML='';
	}
	else
	{
		viewEl.innerHTML=baseEl.innerHTML;
		baseEl.innerHTML='';
	}
}

function mouseoverObj(id)
{
	var clazz = getElementClass(document.getElementById(id));
	setElementClass(document.getElementById(id),clazz.replace(/_out/,"_over"));
}

function mouseoutObj(id)
{
	var clazz = getElementClass(document.getElementById(id));
	setElementClass(document.getElementById(id),clazz.replace(/_over/,"_out"));
}

function imgToggle(id,img)
{
	var imgEl = document.getElementById(id);
	imgEl.src = img;
}