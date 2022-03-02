# Minotaur Problems - COP4520 PA2

## Program Info

Programming Assignment 2
Minotaur Problems (Minotaur's Birthday Party and Minotaur's Crystal Vase)
Brian Moon
COP4520
UCF

## Instructions

Build Program: javac MinotaursBirthdayParty.java

Run Program And Output To File: java MinotaursBirthdayParty > primes.txt

## Program Summary

This program finds all prime numbers between 1 and 10^8. Once the prime numbers have been found, the program calculates the number of primes found, the sum of the primes, as well as the the top ten maximum primes, listed in ascending order.

## Program Evaluation

The initial naive approach, was to determine if each number was prime within the given range. This produced results that took way too long for the task we are trying to accomplish. Therefore, a sieve method was implemented to reduce the time needed for identifying the prime numbers. With a single thread, the execution time was ~766ms, which is very good, but, we want to reduce this time by distrubuting the work across eight threads.

With 8 threads, the time was decreased to ~538ms, resulting in a program that is 1.44 times faster. If we use Amdahl's Law to determine the amount of the program that was sped up by using multithreading, or in other words, determine the amount of the program, we see that we get a result of 30.763%. This means that ~31% of the program is parallel. This could be further optimized with more research and refactoring.

> Efficiency: As mentioned in the previous paragraph, the Sieve method drastically reduced the time needed to find the prime numbers, even before implementing multiple threads. The Sieve method works by marking all numbers that are not prime, which are all multiples of numbers from 2 to the square root on n. Once this method was distributed among the threads, further speedups were accomplished.

> Correctness: The sieve is a well known method for finding primes, but to ensure the proper results were shipped to the boss, all output has been verified using external tools to ensure the correct numbers were found, the sum was correct, and the top ten primes were also correct before pushing this code. Therefore, the program has been tested, and the code has been pushed to GitHub.
# minotaur_problems
