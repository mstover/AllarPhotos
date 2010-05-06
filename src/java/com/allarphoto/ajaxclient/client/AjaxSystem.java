package com.lazerinc.ajaxclient.client;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.components.PopupWarning;
import com.lazerinc.ajaxclient.client.components.basic.ComboBox;

public class AjaxSystem {
	static int count = 0;

	static PopupPanel productPopup;

	public static HTML NBS = new HTML("&nbsp;");

	public static void arraycopy(Object[] src, int index, Object[] dest,
			int toIndex, int length) {
		int end = index + length;
		for (int i = index; i < end; i++) {
			dest[toIndex++] = src[i];
		}
	}

	public static ScrollPanel findParentScrollPanel(Widget widget) {
		if (widget == null)
			return null;
		if (widget instanceof ScrollPanel) {
			return (ScrollPanel) widget;
		}
		Widget parent = widget.getParent();
		return findParentScrollPanel(parent);
	}

	public static int getAbsoluteTop(Widget w) {
		if (Services.getServices().browser.equals("Mozilla")) {
			ScrollPanel parent = findParentScrollPanel(w);
			if (parent != null) {
				return w.getAbsoluteTop();
			}
		}
		return w.getAbsoluteTop();
	}

	public static void clearPopup() {
		if (productPopup != null)
			productPopup.hide();
	}

	public static String getStackTrace(Throwable e) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < e.getStackTrace().length; i++) {
			buf.append(e.getStackTrace()[i].toString()).append("\n");
		}
		return buf.toString();
	}

	public static void setPopup(PopupPanel p) {
		productPopup = p;
	}

	public static Object[] toArray(Collection c) {
		Object[] o = new Object[c.size()];
		Iterator iter = c.iterator();
		int count = 0;
		while (iter.hasNext()) {
			o[count++] = iter.next();
		}
		return o;
	}

	public static String getHeightToWindowBottom(int position) {
		return (Window.getClientHeight() - position - 2) + "px";
	}

	public static String getWidthToWindowLeft(int position) {
		return (Window.getClientWidth() - position - 5) + "px";
	}

	public static String[] toStringArray(Collection c) {
		String[] o = new String[c.size()];
		Iterator iter = c.iterator();
		int count = 0;
		while (iter.hasNext()) {
			o[count++] = (String) iter.next();
		}
		return o;
	}

	public static String uniqueId() {
		return "lazer_id_" + (count++);
	}

	public static String parseId(String html) {
		int index = html.indexOf("=\"lazer_id_");
		if (index > -1) {
			StringBuffer buf = new StringBuffer("lazer_id_");
			index += 11;
			while (html.charAt(index) >= '0' && html.charAt(index) <= '9')
				buf.append(html.charAt(index++));
			return buf.toString();
		}
		return "";
	}

	public static Label getLabel(String text) {
		Label l = new Label(text);
		l.setStyleName("label");
		return l;
	}

	public static Label getLabel(String text, String style) {
		Label l = getLabel(text);
		l.addStyleName(style);
		return l;
	}

	public static Label getTitle(String text) {
		Label l = new Label(text);
		l.setStyleName("title");
		return l;
	}

	public static Label getText(String text) {
		Label l = new Label(text);
		l.setStyleName("text");
		return l;
	}

	public static Label getText(String text, String style) {
		Label l = new Label(text);
		l.setStyleName(style);
		return l;
	}

	public static void addToList(ListBox list, String[] items) {
		if (list != null && items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++)
				list.addItem(items[i]);
		}
	}

	public static void addToComboList(ComboBox list, String[] items) {
		if (list != null && items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++)
				list.addItem(items[i]);
		}
	}

	public static void addToList(ListBox list, AjaxGroup[] items) {
		if (list != null && items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++)
				list.addItem(items[i].getName());
		}
	}

	public static int findIndex(String[] list, String value) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(value))
				return i;
		}
		return -1;
	}

	private static void verifySorted(String[] list) {
		if (list.length > 1) {
			String prev = list[0];
			for (int i = 1; i < list.length; i++) {
				if (prev.compareTo(list[i]) > 0)
					new PopupWarning("Bad list for binary search: "
							+ printList(list));
				prev = list[i];
			}
		}
	}

	private static String printList(String[] list) {
		StringBuffer buf = new StringBuffer("[");
		for (int i = 0; i < list.length; i++)
			buf.append(list[i]).append(", ");
		buf.append("]");
		return buf.toString();
	}

	public static int findIndexBinarySearch(String[] list, String value) {
		verifySorted(list);
		int prevHigh = list.length;
		int prevLow = 0;
		int prev = -1;
		int i = prevHigh / 2;
		while (prev != i) {
			prev = i;
			String listValue = list[i];
			int comp = listValue.compareTo(value);
			if (comp == 0)
				return i;
			else if (comp < 0) {
				prevLow = i;
				i = (i + prevHigh) / 2;
			} else {
				prevHigh = i;
				i = (i + prevLow) / 2;
			}
		}
		return -1;
	}

	public static int findIndex(ListBox list, String value) {
		for (int i = 0; i < list.getItemCount(); i++) {
			if (list.getItemText(i).equals(value))
				return i;
		}
		return 0;
	}

	public static int findIndexBinarySearch(ListBox list, String value) {
		int prevHigh = list.getItemCount();
		int prevLow = 0;
		int prev = -1;
		int i = prevHigh / 2;
		while (prev != i) {
			prev = i;
			String listValue = list.getItemText(i);
			int comp = listValue.compareTo(value);
			if (comp == 0)
				return i;
			else if (comp < 0) {
				prevLow = i;
				i = (i + prevHigh) / 2;
			} else {
				prevHigh = i;
				i = (i + prevLow) / 2;
			}
		}
		return 0;
	}

	public static int findComboIndexBinarySearch(ComboBox list, String value) {
		int prevHigh = list.getItemCount();
		int prevLow = 0;
		int prev = -1;
		int i = prevHigh / 2;
		while (prev != i) {
			prev = i;
			String listValue = list.getItemText(i);
			int comp = listValue.compareTo(value);
			if (comp == 0)
				return i;
			else if (comp < 0) {
				prevLow = i;
				i = (i + prevHigh) / 2;
			} else {
				prevHigh = i;
				i = (i + prevLow) / 2;
			}
		}
		return 0;
	}

}
