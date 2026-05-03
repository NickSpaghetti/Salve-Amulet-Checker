# SalveAmuletChecker
Checks which players do not have their Salve Amulet equip at Bloat Or Mystics.

## Settings
[Enable Salve Amulet Check in Cox](#enable-salve-amulet-check-in-cox)

[Enable Salve Amulet Check in Tob](#enable-salve-amulet-check-in-tob)

[Show Location in Chambers of Xeric](#show-location-in-chambers-of-xeric)

[Show Location in Theatre of Blood](#show-location-in-theatre-of-blood)

[Call out players](###call-out-players)

[Remove navigation button from side panel](###navigation-button-visible)

### Enable Salve Amulet Check in Cox
When checked an overlay will show up when you are in the mystic room displaying who yes/no if a player is wearing their salve amulet

![Salve Amulet Checker in Mystics](src/main/resources/com/sac/MysticChecker.png)
### Enable Salve Amulet Check in Tob
When checked an overlay will show up when you are in the Bloat room displaying who yes/no if a player is wearing their salve amulet

![Salve Amulet Checker in Mystics](src/main/resources/com/sac/TobSalveAmuletChecker.png)
### Show Location in Chambers of Xeric
A little overlay box will show up displaying the current room you are in.

![Cox Location](src/main/resources/com/sac/CoxLocation.png)
### Show Location in Theatre of Blood
A little overlay box will show up displaying the current room you are in.

![Cox Location](src/main/resources/com/sac/TobLocation.png)
### Call out players
will call out players in the chat if they are not wearing their salve amulet in bloat & mystic room

### Navigation button visible
When unchecked will remove the Salve Amulet (e) icon from the Navigation side panel.

### Building the Project locally in IntelliJ
1. You must have installed Java 11 Eclipse Temurin (AdoptOpenJDK) installed.
2. In the sidebar navigate to`src/test/java/com/sac/SalveAmuletCheckerPluginTest.java` then right click and Select
`More Run/Debug` => `Modify Run Configuration...`
3. Select `Modify Options` and select `Add VM Options`
4. In VM Options add the following argument `-ea`.
5. Optionally add the following CLI Arguments if you want advance debugging `--developer-mode --debug`.