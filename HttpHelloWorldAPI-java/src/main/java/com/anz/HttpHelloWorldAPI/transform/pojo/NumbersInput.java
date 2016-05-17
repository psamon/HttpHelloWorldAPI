package com.anz.HttpHelloWorldAPI.transform.pojo;

import com.anz.common.transform.ITransformPojo;

/**
 * @author sanketsw
 *
 */
public class NumbersInput implements ITransformPojo {
	
	String text;
	
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param string the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
