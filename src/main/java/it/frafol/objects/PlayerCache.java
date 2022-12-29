package it.frafol.objects;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.UUID;

@UtilityClass
public class PlayerCache {

    @Getter
    private final HashMap<UUID, Integer> CommandExecuted = new HashMap<>();

}