package com.example.be_pro_chat_service.service;

import com.example.be_pro_chat_service.model.Group;
import com.example.be_pro_chat_service.repo.GroupRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepo groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group not found with id: " + id));
    }

    public Group getGroupByIdWithUsers(Long id) {
        return groupRepository.findByIdWithUsers(id)
                .orElseThrow(() -> new NoSuchElementException("Group not found with id: " + id));
    }

    public Group createGroup(Group group) {
        group.getUsers().add(group.getAdminUsername());
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group updatedGroup) {
        Group existing = getGroupById(id);

        existing.setGroupName(updatedGroup.getGroupName());
        existing.setAdminUsername(updatedGroup.getAdminUsername());
        existing.setUsers(updatedGroup.getUsers());

        return groupRepository.save(existing);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Transactional
    public Group addUserToGroup(Long groupId, String username) {
        Group group = getGroupById(groupId);
        if (!group.getUsers().contains(username)) {
            group.getUsers().add(username);
        }
        return groupRepository.save(group);
    }

    public Group removeUserFromGroup(Long groupId, String username) {
        Group group = getGroupById(groupId);
        group.getUsers().remove(username);
        return groupRepository.save(group);
    }

    public List<Group> getGroupsByUsername(String username) {
        return groupRepository.findByUsersContaining(username);
    }

}

