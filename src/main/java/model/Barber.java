package model;

public class Barber extends Base{
	public Barber(int id, String code, String name) {
		this.setId(id);
		this.setCode(code);
		this.setName(name);
	}

	@Override
	public String toString() {
		return getName();
	}
}
