#include <atomic>
#include <chrono>
#include <iostream>
#include <mutex>
#include <thread>
#include <vector>

void func_atomic() {
  std::atomic<int> variable;
  variable.store(0);
  std::vector<std::thread> hilos;

  for (size_t i = 0; i < 4; i++) {
    hilos.push_back(std::thread([&variable]() {
      std::cout << std::this_thread::get_id() << std::endl;
      for (size_t i = 0; i < 10000; i++, variable++)
        ;
    }));
  }
  for (auto &hilo : hilos) {
    hilo.join();
  }
  std::cout << variable.load() << std::endl;
}

void func_mutex() {
  std::mutex lock;
  std::vector<std::thread> hilos;
  int variable = 0;

  for (size_t i = 0; i < 4; i++) {
    hilos.push_back(std::thread([&variable, &lock]() {
      std::cout << std::this_thread::get_id() << std::endl;
      for (size_t i = 0; i < 10000; i++) {
        lock.lock();
        variable++;
        lock.unlock();
      }
    }));
  }
  for (auto &hilo : hilos) {
    hilo.join();
  }
  std::cout << variable << std::endl;
}

int main(int argc, char const *argv[]) {
  std::chrono::time_point<std::chrono::system_clock> start, end;
  start = std::chrono::system_clock::now();
  func_atomic();
  end = std::chrono::system_clock::now();

  std::chrono::duration<double> elapsed_seconds = end - start;
  std::time_t end_time = std::chrono::system_clock::to_time_t(end);
  std::cout << "Calculo terminado a las: " << std::ctime(&end_time)
            << "Tiempo: " << elapsed_seconds.count() << "s\n";

  start = std::chrono::system_clock::now();
  func_mutex();
  end = std::chrono::system_clock::now();

  elapsed_seconds = end - start;
  end_time = std::chrono::system_clock::to_time_t(end);
  std::cout << "Calculo terminado a las: " << std::ctime(&end_time)
            << "Tiempo: " << elapsed_seconds.count() << "s\n";

  return 0;
}
