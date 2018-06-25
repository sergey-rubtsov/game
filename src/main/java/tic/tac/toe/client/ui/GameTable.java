package tic.tac.toe.client.ui;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tic.tac.toe.client.service.ClientService;
import tic.tac.toe.client.service.TurnListener;
import tic.tac.toe.server.GameConstants;
import tic.tac.toe.server.message.CellMessage;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//this is a customized table
//we need it to display game field's content
public class GameTable extends JTable implements TurnListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameTable.class);

    public GameTable(int size, ClientService clientService) {
        int cellSize = (MainFrame.WIDTH) / size;
        //we need this customized TableModel here to disable editing of table cell by double-click of mouse
        TableModel model = new TableModel(size, size);
        setModel(model);
        //in cycle we set the size of columns and add our customized BoardTableCellRenderer into each column
        for (int i = 0; i < size; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            BoardTableCellRenderer renderer = new BoardTableCellRenderer();
            column.setCellRenderer(renderer);
            column.setMinWidth(cellSize);
            column.setMaxWidth(cellSize);
            column.setPreferredWidth(cellSize);
        }
        //here we set rows height to make cells dimensions equals
        setRowHeight(cellSize);
        JTable table = this;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                LOGGER.debug("mouse clicked, row " + row + " col " + col);
                clientService.sendTurn(row, col);
            }
        });
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.getModel().setValueAt(GameConstants.EMPTY_CELL, i, j);
            }
        }
    }

    public void receiveTurn(int x, int y, Character mark) {
        this.getModel().setValueAt(mark, x, y);
    }

    public void highlightCells(List<CellMessage> row) {
        row.forEach(cell -> {
            BoardTableCellRenderer tableCellRenderer =(BoardTableCellRenderer)
                    (getCellRenderer(cell.getX(), cell.getY()));
            tableCellRenderer.setCellColor(Color.RED);
            tableCellRenderer.setCells(row);
        });
        validate();
        repaint();
    }

}

