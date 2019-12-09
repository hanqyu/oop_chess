package ui;

import pieces.Piece;

import javax.swing.*;
import java.awt.*;

public class PromotionFrame extends JFrame {
    public PromotionFrame(char promotionFile, int promotionRank, BoardPanel boardPanel, Piece.Color color){
        setTitle("Promotion Select");
        setSize(400,200);
        setLayout(new BorderLayout());
        JPanel textPanel = new JPanel();
        JPanel textPanel2 = new JPanel();
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel textLabel = new JLabel("SELECT PIECE TO PROMOTE");
        JLabel textLabel2 = new JLabel("CLOSE THIS WINDOW MANUALLY AFTER CHOOSING PIECE");
        JButton selectQueen = new JButton("QUEEN");
        JButton selectRook = new JButton("ROOK");
        JButton selectBishop = new JButton("BISHOP");
        JButton selectKnight = new JButton("KNIGHT");
        selectQueen.addActionListener(new PromotionListener(promotionFile, promotionRank, boardPanel, Piece.Type.QUEEN, color));
        selectRook.addActionListener(new PromotionListener(promotionFile, promotionRank, boardPanel, Piece.Type.ROOK, color));
        selectBishop.addActionListener(new PromotionListener(promotionFile, promotionRank, boardPanel, Piece.Type.BISHOP, color));
        selectKnight.addActionListener(new PromotionListener(promotionFile, promotionRank, boardPanel, Piece.Type.KNIGHT, color));
        textPanel.add(textLabel);
        textPanel2.add(textLabel2);
        selectPanel.add(selectQueen);
        selectPanel.add(selectRook);
        selectPanel.add(selectBishop);
        selectPanel.add(selectKnight);
        add(textPanel, BorderLayout.NORTH);
        add(textPanel2, BorderLayout.SOUTH);
        add(selectPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
