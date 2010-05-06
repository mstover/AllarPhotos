/***********************************************************************************************************************
 * Copyright (C) 1999 Michael Stover This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA. Michael Stover can be reached via email at mstover1@rochester.rr.com or via snail mail at 130 Corwin
 * Rd. Rochester, NY 14610 The following exception to this license exists for Lazer Incorporated: Lazer Inc is excepted
 * from all limitations and requirements stipulated in the GPL. Lazer Inc. is the only entity allowed this limitation.
 * Lazer does have the right to sell this exception, if they choose, but they cannot grant additional exceptions to any
 * other entities.
 **********************************************************************************************************************/

package com.lazerinc.utils;

import java.util.Vector;

/*******************************************************************************
 * The Tree class holds a node that contains an object, an int value, an int
 * value of its parent, and a vector holding all its subnodes (children). When
 * new nodes are added, the Tree is searched to find the correct place to put
 * the node - to find if the node is a parent or child of a pre-existing node.
 * If not, then the node will be added as a child to the root node. Nodes can
 * also be added only if they are a child of a pre-existing node, or only if
 * they are a parent of a pre-existing node. In all cases, the Tree keeps itself
 * up-to-date, making sure all children and parents are properly organized
 * within it.
 * 
 * @author Michael Stover
 * @version 1.0 10/14/1998
 ******************************************************************************/
public class Tree {

	Object mainData;

	int ID, parentID;

	Vector children;

	Tree parent;

	int pointer;

	/***************************************************************************
	 * Blank constructor for Tree class.
	 **************************************************************************/
	public Tree() {
		children = new Vector();
		pointer = -1;
	} // End Method

	/***************************************************************************
	 * Returns the OrderedObject value for this tree node - the Parent ID
	 * 
	 * @return Parent ID
	 **************************************************************************/
	public int getValue() {
		return parentID;
	}

	/***************************************************************************
	 * Constructor for Tree. Parent node is set to null, parent ID is set to -1.
	 * 
	 * @param d
	 *            Object representing main data for the node.
	 * @param v
	 *            ID of node.
	 **************************************************************************/
	public Tree(Object d, int v) {
		mainData = d;
		ID = v;
		children = new Vector();
		parent = null;
		parentID = -1;
		pointer = -1;
	} // End Method

	/***************************************************************************
	 * Constructor for Tree.
	 * 
	 * @param d
	 *            Object representing main data for the node.
	 * @param v
	 *            ID of node.
	 * @param p
	 *            Parent node.
	 * @param pID
	 *            ID of parent node.
	 **************************************************************************/
	public Tree(Object d, int v, int pID) {
		mainData = d;
		ID = v;
		children = new Vector();
		parent = null;
		parentID = pID;
		pointer = -1;
	} // End Method

	/***************************************************************************
	 * Constructor for Tree.
	 * 
	 * @param d
	 *            Object representing main data for the node.
	 * @param v
	 *            ID of node.
	 * @param p
	 *            Parent node.
	 * @param pID
	 *            ID of parent node.
	 **************************************************************************/
	public Tree(Object d, int v, Tree p, int pID) {
		mainData = d;
		ID = v;
		children = new Vector();
		parent = p;
		parentID = pID;
		pointer = -1;
	} // End Method

	/***************************************************************************
	 * Resets the pointer to before the first child node.
	 **************************************************************************/
	public void reset() {
		pointer = -1;
	} // End Method

	/***************************************************************************
	 * Moves the pointer to the next child of this node. Returns true if there
	 * is a next child, false otherwise.
	 * 
	 * @return true if there is a next child, false otherwise.
	 **************************************************************************/
	public boolean next() {
		pointer++;
		if (pointer < children.size())
			return true;
		else
			return false;
	} // End Method

	/***************************************************************************
	 * Gets the next child node.
	 * 
	 * @return next child node of the current node.
	 **************************************************************************/
	public Tree getChild() {
		return (Tree) children.elementAt(pointer);
	} // End Method

	/***************************************************************************
	 * Gets the main data for this node.
	 * 
	 * @return The main data object for this node.
	 **************************************************************************/
	public Object getData() {
		return mainData;
	} // End Method

	/***************************************************************************
	 * Sets the main data for this node.
	 * 
	 * @param data
	 *            Object to set as node's main data.
	 **************************************************************************/
	public void setData(Object data) {
		mainData = data;
	}

	/***************************************************************************
	 * Gets the ID for this node.
	 * 
	 * @return Integer ID of node.
	 **************************************************************************/
	public int getID() {
		return ID;
	} // End Method

	/***************************************************************************
	 * Adds the node as a child of current node.
	 * 
	 * @param node
	 *            Tree object to be added.
	 **************************************************************************/
	public void add(Tree node) {
		children.addElement(node);
		node.setParent(this);
	} // End Method

