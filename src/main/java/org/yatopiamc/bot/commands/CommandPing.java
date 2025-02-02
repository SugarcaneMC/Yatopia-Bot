package org.yatopiamc.bot.commands;

import com.mrivanplays.jdcf.Command;
import com.mrivanplays.jdcf.CommandExecutionContext;
import com.mrivanplays.jdcf.args.CommandArguments;
import com.mrivanplays.jdcf.data.CommandAliases;
import com.mrivanplays.jdcf.data.CommandDescription;
import com.mrivanplays.jdcf.data.CommandUsage;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

@CommandAliases("ping")
@CommandDescription("Shows latencies")
@CommandUsage("?ping")
public class CommandPing extends Command {

  @Override
  public boolean execute(@NotNull CommandExecutionContext context, @NotNull CommandArguments args) {
    MessageChannel channel = context.getChannel();
    long gateway = context.getJda().getGatewayPing();
    channel.sendMessage("Heartbeat latency: " + gateway + "ms").queue();
    context
        .getJda()
        .getRestPing()
        .queue(latency -> channel.sendMessage("Latency to Discord API: " + latency + "ms").queue());
    return true;
  }
}
