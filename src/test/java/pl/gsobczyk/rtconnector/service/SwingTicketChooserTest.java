package pl.gsobczyk.rtconnector.service;
import java.util.List;

import javax.swing.UnsupportedLookAndFeelException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.ui.SwingTicketChooser;

import com.google.common.collect.Lists;


public class SwingTicketChooserTest {

	private SwingTicketChooser dialog;
	@Before
	public void before() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		dialog = new SwingTicketChooser();
		dialog.postContruct();
	}

	@Test
	@Ignore("wymaga reakcji użytkownika")
	public void shouldSelectTicket() throws Exception {
		// given
		Ticket t1 = new Ticket();
		t1.setName("t1");
		Ticket t2 = new Ticket();
		t2.setName("t2 lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
		t2.setProject("mig/2008-mig");
		t2.setQueue("driw");
		t2.setId(20L);
		List<Ticket> tickets = Lists.newArrayList(t2, t1);

		// when
		Ticket choosen = dialog.chooseBestTicket(tickets, "ticketQuery");

		// then
		Assert.notNull(choosen);
	}
}
