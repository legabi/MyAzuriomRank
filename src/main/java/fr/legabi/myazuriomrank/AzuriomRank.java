package fr.legabi.myazuriomrank;

import fr.legabi.myazuriomrank.events.RankChange;
import fr.legabi.myazuriomrank.utils.ConnectionUtils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class AzuriomRank extends JavaPlugin {

    Connection connection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        String host = getConfig().getString("db_host");
        String db = getConfig().getString("db_name");
        String user = getConfig().getString("db_user");
        String pass = getConfig().getString("db_pass");

        try {
            Connection connection = ConnectionUtils.getMyConnection("127.0.0.1", "azuriom", "root", "1234");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();

            // register event when rank is changed
            Bukkit.getPluginManager().registerEvents(new RankChange(api, this, connection), this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public Connection getConnection() {
        return connection;
    }
}
