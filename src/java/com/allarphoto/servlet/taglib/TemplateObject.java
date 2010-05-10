package com.allarphoto.servlet.taglib;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

import javax.servlet.jsp.tagext.BodyTagSupport;

public abstract class TemplateObject extends BodyTagSupport implements
		ContextualTag {

	/***************************************************************************
	 * !ToDo (Field description)
	 **************************************************************************/
	protected final static String CONTEXT_VARIABLE = "$_";

	protected final static String CONTEXT_ASSOCIATED_VARIABLE = "@_";

	protected String nullText = "";

	protected boolean urlEncode = false;

	protected Object validateValue(Object value) {
		if (value == null)
			value = nullText;
		return value;
	}

	protected Object validateValue(Object value, Object alternate) {
		if (value == null) {
			return alternate;
		}
		return value;
	}

	protected Object getContextValue(String on) {
		if (on == null)
			return null;
		Object o = null;
		if (on.endsWith(CONTEXT_VARIABLE))
			o = getCurrentValue(on.length() - 2);
		else if (on.endsWith(CONTEXT_ASSOCIATED_VARIABLE))
			o = getAssociatedValue(on.length() - 2);
		else if (on.startsWith(CONTEXT_VARIABLE)) {
			return pageContext.findAttribute(on.substring(2));
		}
		return o;
	}

	public void setEncode(boolean encode) {
		urlEncode = encode;
	}

	public void setEncode(String encode) {
		urlEncode = Boolean.parseBoolean(encode);
	}

	public int doAfterBody() {
		try {
			bodyContent.getEnclosingWriter().write(
					encodeString(bodyContent.getString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	protected String encodeString(String value) {
		if (urlEncode) {
			return URLEncoder.encode(value);
		}
		return value;
	}

	public int doStartTag() {
		return EVAL_BODY_TAG;
	}

	public void gotoNext() throws NoSuchElementException {
		if (getParent() instanceof ContextualTag) {
			((ContextualTag) getParent()).gotoNext();
		} else
			throw new NoSuchElementException();
	}

	public Object getCurrentValue(int recursions) {
		if (recursions < 0) {
			return getCurrentValue();
		}
		recursions--;
		for (int i = recursions; i >= -1; i--) {
			try {
				return ((ContextualTag) getParent()).getCurrentValue(i);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}

	public Object getAssociatedValue(int recursions) {
		if (recursions < 0) {
			return getAssociatedValue();
		}
		recursions--;
		for (int i = recursions; i >= -1; i--) {
			try {
				return ((ContextualTag) getParent()).getAssociatedValue(i);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}

	public Object getAssociatedValue() {
		if (getParent() instanceof ContextualTag) {
			return ((ContextualTag) getParent()).getAssociatedValue();
		}
		return null;
	}

	public Object getCurrentValue() {
		if (getParent() instanceof ContextualTag) {
			return ((ContextualTag) getParent()).getCurrentValue();
		}
		return null;
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param nt
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setNullText(String nt) {
		nullText = nt;
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 **************************************************************************/
	protected void writeNullText() {
		try {
			if (urlEncode) {
				nullText = URLEncoder.encode(nullText);
			}
			bodyContent.getEnclosingWriter().write(nullText);
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

}