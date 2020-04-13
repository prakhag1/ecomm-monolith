package demo.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableListMultimap;

import demo.model.Ad;
import demo.model.Product;

@Service
public class AdsService {

  private static final ImmutableListMultimap<String, Ad> adsMap = createAdsMap();
  private static final Random random = new Random();

  public Ad getrecommendedAd(Product product) {
	  List<String> categories = product.getCategories();
	  List<Ad> ads = adsMap.get(categories.get(random.nextInt(categories.size())));
	  return ads.get(random.nextInt(ads.size()));
  }

  private static ImmutableListMultimap<String, Ad> createAdsMap() {
    Ad camera =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/2ZYFJ3GM2N")
            .setText("Film camera for sale. 50% off.")
            .build();
    Ad lens =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/66VCHSJNUP")
            .setText("Vintage camera lens for sale. 20% off.")
            .build();
    Ad recordPlayer =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/0PUK6V6EV0")
            .setText("Vintage record player for sale. 30% off.")
            .build();
    Ad bike =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/9SIQT8TOJO")
            .setText("City Bike for sale. 10% off.")
            .build();
    Ad baristaKit =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/1YMWWN1N4O")
            .setText("Home Barista kitchen kit for sale. Buy one, get second kit for free.")
            .build();
    Ad airPlant =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/6E92ZMYYFZ")
            .setText("Air plants for sale. Buy two, get third one for free.")
            .build();
    Ad terrarium =
        new Ad.AdBuilder()
            .setRedirectUrl("/product/L9ECAV7KIM")
            .setText("Terrarium for sale. Buy one, get second one for free.")
            .build();

    return ImmutableListMultimap.<String, Ad>builder()
        .putAll("photography", camera, lens)
        .putAll("vintage", camera, lens, recordPlayer)
        .put("cycling", bike)
        .put("cookware", baristaKit)
        .putAll("gardening", airPlant, terrarium)
        .build();
  }
}
