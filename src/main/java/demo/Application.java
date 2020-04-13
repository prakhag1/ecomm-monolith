package demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import demo.model.Product;

@ComponentScan
@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootApplication
public class Application {

  private List<Product> productList;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void init() throws FileNotFoundException, IOException {
    InputStream stream = getClass().getClassLoader().getResourceAsStream("products.json");
    Reader reader = new InputStreamReader(stream, "UTF-8");
    setProductList(new Gson().fromJson(reader, new TypeToken<List<Product>>() {}.getType()));
  }

  @Bean
  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }
}
