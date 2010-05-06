// A utility function that returns true if a string contains only white space
function isblank(s)
{
    for(var i = 0; i < s.length; i++)
    {
        var c = s.charAt(i);
        if((c != ' ') && (c != '\n') && (c != ''))
            return false;
    }
    return true;
}

// This is the function that performs form verification. It is invoked from
// the onsubmit event handler. The handler should return whatever value this 
// function returns.
function verify(f)
{
    var msg;
    var empty_fields = "";
    var errors = "";

    // Loop through the form elements, looking for all Text and Textarea 
    // elements that don't have an "optional" property defined. Then check 
    // for fields that are empty and make a list of them. Also, if any of 
    // these elements have a "min" or a "max" property defined, verify that 
    // they are numbers and are in the right range. If the element has a 
    // "numeric" property defined, verify that it is a number, but don't 
    // check it's range. Put together the error messages for fields that 
    // do not meet the preset specifications.
    for(var i = 0; i < f.length; i++)
    {
        var e = f.elements[i];
        if(( (e.type == "text") || (e.type == "textarea")) && e.getAttribute("optional") != "true")
        {
            // First check if the field is empty
            if((e.value == null) || (e.value == "") || isblank(e.value))
            {
                empty_fields += "\n        " + e.name;
                continue;
            }

            // Now check for fields that are suppose to be numeric
            if(e.numeric || (e.min != null) || (e.max != null))
            {
                var v = parseFloat(e.value);
                if(isNaN(v) ||
                        ((e.min != null) && (v < e.min)) ||
                        ((e.max != null) && (v > e.max)) )
                {
                    errors += "- The Field " + e.name + " must be a number";
                    if(e.min != null)
                        errors += " that is greater than " + e.min;
                    if((e.max != null) && (e.min != null))
                        errors += " and less than " + e.max;
                    else if(e.max != null)
                        errors += " that is less than " + e.max;
                    errors += ".\n";
                }
            }
        }
    }
    // Now, if there were any errors, display the messages, and
    // return false to prevent the form from being submitted. 
    // Otherwise return true.
    if (!empty_fields && !errors) return true;

    msg  = "______________________________________________________\n\n"
    msg += "The form was not submitted because of the following error(s).\n";
    msg += "Please correct these error(s) and re-submit.\n";
    msg += "______________________________________________________\n\n"

    if (empty_fields) {
        msg += "- The following required field(s) are empty:" 
                + empty_fields + "\n";
        if (errors) msg += "\n";
    }
    msg += errors;
    alert(msg);
    return false;
}

// This Function will enforce a Maximum Length on a TextArea input field
// Tag the textarea input element with name="message" and 
// 	onKeyDown="enforceMaxLength(this.form.message,this.form.remaining,250);"
//	onKeyUp="enforceMaxLength(this.form.message,this.form.remaining,250);"
function enforceMaxLength(field, countfield, maxLength) {
	if (field.value.length > maxLength) {
		// if too long...trim it!
		field.value = field.value.substring(0, maxLength);
	}else{ 
		// otherwise, update 'characters left' counter
		countfield.value = (maxLength - field.value.length);
	}
}