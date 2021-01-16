package net.anarchy.social.samplesn.frontend.controllers;

import net.anarchy.social.samplesn.backend.SocialNetworkException;
import net.anarchy.social.samplesn.backend.common.SearchResult;
import net.anarchy.social.samplesn.backend.dao.UserDao;
import net.anarchy.social.samplesn.backend.entity.User;
import net.anarchy.social.samplesn.backend.service.SearchService;
import net.anarchy.social.samplesn.backend.service.UserService;
import net.anarchy.social.samplesn.frontend.form.AnquetteForm;
import net.anarchy.social.samplesn.frontend.form.RegisterForm;
import net.anarchy.social.samplesn.frontend.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    SearchService searchService;

    @Autowired
    UserDao userDao;

    @GetMapping("/error")
    public String error(Model model, HttpServletRequest req, HttpServletResponse resp) {
        return "error";
    }

    @GetMapping("/addToFriends/{friendId}")
    public String addToFriends(@PathVariable Long friendId)  throws SocialNetworkException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        if (isAuthenticated(auth)) {
            User auser = userDao.findByEmail(userName).orElseThrow(() -> new SocialNetworkException(HttpStatus.NOT_FOUND,"user not found"));
            if (friendId == auser.getId()) {
                throw new SocialNetworkException(HttpStatus.INTERNAL_SERVER_ERROR, "you cannot add youself to friends");
            }
            userService.addToFriend(auser,friendId);
        } else {
            throw new SocialNetworkException(HttpStatus.FORBIDDEN, "user is not authenticated");
        }
        return "redirect:/index";
    }

    @GetMapping("/removeFromFriends/{friendId}")
    public String removeFromFriends(@PathVariable Long friendId)  throws SocialNetworkException {
        internalRemoveFromFriends(friendId);
        return "redirect:/index";
    }

    @GetMapping("/removeFromFriendsAnquette/{friendId}")
    public String removeFromFriendsAnquette(@PathVariable Long friendId)  throws SocialNetworkException {
        internalRemoveFromFriends(friendId);
        return "redirect:/secured/anquette";
    }

    private void internalRemoveFromFriends(long friendId) throws SocialNetworkException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        if (isAuthenticated(auth)) {
            User auser = userDao.findByEmail(userName).orElseThrow(() -> new SocialNetworkException(HttpStatus.NOT_FOUND,"user not found"));
            if (friendId == auser.getId()) {
                throw new SocialNetworkException( HttpStatus.INTERNAL_SERVER_ERROR, "you cannot remove youself from friends");
            }
            userService.removeFromFriends(auser,friendId);
        } else {
            throw new SocialNetworkException(HttpStatus.FORBIDDEN, "user is not authenticated");
        }
    }

    @GetMapping({"/index","/"})
    public String index(@ModelAttribute SearchForm searchForm, Model model) throws SocialNetworkException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        model.addAttribute("isAuth", isAuthenticated(auth));
        model.addAttribute("userName", userName);
        if (searchForm == null) {
            searchForm = new SearchForm();
        }
        model.addAttribute("searchForm", searchForm);

        SearchResult<User> res = searchService.find(searchForm.getSearchText(),searchForm.getPageNo(), PAGE_SIZE);
        if (isAuthenticated(auth)) {
            User auser = userDao.findByEmail(userName).orElseThrow(() -> new SocialNetworkException(HttpStatus.NOT_FOUND,"user not found"));

            {
                // mark my friends in founded users
                final List<Long> fids = userDao.findFriendIds(auser.getId(), res.getRecords().stream().map(u -> u.getId()).collect(Collectors.toSet()));
                res.getRecords().forEach(u -> u.setAddedToFriends(fids.contains(u.getId())));
            }

            {
                // mark friend-of  in founded users
                final List<Long> fids = userDao.findFriendOfUserIds(auser.getId(), res.getRecords().stream().map(u -> u.getId()).collect(Collectors.toSet()));
                res.getRecords().forEach(u -> u.setiAmAFriendOf(fids.contains(u.getId())));
            }

            model.addAttribute("authUser", auser);
        }
        model.addAttribute("foundedUsers", res);

        return "index";
    }

    @GetMapping("/view/{userId}")
    public String viewUser(@PathVariable Long userId, Model model) throws SocialNetworkException {
        User viewUser = userService.getById(userId,true,true);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        model.addAttribute("isAuth", isAuthenticated(auth));
        model.addAttribute("userName", userName);
        model.addAttribute("viewUser", viewUser);

        return "view";
    }

    @GetMapping("/secured/anquette")
    public String anquette(Model model) throws SocialNetworkException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User auser = userService.getByEmail(userName, true, true);
        AnquetteForm frm = new AnquetteForm();
        frm.initalize(auser);
        model.addAttribute("anquetteForm", frm);

        model.addAttribute("myFriends",  userDao.findFriends(auser.getId()));
        model.addAttribute("friendsOf", userDao.findFriendOfUsers(auser.getId()));
        return "secured/anquette";
    }

    @PostMapping("/secured/updateuser")
    public String updateAnquette(@ModelAttribute AnquetteForm anquetteForm, Model model) throws SocialNetworkException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User auser = userService.getByEmail(userName, false, false);

        auser = userService.update(auser.getId(), anquetteForm.getLastName(), anquetteForm.getFirstName(),anquetteForm.getAge(),anquetteForm.getGender(),anquetteForm.getCityName(),anquetteForm.parsedInterests());
        return "redirect:/secured/anquette";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("isAuth", isAuthenticated());
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest req) {
        model.addAttribute("isAuth", isAuthenticated());
        String errorCode = req.getParameter("error");
        if ("loginError".equals(errorCode)) {
            model.addAttribute("errorMessage", "Invalid email or password");
        }
        return "login";
    }

    @PostMapping("/doregister")
    public String doRegister(@ModelAttribute RegisterForm registerForm, Model model) throws SocialNetworkException {
        userService.register(registerForm.getEmail(), registerForm.getPassword());
        return "redirect:/login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    private boolean isAuthenticated(Authentication authentication) {
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    private static final int PAGE_SIZE = 10;
}