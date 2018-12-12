package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.romanpulov.violetnote.db.tabledef.DBDefFactory;

import static com.romanpulov.violetnote.db.tabledef.DBCommonDef.*;

/**
 * BasicNote database open helper
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "basic_note.db";
    public static final int DATABASE_VERSION = 3;


    //note groups
    public static final String NOTE_GROUPS_TABLE_NAME = "note_groups";
    public static final String NOTE_GROUP_TYPE_COLUMN_NAME = "note_group_type";
    public static final String NOTE_GROUP_NAME_COLUMN_NAME = "note_group_name";
    public static final String NOTE_GROUP_ICON_COLUMN_NAME = "note_group_icon";

    public static final String[] NOTE_GROUPS_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            NOTE_GROUP_TYPE_COLUMN_NAME,
            NOTE_GROUP_NAME_COLUMN_NAME,
            NOTE_GROUP_ICON_COLUMN_NAME,
            ORDER_COLUMN_NAME
    };

    private static final String NOTE_GROUPS_TABLE_CREATE =
            "CREATE TABLE " + NOTE_GROUPS_TABLE_NAME + "(" +
                    NOTE_GROUPS_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_GROUPS_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTE_GROUPS_TABLE_COLS[2] + " TEXT NOT NULL," +
                    NOTE_GROUPS_TABLE_COLS[3] + " INTEGER NOT NULL," +
                    NOTE_GROUPS_TABLE_COLS[4] + " INTEGER NOT NULL" +
                    ");";

    private static final String NOTE_GROUPS_U_INDEX_CREATE =
            "CREATE UNIQUE INDEX u_note_groups ON " +
                    NOTE_GROUPS_TABLE_NAME + " (" +
                    NOTE_GROUPS_TABLE_COLS[1] +
                    ");";

    private static final String NOTE_GROUPS_TABLE_INITIAL_LOAD =
            "INSERT INTO " + NOTE_GROUPS_TABLE_NAME + "(" +
            NOTE_GROUP_TYPE_COLUMN_NAME + ", " +
            NOTE_GROUP_NAME_COLUMN_NAME + ", " +
            NOTE_GROUP_ICON_COLUMN_NAME + ", " +
            ORDER_COLUMN_NAME + ") " +
            "SELECT 1, 'Password Notes', 0, 1 " +
            "UNION ALL " +
            "SELECT 10, 'Basic Notes', 0, 2;";

    //note item param types
    public static final String NOTE_ITEM_PARAM_TYPES_TABLE_NAME = "note_item_param_types";
    public static final String PARAM_TYPE_NAME_COLUMN_NAME = "param_type_name";
    public static final String[] NOTE_ITEM_PARAM_TYPES_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            PARAM_TYPE_NAME_COLUMN_NAME,
            ORDER_COLUMN_NAME
    };
    public static final String NOTE_ITEM_PARAM_TYPE_NAME_PRICE = "Price";

    private static final String NOTE_ITEM_PARAM_TYPES_TABLE_CREATE =
            "CREATE TABLE " + NOTE_ITEM_PARAM_TYPES_TABLE_NAME + "(" +
                    NOTE_ITEM_PARAM_TYPES_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_ITEM_PARAM_TYPES_TABLE_COLS[1] + " TEXT NOT NULL," +
                    NOTE_ITEM_PARAM_TYPES_TABLE_COLS[2] + " INTEGER NOT NULL" +
                    ");";

    private static final String NOTE_ITEM_PARAM_TYPES_U_INDEX_CREATE =
            "CREATE UNIQUE INDEX u_note_item_param_types ON " +
                    NOTE_ITEM_PARAM_TYPES_TABLE_NAME + " (" +
                    NOTE_ITEM_PARAM_TYPES_TABLE_COLS[1] +
                    ");";

    private static final String NOTE_ITEM_PARAM_TYPES_INITIAL_LOAD =
            "INSERT INTO " + NOTE_ITEM_PARAM_TYPES_TABLE_NAME + "(" +
                    PARAM_TYPE_NAME_COLUMN_NAME + ", " +
                    ORDER_COLUMN_NAME + ") " +
                    "SELECT '" + NOTE_ITEM_PARAM_TYPE_NAME_PRICE + "', 1;";

    //notes
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String GROUP_ID_COLUMN_NAME = "group_id";
    public static final String NOTE_TYPE_COLUMN_NAME = "note_type";
    public static final String IS_ENCRYPTED_COLUMN_NAME = "is_encrypted";
    public static final String TITLE_COLUMN_NAME = "title";
    public static final String[] NOTES_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            ORDER_COLUMN_NAME,
            GROUP_ID_COLUMN_NAME,
            NOTE_TYPE_COLUMN_NAME,
            TITLE_COLUMN_NAME,
            IS_ENCRYPTED_COLUMN_NAME,
            ENCRYPTED_STRING_COLUMN_NAME
    };
    private static final String NOTES_TABLE_CREATE =
            "CREATE TABLE " + NOTES_TABLE_NAME + " (" +
                    NOTES_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTES_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[3] + " INTEGER NOT NULL, " +
                    NOTES_TABLE_COLS[4] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[5] + " TEXT NOT NULL," +
                    NOTES_TABLE_COLS[6] + " INTEGER," +
                    NOTES_TABLE_COLS[7] + " TEXT," +
                    " FOREIGN KEY (" + NOTES_TABLE_COLS[3] + ") REFERENCES " + NOTE_GROUPS_TABLE_NAME + "(" + NOTE_GROUPS_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTES_TABLE_ADD_GROUP_ID =
            "ALTER TABLE " + NOTES_TABLE_NAME + " ADD " +
            NOTES_TABLE_COLS[7] + " INTEGER ";

    private static final String NOTES_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTES_TABLE_NAME +
                    " ON " + NOTES_TABLE_NAME + "(" +
                    GROUP_ID_COLUMN_NAME + ")";

    //note items
    public static final String NOTE_ITEMS_TABLE_NAME = "note_items";
    public static final String[] NOTE_ITEMS_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            ORDER_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            NAME_COLUMN_NAME,
            VALUE_COLUMN_NAME,
            CHECKED_COLUMN_NAME,
            PRIORITY_COLUMN_NAME
    };
    private static final String NOTE_ITEMS_TABLE_CREATE =
            "CREATE TABLE " + NOTE_ITEMS_TABLE_NAME + " (" +
                    NOTE_ITEMS_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_ITEMS_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_TABLE_COLS[3] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_TABLE_COLS[4] + " TEXT," +
                    NOTE_ITEMS_TABLE_COLS[5] + " TEXT," +
                    NOTE_ITEMS_TABLE_COLS[6] + " INTEGER," +
                    NOTE_ITEMS_TABLE_COLS[7] + " INTEGER NOT NULL DEFAULT 0," +
                    " FOREIGN KEY (" + NOTE_ITEMS_TABLE_COLS[3] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTE_ITEMS_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_ITEMS_TABLE_NAME +
                    " ON " + NOTE_ITEMS_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ")";

    //note values
    public static final String NOTE_VALUES_TABLE_NAME = "note_values";
    public static final String[] NOTE_VALUES_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            VALUE_COLUMN_NAME
    };
    private static final String NOTE_VALUES_TABLE_CREATE =
            "CREATE TABLE " + NOTE_VALUES_TABLE_NAME + " (" +
                    NOTE_VALUES_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_VALUES_TABLE_COLS[1] + " INTEGER ," +
                    NOTE_VALUES_TABLE_COLS[2] + " TEXT," +
                    " FOREIGN KEY (" + NOTE_VALUES_TABLE_COLS[1] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTE_VALUES_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_VALUES_TABLE_NAME +
                    " ON " + NOTE_VALUES_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ")";
    private static final String NOTE_VALUES_U_INDEX_CREATE =
            "CREATE UNIQUE INDEX u_" + NOTE_VALUES_TABLE_NAME +
                    " ON " + NOTE_VALUES_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ", " +
                    VALUE_COLUMN_NAME +
                    ")";


    //note items history
    public static final String NOTE_ITEMS_HISTORY_TABLE_NAME = "note_items_history";
    public static final String[] NOTE_ITEMS_HISTORY_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            VALUE_COLUMN_NAME
    };
    private static final String NOTE_ITEMS_HISTORY_TABLE_CREATE =
            "CREATE TABLE " + NOTE_ITEMS_HISTORY_TABLE_NAME + " (" +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[3] + " TEXT NOT NULL," +
                    " FOREIGN KEY (" + NOTE_ITEMS_HISTORY_TABLE_COLS[2] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTE_ITEMS_HISTORY_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_ITEMS_HISTORY_TABLE_NAME +
                    " ON " + NOTE_ITEMS_HISTORY_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ")";
    private static final String NOTE_ITEMS_HISTORY_U_INDEX_CREATE =
            "CREATE UNIQUE INDEX u_node_items_history ON " +
                    NOTE_ITEMS_HISTORY_TABLE_NAME + " (" +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[2] + ", " +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[3] +
                    ");";

    //note item params
    public static final String NOTE_ITEM_PARAMS_TABLE_NAME = "note_item_params";
    public static final String NOTE_ITEM_ID_COLUMN_NAME = "note_item_id";
    public static final String NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME = "note_item_param_type_id";
    public static final String V_INT_COLUMN_NAME = "v_int";
    public static final String V_TEXT_COLUMN_NAME = "v_text";
    public static final String[] NOTE_ITEM_PARAMS_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            NOTE_ITEM_ID_COLUMN_NAME,
            NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME,
            V_INT_COLUMN_NAME,
            V_TEXT_COLUMN_NAME
    };

    private static final String NOTE_ITEM_PARAMS_TABLE_CREATE =
            "CREATE TABLE " + NOTE_ITEM_PARAMS_TABLE_NAME + "(" +
                    NOTE_ITEM_PARAMS_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_ITEM_PARAMS_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTE_ITEM_PARAMS_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTE_ITEM_PARAMS_TABLE_COLS[3] + " INTEGER," +
                    NOTE_ITEM_PARAMS_TABLE_COLS[4] + " TEXT," +
                    " FOREIGN KEY (" + NOTE_ITEM_PARAMS_TABLE_COLS[1] + ") REFERENCES " + NOTE_ITEMS_TABLE_NAME + "(" + NOTE_ITEMS_TABLE_COLS[0] + ")," +
                    " FOREIGN KEY (" + NOTE_ITEM_PARAMS_TABLE_COLS[2] + ") REFERENCES " + NOTE_ITEM_PARAM_TYPES_TABLE_NAME + "(" + NOTE_ITEM_PARAM_TYPES_TABLE_COLS[0] + ")" +
                    ")";
    private static final String NOTE_ITEM_PARAMS_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_ITEM_PARAMS_TABLE_NAME +
                    " ON " + NOTE_ITEM_PARAMS_TABLE_NAME + "(" +
                    NOTE_ITEM_ID_COLUMN_NAME + ")";
    private static final String NOTE_ITEM_PARAMS_FK_TYPE_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_ITEM_PARAMS_TABLE_NAME + "_type" +
                    " ON " + NOTE_ITEM_PARAMS_TABLE_NAME + "(" +
                    NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME + ")";


    //note_id selection
    public static final String NOTE_ID_SELECTION_STRING = NOTE_ID_COLUMN_NAME + " = ?";

    //priority selection
    public static final String PRIORITY_SELECTION_STRING = PRIORITY_COLUMN_NAME + " = ?";

    public static final String AND_STRING = " AND ";

    public DBBasicNoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String s : DBDefFactory.buildDBCreate()) {
            db.execSQL(s);
        }
        /*
        db.execSQL(NOTE_GROUPS_TABLE_CREATE);
        db.execSQL(NOTE_GROUPS_U_INDEX_CREATE);
        db.execSQL(NOTE_GROUPS_TABLE_INITIAL_LOAD);

        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(NOTES_FK_INDEX_CREATE);

        db.execSQL(NOTE_ITEMS_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_FK_INDEX_CREATE);

        db.execSQL(NOTE_VALUES_TABLE_CREATE);
        db.execSQL(NOTE_VALUES_FK_INDEX_CREATE);
        db.execSQL(NOTE_VALUES_U_INDEX_CREATE);

        db.execSQL(NOTE_ITEMS_HISTORY_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_HISTORY_FK_INDEX_CREATE);
        db.execSQL(NOTE_ITEMS_HISTORY_U_INDEX_CREATE);

        db.execSQL(NOTE_ITEM_PARAM_TYPES_TABLE_CREATE);
        db.execSQL(NOTE_ITEM_PARAM_TYPES_U_INDEX_CREATE);
        db.execSQL(NOTE_ITEM_PARAM_TYPES_INITIAL_LOAD);

        db.execSQL(NOTE_ITEM_PARAMS_TABLE_CREATE);
        db.execSQL(NOTE_ITEM_PARAMS_FK_INDEX_CREATE);
        db.execSQL(NOTE_ITEM_PARAMS_FK_TYPE_INDEX_CREATE);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + NOTE_ITEMS_TABLE_NAME + " ADD priority INTEGER NOT NULL DEFAULT 0;");
            case 2:
                db.execSQL(NOTE_GROUPS_TABLE_CREATE);
                db.execSQL(NOTE_GROUPS_U_INDEX_CREATE);

                db.execSQL(NOTE_ITEM_PARAM_TYPES_TABLE_CREATE);
                db.execSQL(NOTE_ITEM_PARAM_TYPES_U_INDEX_CREATE);
                db.execSQL(NOTE_ITEM_PARAM_TYPES_INITIAL_LOAD);

                db.execSQL(NOTE_ITEM_PARAMS_TABLE_CREATE);
                db.execSQL(NOTE_ITEM_PARAMS_FK_INDEX_CREATE);
                db.execSQL(NOTE_ITEM_PARAMS_FK_TYPE_INDEX_CREATE);

                db.execSQL(NOTES_TABLE_ADD_GROUP_ID);
                db.execSQL(NOTE_GROUPS_TABLE_INITIAL_LOAD);
                db.execSQL(NOTES_FK_INDEX_CREATE);

            case 100:
                break;
            default:
                db.execSQL("DROP TABLE IF EXISTS " + NOTE_GROUPS_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + NOTE_ITEM_PARAMS_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + NOTE_ITEM_PARAM_TYPES_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + NOTE_ITEMS_HISTORY_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + NOTE_ITEMS_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + NOTE_VALUES_TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
                onCreate(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
