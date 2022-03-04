// Brian Moon
// COP4520
// PA2 - Problem 2
// MinotaursCrystalVase.java

import java.util.Set;
import java.util.Random;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class MinotaursCrystalVase extends Thread {
  // constants
  public static final Boolean BUSY = false;
  public static final Boolean AVAILABLE = true;

  // public member variables
  public static int numGuests;
  public static Boolean isOpenForBusiness = true;
  public static Set<Integer> set = new HashSet<Integer>();  

  // private member variables
  private int limit = MIN_DELAY;
  private int currentGuestNumber;
  private static final int MIN_DELAY = 10;
  private static final int MAX_DELAY = 1000;
  private static AtomicBoolean isDoorUnlocked = new AtomicBoolean(true);

  // constructor for setting the guest's number
  MinotaursCrystalVase(int guestNumber) {
    this.currentGuestNumber = guestNumber;
  }    
  
  // backoff tells guests to look
  // at the Available or Busy sign again
  // after waiting for a bit, this reduces
  // crowds forming waiting for sign to change
  public void backoff() {
    Random random = new Random();
    try {
      int delay = random.nextInt(limit); 
      limit = Math.min(MAX_DELAY, 2 * limit); 
      Thread.sleep(delay);
    } catch (Exception e) {
      System.out.println(e);
    }
  } 

  // Switch the sign to "Busy" on the Crystal Vase Showroom door
  public void locker() {
    // System.out.println(this.currentGuestNumber);
    while (AVAILABLE) {
      // System.out.println("Door is now unlocked");
      while (!isDoorUnlocked.get()) {
      };
      // System.out.println(isDoorUnlocked);
      if (isDoorUnlocked.getAndSet(BUSY)) {
        // System.out.println("Door is now locked");
        return; 
      } else {
        // System.out.println("Backoff bruh");
        backoff();
      }
    }
  }
  // Switch the sign to "Available" on the Crystal Vase Showroom door
  public void unlocker() {
    isDoorUnlocked.set(AVAILABLE);
    // System.out.println("Door is now unlocked");
  }

  public boolean isCrystalVaseShowroomOpen() {
    return isOpenForBusiness;
  }

  // Guest shows up to the Minotaur's
  // crystal vase showroom and attempt to get in
  public void run() {
    while (isCrystalVaseShowroomOpen()) {
      tryToEnterCrystalVaseShowroom();
    }
  }  

  // Guest sees the sign on the door says "Available"
  // and attempts to reach for the door
  public void tryToEnterCrystalVaseShowroom() {
    if (isDoorUnlocked.get() == AVAILABLE) {
      enterCrystalVaseShowroom();
    }
  }
    
  // The guest enters Minotaur's Crystal Vase Showroom
  public void enterCrystalVaseShowroom() {
    // Switch sign on the door to "Busy"
    this.locker();
    try {
      // set.add(this.currentGuestNumber);
      // System.out.println("Guest in showroom: " + currentGuestNumber);
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        // Switch sign on the door to "Available"
        this.unlocker();
    }
  }
  
  // main method
  public static void main(String[] args) {
    // operating hours
    int openDuration = 3000;
    int timeNeededForClosing = 1000;
    long operatingHours = System.currentTimeMillis() + openDuration + timeNeededForClosing;
    // start time
    long startTime = System.currentTimeMillis();
    // number of threads/guests
    numGuests = 100;
    MinotaursCrystalVase[] minotaursGuests = new MinotaursCrystalVase[numGuests];
    System.out.println("\nMinotaur's Crystal Vase Showroom Is Now Open For Business!!!!");
    System.out.println("-------------------------------------------------------------\n");
    // create threads/guests
    for (int guestNumber = 0; guestNumber < numGuests; ++guestNumber) {
      minotaursGuests[guestNumber] = new MinotaursCrystalVase(guestNumber);
      minotaursGuests[guestNumber].start();
    }

    System.out.println("STATUS -> Guests are roaming the castle and the Crystal Vase Showroom...");

    // While the Crystal Vase Showroom is open for business,
    // let guests in
    while (System.currentTimeMillis() < operatingHours) {
      // business is open
      if (operatingHours - System.currentTimeMillis() <= timeNeededForClosing && isOpenForBusiness == true) {
        System.out.println("\nSTATUS -> Closing the showroom...\n");
        isOpenForBusiness = false;
      }
    }
    // join threads/guests
    for (int guestNumber = 0; guestNumber < numGuests; ++guestNumber) {
      try {
        minotaursGuests[guestNumber].join();
      } catch(Exception e) { e.printStackTrace(); }
    }
    
    
    System.out.println("Minotaur's Crystal Vase Showroom Is Now Closed For The Day :) \n-------------------------------------------------------------\n\nThanks For Stopping By!!\n");
    // System.out.println("------------------------------------------------------------\n");
    
    long endTime = System.currentTimeMillis();
    System.out.println("Total time open for business: " + (endTime - startTime) + "ms");
    
    // Test set to ensure all guests made it through the labyrinth.
    // for (int item : set) {
    //   System.out.println(item);
    // }
  }
}