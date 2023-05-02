In general, if we deal with the IP address as a 32-bit number, and we have a subnet mask of x, then the subnet mask theoretically can take values between [0, 32]. Then, we have two things to estimate, what are the number of possible addresses with the given subnet mask, and what is the starting address.

We can get the number of possible addresses easily by the following formula 2^{(32 - x)}. That means if we have a subnet mask of 0, then the possible addresses are 2^{32}. And if we have a subnet mask of 32, then the possible number of addresses is 2^0 = 1, which means the given address is the only possible address in this case. One more example the case of subnet mask of /24, we will have 2^{(32 - 24)} = 2^{8} = 256 addresses.

Then, we need to interpret this subnet mask in the octets form. If we have a subnetmask of /24, we are actually having a 32-bit number that has the left most 24-bits as ones and the rest are zeros:



Class address ranges:
Class A = 1.0.0.0 to 126.0.0.0
Class B = 128.0.0.0 to 191.255.0.0
Class C = 192.0.1.0 to 223.255.255.0
Reserved address ranges for private (non-routed) use:
10.0.0.0 -> 10.255.255.255
172.16.0.0 -> 172.31.255.255
192.168.0.0 -> 192.168.255.255
Other reserved addresses:
127.0.0.0 is reserved for loopback and IPC on the local host
224.0.0.0 -> 239.255.255.255 is reserved for multicast addresses
Chart notes:
Number of Subnets - "( )" Refers to the number of effective subnets, since the use of subnet numbers of all 0s or all 1s is highly frowned upon and RFC non-compliant.
Number of Hosts - Refers to the number of effective hosts, excluding the network and broadcast address.


Class A
Network Bits	Subnet Mask	Number of Subnets	Number of Hosts
/8	255.0.0.0	0	16777214
/9	255.128.0.0	2 (0)	8388606
/10	255.192.0.0	4 (2)	4194302
/11	255.224.0.0	8 (6)	2097150
/12	255.240.0.0	16 (14)	1048574
/13	255.248.0.0	32 (30)	524286
/14	255.252.0.0	64 (62)	262142
/15	255.254.0.0	128 (126)	131070
/16	255.255.0.0	256 (254)	65534
/17	255.255.128.0	512 (510)	32766
/18	255.255.192.0	1024 (1022)	16382
/19	255.255.224.0	2048 (2046)	8190
/20	255.255.240.0	4096 (4094)	4094
/21	255.255.248.0	8192 (8190)	2046
/22	255.255.252.0	16384 (16382)	1022
/23	255.255.254.0	32768 (32766)	510
/24	255.255.255.0	65536 (65534)	254
/25	255.255.255.128	131072 (131070)	126
/26	255.255.255.192	262144 (262142)	62
/27	255.255.255.224	524288 (524286)	30
/28	255.255.255.240	1048576 (1048574)	14
/29	255.255.255.248	2097152 (2097150)	6
/30	255.255.255.252	4194304 (4194302)	2
 

Class B
Network Bits	Subnet Mask	Number of Subnets	Number of Hosts
/16	255.255.0.0	0	65534
/17	255.255.128.0	2 (0)	32766
/18	255.255.192.0	4 (2)	16382
/19	255.255.224.0	8 (6)	8190
/20	255.255.240.0	16 (14)	4094
/21	255.255.248.0	32 (30)	2046
/22	255.255.252.0	64 (62)	1022
/23	255.255.254.0	128 (126)	510
/24	255.255.255.0	256 (254)	254
/25	255.255.255.128	512 (510)	126
/26	255.255.255.192	1024 (1022)	62
/27	255.255.255.224	2048 (2046)	30
/28	255.255.255.240	4096 (4094)	14
/29	255.255.255.248	8192 (8190)	6
/30	255.255.255.252	16384 (16382)	2
 

Class C
Network Bits	Subnet Mask	Number of Subnets	Number of Hosts
/24	255.255.255.0	0	254
/25	255.255.255.128	2 (0)	126
/26	255.255.255.192	4 (2)	62
/27	255.255.255.224	8 (6)	30
/28	255.255.255.240	16 (14)	14
/29	255.255.255.248	32 (30)	6
/30	255.255.255.252	64 (62)	2