	/***************************************************************************
	 * Returns the parent node.
	 * 
	 * @return parent node of current node. Note - could be null.
	 **************************************************************************/
	public Tree getParent() {
		return parent;
	} // End Method

	/***************************************************************************
	 * Sets the parent of the current node.
	 * 
	 * @param t
	 *            node to set parent to.
	 **************************************************************************/
	public void setParent(Tree t) {
		parent = t;
	} // End Method

	/***************************************************************************
	 * Returns the parent int ID.
	 * 
	 * @return parent int ID.
	 **************************************************************************/
	public int getParentID() {
		return parentID;
	} // End Method

	/***************************************************************************
	 * Removes the element from the list of children.
	 * 
	 * @param node
	 *            node to be removed.
	 **************************************************************************/
	public void remove(Tree node) {
		children.removeElement(node);
		if (pointer > -1)
			pointer--;
	} // End Method

	/***************************************************************************
	 * Adds a node to the tree by finding the parent and adding the new node as
	 * it's child. Or adds the new node as parent if it finds a child. If no
	 * children or parents are found, adds the node at the root level.
	 * 
	 * @param data
	 *            object to be added.
	 * @param v
	 *            ID of object.
	 * @param p
	 *            ID of parent.
	 * @return true or false depending on whether the parent had yet been found.
	 **************************************************************************/
	public boolean put(Object data, int v, int p) {
		boolean returnValue = false;
		if (ID == p) {
			returnValue = true;
			if (!checkChildren(v, p)) {
				Tree temp = new Tree(data, v, this, p);
				add(temp);
				redoTopChildren(temp);
			}

			else
				return true;
		} else if (parentID == v) {
			Tree par = this.getParent();
			Tree temp = new Tree(data, v, par, p);
			temp.add(this);
			par.remove(this);
			par.add(temp);
			redoTopChildren(temp);
			returnValue = true;
		} else if (ID == v && parentID == p)
			return true;
		else {
			int size = children.size();
			if (size > 0) {
				int count = 0;
				while (count < size) {
					returnValue = ((Tree) children.elementAt(count++)).putIt(
							data, v, p);
					if (returnValue)
						break;
				}
			}
		}
		if (!returnValue) {
			Tree temp = new Tree(data, v, this, p);
			add(temp);
			returnValue = true;
		}
		return returnValue;
	} // End Method

	/***************************************************************************
	 * Private method that adds a node to the tree by finding the parent and
	 * adding the new node as it's child, or finding a child and adding the new
	 * node as its parent.
	 * 
	 * @param data
	 *            object to be added.
	 * @param v
	 *            ID of object.
	 * @param p
	 *            ID of parent.
	 * @return true or false depending on whether the parent had yet been found.
	 **************************************************************************/
	private boolean putIt(Object data, int v, int p) {
		boolean returnValue = false;
		if (ID == p) {
			returnValue = true;
			if (!checkChildren(v, p)) {
				Tree temp = new Tree(data, v, this, p);
				add(temp);
				redoTopChildren(temp);
			} else
				return true;
		} else if (parentID == v) {
			Tree par = this.getParent();
			Tree temp = new Tree(data, v, par, p);
			temp.add(this);
			par.remove(this);
			par.add(temp);
			redoTopChildren(temp);
			returnValue = true;

		} else if (ID == v && parentID == p)
			return true;
		else {
			int size = children.size();
			if (size > 0) {
				int count = 0;
				while (count < size) {
					returnValue = ((Tree) children.elementAt(count++)).putIt(
							data, v, p);
					if (returnValue)
						break;
				}
			}
		}
		return returnValue;
	} // End Method

	/***************************************************************************
	 * Adds a node to the tree by finding the parent and adding the new node as
	 * it's child. Does not add the new node as parent if it finds a child. If
	 * no children or parents are found, the node is not added.
	 * 
	 * @param data
	 *            object to be added.
	 * @param v
	 *            ID of object.
	 * @param p
	 *            ID of parent.
	 * @return true or false depending on whether the parent had yet been found.
	 **************************************************************************/
	public boolean putIfChild(Object data, int v, int p) {
		boolean returnValue = false;
		if (ID == p) {
			returnValue = true;
			if (!checkChildren(v, p)) {
				Tree temp = new Tree(data, v, this, p);
				add(temp);
				redoTopChildren(temp);
			} else
				return true;
		} else if (ID == v && parentID == p)
			return true;
		else {
			int size = children.size();
			if (size > 0) {
				int count = 0;
				while (count < size) {
					returnValue = ((Tree) children.elementAt(count++))
							.putItIfChild(data, v, p);
					if (returnValue)
						break;
				}
			}

		}
		return returnValue;
	} // End Method

