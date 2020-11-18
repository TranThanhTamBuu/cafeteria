package source.Food;

public abstract class Food {
	protected String name, type;
	protected Float discount;
	protected Integer price;

	public Food(String name, String type, Float discount, Integer price) {
		this.name = name;
		this.type = type;
		this.discount = discount;
		this.price = price;
	}

	public Food modifyName(String name) {
		this.name = name;
		return this;
	}

	public Food modifyType(String type) {
		this.type = type;
		return this;
	}

	public Food modifyDiscount(Float discount) {
		this.discount = discount;
		return this;
	}

	public Food modifyPrice(Integer price) {
		this.price = price;
		return this;
	}

	public String getName() {
		return name;
	}

	public Float getDiscount() {
		return discount;
	}

	public Integer getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}

}
