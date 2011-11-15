package pl.gsobczyk.rtconnector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExitAction implements ActionListener {
	@Autowired private AbstractApplicationContext applicationContext;

	@Override
	public void actionPerformed(ActionEvent e) {
		applicationContext.close();
		System.exit(0);
	}

}
