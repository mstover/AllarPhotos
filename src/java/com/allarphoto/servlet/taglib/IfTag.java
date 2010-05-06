package com.lazerinc.servlet.taglib;

/**
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @version 1.0
 */

public class IfTag extends IfBlock {
	private static final long serialVersionUID = 1;

	public int doStartTag() {
		super.doStartTag();
		if (parentIsTrue())
			return EVAL_BODY_TAG;
		else
			return SKIP_BODY;
	}

	public boolean parentIsTrue() {
		try {
			if (!isThisObject()) {
				IfBlock parentTag = (IfBlock) getParent();
				return parentTag.isConditionTrue();
			} else {
				return isConditionTrue();
			}
		} catch (Exception e) {
			return false;
		}
	}

}