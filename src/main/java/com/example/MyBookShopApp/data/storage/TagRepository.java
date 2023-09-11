package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.tags.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

}
