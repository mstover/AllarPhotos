package com.allarphoto.servlet.taglib;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.allarphoto.utils.Functions;

/*******************************************************************************
 * @author Michael Stover
 * @created $Date: 2007/07/17 15:26:21 $
 * @version 1.0
 ******************************************************************************/

public class ListTag extends ObjectCallTemplateObject implements ContextualTag {
	private static final long serialVersionUID = 1;

	protected Object currentValue;

	protected String altText = "";

	protected String betweenText = "";

	protected String nullText = "";

	protected Iterator iter;

	private Collection list;

	/***************************************************************************
	 * Sets the Collection attribute of the ListTag object
	 * 
	 * @param c
	 *            The new Collection value
	 **************************************************************************/
	public void setCollection(Collection c) {
		list = c;
		if (list == null) {
			list = new LinkedList();
		}
	}

	public void setCollection(Object[] arr) {
		if (arr == null) {
			list = new LinkedList();
		} else {
			list = Arrays.asList(arr);
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param col
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setCollection(String col) {
		try {
			list = (Collection) getContextValue(col);
			if (list == null) {
				list = (Collection) pageContext.getAttribute(col);
			}
		} catch (ClassCastException e) {
			list = new LinkedList();
		}
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
	 * 
	 * @param bt
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setBetweenText(String bt) {
		betweenText = bt;
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @param at
	 *            !ToDo (Parameter description)
	 **************************************************************************/
	public void setAltText(String at) {
		altText = at;
	}

	/***************************************************************************
	 * Gets the CurrentValue attribute of the ListTag object
	 * 
	 * @return The CurrentValue value
	 **************************************************************************/
	public Object getCurrentValue() {
		return currentValue;
	}

	/***************************************************************************
	 * Gets the AssociatedValue attribute of the ListTag object
	 * 
	 * @return The AssociatedValue value
	 **************************************************************************/
	public Object getAssociatedValue() {
		return null;
	}

	protected void initialize() {
		if (list == null) {
			try {
				list = (Collection) this.getMethod(object, methodName, classes)
						.invoke(object, args);
			} catch (Exception e) {
				list = new LinkedList();
			}
		}
		iter = list.iterator();
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doStartTag() {
		initialize();
		if (hasNext()) {
			assignCurrentValue();
			return EVAL_BODY_TAG;
		} else {
			try {
				pageContext.getOut().write(altText);
			} catch (IOException e) {
			}
			return SKIP_BODY;
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doAfterBody() {
		try {
			bodyContent.getEnclosingWriter().write(
					encodeString(bodyContent.getString()));
			bodyContent.clearBody();
			if (hasNext()) {
				assignCurrentValue();
				bodyContent.getEnclosingWriter().write(betweenText);
				return EVAL_BODY_TAG;
			} else {
				return SKIP_BODY;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return SKIP_BODY;
		}
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @return !ToDo (Return description)
	 **************************************************************************/
	public int doEndTag() {
		return EVAL_PAGE;
	}

	/***************************************************************************
	 * !ToDo (Method description)
	 * 
	 * @exception NoSuchElementException
	 *                Description of Exception
	 **************************************************************************/
	public void gotoNext() throws NoSuchElementException {
		if (hasNext()) {
			assignCurrentValue();
		} else {
			currentValue = null;
			throw new NoSuchElementException();
		}
	}

	/***************************************************************************
	 * Gets the AssociatedCollection attribute of the ListTag object
	 * 
	 * @return The AssociatedCollection value
	 **************************************************************************/
	protected Object getAssociatedCollection() {
		Functions.javaLog("getAssociatedCollection() : getParent = "
				+ getParent());
		if (getParent() instanceof ContextualTag) {
			try {
				return ((ContextualTag) getParent()).getAssociatedValue();
			} catch (ClassCastException e) {
			}
		}
		return null;
	}

	/***************************************************************************
	 * Gets the ContextualCollection attribute of the ListTag object
	 * 
	 * @return The ContextualCollection value
	 **************************************************************************/
	protected Object getContextualCollection() {
		if (getParent() instanceof ContextualTag) {
			try {
				return ((ContextualTag) getParent()).getCurrentValue();
			} catch (ClassCastException e) {
			}
		}
		return null;
	}

	/***************************************************************************
	 * Description of the Method
	 **************************************************************************/
	protected void assignCurrentValue() {
		currentValue = iter.next();
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 **************************************************************************/
	protected boolean hasNext() {
		return iter.hasNext();
	}

}
