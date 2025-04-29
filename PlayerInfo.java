import javax.swing.*;
import java.awt.*;

public class PlayerInfo {
    public JPanel PlayerInfo(Player player, String side) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Color green = new Color(25, 70, 26);
        Color white = new Color(199, 199, 204);

        if(side.equals("l")) {
            JLabel name = new JLabel(player.getName());
            c.gridx = 0;
            c.gridy = 0;
            panel.add(name, c);
            name.setForeground(white);

            JLabel money = new JLabel("$"+player.getMoney());
            c.gridy = 1;
            panel.add(money, c);
            money.setForeground(white);

            JLabel image = new JLabel();
            String path = System.getProperty("user.dir")+"\\person.png";
            ImageIcon icon = new ImageIcon(path);
            image.setIcon(icon);
            c.gridheight = 2;
            c.gridx = 0;
            c.gridy = 1;
            panel.add(image, c);
        }
        if(side.equals("r")) {
            JLabel name = new JLabel(player.getName());
            c.gridx = 1;
            c.gridy = 0;
            panel.add(name, c);
            name.setForeground(white);

            JLabel money = new JLabel("$"+player.getMoney());
            c.gridy = 1;
            panel.add(money, c);
            money.setForeground(white);

            JLabel image = new JLabel();
            String path = System.getProperty("user.dir")+"\\person.png";
            ImageIcon icon = new ImageIcon(path);
            image.setIcon(icon);
            c.gridheight = 2;
            c.gridx = 0;
            c.gridy = 0;
            panel.add(image, c);
        }

        panel.setBackground(green);
        return panel;
    }
}