package ui;

import pieces.Piece.Color;
import pieces.Piece.Type;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PromotionListener implements ActionListener {

    private PromotionFrame promotionFrame;
    BoardPanel boardPanel;
    char promotionFile;
    int promotionRank;
    Type type;
    Color color;

    public PromotionListener(PromotionFrame promotionFrame, char promotionFile, int promotionRank, BoardPanel boardPanel, Type type, Color color){
        this.promotionFrame = promotionFrame;
        this.boardPanel = boardPanel;
        this.promotionFile = promotionFile;
        this.promotionRank = promotionRank;
        this.type = type;
        this.color = color;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        boardPanel.submitPromotionRequest(promotionFile, promotionRank, type, color);
        this.promotionFrame.setVisible(false);
    }
}
