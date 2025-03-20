package com.qwerty.blackjackgame.game;

import com.qwerty.blackjackgame.model.Deck;
import com.qwerty.blackjackgame.model.Hand;
import com.qwerty.blackjackgame.strategy.AggressiveStrategy;
import com.qwerty.blackjackgame.strategy.DealerStrategy;
import com.qwerty.blackjackgame.strategy.SmartStrategy;
import com.qwerty.blackjackgame.strategy.ConservativeStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@NoArgsConstructor
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

    public void startNewGame(int bet) {
        if (bet > playerChips || bet <= 0) {
            throw new IllegalArgumentException("Invalid bet amount");
        }

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
            throw new IllegalArgumentException("Not enough chips to double down");
        }
        playerBet *= 2;
        playerHit();
    }


    public String playerSurrender() {
        if (playerBet == 0) {
            throw new IllegalStateException("No active bet to surrender");
        }
        surrendered = true;
        playerChips -= playerBet;
        playerChips += playerBet / 2;
        String result = "Player surrendered. Half of the bet (" + (playerBet / 2) + ") returned.";
        playerBet = 0;
        return result;
    }

    public void dealerPlay() {
        while (dealerHand.calculateScore() < 17) {
            dealerHand.addCard(deck.draw());
        }
    }

    public String determineWinner() {
        if (surrendered) {
            return playerSurrender();
        }
        int playerScore = playerHand.calculateScore();
        int dealerScore = dealerHand.calculateScore();

        if (playerScore > 21) {
            playerChips -= playerBet;
            return "Dealer wins! Player busts. Bet " + playerBet + " lost.";
        }
        if (dealerScore > 21) {
            playerChips += playerBet;
            return "Player wins! Dealer busts. Bet " + playerBet + " won.";
        }
        if (playerScore > dealerScore) {
            playerChips += playerBet;
            return "Player wins! Bet " + playerBet + " won.";
        }
        if (dealerScore > playerScore) {
            playerChips -= playerBet;
            return "Dealer wins! Bet " + playerBet + " lost.";
        }
        return "Push! No change in chips.";
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

    public String getCurrentStrategy() {
        if (dealerStrategy instanceof ConservativeStrategy) return "Conservative";
        if (dealerStrategy instanceof AggressiveStrategy) return "Aggressive";
        if (dealerStrategy instanceof SmartStrategy) return "Smart";
        return "Smart";
    }

    private boolean isBlackjack(Hand hand) {
        return hand.calculateScore() == 21 && hand.getCards().size() == 2;
    }
}