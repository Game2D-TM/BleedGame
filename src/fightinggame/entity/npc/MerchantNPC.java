package fightinggame.entity.npc;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Dialogue;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.state.GameState;
import fightinggame.input.handler.game.player.PlayerKeyboardHandler;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class MerchantNPC extends NPC {

    private PlayerKeyboardHandler keyHandler;
    private Dialogue dialogue;
    private boolean isConversation;
    private boolean openOption;
    private final String[] options = {"Yes", "No"};
    private int optionIndex = 0;

    public MerchantNPC(int id, String name, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay, boolean isLTR) {
        super(id, name, position, animations, gameplay, isLTR);
        avatar = ImageManager.flipImage(avatar);
        Player player = gameplay.getPlayer();
        if (player != null) {
            keyHandler = (PlayerKeyboardHandler) player.getController().get(0);
        }
        dialogue = new Dialogue(this, gameplay);
        List<String> texts = new ArrayList<>();
        texts.add("Hi I'm " + name + ". Nice to see you. Seem you are like me, A Advanturer!");
        texts.add("I have some stuffs wanna check it out?");
        dialogue.setTexts(texts);
    }

    public MerchantNPC() {
    }

    @Override
    public void tick() {
        super.tick();
        if (keyHandler != null) {
            Player player = gameplay.getPlayer();
            if (checkHit(player.getHitBoxPosition(), player)) {
                if (isConversation) {
                    if (openOption) {
                        if (keyHandler.isDownArrowPressed()) {
                            if (optionIndex < options.length - 1) {
                                optionIndex++;
                            } else {
                                optionIndex = 0;
                            }
                            keyHandler.setDownArrowPressed(false);
                        }
                        if (keyHandler.isUpArrowPressed()) {
                            if (optionIndex > 0) {
                                optionIndex--;
                            } else {
                                optionIndex = options.length - 1;
                            }
                            keyHandler.setUpArrowPressed(false);
                        }
                        if (keyHandler.isEnterPressed()) {
                            switch (optionIndex) {
                                case 0:
                                    break;
                                case 1:
                                    isConversation = false;
                                    dialogue.setEndDialogue(false);
                                    openOption = false;
                                    optionIndex = 0;
                                    Game.STATE = GameState.GAME_STATE;
                                    break;
                            }
                            keyHandler.setEnterPressed(false);
                        }
                    } else {
                        if (keyHandler.isSpaceBtnPressed()) {
                            if (dialogue.haveNextText()) {
                                dialogue.nextLoop();
                                keyHandler.setSpaceBtnPressed(false);
                            } else {
                                openOption = true;
                                Game.STATE = GameState.DIALOGUE_STATE;
                            }
                        }
                    }
                } else {
                    if (keyHandler.eBtnPressed()) {
                        dialogue.setIndex(0);
                        if (player.isLTR()) {
                            isLTR = false;
                        } else {
                            isLTR = true;
                        }
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.IDLE_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.IDLE_RTL);
                        }
                        isConversation = true;
                    }
                }
            } else {
                isConversation = false;
                openOption = false;
                optionIndex = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isConversation) {
            if (dialogue != null) {
                dialogue.render(g);
            }
            if (openOption) {
                GamePosition dialoguePos = dialogue.getPosition();
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0, 0, 0, 220));
                g2.fillRoundRect(dialoguePos.getMaxX() - 100, dialoguePos.getYPosition() - 80,
                        150, 200, 35, 35);
                g2.setColor(Color.white);
                g2.setStroke(new BasicStroke(5));
                g2.drawRoundRect(dialoguePos.getMaxX() - 100, dialoguePos.getYPosition() - 80,
                        150, 200, 25, 25);
                g2.setFont(DataManager.getFont(40f));
                g2.setColor(Color.white);
                if (options != null && options.length > 0) {
                    int y = dialoguePos.getYPosition() - 10;
                    int x = dialoguePos.getMaxX() - 50;
                    for (String option : options) {
                        g2.drawString(option, x, y);
                        y += 65;
                        x += 5;
                    }
                    g.setColor(new Color(133, 0, 0));
                    switch (optionIndex) {
                        case 0:
                            y = dialoguePos.getYPosition() - 10;
                            x = dialoguePos.getMaxX() - 50;
                            break;
                        case 1:
                            y = dialoguePos.getYPosition() + 55;
                            x = dialoguePos.getMaxX() - 45;
                            break;
                    }
                    g2.drawString(options[optionIndex], x, y);
                }
            }
        }
    }

}
