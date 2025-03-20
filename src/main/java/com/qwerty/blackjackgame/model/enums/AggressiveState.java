package com.qwerty.blackjackgame.model.enums;

public enum State {
    LOW_RISK,    // очки < 16 - всегда брать карту
    MODERATE_RISK, // очки 16–18 - брать карту в зависимости от карты игрока
    HIGH_RISK    // очки >= 19 - остановиться



}
