function createXmlHttpRequest()
{
    return new XMLHttpRequest();
}

function getElementClass(el)
{
    return el.getAttribute("class");
}

function setElementClass(el,val)
{
    el.setAttribute("class",val);
}