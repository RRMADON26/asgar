package model;

public class Base {

	private int id;

	private String code;

	private String name;

	public Base(){}

	public Base(int id, String code, String name) {
		this.id  =  id;
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
