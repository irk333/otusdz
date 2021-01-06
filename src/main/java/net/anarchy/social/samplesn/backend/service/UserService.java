    package net.anarchy.social.samplesn.backend.service;

import net.anarchy.social.samplesn.backend.SocialNetworkException;
import net.anarchy.social.samplesn.backend.common.SearchResult;
import net.anarchy.social.samplesn.backend.dao.CityDao;
import net.anarchy.social.samplesn.backend.dao.InterestDao;
import net.anarchy.social.samplesn.backend.dao.UserDao;
import net.anarchy.social.samplesn.backend.entity.City;
import net.anarchy.social.samplesn.backend.entity.Interest;
import net.anarchy.social.samplesn.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    CityDao cityDao;

    @Autowired
    InterestDao interestDao;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public void addToFriend(User user, long friendId) {
        userDao.insertFriend(user.getId(),friendId);
    }

    public void removeFromFriends(User user, long friendId) {
        userDao.deleteFriend(user.getId(),friendId);
    }

    public User getByEmail(String email, boolean loadInterests, boolean loadFriends) throws SocialNetworkException {
        User auser = userDao.findByEmail(email).orElseThrow(() -> new SocialNetworkException("user not exists"));
        if (loadInterests) {
            auser.setInterests(userDao.loadInterests(auser.getId()));
        }
        if (loadFriends) {
            auser.setFriends(userDao.loadFriends(auser.getId()));
        }
        return auser;
    }

    public User getById(long id, boolean loadInterests, boolean loadFriends) throws SocialNetworkException {
        User auser = userDao.findById(id).orElseThrow(() -> new SocialNetworkException("user not exists"));
        if (loadInterests) {
            auser.setInterests(userDao.loadInterests(auser.getId()));
        }
        if (loadFriends) {
            auser.setFriends(userDao.loadFriends(auser.getId()));
        }
        return auser;
    }

    @Transactional
    public User register(@NonNull String email, @NonNull String password) throws SocialNetworkException {
        if (userDao.findByEmail(email).isPresent() ) {
            throw new SocialNetworkException("User already registered");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setLastName("New");
        newUser.setFirstName("User");
        newUser.setAge(18);
        newUser.setGender(User.Gender.MALE);
        newUser.setCity(null);

        long userId = userDao.insert(newUser, passwordEncoder.encode(password) );

        return userDao.findById(userId).get();
    }

    @Transactional
    public User update(long id, @NonNull String lastName, @NonNull String firstName, int age, int gender, String cityName, Set<String> interests)  throws SocialNetworkException {
        if (age < 5 || age > 120) {
            throw new SocialNetworkException("invalid age");
        }
        User.Gender uGender = User.Gender.findById(gender);
        if (uGender == null) {
            throw new SocialNetworkException("invalid gender");
        }

        User aUser = userDao.findById(id).orElseThrow(() -> new SocialNetworkException("User not exists"));

        // city
        final String cityNamePrepared = cityName.trim();
        City city = cityDao
            .findByNameExact(cityName)
            .orElseGet(() -> {
                long cityId = cityDao.insert(cityNamePrepared);
                return cityDao.findById(cityId).get();
            });

        // interests
        Set<String> interestNames = interests.stream().map(s -> s.trim().toLowerCase()).collect(Collectors.toSet());
        List<Interest> lstEx = interestDao.findByNames(interestNames);
        Set<Long> interestIds = lstEx.stream().map(i -> i.getId()).collect(Collectors.toSet());
        for (String interestName: interestNames) {
            if (lstEx.stream().filter(i -> i.getName().equals(interestName)).findAny().isPresent() ) {
                continue;
            }

            long interestId = interestDao.insert(interestName);
            interestIds.add(interestId);
        }
        userDao.updateInterests(id, interestIds);

        aUser.setLastName(lastName);
        aUser.setFirstName(firstName);
        aUser.setAge(age);
        aUser.setGender(uGender);
        aUser.setCity(city);

        aUser = userDao.update(aUser);
        return aUser;
    }

    public void addFriend(long userId, long friendUserId) throws SocialNetworkException {
        try {
            userDao.insertFriend(userId, friendUserId);
        } catch (Exception ex) {
            throw new SocialNetworkException("friend does not added");
        }
    }
}
