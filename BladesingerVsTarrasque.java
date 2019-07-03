/*
This program simulates a 1v1 encounter between a Bladesinger and a Tarrasque. In terms of HP, we use the average amount
per die. In terms of damage, we will use Math.random() to simulate the die roll.

To make our Bladesinger, we start with 1 level of Fighter, then 19 in Bladesinger. With the Warforged race, Envoy subrace,
and point buy, we get 8/16/16/16/8/8 starting stats. With 4 ASIs in Dex and Int, we get 8/20/16/20/8/8 stats. We take the
Dual Wielder Feat, Darkwood Core natural armor of the Warforged, get our hands on Bracers of Defense, Ring of Protection
and Cloak of Protection, and Mariner Fighting Style. We also take Shield and Misty Step as our signature spells. While
Bladesinging, with our reaction to Shield, we get an AC of 38. As a Tarrasque has a +19 to hit on all attacks, it needs a
19 or a 20 to hit us. If we cast Foresight on ourselves beforehand, the Tarrasque has disadvantage on all attacks.

We are also dual-wielding two +3 rapiers, for a +14 to hit. With the Tarrasque's AC at 25, we need at least an 11 to hit.
Again, with Foresight cast we have advantage on all attacks.
*/

import java.lang.Math;

public class BladesingerVsTarrasque {
   public static boolean isBitten = false;
   public static boolean isFrightened = false;
   
   public static void main(String[] args) {
      int wins = 0;
      for (int i = 0; i < 20; i++) {
         wins += runCombat();
      }
      System.out.print("The Bladesinger won " + wins + "/20 times!") ;
   }
   
   public static int runCombat() {
      int bInitiative = Math.max((int)(Math.random() * 20 + 6), (int)(Math.random() * 20 + 6));
      int tInitiative = (int)(Math.random() * 20 + 1);
      int rounds = 0;
      int tarrasqueHP = 676;
      int bladesingerHP = 146;
      if (bInitiative >= tInitiative) { // Bladesinger wins initiative ties due to higher Dex
         while (bladesingerHP > 0 && tarrasqueHP > 0) {
            tarrasqueHP -= bladesingerTurn(rounds);
            if (tarrasqueHP <= 0) {
               System.out.println("Combat completed in " + rounds + " rounds.");
               return 1;
            }
            bladesingerHP -= legendaryBite(rounds);
            if (bladesingerHP <= 0) {
               System.out.println("Combat completed in " + rounds + " rounds.");
               return 0;
            }
            bladesingerHP -= tarrasqueTurn(rounds);
            if (bladesingerHP <= 0) {
               System.out.println("Combat completed in " + rounds + " rounds.");
               return 0;
            }
            rounds++;
         }
      } else {
         while (bladesingerHP > 0 && tarrasqueHP > 0) {
            bladesingerHP -= tarrasqueTurn(rounds);
            if (bladesingerHP <= 0) {
               System.out.println("Combat completed in " + rounds + " rounds.");
               return 0;
            }
            tarrasqueHP -= bladesingerTurn(rounds);
            if (tarrasqueHP <= 0) {
               System.out.println("Combat completed in " + rounds + " rounds.");
               return 1;
            }
            bladesingerHP -= legendaryBite(rounds);
            if (bladesingerHP <= 0) {
               System.out.println("Combat completed in " + rounds + " rounds.");
               return 0;
            }
            rounds++;
         }
      }
      System.out.println("Combat completed in " + rounds + " rounds.");
      return 0;
   }
   
