#include <iostream>
#include <thread>

void funcion() { std::cout << "Hello!!" << std::endl; }

int main(int argc, char const *argv[]) {
  std::thread hilo(funcion);
  hilo.join();
}
