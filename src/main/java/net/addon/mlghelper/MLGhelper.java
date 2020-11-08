package net.addon.mlghelper;

import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class MLGhelper extends LabyModAddon {

    public static boolean enabled;
    private int keyInt;
    public static String mlg;

    private String jumpS;
    private String walkS;
    private String bothS;
    private String notS;

    private String nowebS;

    private String disabledS;
    @Override
    public void onEnable() {
        System.out.println("[MLGhelper] Enabled succesfully!");
        this.getApi().registerForgeListener(this);

        this.getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
            @Override
            public void accept(ServerData serverData) {
                if(enabled) {
                    LabyMod.getInstance().displayMessageInChat("§6[MLGhelper] §7Du bist dem Server " + serverData.getIp() + " gejoint!");
                }
            }
        });
        this.getApi().registerModule(new MLGmodule());

    }

    @Override
    public void loadConfig() {
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.keyInt = this.getConfig().has("key") ? this.getConfig().get("key").getAsInt() : 50;

        if(!(this.getConfig().has("jump"))) {
            this.getConfig().addProperty("jump", "Springen");
        }
        if(!(this.getConfig().has("walk"))) {
            this.getConfig().addProperty("walk", "Laufen");
        }
        if(!(this.getConfig().has("both"))) {
            this.getConfig().addProperty("both", "Laufen & Springen");
        }
        if(!(this.getConfig().has("not"))) {
            this.getConfig().addProperty("not", "Nicht möglich");
        }
        saveConfig();
        this.jumpS = this.getConfig().has("jump") ? this.getConfig().get("jump").getAsString() : "Springen";
        this.walkS = this.getConfig().has("walk") ? this.getConfig().get("walk").getAsString() : "Laufen";
        this.bothS = this.getConfig().has("both") ? this.getConfig().get("both").getAsString() : "Laufen & Springen";
        this.notS = this.getConfig().has("not") ? this.getConfig().get("not").getAsString() : "Nicht möglich";

        this.nowebS = this.getConfig().has("noweb") ? this.getConfig().get("noweb").getAsString() : "Kein Cobweb";

        this.disabledS = this.getConfig().has("disabled") ? this.getConfig().get("disabled").getAsString() : "Disabled";
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        BooleanElement enabledElement = new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {

            @Override
            public void accept(final Boolean settingEnabled) {
                enabled = settingEnabled;

                getConfig().addProperty("enabled", settingEnabled);
                saveConfig();
            }

        }, this.enabled);

        KeyElement hotkey = new KeyElement("Hotkey", new ControlElement.IconData(Material.PAPER),
                keyInt, new Consumer<Integer>(){
            @Override
            public void accept(Integer settingKey){
                keyInt = settingKey;

                getConfig().addProperty("key", settingKey);
                saveConfig();
            }
        });
        list.add(enabledElement);
        list.add(hotkey);
    }

    @SubscribeEvent
    public void onKeyPress(final InputEvent.KeyInputEvent e){
        if(Keyboard.isKeyDown(keyInt)){
            if(enabled){
                enabled = false;
                getConfig().addProperty("enabled", false);
            } else {
                enabled = true;
                getConfig().addProperty("enabled", true);
            }
            saveConfig();
        }
    }

    @SubscribeEvent
    public void onRender(final FOVUpdateEvent e) {
        if (enabled) {
            Integer[] jump = {30, 31, 35, 37, 39, 41, 43, 45, 50, 52, 54, 56, 59, 61, 63, 66, 68, 71, 73, 76, 78, 81};
            Integer[] walk = {34, 36, 38, 40, 42, 44, 49, 51, 53, 55, 58, 60, 62, 65, 67, 69, 72, 74, 77, 79};
            Integer[] both = {33, 47};
            Integer[] not = {32, 46, 48, 57, 64, 70, 75, 80};

            int maxDistance = 256;
            EntityPlayer p = e.entity;

            ItemStack itemHand = p.inventory.getCurrentItem();
            if(!(itemHand == null)) {
                if (itemHand.getItem() == Item.getByNameOrId("web")) {
                    MovingObjectPosition mop = p.rayTrace(maxDistance, 1.0f);

                    int blockx = mop.getBlockPos().getX();
                    int blocky = mop.getBlockPos().getY();
                    int blockz = mop.getBlockPos().getZ();

                    int playerx = p.getPosition().getX();
                    int playery = p.getPosition().getY();
                    int playerz = p.getPosition().getZ();

                    int maxmlg = 25;
                    int blockdistanceX = playerx - blockx;
                    int blockdistanceZ = playerz - blockz;

                    if (Math.abs(blockdistanceX) < maxmlg && Math.abs(blockdistanceZ) < maxmlg) {
                        if (playery > blocky) {
                            int distancewrong = playery - blocky;
                            int distance = distancewrong - 1;
                            if (distance > 30) {
                                if (Arrays.asList(jump).contains(distance)) {
                                    mlg = jumpS;
                                } else if (Arrays.asList(walk).contains(distance)) {
                                    mlg = walkS;
                                } else if (Arrays.asList(both).contains(distance)) {
                                    mlg = bothS;
                                } else if (Arrays.asList(not).contains(distance)) {
                                    mlg = notS;
                                } else {
                                    mlg = notS;
                                }
                            } else if (distance >= 10) {
                                mlg = bothS;
                            } else {
                                mlg = "";
                            }
                        } else {
                            mlg = " ";
                        }
                    } else {
                        mlg = " ";
                    }
                } else {
                    mlg = this.nowebS;
                }
            } else {
                mlg = this.nowebS;
            }
        } else {
            mlg = this.disabledS;
        }
    }
}
