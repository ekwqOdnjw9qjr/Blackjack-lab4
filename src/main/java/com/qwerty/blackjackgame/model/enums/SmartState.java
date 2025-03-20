package com.qwerty.blackjackgame.model.enums;

public enum SmartState {
    LOW_SCORE,   // очки <= 11 - всегда брать карту
    MID_SCORE,   // очки 12–16 - решение зависит от карты игрока
    HIGH_SCORE   // очки >= 17 - остановиться
}
