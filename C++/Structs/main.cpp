#include "Person.h" // Need quotes to compile
#include <stdlib.h> // For malloc
#include <iostream> // For stdio out

int main(int argc, char const *argv[])
{
    struct Person *mike;

    mike = (struct Person *) malloc(sizeof(struct Person));
    mike->age = 63;
    mike->name = "mike";

    std::cout << mike->name;

    return 0;
}
