package com.qwerty.blackjackgame.controller;




import com.qwerty.blackjackgame.game.BlackjackGame;
import com.qwerty.blackjackgame.model.Card;
import com.qwerty.blackjackgame.model.Hand;
import com.qwerty.blackjackgame.service.BlackjackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/blackjack")
@RequiredArgsConstructor
public class BlackjackController {

    private final BlackjackService service;
    private final BlackjackGame blackjackGame;

    @GetMapping
    public String showGame(Model model) {
        model.addAttribute("playerChips", blackjackGame.getPlayerChips());
        model.addAttribute("playerHands", null);
        model.addAttribute("dealerCards", null);
        model.addAttribute("playerScore", 0);
        model.addAttribute("dealerScore", 0);
        model.addAttribute("currentBet", 0);
        model.addAttribute("gameOver", false);
        model.addAttribute("currentStrategy", blackjackGame.getCurrentStrategy());
        return "blackjack";
    }

    @PostMapping("/start")
    public String startGame(@RequestParam int bet, Model model) {
        try {
            blackjackGame.startNewGame(bet);
            updateModel(model, false);
            if (blackjackGame.hasBlackjack() || blackjackGame.dealerHasBlackjack()) {
                blackjackGame.dealerPlay();
                model.addAttribute("result", blackjackGame.determineWinner());
                updateModel(model, true);
            }
            return "blackjack";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("playerChips", blackjackGame.getPlayerChips());
            model.addAttribute("playerHands", null);
            model.addAttribute("dealerCards", null);
            model.addAttribute("playerScore", 0);
            model.addAttribute("dealerScore", 0);
            model.addAttribute("currentBet", 0);
            model.addAttribute("gameOver", false);
            model.addAttribute("currentStrategy", blackjackGame.getCurrentStrategy());
            return "blackjack";
        }
    }

    @PostMapping("/hit")
    public String hit(Model model) {
        blackjackGame.playerHit();
        boolean gameOver = blackjackGame.isGameOver();
        updateModel(model, gameOver);
        if (gameOver) {
            blackjackGame.dealerPlay();
            model.addAttribute("result", blackjackGame.determineWinner());
        }
        return "blackjack";
    }

    @PostMapping("/double")
    public String doubleDown(Model model) {
        try {
            blackjackGame.playerDoubleDown();
            blackjackGame.dealerPlay();
            model.addAttribute("result", blackjackGame.determineWinner());
            updateModel(model, true);
            return "blackjack";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            updateModel(model, false);
            return "blackjack";
        }
    }

    @PostMapping("/surrender")
    public String surrender(Model model) {
        try {
            model.addAttribute("result", blackjackGame.playerSurrender());
            updateModel(model, true);
            return "blackjack";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            updateModel(model, false);
            return "blackjack";
        }
    }

    @PostMapping("/stand")
    public String stand(Model model) {
        blackjackGame.dealerPlay();
        updateModel(model, true);
        model.addAttribute("result", blackjackGame.determineWinner());
        return "blackjack";
    }

    @PostMapping("/new-game")
    public String newGame(Model model) {
        blackjackGame.resetBet();
        model.addAttribute("playerChips", blackjackGame.getPlayerChips());
        model.addAttribute("playerHands", null);
        model.addAttribute("dealerCards", null);
        model.addAttribute("playerScore", 0);
        model.addAttribute("dealerScore", 0);
        model.addAttribute("currentBet", 0);
        model.addAttribute("gameOver", false);
        model.addAttribute("currentStrategy", blackjackGame.getCurrentStrategy());
        return "blackjack";
    }

    @PostMapping("/set-strategy")
    public String setStrategy(@RequestParam String strategy, Model model) {
        blackjackGame.setDealerStrategy(strategy);
        model.addAttribute("playerChips", blackjackGame.getPlayerChips());
        model.addAttribute("playerHands", null);
        model.addAttribute("dealerCards", null);
        model.addAttribute("playerScore", 0);
        model.addAttribute("dealerScore", 0);
        model.addAttribute("currentBet", 0);
        model.addAttribute("gameOver", false);
        model.addAttribute("currentStrategy", blackjackGame.getCurrentStrategy());
        return "blackjack";
    }

    private void updateModel(Model model, boolean gameOver) {
        List<Hand> playerHands = blackjackGame.getPlayerHand() != null ? List.of(blackjackGame.getPlayerHand()) : new ArrayList<>();
        model.addAttribute("playerHands", playerHands);
        List<Card> dealerCards = service.getDealerCards(gameOver);
        model.addAttribute("dealerCards", dealerCards);
        model.addAttribute("playerScore", service.getPlayerScore());
        int dealerScore = gameOver ? service.getDealerScore() : (dealerCards.isEmpty() ? 0 : dealerCards.get(0).getValue());
        model.addAttribute("dealerScore", dealerScore);
        model.addAttribute("playerChips", blackjackGame.getPlayerChips());
        model.addAttribute("currentBet", blackjackGame.getPlayerBet());
        model.addAttribute("gameOver", gameOver);
        model.addAttribute("currentStrategy", blackjackGame.getCurrentStrategy());
    }
}