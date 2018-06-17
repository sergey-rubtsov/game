package tic.tac.toe.client.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tic.tac.toe.client.Client;
import tic.tac.toe.client.MainFrame;
import tic.tac.toe.client.service.EventListener;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//this is a customized table
//we need it to display game field's content
@Component
public class GameTable extends JTable implements EventListener {

    @Value("${title}")
    String title;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameTable.class);

    private Character[][] cells;

    public GameTable(int size) {
        this.cells = initCells(size);
        int cellSize = (MainFrame.WIDTH - 5) / size;
        //we need this customized TableModel here to disable editing of table cell by double-click of mouse
        TableModel model = new TableModel(cells.length, cells[0].length);
        setModel(model);
        //in cycle we set the size of columns and add our customized BoardTableCellRenderer into each column
        for (int i = 0; i < cells.length; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            BoardTableCellRenderer renderer = new BoardTableCellRenderer();
            column.setCellRenderer(renderer);
            column.setMinWidth(cellSize);
            column.setMaxWidth(cellSize);
            column.setPreferredWidth(cellSize);
        }
        //this method to set our Cell objects into each cell of table model
        refreshTable(cells);
        //here we set rows height to make cells dimensions equals
        setRowHeight(cellSize);
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

    public static Character[][] initCells(int size) {
        Character[][] cells = new Character[size][size];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = ' ';
            }
        }
        return cells;
    }

    public void drawSymbol(int x, int y, Character symbol, Character turn) {
        this.getModel().setValueAt(symbol, x, y);
    }

    public void showWinner(Character winner) {

    }

    public void startGame(Character yourSymbol, Character turn) {

    }
}