	/***************************************************************************
	 * Adds the new node as parent if it finds a child. Does not add a node to
	 * the tree if it finds the parent. If no children or parents are found, the
	 * node is not added.
	 * 
	 * @param data
	 *            object to be added.
	 * @param v
	 *            ID of object.
	 * @param p
	 *            ID of parent.
	 * @return true or false depending on whether the parent had yet been found.
	 **************************************************************************/
	public boolean putIfParent(Object data, int v, int p) {
		boolean returnValue = false;
		if (getParentID() == v) {
			Tree par = this.getParent();
			Tree temp = new Tree(data, v, par, p);
			temp.add(this);
			par.remove(this);
			par.add(temp);
			this.setParent(temp);
			redoTopChildren(temp);
			returnValue = true;
		} else if (ID == v && parentID == p)
			return true;
		else {
			int size = children.size();
			if (size > 0) {
				int count = 0;
				while (count < size) {
					returnValue = ((Tree) children.elementAt(count++))
							.putItIfParent(data, v, p);
					if (returnValue)
						break;
				}
			}
		}
		return returnValue;
	} // End Method

	/***************************************************************************
	 * Private method that adds a node to the tree by finding the parent and
	 * adding the new node as it's child. Does not add the node if such a
	 * pre-existing node is not found.
	 * 
	 * @param data
	 *            object to be added.
	 * @param v
	 *            ID of object.
	 * @param p
	 *            ID of parent.
	 * @return true or false depending on whether the parent had yet been found.
	 **************************************************************************/
	private boolean putItIfChild(Object data, int v, int p) {
		boolean returnValue = false;
		if (ID == p) {
			returnValue = true;
			if (!checkChildren(v, p)) {
				Tree temp = new Tree(data, v, this, p);
				add(temp);
				redoTopChildren(temp);
			} else
				return true;
		} else if (ID == v && parentID == p)
			return true;
		else {
			int size = children.size();
			if (size > 0) {
				int count = 0;
				while (count < size) {
					returnValue = ((Tree) children.elementAt(count++))
							.putItIfChild(data, v, p);
					if (returnValue)
						break;
				}
			}

		}
		return returnValue;
	} // End Method

	/***************************************************************************
	 * Adds a node to the tree by finding the child and adding the new node as
	 * it's parent. Does not add the node if such a pre-existing node is not
	 * found.
	 * 
	 * @param data
	 *            object to be added.
	 * @param v
	 *            ID of object.
	 * @param p
	 *            ID of parent.
	 * @return true or false depending on whether the parent had yet been found
	 **************************************************************************/
	private boolean putItIfParent(Object data, int v, int p) {
		boolean returnValue = false;
		if (getParentID() == v) {
			Tree par = this.getParent();
			Tree temp = new Tree(data, v, par, p);
			temp.add(this);
			par.remove(this);
			par.add(temp);
			this.setParent(temp);
			redoTopChildren(temp);
			returnValue = true;
		} else if (ID == v && parentID == p)
			return true;
		else {
			int size = children.size();
			if (size > 0) {
				int count = 0;
				while (count < size) {
					returnValue = ((Tree) children.elementAt(count++))
							.putItIfParent(data, v, p);
					if (returnValue)
						break;
				}
			}
		}
		return returnValue;
	} // End Method

	/***************************************************************************
	 * Look at all the children of the Top node and see if the current node is
	 * actually their parent. This keeps the Tree up-to-date as new nodes get
	 * added.
	 * 
	 * @param set
	 *            Potential new parent node
	 **************************************************************************/
	private void redoTopChildren(Tree set) {
		Tree temp, top = set.getParent();
		while ((temp = top.getParent()) != null)
			top = temp;
		top.reset();
		while (top.next()) {
			temp = top.getChild();
			if (temp.getParentID() == set.getID()) {
				top.remove(temp);
				set.add(temp);
				temp.setParent(set);
			}
		}
		top.reset();
	}

	/***************************************************************************
	 * Finds the tree node with the given ID value.
	 * 
	 * @param value
	 *            ID value searching for.
	 * @return Tree node with the given value.
	 **************************************************************************/
	public Tree findNode(int v) {
		Tree result = null;
		if (ID == v)
			result = this;
		else {
			reset();
			while (next()) {
				if (result == null)
					result = getChild().findNode(v);
				else
					break;
			}
		}
		return result;
	}

	/***************************************************************************
	 * Checks all the children of the current node for ID v and parent ID p
	 * 
	 * @param v
	 *            ID of node searching for.
	 * @param p
	 *            ID of parent node of node searching for.
	 * @return Returns true if one child of current node has ID v and parent ID
	 *         p. False otherwise.
	 **************************************************************************/
	public boolean checkChildren(int v, int p) {
		int x = 0;
		boolean result = false;
		while (x < children.size()
				&& !(((Tree) children.elementAt(x)).getID() == v && ((Tree) children
						.elementAt(x)).getParentID() == p))
			x++;
		if (x < children.size())
			result = true;
		return result;
	} // End Method

} // End Class
