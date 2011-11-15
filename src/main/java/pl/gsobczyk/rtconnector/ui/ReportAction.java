package pl.gsobczyk.rtconnector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportAction implements ActionListener{

	@Autowired private JTable table;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean correct = true;
		for (int x=0; x<table.getRowCount(); x++) {
			if (table.getValueAt(x, 1)!=null){
				table.editCellAt(x, 1);
				correct&=table.getCellEditor(x, 1).stopCellEditing();
			}
		}
		if (correct){
			JOptionPane.showMessageDialog(null, "Pola uzupełniono poprawnie!");
		}
	}

}
