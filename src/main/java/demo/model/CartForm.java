package demo.model;

import java.io.Serializable;

/**
 * Capture the form value submitted while adding things to the cart
 * This is passed as ModelAttribute to the CartController
 */
public class CartForm implements Serializable {

	private static final long serialVersionUID = 6573246476319810641L;
	private String product_id;
	private String quantity;
	
	public String getProduct_id() {
		return product_id;
	}
	
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
	public String getQuantity() {
		return quantity;
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}