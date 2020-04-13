package demo.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import demo.model.Cart;
import demo.model.CartForm;
import demo.model.CartItem;
import demo.service.CartService;

@Controller
public class CartController {

	@Autowired
	private HttpSession session;
	@Autowired
	private CartService cartService;
	
	/**
	 * Add a selected product to the cart
	 * Cart is saved in the session and persisted in database only at the time of checkout
	 * 
	 * @param formData
	 * @param model
	 */
	@RequestMapping(value = "/cart", method = RequestMethod.POST)
	public String addToCart(@ModelAttribute("cartForm") CartForm formData, Model model) {
		
		Cart cart = cartService.getCart();
		List<CartItem> cartItems = cart.getCartItems();
		
		String selectedProdId = formData.getProduct_id();
		int selectedQty = Integer.parseInt(formData.getQuantity());
		
		Optional<CartItem> item = cartService.exists(selectedProdId, cartItems);
		if(item.isPresent()) {
			selectedQty = item.get().getQuantity() + selectedQty;
			cartItems.remove(item.get());
		}
		
		cartItems.add(cartService.newCartItem(selectedProdId, selectedQty, cart));
		cart.setCartItems(cartItems);
		
		session.setAttribute("cart", cart);
		session.setAttribute("cart_size", cart.getCartItems().size());
		
		model.addAttribute("cart", cartService.getSelectedProducts(session, cart));
		return "cart";
	}
	
	/**
	 * Show cart items
	 * Certain values such as quantity etc. are not included in this implementation 
	 * 
	 * @param model
	 */
	@GetMapping(value = {"/showcart", "/cart"})
	public String showCart(Model model) {
		
		Cart cart = cartService.getCart();
		model.addAttribute("cart", cartService.getSelectedProducts(session, cart));
		return "cart";
	}
	
	/**
	 * Empty cart and reset values saved in the session scope
	 * 
	 * @param formData - capture the form value submitted while adding things to the cart
	 * @param model
	 */
	@RequestMapping(value = "/cart/empty", method = RequestMethod.POST)
	public String emptyCart(@ModelAttribute("cartForm") CartForm formData, Model model) {
		
		Cart cart = cartService.emptyCart();
		session.setAttribute("cart", cart);
		session.setAttribute("cart_size", cart.getCartItems().size());		
		return "redirect:/";
	}
}