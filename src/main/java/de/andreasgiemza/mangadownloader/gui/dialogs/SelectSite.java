package de.andreasgiemza.mangadownloader.gui.dialogs;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import de.andreasgiemza.mangadownloader.MangaDownloader;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.data.MangaList;
import de.andreasgiemza.mangadownloader.gui.site.SiteTableModel;
import de.andreasgiemza.mangadownloader.sites.Site;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SelectSite extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private final java.awt.Frame parentFrame;

	public SelectSite(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.parentFrame = parent;
		initComponents();
		setLocation(parent.getX() + (parent.getWidth() / 2) - (getWidth() / 2),
				parent.getY() + (parent.getHeight() / 2) - (getHeight() / 2));

		sitesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {
					Site selectedSite = ((SiteTableModel) sitesTable.getModel())
							.getSite(sitesTable.convertRowIndexToModel(row));

					((MangaDownloader) parentFrame).loadManga(selectedSite);

					dispose();
				}
			}
		});
	}

	private Site getSelectedSite() {
		int selectedRow = sitesTable.getSelectedRow();

		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(parentFrame, "Please select a site!",
					"Info", JOptionPane.INFORMATION_MESSAGE);

			return null;
		}

		return ((SiteTableModel) sitesTable.getModel()).getSite(sitesTable
				.convertRowIndexToModel(selectedRow));
	}

	private void initComponents() {

		sitesScrollPane = new javax.swing.JScrollPane();
		sitesTable = new javax.swing.JTable();
		selectButton = new javax.swing.JButton();
		updateButton = new javax.swing.JButton();
		updateAllButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		sitesTable.setAutoCreateRowSorter(true);
		sitesTable.setModel(new SiteTableModel());
		sitesTable
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		sitesScrollPane.setViewportView(sitesTable);

		selectButton.setText("Select");
		selectButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectButtonActionPerformed(evt);
			}
		});

		updateButton.setText("Update");
		updateButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateButtonActionPerformed(evt);
			}
		});

		updateAllButton.setText("Update all");
		updateAllButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateAllButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														sitesScrollPane,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														620, Short.MAX_VALUE)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		selectButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		67,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		updateButton)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		updateAllButton)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		cancelButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		67,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(sitesScrollPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										424, Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(cancelButton)
												.addComponent(selectButton)
												.addComponent(updateButton)
												.addComponent(updateAllButton))
								.addContainerGap()));

		pack();
	}

	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Site selectedSite = getSelectedSite();

		if (selectedSite == null) {
			return;
		}

		if (MangaList.getLastListUpdate(selectedSite) == null) {
			JOptionPane.showMessageDialog(parentFrame,
					"Please update site before continuing!", "Info",
					JOptionPane.INFORMATION_MESSAGE);

			return;
		}

		((MangaDownloader) parentFrame).loadManga(selectedSite);

		dispose();
	}

	private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
		final Site selectedSite = getSelectedSite();

		if (selectedSite == null) {
			return;
		}

		final Loading loading = new Loading(parentFrame, true, getX(), getY(),
				getWidth(), getHeight());
		loading.startRunnable(new Runnable() {

			@Override
			public void run() {
				List<Manga> mangas;

				try {
					mangas = selectedSite.getMangaList();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(loading, "Cant't connect to "
							+ selectedSite.getName() + "!", "Error",
							JOptionPane.ERROR_MESSAGE);

					loading.dispose();
					return;
				}

				// Save data to file
				MangaList.save(selectedSite, mangas);

				loading.dispose();

				((SiteTableModel) sitesTable.getModel()).fireTableDataChanged();

				int selectedRow = sitesTable
						.convertRowIndexToView(((SiteTableModel) sitesTable
								.getModel()).getIndexOf(selectedSite));
				sitesTable.setRowSelectionInterval(selectedRow, selectedRow);
			}
		});
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void updateAllButtonActionPerformed(java.awt.event.ActionEvent evt) {
		final List<Site> sites = ((SiteTableModel) sitesTable.getModel())
				.getSites();

		if (sites == null) {
			return;
		}

		final Loading loading = new Loading(parentFrame, true, getX(), getY(),
				getWidth(), getHeight());
		loading.startRunnable(new Runnable() {

			@Override
			public void run() {
				String errors = "";

				for (Site site : sites) {
					List<Manga> mangas;

					try {
						mangas = site.getMangaList();

						// Save data to file
						MangaList.save(site, mangas);
					} catch (Exception ex) {
						errors += "Cant't connect to " + site.getName() + "!\n";
					}
				}

				if (!errors.isEmpty()) {
					JOptionPane.showMessageDialog(loading, errors, "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				loading.dispose();

				((SiteTableModel) sitesTable.getModel()).fireTableDataChanged();
			}
		});
	}

	private javax.swing.JButton cancelButton;
	private javax.swing.JButton selectButton;
	private javax.swing.JScrollPane sitesScrollPane;
	private javax.swing.JTable sitesTable;
	private javax.swing.JButton updateAllButton;
	private javax.swing.JButton updateButton;
}
