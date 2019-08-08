package com.testinium.gauge.mapping;

public enum MapValue {

	ID("id"), CLASSNAME("className"), LINKTEXT("linkText"), CSSSELECTOR("cssSelector"), XPATH("xpath"), CONTAINS(
			"contains");

	private final String text;

	public String getText() {
		return text;
	}

	MapValue(String text) {
		this.text = text;
	}

}
