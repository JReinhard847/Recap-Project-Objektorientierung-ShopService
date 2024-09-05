import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = () -> UUID.randomUUID().toString();
        productRepo.addProduct(new Product("1","Banana"));
        productRepo.addProduct(new Product("2","Apple"));
        productRepo.addProduct(new Product("3","Pear"));
        ShopService service = new ShopService(productRepo, orderRepo, idService);
//        service.addOrder(List.of("1"));
//        service.addOrder(List.of("2","3"));
//        service.addOrder(List.of("1","2","3"));

        service.processTransactions("src/main/resources/transaction.txt");
    }
}
