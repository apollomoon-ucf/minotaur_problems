// Brian Moon
// COP4520
// PA2
// MinotaursBirthdayParty

import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MinotaursBirthdayParty extends Thread {
  // member variables
  public static int numGuests;
  private int currentGuestNumber;
  private AtomicBoolean hasEatenCupcake = new AtomicBoolean(false);
  // private AtomicBoolean selected = new AtomicBoolean(false);
  public static int numOfIterationsRequired = 0;
  public static AtomicInteger counter = new AtomicInteger(1);
  public static AtomicBoolean cupcake = new AtomicBoolean(true);
  public static ReentrantLock lock = new ReentrantLock();
  public static Set<Integer> set = new HashSet<Integer>();
  
  // static boolean[] hasEatenCupcake = new boolean[numGuests];
  static boolean[] isSelected = new boolean[100];

  MinotaursBirthdayParty(int guestNumber) {
    this.currentGuestNumber = guestNumber;
  }

  // run method
  public void run() {
    
    // this.hasEatenCupcake = false;
    while (counter.get() < numGuests) {
      tryToEnterLabyrinth();
    }
  }  

  public void tryToEnterLabyrinth() {
    if (isSelected[this.currentGuestNumber] == false) {
      isSelected[this.currentGuestNumber] = true;
      enterLabyrinth();
    }
  }
    
  // The guest enters Minotaur's Labyrinth
  public synchronized void enterLabyrinth() {
    lock.lock();
    try {
      // set.add(this.currentGuestNumber);
      if (this.currentGuestNumber == 0 && cupcake.get() == false) {
        counter.getAndIncrement();
        cupcake.set(true);
        System.out.println("The captain increments the count and requests a new yummy cupcake!"); 
      } else if (this.hasEatenCupcake.get() == false && cupcake.get() == true && this.currentGuestNumber != 0) {
        this.hasEatenCupcake.set(true);
        cupcake.set(false);
        System.out.println("Hungry Guest Number " + this.currentGuestNumber + " has eaten a cupcake!");
      }
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        lock.unlock();
    }
  }

  
  // main method
  public static void main(String[] args) {
    // start time
    long startTime = System.currentTimeMillis();
    // number of threads/guests
    numGuests = 100;
    Random random = new Random();
    MinotaursBirthdayParty[] minotaursGuests = new MinotaursBirthdayParty[numGuests];

    // create threads/guests
    for (int guestNumber = 0; guestNumber < numGuests; ++guestNumber) {
      minotaursGuests[guestNumber] = new MinotaursBirthdayParty(guestNumber);
      minotaursGuests[guestNumber].start();
    }
    // Minotaur picks random guests for labyrinth
    while (counter.get() < numGuests) {
      // System.out
      int randomGuest = random.nextInt(numGuests);
      
      // minotaursGuests[randomGuest].selected.set(true);
      isSelected[randomGuest] = false;
      // System.out.println("Guest Selected By Minotaur: " + randomGuest);
      // minotaursGuests[randomGuest].enterLabyrinth();
      // join threads/guests
      if (counter.get() == numGuests) {
        for (int guestNumber = 0; guestNumber < numGuests; ++guestNumber) {
          try {
            minotaursGuests[guestNumber].join();
            // System.out.println(guestNumber);
          } catch(Exception e) { e.printStackTrace(); }
        }
      }
      ++numOfIterationsRequired;
    }
    // System.out.println(numOfIterationsRequired + " iterations required for " + numGuests + " guests.");
    System.out.println("Guest Count: " + counter);
    long endTime = System.currentTimeMillis();
    System.out.println("Execution Time: " + (endTime - startTime) + "ms");
    // for (int item : set) {
    //   System.out.println(item);
    // }

  }
}