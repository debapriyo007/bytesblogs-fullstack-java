package com.blogapp.service.impl;

import com.blogapp.dto.request.TagRequest;
import com.blogapp.dto.response.TagResponse;
import com.blogapp.entity.Tag;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.mapper.TagMapper;
import com.blogapp.repository.BlogRepository;
import com.blogapp.repository.TagRepository;
import com.blogapp.service.interfaces.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final BlogRepository blogRepository;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, BlogRepository blogRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.blogRepository = blogRepository;
    }

    @Override
    public TagResponse createTag(TagRequest request) {
        String tagName = request.getName().trim();
        if (tagRepository.existsByName(tagName)) {
            throw new IllegalArgumentException("Tag already exists with name: " + tagName);
        }

        Tag tag = tagMapper.toEntity(request);
        tag.setName(tagName);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toResponse(savedTag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return tagMapper.toResponse(tag);
    }

    @Override
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        String tagName = request.getName().trim();
        tagRepository.findByName(tagName).ifPresent(existingTag -> {
            if (!existingTag.getId().equals(id)) {
                throw new IllegalArgumentException("Tag name is already taken: " + tagName);
            }
        });

        tag.setName(tagName);
        Tag updatedTag = tagRepository.save(tag);
        return tagMapper.toResponse(updatedTag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        blogRepository.deleteTagAssociations(id);
        tagRepository.delete(tag);
    }
}
