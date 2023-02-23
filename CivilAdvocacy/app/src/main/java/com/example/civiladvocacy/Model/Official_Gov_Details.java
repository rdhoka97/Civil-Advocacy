package com.example.civiladvocacy.Model;

import java.io.Serializable;
import java.util.List;

public class Official_Gov_Details implements Serializable {
    String post,name,address,party,officialAddress,phoneNumber,urls,email,photoUrl;
    List<String[]> channelList;


    public Official_Gov_Details(String name, String party, String photoUrl,
                                String officialAddress, String phoneNumber,
                                String email, String urls, List<String[]> channelList) {

        this.photoUrl = photoUrl;this.name = name;
        this.party = party;this.officialAddress = officialAddress;
        this.phoneNumber = phoneNumber;this.urls = urls;
        this.email = email; this.channelList = channelList;

    }

    public Official_Gov_Details(String address, String post, String name,
                                String party, String officialAddress,
                                String phoneNumber, String urls, String email,
                                String photoUrl, List<String[]> channelList) {

        this.officialAddress = officialAddress;this.phoneNumber = phoneNumber;
        this.urls = urls;this.email = email;
        this.photoUrl = photoUrl;this.channelList = channelList;
        this.address = address;this.post = post;
        this.name = name;this.party = party;

    }

    public String getUrls() {
        return urls;
    }

    public String getEmail() {
        return email;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getOfficialAddress() {
        return officialAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getParty() {
        return party;
    }


    public List<String[]> getChannelList() {
        return channelList;
    }

    public String getPost() {
        return post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GovernmentOfficials{" +"post='" + post + '\'' + ", name='" + name + '\'' +
                ", address='" + address + '\'' +", party='" + party + '\'' +
                ", officialAddress='" + officialAddress + '\'' + ", phoneNumber='" + phoneNumber + '\'' +
                ", urls='" + urls + '\'' +", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +", channelList=" + channelList + '}';
    }
}
