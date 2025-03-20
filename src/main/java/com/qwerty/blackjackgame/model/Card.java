package com.qwerty.blackjackgame.model;

import com.qwerty.blackjackgame.model.enums.Rank;
import com.qwerty.blackjackgame.model.enums.Suit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Card {
    private final Suit suit;
    private final Rank rank;

    public String getDisplayRank() {
        switch (rank) {
            case ACE: return "A";
            case JACK: return "J";
            case QUEEN: return "Q";
            case KING: return "K";
            case TEN: return "10";
            default: return String.valueOf(rank.getValue());
        }
    }

    public int getValue() {
        return rank.getValue();
    }

    public String getSuitSymbol() {
        return switch (suit) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}