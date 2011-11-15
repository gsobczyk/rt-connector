package pl.gsobczyk.rtconnector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import pl.gsobczyk.rtconnector.service.RTService;

@Component
public class ReportAction implements ActionListener{
	@Autowired @Qualifier("tableHolder")
	private ComponentHolder<JTable> tableHolder;
	@Autowired @Qualifier("comboBoxHolder")
	private ComponentHolder<JComboBox> comboBoxHolder;
	@Autowired private RTService rtService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			for (Vector row : (Vector<Vector<?>>)model.getDataVector()) {
				String ticketQuery = (String) row.get(0);
				BigDecimal interval = (BigDecimal) row.get(1);
				int minutes = unit.getMinutes(interval);
				if (StringUtils.hasText(ticketQuery) && minutes>0){
					JOptionPane.showMessageDialog(null, MessageFormat.format("query: {0}, minuty: {1}!", ticketQuery, minutes));
				}
			}
			JOptionPane.showMessageDialog(null, MessageFormat.format("Pola uzupełniono poprawnie w {0}!", unit));
		}
	}

}
