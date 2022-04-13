package com.sac.panel;
import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.constants.EntityNames;
import lombok.extern.slf4j.Slf4j;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@Slf4j
public class SalveAmuletCheckerPanel extends PluginPanel {


    private JComboBox monsterDropDown;
    private JTextField activeMonsterTextField;
    private Constructor<EntityNames> EntityNamesConstructor;
    private final JLabel overallIcon = new JLabel();


    @Inject
    public SalveAmuletCheckerPanel(SalveAmuletCheckerConfig config)  {
        super(true);
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new GridBagLayout());
        loadMonsterDropDown();
    }


    private void loadMonsterDropDown(){
        JPanel monsterDropDownPanel = new JPanel();
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
            renderer.setDefaultText("See Monsters");
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
                    setActiveMonster(mob.getText(),false);
                }
            });


            monsterDropDown.setSelectedIndex(-1);
            monsterDropDownPanel.add(monsterDropDown,BorderLayout.NORTH);
            add(monsterDropDownPanel);
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

    private void loadActiveMonsterTextField(){
        final JPanel activeMonsterPanel = new JPanel();
        activeMonsterPanel.setLayout(new GridLayout(1, 1));
        activeMonsterTextField = new JTextField();
        activeMonsterTextField.setEditable(false);
        activeMonsterTextField.setText("No Active Monster");
        activeMonsterTextField.setFocusable(false);
        activeMonsterTextField.setForeground(Color.WHITE);
        activeMonsterPanel.add(activeMonsterTextField,BorderLayout.CENTER);
        add(activeMonsterPanel);

    }

    public String getActiveMonster(){
        ComboBoxIconEntity<String> activeMonster = (ComboBoxIconEntity<String>) monsterDropDown.getSelectedItem();
        return activeMonster.getText();
    }

    public void loadHeaderIcon(BufferedImage img) {
        overallIcon.setIcon(new ImageIcon(img));
    }

    public void setActiveMonster(String activeMonster, boolean isDropDownChanged){
        if(!isDropDownChanged){
            return;
        }

        for (int i = 0; i < monsterDropDown.getItemCount(); i++){
            ComboBoxIconEntity<String> entity = (ComboBoxIconEntity<String>) monsterDropDown.getItemAt(i);
            if(entity.getText() == activeMonster){
                monsterDropDown.setSelectedIndex(i);
                return;
            }
        }

    }

}
