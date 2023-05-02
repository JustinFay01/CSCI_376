public class App {
    public static void main(String[] args) throws Exception {
        IPAddress ip = new IPAddress("192.168.0.10", "255.255.255.0");
        ip.printInfo();
    }
}
