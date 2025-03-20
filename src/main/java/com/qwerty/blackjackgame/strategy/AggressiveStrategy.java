package com.qwerty.blackjackgame.strategy;

import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;

public class AggressiveStrategy implements DealerStrategy {
    private static final int RISK_THRESHOLD = 19;

    @Override
    public boolean shouldHit(Hand hand, Card playerVisibleCard) {
        int score = hand.calculateScore();
        return score < RISK_THRESHOLD && (playerVisibleCard.getValue() < 10 || score < 16);
    }
}