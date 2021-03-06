package main.java.de.voidtech.alison.util;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public class ButtonConsumer {
	
	private Message message;
	private ButtonClickEvent button;
	
	public ButtonConsumer(ButtonClickEvent button, Message message) {
		this.message = message;
		this.button = button;
	}
	
	public Message getMessage() {
		return this.message;
	}
	
	public ButtonClickEvent getButton() {
		return this.button;
	}
}
