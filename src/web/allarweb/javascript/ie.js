// if IE5.5+ on Win32, then display PNGs with AlphaImageLoader
var pngAlpha = true;

function createXmlHttpRequest()
{
    try {
  return new ActiveXObject("Msxml2.XMLHTTP");
 } catch (e) {
  try {
   return new ActiveXObject("Microsoft.XMLHTTP");
  } catch (E) {
   alert("Your Browser does not support a necessary JavaScript component.  We recommend you use Safari or Firefox for Mac.");
   return null;
  }
 }
}

function getElementClass(el)
{
    return el.className;
}

function setElementClass(el,val)
{
    el.className = val;
}