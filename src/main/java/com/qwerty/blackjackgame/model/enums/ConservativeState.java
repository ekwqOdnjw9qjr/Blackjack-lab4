package com.qwerty.blackjackgame.model.enums;

public enum ConservativeState {
    COLLECTING,  // очки < 17 - дилер продолжает брать карты
    STOPPED      // очки >= 17 - дилер останавливается
}
