from enum import IntEnum
from readers import get_lines


class Shape(IntEnum):
    ROCK = 1,
    PAPER = 2,
    SCISSORS = 3


winning_rules = {Shape.ROCK: Shape.SCISSORS,
                 Shape.PAPER: Shape.ROCK, Shape.SCISSORS: Shape.PAPER}

losing_rules = {Shape.ROCK: Shape.PAPER,
                Shape.PAPER: Shape.SCISSORS, Shape.SCISSORS: Shape.ROCK}

shape_mappings = {'A': Shape.ROCK, 'B': Shape.PAPER, 'C': Shape.SCISSORS,
                  'X': Shape.ROCK, 'Y': Shape.PAPER, 'Z': Shape.SCISSORS}


def get_my_score_s1(raw_line: str) -> int:
    opponent, me = map(
        lambda split: shape_mappings[split], raw_line.split(' '))
    if opponent == me:
        return 3 + me
    elif winning_rules[opponent] == me:
        return me
    else:
        return 6 + me


def get_my_score_s2(raw_line: str) -> int:
    opponent_str, result = raw_line.split(' ')
    opponent = shape_mappings[opponent_str]

    if result == 'X':  # I lose
        return winning_rules[opponent]
    elif result == 'Y':  # draw
        return 3 + opponent
    else:  # I win
        return 6 + losing_rules[opponent]


def s1(ins: str) -> int:
    return sum(map(lambda line: get_my_score_s1(line), get_lines(ins)))

def s2(ins: str) -> int:
    return sum(map(lambda line: get_my_score_s2(line), get_lines(ins)))

def run_samples():
    assert s1("d02.sample.in") == 15
    assert s2("d02.sample.in") == 12


if __name__ == "__main__":
    run_samples()
    print("s1=", s1("d02.in"))
    print("s2=", s2("d02.in"))
