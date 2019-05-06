package radio.exp.stream.QuizCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * 加载卡牌呢数据
 *
 * @author wzy
 */
public class QuizCardPlayer {

    private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String[] args) {
        QuizCardPlayer player = new QuizCardPlayer();
        player.go();
    }

    public void go() {
        //创建并显示GUI
        frame = new JFrame("Quiz Card Player");
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroll = new JScrollPane(display);
        qScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nextButton = new JButton("Show Question");
        mainPanel.add(qScroll);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(new OpenMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);
    }

    class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //如果是个问题，显示答案，否则显示下一个问题
            if (isShowAnswer) {
                //显示答案
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next Card");
                //改一个标识表明我们已经浏览了问题或答案
                isShowAnswer = false;
            } else {
                //显示问题
                if (currentCardIndex < cardList.size()) {
                    ShowNextCard();
                } else {
                    //没有更多卡片了
                    display.setText("That was last card");
                    nextButton.setEnabled(false);
                }
            }

        }
    }

    class OpenMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //生成一个文件对话框
            //让用户把一个卡片设置打开
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    private void loadFile(File file) {
        //创建卡片的ArrayList,并从文本文件中去读取它们
        //调用OpenMenuListener事件处理器，每次从文件中读取一行
        //告诉makeCard()方法创建一个新卡片
        cardList = new ArrayList<QuizCard>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read the card file");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Couldn't read the card file");
            e.printStackTrace();
        }

    }

    private void makeCard(String lineToParse) {
        //调用loadFile方法，从文本文件中读取一行
        //创建一个新的QuizCard，通过调用CardList把它加入ArrayList中
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard();
        card.setQuestion(result[0]);
        card.setAnswer(result[1]);
        cardList.add(card);
        System.out.println("made a card");
    }

    private void ShowNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;
    }
}
