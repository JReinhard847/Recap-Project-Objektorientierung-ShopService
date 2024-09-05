public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product mit der Id: " + id + " konnte nicht bestellt werden!");
    }
}
