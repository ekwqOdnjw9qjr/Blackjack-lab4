package com.qwerty.blackjackgame.model;

import com.qwerty.blackjackgame.model.enums.Rank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            int value = card.getValue();
            score += value;

            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        while (score <= 21 && aces > 0) {
            score += 10;
            aces--;
        }

        return score;
    }

    public void clear() {
        cards.clear();
    }
}