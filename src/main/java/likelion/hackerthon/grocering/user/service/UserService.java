package likelion.hackerthon.grocering.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import likelion.hackerthon.grocering.user.dto.UserPreferences;
import likelion.hackerthon.grocering.user.entity.User;
import likelion.hackerthon.grocering.user.exceptions.UserNotFoundException;
import likelion.hackerthon.grocering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String saveUser(HttpServletRequest request) {
        User user = new User();
        userRepository.save(user);

        HttpSession session = request.getSession(true);
        String guestId = "guest_" + UUID.randomUUID().toString();
        session.setAttribute("guestId", guestId);
        session.setAttribute("userId", user.getId());

        user.setGuestId(guestId);
        return guestId;
    }

    @Transactional
    public void savePreferences(UserPreferences preferences, Long userId) {
        User user = getUserById(userId);
        user.setPreferences(preferences);
    }

    public void renewSession(HttpServletRequest request, String guestId) {
        User user = getUserByGuestId(guestId);

        HttpSession oldSession = request.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("guestId", user.getGuestId());
        newSession.setAttribute("userId", user.getId());
    }

    private User getUserByGuestId(String guestId) {
        return userRepository.findByGuestId(guestId).orElseThrow(UserNotFoundException::new);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
