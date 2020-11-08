package net.addon.mlghelper;

import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class MLGmodule extends SimpleModule {
    @Override
    public String getDisplayName() {
        return "MLG";
    }

    @Override
    public String getDisplayValue() {
        return MLGhelper.mlg;
    }

    @Override
    public String getDefaultValue() {
        return "";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.WEB);
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getControlName() {
        return "MLG Helper";
    }

    @Override
    public String getSettingName() {
        return "MLG Helper";
    }

    @Override
    public String getDescription() {
        return "MLG-Helper Module";
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    public ModuleCategory getCategory() {
        return ModuleCategoryRegistry.CATEGORY_OTHER;
    }

    @Override
    public void draw(double x, double y, double rightX) {
        if(MLGhelper.enabled == true) {
            super.draw(x, y, rightX);
        }
    }
}
