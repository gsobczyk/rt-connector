package pl.gsobczyk.rtconnector.ui;
import java.util.List;

import javax.swing.JOptionPane;

import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.service.TicketChooser;

public class SwingTicketChooser implements TicketChooser {
	
	@Override
	public Ticket chooseBestTicket(List<Ticket> tickets, String ticketQuery) {
		if (tickets.size()==1){
			return tickets.iterator().next();
		}
		Object[] possibleValues = tickets.toArray();
		Ticket result = null;
		int tries = 0;
		while (result==null && tries++<3){
			result = (Ticket) JOptionPane.showInputDialog(
							null,
							Messages.getString("SwingTicketChooser.message", ticketQuery), 
							Messages.getString("SwingTicketChooser.title"), 
							JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]); //$NON-NLS-1$
		}
		return result;
	}

}
