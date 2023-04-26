#include <iostream>

class Sorter
{

public:
    Sorter()
    {
    }

public:
    void sort(int *arr, int n)
    {
        for (int i = 0; i < n; i++)
        {
            int min_indx = i;
            // Find smalles element in remaining values
            for (int j = i + 1; j < n; j++)
            {
                if (*(arr + j) < *(arr + min_indx))
                {
                    min_indx = j;
                }
            }
            // Swap
            int temp = *(arr+min_indx);
            *(arr+min_indx) = *(arr+i);
            *(arr+i) = temp;
        }
    }
};

int main(int argc, char const *argv[])
{
    Sorter s;
    int arr[] = {64, 34, 25, 12, 22, 11, 90};
    int n = sizeof(arr)/sizeof(arr[0]);

    s.sort(arr, n);

    printf("Sorted array: ");
    for (int i=0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
    return 0;
}