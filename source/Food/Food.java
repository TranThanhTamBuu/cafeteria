package source.Food;

public abstract class Food implements Comparable<Food> {
	protected Integer id;
	protected String name, category, specific_type;
	protected Float discount;
	protected Integer price;

	public Food(Integer id, String name, String category, Float discount, Integer price) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.discount = discount;
		this.price = price;
	}

	public Food modifyName(String name) {
		this.name = name;
		return this;
	}

	public Food modifyCategory(String category) {
		this.category = category;
		return this;
	}

	public Food modifySpecification(String specific_type) {
		this.specific_type = specific_type;
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

	public String getCategory() {
		return category;
	}

	public String getSpecificType() {
		return this.specific_type;
	}

	public Long cost() {
		return (long) ((long) price - (price * discount));
	}

	public int GetID() {
		return this.id;
	}

	@Override
	public int compareTo(Food o) {
		if (this.id < o.id)
			return -1;
		else if (this.id > o.id)
			return 1;
		return 0;
	}
}
