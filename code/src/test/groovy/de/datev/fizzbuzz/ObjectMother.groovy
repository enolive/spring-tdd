package de.datev.fizzbuzz

import java.security.SecureRandom

class ObjectMother {
  static int aNumberUpTo(int limit) {
    new SecureRandom().nextInt(limit)
  }
}
