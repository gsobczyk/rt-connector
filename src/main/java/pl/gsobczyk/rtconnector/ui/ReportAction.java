package pl.gsobczyk.rtconnector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ReportAction implements ActionListener{
	@Autowired @Qualifier("tableHolder")
	private ComponentHolder<JTable> tableHolder;
	@Autowired @Qualifier("comboBoxHolder")
	private ComponentHolder<JComboBox> comboBoxHolder;
	@Autowired @Qualifier("commentHolder")
	private ComponentHolder<JTextField> txtCommentHolder;
	@Autowired private ProgressBarReporter progressBarReporter;

	
	@SuppressWarnings("unchecked" )
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean correct = true;
		for (int x=0; x<tableHolder.get().getRowCount(); x++) {
			if (tableHolder.get().getValueAt(x, 1)!=null){
				tableHolder.get().editCellAt(x, 1);
				correct&=tableHolder.get().getCellEditor(x, 1).stopCellEditing();
			}
		}
		if (correct){
			TimeUnit unit = (TimeUnit) comboBoxHolder.get().getSelectedItem();
			DefaultTableModel model =  (DefaultTableModel) tableHolder.get().getModel();
			int result = JOptionPane.showConfirmDialog(null, "Pola uzupełniono poprawnie.\nKontynuować raportowanie?", "Pytanie", JOptionPane.YES_NO_OPTION);
			if (result==0){
				List<TicketEntry> data = Lists.newArrayList();
				for (Vector<?> entry : (Vector<Vector<?>>)model.getDataVector()) {
					data.add(new TicketEntry((String)entry.get(0), (BigDecimal)entry.get(1)));
				}
				progressBarReporter.report(data, unit, txtCommentHolder.get().getText());
//				addTimes(unit, model.getDataVector());
			}
		}
	}

//	public void addTimes(TimeUnit unit, Vector<Vector<?>> tableData) {
//		rtService.login();
//		for (Vector<?> row : tableData) {
//			String ticketQuery = (String) row.get(0);
//			BigDecimal interval = (BigDecimal) row.get(1);
//			int minutes = unit.getMinutes(interval);
//			if (StringUtils.hasText(ticketQuery) && minutes>0){
//				try {
//					rtService.addTime(ticketQuery, minutes, txtCommentHolder.get().getText());
//				} catch (QuerySyntaxException e1) {
//					JOptionPane.showMessageDialog(null, e1.getLocalizedMessage(), "Błąd!", JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		}
//	}

}
