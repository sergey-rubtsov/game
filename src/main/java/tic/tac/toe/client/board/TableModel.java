package tic.tac.toe.client.board;

import javax.swing.table.DefaultTableModel;

//we need this customized TableModel here to disable editing of table cell by double-click of mouse
public class TableModel extends DefaultTableModel {

    public TableModel(int i, int j) {
        super(i, j);
    }

    public boolean isCellEditable(int row, int cols) {
        return false;
    }

}
