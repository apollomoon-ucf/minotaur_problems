// Brian Moon
// COP4520
// PA2 - Problem 1
// MinotaursBirthdayParty.java

import java.util.Set;
import java.util.Random;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MinotaursBirthdayParty extends Thread {
  // public member variables
  public static int numGuests;
  public static int numOfIterationsRequired = 0;
  public static ReentrantLock lock = new ReentrantLock();
  public static Set<Integer> set = new HashSet<Integer>();
  public static AtomicInteger counter = new AtomicInteger(1);  
  public static AtomicBoolean cupcake = new AtomicBoolean(true);

  // private member variables
  private int currentGuestNumber;
  private AtomicBoolean selected = new AtomicBoolean(false);
  private AtomicBoolean hasEatenCupcake = new AtomicBoolean(false);

  // constructor for setting the guest's number
  MinotaursBirthdayParty(int guestNumber) {
    this.currentGuestNumber = guestNumber;
  }

  // Guest shows up to the Minotaur's birthday party
  // and attempts to get in the Labyrinth.
  public void run() {
    while (counter.get() < numGuests) {
      tryToEnterLabyrinth();
    }
  }  

  // Guest is selected to enter Minotaur's labyrinth.
  public void tryToEnterLabyrinth() {
    if (this.selected.get() == true) {
      this.selected.set(false);
      enterLabyrinth();
    }
  }
    
  // The guest enters Minotaur's Labyrinth
  public void enterLabyrinth() {
    lock.lock();
    try {
      // set.add(this.currentGuestNumber);
      if (this.currentGuestNumber == 0 && cupcake.get() == false) {
        counter.getAndIncrement();
        cupcake.set(true);
        // System.out.println("The captain increments the count and requests a new yummy cupcake!"); 
      } else if (this.hasEatenCupcake.get() == false && cupcake.get() == true && this.currentGuestNumber != 0) {
        this.hasEatenCupcake.set(true);
        cupcake.set(false);
        // System.out.println("Hungry Guest Number " + this.currentGuestNumber + " has eaten a cupcake!");
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
    System.out.println("Loading...");
    // Minotaur picks random guests for labyrinth
    while (counter.get() < numGuests) {
      int randomGuest = random.nextInt(numGuests);
      minotaursGuests[randomGuest].selected.set(true);
      
      // join threads/guests
      if (counter.get() == numGuests) {
        for (int guestNumber = 0; guestNumber < numGuests; ++guestNumber) {
          try {
            minotaursGuests[guestNumber].join();
          } catch(Exception e) { e.printStackTrace(); }
        }
      }
    }
    System.out.println("Loading Complete!");
    System.out.println("All " + counter + " guests have made it through the Minotaur's Labyrinth!");
    long endTime = System.currentTimeMillis();
    System.out.println("Execution Time: " + (endTime - startTime) + "ms");
    
    // Test set to ensure all guests made it through the labyrinth.
    // for (int item : set) {
    //   System.out.println(item);
    // 
  }
}