package com.qwerty.blackjackgame.strategy;


import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;

public interface DealerStrategy {
    boolean shouldHit(Hand hand, Card playerVisibleCard);

    void reset();
}