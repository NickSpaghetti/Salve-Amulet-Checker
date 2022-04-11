package com.sac.panel;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.constants.EntityNames;
import com.sac.state.PanelState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class SalveAmuletCheckerPanel extends PluginPanel {


    private JComboBox monsterDropDown;
    private Constructor<EntityNames> EntityNamesConstructor;
    private final JLabel overallIcon = new JLabel();


    public SalveAmuletCheckerPanel()  {
        super(true);

        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new GridBagLayout());

        /* Setup overview panel */
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setLayout(new BorderLayout());

        JLabel title = new JLabel();
        title.setText("Salve Amulet Checker");
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.WEST);

        //loads the monster dropdown
        loadMonsterDropDown();


        final JPanel overallInfo = new JPanel();
        overallInfo.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        overallInfo.setLayout(new GridLayout(3, 1));
        overallInfo.setBorder(new EmptyBorder(2, 10, 2, 0));
    }


    private void loadMonsterDropDown(){
        try{
            Field[] fields = EntityNames.class.getFields();
            EntityNamesConstructor = EntityNames.class.getDeclaredConstructor();
            EntityNamesConstructor.setAccessible(true);
            EntityNames entityNames = EntityNamesConstructor.newInstance();
            ArrayList<String> values = new ArrayList<String>();

            for(Field field : fields){
                values.add(field.get(entityNames).toString());
            }

            monsterDropDown = new JComboBox<ComboBoxIconEntity<String>>();
            monsterDropDown.setFocusable(false);

            monsterDropDown.setMaximumRowCount(values.size());
            monsterDropDown.setForeground(Color.WHITE);
            final ComboBoxIconListRenderer renderer = new ComboBoxIconListRenderer();
            renderer.setDefaultText("Select a Monster");
            monsterDropDown.setRenderer(renderer);

            for(String mobName : values){

                final String imageName = mobName.replaceAll("\\s","").concat("Small.png");
                final BufferedImage dropDownIcon = ImageUtil.loadImageResource(SalveAmuletCheckerPlugin.class,imageName );
                final ComboBoxIconEntity<String> entity = new ComboBoxIconEntity(new ImageIcon(dropDownIcon),mobName,imageName);
                monsterDropDown.addItem(entity);

            }

            monsterDropDown.addItemListener(e ->
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    final ComboBoxIconEntity mob = (ComboBoxIconEntity) e.getItem();
                    PanelState.CurrentMonsterSelected = mob.getText();
                }
            });

            monsterDropDown.setSelectedIndex(-1);

            add(monsterDropDown);
        }
        catch (IllegalAccessException iaex){
            iaex.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void loadHeaderIcon(BufferedImage img) {
        overallIcon.setIcon(new ImageIcon(img));
    }

}
