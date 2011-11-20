package pl.gsobczyk.rtconnector.ui;

import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.util.StringUtils;

import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.service.QuerySyntaxException;
import pl.gsobczyk.rtconnector.service.RTService;

public class TicketReporter extends SwingWorker<Void, Void> {
	private static final String NEW_LINE = "\n";
	private final Vector<Vector<?>> tableData;
	private final JTextArea taskOutput;
	private final RTService rtService;
	private final TimeUnit timeUnit;
	private final String comment;
	
	TicketReporter(Vector<Vector<?>> tableData, TimeUnit timeUnit, String comment, JTextArea taskOutput, RTService rtService){
		this.tableData = tableData;
		this.timeUnit = timeUnit;
		this.comment = comment;
		this.taskOutput = taskOutput;
		this.rtService = rtService;
	}
	
    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
    	try {
	    	int steps = tableData.size()+1;
	    	taskOutput.append("Logowanie do RT"+NEW_LINE);
			rtService.login();
	    	taskOutput.append("Zalogowano"+NEW_LINE);
			int step=1;
			setProgress(100/steps*step++);
			for (Vector<?> row : tableData) {
				String ticketQuery = (String) row.get(0);
				BigDecimal interval = (BigDecimal) row.get(1);
				int minutes = timeUnit.getMinutes(interval);
				if (StringUtils.hasText(ticketQuery) && minutes>=0){
					try {
						taskOutput.append("Raportowanie: "+ticketQuery+NEW_LINE);
						Ticket result = rtService.addTime(ticketQuery, minutes, comment);
						taskOutput.append("Zaraportowano ticket: "+result+NEW_LINE);
					} catch (QuerySyntaxException e1) {
						JOptionPane.showMessageDialog(null, e1.getLocalizedMessage(), "Błąd!", JOptionPane.ERROR_MESSAGE);
					}
				}
				setProgress(100/steps*step++);
			}
			taskOutput.append("Zaraportowano wszytkie tickety"+NEW_LINE);
    	} catch (Exception e) {
			taskOutput.append(ExceptionUtils.getStackTrace(e));
		}
		setProgress(100);
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
        setProgress(100);
        taskOutput.append("Done!\n");
    }
}
