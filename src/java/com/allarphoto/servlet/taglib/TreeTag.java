package com.lazerinc.servlet.taglib;

import java.io.IOException;

import org.coinjema.collections.HashTree;

import com.lazerinc.utils.Functions;

/**
 * Title: Lazerweb Description: Lazerweb - Lazer Inc. version 3.0 Copyright:
 * Copyright (c) 2001 Company: Lazer Inc.
 * 
 * @author Michael Stover
 * @version 1.0
 */

public class TreeTag extends MapTag {
	private static final long serialVersionUID = 1;

	private HashTree tree;

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doStartTag() {
		if (tree.size() > 0) {
			iter = tree.list().iterator();
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
		Functions.javaLog("getAssociatedValue() : currentValue = "
				+ currentValue);
		return tree.getTree(currentValue);
	}

	private void setTree(HashTree t) {
		Functions.javaLog("setTree(HashTree) : tree = " + t);
		tree = t;
		if (tree == null) {
			tree = new HashTree();
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param col
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setTree(String col) {
		Functions.javaLog("setTree(String) : tree = " + col);
		try {
			if (CONTEXT_VARIABLE.equals(col)) {
				tree = (HashTree) getContextualCollection();
			} else if (CONTEXT_ASSOCIATED_VARIABLE.equals(col)) {
				tree = (HashTree) getAssociatedCollection();
			} else {
				tree = (HashTree) pageContext.getAttribute(col);
			}
		} catch (ClassCastException e) {
			tree = new HashTree();
		}
	}

	public void setTree(Object o) {
		if (o instanceof String) {
			setTree((String) o);
		} else if (o instanceof HashTree) {
			setTree((HashTree) o);
		}
	}

}