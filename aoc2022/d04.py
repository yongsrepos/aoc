from readers import get_lines
from solution import Solutions2022


class D4(Solutions2022):
    def is_fully_contained(self, line: str) -> bool:
        a, b = line.split(',')
        a_start, a_end = a.split('-')
        b_start, b_end = b.split('-')
        return (int(a_start) >= int(b_start) and int(a_end) <= int(b_end)) or (int(a_start) <= int(b_start) and int(a_end) >= int(b_end))

    def s1(self, ins: str) -> int:
        return sum(map(lambda line: self.is_fully_contained(line), get_lines(ins)))

    def is_overlapping(self, line: str) -> bool:
        a, b = line.split(',')
        a_start, a_end = a.split('-')
        b_start, b_end = b.split('-')
        return (int(a_start) >= int(b_start) and int(a_start) <= int(b_end)) or (int(a_start) <= int(b_start) and int(a_end) >= int(b_start))

    def s2(self, ins: str) -> int:
        return sum(map(lambda line: self.is_overlapping(line), get_lines(ins)))


if __name__ == "__main__":
    D4(4).solve()
