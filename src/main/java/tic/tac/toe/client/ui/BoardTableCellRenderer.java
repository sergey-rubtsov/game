package tic.tac.toe.client.ui;

import tic.tac.toe.server.message.CellMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


//Standard JTable contains interface TableCellRenderer class to render table cells by default
//We have to implement it to render our cells as we need
public class BoardTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private Color color;

    private List<CellMessage> cells = new ArrayList<>();

    public BoardTableCellRenderer() {
    }

    //The table model calls this method every time when it builds cell
    //Here we decide how to display them
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        //other cells of table must contain Cell objects, we set them before
        //and we handle them in that method:
        JButton button = new JButton(String.valueOf(value));
        if (!cells.isEmpty() && cells.stream().anyMatch(c -> (c.getX() == row) && (c.getY() == column))) {
            button.setBackground(color);
        } else {
            button.setBackground(Color.WHITE);
        }
        Font font = button.getFont();
        button.setFont((new Font(font.getName(), Font.PLAIN, table.getRowHeight() / 2)));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return button;
    }

    public void setCellColor(Color color) {
        this.color = color;
    }

    public void setCells(List<CellMessage> cells) {
        this.cells = cells;
    }
}