package com.allarphoto.ajaxclient.client;

import com.allarphoto.ajaxclient.client.beans.Request;

public interface CartListener {

	public void removed(Request p);

	public void added(Request p);

	public void cartCleared();

	public void redraw();

}
