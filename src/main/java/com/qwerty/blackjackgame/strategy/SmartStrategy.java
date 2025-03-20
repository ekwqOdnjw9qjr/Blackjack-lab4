package com.qwerty.blackjackgame.strategy;

import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;
import com.qwerty.blackjackgame.model.enums.SmartState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmartStrategy implements DealerStrategy {
    private static final int LOW_THRESHOLD = 11;
    private static final int HIGH_THRESHOLD = 17;
    private static final int MID_THRESHOLD = 14;
    private static final int PLAYER_CARD_THRESHOLD = 7;

    private SmartState currentState;

    public SmartStrategy() {
        currentState = SmartState.LOW_SCORE;
        log.info("Инициализация SmartStrategy: начальное состояние = " + currentState);
    }

    @Override
    public boolean shouldHit(Hand hand, Card playerVisibleCard) {
        int dealerScore = hand.calculateScore();
        log.info("SmartStrategy: Очки дилера = " + dealerScore + ", Текущее состояние = " + currentState);
        updateState(dealerScore);
        boolean decision = makeDecision(dealerScore, playerVisibleCard);
        log.info("SmartStrategy: Решение взять карту = " + decision);
        return decision;
    }

    @Override
    public void reset() {
        currentState = SmartState.LOW_SCORE;
        log.info("Сброс состояния SmartStrategy: новое состояние = " + currentState);
    }

    private void updateState(int score) {
        SmartState previousState = currentState;
        switch (currentState) {
            case LOW_SCORE:
                if (score > LOW_THRESHOLD && score < HIGH_THRESHOLD) {
                    currentState = SmartState.MID_SCORE;
                } else if (score >= HIGH_THRESHOLD) {
                    currentState = SmartState.HIGH_SCORE;
                }
                break;

            case MID_SCORE:
                if (score <= LOW_THRESHOLD) {
                    currentState = SmartState.LOW_SCORE;
                } else if (score >= HIGH_THRESHOLD) {
                    currentState = SmartState.HIGH_SCORE;
                }
                break;

            case HIGH_SCORE:
                if (score <= LOW_THRESHOLD) {
                    currentState = SmartState.LOW_SCORE;
                } else if (score < HIGH_THRESHOLD) {
                    currentState = SmartState.MID_SCORE;
                }
                break;
        }
        if (previousState != currentState) {
            log.info("SmartStrategy: Состояние изменено с " + previousState + " на " + currentState);
        }
    }

    private boolean makeDecision(int dealerScore, Card playerVisibleCard) {
        int playerCardValue = playerVisibleCard.getValue();
        log.info("SmartStrategy: Значение видимой карты игрока = {}", playerCardValue);

        return switch (currentState) {
            case LOW_SCORE -> {
                log.info("SmartStrategy: Состояние LOW_SCORE, всегда берем карту");
                yield true;
            }
            case MID_SCORE -> {
                boolean shouldHit = (playerCardValue >= PLAYER_CARD_THRESHOLD && dealerScore < HIGH_THRESHOLD) || dealerScore < MID_THRESHOLD;
                log.info("SmartStrategy: Состояние MID_SCORE, решение: {} (карта игрока >= {}: {}, очки дилера < {}: {}, или очки дилера < {}: {})",
                        shouldHit, PLAYER_CARD_THRESHOLD, playerCardValue >= PLAYER_CARD_THRESHOLD,
                        HIGH_THRESHOLD, dealerScore < HIGH_THRESHOLD,
                        MID_THRESHOLD, dealerScore < MID_THRESHOLD);
                yield shouldHit;
            }
            case HIGH_SCORE -> {
                log.info("SmartStrategy: Состояние HIGH_SCORE, останавливаемся");
                yield false;
            }
        };
    }


}