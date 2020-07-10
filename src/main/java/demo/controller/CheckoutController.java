package demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import demo.model.Cart;
import demo.service.CartService;
import demo.service.OrderService;
import demo.service.PaymentService;

@Controller
public class CheckoutController {

	@Autowired
	private HttpSession session;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartService cartService;
	@Autowired
	private PaymentService paymentService;
		
	/**
	 * Checkout calls OrderService downstream which persists cart and cart items in a database
	 * The flow below does not take into account into any payment flow. 
	 * That is introduced in checkout v2 which is implemented through microservices. 
	 * See demo.microservices.async.checkout for details.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cart/checkout", method = RequestMethod.POST)
	public String checkout(Model model) throws Exception {
		
		Cart cart = cartService.getCart();		
		// Process payment
		paymentService.makePayment();
		// Save order
		String orderId = orderService.addCustomerOrder(cart);
		cart = cartService.emptyCart();
		session.setAttribute("cart", cart);
		session.setAttribute("cart_size", cart.getCartItems().size());		

		model.addAttribute("orderId", orderId);
		return "order";
	}
}
