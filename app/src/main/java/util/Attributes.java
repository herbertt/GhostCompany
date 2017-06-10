package util;

import java.util.List;

public class Attributes {

	String name;
	String type;
	String value;

	public Attributes(String name, String type, String value) {
		super();
		this.setName(name);
		this.setType(type);
		this.setValue(value);
	}

	public Attributes() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
