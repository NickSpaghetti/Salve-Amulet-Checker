package com.sac;

import com.google.inject.Provides;
import com.sac.enums.EntityNames;
import com.sac.enums.TobState;
import com.sac.managers.CoxManager;
import com.sac.managers.TobManager;
import com.sac.overlays.BloatRoomOverlay;
import com.sac.overlays.CoxLocationOverlay;
import com.sac.overlays.MysticRoomOverlay;
import com.sac.overlays.TobLocationOverlay;
import com.sac.panel.SalveAmuletCheckerPanel;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@PluginDescriptor(
        name = "SalveAmuletChecker"
)
public class SalveAmuletCheckerPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private SalveAmuletCheckerConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    public TobManager tobManager;

    @Inject
    public CoxManager coxManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private MysticRoomOverlay mysticRoomOverlay;
    @Inject
    private CoxLocationOverlay coxLocationOverlay;
    @Inject
    private BloatRoomOverlay bloatRoomOverlay;
    @Inject
    private TobLocationOverlay tobLocationOverlay;


    private TobState currentTobState;
    private SalveAmuletCheckerPanel panel;
    private NavigationButton navButton;

    private static final Set<Integer> IMBUED_SALVE_AMULET_IDS = Set.of(
            ItemID.NZONE_SALVE_AMULET,
            ItemID.NZONE_SALVE_AMULET_E,
            ItemID.SW_SALVE_AMULET,
            ItemID.SW_SALVE_AMULET_E,
            ItemID.PVPA_SALVE_AMULET,
            ItemID.PVPA_SALVE_AMULET_E
    );

    private static final Set<Integer> SALVE_AMULET_IDS = Stream.concat(
            IMBUED_SALVE_AMULET_IDS.stream(),
            Stream.of(net.runelite.api.ItemID.SALVE_AMULET, net.runelite.api.ItemID.SALVE_AMULET_E)
    ).collect(Collectors.toUnmodifiableSet());


    @Override
    protected void startUp() throws Exception {
        if (config.isSidePanelVisible()) {
            addNavBar();
        }

        log.info("Salve Amulet Checker started!");

        overlayManager.add(mysticRoomOverlay);
        overlayManager.add(coxLocationOverlay);
        overlayManager.add(bloatRoomOverlay);
        overlayManager.add(tobLocationOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        clientToolbar.removeNavigation(navButton);
        panel = null;
        navButton = null;
        log.info("Salve Amulet Checker stopped!");
        overlayManager.remove(mysticRoomOverlay);
        overlayManager.remove(coxLocationOverlay);
        overlayManager.remove(bloatRoomOverlay);
        overlayManager.remove(tobLocationOverlay);
    }


    @Provides
    SalveAmuletCheckerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(SalveAmuletCheckerConfig.class);
    }


    public void addNavBar() {
        // Can't @Inject because it is nulled out in shutdown()
        panel = injector.getInstance(SalveAmuletCheckerPanel.class);

        BufferedImage ICON = ImageUtil.loadImageResource(SalveAmuletCheckerPlugin.class, "salveAmuletEi.png");
        navButton = NavigationButton.builder()
                .tooltip("Salve Amulet Checker")
                .icon(ICON)
                .priority(9)
                .panel(panel)
                .build();
        clientToolbar.addNavigation(navButton);
    }

    public void removeNavBar() {
        if (navButton != null && panel != null) {
            clientToolbar.removeNavigation(navButton);
            navButton = null;
            panel = null;
        }
    }

    @Subscribe()
    public void onConfigChanged(ConfigChanged event) {
        String configName = event.getKey();
        if (configName.equals("isSidePanelVisible")) {
            boolean isSidePanelVisible = Boolean.parseBoolean(event.getNewValue());
            if (isSidePanelVisible && navButton == null && panel == null) {
                addNavBar();
            } else {
                removeNavBar();
            }
        }
    }

    @Subscribe(priority = -1)
    public void onGameTick(GameTick event) {
        computeActiveCheck();
    }

    private void computeActiveCheck() {
        HandelTobCompute();
        HandelCoxCompute();

    }

    private void HandelTobCompute() {
        if (!config.isEnabledInTob()) {
            return;
        }

        currentTobState = tobManager.getTobState();
        if (currentTobState != TobState.InTob) {
            return;
        }

        if (config.isSidePanelVisible()) {
            panel.setActiveMonster(EntityNames.BLOAT.getEntityName(), true);
        }

        tobManager.LoadRaiders();
        if (!tobManager.isBloatActive() || !config.isToxic()) {
            return;
        }

        for (Player player : client.getLocalPlayer().getWorldView().players()) {
            if (tobManager.getRaiderNames().contains(player.getName()) && !isSalveAmuletEquipped(player)) {
                whenSalveAmuletNotEquipped(player);
            }
        }

    }

    private void HandelCoxCompute() {
        if (!config.isEnabledInCox() || !coxManager.isPlayerInCoxRaid()) {
            return;
        }
        coxManager.LoadRaiders();
        if (config.isSidePanelVisible()) {
            panel.setActiveMonster(EntityNames.MYSTIC.getEntityName(), true);
        }
        if (!config.isToxic()) {
            return;
        }
        val playersMap = coxManager.getPlayersActiveInMysticRoom();
        playersMap.forEach((player) -> {
            if (!isImbuedSalveAmuletEquipped(player)) {
                whenSalveAmuletNotEquipped(player);
            }
        });
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String chatMessage = Text.removeTags(event.getMessage());
        if (config.isEnabledInCox() && coxManager.isPlayerInCoxRaid()) {
            coxChatMessageAction(chatMessage);
        }
    }

    private void coxChatMessageAction(String chatMessage) {
        if (chatMessage.startsWith(CoxManager.RAID_END_MESSAGE)) {
            coxManager.clearRaiders();
        }
    }

    public boolean isSalveAmuletEquipped(Player player) {
        return isSalveAmulet(getEquippedAmuletId(player));
    }

    public boolean isImbuedSalveAmuletEquipped(Player player) {
        return isImbuedSalveAmulet(getEquippedAmuletId(player));
    }

    private int getEquippedAmuletId(Player player) {
        return player.getPlayerComposition().getEquipmentId(KitType.AMULET);
    }

    private void whenSalveAmuletNotEquipped(Player offendingPlayer) {

        client.addChatMessage(ChatMessageType.PUBLICCHAT, offendingPlayer.getName(), String.format("%s Is not wearing their Salve Amulet!", offendingPlayer.getName()), null);
    }

    public boolean isSalveAmulet(int itemId) {
        return SALVE_AMULET_IDS.contains(itemId);
    }

    public boolean isImbuedSalveAmulet(int itemId) {
        return IMBUED_SALVE_AMULET_IDS.contains(itemId);
    }

}
