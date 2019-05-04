package radio.exp.stream;

import java.io.*;

/**
 * 存储与恢复游戏任务-对象流（用于存储和恢复数据）
 *
 * @author wzy
 */
public class GameSaverTest implements Serializable {
    public static void main(String[] args) {
        //创建人物
        GameCharacter one = new GameSaverTest().new GameCharacter(50, "Elf", new String[]{"bow", "sword", "dust"});
        GameCharacter two = new GameSaverTest().new GameCharacter(200, "Troll", new String[]{"bare hands", "big ax"});
        GameCharacter three = new GameSaverTest().new GameCharacter(200, "Magician", new String[]{"spells", "invisibility"});

        try {
            //未指定时会自动生成在项目根目录下
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream("Game.ser"));
            oo.writeObject(one);
            oo.writeObject(two);
            oo.writeObject(three);
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        one = null;
        two = null;
        three = null;

        try {
            ObjectInputStream oi = new ObjectInputStream(new FileInputStream("Game.ser"));
            try {
                GameCharacter oneStore = (GameCharacter) oi.readObject();
                GameCharacter twoStore = (GameCharacter) oi.readObject();
                GameCharacter threeStore = (GameCharacter) oi.readObject();

                System.out.println("One's type: " + oneStore.getType());
                System.out.println("Two's type: " + twoStore.getType());
                System.out.println("Three's type: " + threeStore.getType());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class GameCharacter implements Serializable {
        int power;
        String type;
        String[] weapons;

        public GameCharacter(int power, String type, String[] weapons) {
            this.power = power;
            this.type = type;
            this.weapons = weapons;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWeapons() {
            StringBuilder weaponList = new StringBuilder();
            for (int i = 0; i < weapons.length; i++) {
                weaponList.append(weapons[i] + " ");
            }
            return weaponList.toString();
        }
    }

}
