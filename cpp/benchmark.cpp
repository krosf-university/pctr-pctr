#include <fmt/format.h>
#include <chrono>
#include <thread>

class BenchMark {
  std::chrono::time_point<std::chrono::steady_clock, std::chrono::nanoseconds>
      start_, end_;
  std::chrono::duration<double, std::milli> elapsed_;

 public:
  BenchMark() = default;
  void start() { start_ = std::chrono::high_resolution_clock::now(); }
  void end() { end_ = std::chrono::high_resolution_clock::now(); }
  double elapsed() {
    elapsed_ = end_ - start_;
    return elapsed_.count();
  }
};

int main(int argc, char const *argv[]) {
  BenchMark b;
  b.start();
  std::this_thread::sleep_for(std::chrono::seconds(20));
  b.end();
  fmt::print("Elapsed time: {}ms\n", b.elapsed());
  return 0;
}
