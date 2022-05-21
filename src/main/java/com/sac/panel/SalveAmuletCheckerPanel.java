package com.sac.panel;

import com.sac.SalveAmuletCheckerConfig;
import com.sac.SalveAmuletCheckerPlugin;
import com.sac.enums.EntityNames;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
@Slf4j
public class SalveAmuletCheckerPanel extends PluginPanel {


    private JComboBox monsterDropDown;
    private JTextField activeMonsterTextField;
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

            monsterDropDown = new JComboBox<ComboBoxIconEntity<String>>();
            monsterDropDown.setFocusable(false);
            val values = EntityNames.values();
            monsterDropDown.setMaximumRowCount(values.length);
            monsterDropDown.setForeground(Color.WHITE);
            final ComboBoxIconListRenderer renderer = new ComboBoxIconListRenderer();
            renderer.setDefaultText("See Monsters");
            monsterDropDown.setRenderer(renderer);

            for(EntityNames mobName : values){

                final String imageName = mobName.getEntityName().replaceAll("\\s","").concat("Small.png");
                final BufferedImage dropDownIcon = ImageUtil.loadImageResource(SalveAmuletCheckerPlugin.class,imageName );
                final ComboBoxIconEntity<String> entity = new ComboBoxIconEntity(new ImageIcon(dropDownIcon),mobName.getEntityName(),imageName);
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
        catch (Exception ex){
            ex.printStackTrace();
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
