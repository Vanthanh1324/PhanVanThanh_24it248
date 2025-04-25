package project;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class bai1 {
	public static void main(String[] args) {
		Product p1= new Product("robot","Toys",20);
		Product p2= new Product("dino","eletronist",30);
		Product p3= new Product("phone", "Toys", 35);
		
		Customer c1= new Customer("thanh",2);
		Customer c2= new Customer("Van",1);
		Customer c3= new Customer("Phan",3);
		
		Order o1=new Order(LocalDate.of(2021, 2, 10), LocalDate.of(2021, 2, 15), "delivered", c1, Arrays.asList(p1, p3));
		Order o2=new Order(LocalDate.of(2021, 3, 16), LocalDate.of(2021, 3, 25), "delivered", c2, Arrays.asList(p1, p2));
		Order o3=new Order(LocalDate.of(2021, 4, 2),LocalDate.of(2021, 4, 5),"delivered",c3,Arrays.asList(p2,p3));
		List<Order> orders=Arrays.asList(o1,o2,o3);
		
		 LocalDate start = LocalDate.of(2021, 2, 1);
	        LocalDate end = LocalDate.of(2021, 4, 1);
		
		 List<Order> filteredOrders = orders.stream()
	                .filter(o -> o.getCustomer().getTier() == 2)
	                .filter(o -> !o.getOrderDate().isBefore(start) && !o.getOrderDate().isAfter(end))
	                .toList();
		 filteredOrders.forEach(System.out::println);
		 List<Product> toys = filteredOrders.stream()
	                .flatMap(o -> o.getProduct().stream())
	                .filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
	                .map(p -> new Product(p.getName(), p.getCategory(),(p.getPrice() * 0.9)))
	                .distinct()
	                .toList();

	        System.out.println("Danh sách sản phẩm Toys đã giảm giá:");
	        toys.forEach(System.out::println);
	}
}	
class Customer{
	int tier;
	String name;
	public int getTier() {
		return tier;
	}
	public String getName() {
		return name;
	}
	public Customer(String name,int tier) {
		this.name=name;
		this.tier=tier;
	}
}
class Product{
	private String name;
	private String category;
	private double price;
	public Product(String name, String category, double price) {
		this.category=category;
		this.name=name;
		this.price=price;
	}
	public double getPrice() {
		return price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public String toString(){
		return "("+ name +","+category+","+price+")";
	}
	   @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Product)) return false;
	        Product p = (Product) o;
	        return name.equals(p.name) && category.equals(p.category);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(name, category);
	    }
	}
class Order{
	LocalDate orderDate;
	LocalDate deliveryDate;
	String status;
	Customer customer;
	List<Product> product;
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<Product> getProduct() {
		return product;
	}
	public void setProduct(List<Product> product) {
		this.product = product;
	}
	public Order(LocalDate orderDate,LocalDate deliveryDate,String status,Customer customer,List<Product> product) {
		this.orderDate=orderDate;
		this.deliveryDate=deliveryDate;
		this.status=status;
		this.customer=customer;
		this.product=product;
	}
	 public String toString() {
	        return "Order{" +
	               "orderDate=" + orderDate +
	               ", deliveryDate=" + deliveryDate +
	               ", status='" + status + '\'' +
	               ", customer=" + customer.getName() +
	               ", tier=" + customer.getTier() +
	               ", products=" + product +
	               '}';
	    }
}
