package de.mc503.kleinanzeigencrawler.enums;

import lombok.Getter;

public enum Zustand {

    NEU("new"), SEHR_GUT("like_new"), GUT("ok"), IN_ORDNUNG("alright"), DEFEKT("defect");

    @Getter
    private final String stringValue;

    Zustand(String stringValue) {
        this.stringValue = stringValue;
    }
}
