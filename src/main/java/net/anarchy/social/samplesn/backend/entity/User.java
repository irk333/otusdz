package net.anarchy.social.samplesn.backend.entity;

import java.util.ArrayList;
import java.util.List;

public class User {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Boolean getAddedToFriends() {
        return addedToFriends;
    }

    public void setAddedToFriends(Boolean addedToFriends) {
        this.addedToFriends = addedToFriends;
    }

    public Boolean getiAmAFriendOf() {
        return iAmAFriendOf;
    }

    public void setiAmAFriendOf(Boolean iAmAFriendOf) {
        this.iAmAFriendOf = iAmAFriendOf;
    }

    public List<User> getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(List<User> friendOf) {
        this.friendOf = friendOf;
    }

    public static enum Gender {
        MALE(1),
        FEMALE(2);

        private int id;

        private Gender(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Gender findById(int aid) {
            for (Gender g: Gender.values()) {
                if (g.id == aid) {
                    return g;
                }
            }
            return null;
        }
    }

    private long id;
    private String email;
    private String userPassword;
    private String lastName;
    private String firstName;
    private int age = 18;
    private Gender gender = Gender.MALE;
    private City city;
    private List<Interest> interests = new ArrayList<>();
    private List<User> friends = new ArrayList<>();
    private List<User> friendOf = new ArrayList<>();
    private Boolean addedToFriends;
    private Boolean iAmAFriendOf;
}
