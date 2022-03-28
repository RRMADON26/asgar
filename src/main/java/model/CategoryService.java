package model;

import java.math.BigDecimal;

public class CategoryService extends Base {
	private BigDecimal price;

	public CategoryService() {
	}

	public CategoryService(int id, String code, String name, BigDecimal price) {
		super(id, code, name);
		this.price = price;
	}

	@Override
	public String toString() {
		return this.getName() + " - $" + this.price;
	}
}
