package edu.oregonstate.reuseandrepair.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Donald on 5/23/2015.
 *
 * Creates a custom SQLite database for the application
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteOpenHelper.class.getName();

    private static final String DATABASE_NAME = "cs419-g15";
    private static final int DATABASE_VERSION = 10;

    // Table name constants
    private static final String TABLE_CATEGORY = "Category";
    private static final String TABLE_ITEM = "Item";
    private static final String TABLE_ORGANIZATION = "Organization";
    private static final String TABLE_ITEM_CATEGORY = "Item_Category";
    private static final String TABLE_ORGANIZATION_REUSE_ITEM = "Organization_Reuse_Item";
    private static final String TABLE_ORGANIZATION_REPAIR_ITEM = "Organization_Repair_Item";

    //
    // Column name constants
    public static final String TABLE_ORGANIZATION_COL_ID = "_id";
    public static final String TABLE_ORGANIZATION_COL_NAME = "organization_name";
    public static final String TABLE_ORGANIZATION_PHONE_NUMBER = "phone_number";
    public static final String TABLE_ORGANIZATION_PHYSICAL_ADDRESS = "physical_address";
    public static final String TABLE_ORGANIZATION_WEBSITE_URL = "website_url";

    public static final String TABLE_CATEGORY_COL_ID = "_id";
    public static final String TABLE_CATEGORY_COL_NAME = "category_name";

    public static final String TABLE_ITEM_COL_ID = "_id";
    public static final String TABLE_ITEM_COL_NAME = "item_name";

    public static final String TABLE_ITEM_CATEGORY_COL_ITEM_ID = "item_id";
    public static final String TABLE_ITEM_CATEGORY_COL_CATEGORY_ID = "category_id";

    public static final String TABLE_ORGANIZATION_REUSE_ITEM_COL_ORGANIZATION_ID = "organization_id";
    public static final String TABLE_ORGANIZATION_REUSE_ITEM_COL_ITEM_ID = "item_id";

    private static final String TABLE_ORGANIZATION_REPAIR_ITEM_COL_ORGANIZATION_ID = "organization_id";
    private static final String TABLE_ORGANIZATION_REPAIR_ITEM_COL_ITEM_ID = "item_id";
    private static final String TABLE_ORGANIZATION_REPAIR_COL_ADDITIONAL_REPAIR_INFO = "additional_repair_info";
    //

    //
    // Network data protocol constants
    private static final String PRP_ORGANIZATION_ID = "id";
    private static final String PRP_ORGANIZATION_NAME = "name";
    private static final String PRP_ORGANIZATION_PHONE_NUMBER = "phoneNumber";
    private static final String PRP_ORGANIZATION_PHYSICAL_ADDRESS = "physicalAddress";
    private static final String PRP_ORGANIZATION_WEBSITE_URL = "websiteUrl";

    private static final String PRP_CATEGORY_ID = "id";
    private static final String PRP_CATEGORY_NAME = "name";

    private static final String PRP_ITEM_ID = "id";
    private static final String PRP_ITEM_NAME = "name";

    private static final String PRP_ITEM_CATEGORY_ITEM_ID = "itemId";
    private static final String PRP_ITEM_CATEGORY_CATEGORY_ID = "categoryId";

    private static final String PRP_ORGANIZATION_REUSE_ITEM_ORGANIZATION_ID = "organizationId";
    private static final String PRP_ORGANIZATION_REUSE_ITEM_ITEM_ID = "itemId";

    private static final String PRP_ORGANIZATION_REPAIR_ITEM_ORGANIZATION_ID = "organizationId";
    private static final String PRP_ORGANIZATION_REPAIR_ITEM_ITEM_ID = "itemId";
    private static final String PRP_ORGANIZATION_REPAIR_ITEM_ADDITIONAL_REPAIR_INFO = "additionalRepairInfo";
    //

    //
    // SQL statements
    private static final String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys = ON";

    private static final String DROP_ORGANIZATION_REPAIR_ITEM_TABLE = "DROP TABLE IF EXISTS `" + TABLE_ORGANIZATION_REPAIR_ITEM + "`;";
    private static final String DROP_ORGANIZATION_REUSE_ITEM_TABLE = "DROP TABLE IF EXISTS `" + TABLE_ORGANIZATION_REUSE_ITEM + "`;";
    private static final String DROP_ITEM_CATEGORY_TABLE = "DROP TABLE IF EXISTS `" + TABLE_ITEM_CATEGORY + "`;";
    private static final String DROP_ORGANIZATION_TABLE = "DROP TABLE IF EXISTS `" + TABLE_ORGANIZATION + "`;";
    private static final String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS `" + TABLE_CATEGORY + "`;";
    private static final String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS `" + TABLE_ITEM + "`;";

    private static final String EMPTY_ORGANIZATION_TABLE = "DELETE FROM `" + TABLE_ORGANIZATION + "`;";
    private static final String EMPTY_CATEGORY_TABLE = "DELETE FROM `" + TABLE_CATEGORY + "`;";
    private static final String EMPTY_ITEM_TABLE = "DELETE FROM `" + TABLE_ITEM + "`;";
    private static final String EMPTY_ITEM_CATEGORY_TABLE = "DELETE FROM `" + TABLE_ITEM_CATEGORY + "`;";
    private static final String EMPTY_ORGANIZATION_REUSE_ITEM_TABLE = "DELETE FROM `" + TABLE_ORGANIZATION_REUSE_ITEM + "`;";
    private static final String EMPTY_ORGANIZATION_REPAIR_ITEM_TABLE = "DELETE FROM `" + TABLE_ORGANIZATION_REPAIR_ITEM + "`;";

    private static final String CREATE_ORGANIZATION_TABLE =
        "CREATE TABLE `" + TABLE_ORGANIZATION + "` (" +
            "`" + TABLE_ORGANIZATION_COL_ID + "` INT NOT NULL PRIMARY KEY," +
            "`organization_name` VARCHAR ( 255 ) NOT NULL," +
            "`phone_number` VARCHAR( 14 ) NULL," +
            "`website_url` VARCHAR( 255 ) NULL," +
            "`physical_address` VARCHAR( 255 ) NULL" +
    ")";

    private static final String CREATE_CATEGORY_TABLE =
        "CREATE TABLE `" + TABLE_CATEGORY + "` (" +
            "`" + TABLE_CATEGORY_COL_ID + "` INT NOT NULL PRIMARY KEY," +
            "`category_name` VARCHAR( 255 ) NOT NULL" +
    ")";

    private static final String CREATE_ITEM_TABLE =
        "CREATE TABLE `" + TABLE_ITEM + "` (" +
            "`" + TABLE_ITEM_COL_ID + "` INT NOT NULL PRIMARY KEY," +
            "`item_name` VARCHAR( 255 ) NOT NULL" +
    ")";

    private static final String CREATE_ITEM_CATEGORY_TABLE =
        "CREATE TABLE `" + TABLE_ITEM_CATEGORY + "` (" +
            "`category_id` INT NOT NULL," +
            "`item_id` INT NOT NULL," +
        "FOREIGN KEY (`category_id`) REFERENCES `Category`(`category_id`) ON DELETE CASCADE," +
        "FOREIGN KEY (`item_id`) REFERENCES `Item`(`item_id`) ON DELETE CASCADE," +
        "PRIMARY KEY (`category_id`, `item_id`)" +
    ")";

    private static final String CREATE_ORGANIZATION_REUSE_ITEM_TABLE =
        "CREATE TABLE `" + TABLE_ORGANIZATION_REUSE_ITEM + "` (" +
            "`organization_id` INT NOT NULL," +
            "`item_id` INT NOT NULL," +
        "FOREIGN KEY (`organization_id`) REFERENCES `Organization`(`organization_id`) ON DELETE CASCADE," +
        "FOREIGN KEY (`item_id`) REFERENCES `Item`(`item_id`) ON DELETE CASCADE," +
        "PRIMARY KEY (`organization_id`, `item_id`)" +
    ")";

    private static final String CREATE_ORGANIZATION_REPAIR_ITEM_TABLE =
        "CREATE TABLE `" + TABLE_ORGANIZATION_REPAIR_ITEM + "` (" +
            "`organization_id` INT NOT NULL," +
            "`item_id` INT NOT NULL," +
            "`additional_repair_info` TEXT," +
        "FOREIGN KEY (`organization_id`) REFERENCES `Organization`(`organization_id`) ON DELETE CASCADE," +
        "FOREIGN KEY (`item_id`) REFERENCES `Item`(`item_id`) ON DELETE CASCADE," +
        "PRIMARY KEY (`organization_id`, `item_id`)" +
    ")";
    //

    // Queries
    private static final String SELECT_ITEMS_BY_CATEGORY_QUERY =
        "SELECT * FROM `" + TABLE_ITEM + "` i " +
            "LEFT JOIN `" + TABLE_ITEM_CATEGORY + "` ic ON i.`" + TABLE_ITEM_COL_ID + "` = ic.`" + TABLE_ITEM_CATEGORY_COL_ITEM_ID + "` " +
            "LEFT JOIN `" + TABLE_CATEGORY + "` c ON c.`" + TABLE_CATEGORY_COL_ID + "` = ic.`" + TABLE_ITEM_CATEGORY_COL_CATEGORY_ID + "` " +
        "WHERE c.`" + TABLE_CATEGORY_COL_ID + "` = ?";

    private static final String SELECT_ORGANIZATIONS_BY_REUSE_ITEM =
        "SELECT * FROM `" + TABLE_ORGANIZATION + "` o " +
            "INNER JOIN `" + TABLE_ORGANIZATION_REUSE_ITEM + "` oi " +
                "ON o.`" + TABLE_ORGANIZATION_COL_ID + "` = oi.`" + TABLE_ORGANIZATION_REUSE_ITEM_COL_ORGANIZATION_ID + "` " +
        "WHERE oi.`" + TABLE_ORGANIZATION_REUSE_ITEM_COL_ITEM_ID + "` = ?";

    private static final String SELECT_ORGANIZATIONS_BY_REPAIR_ITEM =
            "SELECT * FROM `" + TABLE_ORGANIZATION + "` o " +
                    "INNER JOIN `" + TABLE_ORGANIZATION_REPAIR_ITEM + "` oi " +
                    "ON o.`" + TABLE_ORGANIZATION_COL_ID + "` = oi.`" + TABLE_ORGANIZATION_REPAIR_ITEM_COL_ORGANIZATION_ID + "` " +
                    "WHERE oi.`" + TABLE_ORGANIZATION_REPAIR_ITEM_COL_ITEM_ID + "` = ?";

    public MySQLiteOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        rebuildDatabase(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        rebuildDatabase(db);
    }

    private void rebuildDatabase(SQLiteDatabase db) {
        try {
            db.execSQL(ENABLE_FOREIGN_KEYS);

            db.execSQL(DROP_ORGANIZATION_REPAIR_ITEM_TABLE);
            db.execSQL(DROP_ORGANIZATION_REUSE_ITEM_TABLE);
            db.execSQL(DROP_ITEM_CATEGORY_TABLE);
            db.execSQL(DROP_ITEM_TABLE);
            db.execSQL(DROP_CATEGORY_TABLE);
            db.execSQL(DROP_ORGANIZATION_TABLE);

            db.execSQL(CREATE_ORGANIZATION_TABLE);
            db.execSQL(CREATE_CATEGORY_TABLE);
            db.execSQL(CREATE_ITEM_TABLE);
            db.execSQL(CREATE_ITEM_CATEGORY_TABLE);
            db.execSQL(CREATE_ORGANIZATION_REUSE_ITEM_TABLE);
            db.execSQL(CREATE_ORGANIZATION_REPAIR_ITEM_TABLE);
        }
        catch (final SQLException e) {
            Log.e(TAG, "ERROR: Invalid SQL statement; " + e.getMessage());
        }
    }

    private void syncOrganizations(final SQLiteDatabase db, final JSONArray organizations) throws JSONException, DatabaseException {

        Log.i(TAG, "ORGANIZATIONS: '" + organizations.toString() + "'");

        try {
            db.execSQL(EMPTY_ORGANIZATION_TABLE);

            for (int i = 0; i < organizations.length(); ++i) {
                final JSONObject organization = organizations.getJSONObject(i);
                final ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_ORGANIZATION_COL_ID, organization.getLong(PRP_ORGANIZATION_ID));
                contentValues.put(TABLE_ORGANIZATION_COL_NAME, organization.getString(PRP_ORGANIZATION_NAME));
                contentValues.put(TABLE_ORGANIZATION_PHONE_NUMBER, organization.getString(PRP_ORGANIZATION_PHONE_NUMBER));
                contentValues.put(TABLE_ORGANIZATION_PHYSICAL_ADDRESS, organization.getString(PRP_ORGANIZATION_PHYSICAL_ADDRESS));
                contentValues.put(TABLE_ORGANIZATION_WEBSITE_URL, organization.getString(PRP_ORGANIZATION_WEBSITE_URL));
                db.insertOrThrow(TABLE_ORGANIZATION, null, contentValues);
            }
        }
        catch (final SQLException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unable to sync organizations", e);
        }
    }

    private void syncCategories(final SQLiteDatabase db, final JSONArray categories) throws JSONException, DatabaseException {

        Log.i(TAG, "CATEGORIES: '" + categories.toString() + "'");

        try {
            db.execSQL(EMPTY_CATEGORY_TABLE);

            for (int i = 0; i < categories.length(); ++i) {
                final JSONObject category = categories.getJSONObject(i);
                final ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_CATEGORY_COL_ID, category.getLong(PRP_CATEGORY_ID));
                contentValues.put(TABLE_CATEGORY_COL_NAME, category.getString(PRP_CATEGORY_NAME));
                db.insertOrThrow(TABLE_CATEGORY, null, contentValues);
            }
        }
        catch (final SQLException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unable to sync categories", e);
        }
    }

    private void syncItems(final SQLiteDatabase db, final JSONArray items) throws JSONException, DatabaseException {

        Log.i(TAG, "ITEMS: '" + items.toString() + "'");

        try {
            db.execSQL(EMPTY_ITEM_TABLE);

            for (int i = 0; i < items.length(); ++i) {
                final JSONObject item = items.getJSONObject(i);
                final ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_ITEM_COL_ID, item.getLong(PRP_ITEM_ID));
                contentValues.put(TABLE_ITEM_COL_NAME, item.getString(PRP_ITEM_NAME));
                db.insertOrThrow(TABLE_ITEM, null, contentValues);
            }
        }
        catch (final SQLException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unable to sync items", e);
        }
    }

    private void syncItemCategories(final SQLiteDatabase db, final JSONArray itemCategories) throws JSONException, DatabaseException {

        Log.i(TAG, "ITEM_CATEGORIES: '" + itemCategories.toString() + "'");

        try {
            db.execSQL(EMPTY_ITEM_CATEGORY_TABLE);

            for (int i = 0; i < itemCategories.length(); ++i) {
                final JSONObject itemCategory = itemCategories.getJSONObject(i);
                final ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_ITEM_CATEGORY_COL_ITEM_ID, itemCategory.getLong(PRP_ITEM_CATEGORY_ITEM_ID));
                contentValues.put(TABLE_ITEM_CATEGORY_COL_CATEGORY_ID, itemCategory.getLong(PRP_ITEM_CATEGORY_CATEGORY_ID));
                db.insertOrThrow(TABLE_ITEM_CATEGORY, null, contentValues);
            }
        }
        catch (final SQLException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unable to sync item-category relationships", e);
        }
    }

    private void syncOrganizationReuseItems(final SQLiteDatabase db, final JSONArray organizationReuseItems) throws JSONException, DatabaseException {

        Log.i(TAG, "ORGANIZATION-REUSE-ITEMS: '" + organizationReuseItems.toString() + "'");

        try {
            db.execSQL(EMPTY_ORGANIZATION_REUSE_ITEM_TABLE);

            for (int i = 0; i < organizationReuseItems.length(); ++i) {
                final JSONObject organizationReuseItem = organizationReuseItems.getJSONObject(i);
                final ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_ORGANIZATION_REUSE_ITEM_COL_ORGANIZATION_ID, organizationReuseItem.getLong(PRP_ORGANIZATION_REUSE_ITEM_ORGANIZATION_ID));
                contentValues.put(TABLE_ORGANIZATION_REUSE_ITEM_COL_ITEM_ID, organizationReuseItem.getLong(PRP_ORGANIZATION_REUSE_ITEM_ITEM_ID));
                db.insertOrThrow(TABLE_ORGANIZATION_REUSE_ITEM, null, contentValues);
            }
        }
        catch (final SQLException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unable to sync organization reuse item relationships", e);
        }
    }

    private void syncOrganizationRepairItems(final SQLiteDatabase db, final JSONArray organizationRepairItems) throws JSONException, DatabaseException {

        Log.i(TAG, "ORGANIZATION-REPAIR-ITEMS: '" + organizationRepairItems.toString() + "'");

        try {
            db.execSQL(EMPTY_ORGANIZATION_REPAIR_ITEM_TABLE);

            for (int i = 0; i < organizationRepairItems.length(); ++i) {
                final JSONObject organizationRepairItem = organizationRepairItems.getJSONObject(i);
                final ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_ORGANIZATION_REPAIR_ITEM_COL_ORGANIZATION_ID, organizationRepairItem.getLong(PRP_ORGANIZATION_REPAIR_ITEM_ORGANIZATION_ID));
                contentValues.put(TABLE_ORGANIZATION_REPAIR_ITEM_COL_ITEM_ID, organizationRepairItem.getLong(PRP_ORGANIZATION_REPAIR_ITEM_ITEM_ID));
                contentValues.put(TABLE_ORGANIZATION_REPAIR_COL_ADDITIONAL_REPAIR_INFO, organizationRepairItem.getString(PRP_ORGANIZATION_REPAIR_ITEM_ADDITIONAL_REPAIR_INFO));
                db.insertOrThrow(TABLE_ORGANIZATION_REPAIR_ITEM, null, contentValues);
            }
        }
        catch (final SQLException e) {
            Log.e(TAG, e.getMessage());
            throw new DatabaseException("Unable to sync organization repair item relationships", e);
        }
    }

    public void syncDatabase(final JSONObject database) throws DatabaseException {

        final SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            final JSONArray organizations = database.getJSONArray("organizations");
            final JSONArray categories = database.getJSONArray("categories");
            final JSONArray items = database.getJSONArray("items");
            final JSONArray itemCategories = database.getJSONArray("itemCategories");
            final JSONArray organizationReuseItems = database.getJSONArray("organizationReuseItems");
            final JSONArray organizationRepairItems = database.getJSONArray("organizationRepairItems");

            syncOrganizations(db, organizations);
            syncCategories(db, categories);
            syncItems(db, items);
            syncItemCategories(db, itemCategories);
            syncOrganizationReuseItems(db, organizationReuseItems);
            syncOrganizationRepairItems(db, organizationRepairItems);

            db.setTransactionSuccessful();
        }
        catch (final JSONException e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, "DATABASE: '" + database.toString() + "'");
            throw new DatabaseException("Unexpected JSON data", e);
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * get and return an SQLite cursor containing each category
     *
     * @return the cursor containing each category
     */
    public Cursor getCategoriesCursor() {
        final SQLiteDatabase db = getReadableDatabase();
        final String[] selectionArgs = {};
        return db.query(TABLE_CATEGORY, null, null, selectionArgs, null, null, null);
    }

    /**
     * get and return an SQLite cursor containing all items that belong to a given category
     *
     * @param categoryId the id of the category to filter the results by
     * @return the cursor containing all items that belong to a given category
     */
    public Cursor getItemsCursorByCategory(final long categoryId) {
        final SQLiteDatabase db = getReadableDatabase();
        final String[] selectionArgs = {String.valueOf(categoryId)};

        // TODO handle category 0 ( no category )

        Log.i(TAG, SELECT_ITEMS_BY_CATEGORY_QUERY);

        return db.rawQuery(SELECT_ITEMS_BY_CATEGORY_QUERY, selectionArgs, null);
    }

    /**
     * get and return an SQLite cursor containing all organization that reuse a given item
     *
     * @param itemId the id of the item to filter the results by
     * @return the cursor containing all organization that reuse the given item
     */
    public Cursor getOrganizationsCursorByReuseItem(final long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] selectionArgs = {String.valueOf(itemId)};

        return db.rawQuery(SELECT_ORGANIZATIONS_BY_REUSE_ITEM, selectionArgs, null);
    }

    /**
     * get and return an SQLite cursor containing all organization that repair a given item
     *
     * @param itemId the id of the item to filter the results by
     * @return the cursor containing all organization that repair the given item
     */
    public Cursor getOrganizationsCursorByRepairItem(final long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] selectionArgs = {String.valueOf(itemId)};

        return db.rawQuery(SELECT_ORGANIZATIONS_BY_REPAIR_ITEM, selectionArgs, null);
    }

    // Data about organization matching orgId
    public Cursor getOrgInfoCursor(final long orgId) {
        SQLiteDatabase db = getReadableDatabase();
        String orgIdString = String.valueOf(orgId);
        String selectionStatement = "_id=?";
        String[] selectionArgs = {orgIdString};

        return db.query(TABLE_ORGANIZATION, null, selectionStatement, selectionArgs, null, null, null, null);
    }
}