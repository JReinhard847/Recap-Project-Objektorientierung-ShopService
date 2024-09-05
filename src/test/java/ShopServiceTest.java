import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        IdService idService = () -> UUID.randomUUID().toString();
        ShopService shopService = new ShopService(new ProductRepo(),new OrderMapRepo(),idService);
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")));
        try {
            assertEquals(expected.products(), actual.products());
        } catch (ProductNotFoundException e) {
            fail();
        }

    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        IdService idService = () -> UUID.randomUUID().toString();
        ShopService shopService = new ShopService(new ProductRepo(),new OrderMapRepo(),idService);
        List<String> productsIds = List.of("1", "2");

        //WHEN

        //THEN
        assertThrows(ProductNotFoundException.class,() -> shopService.addOrder(productsIds));

    }
}
