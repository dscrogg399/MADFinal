import random
import statistics


def runTrials (runs):

    randNums = []

    for x in range(3):
        for run in runs:
            a = random.uniform(-1, 1)
            randNums.append(a)

    average = (sum(randNums)/lend(randNums))
    dev = statistics.stdev(randNums)

    print(f"For {runs} runs, sample size 3: Average = {average}, Standard Deviation = {dev}")        


def main():
    runTrials(10)

main()

