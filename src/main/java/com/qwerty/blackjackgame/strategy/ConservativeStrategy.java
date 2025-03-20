package com.qwerty.blackjackgame.strategy;

import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;
import com.qwerty.blackjackgame.model.enums.ConservativeState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ConservativeStrategy implements DealerStrategy {
    private static final int DEALER_THRESHOLD = 17;
    private ConservativeState currentState;


    public ConservativeStrategy() {
        currentState = ConservativeState.COLLECTING;
    }

    @Override
    public boolean shouldHit(Hand hand, Card playerVisibleCard) {
        int score = hand.calculateScore();
        log.info("ConservativeStrategy: Очки дилера = " + score + ", Текущее состояние = " + currentState);
        updateState(score);
        boolean decision = makeDecision();
        log.info("ConservativeStrategy: Решение брать карту = " + decision);
        return decision;
    }

    private void updateState(int score) {
        switch (currentState) {
            case COLLECTING:
                if (score >= DEALER_THRESHOLD) {
                    currentState = ConservativeState.STOPPED;
                }
                break;

            case STOPPED:
                if (score < DEALER_THRESHOLD) {
                    currentState = ConservativeState.COLLECTING;
                }
                break;
        }
        log.info("ConservativeStrategy: Обновленное состояние = " + currentState);
    }

    private boolean makeDecision() {
        return switch (currentState) {
            case COLLECTING -> true;
            case STOPPED -> false;
        };
    }

    @Override
    public void reset() {
        this.currentState = ConservativeState.COLLECTING;
    }

}