package demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.model.Product;

@Service
public class RecommendationService {

  @Autowired private ProductService productService;

  public List<Product> getrecommendedProducts(Product product) {
    List<Product> recommendedProducts =
        productService.findProductsByCategory(product.getCategories());
    recommendedProducts.remove((Product) product);
    return recommendedProducts;
  }
}
