package fr.legabi.myazuriomrank;

import fr.legabi.myazuriomrank.utils.ConnectionUtils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class AzuriomRank extends JavaPlugin {

    Connection connection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        System.out.println("Starting AzuriomRank");

        String host = getConfig().getString("db_host");
        String db = getConfig().getString("db_name");
        String user = getConfig().getString("db_user");
        String pass = getConfig().getString("db_pass");

        System.out.println("Connecting to database");
        try {
            Connection connection = ConnectionUtils.getMyConnection(host, db, user, pass);

            if (connection != null) {
                System.out.println("Connected to database");
                this.connection = connection;
            } else {
                System.out.println("Failed to connect to database");
                return;
            }
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                LuckPerms api = provider.getProvider();

                // Register a task to run every 10 seconds
                Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
                    @Override
                    public void run() {
                        // get all online players
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            // get the player's primary group
                            String group = api.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
                            try {
                                Statement statement = getConnection().createStatement();
                                String sql = "SELECT * FROM `roles` WHERE name = '" + group + "'";
                                ResultSet resultSet = statement.executeQuery(sql);

                                if (resultSet.next()) {
                                    int id = resultSet.getInt("id");
                                    // update the player's role in the database if player role is different from the one in the database
                                    sql = "SELECT * FROM `users` WHERE name = '" + player.getName() + "'";
                                    resultSet = statement.executeQuery(sql);
                                    if (resultSet.next()) {
                                        int roleId = resultSet.getInt("role_id");
                                        if (roleId != id) {
                                            sql = "UPDATE `users` SET role_id = " + id + " WHERE name = '" + player.getName() + "'";
                                            statement.executeUpdate(sql);
                                        }
                                    }
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        });
                    }
                }, 0L, 20L * 10);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
