<%@ include file="header.jsp" %>
<% use_image=false;
	String email_support = "webmaster@lazerinc.com";
	
%>
<div style='padding:5px;'>
	<div style='width: 100px; float:right;'><a href='javascript:window.close();'>Close Page</a></div>
	<h2 style='margin-top: 0; width: 175px;'>User Tips</h2>
	<ul>
		<li>Image Size Options
			<ul>
				<li>PowerPoint, Word, and Web</li>
				<li>Line Art/Vector Images</li>
				<li>High-End Print</li>
			</ul>
		</li>
		<li>Searching For Images
			<ul>
				<li>Browsing</li>
				<li>Quick Search Filters</li>
			</ul>
		</li>
		<li>Not finding an image to use?</li>
	</ul>
</div>
<%@ include file="midsplit.jsp" %>

<H3>Searching for Images:</H3>
<P>
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>You can search for images by using the Quick Image Search or by Browse Image Libraries. </SPAN
></P>
<P style="margin-right:26px; line-height:18px">
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>In </SPAN
><SPAN style="font-size:12pt; color:#000000"
>Quick Image Search</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>, use a keyword (product name, product number, or image content) to find an image. Please double-check spelling and use minimal keywords to find image. </SPAN
></P>
<P style="margin-right:30px">
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>To </SPAN
><SPAN style="font-size:12pt; color:#000000"
>Browse Image Libraries</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>, select a library folder icon and browse through library subcategories. All images related to category will appear to right. </SPAN
></P>
<P style="margin-right:39px; line-height:18px">
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>For more information about image specifications and download instructions, please </SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#0000FF; text-decoration:underline"
><a href='mailto:<%= email_support %>'>email</a>.</SPAN
></P>
<P style="margin-right:0px; line-height:18px">

<h3>Image Size Options:</h3>
<p>
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>Select Low-Resolution</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
> images for Microsoft&reg; PowerPoint&reg; presentations, Microsoft&reg;<BR></SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>Word&reg; documents or the web. Please do not select high-resolution for these applications</SPAN
><SPAN style=" font-weight:normal"
>.<BR>
</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>High resolution files will take up valuable space on your computer and slow your </SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>presentation. If you need a high-resolution file in the future, please come back to the<BR>
</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>Welch Allyn Image Library and order high-resolution image</SPAN
><SPAN style=" font-weight:normal"
>.<BR></SPAN
></P>
<P style="margin-right:10px; line-height:18px">
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>Select </SPAN
><SPAN style="font-size:12pt; color:#000000"
>High-Resolution</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
> images for offset or high-end digital printing (typically used b</SPAN
><SPAN style=" font-weight:normal"
>y<BR></SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>graphic designers for </SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>catalogs, advertisements, brochures, sell sheets and tradeshow graphics</SPAN
><SPAN style=" font-weight:normal"
>)<BR>
</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>Designers please note: JPG images are formatted for RGB color. Files will need to b</SPAN
><SPAN style=" font-weight:normal"
>e<BR></SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>converted from RGB to CMYK in order to display correctly in printed documents</SPAN
><SPAN style=" font-weight:normal"
>.<BR></SPAN
></P>
<P style="margin-right:0px; line-height:18px">
<SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>Select </SPAN
><SPAN style="font-size:12pt; color:#000000"
>Line Art</SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
> for logos and line art illustrations. Only select low-resolution if you nee</SPAN
><SPAN style=" font-weight:normal"
>d<BR></SPAN
><SPAN style="font-family:'serif', 'Times New Roman PSMT'; font-size:12pt; font-weight:normal; color:#000000"
>to use image in Word or Powerpoint</SPAN
><SPAN style=" font-weight:normal"
>.<BR></SPAN
></P>

<h3>Not Finding an Image to Use?</h3>
For specific artwork needs not served by our online library, please 
<a href='mailto:<%= email_support %>'>contact us</a>.

</DIV>

<%@ include file="footer.jsp" %>