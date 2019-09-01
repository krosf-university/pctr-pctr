#include <atomic>
#include <iostream>
#include <thread>
#include <vector>

struct Contador {
  Contador(int initial = 0) { valor.store(initial); }
  std::atomic<int> valor;
  void inc() { valor++; }
  void dec() { valor--; }
  int value() { return valor.load(); }
};

int main(int argc, char const *argv[]) {
  Contador c;
  std::vector<std::thread> hilos;
  for (size_t i = 0; i < 3; ++i) {
    hilos.push_back(std::thread([&c]() {
      for (size_t i = 0; i < 100; ++i, c.inc())
        ;
    }));
    hilos.push_back(std::thread([&c]() {
      for (size_t i = 0; i < 50; ++i, c.dec())
        ;
    }));
  }
  for (auto &hilo : hilos) {
    hilo.join();
  }
  std::cout << c.value() << std::endl;
}
