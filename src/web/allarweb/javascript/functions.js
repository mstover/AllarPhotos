var dateField = 0 ;

var currentMenu = "";
var previousMenu = "";

NS4 = (document.layers) ? 1 : 0;
IE4 = (document.all && !document.getElementById) ? 1 : 0;
IE5 = (document.all && document.getElementById) ? 1 : 0;
NS6 = (!document.all && document.getElementById) ? 1 : 0



function popUp(on)
{
   ILay = document.getElementById('popCal');
   if (on) {
      ILay.style.visibility = "visible";
   }
   else {
      ILay.style.visibility = "hidden";
   }
}

function setCookie(name,value)
{
	document.cookie="stratlib_"+name+"="+escape(value);
}

function getCurrentMenu()
{
	return currentMenu;
}

function enterMenu(menu)
{
	if(previousMenu != "" && previousMenu != menu)
	{
		setLayerVisible(false,previousMenu);
	}
	setLayerVisible(true,menu);
	currentMenu = menu;
	previousMenu = menu;
}

function exitMenu(menu)
{
	previousMenu = menu;
	currentMenu = "";
	var nowMenu = setTimeout("closeMenu()",1000);
}

function closeMenu()
{
	if(currentMenu == "")
	{
		setLayerVisible(false,previousMenu);
	}
}

function selectCheckboxes(form,name)
{
	for (var i=0; i < form.elements.length; ++i)
	{
		el = form.elements.item(i);
		if(el.getAttribute("type") != null && el.getAttribute("type").toUpperCase() == "CHECKBOX" && 
		      el.getAttribute("name") != null && el.getAttribute("name") == name)
		{
			el.setAttribute("checked","checked");
		}
	}
}

function unselectAllCheckboxes(form)
{
	for (var i=0; i < form.elements.length; ++i)
	{
		el = form.elements.item(i);
		if(el.getAttribute("type") != null && el.getAttribute("type").toUpperCase() == "CHECKBOX")
		{
			el.removeAttribute("checked");
		}
	}
}


function setLayerVisible(on,name)
{
   ILay = document.getElementById(name);
   if(ILay != null)
   {
      if (on) {
         ILay.style.visibility = "visible";
      }
      else {
         ILay.style.visibility = "hidden";
      }
   }
}

function notValidDate(dateStr) {
   var datePat = /^\d{1,2}(\/)\d{1,2}(\/)\d{2,4}$/
   if (dateStr.match(datePat)) {
       return false
   } else {
       return true
   }
}

function SeeDate(dateValue)
{
  dateField.value = dateValue;

  HideDate();

  return;
}


function ShowDate(i)
{
  if (dateField != null) {
          HideDate() ;
  }

  dateField = i ;

  popUp(true);

  return;
}


function moveLayers()
{
   ILay = document.getElementById('popCal');
   ILay.style.pixelLeft = x;
   ILay.style.pixelTop = y;
}

function HideDate()
{
  popUp(false) ;

  return;
}

function redirect(url) {
  location.href=url;
}

function changeAction(f,act)
{
	f.action = act;
	f.submit();
}

function getRejectReason()
{
	return prompt("Please provide a reason for rejecting this request","");
}

function confirmAndCommit(message)
{
	if(confirm(message))
	{
		return true;
	}
	else
	{
		return false;
	}
}

function changeFormValue(element,val)
{
	element.value=val;
	element.form.submit;
}

function inArray(ary,item)
{
	for(var i = 0;i < ary.length;i++)
	{
		if(ary[i] == item) return true;
	}
	return false;
}


function show_people_picker(url,formName,elementName, userId,multiple)
{
var full_url = url+"?formName="+formName+"&elementName="+elementName+
				"&userId="+userId+"&multiple="+multiple;
var vWinCal = window.open(full_url, "PeoplePicker", 
		"width=400,height=250,status=no,resizable=yes,top=200,left=200");
	vWinCal.opener = self;
}
