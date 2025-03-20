package com.qwerty.blackjackgame.strategy;

import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;
import com.qwerty.blackjackgame.model.enums.AggressiveState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AggressiveStrategy implements DealerStrategy {

    private static final int LOW_THRESHOLD = 16;
    private static final int HIGH_THRESHOLD = 19;
    private static final int PLAYER_CARD_THRESHOLD = 10;

    private AggressiveState currentState;

    public AggressiveStrategy() {
        currentState = AggressiveState.LOW_RISK;
        log.info("Инициализация AggressiveStrategy: начальное состояние = " + currentState);
    }

    @Override
    public boolean shouldHit(Hand hand, Card playerVisibleCard) {
        int score = hand.calculateScore();
        log.info("AggressiveStrategy: Очки дилера = " + score + ", Текущее состояние = " + currentState);
        updateState(score);
        boolean decision = makeDecision(playerVisibleCard);
        log.info("AggressiveStrategy: Решение взять карту = " + decision);
        return decision;
    }

    private void updateState(int score) {
        AggressiveState previousState = currentState;
        switch (currentState) {
            case LOW_RISK:
                if (score >= LOW_THRESHOLD && score < HIGH_THRESHOLD) {
                    currentState = AggressiveState.MIDDLE_RISK;
                } else if (score >= HIGH_THRESHOLD) {
                    currentState = AggressiveState.HIGH_RISK;
                }
                break;

            case MIDDLE_RISK:
                if (score < LOW_THRESHOLD) {
                    currentState = AggressiveState.LOW_RISK;
                } else if (score >= HIGH_THRESHOLD) {
                    currentState = AggressiveState.HIGH_RISK;
                }
                break;

            case HIGH_RISK:
                if (score < LOW_THRESHOLD) {
                    currentState = AggressiveState.LOW_RISK;
                } else if (score < HIGH_THRESHOLD) {
                    currentState = AggressiveState.MIDDLE_RISK;
                }
                break;
        }
        if (previousState != currentState) {
            log.info("AggressiveStrategy: Состояние изменено с " + previousState + " на " + currentState);
        }
    }

    private boolean makeDecision(Card playerVisibleCard) {
        int playerCardValue = playerVisibleCard.getValue();
        log.info("AggressiveStrategy: Значение видимой карты игрока = {}", playerCardValue);

        return switch (currentState) {
            case LOW_RISK -> {
                log.info("AggressiveStrategy: Состояние LOW_RISK, берем карту");
                yield true;
            }
            case MIDDLE_RISK -> {
                boolean takeCard = playerCardValue < PLAYER_CARD_THRESHOLD;
                log.info("AggressiveStrategy: Состояние MIDDLE_RISK, {} (карта < {}: {})",
                        takeCard ? "берем карту" : "останавливаемся",
                        PLAYER_CARD_THRESHOLD, takeCard);
                yield takeCard;
            }
            case HIGH_RISK -> {
                log.info("AggressiveStrategy: Состояние HIGH_RISK, останавливаемся");
                yield false;
            }
        };
    }


    @Override
    public void reset() {
        currentState = AggressiveState.LOW_RISK;
        log.info("Сброс состояния AggressiveStrategy: новое состояние = " + currentState);
    }

}