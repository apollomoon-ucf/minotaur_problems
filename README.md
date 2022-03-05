# Minotaur Problems - COP4520 PA2

## Program Info

Programming Assignment 2
Minotaur Problems (Minotaur's Birthday Party and Minotaur's Crystal Vase)
Brian Moon
COP4520
UCF

## Instructions

Build Minotaur's Crystal Vase Program:

> javac MinotaursCrystalVase.java

Run Program And Output To File:

> java MinotaursCrystalVase > MinotaursCrystalVaseResults.txt

Build Minotaur's Birthday Party Program:

> javac MinotaursBirthdayParty.java

Run Program And Output To File:

> java MinotaursBirthdayParty > MinotaursBirthdayPartyResults.txt

## Minotaur's Birthday Party - Program Summary

Welcome to Minotaur's Birthday Party! This program simulates a birthday party when a Minotaur invites n number of guests to enter his labyrinth. At the exit of the labyrinth lies a cupcake, waiting to be devoured by a hungry guest. The Minotaur wants to know when all of his guests have made it through the labyrinth, so this program alerts the Minotaur when this has happened. Each guest is represented by a running thread, which gets randomly selected by the Minotaur to enter the labyrinth, until eventually all guests have made it through the labyrinth.

## Minotaur's Birthday Party - Program Evaluation

> Efficiency: Due to the random nature of the Minotaur selecting guests, the time taken for the program to complete will vary, ranging from ~3000ms up ~5000ms. I implemented a second solution to this program where all threads attempt to enter the door at once, which does show a speed up in time compared to having the Minotaur selecting randomly, but I felt that using a random number generator in this program more closely simulated the Birthday Party scenario. Due to locking, where guests have to wait for the previous guest to finish, this program does not have the speed increase we would see if we made a program that, for example, had several different rooms for guests to enter.

> Correctness: To ensure all of the guests were making it through the labyrinth, several tests were carried out. Initially, print statements allowed me to see when someone ate a cupcake and when the counter incremented the guest count, but since print statements can hide some issues with parallel programs, I inserted each guest into a set as they ate a cupcake to ensure that each person got a chance to eat a delicious cupcake. The results showed that every guest did indeed make it through the labyrinth.

## Minotaur's Crystal Vase - Program Summary

Are you excited to see Minotaur's Crystal Vase? Well Minotaur's guests are all lined up and ready to enter his showroom, but the Minotaur only wants one guest in the room at a time to avoid breaking his favorite crystal vase. This program simulates how we would deal with multiple people trying to enter at the same time. Each guest is represented by a running thread, which has a chance to enter the room. When a guest enters the room, they change the sign on the door to BUSY, and when they leave the room, they change the sign on the door to AVAILABLE. When a guest looks over at the sign and sees that it reads BUSY, they wander around the castle and then glance over again to check if the sign is now AVAILABLE.

## Minotaur's Crystal Vase - Program Evaluation

> Efficiency: This program will run for as long as the showroom is open which is set in the program with the constant: openDuration. To reduce several guests trying to change the sign to AVAILABLE or BUSY at the same time, there is a backoff mechanism that makes the guest roam the castle for a bit for before trying to read the sign again. Expontential backoff is used to achieve this.

> Correctness: To ensure the correctness of this program, I initially printed out a statement each time a guest entered the room and changed the sign to BUSY, while they were in the room, and once more when the guest left the room and changed the sign back to AVAILABLE for the next guest. While this worked to see that each guest was properly setting their sign, these print statement could also hide some underlying issues. So like in the previous problem, I added each guest to a set as they enter the Crystal Vase showroom and printed out the set at the end of the program. This allowed me to see that all guests were making it into the showroom when I opened the showroom for enough time.

## Advantages and Disadvantages of the Minotaur's Three Solutions

Below, I will discuss a few advantages and disadvantges of each approach:

> Approach 1: The Minotaur's first strategy was to allow all of the guests to try and enter the room at once, which may lead to large crowds of guests to gathering around the door. This approach has a big disadvantage due to the fact that some of the guests, especially in the back of the crowd, may never get a chance to enter the showroom and see Minotaur's Crystal Vase.

> Approach 2: The Minotaur's second strategy is having the guests change a sign on the showroom door to either BUSY or AVAILABLE depending on whether or not a guest was in the room For my program, I implemented this solution. And to deal with the potential problem of several guests trying to set the sign at the same time, I included a backoff function which will tell the guest to wait a bit before trying again. This increases the efficiency of the program in the sense that it increases the possibility of each guest having a chance to view the Minotaur's Crystal Vase, unlike approach 1. A disadvantage to this approach is the fact that if not enough time is given, some guests may miss out on seeing the vase unlike the queue approach which I will discuss next.

Approach 3: The Minotaur's third strategy involved creating a queue where each exiting guest was responsible for notifying the guest standing in front of the queue that the showroom is now available. An advantage to this strategy is it most closely resembles a real life scenario if every guest wanted to view the Crystal Vase in a fair way, because each guest that enters the queue would be guaranteed to enter the showroom, unlike with approach 1. Approach 2, with my backoff modification, is closer to the fairness of Approach 3 because it tell guests to wait a bit. But after experiementation and research, I believe the queue method would be the fairest, with the disadvantage being that with an array-based queue method, the array can get large as the number of guests increases.