   public static int bladesingerTurn(int rounds) {
      boolean bonusActionUsed = false;
      if (isBitten) { // Misty Step out of the Bite to free from restrain
         bonusActionUsed = true;
         isBitten = false;
      } else if (rounds == 0 || rounds == 11) { //start or refresh Bladesinging
         bonusActionUsed = true;
      }
      int damage = 0;
      int firstAttack = 0;
      int secondAttack = 0;
      int thirdAttack = 0;
      if (isFrightened) { //frightened condition nullifies advantage from Foresight
         firstAttack = (int)(Math.random() * 20 + 1);
         secondAttack = (int)(Math.random() * 20 + 1);
         if (!bonusActionUsed) {
            thirdAttack = (int)(Math.random() * 20 + 1);
         }
         if ((rounds > 10) || (Math.random() * 20 >= 17)) { //frightening presence lasts 1 minute
            isFrightened = false;
         }
      } else {
         firstAttack = Math.max((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         secondAttack = Math.max((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         if (!bonusActionUsed) {
            thirdAttack = Math.max((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         }
      }
      if (firstAttack >= 11) {
         damage += (int)(Math.random() * 8) + 1;
         if (firstAttack == 20) {
            damage = damage * 2;
         }
         damage += 8;
         if (rounds < 21) { //checks if we're still Bladesinging
            damage += 5; //add our Int mod to damage if Bladesinging
         }
      }
      if (secondAttack >= 11) {
         damage += (int)(Math.random() * 8) + 1;
         if (secondAttack == 20) {
            damage = damage * 2;
         }
         damage += 8;
         if (rounds < 21) { //checks if we're still Bladesinging
            damage += 5; //add our Int mod to damage if Bladesinging
         }
      }
      if (thirdAttack >= 11) {
         damage += (int)(Math.random() * 8) + 1;
         if (thirdAttack == 20) {
            damage = damage * 2;
         }
         damage += 3; // don't add Dex mod on Bonus Action attack
         if (rounds < 21) { //checks if we're still Bladesinging
            damage += 5; //add our Int mod to damage if Bladesinging
         }
      }
      return damage;  
   }
   
   public static int legendaryBite(int rounds) {
      // tarrasque will always go for bite here. Since this comes directly after our turn, and we misty step if we're
      // bitten, we will never be already bitten/grappled here.
      int damage = 0;
      int attack = Math.min((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
      if (rounds > 20) { // we've run out of Bladesinging, so our AC is lower; now needs a 14 or higher to hit
         if (attack > 13) {
            isBitten = true;
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 12 + 1);
            }
            if (attack == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
      } else {
         if (attack > 18) {
            isBitten = true;
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 12 + 1);
            }
            if (attack == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
      }
      return damage;
   }
   
   public static int tarrasqueTurn(int rounds) {
      if (rounds == 0) { // Bladesinger immune to frightening presence if they succeed
         if (Math.max((int)(Math.random() * 20), (int)(Math.random() * 20)) < 17) {
            isFrightened = true;
         }
      }
      // Tarrasque will always go for Bite first, on chance of it hitting and negating disadvantage for all
      // other attacks
      int damage = 0;
      int bite = 0;
      int clawOne = 0;
      int clawTwo = 0;
      int horns = 0;
      int tail = 0;
      if (isBitten) {
         bite = (int)(Math.random() * 20 + 1);
      } else { // to determine if Bladesinger will be bitten for remainder of attacks
         bite = Math.min((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         if (rounds > 20) {
            if (bite > 13) {
               isBitten = true;
            }
         } else {
            if (bite > 18) {
               isBitten = true;
            }
         }
      }
      if (isBitten) {
         clawOne = (int)(Math.random() * 20 + 1);
         clawTwo = (int)(Math.random() * 20 + 1);
         horns = (int)(Math.random() * 20 + 1);
         tail = (int)(Math.random() * 20 + 1);
      } else {
         clawOne = Math.min((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         clawTwo = Math.min((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         horns = Math.min((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
         tail = Math.min((int)(Math.random() * 20 + 1), (int)(Math.random() * 20 + 1));
      }
      if (rounds > 20) { // no longer Bladesinging, so it needs a 14 or higher to hit
         if (bite > 13) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 12 + 1);
            }
            if (bite == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (clawOne > 13) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 8 + 1);
            }
            if (clawOne == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (clawTwo > 13) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 8 + 1);
            }
            if (clawTwo == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (horns > 13) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 10 + 1);
            }
            if (horns == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (tail > 13) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 6 + 1);
            }
            if (tail == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
      } else {
         if (bite > 18) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 12 + 1);
            }
            if (bite == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (clawOne > 18) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 8 + 1);
            }
            if (clawOne == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (clawTwo > 18) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 8 + 1);
            }
            if (clawTwo == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (horns > 18) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 10 + 1);
            }
            if (horns == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
         if (tail > 18) {
            int aDamage = 0;
            for (int i = 0; i < 4; i++) {
               aDamage += (int)(Math.random() * 6 + 1);
            }
            if (tail == 20) {
               aDamage = aDamage * 2;
            }
            damage += aDamage + 10;
         }
      }
      return damage;
   }
}