package org.xiangbalao.floatview.bean;

import java.util.Observable;

public class SafeMsg extends Observable {

	private int drawableId;
	private String msg;

	public int getDrawableId() {
		return drawableId;

	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
		setChanged();
		notifyObservers(this);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {

		this.msg = msg;
		setChanged();
		notifyObservers(this);
	}

}
