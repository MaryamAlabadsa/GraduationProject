package com.example.graduationproject.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.graduationproject.retrofit.post.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post ORDER BY id desc ")
    public List<Post> readAll();

    @Insert
    public long insert(Post post);

    @Update
    public int update(Post post);

    @Delete
    public int delete(Post post);

    @Insert
    public long[] insertToPostList(List<Post> ordersTableList);

    @Query("DELETE FROM Post")
   public void clearAllData();
}
