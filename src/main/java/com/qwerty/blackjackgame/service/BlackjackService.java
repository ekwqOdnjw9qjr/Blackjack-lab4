package com.qwerty.blackjackgame.service;


import com.qwerty.blackjackgame.game.BlackjackGame;
import com.qwerty.blackjackgame.model.Card;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class BlackjackService {

    private final BlackjackGame game;

    public int getPlayerScore() {
        return game.getPlayerHand().calculateScore();
    }

    public int getDealerScore() {
        return game.getDealerHand().calculateScore();
    }

    public List<Card> getDealerCards(boolean gameOver) {
        List<Card> dealerCards = game.getDealerHand().getCards();
        if (gameOver || dealerCards.size() < 2) {
            return dealerCards;
        }
        List<Card> visibleCards = new ArrayList<>();
        visibleCards.add(dealerCards.get(0));
        visibleCards.add(new Card(null, null));
        return visibleCards;
    }

}
