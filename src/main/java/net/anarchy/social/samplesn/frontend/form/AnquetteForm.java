package net.anarchy.social.samplesn.frontend.form;

import net.anarchy.social.samplesn.backend.entity.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AnquetteForm {
    private long id;
    private String email;
    private String lastName;
    private String firstName;
    private int age;
    private int gender;
    private String cityName;
    private String interestsAsString;

    public void initalize(User auser) {
        this.id = auser.getId();
        this.email = auser.getEmail();
        this.lastName = auser.getLastName();
        this.firstName = auser.getFirstName();
        this.age = auser.getAge();
        this.gender = auser.getGender().getId();
        this.cityName = (auser.getCity() == null)?"":auser.getCity().getName();
        if (auser.getInterests() != null && auser.getInterests().size() > 0) {
            interestsAsString = auser.getInterests().stream().map(i -> i.getName()).collect(Collectors.joining(","));
        }
    }

    public String getInterestsAsString() {
        return interestsAsString;
    }

    public void setInterestsAsString(String interestsAsString) {
        this.interestsAsString = interestsAsString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Set<String> parsedInterests() {
        Set<String> sres = new HashSet<>();
        if (this.interestsAsString != null) {
            String[] sarr = interestsAsString.split(",");
            for (String ss: sarr) {
                ss = ss.trim();
                if (ss.length() > 0) {
                    sres.add(ss);
                }
            }
        }
        return sres;
    }
}
