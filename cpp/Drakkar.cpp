#include <fmt/color.h>
#include <fmt/format.h>
#include <condition_variable>
#include <mutex>
#include <thread>
#include <vector>

class Marmita {
  inline static int quantity{0};
  std::mutex mutex;
  std::condition_variable cv;

 public:
  Marmita() = default;
  void cook() {
    std::unique_lock<std::mutex> lck(mutex);
    quantity += 10;
    fmt::print(fmt::fg(fmt::color::green), "Cooking: {}\n", quantity);
    cv.notify_all();
    while (quantity > 0) {
      cv.wait(lck);
    }
  }
  void eat() {
    std::unique_lock<std::mutex> lck(mutex);
    if (quantity > 0) {
      fmt::print(fmt::fg(fmt::color::crimson), "Eating: {}\n", quantity);
      --quantity;
    } else {
      cv.notify_one();
      cv.wait(lck);
    }
  }
};

void Drakkar(Marmita& m, int type) {
  while (true) {
    if (type == 0) {
      m.cook();
    } else {
      m.eat();
    }
  }
}

int main() {
  Marmita m;
  std::vector<std::thread> threads;
  int cores = std::thread::hardware_concurrency();
  for (size_t i = 0; i < cores; ++i) {
    threads.push_back(std::thread(Drakkar, std::ref(m), i));
  }

  for (auto& t : threads) {
    t.join();
  }
}
