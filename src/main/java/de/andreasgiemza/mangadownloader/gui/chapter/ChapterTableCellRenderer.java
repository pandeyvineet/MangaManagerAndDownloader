package de.andreasgiemza.mangadownloader.gui.chapter;

import de.andreasgiemza.mangadownloader.data.Chapter;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        Chapter chapter = ((ChapterTableModel) table.getModel())
                .getChapterAt(table.convertRowIndexToModel(row));

        if (chapter.isAlreadyDownloaded()) {
            c.setBackground(Color.decode("#D2D2D2"));
        } else {
            c.setBackground(UIManager.getColor("Table.background"));
        }

        return c;
    }
}
