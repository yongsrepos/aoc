from readers import get_lines


def get_priority(c: str) -> int:
    if c >= 'a' and c <= 'z':
        return ord(c) - ord('a') + 1
    else:
        return ord(c) - ord('A') + 27


def s1(ins: str) -> int:
    r = 0
    for line in get_lines(ins):
        firstpart, secondpart = line[:len(line)//2], line[len(line)//2:]
        first_chars = set([c for c in firstpart])
        second_chars = set([c for c in secondpart])
        repeated = first_chars.intersection(second_chars).pop()
        r += get_priority(repeated)
    return r


def get_group_type(group: list) -> str:
    a, b, c = group
    return set([i for i in a]).intersection(set([i for i in b])).intersection(set([i for i in c])).pop()


def s2(ins: str) -> int:
    lines = get_lines(ins)
    start = 0
    end = len(lines)
    r = 0
    for i in range(start, end, 3):
        group = lines[i:i+3]
        r += get_priority(get_group_type(group))
    return r


def sample():
    assert s1("d03.sample.in") == 157
    assert s2("d03.sample.in") == 70


if __name__ == "__main__":
    sample()
    print("s1=", s1("d03.in"))
    print("s2=", s2("d03.in"))
