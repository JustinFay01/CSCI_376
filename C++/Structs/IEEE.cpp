// C++ program to convert a real value
// to IEEE 754 floating point representation
#include <stdlib.h> // For malloc
#include <iostream> // For stdio out
using namespace std;

void printBinary(int n, int i)
{

    // Prints the binary representation
    // of a number n up to i-bits.
    int k;
    for (k = i - 1; k >= 0; k--)
    {

        if ((n >> k) & 1)
            cout << "1";
        else
            cout << "0";
    }
}

typedef union
{

    float f;
    struct
    {

        // Order is important.
        // Here the members of the union data structure
        // use the same memory (32 bits).
        // The ordering is taken
        // from the LSB to the MSB.
        unsigned int mantissa : 23;
        unsigned int exponent : 8;
        unsigned int sign : 1;

    } raw;
} myfloat;

// Function to convert real value
// to IEEE floating point representation
void printIEEE(myfloat var)
{

    // Prints the IEEE 754 representation
    // of a float value (32 bits)

    cout << var.raw.sign << " | ";
    printBinary(var.raw.exponent, 8);
    cout << " | ";
    printBinary(var.raw.mantissa, 23);
    cout << "\n";
}

// Driver Code
int main()
{

    // Instantiate the union
    while (true)
    {
        myfloat var;

        // Get the real value
        cout << "Enter number:" << endl;
        cin >> var.f;

        // Get the IEEE floating point representation
        cout << "IEEE 754 representation of " << var.f << " is: \n";
        printIEEE(var);
    }

    return 0;
}

// This code is contributed by shubhamsingh10