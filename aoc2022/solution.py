class Solutions2022():

    def __init__(self, day:int) -> None:
        self.ins = f"d{day:02d}.in"
        self.samples = f"d{day:02d}.sample.in"
        pass

    def s1(self, ins:str)->int:
        pass

    def s2(self, ins:str)->int:
        pass

    def sample(self):
        print("sample.s1", self.s1(self.samples))
        print("sample.s2", self.s2(self.samples))
    
    def solve(self):
        self.sample()
        print("s1", self.s1(self.ins))
        print("s2", self.s2(self.ins))
        