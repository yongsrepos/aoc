from pathlib import Path


def get_raw_content(filename: str):
    return Path("aoc2022/"+filename).read_text()


def get_blocks(filename: str):
    return get_raw_content(filename).split("\n\n")


def get_lines(filename: str):
    with open("aoc2022/"+filename) as f:
        return [line.rstrip() for line in f]
