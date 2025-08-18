package likelion.hackerthon.grocering.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import likelion.hackerthon.grocering.user.entity.User;
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
}
