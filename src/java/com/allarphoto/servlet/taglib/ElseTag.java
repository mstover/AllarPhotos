package com.allarphoto.servlet.taglib;

/**
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @version 1.0
 */

public class ElseTag extends IfTag implements ContextualTag {
	private static final long serialVersionUID = 1;

	public int doStartTag() {
		if (!parentIsTrue())
			return EVAL_BODY_TAG;
		else
			return SKIP_BODY;
	}

}