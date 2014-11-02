package com.frederis.seelahtracker;

import android.util.Log;

import com.frederis.seelahtracker.card.CardType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static java.util.Collections.frequency;

public class CardManager {

    private List<CardType> mTotalTypes;

    private List<CardType> mDeck;
    private List<CardType> mHand;
    private List<CardType> mDiscard;

    private List<Listener> mListeners;

    @Inject
    public CardManager() {
        mListeners = new ArrayList<Listener>();
    }

    public void initialize(int blessingCount,
                           int cureCount,
                           int spellCount,
                           int weaponCount,
                           int armorCount,
                           int allyCount) {
        mTotalTypes = new ArrayList<CardType>();

        addToTotalTypes(CardType.BLESSING, blessingCount);
        addToTotalTypes(CardType.CURE, cureCount);
        addToTotalTypes(CardType.SPELL, spellCount);
        addToTotalTypes(CardType.OTHER, weaponCount + armorCount + allyCount);

        mDeck = new ArrayList<CardType>();
        for (int i = 0; i < mTotalTypes.size(); i++) {
            mDeck.add(CardType.UNKNOWN);
        }

//        mDeck.set(3, CardType.BLESSING);
//        mDeck.set(2, CardType.SPELL);
//        mDeck.set(1, CardType.OTHER);

        mHand = new ArrayList<CardType>();
        mDiscard = new ArrayList<CardType>();

        update();
    }

    private void addToTotalTypes(CardType type, int times) {
        for (int i = 0; i < times; i++) {
            mTotalTypes.add(type);
        }
    }

    public void drawCard() {
        if (mDeck.size() <= 0) {
            throw new IllegalStateException("You're dead, stop drawing cards");
        }

        mHand.add(mDeck.remove(mDeck.size() - 1));

        update();
    }

    public void acquireCard(CardType cardType) {
        mTotalTypes.add(cardType);
        mHand.add(cardType);

        update();
    }

    public void discardFromTop(CardType cardType) {
        mDeck.remove(mDeck.size() - 1);
        mDiscard.add(cardType);

        update();
    }

    public void rechargeFromTop(CardType cardType) {
        mDeck.remove(mDeck.size() - 1);
        mDeck.add(0, cardType);

        update();
    }

    public void paulaCard(CardType cardType) {
        cardType.accept(new CardType.Visitor<Void>() {
            @Override
            public Void visitBlessing() {
                rechargeFromTop(CardType.BLESSING);
                return null;
            }

            @Override
            public Void visitCure() {
                rechargeFromTop(CardType.CURE);
                return null;
            }

            @Override
            public Void visitSpell() {
                rechargeFromTop(CardType.SPELL);
                return null;
            }

            @Override
            public Void visitOther() {
                discardFromTop(CardType.OTHER);
                return null;
            }

            @Override
            public Void visitUnknown() {
                throw new IllegalAccessError("Can't paula an unknown card");
            }
        });
    }

    public void discardFromHand(CardType cardType) {
        if (!mHand.remove(cardType)) {
            throw new IllegalArgumentException("discardFromHand called with card not in hand");
        }

        mDiscard.add(cardType);

        Log.d("SEELAH", "Added to discard, current state: " + mDiscard);

        update();
    }

    public void rechargeFromHand(CardType cardType) {
        if (!mHand.remove(cardType)) {
            throw new IllegalArgumentException("rechargeFromHand called with card not in hand");
        }

        mDeck.add(0, cardType);

        update();
    }

    public void removeFromHand(CardType cardType) {
        if (!mHand.remove(cardType)) {
            throw new IllegalArgumentException("removeFromHand called with card not in hand");
        }

        mTotalTypes.remove(cardType);

        update();
    }

    public void addToHandFromDiscard(CardType cardType) {
        if (!mDiscard.remove(cardType)) {
            throw new IllegalArgumentException("addToHand called with card not in discard");
        }

        mHand.add(cardType);

        update();
    }

    public void rechargeFromDiscard(CardType cardType) {
        if (!mDiscard.remove(cardType)) {
            throw new IllegalArgumentException("rechargeFromDiscard called with card not in discard");
        }

        mDeck.add(0, cardType);

        update();
    }

    public void removeFromDiscard(CardType cardType) {
        if (!mDiscard.remove(cardType)) {
            throw new IllegalArgumentException("removeFromDiscard called with card not in discard");
        }

        mTotalTypes.remove(cardType);

        update();
    }

    public void cureCardsFromDiscard(List<CardType> cardTypes) {
        for (CardType cardType : cardTypes) {
            if (!mDiscard.remove(cardType)) {
                throw new IllegalArgumentException("Attempted to cure a card not in discard");
            }

            mDeck.add(cardType);
        }

        shuffleDeck();

        rechargeFromHand(CardType.CURE);

        update();
    }

    public void identifyCardInHand(CardType previousType, CardType identifiedType) {
        int position = mHand.indexOf(previousType);

        if (position < 0) {
            throw new IllegalArgumentException("No card in hand of type: " + previousType);
        }

        mHand.set(position, identifiedType);

        update();
    }

    private void shuffleDeck() {
        for (int i = 0; i < mDeck.size(); i++) {
            mDeck.set(i, CardType.UNKNOWN);
        }
    }

    private void update() {
        for (Listener listener : mListeners) {
            listener.onCardsUpdated(mDeck, mHand, mDiscard, computeUnknownPercentage());
        }
    }

    private int computeUnknownPercentage() {
        return Math.round(100 * (getNumberOfMissingGoodOnes() / getUnknownsInList(mDeck)));
    }

    private float getNumberOfMissingGoodOnes() {
        return getGoodOnesInList(mTotalTypes) - getGoodOnesInList(mDeck) - getGoodOnesInList(mDiscard) - getGoodOnesInList(mHand);
    }

    private float getUnknownsInList(List<CardType> collection) {
        float totalCount = 0.0f;

        for (CardType type : collection) {
            if (type.equals(CardType.UNKNOWN)) {
                totalCount++;
            }
        }

        return totalCount;
    }

    private float getGoodOnesInList(List<CardType> collection) {
        float totalCount = 0.0f;

        for (CardType type : collection) {
            if (isGoodOne(type)) {
                totalCount++;
            }
        }

        return totalCount;
    }

    private boolean isGoodOne(CardType type) {
        return type.equals(CardType.BLESSING) || type.equals(CardType.CURE) || type.equals(CardType.SPELL);
    }

    public void addListener(Listener listener) {
        addListener(listener, false);
    }

    public void addListener(Listener listener, boolean deliverImmediately) {
        mListeners.add(listener);

        if (deliverImmediately) {
            listener.onCardsUpdated(mDeck, mHand, mDiscard, computeUnknownPercentage());
        }
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    public static interface Listener {
        void onCardsUpdated(List<CardType> deck,
                            List<CardType> hand,
                            List<CardType> discard,
                            int unknownPercentage);
    }

}
