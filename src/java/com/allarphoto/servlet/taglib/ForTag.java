package com.lazerinc.servlet.taglib;

public class ForTag extends ListTag {
	private static final long serialVersionUID = 1;

	private int counter;

	private int max = Integer.MAX_VALUE;

	private int min = Integer.MIN_VALUE;

	private int incrValue = 1;

	public void setStart(int start) {
		counter = start;
	}

	protected void initialize() {
	}

	public void setIncrement(int inc) {
		incrValue = inc;
	}

	public void setIncrement(Integer inc) {
		if (inc != null)
			incrValue = inc.intValue();
		else
			incrValue = 1;
	}

	public void setIncrement(String inc) {
		try {
			incrValue = Integer.parseInt(inc);
		} catch (NumberFormatException e) {
			incrValue = 1;
		}
	}

	public void setStart(Integer start) {
		if (start != null) {
			counter = start.intValue();
		} else {
			counter = 0;
		}
	}

	public void setStart(String start) {
		try {
			counter = Integer.parseInt(start);
		} catch (NumberFormatException e) {
			counter = 0;
		}
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMax(Integer max) {
		if (max != null) {
			this.max = max.intValue();
		} else {
			this.max = 0;
		}
	}

	/***************************************************************************
	 * Description of the Method
	 **************************************************************************/
	protected void assignCurrentValue() {
		currentValue = new Integer(counter + incrValue);
	}

	/***************************************************************************
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 **************************************************************************/
	protected boolean hasNext() {
		return (counter + incrValue) < max && (counter + incrValue) > min;
	}

	public void setMax(String max) {
		try {
			this.max = Integer.parseInt(max);
		} catch (NumberFormatException e) {
			this.max = 0;
		}
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setMin(Integer min) {
		if (min != null) {
			this.min = min.intValue();
		} else {
			this.min = 0;
		}
	}

	public void setMin(String min) {
		try {
			this.min = Integer.parseInt(min);
		} catch (NumberFormatException e) {
			this.min = 0;
		}
	}
}