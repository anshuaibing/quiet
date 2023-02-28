package com.example.andemo.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.example.andemo.data.DataBases.MySQLiteOpenHelper;

public class DaoManager {
    // 数据库名称
    private  String DB_NAME = "user_info.db";

    private static DaoManager instance;

    private SQLiteOpenHelper mHelp;
    private DaoSession mDaoSession;

    /**
     * 使用单例模式获得操作数据库的对象
     */
    public static DaoManager getInstance() {
        if (instance == null)
            instance = new DaoManager();
        return instance;
    }

    public void initGreenDao(Context mContext) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    // 关闭数据库
    public void closeGreenDao() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
        if (mHelp != null) {
            mHelp.close();
            mHelp = null;
        }
    }


}
