package com.lypaka.betterlures.Utils;

import com.lypaka.betterlures.Lures.Lure;
import com.lypaka.betterlures.Lures.LureUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarHandler {

    private final ServerPlayerEntity player;
    private final Lure lure;

    public BossBarHandler (ServerPlayerEntity player, Lure lure) {

        this.player = player;
        this.lure = lure;

    }

    public void build() {

        String color = this.lure.getBossBarColor();
        String text = this.lure.getBossBarText();
        if (!LureUtils.barMap.containsKey(this.player.getUniqueID())) {

            Map<String, ServerBossInfo> map = new HashMap<>();
            ServerBossInfo bar = new ServerBossInfo(
                    FancyText.getFormattedText(text),
                    getColorFromName(color),
                    BossInfo.Overlay.PROGRESS
            );

            map.put(this.lure.getLureName(), bar);
            LureUtils.barMap.put(this.player.getUniqueID(), map);
            bar.addPlayer(this.player);
            bar.setPercent(100);
            bar.setVisible(true);

        } else {

            Map<String, ServerBossInfo> innerMap = LureUtils.barMap.get(this.player.getUniqueID());
            if (!innerMap.containsKey(this.lure.getLureName())) {

                ServerBossInfo bar = new ServerBossInfo(
                        FancyText.getFormattedText(text),
                        getColorFromName(color),
                        BossInfo.Overlay.PROGRESS
                );

                innerMap.put(this.lure.getLureName(), bar);
                LureUtils.barMap.put(this.player.getUniqueID(), innerMap);
                bar.addPlayer(this.player);
                bar.setPercent(100);
                bar.setVisible(true);

            }

        }

    }

    private static BossInfo.Color getColorFromName (String name) {

        switch (name.toLowerCase()) {

            case "pink":
                return BossInfo.Color.PINK;

            case "blue":
                return BossInfo.Color.BLUE;

            case "red":
                return BossInfo.Color.RED;

            case "green":
                return BossInfo.Color.GREEN;

            case "yellow":
                return BossInfo.Color.YELLOW;

            case "purple":
                return BossInfo.Color.PURPLE;

            default:
                return BossInfo.Color.WHITE;

        }

    }

}
