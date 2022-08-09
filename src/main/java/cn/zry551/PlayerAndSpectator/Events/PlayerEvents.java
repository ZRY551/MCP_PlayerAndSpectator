package cn.zry551.PlayerAndSpectator.Events;

import cn.zry551.PlayerAndSpectator.PlayerAndSpectator;
import cn.zry551.PlayerAndSpectator.StringTools;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class PlayerEvents implements Listener {
    public List<String> PlayerList = new ArrayList<String>();
    public PlayerAndSpectator Self;
    public boolean isUUID = false;
    public boolean DebugMode = false;

    public Logger Log;
    public FileConfiguration Cfg;

    public HashMap<Integer,String> MsgMap = new HashMap<Integer, String>();

    public HashMap<String,World> ServerWorldList = new HashMap<String,World>();
    public List<String> ServerWorldNameList = new ArrayList<String>();

    public PlayerEvents(List<String> PlayerList2, PlayerAndSpectator PluginSelf,boolean ListIsUUID,boolean isDebugMode){
        PlayerList = PlayerList2;
        Self = PluginSelf;
        isUUID = ListIsUUID;
        DebugMode = isDebugMode;


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
        Log.info("Load Player Event Finish !");
    }

    private void LoadMsgMap(){
        MsgMap.put(1,Cfg.getString("Messages.NoInListPlayers.FirstJoin","&f[&aPAS&f] &bNow you are a Spectator !"));
        MsgMap.put(2,Cfg.getString("Messages.NoInListPlayers.TryNotAllowedEvent","&f[&aPAS&f] &cYou Can't Do It !"));








    }


    public boolean CheckPlayerInList(Player p){
        if(Self.ListIsUUID){
            if(!PlayerList.contains(p.getUniqueId().toString())) {
                return false;
            }else{
                return true;
            }
        }else{
            if(!PlayerList.contains(p.getName())){
                return false;
            }else{
                return true;
            }
        }
    }





    @EventHandler
    public void Event_PlayerJoin(PlayerJoinEvent e){
        try {
            if (Cfg.getBoolean("NoInListPlayers.Join_SetGameMode", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                        e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    }
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    e.getPlayer().sendRawMessage(StringTools.HandleColorString(MsgMap.get(1)));

                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void Event_PlayerLogin(PlayerLoginEvent e){
        try {
            if (Cfg.getBoolean("NoInListPlayers.Login_SetGameMode", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                        e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    }
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    //e.getPlayer().sendRawMessage(StringTools.HandleColorString(MsgMap.get(1)));

                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void Event_PlayerChangeGameMode(PlayerGameModeChangeEvent e){
        try {
            if (Cfg.getBoolean("DontAllow.ChangeGameMode", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                        e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    }

                    if (e.getNewGameMode() != GameMode.SPECTATOR) {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                        e.getPlayer().sendRawMessage(StringTools.HandleColorString(MsgMap.get(2)));
                        e.setCancelled(true);
                    }
                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }

    }

    @EventHandler
    public void Event_PlayerDropItemEvent(PlayerDropItemEvent e){
        try {
            if (Cfg.getBoolean("DontAllow.DropItemEvent", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                        e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    }
                    //e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    if (Cfg.getBoolean("DontAllowToSpectator", false)) {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                    e.getPlayer().sendRawMessage(StringTools.HandleColorString(MsgMap.get(2)));
                    e.setCancelled(true);

                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void Event_PlayerAttemptPickupItemEvent(PlayerAttemptPickupItemEvent e){
        try {
            if (Cfg.getBoolean("DontAllow.AttemptPickupItemEvent", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                        e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    }
                    if (Cfg.getBoolean("DontAllowToSpectator", false)) {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                    e.getPlayer().sendRawMessage(StringTools.HandleColorString(MsgMap.get(2)));
                    e.setCancelled(true);

                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void Event_PlayerDeathEvent(PlayerDeathEvent e){
        try {
            if (Cfg.getBoolean("DontAllow.Death", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                        e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    }
                    if (Cfg.getBoolean("DontAllowToSpectator", false)) {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    }
                    //e.getPlayer().sendRawMessage(StringTools.HandleColorString(MsgMap.get(2)));
                    e.setCancelled(true);

                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void Event_PlayerMoveEvent(PlayerMoveEvent e){
        try {
            if (Cfg.getBoolean("NoInListPlayers.AutoGetNightVision", true)) {
                if (!CheckPlayerInList(e.getPlayer())) {
                    //if(Cfg.getBoolean("NoInListPlayers.AutoGetNightVision",true)) {
                    e.getPlayer().addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(2000000, 255));
                    //}

                }
            }
        }catch (Exception ex){
            Debug("Catch the Error in Event !");
            ex.printStackTrace();
            return;
        }
    }


















    public void Debug(String MSG){
        if(DebugMode){
            Log.info(MSG);
        }
    }





}
