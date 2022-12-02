from readers import  get_blocks


def get_elves(ins: str):
    return map(lambda block: sum(map(lambda line: int(line), block.split('\n'))),  get_blocks(ins))


def s1(ins: str): 
    return max(get_elves(ins))


def s2(ins: str):
    return sum(sorted(get_elves(ins))[-3:])


def run_samples():
    assert 24000 == s1("d01.sample.in")
    assert 45000 == s2("d01.sample.in")


if __name__ == "__main__":
    run_samples()
    print("s1=", s1("d01.in"))  # 71506
    print("s2=", s2("d01.in"))  # 209603
