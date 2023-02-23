package com.example.civiladvocacy.Model;

import java.io.Serializable;
import java.util.List;

public class OfficesModel implements Serializable {
    String name;
    List<Integer> list;

    public OfficesModel(String name, List<Integer> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getList() {
        return list;
    }
}
