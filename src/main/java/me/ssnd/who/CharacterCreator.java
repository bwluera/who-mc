package me.ssnd.who;

import java.util.Map;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import me.ssnd.who.character.Race;
import me.ssnd.who.config.RaceConfig;
import me.ssnd.who.utils.Common;

public class CharacterCreator {

	public CharacterCreator(Player player) {
		final Conversation convo = new ConversationFactory(Who.getInstance())
				.withModality(true)
				.withPrefix(context -> Common.colorize("&8RP Character Creator | &f"))
				.withTimeout(60)
				.withEscapeSequence("quit")
				.thatExcludesNonPlayersWithMessage("Only online players can create or edit their profile.")
				.addConversationAbandonedListener(e -> {
					final Conversable c = e.getContext().getForWhom();
					final Map<Object, Object> m = e.getContext().getAllSessionData();

					if (e.gracefulExit()) {
						final PlayerCache cache = Who.getCache(((Player)c).getUniqueId());

						cache.setRace(RaceConfig.fromString(String.valueOf(m.get(Data.RACE))));
						cache.setName(String.valueOf(m.get(Data.NAME)));
						cache.setAge((int) m.get(Data.AGE));
						cache.setSex(PlayerCache.Sex.valueOf(m.get(Data.SEX).toString().toUpperCase()));
						cache.setHeight((int) m.get(Data.HEIGHT));
						cache.setDescription(String.valueOf(m.get(Data.DESCRIPTION)));

						player.setDisplayName(cache.getName());

						cache.save();

						c.sendRawMessage(Common.colorize("&aCongratulations!&f You have finished your profile successfully and your data have been saved, " + m.get(Data.NAME) + "."));
					} else
						c.sendRawMessage(Common.colorize("&cCharacter creation has been cancelled."));
				})

				.withFirstPrompt(new RacePrompt())
				.buildConversation(player);

		player.beginConversation(convo);
	}

	enum Data {
		RACE,
		NAME,
		AGE,
		SEX,
		HEIGHT,
		DESCRIPTION
	}

	class RacePrompt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return Common.colorize("&ePlease enter your desired race. This is an important decision, " +
					"so please make sure you know what races there are available and their individual benefits. Type 'quit' to exit." );
		}

		@Override
		protected String getFailedValidationText(ConversationContext context, String invalidInput) {
			return Common.colorize("&ePlease enter a valid race. Use '/race list' to see all available races.");
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String input) {
			return Race.isValid(input);
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String input) {
			context.setSessionData(Data.RACE, input);

			return new NamePrompt();
		}
	}

	class NamePrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return Common.colorize("&ePlease enter your name to the chat.");
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			context.setSessionData(Data.NAME, input);

			return new AgePrompt();
		}
	}

	class AgePrompt extends NumericPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return Common.colorize("&ePlease enter your age as a whole number.");
		}

		@Override
		protected boolean isNumberValid(ConversationContext context, Number input) {
			return input.intValue() > 5 && input.intValue() < 100;
		}

		@Override
		protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
			return Common.colorize("&cPlease only enter a whole number here.");
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
			context.setSessionData(Data.AGE, input.intValue());

			return new SexPrompt();
		}
	}

	class SexPrompt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return Common.colorize("&ePlease enter your sex, either 'male' or 'female'.");
		}

		@Override
		protected String getFailedValidationText(ConversationContext context, String invalidInput) {
			return Common.colorize("&ePlease only enter 'male' or 'female'.");
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String input) {
			return input.equals("male") || input.equals("female");
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String input) {
			context.setSessionData(Data.SEX, input);

			return new HeightPrompt();
		}
	}

	class HeightPrompt extends NumericPrompt {
		@Override
		public String getPromptText(ConversationContext context) {
			return Common.colorize("&ePlease enter your height in inches.");
		}

		@Override
		protected boolean isNumberValid(ConversationContext context, Number input) {
			return input.intValue() >= 48 && input.intValue() <= 96;
		}

		@Override
		protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
			return Common.colorize("&cNumbers between 48 and 96 (4 ft and 8 ft) only.");
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
			context.setSessionData(Data.HEIGHT, input.intValue());

			return new DescriptionPrompt();
		}
	}

	class DescriptionPrompt extends StringPrompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return Common.colorize("&eEnter a brief description of your character.");
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			context.setSessionData(Data.DESCRIPTION, input);

			return Prompt.END_OF_CONVERSATION;
		}
	}
}
