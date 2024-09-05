import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;


@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
            products.add(productToOrder);
        }
        Order newOrder = new Order(idService.generateId(), products, ZonedDateTime.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getAllOrdersWithStatus(OrderStatus status) {
        return orderRepo.getOrders().stream()
                .filter(ord -> ord.status() == status)
                .toList();
    }

    public void updateOrder(String orderId, OrderStatus status) {
        Order toUpdate = Optional.ofNullable(orderRepo.getOrderById(orderId)).orElseThrow(() -> new OrderNotFoundException(orderId));
        orderRepo.removeOrder(orderId);
        orderRepo.addOrder(toUpdate.withStatus(status));
    }

    private Order getOldestOrderWithStatus(OrderStatus status) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == status)
                .min((a, b) -> Math.toIntExact(ChronoUnit.SECONDS.between(a.timestamp(), b.timestamp())))
                .orElse(null);
    }

    public Map<OrderStatus, Order> getOldestOrderByOrderStatus() {
        Map<OrderStatus, Order> map = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            map.put(status, getOldestOrderWithStatus(status));
        }
        return map;
    }

    public void processTransactions(String filepath) {
        Map<String, String> nameToId = new HashMap<>();
        try (Stream<String> lines = Files.lines(Path.of(filepath))) {
            lines.forEach(transaction -> this.handleTransaction(transaction, nameToId));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void handleTransaction(String transaction, Map<String, String> idMap) {
        String[] split = transaction.split(" ");
        switch (split[0]) {
            case "addOrder":
                List<String> productIds = Arrays.stream(split).skip(2).toList();
                String id = addOrder(productIds).id();
                idMap.put(split[1], id);
                break;
            case "setStatus":
                updateOrder(idMap.get(split[1]), OrderStatus.valueOf(split[2]));
                break;
            case "printOrders":
                orderRepo.getOrders().forEach(System.out::println);
                break;
            default: throw new IllegalArgumentException();
        }
    }

}
