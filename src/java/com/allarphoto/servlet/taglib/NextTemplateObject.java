package com.allarphoto.servlet.taglib;

import java.util.NoSuchElementException;

/*******************************************************************************
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @created $Date: 2007/07/17 15:26:21 $
 * @version 1.0
 ******************************************************************************/

public class NextTemplateObject extends TemplateObject implements ContextualTag {
	private static final long serialVersionUID = 1;

	public int doStartTag() {
		if (getParent() instanceof ContextualTag) {
			try {
				((ContextualTag) getParent()).gotoNext();
			} catch (NoSuchElementException e) {
				writeNullText();
				return SKIP_BODY;
			}
		}
		return EVAL_BODY_TAG;
	}

	protected void writeNullText() {
		try {
			pageContext.getOut().write(encodeString(nullText));
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

}
