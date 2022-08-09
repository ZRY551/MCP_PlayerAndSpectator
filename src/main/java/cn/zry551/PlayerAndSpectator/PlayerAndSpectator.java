package cn.zry551.PlayerAndSpectator;

import cn.zry551.PlayerAndSpectator.Commands.PAS;
import cn.zry551.PlayerAndSpectator.Events.PlayerChat;
import cn.zry551.PlayerAndSpectator.Events.PlayerEvents;
import cn.zry551.PlayerAndSpectator.Events.PlayerJoinMSG;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class PlayerAndSpectator extends JavaPlugin {

    public PlayerAndSpectator Self = this;
    public Logger Log = getLogger();
    public FileConfiguration Cfg = getConfig();

    public List<String> PlayerList = new ArrayList<String>();
    public boolean ListIsUUID = false;

    public boolean Debug = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Log.info("PlayerAndSpectator is on Enable");
        saveDefaultConfig();
        try {
            Log.info("Loading Config Files ......");
            PlayerList = Cfg.getStringList("Players");
            ListIsUUID = Cfg.getBoolean("ListIsUUID", false);
            Debug = Cfg.getBoolean("DebugMode", false);
            if(!Cfg.getBoolean("Enable",true)){
                PluginConfigError("Stop Load This Plugin because 'Enable' in config.yml is False !");
                return;
            }
            if (PlayerList == null) {
                PluginConfigError("Can't Load This Plugin because \"Players\" in config.yml is Null !");
                return;
            } else {
                if (PlayerList.isEmpty()) {
                    PluginConfigError("Can't Load This Plugin because \"Players\" in config.yml is Empty !");
                    return;
                } else {
                    Log.info("Registering Plugin Event ......");
                    Debug("Register : JavaClass = PlayerEvents");
                    Bukkit.getPluginManager().registerEvents(new PlayerEvents(PlayerList, Self, ListIsUUID,Debug), this);
                    if(Cfg.getBoolean("PlayerJoinMSG.Enable",true)){
                        Debug("Register : JavaClass = PlayerJoinMSG");
                        Bukkit.getPluginManager().registerEvents(new PlayerJoinMSG(Self,Debug), this);
                    }
                    if(Cfg.getBoolean("Chat.Enable",true)){
                        Debug("Register : JavaClass = PlayerChat");
                        Bukkit.getPluginManager().registerEvents(new PlayerChat(PlayerList,ListIsUUID,Self,Debug), this);
                    }
                    Log.info("Registering Command ......");
                    if (Bukkit.getPluginCommand("playerandspectator") != null) {
                        Debug("Register : Command = PSA");
                        Bukkit.getPluginCommand("playerandspectator").setExecutor(new PAS(PlayerList,ListIsUUID,Self,Debug));
                    }


                    Log.info("Register Finish !");


                }
            }
        }catch (NullPointerException ex){
            PluginConfigError("Can't Load This Plugin because \"Players\" in config.yml is Null [ or others ERROR ] !");
            Debug("Catch Error : " + ex.getMessage());
            ex.printStackTrace();
            return;


        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Log.info("PlayerAndSpectator is on Disable");





    }

    public void PluginConfigError(String MSG){
        Log.warning(MSG);
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void Debug(String MSG){
        if(Debug){
            Log.info(MSG);
        }
    }
}
