package demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.model.Cart;
import demo.model.CartItem;
import demo.model.Product;

@Service
public class CartService {

  @Autowired private HttpSession session;
  @Autowired private ProductService productService;

  public Cart getCart() {
    return (Cart) Optional.ofNullable(session.getAttribute("cart")).orElseGet(Cart::new);
  }

  /** Empty cart and reset values in session */
  public Cart emptyCart() {
    Cart cart = new Cart();
    List<CartItem> items = new ArrayList<CartItem>();
    cart.setCartItems(items);
    return cart;
  }

  /**
   * Check if a product is already added to the cart
   *
   * @param id
   * @param cart
   */
  public Optional<CartItem> exists(String id, List<CartItem> cart) {
    return cart.stream().filter(c -> c.getProductId().equals(id)).findAny();
  }

  /**
   * Create list of products from the cart items. This is more of a workaround to cater to what's
   * expected to show up in the ftl templates
   *
   * @param session
   * @param cart
   */
  public List<Product> getSelectedProducts(HttpSession session, Cart cart) {
    List<Product> selectedProducts = new ArrayList<>();
    for (CartItem item : cart.getCartItems()) {
      selectedProducts.add(productService.findProductById(item.getProductId()).get());
    }
    return selectedProducts;
  }

  /**
   * Create new cart item based on the user selection
   *
   * @param selectedProduct
   * @param selectedQty
   * @param cart
   */
  public CartItem newCartItem(String selectedProduct, int selectedQty, Cart cart) {
    CartItem cartItem = new CartItem();
    cartItem.setProductId(selectedProduct);
    cartItem.setQuantity(selectedQty);
    cartItem.setCart(cart);
    return cartItem;
  }
}
