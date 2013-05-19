//I Dont Post The Jar So Feel Free To Compile It Yourself Just Dont Distubute It.

package main.pac;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class A2 extends JavaPlugin
{
  public static final int NAME = 0;
	public static final int LORE = 1;
	private CmdDesc[] help = {
		new CmdDesc("/renamer help", "Shows this menu", null),
		new CmdDesc("/renamer name <name>", "Names your item", "renamer.name"),
		new CmdDesc("/renamer lore <lore>", "Sets the lore of your item", "itemizer.lore"),
	};
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length >= 1)
		{
			if(args[0].equalsIgnoreCase("name"))renameCmd(sender, args, false);
			else if(args[0].equalsIgnoreCase("help"))helpCmd(sender, args, help, "Renamer Help");
			else if(args[0].equalsIgnoreCase("lore"))renameCmd(sender, args, true);
			else return msg(sender, ChatColor.GOLD + "Command not found.  Type " + ChatColor.AQUA + "/renamer help" + ChatColor.GOLD + " for help");
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE + "Created by West_dover");
			sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.AQUA + "/renamer help" + ChatColor.GOLD + " for help");
		}
		return true;
	}
	public boolean helpCmd(CommandSender sender, String[] args, CmdDesc[] help, String title)
	{
		int page = 1;
		if(args.length == 2)
		{
			try{page = Integer.parseInt(args[1]);}catch(NumberFormatException nfe){return msg(sender, ChatColor.RED + "\"" + args[1] + "\" is not a valid number");}
		}
		ArrayList<String>d = new ArrayList<String>();
		int max = 1;
		int cmda = 0;
		for(int i = 0; i < help.length; i ++)
		{
			CmdDesc c = help[i];
			if(c.getPerm() != null)
			{
				if(!sender.hasPermission(c.getPerm()))continue;
			}
			if(d.size() < 10)
			{
				if(i >= (page - 1)*10 && i <= ((page - 1)*10) + 9)d.add(c.asDef());
			}
			if(cmda > 10 && cmda % 10 == 1)max ++;
			cmda ++;
		}
		sender.sendMessage(ChatColor.GOLD + title + "(" + ChatColor.AQUA + page + ChatColor.GOLD + "/" + ChatColor.AQUA + max + ChatColor.GOLD + ")");
		for(String s:d)sender.sendMessage(s);
		return true;
	}
	public boolean renameCmd(CommandSender sender, String[] args, boolean lore)
	{
		String a = (lore ? "lore" : "name");
		if(noPerm(sender, "renamer." + a))return true;
		if(noConsole(sender))return true;
		if(args.length <= 1)return usage(sender, "renamer " + a);
		Player player = (Player)sender;
		ItemStack item = player.getItemInHand();
		if(item == null)return msg(sender, ChatColor.RED + "You need to hold an item in your hand!");
		String name = "";
		for(int i = 1; i < args.length; i ++)
		{
			if(name.isEmpty())name = name + args[i];
			else name = name + " " + args[i];
		}
		displayAction(item, col(name), (lore ? LORE : NAME));
		sender.sendMessage(ChatColor.GREEN + "The " + a + " of the item in your hand has been set to \"" + name + "\"!");
		return true;
	}
	private boolean msg(CommandSender sender, String msg)
	{
		sender.sendMessage(msg);
		return true;
	}
	private boolean noConsole(CommandSender sender)
	{
		if(sender instanceof Player)return false;
		sender.sendMessage(ChatColor.RED + "This command can only be used as an in-game player!");
		return true;
	}
	private boolean noPerm(CommandSender sender, String node)
	{
		if(sender.hasPermission(node))return false;
		sender.sendMessage(ChatColor.RED + "NO YOU CAN NOT RENAME THIS ITEM!");
		return true;
	}
	private boolean usage(CommandSender sender, String cmd)
	{
		return msg(sender, ChatColor.RED + "Usage: " + (sender instanceof Player ? "/" : "") + cmd);
	}
	public void displayAction(ItemStack item, String data, int action)
	{
		ItemMeta meta = item.getItemMeta();
		if(action == NAME)meta.setDisplayName(data);
		else if(action == LORE)
		{
			String[] d = data.split(" ");
			String temp = null;
			ArrayList<String>n = new ArrayList<String>();
			for(String s:d)
			{
				if(temp == null)
				{
					temp = "" + s;
					continue;
				}
				int sl = ChatColor.stripColor(s).length();
				if(sl >= 24)
				{
					n.add(temp);
					temp = null;
					n.add(s);
					continue;
				}
				int nl = sl + ChatColor.stripColor(temp).length();
				if(nl >= 24)
				{
					n.add(temp);
					temp = "" + s;
				}
				else temp = temp + " " + s;
			}
			if(temp != null)n.add(temp);
			ArrayList<String>fin = new ArrayList<String>();
			for(String s:n)
			{
				String[] t = s.split("\\\\n");
				for(int i = 0; i < t.length; i ++)fin.add(t[i]);
			}
			meta.setLore(fin);
		}
		item.setItemMeta(meta);
	}
	private String col(String s)
	{
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	private class CmdDesc
	{
		private String cmd;
		private String desc;
		private String perm;
		public CmdDesc(String cmd, String desc, String perm)
		{
			this.cmd = cmd;
			this.desc = desc;
			this.perm = perm;
		}
		public String asDef()
		{
			return ChatColor.AQUA + cmd + ChatColor.RED + " - " + ChatColor.GOLD + desc;
		}
		public String getPerm()
		{
			return perm;
		}
	}
}
