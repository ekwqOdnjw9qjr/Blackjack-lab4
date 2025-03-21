package com.qwerty.blackjackgame.game;

import com.qwerty.blackjackgame.model.Deck;
import com.qwerty.blackjackgame.model.Hand;
import com.qwerty.blackjackgame.strategy.AggressiveStrategy;
import com.qwerty.blackjackgame.strategy.DealerStrategy;
import com.qwerty.blackjackgame.strategy.SmartStrategy;
import com.qwerty.blackjackgame.strategy.ConservativeStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Component
public class BlackjackGame {
    private Deck deck = new Deck();
    private Hand playerHand = new Hand();
    private List<Integer> playerBets = new ArrayList<>();
    private int currentHandIndex = 0;
    private Hand dealerHand = new Hand();
    private DealerStrategy dealerStrategy;
    private int playerChips = 1000;
    private int playerBet = 0;
    private int insuranceBet = 0;
    private boolean surrendered = false;

    public BlackjackGame() {
        this.dealerStrategy = new SmartStrategy();
    }



    public void startNewGame(int bet) {
        if (bet > playerChips || bet <= 0) {
            throw new IllegalArgumentException("Неверная сумма ставки");
        }

        surrendered = false;
        playerBet = bet;
        playerHand.clear();
        dealerHand.clear();

        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
    }

    public void playerHit() {
        playerHand.addCard(deck.draw());
    }

    public void playerDoubleDown() {
        if (playerBet * 2 > playerChips) {
            throw new IllegalArgumentException("Не хватает фишек для удвоения");
        }
        playerBet *= 2;
        playerHit();
    }


    public String playerSurrender() {
        if (playerBet == 0) {
            throw new IllegalStateException("Нет активной ставки чтобы сдаться");
        }
        surrendered = true;
        playerChips -= playerBet;
        playerChips += playerBet / 2;
        String result = "Игрок сдался. Половина ставки (" + (playerBet / 2) + ") возвращена.";
        playerBet = 0;
        return result;
    }

    public String determineWinner() {
        if (surrendered) {
            return playerSurrender();
        }
        int playerScore = playerHand.calculateScore();
        int dealerScore = dealerHand.calculateScore();
        log.info("Определение победителя: Очки игрока = " + playerScore + ", Очки дилера = " + dealerScore);

        if (playerScore > 21 && dealerScore > 21) {
            return "Push! Оба перебрали. Изменений в фишках нет.";
        }

        if (playerScore > 21) {
            playerChips -= playerBet;
            return "Dealer wins! У игрока перебор. Ставка " + playerBet + " проигрыш.";
        }

        if (dealerScore > 21) {
            playerChips += playerBet;
            return "Player wins! У дилера перебор. Ставка " + playerBet + " победа.";
        }

        if (playerScore > dealerScore) {
            playerChips += playerBet;
            return "Player wins! Ставка " + playerBet + " победа.";
        }
        if (dealerScore > playerScore) {
            playerChips -= playerBet;
            return "Dealer wins! Ставка " + playerBet + " проигрыш.";
        }
        return "Push! Фишки не изменились.";
    }

    public boolean isGameOver() {
        return playerHand.calculateScore() > 21 || dealerHand.calculateScore() > 21 || surrendered;
    }

    public boolean hasBlackjack() {
        return playerHand.calculateScore() == 21 && playerHand.getCards().size() == 2;
    }

    public boolean dealerHasBlackjack() {
        return dealerHand.calculateScore() == 21 && dealerHand.getCards().size() == 2;
    }

    public void resetBet() {
        playerBet = 0;
        insuranceBet = 0;
    }

    public void setDealerStrategy(String strategyName) {
        switch (strategyName.toLowerCase()) {
            case "conservative" -> dealerStrategy = new ConservativeStrategy();
            case "aggressive" -> dealerStrategy = new AggressiveStrategy();
            case "smart" -> dealerStrategy = new SmartStrategy();
            default -> dealerStrategy = new SmartStrategy();
        }
    }

    public void dealerPlay() {
        log.info("Дилер начинает играть по стратегии " + getCurrentStrategy());
        while (true) {
            int dealerScore = dealerHand.calculateScore();
            log.info("Очки дилера: " + dealerScore + ", проверка, стоит ли брать карту...");
            if (!dealerStrategy.shouldHit(dealerHand, playerHand.getCards().get(0))) {
                break;
            }
            log.info("Очки дилера: " + dealerScore + ", взятие карты...");
            dealerHand.addCard(deck.draw());
        }
        log.info("Дилер завершил игру. Итоговый счет: " + dealerHand.calculateScore());
    }

    public String getCurrentStrategy() {
        if (dealerStrategy instanceof ConservativeStrategy) return "Консервативная";
        if (dealerStrategy instanceof AggressiveStrategy) return "Агрессивная";
        if (dealerStrategy instanceof SmartStrategy) return "Адаптирующаяся";
        return "Smart";
    }

    private boolean isBlackjack(Hand hand) {
        return hand.calculateScore() == 21 && hand.getCards().size() == 2;
    }
}