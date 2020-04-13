package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.model.Product;
import demo.service.AdsService;
import demo.service.ProductService;
import demo.service.RecommendationService;

@Controller
public class ProductController {

  @Autowired private ProductService productService;
  @Autowired private RecommendationService recommendationService;
  @Autowired private AdsService adsService;

  @RequestMapping("/product/{id}")
  public String getProductById(@PathVariable String id, Model model) throws Exception {

    Product prod = productService.findProductById(id).get();

    model.addAttribute("prod", prod);
    model.addAttribute("recommend", recommendationService.getrecommendedProducts((Product) prod));
    model.addAttribute("ad", adsService.getrecommendedAd((Product) prod));

    return "product";
  }
}
