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
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel textLabel = new JLabel("SELECT PIECE TO PROMOTE");
        JButton selectQueen = new JButton("QUEEN");
        JButton selectRook = new JButton("ROOK");
        JButton selectBishop = new JButton("BISHOP");
        JButton selectKnight = new JButton("KNIGHT");
        selectQueen.addActionListener(new PromotionListener(this, promotionFile, promotionRank, boardPanel, Piece.Type.QUEEN, color));
        selectRook.addActionListener(new PromotionListener(this, promotionFile, promotionRank, boardPanel, Piece.Type.ROOK, color));
        selectBishop.addActionListener(new PromotionListener(this, promotionFile, promotionRank, boardPanel, Piece.Type.BISHOP, color));
        selectKnight.addActionListener(new PromotionListener(this, promotionFile, promotionRank, boardPanel, Piece.Type.KNIGHT, color));
        textPanel.add(textLabel);
        selectPanel.add(selectQueen);
        selectPanel.add(selectRook);
        selectPanel.add(selectBishop);
        selectPanel.add(selectKnight);
        add(textPanel, BorderLayout.CENTER);
        add(selectPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
