package main.java.de.voidtech.alison.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;

import org.springframework.beans.factory.annotation.Autowired;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import main.java.de.voidtech.alison.service.WordService;
import main.java.de.voidtech.alison.util.ButtonConsumer;
import main.java.de.voidtech.alison.annotations.Command;

@Command
public class ClearCommand extends AbstractCommand
{
	@Autowired
	private EventWaiter waiter;
	
    @Autowired
    private WordService wordService;
    
	private static final String TRUE_EMOTE = "\u2705";
	private static final String FALSE_EMOTE = "\u274C";
    
	private List<Component> createTrueFalseButtons() {
		List<Component> components = new ArrayList<>();
		components.add(Button.secondary("YES", TRUE_EMOTE));
		components.add(Button.secondary("NO", FALSE_EMOTE));
		return components;
	}
	
	private void getAwaitedButton(Message message, String question, List<Component> actions, Consumer<ButtonConsumer> result) {
        Message msg = message.reply(question).setActionRow(actions).mentionRepliedUser(false).complete();
        waiter.waitForEvent(ButtonClickEvent.class,
                e -> e.getUser().getId().equals(message.getAuthor().getId()),
				e -> result.accept(new ButtonConsumer(e, msg)), 30, TimeUnit.SECONDS,
                () -> message.getChannel().sendMessage("Timed out waiting for reply. Your data has not been erased.").queue());
    }
	
    @Override
    public void execute(final Message message, final List<String> args) {
    	getAwaitedButton(message, "Are you sure you want to delete all your data? **This cannot be undone!**", createTrueFalseButtons(), result -> {
			result.getButton().deferEdit().queue();
			switch (result.getButton().getComponentId()) {
			case "YES":
				wordService.clearUser(message.getAuthor().getId());
				result.getMessage().editMessage("Your data has been cleared! If you want to stop data collection, use the `optout` command!").queue();
				break;
			case "NO":
				result.getMessage().editMessage("Your data has been left alone for now.").queue();
				break;
			}
		});		
    }
    
    @Override
    public String getName() {
        return "clear";
    }

	@Override
	public String getUsage() {
		return "clear";
	}

	@Override
	public String getDescription() {
		return "Allows you to delete all your learnt words. If you want ALISON to stop learning, use the optout command!";
	}

	@Override
	public String getShortName() {
		return "c";
	}

	@Override
	public boolean isHidden() {
		return false;
	}
}
