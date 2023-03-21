import javax.swing.table.AbstractTableModel;

@interface ColumnNames
{
    String[] names();
    int[] order();
}

public abstract class GenericTable extends AbstractTableModel {

    @ColumnNames(
    names = {"name", "surname", "studiesYear", "birth", "points"},
    order = {0,1,2,3,4})
    public String[] col_names = {"name", "surname", "studiesYear", "birth", "points"};

    @Override
    public int getColumnCount()
    {
        return 5;
    }

    @Override
    public String getColumnName(int col) {
        return col_names[col];
    }

}
