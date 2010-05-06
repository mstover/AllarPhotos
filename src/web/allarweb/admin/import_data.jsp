<%@ include file="/include/global.inc" %>
<act:request action="action_validate_login"/>
<act:request action="get_families"/>
<act:executeActions/>

<vel:velocity>
	
	#macro(mainContent)
	<h2>Import Keyword File</h2>
	<form METHOD="POST" ENCTYPE='multipart/form-data'>
		#hidden("actiona" "keyword_file_import")
		<span class="label">Select Library:</span> <select name="product_family" size="1">
			#optionList($productFamilies $NULL '')
		</select>
		<span class="label">File To Upload:</span> <input type="file" name="keyword_file"><p>
		<span class="label">Character Encoding:</span> <select name="charSet" size="1">
			<option value="" selected>Default(iso-8859-1)</option>
			<option value="UNICODE">Unicode</option>
			<option value="utf-8">UTF-8</option>
		</select>
		<input type="submit">
	</form>
#end
	#page("Upload Keyword File" "admin")
</vel:velocity>