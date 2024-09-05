import lombok.With;

import java.time.ZonedDateTime;
import java.util.List;

public record Order(
        String id,
        List<Product> products,
        @With OrderStatus status,
        ZonedDateTime timestamp
) {
    public Order(String id, List<Product> products){
        this(id,products,OrderStatus.PROCESSING,null);
    }

    public Order(String id, List<Product> products, ZonedDateTime timestamp){
        this(id,products,OrderStatus.PROCESSING,timestamp);
    }
}
