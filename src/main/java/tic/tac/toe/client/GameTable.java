package tic.tac.toe.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//this is a customized table
//we need it to display game field's content
public class GameTable extends JTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameTable.class);

    private Character[][] cells;

    public GameTable() {
        //this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        this.cells = getCells();
        //we need this customized TableModel here to disable editing of table cell by double-click of mouse
        TableModel model = new TableModel(cells.length, cells[0].length);
        setModel(model);
        //in cycle we set the size of columns and add our customized BoardTableCellRenderer into each column
        for (int i = 0; i < cells.length; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            BoardTableCellRenderer renderer = new BoardTableCellRenderer();
            column.setCellRenderer(renderer);
            column.setMinWidth(Client.CELL_SIZE);
            column.setMaxWidth(Client.CELL_SIZE);
            column.setPreferredWidth(Client.CELL_SIZE);
        }
        //this method to set our Cell objects into each cell of table model
        refreshTable(cells);
        //here we set rows height to make cells dimensions equals
        setRowHeight(Client.CELL_SIZE);
        JTable table = this;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    LOGGER.info(row + " " + col);
                }
            }
        });
    }



    //set our Cell objects into each cell of table model in cycle
    //later our Cell objects will be rendered into another place
    public void refreshTable(Character[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                this.getModel().setValueAt(cells[i][j], i, j);
            }
        }
    }

    public static Character[][] getCells() {
        Character[][] cells = new Character[10][10];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = ' ';
            }
        }
        return cells;
    }

}

