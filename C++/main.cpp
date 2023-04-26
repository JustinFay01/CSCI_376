// Your First C++ Program

#include <iostream>

int factRec(int x)
{
    if (x < 0)
        return -1;
    else if (x == 0)
        return 1;
    else if (x == 1)
        return x;
    else
        return x * factRec(x - 1);
}

int fact(int x)
{
    // Declare array of size 100 to store information
    int facts[100];
    // Loop through computing the factorial with the equation
    //  !x = Summation(frm x to 1)
    facts[0] = 1;
    facts[1] = 1;
    for (int i = 2; i <= x; i++)
    {
        facts[i] = i * facts[i - 1];
    }
    return facts[x];
}

int ptrFact(int x)
{
    
}

int main()
{
    std::cout << "Recursive Fact: " << factRec(6) << "\n";
    std::cout << "Iterative Fact: " << fact(6);
    return 0;
}