/***********************************************************************************************************************
 * Copyright 1999 (c) Lazer Incorporated All code contained in this file is the property of Lazer Incorporated. All
 * rights reserved.
 **********************************************************************************************************************/

package com.allarphoto.application;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface CartObject extends Serializable {

	public Product getProduct();

	public Map<String, Set<String>> getInstructions();

	public int getQuantity();

}
