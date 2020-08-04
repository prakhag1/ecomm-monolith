package demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@DynamicUpdate
@Entity
@Table(name="Cart")
public class Cart implements Serializable{
	
	private static final long serialVersionUID = -5441856569010751960L;
		
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name="cart_id")
	private String id;
	
	@Column(name="total")
	private double totalPrice;
	
	@OneToMany(mappedBy="cart", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<CartItem> cartItems;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	
	public void setCartItems(List<CartItem> cartItem) {
		this.cartItems = cartItem;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	
	public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Cart() {
		super();
		this.cartItems = new ArrayList<>();
	}
}
