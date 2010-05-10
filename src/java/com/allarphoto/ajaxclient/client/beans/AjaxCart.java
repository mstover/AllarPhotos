package com.allarphoto.ajaxclient.client.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.allarphoto.ajaxclient.client.CartListener;
import com.allarphoto.ajaxclient.client.components.icons.BaseIcon;

public class AjaxCart implements IsSerializable {

	/**
	 * @gwt.typeArgs <com.allarphoto.ajaxclient.client.beans.Request>
	 */
	Set requests = new HashSet();

	transient List listeners;

	public AjaxCart() {
		listeners = new ArrayList();
	}

	public void add(Request r) {
		requests.add(r);
		notifyListeners(r, true);
	}

	protected void notifyListeners(Request r, boolean added) {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			CartListener cl = (CartListener) iter.next();
			if (added)
				cl.added(r);
			else
				cl.removed(r);
		}
	}

	public void transfer(AjaxCart c) {
		requests = c.requests;
	}

	public void remove(Request r) {
		requests.remove(r);
		notifyListeners(r, false);
	}

	public void redraw() {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			CartListener cl = (CartListener) iter.next();
			cl.redraw();
		}
	}

	public Collection getRequests() {
		return requests;
	}

	public Request getRequest(AjaxProduct p) {
		Iterator iter = requests.iterator();
		while (iter.hasNext()) {
			Request r = (Request) iter.next();
			if (r.getProduct().equals(p))
				return r;
		}
		return null;
	}

	public void clear() {
		Iterator r = requests.iterator();
		while (r.hasNext()) {
			BaseIcon back = ((Request) r.next()).backIcon;
			if (back != null && back.isAttached())
				back.onClick(back);
		}
		requests.clear();
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			CartListener cl = (CartListener) iter.next();
			cl.cartCleared();
		}
	}

	public boolean contains(Request r) {
		return requests.contains(r);
	}

	public void addListener(CartListener listener) {
		listeners.add(listener);
	}

}
