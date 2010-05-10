package com.allarphoto.servlet.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @version 1.0
 */

public class SaveString extends TemplateObject {
	private static final long serialVersionUID = 1;

	private static Map saved = new HashMap();

	private Object id;

	private boolean print = false;

	private boolean override = false;

	public void setId(String i) {
		id = i;
	}

	public void setId(Object i) {
		if (i instanceof String) {
			setId((String) i);
		} else {
			id = i;
		}
	}

	public void setOverride(String over) {
		override = Boolean.parseBoolean(over);
	}

	public void setPrint(String p) {
		print = Boolean.parseBoolean(p);
	}

	public int doStartTag() {
		if (saved.containsKey(id) && !override && !print) {
			return SKIP_BODY;
		} else {
			return EVAL_BODY_TAG;
		}
	}

	public int doAfterBody() {
		try {
			if (print) {
				bodyContent.getEnclosingWriter().write(
						encodeString((String) saved.get(id)));
			} else {
				saved.put(id, bodyContent.getString());
			}
		} catch (IOException e) {
		}
		return SKIP_BODY;
	}

}