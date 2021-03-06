package main.java.de.voidtech.alison.commands;

import java.awt.Color;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import main.java.de.voidtech.alison.GlobalConstants;
import main.java.de.voidtech.alison.annotations.Command;
import main.java.de.voidtech.alison.service.WordService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Command
public class InfoCommand extends AbstractCommand {
	
	@Autowired
	private WordService wordService;
	
	@Override
	public void execute(Message message, List<String> args) {
		long guildCount = message.getJDA().getGuildCache().size();
		long memberCount = message.getJDA().getGuildCache().stream().mapToInt(Guild::getMemberCount).sum();
		long wordCount = wordService.getTotalWords();
		
		MessageEmbed informationEmbed = new EmbedBuilder()
				.setColor(Color.ORANGE)
				.setTitle("ALISON - Automatic Learning Intelligent Sentence Organising Network", GlobalConstants.REPO_URL)
				.addField("Guild Count", "```" + guildCount + "```", true)
				.addField("Member Count", "```" + memberCount + "```", true)
				.addField("Active Threads", "```" + Thread.activeCount() + "```", true)
				.addField("Total Word Count", "```" + wordCount + "```", true)
				.setDescription("**Important Privacy Notice**\n"
						+ "Data collected by ALISON is only available whilst you are opted in to the data collection program."
						+ " To stop data collection, use the optout command."
						+ " Once you are opted out, you can still use ALISON, but messages you send will not be processed or persisted.")
				.setThumbnail(message.getJDA().getSelfUser().getAvatarUrl())
				.setFooter("Use the help command to see what else I can do!\nVersion: " + GlobalConstants.VERSION,
						message.getJDA().getSelfUser().getAvatarUrl())
				.build();
		message.replyEmbeds(informationEmbed).mentionRepliedUser(false).queue();
	}

	@Override
	public String getDescription() {
		return "Provides information about ALISON";
	}

	@Override
	public String getUsage() {
		return "info";
	}

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getShortName() {
		return "inf";
	}
	
	@Override
	public boolean isHidden() {
		return false;
	}
}

