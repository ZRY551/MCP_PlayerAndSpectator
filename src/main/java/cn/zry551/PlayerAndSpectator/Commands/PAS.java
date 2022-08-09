package cn.zry551.PlayerAndSpectator.Commands;

import cn.zry551.PlayerAndSpectator.PlayerAndSpectator;
import cn.zry551.PlayerAndSpectator.StringTools;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PAS implements CommandExecutor, TabExecutor {
    public List<String> PlayerList = new ArrayList<String>();
    public boolean IsUUID = false;
    public boolean DebugMode = false;
    public PlayerAndSpectator Self;
    public Logger Log;
    public FileConfiguration Cfg;

    public Map<Integer,String> MsgMap = new HashMap<Integer,String>();

    public HashMap<String, World> ServerWorldList = new HashMap<String,World>();
    public List<String> ServerWorldNameList = new ArrayList<String>();

    public PAS(List<String> PlayerList2,boolean ListIsUUID,PlayerAndSpectator PluginSelf, boolean isDebugMode){
        Self = PluginSelf;
        DebugMode = isDebugMode;
        PlayerList = PlayerList2;
        IsUUID = ListIsUUID;
        Log = Self.getLogger();
        Cfg = Self.Cfg;

        List<World> TheWorldList = Self.getServer().getWorlds();
        Log.info("GetWorldList Finish !");
        World Temp;
        for (int i = 0; i < TheWorldList.size(); i++) {
            Temp = TheWorldList.get(i);
            ServerWorldList.put(Temp.getName(),Temp);
            ServerWorldNameList.add(Temp.getName());
            Debug("SetWorldList[" + String.valueOf(i) + "] : '" + Temp.getName() + "' , ServerWorldList.Size = " + String.valueOf(ServerWorldList.size()) +
                    ", ServerWorldNameList.Size = " + String.valueOf(ServerWorldNameList.size())+
                    " !");

        }





        LoadMsgMap();
        Log.info("Load Command [Player] Finish !");
    }

    public void LoadMsgMap(){
        MsgMap.put(1,Cfg.getString("Messages.PermissionDenied","&f[&aPAS&f] &cPermission Denied !"));
        MsgMap.put(2,Cfg.getString("Messages.UnknownCommand","&f[&aPAS&f] &cUnknown Command !"));
        MsgMap.put(3,Cfg.getString("Messages.CommandDone","&f[&aPAS&f] &aDone !"));








    }

    public void Debug(String MSG){
        if(DebugMode){
            Log.info(MSG);
        }
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> Tab = new ArrayList<>();
        try {
            if (args.length == 1) {
                Tab.add("nv");
                Tab.add("wtp");
                Tab.add("wtp2");
                Tab.add("tp");
                Tab.add("tpp");
                Tab.add("wls");
            } else if (args.length == 2) {
                if(args[1] == "wtp"){
                    Tab.add("<World Name>");
                }else if(args[1] == "wtp2"){
                    Tab.add("<World Name> <x> <y> <z>");
                }else  if(args[1] == "tp"){
                    Tab.add("<x> <y> <z>");
                }else if(args[1] == "tpp"){
                    Tab.add("<Player Name>");
                }
            }
        }catch (IndexOutOfBoundsException ex){
            //return Tab;
        }
        return Tab;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0) {
                sender.sendMessage(StringTools.HandleColorString(MsgMap.get(2)));
                return false;
            } else if (args.length == 1) {
                if(args[0].toLowerCase().equals("nv") || args[0].toLowerCase().equals("nightvision")){
                    if(sender.hasPermission("PlayerAndSpectator.Player.NightVision")){
                        try {
                            sender.getServer().getPlayer(sender.getName()).addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                            sender.sendMessage(StringTools.HandleColorString(MsgMap.get(3)));
                            return true;
                        }catch (Exception ex){
                           Log.warning("Command Run Warning !");
                           ex.printStackTrace();
                           Debug("SenderName : '" + sender.getName() + "',CommandName : " + command.getName());
                           return false;
                        }
                    }else{
                        sender.sendMessage(StringTools.HandleColorString(MsgMap.get(1)));
                        return false;
                    }
                    //NightVision
                }else if(args[0].toLowerCase().equals("wls") || args[0].toLowerCase().equals("worldlist")){
                    if(sender.hasPermission("PlayerAndSpectator.Player.WorldList")){
                        try {
                            //Player Player = sender.getServer().getPlayer(sender.getName());
                            Player Player = (Player) sender;
                            String Temp = "";
                            for (int i = 0; i < ServerWorldNameList.size(); i++) {
                                Temp = Temp + "'" + ServerWorldNameList.get(i) + "',";

                            }

                            sender.sendMessage(StringTools.HandleColorString(MsgMap.get(3) + "\n&b" + Temp));
                            return true;
                        }catch (Exception ex){
                            Log.warning("Command Run Warning !");
                            ex.printStackTrace();
                            Debug("SenderName : '" + sender.getName() + "',CommandName : " + command.getName());
                            return false;
                        }
                    }else{
                        sender.sendMessage(StringTools.HandleColorString(MsgMap.get(1)));
                        return false;
                    }
                    //WorldTP
                }

            } else if (args.length == 2) {
                if(args[0].toLowerCase().equals("wtp") || args[0].toLowerCase().equals("worldtp")){
                    if(sender.hasPermission("PlayerAndSpectator.Player.WorldTelePort")){
                        try {
                            //Player Player = sender.getServer().getPlayer(sender.getName());
                            Player Player = (Player) sender;
                            Location Local = Player.getLocation();
                            Local.setWorld(sender.getServer().getWorld(args[1]));
                            Player.teleport(Local);
                            sender.sendMessage(StringTools.HandleColorString(MsgMap.get(3)));
                            return true;
                        }catch (Exception ex){
                            Log.warning("Command Run Warning !");
                            ex.printStackTrace();
                            Debug("SenderName : '" + sender.getName() + "',CommandName : " + command.getName());
                            return false;
                        }
                    }else{
                        sender.sendMessage(StringTools.HandleColorString(MsgMap.get(1)));
                        return false;
                    }
                    //WorldTP
                }else if(args[0].toLowerCase().equals("tpp") || args[0].toLowerCase().equals("teleportplayer")){
                    if(sender.hasPermission("PlayerAndSpectator.Player.TelePortPlayer")){
                        try {
                            //Player Player = sender.getServer().getPlayer(sender.getName());
                            Player Player = (Player) sender;
                            Location Local = sender.getServer().getPlayer(args[1]).getLocation();
                            Player.teleport(Local);
                            sender.sendMessage(StringTools.HandleColorString(MsgMap.get(3)));
                            return true;
                        }catch (Exception ex){
                            Log.warning("Command Run Warning !");
                            ex.printStackTrace();
                            Debug("SenderName : '" + sender.getName() + "',CommandName : " + command.getName());
                            return false;
                        }
                    }else{
                        sender.sendMessage(StringTools.HandleColorString(MsgMap.get(1)));
                        return false;
                    }
                    //WorldTP
                }
            } else if (args.length == 5) {
                if(args[0].toLowerCase().equals("wtp2") || args[0].toLowerCase().equals("worldtp2")){
                    if(sender.hasPermission("PlayerAndSpectator.Player.WorldTelePort2")){
                        try {
                            //Player Player = sender.getServer().getPlayer(sender.getName());
                            Player Player = (Player) sender;
                            Location Local = Player.getLocation();
                            Local.setWorld(sender.getServer().getWorld(args[1]));
                            Local.setX(Double.parseDouble(args[2]));
                            Local.setY(Double.parseDouble(args[3]));
                            Local.setZ(Double.parseDouble(args[4]));
                            Player.teleport(Local);
                            sender.sendMessage(StringTools.HandleColorString(MsgMap.get(3)));
                            return true;
                        }catch (Exception ex){
                            Log.warning("Command Run Warning !");
                            ex.printStackTrace();
                            Debug("SenderName : '" + sender.getName() + "',CommandName : " + command.getName());
                            return false;
                        }
                    }else{
                        sender.sendMessage(StringTools.HandleColorString(MsgMap.get(1)));
                        return false;
                    }
                    //WorldTP
                }
            } else if (args.length == 4) {
                if(args[0].toLowerCase().equals("tp") || args[0].toLowerCase().equals("teleport")){
                    if(sender.hasPermission("PlayerAndSpectator.Player.TelePort")){
                        try {
                            //Player Player = sender.getServer().getPlayer(sender.getName());
                            Player Player = (Player) sender;
                            Location Local = Player.getLocation();
                            //Local.setWorld(sender.getServer().getWorld(args[1]));
                            Local.setX(Double.parseDouble(args[1]));
                            Local.setY(Double.parseDouble(args[2]));
                            Local.setZ(Double.parseDouble(args[3]));
                            Player.teleport(Local);
                            sender.sendMessage(StringTools.HandleColorString(MsgMap.get(3)));
                            return true;
                        }catch (Exception ex){
                            Log.warning("Command Run Warning !");
                            ex.printStackTrace();
                            Debug("SenderName : '" + sender.getName() + "',CommandName : " + command.getName());
                            return false;
                        }
                    }else{
                        sender.sendMessage(StringTools.HandleColorString(MsgMap.get(1)));
                        return false;
                    }
                    //WorldTP
                }
            }



        }catch (IndexOutOfBoundsException ex){
            //return Tab;
        }
        return false;
    }





}
