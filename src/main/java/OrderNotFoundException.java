public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String id) {
        super("Es gibt keine Order mit der id "+id);
    }
}
