package com.qwerty.blackjackgame.strategy;

import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;

public class SmartStrategy implements DealerStrategy {
    @Override
    public boolean shouldHit(Hand hand, Card playerVisibleCard) {
        int dealerScore = hand.calculateScore();
        int playerCardValue = playerVisibleCard.getValue();

        if (dealerScore >= 17) {
            return false;
        }
        if (dealerScore <= 11) {
            return true;
        }
        if (playerCardValue >= 7 && dealerScore < 17) {
            return true;
        }
        return dealerScore < 14;
    }
}