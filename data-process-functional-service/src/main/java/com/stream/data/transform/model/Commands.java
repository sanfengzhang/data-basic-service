package com.stream.data.transform.model;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/3
 * @desc:
 */
public class  Commands {

    /**
     * 唯一ID
     */
    private String id;

    /**
     * 一个规则的过程的描述
     */
    private String processDesc;

    /**
     * 具体的规则组合
     */
    private List commands = new ArrayList();

    private Map<String, Object> commandsMap = new HashMap<>();

    public Commands(String id) {
        this(id, Collections.emptyList());
    }

    public Commands(String id, List<String> importCommands) {
        commandsMap.put("id", id);
        List<String> importCommandsTmp = new ArrayList<>();
        importCommandsTmp.add("org.kitesdk.**");
        importCommandsTmp.addAll(importCommands);
        commandsMap.put("importCommands", importCommandsTmp);

        commandsMap.put("commands", commands);
    }

    public Commands addCommand(Map<String, Object> command) {
        commands.add(command);
        return this;
    }

    public Commands addAllCommand(List<Map<String, Object>> commandList) {
        commands.add(commandList);
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDesc() {
        return processDesc;
    }

    public void setProcessDesc(String processDesc) {
        this.processDesc = processDesc;
    }

    public Map<String, Object> get() {
        return commandsMap;
    }

    public void setCommandsMap(Map<String, Object> commandsMap) {
        this.commandsMap = commandsMap;
    }

    public void addCommand(String command) {

    }

    public static Commands build(String id) {

        return new Commands(id);
    }

    public static Commands build(String id, List<String> importCommands) {

        return new Commands(id, importCommands);
    }
}
