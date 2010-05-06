<%@ include file="/include/global.inc" %>
<act:request action="get_families"/>
<act:request action="action_validate_login"/>
<act:executeActions/>

<vel:velocity>
	
	#macro(mainContent)
	<h2>View Download Activities</h2>
	<form METHOD="GET" name="download_activity">
		#hidden("actiona" "get_download_requests")
		<span class="label">Select Library:</span> <select name="product_family" size="1">
			#optionList($productFamilies $NULL '')
		</select>
		<span class="label">From Date</span><input type="text" size="10" name="since_date" value='$!params.getParameter("since_date",null)'>
		#datePick("download_activity" "since_date")
		<span class="label">To Date</span><input type="text" size="10" name="to_date" value='$!params.getParameter("to_date",null)'>
		#datePick("download_activity" "to_date")
		<input type="submit">
	</form>
	#if($totalSize)
		<h2>$params.getParameter("product_family") Download Statistics</h2>
		<div style="float:left;" class="eyecatching">From:</div> <div style="margin-left:440px;"><b>$params.getParameter("since_date")</b></div>
		<div style="clear:left;"><div style="float:left;" class="eyecatching">To:</div> <div style="margin-left:440px;"><b>$params.getParameter("to_date")</b></div></div>
		<div style="clear:left;"><div style="float:left;" class="eyecatching">Total Downloads:</div> <div style="margin-left:440px;"><b>$format.formatNumber($totalSize,"#,#00.00") Mb</b></div></div>
		<div style="clear:left;"><h3 style="float:left;">User Details</h3> <h3 style="margin-left:440px;padding-top:16px;">In KiloBytes</h3></div>
		<ul>#foreach($entry in $userMap.keySet())
			<li style="clear:left;"><div style="float:left;" class="label">$entry.firstName $entry.lastName</div> <div style="width:90px;margin-left:400px;text-align:right;">$format.formatNumber($math.div($userMap.get($entry),1024),"#,#00.00")</div></li>
		#end
		</ul>
	#end
#end
	#page("Download Activity" "admin")
</vel:velocity>