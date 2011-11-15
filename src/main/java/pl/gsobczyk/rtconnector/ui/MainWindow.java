package pl.gsobczyk.rtconnector.ui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



@Component
public class MainWindow {
	@Autowired private ExitAction exitAction;
	@Autowired private ReportAction reportAction;
	@Autowired @Qualifier("tableHolder")
	private ComponentHolder<JTable> tableHolder;
	@Autowired @Qualifier("comboBoxHolder")
	private ComponentHolder<JComboBox> comboBoxHolder;
	@Autowired @Qualifier("commentHolder")
	private ComponentHolder<JTextField> txtCommentHolder;
	
	private JFrame frmRtConnector;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnClean;
	private JComboBox comboBox;
	private JTable table;
	private JTextField txtComment;

	/**
	 * @wbp.parser.entryPoint
	 */
	@PostConstruct
	public void postConstruct() {
		comboBox=comboBoxHolder.get();
		table=tableHolder.get();
		txtComment=txtCommentHolder.get();
		initialize();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmRtConnector.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRtConnector = new JFrame();
		frmRtConnector.setTitle(Messages.getString("MainWindow.title")); //$NON-NLS-1$
		frmRtConnector.setBounds(100, 100, 450, 300);
		frmRtConnector.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frmRtConnector.getContentPane().setLayout(gridBagLayout);
		
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(7, 7, 7, 7));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmRtConnector.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridheight = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(createEmptyDataModel());
		table.addKeyListener(new ClipboardKeyAdapter(table));
		
		btnClean = new JButton(Messages.getString("MainWindow.clean")); //$NON-NLS-1$
		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setModel(createEmptyDataModel());
			}
		});
		
		comboBox.setModel(new DefaultComboBoxModel(TimeUnit.values()));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		panel.add(comboBox, gbc_comboBox);
		GridBagConstraints gbc_btnClean = new GridBagConstraints();
		gbc_btnClean.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClean.anchor = GridBagConstraints.WEST;
		gbc_btnClean.insets = new Insets(0, 0, 5, 0);
		gbc_btnClean.gridx = 1;
		gbc_btnClean.gridy = 2;
		panel.add(btnClean, gbc_btnClean);
		
		JButton btnReport = new JButton(Messages.getString("MainWindow.report")); //$NON-NLS-1$
		GridBagConstraints gbc_btnReport = new GridBagConstraints();
		gbc_btnReport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnReport.anchor = GridBagConstraints.WEST;
		gbc_btnReport.insets = new Insets(0, 0, 5, 0);
		gbc_btnReport.gridx = 1;
		gbc_btnReport.gridy = 3;
		panel.add(btnReport, gbc_btnReport);
		
		JButton btnExit = new JButton(Messages.getString("MainWindow.exit")); //$NON-NLS-1$
		btnExit.addActionListener(exitAction);
		
		txtComment.setText(Messages.getString("MainWindow.txtComment.text")); //$NON-NLS-1$
		GridBagConstraints gbc_txtComment = new GridBagConstraints();
		gbc_txtComment.insets = new Insets(0, 0, 0, 5);
		gbc_txtComment.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtComment.gridx = 0;
		gbc_txtComment.gridy = 4;
		panel.add(txtComment, gbc_txtComment);
		txtComment.setColumns(10);
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExit.anchor = GridBagConstraints.WEST;
		gbc_btnExit.gridx = 1;
		gbc_btnExit.gridy = 4;
		panel.add(btnExit, gbc_btnExit);
		btnReport.addActionListener(reportAction);
	}

	public DefaultTableModel createEmptyDataModel() {
		return new DefaultTableModel(
			new Object[][] {{null, null}},
			new String[] {
				Messages.getString("MainWindow.ticketColumn"), Messages.getString("MainWindow.timeColumn") //$NON-NLS-1$ //$NON-NLS-2$
			}
			
		){
			private static final long serialVersionUID = 2214700486786076489L;
			Class<?>[] columnTypes = new Class[] {
				String.class, BigDecimal.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			};
		};
	}

}
