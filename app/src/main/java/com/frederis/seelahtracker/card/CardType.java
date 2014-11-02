package com.frederis.seelahtracker.card;

public enum CardType {
    BLESSING {
        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitBlessing();
        }
    },
    CURE {
        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitCure();
        }
    },
    SPELL {
        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitSpell();
        }
    },
    OTHER {
        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitOther();
        }
    },
    UNKNOWN {
        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitUnknown();
        }
    };

    abstract public <E> E accept(Visitor<E> visitor);

    public static interface Visitor<E> {
        E visitBlessing();
        E visitCure();
        E visitSpell();
        E visitOther();
        E visitUnknown();
    }

}
