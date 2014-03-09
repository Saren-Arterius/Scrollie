package net.wtako.Scrollie.Methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import net.wtako.Scrollie.Main;
import net.wtako.Scrollie.Utils.Lang;

public class Database {

    private static Database instance;
    public Connection       conn;

    public Database() throws SQLException {
        Database.instance = this;
        final String path = MessageFormat.format("jdbc:sqlite:{0}/{1}", Main.getInstance().getDataFolder()
                .getAbsolutePath(), "scrollie.db");
        conn = DriverManager.getConnection(path);
    }

    private void addConfig(String config, String value) throws SQLException {
        final PreparedStatement stmt = conn.prepareStatement("INSERT INTO `configs` (`config`, `value`) VALUES (?, ?)");
        stmt.setString(1, config);
        stmt.setString(2, value);
        stmt.execute();
        stmt.close();
        return;
    }

    public void createTables() throws SQLException {
        final Statement cur = conn.createStatement();
        cur.execute("CREATE TABLE `scrolls` (`rowid` INTEGER PRIMARY KEY AUTOINCREMENT, `scroll_destination` INT NOT NULL, `target_player` VARCHAR(20) NULL, `destination_x` INT NULL, `destination_y` INT NULL, `destination_z` INT NULL, `destination_world` VARCHAR(128) NULL, `warm_up_time` INT NOT NULL, `cool_down_time` INT NOT NULL, `allow_cross_world_tp` INT NOT NULL, `times_remaining` INT NOT NULL, `timestamp` INT NOT NULL)");
        cur.execute("CREATE TABLE `scroll_creations` (`rowid` INTEGER PRIMARY KEY AUTOINCREMENT, `owner` VARCHAR(20) NOT NULL, `scroll_id` INT NOT NULL, `name` VARCHAR(128) NULL, `scroll_destination` INT NOT NULL, `warm_up_time` INT NOT NULL, `cool_down_time` INT NOT NULL, `allow_cross_world_tp` INT NOT NULL, `times_be_used` INT NOT NULL, `timestamp` INT NOT NULL)");
        cur.execute("CREATE TABLE `tp_denies` (`player` VARCHAR(20) PRIMARY KEY)");
        cur.execute("CREATE TABLE `cooldowns` (`player` VARCHAR(20) PRIMARY KEY, `normal_until` INT NULL, `rescue_until` INT NULL)");
        cur.execute("CREATE TABLE `configs` (`config` VARCHAR(128) PRIMARY KEY, `value` VARCHAR(128) NULL)");
        cur.close();
        addConfig("database_version", "1");
        return;
    }

    private boolean areTablesExist() {
        try {
            final Statement cur = conn.createStatement();
            cur.execute("SELECT * FROM `scrolls` LIMIT 0");
            cur.execute("SELECT * FROM `scroll_creations` LIMIT 0");
            cur.execute("SELECT * FROM `tp_denies` LIMIT 0");
            cur.execute("SELECT * FROM `cooldowns` LIMIT 0");
            cur.execute("SELECT * FROM `configs` LIMIT 0");
            cur.close();
            return true;
        } catch (final SQLException ex) {
            return false;
        }
    }

    public void check() throws SQLException {
        Main.log.info(Lang.TITLE.toString() + "Checking databases...");
        if (!areTablesExist()) {
            Main.log.info(Lang.TITLE.toString() + "Creating databases...");
            createTables();
            Main.log.info(Lang.TITLE.toString() + "Done.");
        }
    }

    public static Database getInstance() {
        return Database.instance;
    }
    // private void updateDatabase() {}
}