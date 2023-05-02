public class App {
    public static void main(String[] args) throws Exception {
        IPAddress ip = new IPAddress("192.168.0.10", "255.255.255.0");
        ip.printInfo();

        printSeperator();
        ip = new IPAddress("209.140.209.140", "255.255.255.0");
        ip.printInfo();

        printSeperator();
        ip = new IPAddress("192.168.1.20", "255.255.240.0");
        ip.printInfo();

        printSeperator();
        ip = new IPAddress("24.1.1.34", "255.255.255.248");
        ip.printInfo();
    }

    public static void printSeperator(){
        for(int i = 0; i < 100; i++){
            System.out.print("-");
        }
        System.out.println();
    }
}
