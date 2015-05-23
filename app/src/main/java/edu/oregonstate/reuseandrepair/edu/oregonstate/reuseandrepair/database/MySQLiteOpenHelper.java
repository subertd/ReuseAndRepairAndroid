package edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Donald on 5/23/2015.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteOpenHelper.class.getName();

    private static final String TABLE_CATEGORY = "Category";
    private static final String TABLE_ITEM = "Item";
    private static final String TABLE_ORGANIZATION = "Organization";
    private static final String TABLE_ITEM_CATEGORY = "Item_Category";
    private static final String TABLE_ORGANIZATION_REUSE_ITEM = "Organization_Repair_Item";
    private static final String TABLE_ORGANIZATION_REPAIR_ITEM = "Organization_Repair_Item";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_DATABASE_STRING =

    "DROP TABLE IF EXISTS `" + TABLE_ORGANIZATION_REPAIR_ITEM + "`;" +
    "DROP TABLE IF EXISTS `" + TABLE_ORGANIZATION_REUSE_ITEM + "`;" +
    "DROP TABLE IF EXISTS `" + TABLE_ITEM_CATEGORY + "`;" +
    "DROP TABLE IF EXISTS `" + TABLE_ORGANIZATION + "`;" +
    "DROP TABLE IF EXISTS `" + TABLE_CATEGORY + "`;" +
    "DROP TABLE IF EXISTS `" + TABLE_ITEM + "`;" +

    "CREATE TABLE `" + TABLE_ORGANIZATION + "` (" +
        "`organization_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "`organization_name` VARCHAR ( 255 ) NOT NULL," +
        "`phone_number` VARCHAR( 14 ) NULL," +
        "`website_url` VARCHAR( 255 ) NULL," +
        "`physical_address` VARCHAR( 255 ) NULL" +
    ") ENGINE = INNODB;" +

    "CREATE TABLE `" + TABLE_CATEGORY + "` (" +
        "`category_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "`category_name` VARCHAR( 255 ) NOT NULL" +
    ") ENGINE = INNODB;" +

    "CREATE TABLE `" + TABLE_ITEM + "` (" +
        "`item_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        "`item_name` VARCHAR( 255 ) NOT NULL" +
    ") ENGINE = INNODB;" +

    "CREATE TABLE `" + TABLE_ITEM_CATEGORY + "` (" +
        "`category_id` INT NOT NULL," +
        "`item_id` INT NOT NULL," +
    "FOREIGN KEY (`category_id`) REFERENCES `Category`(`category_id`) ON DELETE CASCADE," +
    "FOREIGN KEY (`item_id`) REFERENCES `Item`(`item_id`) ON DELETE CASCADE," +
    "UNIQUE KEY (`category_id`, `item_id`)" +
    ") ENGINE = INNODB;" +

    "CREATE TABLE `" + TABLE_ORGANIZATION_REUSE_ITEM + "` (" +
        "`organization_id` INT NOT NULL," +
        "`item_id` INT NOT NULL," +
        "`additional_repair_info` TEXT," +
    "FOREIGN KEY (`organization_id`) REFERENCES `Organization`(`organization_id`) ON DELETE CASCADE," +
    "FOREIGN KEY (`item_id`) REFERENCES `Item`(`item_id`) ON DELETE CASCADE," +
    "UNIQUE KEY (`organization_id`, `item_id`)" +
    ") ENGINE = INNODB;" +

    "CREATE TABLE `" + TABLE_ORGANIZATION_REPAIR_ITEM + "` (" +
        "`organization_id` INT NOT NULL," +
        "`item_id` INT NOT NULL," +
    "FOREIGN KEY (`organization_id`) REFERENCES `Organization`(`organization_id`) ON DELETE CASCADE," +
    "FOREIGN KEY (`item_id`) REFERENCES `Item`(`item_id`) ON DELETE CASCADE," +
    "UNIQUE KEY (`organization_id`, `item_id`)" +
    ") ENGINE = INNODB;";

    public MySQLiteOpenHelper(final Context context) {
        super(context, null, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        Log.e(TAG, "'" + CREATE_DATABASE_STRING + "'");

        db.execSQL(CREATE_DATABASE_STRING);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // TODO implement
    }

    public void syncDatabase(final JSONObject database) throws DatabaseException {

        final SQLiteDatabase db = getWritableDatabase();

        try {
            final JSONArray organizations = database.getJSONArray("organizations");
            final JSONArray categories = database.getJSONArray("categories");
            final JSONArray items = database.getJSONArray("items");
            final JSONArray itemCategories = database.getJSONArray("itemCategories");
            final JSONArray organizationReuseItems = database.getJSONArray("organizationReuseItems");
            final JSONArray organizationRepairItems = database.getJSONArray("organizationRepairItems");

            // TODO sync items
            // TODO sync categories
            // TODO sync organizations
            // TODO sync item categories
            // TODO sync organization reuse items
            // TODO sync organization repair items
            for (int i = 0; i < organizations.length(); ++i) {
                JSONObject row = (JSONObject) organizations.get(i);
            }
        }
        catch (final JSONException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unexpected JSON data", e);
        }
    }

    public Cursor getCategoriesCursor() {

        final SQLiteDatabase db = getReadableDatabase();
        final String[] selectionArgs = {};
        final Cursor result = db.query(TABLE_CATEGORY, null, null, selectionArgs, null, null, null);
        db.close();
        return result;
    }

    public Cursor getItemsCursor(final long categoryId) {

        /*
        final SQLiteDatabase db = getReadableDatabase();
        final String[] selectionArgs = {};
        final Cursor result = db.query(TABLE_ITEM, null, null, selectionArgs, null, null, null);
        */
        throw new RuntimeException("Not yet implemented");
    }

    public Cursor getOrganization(final long itemId) {
        throw new RuntimeException("Not yet implemented");
    }
}
