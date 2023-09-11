package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.TagListDto;
import com.example.MyBookShopApp.data.dto.TagSize;
import com.example.MyBookShopApp.data.storage.Tag2BookRepository;
import com.example.MyBookShopApp.data.storage.TagRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.tags.Tag2BookEntity;
import com.example.MyBookShopApp.struct.tags.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Tag2BookService {

    Tag2BookRepository tag2BookRepository;
    TagRepository tagRepository;

    @Autowired
    public Tag2BookService(Tag2BookRepository tag2BookRepository, TagRepository tagRepository) {
        this.tag2BookRepository = tag2BookRepository;
        this.tagRepository = tagRepository;
    }

    public List<Tag2BookEntity> getAllBooksByTag() {
        return tag2BookRepository.findAll();
    }

    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }

    public Integer getCountByTagId(Integer tagId) {
        return tag2BookRepository.countByTagId(tagId);
    }

    public TagEntity getByTagId(Integer tagId) {
        return tagRepository.findById(tagId).get();
    }

    public List<BookEntity> getPageOfBooksByTag(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookEntity> bookEntities = new ArrayList<>();
        List<Tag2BookEntity> tag2BookEntities = tag2BookRepository.getBooksByTagId(tagId, nextPage).getContent();
        tag2BookEntities.forEach(element -> {
            bookEntities.add(element.getBook());
        });

        return bookEntities;
    }

    public List<TagListDto> getTagListDto() {
        List<TagEntity> tags = getAllTags();
        List<TagListDto> tagsList = new ArrayList<>();
        for (TagEntity tag : tags) {
            TagListDto tagListDto = new TagListDto();
            tagListDto.setName(tag.getName());
            tagListDto.setId(tag.getId());
            tagListDto.setCount(getCountByTagId(tag.getId()));
            tagsList.add(tagListDto);
        }

        if(tagsList.isEmpty()) {
            return tagsList;
        }
        Integer maxCount = Collections.max(tagsList).getCount();
        tagsList.forEach(tag -> {
            tag.setSizeClass(getTagSizeClass(maxCount, tag.getCount()));
        });
        return tagsList;
    }

    public String getTagSizeClass(Integer maxCount, Integer tagCount) {
        Integer counter = 1 + maxCount / TagSize.values().length;
        Integer iterator = 1;

        for (TagSize size : TagSize.values()) {
            Integer pointer = (counter * iterator) > maxCount ? maxCount : counter * iterator;
            if (tagCount <= pointer) {
                String result = "Tag_" + size.name();
                return size.name().equals("Tag") ? " " : result;
            }
            iterator++;
        }
        return " ";
    }

    public List<Tag2BookEntity> getTags2BookByBookId(Integer bookId) {
        return tag2BookRepository.findTagsByBookId(bookId);
    }

    public List<TagEntity> getTagEntity(List<Tag2BookEntity> tag2BookEntities) {
        return tag2BookEntities.stream().map(Tag2BookEntity::getTag).toList();
    }
}
