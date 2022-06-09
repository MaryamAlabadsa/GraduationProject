package com.example.graduationproject.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.graduationproject.retrofit.post.Post;

@Database(entities = {Post.class}, version = 2)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PostDao postDao();
}
