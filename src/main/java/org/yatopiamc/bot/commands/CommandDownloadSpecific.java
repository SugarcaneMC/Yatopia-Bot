package org.yatopiamc.bot.commands;

import com.mrivanplays.jdcf.Command;
import com.mrivanplays.jdcf.CommandExecutionContext;
import com.mrivanplays.jdcf.args.CommandArguments;
import com.mrivanplays.jdcf.data.CommandAliases;
import com.mrivanplays.jdcf.data.CommandDescription;
import com.mrivanplays.jdcf.data.CommandUsage;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

@CommandAliases("download|dev|rc")
@CommandDescription("Download specific commands.")
@CommandUsage("?download|?dev|?rc")
public class CommandDownloadSpecific extends Command {

  @Override
  public boolean execute(@NotNull CommandExecutionContext context, @NotNull CommandArguments args) {
    String alias = context.getAlias();
    MessageChannel channel = context.getChannel();
    switch (alias) {
      case "download":
        channel
          .sendMessage("<https://ci.sugarcanemc.org/job/Sugarcane/>")
          .queue();
        break;
      case "dev":
        channel
            .sendMessage(
                "<https://ci.sugarcanemc.org/job/Sugarcane/job/1.17.1%252Fdev/>")
            .queue();
        break;
      case "rc":
        channel
            .sendMessage(
                "<https://ci.sugarcanemc.org/job/Sugarcane/job/1.17.1%252Frc/>")
            .queue();
        break;
    }
    return true;
  }
}
