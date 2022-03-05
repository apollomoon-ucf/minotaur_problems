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
  // Showroom operating hours (ms)
  public static final int openDuration = 3000;
  public static final Boolean AVAILABLE = true;

  // public member variables
  public static int numGuests = 100;
  public static Boolean isOpenForBusiness = true;
  public static Set<Integer> set = new HashSet<Integer>();  

  // private member variables
  private int limit = MIN_DELAY;
  private int currentGuestNumber;
  private static final int MIN_DELAY = 10;
  private static final int MAX_DELAY = 1000;
  private static AtomicBoolean readSignOnTheDoor = new AtomicBoolean(true);

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
  public void changeSignToBusy() {
    while (AVAILABLE) {
      while (!readSignOnTheDoor.get()) {};
      if (readSignOnTheDoor.getAndSet(BUSY)) {
        // System.out.println("Enter the room, change the sign to BUSY.");
        return; 
      } else {
        // System.out.println("Wait for a bit before looking at sign again..");
        backoff();
      }
    }
  }
  // Switch the sign to "Available" on the Crystal Vase Showroom door
  public void changeSignToAvailable() {
    readSignOnTheDoor.set(AVAILABLE);
    // System.out.println("Leave the room, change the sign to AVAILABLE.");
  }

  public boolean isCrystalVaseShowroomOpen() {
    return isOpenForBusiness;
  }

  // Guest shows up to the Minotaur's
  // crystal vase showroom and attempt to get in
  public void run() {
    while (isCrystalVaseShowroomOpen()) {
      lookAtSignOnCrystalVaseShowroomDoor();
    }
  }  

  // Guest sees the sign on the door says "Available"
  // and attempts to change the sign
  public void lookAtSignOnCrystalVaseShowroomDoor() {
    if (readSignOnTheDoor.get() == AVAILABLE) {
      tryToEnterCrystalVaseShowroom();
    }
  }
    
  // The guest trys to enter Minotaur's Crystal Vase Showroom
  public void tryToEnterCrystalVaseShowroom() {
    // Switch sign on the door to "Busy"
    this.changeSignToBusy();
    try {
      set.add(this.currentGuestNumber);
      // System.out.println("Guest " + currentGuestNumber + " in the Crystal Vase Showroom.");
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        // Switch sign on the door to "Available"
        this.changeSignToAvailable();
    }
  }
  
  // main method
  public static void main(String[] args) {
    long operatingHours = System.currentTimeMillis() + openDuration;
    // start time
    long startTime = System.currentTimeMillis();
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
      if (System.currentTimeMillis() <= operatingHours && isOpenForBusiness == true) {
        // alert the guests that the showroom is closing
        isOpenForBusiness = false;
      }
    }
    System.out.println("\nSTATUS -> Closing the showroom...\n");
    // join threads/guests
    for (int guestNumber = 0; guestNumber < numGuests; ++guestNumber) {
      try {
        minotaursGuests[guestNumber].join();
      } catch(Exception e) { e.printStackTrace(); }
    }
    
    System.out.println("Minotaur's Crystal Vase Showroom Is Now Closed For The Day :) \n-------------------------------------------------------------\n\nThanks For Stopping By!!\n");
    
    long endTime = System.currentTimeMillis();
    System.out.println("Total time: " + (endTime - startTime) + "ms");
    
    // Test set to ensure all guests made it through the labyrinth.
    // for (int item : set) {
    //   System.out.println(item);
    // }
  }
}