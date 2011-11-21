package pl.gsobczyk.rtconnector.ui;

import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

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
	private final List<TicketEntry> tableData;
	private final JTextArea taskOutput;
	private final RTService rtService;
	private final TimeUnit timeUnit;
	private final String comment;
	
	TicketReporter(List<TicketEntry> data, TimeUnit timeUnit, String comment, JTextArea taskOutput, RTService rtService){
		this.tableData = data;
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
	    	taskOutput.append("Zalogowano, następuje raportowanie z komentarzem: "+comment+NEW_LINE);
			int step=1;
			setProgress(100/steps*step++);
			for (TicketEntry row : tableData) {
				String ticketQuery = row.getTicket();
				BigDecimal interval = row.getInterval();
				int minutes = timeUnit.getMinutes(interval);
				if (StringUtils.hasText(ticketQuery) && minutes>0){
					try {
						taskOutput.append("Raportowanie: "+ticketQuery+NEW_LINE);
						Ticket result = rtService.addTime(ticketQuery, minutes, comment);
						taskOutput.append(MessageFormat.format("Zaraportowano ticket: {0}, minut: {1}"+NEW_LINE, result, minutes));
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
