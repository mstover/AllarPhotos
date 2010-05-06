function toggleRight(rightName,resId,groupId,divId)
{
	var value = document.getElementById(divId).src;
	if(value.indexOf("check.gif") > -1)
		value = "true";
	else
		value = "false";
	var newVal = value;
	if(newVal == "true")
		newVal = "false";
	else
		newVal = "true";
	sendRequestWithFailFunc("dummy.jsp?action=update_right&resource_id="+resId+"&right="+rightName+"&group_id="+groupId+
	            "&value="+newVal,changeRightIfSuccess(divId,value),null);
}

function changeRightIfSuccess(divId,value)
{
	var res = function(req)
	{
		el = document.getElementById(divId);
		if(value == "true")
		{
			el.src=el.src.replace(/check/,"x");
		}
		else
		{
			el.src=el.src.replace(/x/,"check");
		}
	}
	return res;
}

function removeFromArray(array,val)
{
	var len = array.length;
	var removed = false;
	for(var i = 0;i < len;i++)
	{
		if(array[i] == val)
		{
			removed = true;
			if(i+1 < len)
			{
				array[i] = array[i+1];
			}
			else
			{
				array[i] = null;
			}
		}
		else if(removed)
		{
			if(i+1 < len)
			{
				array[i] = array[i+1];
			}
			else
			{
				array[i] = null;
			}
		}
	}
	return removed;
}

function getQueryFragment(idList,paramName,startAmp,startQuestion)
{
	var query = "";
	if(startQuestion) query = "?";
	else if(startAmp) query = "&";
	var len = idList.length;
	for(var i = 0;i < len;i++)
	{
		if(idList[i] != null)
		{
			query = query + paramName + "=" + idList[i];
			if(i+1 < len) query = query + "&";
		}
	}
	return query;
}