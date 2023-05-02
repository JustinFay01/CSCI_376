import java.util.Arrays;

public class IPAddress {

    private String address;
    private String subnet;

    private int[] addressArray;
    private int[] subnetArray;
    private int[] ipStartRange;
    private int[] ipEndRange;

    public IPAddress(String address, String subnet) {
        this.address = address;
        this.subnet = subnet;

        addressArray = addressToArray(address);
        subnetArray = addressToArray(subnet);

        ipStartRange = getIPRangeStart();
        ipEndRange = getIPRangeEnd();
    }

    public void printInfo() {
        System.out.println("Ip address is: " + Arrays.toString(addressArray) + "\n"
                + "Subnet address is: " + Arrays.toString(subnetArray) + "\n"
                + "First IP address is: " + Arrays.toString(ipStartRange) + "\n"
                + "Last IP address is: " + Arrays.toString(ipEndRange) + "\n"
                + "Total number of usable IP's (with broad cast): " + totalIpAddresses());
    }

    public int totalIpAddresses(){
        int total = 0;
        for(int i = 0; i < ipStartRange.length; i++){
            total += Math.abs(ipEndRange[i] - ipStartRange[i]);
        }

        return total;
    }

    /**
     * To find the starting address in the following subnet mask, we simply do
     * binary “and” operation between the IP address and the subnet mask:
     * 
     * @return int array of the first valid ip
     */
    public int[] getIPRangeStart() {
        int startArr[] = new int[addressArray.length];
        for (int i = 0; i < startArr.length; i++) {
            startArr[i] = addressArray[i] & subnetArray[i];
        }
        return startArr;
    }

    /**
     * Finally, we calculate the last IP address by applying the “or” operation on
     * it with the bitwise binary inverse of the subnet mask to the first IP
     * address:
     * 
     * @return int array of the last valid ip
     */
    public int[] getIPRangeEnd() {
        int endArr[] = new int[addressArray.length];
        for (int i = 0; i < endArr.length; i++) {
            endArr[i] = Byte.toUnsignedInt((byte) (addressArray[i] | (~subnetArray[i])));
        }
        return endArr;
    }

    public int[] addressToArray(String address) {
        String[] noDots = address.split("\\.");
        int addArr[] = new int[noDots.length];

        for (int i = 0; i < noDots.length; i++) {
            addArr[i] = Integer.valueOf(noDots[i]);
        }

        return addArr;
    }

}
