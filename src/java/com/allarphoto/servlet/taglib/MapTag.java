package com.allarphoto.servlet.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapTag extends ListTag {
	private static final long serialVersionUID = 1;

	private Map map;

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doStartTag() {
		if (map.size() > 0) {
			iter = map.keySet().iterator();
			currentValue = iter.next();
			return EVAL_BODY_TAG;
		} else {
			try {
				pageContext.getOut().write(altText);
			} catch (IOException e) {
			}
			return SKIP_BODY;
		}
	}

	public Object getAssociatedValue() {
		return map.get(currentValue);
	}

	public void setMap(Map m) {
		map = m;
		if (map == null) {
			map = new HashMap();
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param col
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setMap(String col) {
		try {
			if (CONTEXT_VARIABLE.equals(col)) {
				map = (Map) getContextualCollection();
			} else if (CONTEXT_ASSOCIATED_VARIABLE.equals(col)) {
				map = (Map) getAssociatedCollection();
			}
			map = (Map) pageContext.getAttribute(col);
		} catch (ClassCastException e) {
			map = new HashMap();
		}
	}

}