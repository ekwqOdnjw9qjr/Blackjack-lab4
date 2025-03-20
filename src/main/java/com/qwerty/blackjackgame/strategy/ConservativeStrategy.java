package com.qwerty.blackjackgame.strategy;

import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;

public class ConservativeStrategy implements DealerStrategy {
    private static final int DEALER_THRESHOLD = 17;

    @Override
    public boolean shouldHit(Hand hand, Card playerVisibleCard) {
        return hand.calculateScore() < DEALER_THRESHOLD;
    }
}