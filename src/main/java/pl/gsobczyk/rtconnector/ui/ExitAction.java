package pl.gsobczyk.rtconnector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExitAction extends WindowAdapter implements ActionListener {
	@Autowired private AbstractApplicationContext applicationContext;

	@Override
	public void actionPerformed(ActionEvent e) {
		applicationContext.close();
		System.exit(0);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		applicationContext.close();
		super.windowClosing(e);
	}

}
