package com.example.be_pro_chat_service.controllers;

import com.example.be_pro_chat_service.model.Group;
import com.example.be_pro_chat_service.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @GetMapping("/by-user")
    public List<Group> getGroupsByUsername(@RequestParam String username) {
        return groupService.getGroupsByUsername(username);
    }


    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable Long id, @RequestBody Group group) {
        return groupService.updateGroup(id, group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
    }

    @PostMapping("/{id}/users")
    public Group addUser(@PathVariable Long id, @RequestParam String username) {
        return groupService.addUserToGroup(id, username);
    }

    @DeleteMapping("/{id}/users")
    public Group removeUser(@PathVariable Long id, @RequestParam String username) {
        return groupService.removeUserFromGroup(id, username);
    }
}

