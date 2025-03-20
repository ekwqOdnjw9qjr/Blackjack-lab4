package com.qwerty.blackjackgame.model.enums;

public enum AggressiveState {
    LOW_RISK,    // очки < 16 - всегда брать карту
    MIDDLE_RISK, // очки 16–18 - брать карту в зависимости от карты игрока
    HIGH_RISK    // очки >= 19 - остановиться



}
