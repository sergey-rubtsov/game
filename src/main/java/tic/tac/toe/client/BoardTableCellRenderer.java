package tic.tac.toe.client;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

//Standard JTable contains interface TableCellRenderer class to render table cells by default
//We have to implement it to render our cells as we need
public class BoardTableCellRenderer implements TableCellRenderer {


    public BoardTableCellRenderer() {
    }

    //The table model calls this method every time when it builds cell
    //Here we decide how to display them
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        //other cells of table must contain Cell objects, we set them before
        //and we handle them in that method:
        JButton button = new JButton(String.valueOf(value));
        //button.setFont(new Font("WalkwayBold", Font.TRUETYPE_FONT, 12));
        button.setBackground(Color.WHITE);
        return button;
    }

}