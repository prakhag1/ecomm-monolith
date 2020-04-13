package demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.model.Product;

@Service
public class ProductService {

  @Autowired private List<Product> productList;

  public Optional<Product> findProductById(String id) {
    return productList.stream().filter(p -> p.getId().equals(id)).findAny();
  }

  public List<Product> findProductsByCategory(List<String> categories) {
    return productList
        .stream()
        .filter(prod -> categories.stream().anyMatch(cat -> prod.getCategories().contains(cat)))
        .collect(Collectors.toList());
  }
}
