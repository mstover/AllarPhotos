package com.allarphoto.servlet.taglib;

import java.util.NoSuchElementException;

/**
 * Title: Description: Copyright: Copyright (c) 2000 Company:
 * 
 * @author
 * @version 1.0
 */

public interface ContextualTag {

	public Object getCurrentValue();

	public Object getCurrentValue(int i);

	public void gotoNext() throws NoSuchElementException;

	public Object getAssociatedValue();

	public Object getAssociatedValue(int i);
}